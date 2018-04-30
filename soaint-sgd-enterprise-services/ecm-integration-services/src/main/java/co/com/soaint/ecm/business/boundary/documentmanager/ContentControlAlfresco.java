package co.com.soaint.ecm.business.boundary.documentmanager;

import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Configuracion;
import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Utilities;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentControl;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.IRecordServices;
import co.com.soaint.ecm.domain.entity.AccionUsuario;
import co.com.soaint.ecm.domain.entity.Carpeta;
import co.com.soaint.ecm.domain.entity.Conexion;
import co.com.soaint.ecm.util.SystemParameters;
import co.com.soaint.foundation.canonical.ecm.*;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.runtime.ObjectIdImpl;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.UnfileObject;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisBaseException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConstraintException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisContentAlreadyExistsException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by Dasiel
 */
@BusinessControl
public class ContentControlAlfresco implements ContentControl {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LogManager.getLogger(ContentControlAlfresco.class.getName());
/*
    @Value("${claseSubserie}")
    private static String aclaseSubserie;*/

    @Autowired
    private IRecordServices recordServices;

    @Autowired
    private Configuracion configuracion;


    /**
     * Convierte contentStream a File
     *
     * @param contentStream contentStream
     * @return Un objeto File
     * @throws IOException En caso de error
     */
    private static File convertInputStreamToFile(ContentStream contentStream) throws IOException {

        File file = File.createTempFile(contentStream.getFileName(), "");

        try (InputStream inputStream = contentStream.getStream(); OutputStream out = new FileOutputStream(file)) {

            int read;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

        } catch (IOException e) {
            logger.error("Error al convertir el contentStream a File", e);
        }

        file.deleteOnExit();
        return file;
    }

    /**
     * Metodo que obtiene el objeto de conexion que produce Alfresco en conexion
     *
     * @return Objeto de tipo Conexion en este caso devuevle un objeto Session
     */
    @Override
    public Conexion obtenerConexion() {
        Conexion conexion = new Conexion();

        logger.info("*** obtenerConexion ***");

        try {
            Map<String, String> parameter = new HashMap<>();

            // Credenciales del usuario
            parameter.put(SessionParameter.USER, SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_USER));
            parameter.put(SessionParameter.PASSWORD, SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_PASS));

            // configuracion de conexion
            parameter.put(SessionParameter.ATOMPUB_URL, SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_ENDPOINT));
            parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
            parameter.put(SessionParameter.REPOSITORY_ID, configuracion.getPropiedad("REPOSITORY_ID"));

            // Object factory de Alfresco
            parameter.put(SessionParameter.OBJECT_FACTORY_CLASS, "org.alfresco.cmis.client.impl.AlfrescoObjectFactoryImpl");

            // Crear Sesion
            SessionFactory factory = SessionFactoryImpl.newInstance();
            conexion.setSession(factory.getRepositories(parameter).get(0).createSession());

        } catch (Exception e) {
            logger.error("*** Error al obtener conexion *** ", e);
        }

        return conexion;
    }

    /**
     * Metodo que crea carpetas dentro de Alfresco
     *
     * @param folder          Objeto carpeta que contiene un Folder de Alfresco
     * @param nameOrg         Nombre de la carpeta
     * @param codOrg          Codigo de la carpeta que se va a crear
     * @param classDocumental Clase documental que tiene la carpeta que se va a crar.
     * @param folderFather    Carpeta dentro de la cual se va a crear la carpeta
     * @return Devuelve la carpeta creada dentro del objeto Carpeta
     */
    private Carpeta crearCarpeta(Carpeta folder, String nameOrg, String codOrg,
                                 String classDocumental, Carpeta folderFather, String idOrgOfc) {
        Carpeta newFolder = null;
        try {

            logger.info("### Creando Carpeta.. con clase documental:" + classDocumental);
            Map<String, Object> props = new HashMap<>();
            //Se define como nombre de la carpeta nameOrg
            props.put(PropertyIds.NAME, nameOrg);

            //Se estable como codigo unidad administrativa Padre el codigo de la carpeta padre
            switch (classDocumental) {
                case CLASE_BASE:
                    llenarPropiedadesCarpeta(CLASE_BASE, props, codOrg);
                    break;
                case CLASE_DEPENDENCIA:
                    llenarPropiedadesCarpeta(CMCOR_DEP_CODIGO, CLASE_DEPENDENCIA, props, codOrg, folderFather, idOrgOfc);
                    break;
                case CLASE_SERIE:
                    llenarPropiedadesCarpeta(CMCOR_SER_CODIGO, CLASE_SERIE, props, codOrg, folderFather, idOrgOfc);
                    break;
                case CLASE_SUBSERIE:
                    llenarPropiedadesCarpeta(CMCOR_SS_CODIGO, CLASE_SUBSERIE, props, codOrg, folderFather, idOrgOfc);
                    break;
                case CLASE_UNIDAD_DOCUMENTAL:

                    final Folder tmpFolder = folderFather.getFolder();

                    final String depCodeValue = tmpFolder.getPropertyValue(CMCOR_DEP_CODIGO);
                    String depCode = !ObjectUtils.isEmpty(depCodeValue) ? depCodeValue : "";

                    final String serieCodeValue = tmpFolder.getPropertyValue(CMCOR_SER_CODIGO);
                    String serieCode = !ObjectUtils.isEmpty(serieCodeValue) ? serieCodeValue : "";

                    final String subSerieCodeValue = tmpFolder.getPropertyValue(CMCOR_SS_CODIGO);
                    String subSerieCode = !ObjectUtils.isEmpty(subSerieCodeValue) ? subSerieCodeValue : "";

                    final String codUnidadAdminPadreValue = tmpFolder.getPropertyValue(CMCOR_DEP_CODIGO_UAP);
                    String codUnidadAdminPadre = !ObjectUtils.isEmpty(codUnidadAdminPadreValue) ? codUnidadAdminPadreValue : "";

                    props.put(CMCOR_UD_ID, "");
                    props.put(CMCOR_UD_ACCION, "");
                    props.put(CMCOR_UD_DESCRIPTOR_2, "");
                    props.put(CMCOR_UD_CERRADA, false);
                    props.put(CMCOR_UD_SOPORTE, "");
                    props.put(CMCOR_UD_INACTIVO, false);
                    props.put(CMCOR_UD_UBICACION_TOPOGRAFICA, "");
                    props.put(CMCOR_UD_FASE_ARCHIVO, "");
                    props.put(CMCOR_UD_DESCRIPTOR_1, "");
                    props.put(CMCOR_UD_CODIGO, "");
                    props.put(CMCOR_DEP_CODIGO_UAP, !"".equals(subSerieCode) ? subSerieCode :
                            !"".equals(serieCode) ? serieCode :
                                    !"".equals(depCode) ? depCode : codUnidadAdminPadre);
                    props.put(CMCOR_DEP_CODIGO, depCode);
                    props.put(CMCOR_SER_CODIGO, serieCode);
                    props.put(CMCOR_SS_CODIGO, subSerieCode);
                    props.put(PropertyIds.DESCRIPTION, configuracion.getPropiedad(CLASE_UNIDAD_DOCUMENTAL));
                    props.put(PropertyIds.OBJECT_TYPE_ID, "F:cmcor:" + configuracion.getPropiedad(CLASE_UNIDAD_DOCUMENTAL));
                    break;
                default:
                    break;
            }
            //Se crea la carpeta dentro de la carpeta folder
            logger.info("*** Se procede a crear la carpeta ***");
            newFolder = new Carpeta();
            newFolder.setFolder(folder.getFolder().createFolder(props));
            logger.info("---------------------Carpeta: " + newFolder.getFolder().getName() + " creada--------------");
        } catch (Exception e) {
            logger.error("*** Error al crear carpeta *** ", e);
        }
        return newFolder;
    }

    /**
     * @param informationArray Arreglo que trae el nombre de la carpeta para formatearlo para ser usado por el ECM
     * @param formatoConfig    Contiene el formato que se le dara al nombre
     * @return Nombre formateado
     */
    private String formatearNombre(String[] informationArray, String formatoConfig) {
        String formatoCadena;
        StringBuilder formatoFinal = new StringBuilder();
        try {
            formatoCadena = configuracion.getPropiedad(formatoConfig);
            String[] formatoCadenaArray = formatoCadena.split("");
            for (String aFormatoCadenaArray : formatoCadenaArray) {

                String nomSerie = "3";
                String idOrgAdm = "0";
                String idOrgOfc = "1";
                String codSerie = "2";
                String codSubserie = "4";
                String nomSubserie = "5";
                if (aFormatoCadenaArray.equals(idOrgAdm)) {
                    formatoFinal.append(informationArray[Integer.parseInt(idOrgAdm)]);
                } else if (aFormatoCadenaArray.equals(idOrgOfc)) {
                    formatoFinal.append(informationArray[Integer.parseInt(idOrgOfc)]);
                } else if (aFormatoCadenaArray.equals(codSerie)) {
                    formatoFinal.append(informationArray[Integer.parseInt(codSerie)]);
                } else if (aFormatoCadenaArray.equals(nomSerie)) {
                    formatoFinal.append(informationArray[Integer.parseInt(nomSerie)]);
                } else if (aFormatoCadenaArray.equals(codSubserie)) {
                    formatoFinal.append(informationArray[Integer.parseInt(codSubserie)]);
                } else if (aFormatoCadenaArray.equals(nomSubserie)) {
                    formatoFinal.append(informationArray[Integer.parseInt(nomSubserie)]);
                } else if (isNumeric(aFormatoCadenaArray)) {
                    //El formato no cumple con los requerimientos minimos
                    logger.info("El formato no cumple con los requerimientos.");
                    formatoFinal = new StringBuilder();
                    break;
                } else {
                    formatoFinal.append(aFormatoCadenaArray);
                }
            }
        } catch (Exception e) {
            logger.error("*** Error al formatear nombre *** ", e);
        }
        return !ObjectUtils.isEmpty(formatoFinal) ? formatoFinal.toString() : "";
    }

    /**
     * Metodo para devolver documento para su visualización
     *
     * @param documentoDTO Objeto que contiene los metadatos del documento dentro del ECM
     * @param session      Objeto de conexion
     * @return Objeto de tipo response que devuleve el documento
     */
    @Override
    public MensajeRespuesta descargarDocumento(DocumentoDTO documentoDTO, Session session) {
        logger.info(documentoDTO.toString());
        ArrayList<DocumentoDTO> versionesLista = new ArrayList<>();
        ArrayList<DocumentoDTO> documento = new ArrayList<>();
        DocumentoDTO documentoDTO1 = new DocumentoDTO();
        MensajeRespuesta mensajeRespuesta = new MensajeRespuesta();
        try {
            logger.info("Se entra al metodo de descargar el documento");
            Document doc = (Document) session.getObject(documentoDTO.getIdDocumento());
            File file;

            if (documentoDTO.getVersionLabel() != null) {
                List<Document> versions = doc.getAllVersions();
                //Filtrar la version correcta dentro de las versiones del documento para obtener el file
                Optional<Document> version = versions.stream()
                        .filter(p -> p.getVersionLabel().equals(documentoDTO.getVersionLabel())).findFirst();

                file = version.isPresent() ? getFile(documentoDTO, versionesLista, version.get()) : null;

            } else {
                file = convertInputStreamToFile(doc.getContentStream());
            }
            logger.info("Se procede a devolver el documento" + documentoDTO.getNombreDocumento());
            byte[] data = FileUtils.readFileToByteArray(file);
            documentoDTO1.setDocumento(data);

            mensajeRespuesta.setCodMensaje("0000");
            mensajeRespuesta.setMensaje("Documento Retornado con exito");
            documento.add(documentoDTO1);
            mensajeRespuesta.setDocumentoDTOList(documento);

        } catch (Exception e) {
            logger.error("Error en la obtención del documento: ", e);
            mensajeRespuesta.setCodMensaje("2222");
            mensajeRespuesta.setMensaje(ECM_ERROR);

        }
        return mensajeRespuesta;
    }

    /**
     * Metodo para retornar el archivo
     *
     * @param documentoDTO   Objeto que contiene los metadatos
     * @param versionesLista Listado por el que se va a buscar
     * @param version        Version del documento que se esta buscando
     * @return Objeto file
     * @throws IOException Tipo de excepcion
     */
    private File getFile(DocumentoDTO documentoDTO, ArrayList<DocumentoDTO> versionesLista, Document version) throws IOException {
        File fileAux = null;
        if (version.getVersionLabel().equals(documentoDTO.getVersionLabel())) {
            documentoDTO.setNombreDocumento(version.getName());
            versionesLista.add(documentoDTO);
            fileAux = convertInputStreamToFile(version.getContentStream());
        }
        return fileAux;
    }

    /**
     * Metodo que retorna true en caso de que la cadena que se le pasa es numerica y false si no.
     *
     * @param cadena Cadena de texto que se le pasa al metodo
     * @return Retorna true o false
     */
    private boolean isNumeric(String cadena) {
        return cadena.matches("[+-]?\\d*(\\.\\d+)?") && "".equals(cadena) == Boolean.FALSE;
    }

    /**
     * Metodo que obtiene la carpeta dado el nombre
     *
     * @param nombreCarpeta NOmbre de la carpeta que se va a buscar
     * @param session       objeto de conexion al Alfresco
     * @return Retorna la Carpeta que se busca
     */
    private Carpeta obtenerCarpetaPorNombre(String nombreCarpeta, Session session) {
        Carpeta folder = new Carpeta();
        try {
            String queryString = "SELECT " + PropertyIds.OBJECT_ID + " FROM cmis:folder" +
                    " WHERE " + PropertyIds.NAME + " = '" + nombreCarpeta + "'" +
                    " and (cmis:objectTypeId = 'F:cmcor:CM_Unidad_Base'" +
                    " or cmis:objectTypeId = 'F:cmcor:CM_Serie'" +
                    " or cmis:objectTypeId = 'F:cmcor:CM_Subserie'" +
                    " or cmis:objectTypeId = 'F:cmcor:CM_Unidad_Administrativa'" +
                    " or cmis:objectTypeId = 'F:cmcor:CM_Unidad_Documental')";
            ItemIterable<QueryResult> results = session.query(queryString, false);
            for (QueryResult qResult : results) {
                String objectId = qResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
                folder.setFolder((Folder) session.getObject(session.createObjectId(objectId)));
            }
        } catch (Exception e) {
            logger.error("*** Error al obtenerCarpetas *** ", e);
        }
        return folder;
    }

    /**
     * Servicio que devuelve el listado de las Series y de las Dependencias
     *
     * @param dependenciaTrdDTO Objeto dependencia que contiene los datos necesarios para realizar la busqueda
     * @param session           Objeto de conexion
     * @return Objeto de dependencia que contiene las sedes o las dependencias buscadas
     */
    @Override
    public MensajeRespuesta devolverSerieSubSerie(ContenidoDependenciaTrdDTO dependenciaTrdDTO, Session session) throws Exception {
        MensajeRespuesta respuesta = new MensajeRespuesta();
        try {
            if (StringUtils.isEmpty(dependenciaTrdDTO.getIdOrgOfc())) {
                logger.error("Oopss... No se ha especificado el codigo de la dependencia");
                throw new BusinessException("No se ha especificado el codigo de la dependencia");
            }
            String queryString = "SELECT * FROM cmcor:CM_Serie WHERE " + CMCOR_DEP_CODIGO + " LIKE '" + dependenciaTrdDTO.getIdOrgOfc() + "%'";
            queryString += (!StringUtils.isEmpty(dependenciaTrdDTO.getCodSerie())) ?
                    " AND " + CMCOR_SER_CODIGO + " LIKE '" + dependenciaTrdDTO.getCodSerie() + "%'" : "";
            ItemIterable<QueryResult> results = session.query(queryString, false);
            if (results.getPageNumItems() > 0) {
                List<SerieDTO> serieLista = new ArrayList<>();
                List<SubSerieDTO> subSerieLista = new ArrayList<>();
                for (QueryResult qResult : results) {
                    String objectId = qResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
                    Folder folder = (Folder) session.getObject(session.createObjectId(objectId));
                    if ("F:cmcor:CM_Subserie".equals(folder.getType().getId())) {
                        SubSerieDTO subSerie = new SubSerieDTO();
                        subSerie.setCodigoSubSerie(folder.getPropertyValue(CMCOR_SS_CODIGO) + "");
                        subSerie.setNombreSubSerie(folder.getName());
                        subSerieLista.add(subSerieLista.size(), subSerie);
                    } else if ("F:cmcor:CM_Serie".equals(folder.getType().getId())) {
                        SerieDTO serieDTO = new SerieDTO();
                        serieDTO.setCodigoSerie(folder.getPropertyValue(CMCOR_SER_CODIGO) + "");
                        serieDTO.setNombreSerie(folder.getName());
                        serieLista.add(serieLista.size(), serieDTO);
                    }
                }
                dependenciaTrdDTO.setListaSerie(serieLista);
                dependenciaTrdDTO.setListaSubSerie(subSerieLista);
                respuesta.setCodMensaje("0000");
                respuesta.setMensaje("Series o Subseries devueltas correctamente");
                List<ContenidoDependenciaTrdDTO> listaSerieSubSerie = new ArrayList<>();
                listaSerieSubSerie.add(listaSerieSubSerie.size(), dependenciaTrdDTO);
                respuesta.setContenidoDependenciaTrdDTOS(listaSerieSubSerie);
            } else {
                respuesta.setCodMensaje("1111");
                respuesta.setMensaje("No se obtuvieron Series o Subseries por no existir para esta dependencia");
            }
        } catch (Exception e) {
            logger.error("*** Error al obtener las series o subseries *** ", e);
            throw e;
        }
        return respuesta;
    }

    /**
     * Metodo que crea las unidades documentales del ECM
     *
     * @param unidadDocumentalDTO Objeto dependencia que contiene los datos necesarios para realizar la busqueda
     * @param session             Objeto de conexion
     * @return MensajeRespuesta
     */
    @Override
    public MensajeRespuesta crearUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO, Session session) throws BusinessException {
        logger.info("Executing crearUnidadDocumental method");

        final MensajeRespuesta response = new MensajeRespuesta();
        Folder udFolder = getUDFolderById(unidadDocumentalDTO.getId(), session);
        if (!ObjectUtils.isEmpty(udFolder)) {
            logger.error("Ya existe una unidad documental con el id {}", unidadDocumentalDTO.getId());
            throw new BusinessException("Ya existe una unidad documental con el id " + unidadDocumentalDTO.getId());
        }

        try {
            final String dependenciaCode = unidadDocumentalDTO.getCodigoDependencia();

            if (ObjectUtils.isEmpty(dependenciaCode)) {
                logger.error("La Unidad Documental {} no contiene el codigo de la Dependencia", unidadDocumentalDTO);
                response.setMensaje("La Unidad Documental no contiene el codigo de la Dependencia");
                response.setCodMensaje("22223");
                return response;
            }

            final String nombreUnidadDocumental = unidadDocumentalDTO.getNombreUnidadDocumental();

            if (ObjectUtils.isEmpty(nombreUnidadDocumental)) {
                logger.error("La Unidad Documental no contiene Nombre");
                response.setMensaje("La Unidad Documental no contiene Nombre");
                response.setCodMensaje("22223");
                return response;
            }

            String query = "SELECT * FROM " + CMCOR + "" + configuracion.getPropiedad(CLASE_DEPENDENCIA) +
                    " WHERE " + CMCOR_DEP_CODIGO + " = '" + dependenciaCode + "'" +
                    " AND " + PropertyIds.OBJECT_TYPE_ID + " = 'F:" + CMCOR + "" + configuracion.getPropiedad(CLASE_DEPENDENCIA) + "'";

            final String codigoSerie = unidadDocumentalDTO.getCodigoSerie();
            final String codigoSubSerie = unidadDocumentalDTO.getCodigoSubSerie();

            ItemIterable<QueryResult> queryResults;

            boolean flag = false;

            if (ObjectUtils.isEmpty(codigoSerie) && ObjectUtils.isEmpty(codigoSubSerie)) {

                queryResults = session.query(query, false);

                if (queryResults.getPageNumItems() == 0) {
                    logger.error("No existe la dependencia {} en el ECM", dependenciaCode);
                    response.setCodMensaje("1111");
                    response.setMensaje("No existe la dependencia '" + dependenciaCode + "' en el ECM");
                    return response;
                }
                flag = true;
            }
            if (!flag && !ObjectUtils.isEmpty(codigoSerie) && ObjectUtils.isEmpty(codigoSubSerie)) {

                query = "SELECT * FROM " +
                        CMCOR + "" + configuracion.getPropiedad(CLASE_SERIE) + " " +
                        "WHERE " + CMCOR_DEP_CODIGO + " = '" + dependenciaCode + "' " +
                        "AND " + PropertyIds.OBJECT_TYPE_ID + " = 'F:" + CMCOR + "" + configuracion.getPropiedad(CLASE_SERIE) + "' " +
                        "AND " + CMCOR_SER_CODIGO + " = '" + codigoSerie + "'";
                flag = true;
            }
            if (!flag && !ObjectUtils.isEmpty(codigoSerie) && !ObjectUtils.isEmpty(codigoSubSerie)) {

                query = "SELECT * FROM " +
                        CMCOR + "" + configuracion.getPropiedad(CLASE_SUBSERIE) + " " +
                        "WHERE " + CMCOR_DEP_CODIGO + " = '" + dependenciaCode + "' " +
                        "AND " + PropertyIds.OBJECT_TYPE_ID + " = 'F:" + CMCOR + "" + configuracion.getPropiedad(CLASE_SUBSERIE) + "' " +
                        "AND " + CMCOR_SER_CODIGO + " = '" + codigoSerie + "' " +
                        "AND " + CMCOR_SS_CODIGO + " = '" + codigoSubSerie + "'";
                flag = true;
            }

            if (!flag) {
                response.setCodMensaje("111111");
                response.setMensaje("Se debe especificar el codigo de la serie cuando se especifica el de la sub serie");
                return response;
            }

            queryResults = session.query(query, false);
            final Iterator<QueryResult> iterator = queryResults.iterator();
            if (iterator.hasNext()) {

                QueryResult queryResult = iterator.next();
                String objectId = queryResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
                Folder folder = (Folder) session.getObject(session.getObject(objectId));

                final ItemIterable<CmisObject> children = folder.getChildren();

                for (CmisObject cmisObject :
                        children) {
                    String udName = Utilities.reemplazarCaracteresRaros(nombreUnidadDocumental);
                    String folderName = Utilities.reemplazarCaracteresRaros(cmisObject.getName());
                    if (cmisObject instanceof Folder && udName.equals(folderName)) {
                        logger.error("Ya existe una Carpeta con el nombre {}", nombreUnidadDocumental);
                        response.setCodMensaje("1111");
                        response.setMensaje("Ya existe una Carpeta con el nombre " + nombreUnidadDocumental + " en el ECM");
                        return response;
                    }
                }

                final Map<String, Object> props = new HashMap<>();
                props.put(PropertyIds.OBJECT_TYPE_ID, "F:cmcor:" + configuracion.getPropiedad(CLASE_UNIDAD_DOCUMENTAL));
                props.putAll(updateProperties(null, unidadDocumentalDTO));

                logger.info("Making the folder!!!");
                folder.createFolder(props);
                logger.info("Make success");

                response.setCodMensaje("0000");
                response.setMensaje("Unidad Documental creada con exito");

                final Map<String, Object> responsMap = new HashMap<>();

                responsMap.put("unidadDocumental", unidadDocumentalDTO);
                response.setResponse(responsMap);

                return response;
            }
            logger.error("Ningun resultado coincide con el criterio de busqueda");
            response.setCodMensaje("2222");
            response.setMensaje("Ningun resultado coincide con el criterio de busqueda");
            return response;
        } catch (Exception ex) {
            logger.error("Ocurrio un error al crear la Unidad Documental");
            throw new BusinessException("Ocurrio un error al crear la Unidad Documental");
        }
    }

    public MensajeRespuesta eliminarUnidadDocumental(String id,Session session) throws BusinessException {
        MensajeRespuesta mensajeRespuesta=new MensajeRespuesta();
        Folder udFolderById = getUDFolderById(id, session);
        if (udFolderById != null)
        udFolderById.deleteTree(true, UnfileObject.DELETE, true);
        return mensajeRespuesta;
    }

    /**
     * Listar las Unidades Documentales del ECM
     *
     * @return Mensaje de respuesta
     */
    @Override
    public MensajeRespuesta listarUnidadDocumental(final UnidadDocumentalDTO dto,
                                                   final Session session) throws BusinessException {
        final MensajeRespuesta respuesta = new MensajeRespuesta();
        final List<UnidadDocumentalDTO> unidadDocumentalDTOS = buscarUnidadesDocumentales(dto, session, false);
        respuesta.setMensaje("Listado seleccionado correctamente");
        respuesta.setCodMensaje("0000");
        Map<String, Object> map = new HashMap<>();
        map.put("unidadDocumental", unidadDocumentalDTOS);
        respuesta.setResponse(map);
        return respuesta;
    }

    private List<UnidadDocumentalDTO> buscarUnidadesDocumentales(UnidadDocumentalDTO dto, Session session, boolean all) throws BusinessException {
        try {
            List<UnidadDocumentalDTO> unidadDocumentalDTOS;
            if (!ObjectUtils.isEmpty(dto.getAccion())) {
                AccionUsuario accionUsuario = AccionUsuario.valueOf(dto.getAccion().toUpperCase());
                unidadDocumentalDTOS = listarUnidadDocumental(accionUsuario, session);
            } else {
                String query = "SELECT * FROM " + CMCOR + configuracion.getPropiedad(CLASE_UNIDAD_DOCUMENTAL);
                boolean where = false;

                if (!all) {
                    query += " WHERE " + CMCOR_UD_ID + " IS NOT NULL";
                    where = true;
                }
                if (!ObjectUtils.isEmpty(dto.getCodigoUnidadDocumental())) {
                    query += (!where ? " WHERE " : " AND ") + CMCOR_UD_CODIGO + " = '" + dto.getCodigoUnidadDocumental() + "'";
                    where = true;
                }
                if (!ObjectUtils.isEmpty(dto.getDescriptor2())) {
                    query += (!where ? " WHERE " : " AND ") + CMCOR_UD_DESCRIPTOR_2 + " LIKE '%" + dto.getDescriptor2() + "%'";
                    where = true;
                }
                if (!ObjectUtils.isEmpty(dto.getSoporte())) {
                    query += (!where ? " WHERE " : " AND ") + CMCOR_UD_SOPORTE + " = '" + dto.getSoporte() + "'";
                    where = true;
                }
                if (!ObjectUtils.isEmpty(dto.getInactivo())) {
                    query += (!where ? " WHERE " : " AND ") + CMCOR_UD_INACTIVO + " = '" + dto.getInactivo() + "'";
                    where = true;
                }
                if (!ObjectUtils.isEmpty(dto.getDescriptor1())) {
                    query += (!where ? " WHERE " : " AND ") + CMCOR_UD_DESCRIPTOR_1 + " LIKE '%" + dto.getDescriptor1() + "%'";
                    where = true;
                }
                if (!ObjectUtils.isEmpty(dto.getCerrada())) {
                    query += (!where ? " WHERE " : " AND ") + CMCOR_UD_CERRADA + " = '" + dto.getCerrada() + "'";
                    where = true;
                }
                if (!ObjectUtils.isEmpty(dto.getCodigoSerie())) {
                    query += (!where ? " WHERE " : " AND ") + CMCOR_SER_CODIGO + " = '" + dto.getCodigoSerie() + "'";
                    where = true;
                }
                if (!ObjectUtils.isEmpty(dto.getCodigoSubSerie())) {
                    query += (!where ? " WHERE " : " AND ") + CMCOR_SS_CODIGO + " = '" + dto.getCodigoSubSerie() + "'";
                    where = true;
                }
                if (!ObjectUtils.isEmpty(dto.getNombreUnidadDocumental())) {
                    query += (!where ? " WHERE " : " AND ") + PropertyIds.NAME + " LIKE '%" + dto.getNombreUnidadDocumental() + "%'";
                    where = true;
                }
                if (!ObjectUtils.isEmpty(dto.getCodigoDependencia())) {
                    query += (!where ? " WHERE " : " AND ") + CMCOR_DEP_CODIGO + " = '" + dto.getCodigoDependencia() + "'";
                }
                if (!ObjectUtils.isEmpty(dto.getEstado())) {
                    query += (!where ? " WHERE " : " AND ") + CMCOR_UD_ESTADO + " = '" + dto.getEstado() + "'";
                }
                if (!ObjectUtils.isEmpty(dto.getDisposicion())) {
                    query += (!where ? " WHERE " : " AND ") + CMCOR_UD_DISPOSICION + " = '" + dto.getDisposicion() + "'";
                }
                unidadDocumentalDTOS = listarUnidadDocumental(query, dto, session);
            }

            return unidadDocumentalDTOS;

        } catch (Exception e) {
            logger.error("Error al Listar las Unidades Documentales");
            throw new BusinessException("Error al Listar las Unidades Documentales");
        }
    }

    private List<UnidadDocumentalDTO> listarUnidadDocumental(String query, UnidadDocumentalDTO dto, Session session) {
        logger.info("Executing query {}", query);
        final ItemIterable<QueryResult> queryResults = session.query(query, false);
        final List<UnidadDocumentalDTO> unidadDocumentalDTOS = new ArrayList<>();
        for (QueryResult queryResult :
                queryResults) {
            final String objectId = queryResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
            final Folder folder = (Folder) session.getObject(session.getObject(objectId));
            if (ObjectUtils.isEmpty(dto) || areDatesInRange(folder, dto)) {
                unidadDocumentalDTOS.add(unidadDocumentalDTOS.size(),
                        transformarUnidadDocumental(folder));
            }
        }
        return unidadDocumentalDTOS;
    }

    /**
     * Listar las Unidades Documentales del ECM
     *
     * @param accionUsuario Resultado se muestra segun la accion a realizar
     * @return List<UnidadDocumentalDTO> Lst
     */
    private List<UnidadDocumentalDTO> listarUnidadDocumental(AccionUsuario accionUsuario, Session session) throws BusinessException {
        try {
            String query = "SELECT * FROM " + CMCOR + configuracion.getPropiedad(CLASE_UNIDAD_DOCUMENTAL) +
                    " WHERE " + CMCOR_UD_FECHA_INICIAL + " IS NOT NULL AND " + CMCOR_UD_SOPORTE + " IS NOT NULL";
            switch (accionUsuario) {
                case ABRIR:
                case REACTIVAR:
                    query += " AND " + CMCOR_UD_CERRADA + " = 'true' AND " + CMCOR_UD_FECHA_FINAL + " IS NOT NULL" +
                            " AND " + CMCOR_UD_FECHA_CIERRE + " IS NOT NULL AND " + CMCOR_UD_INACTIVO + " = 'true'";
                    break;
                case CERRAR:
                    query += " AND " + CMCOR_UD_CERRADA + " = 'false' AND " + CMCOR_UD_INACTIVO + " = 'false'";
                    break;
            }
            return listarUnidadDocumental(query, null, session);
        } catch (Exception e) {
            throw new BusinessException("ECM ERROR: " + e.getMessage());
        }
    }

    /**
     * Metodo para listar los documentos de una Unidad Documental
     *
     * @param idDocumento Id Documento
     * @param session     Objeto conexion de Alfresco
     * @return MensajeRespuesta con los detalles del documento
     */
    @Override
    public MensajeRespuesta obtenerDetallesDocumentoDTO(String idDocumento, Session session) throws BusinessException {
        logger.info("Ejecutando el metodo MensajeRespuesta obtenerDetallesDocumentoDTO(String idDocumento, Session session)");

        final MensajeRespuesta mensajeRespuesta = new MensajeRespuesta();

        if (ObjectUtils.isEmpty(idDocumento)) {
            logger.info("El ID del documento esta vacio");
            mensajeRespuesta.setMensaje("El ID del documento esta vacio");
            mensajeRespuesta.setCodMensaje("11111");
            return mensajeRespuesta;
        }

        try {
            CmisObject cmisObjectDocument = session.getObject(session.getObject(idDocumento));

            if (!(cmisObjectDocument instanceof Document)) {
                logger.info("Ningun resultado coincide con el criterio de busqueda");
                mensajeRespuesta.setMensaje("Ningun resultado coincide con el criterio de busqueda");
                mensajeRespuesta.setCodMensaje("11111");
                return mensajeRespuesta;
            }

            Map<String, Object> mapResponsonse = new HashMap<>();
            mapResponsonse.put("documentoDTO", transformarDocumento((Document) cmisObjectDocument));
            mensajeRespuesta.setMensaje("Documento devuelto correctamente");
            mensajeRespuesta.setCodMensaje("0000");
            mensajeRespuesta.setResponse(mapResponsonse);

            return mensajeRespuesta;
        } catch (CmisObjectNotFoundException ex) {
            logger.error("Ningun resultado coincide con el ID: {}", idDocumento);
            throw new BusinessException("Ningun resultado coincide con el Id dado");
        }
    }

    /**
     * Metodo para listar los documentos de una Unidad Documental
     *
     * @param idUnidadDocumental Id de la unidad documental
     * @param session            Objeto conexion de Alfresco
     * @return MensajeRespuesta con los documentos dentro del dto Unidad Documental
     */
    @Override
    public MensajeRespuesta listaDocumentosDTOUnidadDocumental(String idUnidadDocumental, Session session) throws BusinessException {
        logger.info("Ejecutando el metodo MensajeRespuesta listaDocumentosDTOUnidadDocumental(String idUnidadDocumental, Session session)");

        final MensajeRespuesta mensajeRespuesta = new MensajeRespuesta();
        {
            mensajeRespuesta.setResponse(new HashMap<>());
        }

        UnidadDocumentalDTO dto = listarDocsDadoIdUD(idUnidadDocumental, session);

        logger.info("Documentos devuelto satisfactoriamente {}", dto);
        mensajeRespuesta.setCodMensaje("0000");
        mensajeRespuesta.setMensaje("OK");
        mensajeRespuesta.getResponse().put("unidadDocumentalDTO", dto);
        return mensajeRespuesta;
    }

    /**
     * Metodo para devolver la Unidad Documental
     *
     * @param idUnidadDocumental Id Unidad Documental
     * @return MensajeRespuesta      Unidad Documntal
     */
    @Override
    public MensajeRespuesta detallesUnidadDocumental(String idUnidadDocumental, Session session) throws BusinessException {
        logger.info("Ejecutando el metodo detallesUnidadDocumental(String idUnidadDocumental, Session session)");

        UnidadDocumentalDTO dto = findUDById(idUnidadDocumental, session);
        MensajeRespuesta respuesta = new MensajeRespuesta();

        respuesta.setCodMensaje("0000");
        respuesta.setMensaje("Detalles Unidad Documental");
        Map<String, Object> map = new HashMap<>();
        map.put("unidadDocumental", dto);
        respuesta.setResponse(map);

        return respuesta;
    }

    /**
     * Metodo que busca una Unidad Documental en el ECM
     *
     * @param idUnidadDocumental Id de la Unidad Documental
     * @param session            Objeto conexion de Alfresco
     * @return UnidadDocumentalDTO si existe, null si no existe
     */
    @Override
    public UnidadDocumentalDTO findUDById(String idUnidadDocumental, Session session) throws BusinessException {
        logger.info("Ejecutando UnidadDocumentalDTO findUDById(String idUnidadDocumental, Session session)");
        Folder unidadDocumentalFolder = getUDFolderById(idUnidadDocumental, session);

        return !ObjectUtils.isEmpty(unidadDocumentalFolder) ?
                transformarUnidadDocumental(unidadDocumentalFolder) : null;
    }

    /**
     * Metodo que devuelve una Unidad Documental con sus Documentos
     *
     * @param idUnidadDocumental Id Unidad Documental
     * @return UnidadDocumentalDTO      Unidad Documntal
     */
    @Override
    public UnidadDocumentalDTO listarDocsDadoIdUD(String idUnidadDocumental, Session session) throws BusinessException {
        logger.info("Ejecutando UnidadDocumentalDTO listarDocsDadoIdUD(String idUnidadDocumental, Session session)");

        final Folder unidadDocumentalFolder = getUDFolderById(idUnidadDocumental, session);

        List<DocumentoDTO> documentoDTOS = new ArrayList<>();
        UnidadDocumentalDTO documentalDTO = new UnidadDocumentalDTO();

        if (!ObjectUtils.isEmpty(unidadDocumentalFolder)) {
            documentalDTO = transformarUnidadDocumental(unidadDocumentalFolder);
            documentoDTOS = getDocumentsFromFolder(unidadDocumentalFolder);

            List<DocumentoDTO> documentosRecord = getDocumentsFromFolder(recordServices.obtenerRecordFolder(idUnidadDocumental));

            for (DocumentoDTO documentoDTO :
                    documentosRecord) {
                documentoDTOS.remove(documentoDTO);
            }
        }
        documentalDTO.setListaDocumentos(documentoDTOS);
        return documentalDTO;
    }

    @Override
    public Folder getUDFolderById(String idUnidadDocumental, Session session) throws BusinessException {
        logger.info("Ejecutando Folder getUDFolderById(String idProperty, Session session)");
        if (ObjectUtils.isEmpty(idUnidadDocumental)) {
            logger.error("No se ha identificado el Id de la Unidad Documental");
            throw new BusinessException("No se ha identificado el Id de la Unidad Documental");
        }

        String queryString = "SELECT * FROM " + CMCOR + configuracion.getPropiedad(CLASE_UNIDAD_DOCUMENTAL) +
                " WHERE " + CMCOR_UD_ID + " = '" + idUnidadDocumental + "'";
        try {
            final ItemIterable<QueryResult> queryResults = session.query(queryString, false);
            final Iterator<QueryResult> iterator = queryResults.iterator();

            if (iterator.hasNext()) {
                QueryResult queryResult = iterator.next();
                String objectId = queryResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
                return (Folder) session.getObject(session.getObject(objectId));
            }
        } catch (CmisObjectNotFoundException noFoundException) {
            logger.error("La Unidad Documental con ID: {} no existe en el ECM", idUnidadDocumental);
        } catch (Exception ex) {
            logger.error("Ocurrio un error en el Servidor");
            throw new BusinessException("Error Interno, consulte al administrador");
        }
        return null;
    }

    @Override
    public boolean actualizarUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO, Session session) throws BusinessException {

        Folder unidadDocumentalFolder = getUDFolderById(unidadDocumentalDTO.getId(), session);

        Map<String, Object> stringObjectMap = updateProperties(unidadDocumentalFolder, unidadDocumentalDTO);
        return !stringObjectMap.isEmpty();
    }

    private Map<String, Object> updateProperties(Folder folder, UnidadDocumentalDTO unidadDocumentalDTO) throws BusinessException {
        logger.info("Creating props to make the folder");

        final Map<String, Object> props = new HashMap<>();

        boolean isEmptyFolder = ObjectUtils.isEmpty(folder);

        props.put(CMCOR_UD_ID, unidadDocumentalDTO.getId());
        props.put(PropertyIds.DESCRIPTION, configuracion.getPropiedad(CLASE_UNIDAD_DOCUMENTAL));

        String nameFolder = !ObjectUtils.isEmpty(unidadDocumentalDTO.getNombreUnidadDocumental()) ?
                Utilities.reemplazarCaracteresRaros(unidadDocumentalDTO.getNombreUnidadDocumental()) : "";
        props.put(PropertyIds.NAME, ((!isEmptyFolder && !ObjectUtils.isEmpty(nameFolder)) || isEmptyFolder) ?
                nameFolder : folder.getPropertyValue(PropertyIds.NAME));

        String descriptor2 = !ObjectUtils.isEmpty(unidadDocumentalDTO.getDescriptor2()) ?
                Utilities.reemplazarCaracteresRaros(unidadDocumentalDTO.getDescriptor2()) : "";
        props.put(CMCOR_UD_DESCRIPTOR_2, (!isEmptyFolder && !ObjectUtils.isEmpty(descriptor2) || isEmptyFolder) ?
                descriptor2 : folder.getPropertyValue(CMCOR_UD_DESCRIPTOR_2));

        String descriptor1 = !ObjectUtils.isEmpty(unidadDocumentalDTO.getDescriptor1()) ?
                Utilities.reemplazarCaracteresRaros(unidadDocumentalDTO.getDescriptor1()) : "";
        props.put(CMCOR_UD_DESCRIPTOR_1, (!isEmptyFolder && !ObjectUtils.isEmpty(descriptor1) || isEmptyFolder) ?
                descriptor1 : folder.getPropertyValue(CMCOR_UD_DESCRIPTOR_1));

        String accion = !ObjectUtils.isEmpty(unidadDocumentalDTO.getAccion()) ? unidadDocumentalDTO.getAccion() : "";
        props.put(CMCOR_UD_ACCION, (!isEmptyFolder && !ObjectUtils.isEmpty(accion) || isEmptyFolder) ?
                accion : folder.getPropertyValue(CMCOR_UD_ACCION));

        Boolean cerrada = !ObjectUtils.isEmpty(unidadDocumentalDTO.getCerrada()) ? unidadDocumentalDTO.getCerrada() : null;
        props.put(CMCOR_UD_CERRADA, (!isEmptyFolder && !ObjectUtils.isEmpty(cerrada) || isEmptyFolder) ?
                cerrada : folder.getPropertyValue(CMCOR_UD_CERRADA));

        Boolean inactivo = !ObjectUtils.isEmpty(unidadDocumentalDTO.getInactivo()) ? unidadDocumentalDTO.getInactivo() : null;
        props.put(CMCOR_UD_INACTIVO, (!isEmptyFolder && !ObjectUtils.isEmpty(inactivo) || isEmptyFolder) ?
                inactivo : folder.getPropertyValue(CMCOR_UD_INACTIVO));

        Calendar fechaCierre = !ObjectUtils.isEmpty(unidadDocumentalDTO.getFechaCierre()) ? unidadDocumentalDTO.getFechaCierre() : null;
        props.put(CMCOR_UD_FECHA_CIERRE, (!isEmptyFolder && !ObjectUtils.isEmpty(fechaCierre) || isEmptyFolder) ?
                fechaCierre : folder.getPropertyValue(CMCOR_UD_FECHA_CIERRE));

        Calendar fechaInicial = !ObjectUtils.isEmpty(unidadDocumentalDTO.getFechaExtremaInicial()) ? unidadDocumentalDTO.getFechaExtremaInicial() : null;
        props.put(CMCOR_UD_FECHA_INICIAL, (!isEmptyFolder && !ObjectUtils.isEmpty(fechaInicial) || isEmptyFolder) ?
                fechaInicial : folder.getPropertyValue(CMCOR_UD_FECHA_INICIAL));

        Calendar fechaFinal = !ObjectUtils.isEmpty(unidadDocumentalDTO.getFechaExtremaFinal()) ? unidadDocumentalDTO.getFechaExtremaFinal() : null;
        props.put(CMCOR_UD_FECHA_INICIAL, (!isEmptyFolder && !ObjectUtils.isEmpty(fechaFinal) || isEmptyFolder) ?
                fechaFinal : folder.getPropertyValue(CMCOR_UD_FECHA_INICIAL));

        Calendar fechaAutoCierre = !ObjectUtils.isEmpty(unidadDocumentalDTO.getFechaAutoCierre()) ? unidadDocumentalDTO.getFechaAutoCierre() : null;
        props.put(CMCOR_UD_FECHA_AUTO_CIERRE, (!isEmptyFolder && !ObjectUtils.isEmpty(fechaAutoCierre) || isEmptyFolder) ?
                fechaAutoCierre : folder.getPropertyValue(CMCOR_UD_FECHA_AUTO_CIERRE));

        String soporte = !ObjectUtils.isEmpty(unidadDocumentalDTO.getSoporte()) ? unidadDocumentalDTO.getSoporte() : "";
        props.put(CMCOR_UD_SOPORTE, (!isEmptyFolder && !ObjectUtils.isEmpty(soporte) || isEmptyFolder) ?
                soporte : folder.getPropertyValue(CMCOR_UD_SOPORTE));

        String ubicacionTopografica = !ObjectUtils.isEmpty(unidadDocumentalDTO.getUbicacionTopografica()) ? unidadDocumentalDTO.getUbicacionTopografica() : "";
        props.put(CMCOR_UD_UBICACION_TOPOGRAFICA, (!isEmptyFolder && !ObjectUtils.isEmpty(ubicacionTopografica) || isEmptyFolder) ?
                ubicacionTopografica : folder.getPropertyValue(CMCOR_UD_UBICACION_TOPOGRAFICA));

        String faseArchivo = !ObjectUtils.isEmpty(unidadDocumentalDTO.getFaseArchivo()) ? unidadDocumentalDTO.getFaseArchivo() : "";
        props.put(CMCOR_UD_FASE_ARCHIVO, (!isEmptyFolder && !ObjectUtils.isEmpty(faseArchivo) || isEmptyFolder) ?
                faseArchivo : folder.getPropertyValue(CMCOR_UD_FASE_ARCHIVO));

        String udCodigo = !ObjectUtils.isEmpty(unidadDocumentalDTO.getCodigoUnidadDocumental()) ? unidadDocumentalDTO.getCodigoUnidadDocumental() : "";
        props.put(CMCOR_UD_CODIGO, (!isEmptyFolder && !ObjectUtils.isEmpty(udCodigo) || isEmptyFolder) ?
                udCodigo : folder.getPropertyValue(CMCOR_UD_CODIGO));

        String serCodigo = !ObjectUtils.isEmpty(unidadDocumentalDTO.getCodigoSerie()) ? unidadDocumentalDTO.getCodigoSerie() : "";
        props.put(CMCOR_SER_CODIGO, (!isEmptyFolder && !ObjectUtils.isEmpty(serCodigo) || isEmptyFolder) ?
                serCodigo : folder.getPropertyValue(CMCOR_SER_CODIGO));

        String ssCodigo = !ObjectUtils.isEmpty(unidadDocumentalDTO.getCodigoSubSerie()) ? unidadDocumentalDTO.getCodigoSubSerie() : "";
        props.put(CMCOR_SS_CODIGO, (!isEmptyFolder && !ObjectUtils.isEmpty(ssCodigo) || isEmptyFolder) ?
                ssCodigo : folder.getPropertyValue(CMCOR_SS_CODIGO));

        String depCodigo = !ObjectUtils.isEmpty(unidadDocumentalDTO.getCodigoDependencia()) ? unidadDocumentalDTO.getCodigoDependencia() : "";
        props.put(CMCOR_DEP_CODIGO, (!isEmptyFolder && !ObjectUtils.isEmpty(depCodigo) || isEmptyFolder) ?
                depCodigo : folder.getPropertyValue(CMCOR_DEP_CODIGO));

        String sedeCodigo = !ObjectUtils.isEmpty(unidadDocumentalDTO.getCodigoSede()) ? unidadDocumentalDTO.getCodigoSede() : "";
        props.put(CMCOR_DEP_CODIGO_UAP, (!isEmptyFolder && !ObjectUtils.isEmpty(sedeCodigo) || isEmptyFolder) ?
                sedeCodigo : folder.getPropertyValue(CMCOR_DEP_CODIGO_UAP));

        String observaciones = !ObjectUtils.isEmpty(unidadDocumentalDTO.getObservaciones()) ? unidadDocumentalDTO.getObservaciones() : "";
        props.put(CMCOR_UD_OBSERVACIONES, (!isEmptyFolder && !ObjectUtils.isEmpty(observaciones) || isEmptyFolder) ?
                observaciones : folder.getPropertyValue(CMCOR_UD_OBSERVACIONES));

        try {
            if (!isEmptyFolder) {
                folder.updateProperties(props);
            }
        } catch (Exception e) {
            throw new BusinessException("ecm.generic.error");
        }

        return props;
    }

    /**
     * Metodo que verifica la existencia de fechas en el dto y el rango de seleccion
     *
     * @param folder {@link Folder}
     * @param dto    {@link UnidadDocumentalDTO}
     * @return boolean {@link Boolean}
     */
    private boolean areDatesInRange(Folder folder, UnidadDocumentalDTO dto) {

        Calendar dtoFechaCierre = dto.getFechaCierre();
        Calendar folderFechaCierre = folder.getPropertyValue(CMCOR_UD_FECHA_CIERRE);

        Calendar dtoFechaInicial = dto.getFechaExtremaInicial();
        Calendar folderFechaInicial = folder.getPropertyValue(CMCOR_UD_FECHA_INICIAL);

        Calendar dtoFechaFinal = dto.getFechaExtremaFinal();
        Calendar folderFechaFinal = folder.getPropertyValue(CMCOR_UD_FECHA_FINAL);

        if (dtoFechaCierre == null && dtoFechaInicial == null && dtoFechaFinal == null) {
            return true;
        }
        if ((dtoFechaCierre != null && folderFechaCierre != null) &&
                (dtoFechaInicial != null && folderFechaInicial != null) &&
                (dtoFechaFinal != null && folderFechaFinal != null)) {
            return (Utilities.comparaFecha(dtoFechaCierre, folderFechaCierre) == 0) &&
                    (Utilities.comparaFecha(dtoFechaInicial, folderFechaInicial) == 0) &&
                    (Utilities.comparaFecha(dtoFechaFinal, folderFechaFinal) == 0) &&
                    (Utilities.comparaFecha(dtoFechaInicial, dtoFechaFinal) <= 0);
        }
        if ((dtoFechaCierre != null && folderFechaCierre != null) &&
                (dtoFechaInicial != null && folderFechaInicial != null) && dtoFechaFinal == null) {
            return (Utilities.comparaFecha(dtoFechaCierre, folderFechaCierre) == 0) &&
                    (Utilities.comparaFecha(dtoFechaInicial, folderFechaInicial) >= 0);
        }
        if ((dtoFechaCierre != null && folderFechaCierre != null) &&
                (dtoFechaFinal != null && folderFechaFinal != null) && dtoFechaInicial == null) {
            return (Utilities.comparaFecha(dtoFechaCierre, folderFechaCierre) == 0) &&
                    (Utilities.comparaFecha(dtoFechaFinal, folderFechaFinal) <= 0);
        }
        if (dtoFechaCierre != null && folderFechaCierre != null) {
            return (Utilities.comparaFecha(dtoFechaCierre, folderFechaCierre) == 0);
        }
        if ((dtoFechaInicial != null && folderFechaInicial != null) &&
                (dtoFechaFinal != null && folderFechaFinal != null)) {
            return (Utilities.comparaFecha(dtoFechaInicial, folderFechaInicial) == 0) &&
                    (Utilities.comparaFecha(dtoFechaFinal, folderFechaFinal) == 0) &&
                    (Utilities.comparaFecha(dtoFechaInicial, dtoFechaFinal) <= 0);
        }
        if (dtoFechaInicial != null && folderFechaInicial != null) {
            return (Utilities.comparaFecha(dtoFechaInicial, folderFechaInicial) >= 0);
        }
        return ((dtoFechaFinal != null && folderFechaFinal != null) &&
                (Utilities.comparaFecha(dtoFechaFinal, folderFechaFinal) <= 0));
    }

    /**
     * Metodo que devuelve las carpetas hijas de una carpeta
     *
     * @param carpetaPadre Carpeta a la cual se le van a buscar las carpetas hijas
     * @return Lista de carpetas resultantes de la busqueda
     */
    private List<Carpeta> obtenerCarpetasHijasDadoPadre(Carpeta carpetaPadre) {
        logger.info("### Obtener Carpetas Hijas Dado Padre: " + carpetaPadre.getFolder().getName());
        List<Carpeta> listaCarpetas = new ArrayList<>();

        try {
            ItemIterable<CmisObject> listaObjetos = carpetaPadre.getFolder().getChildren();
            listaCarpetas = new ArrayList<>();
            //Lista de carpetas hijas
            for (CmisObject contentItem : listaObjetos) {

                if (contentItem instanceof Folder && contentItem.getType().getId().startsWith("F:cmcor")) {
                    Carpeta folder = new Carpeta();
                    folder.setFolder((Folder) contentItem);
                    listaCarpetas.add(folder);
                }
            }
        } catch (Exception e) {
            logger.error("*** Error al obtener Carpetas Hijas dado padre*** ", e);
        }
        return listaCarpetas;
    }

    /**
     * Metodo para actualizar el nombre de la carpeta
     *
     * @param carpeta Carpeta a la cual se le va a actualizar el nombre
     * @param nombre  Nuevo nombre de la carpeta
     */
    private void actualizarNombreCarpeta(Carpeta carpeta, String nombre) {
        logger.info("### Actualizando nombre folder: " + nombre);
        try {
            carpeta.getFolder().rename(nombre);
        } catch (Exception e) {
            logger.error("*** Error al actualizar nombre folder *** ", e);
        }
    }

    /**
     * Metodo que devuelve la carpeta padre de la carpeta que se le pase.
     *
     * @param folderFather Carpeta padre
     * @param codFolder    Codigo de la carpeta que se le va a chequear la carpeta padre
     * @return Carpeta padre
     */
    private Carpeta chequearCapetaPadre(Carpeta folderFather, String codFolder) {
        Carpeta folderReturn = null;
        Conexion conexion = obtenerConexion();
        List<Carpeta> listaCarpeta = obtenerCarpetasHijasDadoPadre(folderFather);

        Iterator<Carpeta> iterator;
        if (!ObjectUtils.isEmpty(listaCarpeta)) {
            iterator = listaCarpeta.iterator();
            while (iterator.hasNext()) {
                Carpeta aux = iterator.next();
                Carpeta carpeta = obtenerCarpetaPorNombre(aux.getFolder().getName(), conexion.getSession());
                String description = carpeta.getFolder().getDescription();
                if (description.equals(configuracion.getPropiedad(CLASE_DEPENDENCIA))) {
                    folderReturn = getCarpeta(codFolder, aux, "metadatoCodDependencia", folderReturn);
                } else if (description.equals(configuracion.getPropiedad(CLASE_SERIE))) {
                    folderReturn = getCarpeta(codFolder, aux, "metadatoCodSerie", folderReturn);
                } else if (description.equals(configuracion.getPropiedad(CLASE_SUBSERIE))) {
                    logger.info("Entro a clase subserie cargando los valores");
                    folderReturn = getCarpeta(codFolder, aux, "metadatoCodSubserie", folderReturn);
                }
            }
        }
        return folderReturn;
    }

    /**
     * Metodo auxuliar para devolver la carpeta padre
     *
     * @param codFolder Codigo de carpeta
     * @param aux       Carpeta por la cual se realiza la busqueda
     * @param metadato  Propiedad por la cual se filtra
     * @return Carpeta padre
     */
    private Carpeta getCarpeta(String codFolder, Carpeta aux, String metadato, Carpeta folderReturn) {
        Carpeta folderAux = folderReturn;
        final Object propertyValue = aux.getFolder().getPropertyValue(CMCOR + configuracion.getPropiedad(metadato));
        if (!ObjectUtils.isEmpty(propertyValue) &&
                propertyValue.equals(codFolder)) {
            folderAux = aux;
        }
        return folderAux;
    }

    /**
     * Metodo para mover carpetas dentro de Alfresco
     *
     * @param session        Objeto de conexion al Alfresco
     * @param documento      Identificador del documento que se va a mover
     * @param carpetaFuente  Carpeta desde donde se va a mover el documento
     * @param carpetaDestino Carpeta a donde se va a mover el documento
     * @return Mensaje de respuesta del metodo(codigo y mensaje)
     */
    @Override
    public MensajeRespuesta movDocumento(Session session, String documento, String carpetaFuente, String carpetaDestino) {
        logger.info("### Mover documento: " + documento);
        MensajeRespuesta response = new MensajeRespuesta();
        try {

            Carpeta carpetaF;
            Carpeta carpetaD;

            carpetaF = obtenerCarpetaPorNombre(carpetaFuente, session);
            carpetaD = obtenerCarpetaPorNombre(carpetaDestino, session);
            ObjectId idDoc = new ObjectIdImpl(documento);

            CmisObject object = session.getObject(idDoc);
            Document mvndocument = (Document) object;
            mvndocument.move(carpetaF.getFolder(), carpetaD.getFolder());
            response.setMensaje("OK");
            response.setCodMensaje("0000");

        } catch (CmisObjectNotFoundException e) {
            logger.error("*** Error al mover el documento *** ", e);
            response.setMensaje("Documento no encontrado");
            response.setCodMensaje("00006");
        }
        return response;
    }

    /**
     * Metodo para generar el arbol de carpetas dentro del Alfresco
     *
     * @param estructuraList Estrcutura que sera creada dentro del Alfresco
     * @param folder         Carpeta padre de la estructura
     * @return Mensaje de respuesta (codigo y mensaje)
     */
    @Override
    public MensajeRespuesta generarArbol(List<EstructuraTrdDTO> estructuraList, Carpeta folder) {
        logger.info("### Generando arbol");
        Conexion conexion = obtenerConexion();
        MensajeRespuesta response = new MensajeRespuesta();
        try {
            int bandera = 0;
            //Recorremos la lista general

            for (EstructuraTrdDTO estructura : estructuraList) {
                List<OrganigramaDTO> organigramaList = estructura.getOrganigramaItemList();
                List<ContenidoDependenciaTrdDTO> trdList = estructura.getContenidoDependenciaList();
                Utilities utils = new Utilities();

                //Grantizar que el orden de la estructura sea de menor a mayor, ideOrgaAdmin
                utils.ordenarListaOrganigrama(organigramaList);
                Carpeta folderFather = null;
                Carpeta folderSon;
                Carpeta folderFatherContainer = null;
                //Recorremos la lista organigrama
                for (OrganigramaDTO organigrama : organigramaList)
                    if (bandera == 0) {

                        folderFather = chequearCapetaPadre(folder, organigrama.getCodOrg());
                        if (ObjectUtils.isEmpty(folderFather)) {
                            if (!ObjectUtils.isEmpty(obtenerCarpetaPorNombre(organigrama.getNomOrg(), conexion.getSession()).getFolder())) {
                                folderFather = obtenerCarpetaPorNombre(organigrama.getNomOrg(), conexion.getSession());
                                logger.info("Organigrama -- ya existe la carpeta: " + folderFather.getFolder().getName());
                            } else {
                                logger.info("Organigrama --  Creando folder: " + organigrama.getNomOrg());
                                folderFather = crearCarpeta(folder, organigrama.getNomOrg(), organigrama.getCodOrg(), CLASE_BASE, null, null);
                            }

                        } else {
                            logger.info("Organigrama --  La carpeta ya esta creado: " + folderFather.getFolder().getName());
                            //Actualización de folder
                            if (!(organigrama.getNomOrg().equals(folderFather.getFolder().getName()))) {
                                logger.info("Se debe actualizar al nombre: " + organigrama.getNomOrg());
                                actualizarNombreCarpeta(folderFather, organigrama.getNomOrg());
                            } else {
                                logger.info("Organigrama --  El folder ya esta creado: " + organigrama.getNomOrg());
                            }
                        }
                        bandera++;
                        folderFatherContainer = folderFather;

                    } else {
                        folderSon = chequearCapetaPadre(folderFather, organigrama.getCodOrg());
                        if (ObjectUtils.isEmpty(folderSon)) {
                            logger.info("Organigrama --  Creando folder: " + organigrama.getNomOrg());
                            folderSon = crearCarpeta(folderFather, organigrama.getNomOrg(), organigrama.getCodOrg(), CLASE_DEPENDENCIA, folderFather, null);
                        } else {
                            logger.info("Organigrama --  El folder ya esta creado2: " + folderSon.getFolder().getName());
                            //Actualización de folder
                            if (!(organigrama.getNomOrg().equals(folderSon.getFolder().getName()))) {
                                logger.info("Se debe actualizar al nombre: " + organigrama.getNomOrg());
                                actualizarNombreCarpeta(folderSon, organigrama.getNomOrg());
                            } else {
                                logger.info("Organigrama --  El folder ya esta creado: " + organigrama.getNomOrg());
                            }
                        }
                        folderFather = folderSon;
                        folderFatherContainer = folderSon;
                        bandera++;

                    }
                //Recorremos la lista de Dependencias TRD
                for (ContenidoDependenciaTrdDTO dependencias : trdList) {
                    String[] dependenciasArray = {dependencias.getIdOrgAdm(),
                            dependencias.getIdOrgOfc(),
                            dependencias.getCodSerie(),
                            dependencias.getNomSerie(),
                            dependencias.getCodSubSerie(),
                            dependencias.getNomSubSerie(),
                    };
                    String nombreSerie = formatearNombre(dependenciasArray, "formatoNombreSerie");
                    folderSon = chequearCapetaPadre(folderFatherContainer, dependencias.getCodSerie());
                    if (ObjectUtils.isEmpty(folderSon)) {
                        if (!ObjectUtils.isEmpty(nombreSerie)) {
                            logger.info("TRD --  Creando folder: " + nombreSerie);
                            folderSon = crearCarpeta(folderFatherContainer, nombreSerie, dependencias.getCodSerie(), CLASE_SERIE, folderFather, dependencias.getIdOrgOfc());
                        } else {
                            logger.info("El formato para el nombre de la serie no es valido.");
                            break;
                        }
                    } else {
                        //Actualización de folder
                        if (!(nombreSerie.equals(folderSon.getFolder().getName()))) {
                            logger.info("Se debe cambiar el nombre: " + nombreSerie);
                            actualizarNombreCarpeta(folderSon, nombreSerie);
                        } else {
                            logger.info("TRD --  El folder ya esta creado: " + nombreSerie);
                        }
                    }
                    folderFather = folderSon;
                    if (!ObjectUtils.isEmpty(dependencias.getCodSubSerie())) {
                        String nombreSubserie = formatearNombre(dependenciasArray, "formatoNombreSubserie");
                        folderSon = chequearCapetaPadre(folderFather, dependencias.getCodSubSerie());
                        if (ObjectUtils.isEmpty(folderSon)) {
                            if (!ObjectUtils.isEmpty(nombreSubserie)) {
                                logger.info("TRD --  Creando folder: " + nombreSubserie);
                                folderSon = crearCarpeta(folderFather, nombreSubserie, dependencias.getCodSubSerie(), CLASE_SUBSERIE, folderFather, dependencias.getIdOrgOfc());
                            } else {
                                logger.info("El formato para el nombre de la subserie no es valido.");
                                break;
                            }
                        } else {

                            //Actualización de folder
                            if (!(nombreSubserie.equals(folderSon.getFolder().getName()))) {
                                logger.info("Se debe cambiar el nombre: " + nombreSubserie);
                                actualizarNombreCarpeta(folderSon, nombreSubserie);
                            } else {
                                logger.info("TRD --  El folder ya esta creado: " + nombreSubserie);
                            }
                        }
                        folderFather = folderSon;
                    }
                }
                bandera = 0;
                response.setCodMensaje("OK");
                response.setMensaje("00000");
            }
        } catch (Exception e) {
            logger.error("Error al crear arbol content ", e);
            response.setCodMensaje("Error al crear el arbol");
            response.setMensaje("111112");
        }
        logger.info("### Estructura creada------..");
        return response;
    }

    /**
     * Metodo para obtener documentos asociados a un documento principal en Alfresco
     *
     * @param session   Objeto de conexion a Alfresco
     * @param documento DTO que contiene los metadatos el documento que se va a buscar
     * @return Devuelve el listado de documentos asociados al id de documento padre
     */
    @Override
    public MensajeRespuesta obtenerDocumentosAdjuntos(Session session, DocumentoDTO documento) {

        logger.info("Se entra al metodo obtenerDocumentosAdjuntos");

        MensajeRespuesta response = new MensajeRespuesta();
        try {
            ItemIterable<QueryResult> resultsPrincipalAdjunto = getPrincipalAdjuntosQueryResults(session, documento);

            ArrayList<DocumentoDTO> documentosLista = new ArrayList<>();
            for (QueryResult qResult : resultsPrincipalAdjunto) {

                DocumentoDTO documentoDTO = new DocumentoDTO();

                String[] parts = qResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID).toString().split(";");
                String idDocumento = parts[0];

                documentoDTO.setIdDocumento(idDocumento);
                if (qResult.getPropertyValueByQueryName(CMCOR_ID_DOC_PRINCIPAL) != null) {
                    documentoDTO.setIdDocumentoPadre(documento.getIdDocumento());
                    documentoDTO.setTipoPadreAdjunto(qResult.getPropertyValueByQueryName(CMCOR_TIPO_DOCUMENTO).toString());
                } else {
                    documentoDTO.setTipoPadreAdjunto("Principal");
                }
                Document document = (Document) session.getObject(session.createObjectId(idDocumento));
                documentoDTO.setNombreDocumento(qResult.getPropertyValueByQueryName(PropertyIds.NAME));
                GregorianCalendar newGregCal = qResult.getPropertyValueByQueryName(PropertyIds.CREATION_DATE);
                documentoDTO.setFechaCreacion(newGregCal.getTime());
                documentoDTO.setTipologiaDocumental(qResult.getPropertyValueByQueryName(CMCOR_TIPOLOGIA_DOCUMENTAL));
                documentoDTO.setTipoDocumento(qResult.getPropertyValueByQueryName(PropertyIds.CONTENT_STREAM_MIME_TYPE).toString());
                documentoDTO.setTamano(qResult.getPropertyValueByQueryName(PropertyIds.CONTENT_STREAM_LENGTH).toString());

                File file = convertInputStreamToFile(document.getContentStream());
                byte[] data = FileUtils.readFileToByteArray(file);
                documentoDTO.setDocumento(data);
                String nroRadicado = qResult.getPropertyValueByQueryName(CMCOR_NRO_RADICADO);
                if (ObjectUtils.isEmpty(nroRadicado))
                    nroRadicado = "";
                documentoDTO.setNroRadicado(nroRadicado);
                documentoDTO.setNombreRemitente(qResult.getPropertyValueByQueryName(CMCOR_NOMBRE_REMITENTE) != null ? qResult.getPropertyValueByQueryName(CMCOR_NOMBRE_REMITENTE).toString() : null);

                documentosLista.add(documentoDTO);
            }
            response.setCodMensaje("0000");
            response.setMensaje("OK");
            response.setDocumentoDTOList(documentosLista);

        } catch (Exception e) {
            response.setCodMensaje("2222");
            response.setMensaje("Error en la obtención de los documentos adjuntos: " + e.getMessage());
            logger.error("Error en la obtención de los documentos adjuntos: ", e);
            response.setDocumentoDTOList(new ArrayList<>());
        }
        logger.info("Se sale del metodo obtenerDocumentosAdjuntos con respuesta: " + response.toString());
        return response;
    }

    private ItemIterable<QueryResult> getPrincipalAdjuntosQueryResults(Session session, DocumentoDTO documento) {
        //Obtener el documentosAdjuntos
        String query = "SELECT * FROM cmcor:CM_DocumentoPersonalizado";
        boolean where = false;

        if (!ObjectUtils.isEmpty(documento.getIdDocumento())) {
            where = true;

            query += " WHERE " + PropertyIds.OBJECT_ID + " = '" + documento.getIdDocumento() + "'" +
                    " OR " + CMCOR_ID_DOC_PRINCIPAL + " = '" + documento.getIdDocumento() + "'";
        }
        if (!ObjectUtils.isEmpty(documento.getNroRadicado())) {

            query += (where ? " AND " : " WHERE ") + CMCOR_NRO_RADICADO + " = '" + documento.getNroRadicado() + "'";
            where = true;
        }
        if (!ObjectUtils.isEmpty(documento.getNroRadicadoReferido())) {
            String[] nroRadicadoReferidoArray = documento.getNroRadicadoReferido();
            String radicadoReferido = null;
            for (String nroRadRef : nroRadicadoReferidoArray
                    ) {
                radicadoReferido = nroRadRef;
                break;
            }
            query += (where ? " AND " : " WHERE ") + "(" + CMCOR_NUMERO_REFERIDO + " LIKE '" + radicadoReferido + SEPARADOR + "%' " +
                    "OR " + CMCOR_NUMERO_REFERIDO + " LIKE '" + SEPARADOR + radicadoReferido + SEPARADOR + "%' " +
                    "OR " + CMCOR_NUMERO_REFERIDO + " LIKE '" + SEPARADOR + radicadoReferido + "%')";

        }
        return session.query(query, false);
    }

    /**
     * Metodo para obtener versiones del documento
     *
     * @param session Objeto de conexion a Alfresco
     * @param idDoc   Id Documento que se va pedir versiones
     * @return Devuelve el listado de versiones asociados al id de documento
     */
    @Override
    public MensajeRespuesta obtenerVersionesDocumento(Session session, String idDoc) {

        logger.info("Se entra al metodo obtenerVersionesDocumento");

        MensajeRespuesta response = new MensajeRespuesta();

        ArrayList<DocumentoDTO> versionesLista = new ArrayList<>();
        try {
            //Obtener documento dado id
            Document doc = (Document) session.getObject(idDoc);
            List<Document> versions = doc.getAllVersions();
            for (Document version : versions) {
                DocumentoDTO documentoDTO = new DocumentoDTO();
                documentoDTO.setNombreDocumento(version.getName());
                documentoDTO.setVersionLabel(version.getVersionLabel());
                documentoDTO.setTamano(String.valueOf(version.getContentStreamLength()));
                documentoDTO.setIdDocumento(idDoc);
                documentoDTO.setTipoDocumento(version.getContentStreamMimeType());
                versionesLista.add(documentoDTO);
            }
            response.setCodMensaje("0000");
            response.setMensaje("OK");
            response.setDocumentoDTOList(versionesLista);
        } catch (Exception e) {
            response.setCodMensaje("2222");
            response.setMensaje("Error en la obtención de las versiones del documento: " + e.getMessage());
            response.setDocumentoDTOList(new ArrayList<>());
            logger.error("Error en la obtención de las versiones del documento: ", e);
        }
        logger.info("Se devuelven las versiones del documento: ", versionesLista.toString());
        logger.info("Se sale del metodo obtenerVersionesDocumento con respuesta: " + response.toString());
        return response;
    }

    /**
     * Metodo para subir documentos al Alfresco
     *
     * @param session   Objeto de conexion a Alfresco
     * @param documento Objeto que contiene los metadatos del documento.
     * @param selector  Selector que dice donde se va a gauardar el documento
     * @return Devuelve el id de la carpeta creada
     */
    @Override
    public MensajeRespuesta subirDocumentoPrincipalAdjunto(Session session, DocumentoDTO documento, String selector) {

        logger.info("Se entra al metodo subirDocumentoPrincipalAdjunto");

        MensajeRespuesta response = new MensajeRespuesta();
        //Se definen las propiedades del documento a subir
        Map<String, Object> properties = new HashMap<>();
        properties.put(PropertyIds.OBJECT_TYPE_ID, "D:cmcor:CM_DocumentoPersonalizado");
        //En caso de que sea documento adjunto se le pone el id del documento principal dentro del parametro cmcor:xIdentificadorDocPrincipal
        if (documento.getIdDocumentoPadre() != null) {
            properties.put(CMCOR_ID_DOC_PRINCIPAL, documento.getIdDocumentoPadre());
            properties.put(CMCOR_TIPO_DOCUMENTO, "Anexo");
        }

        properties.put(PropertyIds.NAME, documento.getNombreDocumento());

        if ("PD".equals(selector)) {
            buscarCrearCarpeta(session, documento, response, documento.getDocumento(), properties, PRODUCCION_DOCUMENTAL);
        } else {
            buscarCrearCarpetaRadicacion(session, documento, response, properties, selector);
        }

        logger.info("Se sale del metodo subirDocumentoPrincipalAdjunto");
        return response;
    }

    /**
     * Metodo para crear Link a un documento dentro de la carpeta Documentos de apoyo
     *
     * @param session   Objeto de conexion a Alfresco
     * @param documento Objeto qeu contiene los datos del documento
     * @return Mensaje de respuesta
     */
    @Override
    public MensajeRespuesta crearLinkDocumentosApoyo(Session session, DocumentoDTO documento) {
        logger.info("Se entra al metodo crearLinkDocumentosApoyo");

        MensajeRespuesta response = new MensajeRespuesta();

        Map<String, Object> properties = new HashMap<>();

        buscarCrearCarpeta(session, documento, response, documento.getDocumento(), properties, DOCUMENTOS_APOYO);

        logger.info("Se sale del metodo crearLinkDocumentosApoyo");
        return response;
    }

    /**
     * Metodo para crear el link
     *
     * @param session      Objeto session
     * @param documentoDTO Identificador del dcumento
     * @param carpetaLink  Carpeta donde se va a crear el link
     */

    private void crearLink(Session session, DocumentoDTO documentoDTO, Carpeta carpetaLink) {

        logger.info("Se entra al metodo crearLink");

        Map<String, Object> properties = new HashMap<>();
        properties.put(PropertyIds.BASE_TYPE_ID, BaseTypeId.CMIS_ITEM.value());

        // define a name and a description for the link
        properties.put(PropertyIds.NAME, documentoDTO.getNombreDocumento() + ".link");
        properties.put("cmis:description", "Documento Vinculado");
        properties.put(PropertyIds.OBJECT_TYPE_ID, "I:app:filelink");

        //define the destination node reference
        properties.put("cm:destination", "workspace://SpacesStore/" + documentoDTO);
        //Se crea el link
        session.createItem(properties, carpetaLink.getFolder());

        logger.info("Se crea el link y se sale del método crearLink");
    }

    /**
     * Metodo que busca una Unidad Documental en el ECM
     *
     * @param folder Obj ECm
     * @return List<DocumentoDTO> Lista de los documentos de la carpeta
     */
    @Override
    public List<DocumentoDTO> getDocumentsFromFolder(Folder folder) {
        final List<DocumentoDTO> documentoDTOS = new ArrayList<>();
        if (folder == null) {
            return documentoDTOS;
        }
        for (CmisObject cmisObject :
                folder.getChildren()) {
            if (cmisObject.getBaseTypeId() == BaseTypeId.CMIS_DOCUMENT &&
                    cmisObject.getType().getId().startsWith("D:cmcor:")) {
                documentoDTOS.add(documentoDTOS.size(), transformarDocumento((Document) cmisObject));
            }
        }
        return documentoDTOS;
    }

    /**
     * Metodo para devolver la Unidad Documental
     *
     * @param unidadDocumentalDTO Obj Unidad Documental
     * @param documentoDTOS       Lista de documentos a guardar
     * @param session             Obj conexion de alfresco
     * @return MensajeRespuesta       Unidad Documental
     */
    @Override
    public MensajeRespuesta subirDocumentosUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO, List<DocumentoDTO> documentoDTOS, Session session) {
        return null;
    }

    @Override
    public MensajeRespuesta subirDocumentoUnidadDocumentalECM(UnidadDocumentalDTO unidadDocumentalDTO, DocumentoDTO documentoDTO, Session session) {
        return null;
    }

    /**
     * Metodo para subir/versionar documentos al Alfresco
     *
     * @param session   Objeto de conexion a Alfresco
     * @param documento Documento que se va a subir/versionar
     * @param selector  parametro que indica donde se va a guardar el documento
     * @return Devuelve el id de la carpeta creada
     */
    @Override
    public MensajeRespuesta subirVersionarDocumentoGenerado(Session session, DocumentoDTO documento, String selector) {

        logger.info("Se entra al metodo subirVersionarDocumentoGenerado");

        MensajeRespuesta response = new MensajeRespuesta();
        List<DocumentoDTO> documentoDTOList = new ArrayList<>();
        Map<String, Object> properties = new HashMap<>();


        byte[] bytes = documento.getDocumento();
        if ("html".equals(documento.getTipoDocumento())) {
            documento.setTipoDocumento("text/html");
        } else {
            documento.setTipoDocumento(APPLICATION_PDF);
        }

        if (documento.getIdDocumento() == null) {

            //Se definen las propiedades del documento a subir

            properties.put(PropertyIds.OBJECT_TYPE_ID, "D:cmcor:CM_DocumentoPersonalizado");
            properties.put(PropertyIds.CONTENT_STREAM_MIME_TYPE, documento.getTipoDocumento());
            properties.put(PropertyIds.NAME, documento.getNombreDocumento());

            if ("PD".equals(selector)) {
                buscarCrearCarpeta(session, documento, response, bytes, properties, PRODUCCION_DOCUMENTAL);
            } else {
                buscarCrearCarpetaRadicacion(session, documento, response, properties, selector);
            }
        } else {
            //Obtener documento dado id
            Document doc = (Document) session.getObject(documento.getIdDocumento());
            properties.put(PropertyIds.NAME, documento.getNombreDocumento());
            properties.put(PropertyIds.CONTENT_STREAM_FILE_NAME, documento.getNombreDocumento());
            doc.updateProperties(properties, true);

            //Obtener el PWC (Private Working copy)
            Document pwc = (Document) session.getObject(doc.checkOut());

            ContentStream contentStream = new ContentStreamImpl(documento.getNombreDocumento(), BigInteger.valueOf(bytes.length), documento.getTipoDocumento(), new ByteArrayInputStream(bytes));
            // Check in the pwc
            try {
                pwc.checkIn(false, properties, contentStream, "nueva version");
                Document docAux = (Document) session.getObject(documento.getIdDocumento());
                response.setCodMensaje("0000");
                response.setMensaje("Documento versionado correctamente");
                documento.setVersionLabel(docAux.getVersionLabel());
                documentoDTOList.add(documento);
                response.setDocumentoDTOList(documentoDTOList);
                logger.info("Documento versionado correctamente con metadatos: " + documento);
            } catch (CmisBaseException e) {
                logger.error("checkin failed, trying to cancel the checkout", e);
                pwc.cancelCheckOut();
                response.setCodMensaje("222222");
                response.setMensaje("Error versionando documento: " + e);
            }
        }

        return response;
    }

    /**
     * Metodo para buscar crear carpetas
     *
     * @param session            Objeto session
     * @param documentoDTO       Objeto qeu contiene los metadatos
     * @param response           Mensaje de respuesta
     * @param bytes              Contenido del documento
     * @param properties         propiedades de carpeta
     * @param carpetaCrearBuscar Carpeta
     */
    private void buscarCrearCarpeta(Session session, DocumentoDTO documentoDTO, MensajeRespuesta response, byte[] bytes, Map<String, Object> properties, String carpetaCrearBuscar) {
        logger.info("MetaDatos: " + documentoDTO.toString());
        String idDocumento;
        List<DocumentoDTO> documentoDTOList = new ArrayList<>();
        try {
            //Se obtiene la carpeta dentro del ECM al que va a ser subido el documento
            logger.info("### Se elige la carpeta donde se va a guardar el documento principal..");
            logger.info("###------------ Se elige la sede donde se va a guardar el documento principal..");

            if (ObjectUtils.isEmpty(documentoDTO.getCodigoDependencia())) {
                throw new BusinessException("No se ha identificado el codigo de la Dependencia");
            }

            Carpeta folderAlfresco;
            //folderAlfresco = obtenerCarpetaPorNombre(documentoDTO.getSede(), session);
            folderAlfresco = obtenerCarpetaPorCodigoDependencia(documentoDTO.getCodigoDependencia(), session);

            if (!ObjectUtils.isEmpty(folderAlfresco.getFolder())) {

                logger.info("Se busca si existe la carpeta de Produccion documental para el año en curso dentro de la dependencia " + documentoDTO.getDependencia());
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                List<Carpeta> carpetasDeLaDependencia = obtenerCarpetasHijasDadoPadre(folderAlfresco);

                Carpeta carpetaTarget;

                Optional<Carpeta> produccionDocumental = carpetasDeLaDependencia.stream()
                        .filter(p -> p.getFolder().getName().equals(carpetaCrearBuscar + year)).findFirst();
                carpetaTarget = getCarpeta(carpetaCrearBuscar, Optional.of(folderAlfresco), year, produccionDocumental);

                idDocumento = crearDocumentoDevolverId(documentoDTO, response, bytes, properties, documentoDTOList, carpetaTarget);
                //Creando el mensaje de respuesta
                response.setCodMensaje("0000");
                response.setMensaje("Documento añadido correctamente");
                logger.info(AVISO_CREA_DOC_ID + idDocumento);
            } else {
                logger.info(NO_EXISTE_DEPENDENCIA + documentoDTO.getDependencia());
                response.setCodMensaje("4444");
                response.setMensaje(NO_EXISTE_DEPENDENCIA + documentoDTO.getDependencia());
            }

            /*if (folderAlfresco.getFolder() != null) {
                logger.info("###------------------- Se obtienen todas las dependencias de la sede..");
                List<Carpeta> carpetasHijas = obtenerCarpetasHijasDadoPadre(folderAlfresco);

                //Se busca si existe la carpeta de Produccion documental para el año en curso dentro de la dependencia
                Optional<Carpeta> dependencia = carpetasHijas.stream()
                        .filter(p -> p.getFolder().getName().equals(documentoDTO.getDependencia())).findFirst();

                logger.info("Se obtienen la dependencia referente a la sede" + folderAlfresco);
                if (dependencia.isPresent()) {

                    logger.info("Se busca si existe la carpeta de Produccion documental para el año en curso dentro de la dependencia " + documentoDTO.getDependencia());
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    List<Carpeta> carpetasDeLaDependencia = obtenerCarpetasHijasDadoPadre(dependencia.get());

                    Carpeta carpetaTarget;

                    Optional<Carpeta> produccionDocumental = carpetasDeLaDependencia.stream()
                            .filter(p -> p.getFolder().getName().equals(carpetaCrearBuscar + year)).findFirst();
                    carpetaTarget = getCarpeta(carpetaCrearBuscar, dependencia, year, produccionDocumental);

                    idDocumento = crearDocumentoDevolverId(documentoDTO, response, bytes, properties, documentoDTOList, carpetaTarget);
                    //Creando el mensaje de respuesta
                    response.setCodMensaje("0000");
                    response.setMensaje("Documento añadido correctamente");
                    logger.info(AVISO_CREA_DOC_ID + idDocumento);
                } else {
                    logger.info(NO_EXISTE_DEPENDENCIA + documentoDTO.getDependencia());
                    response.setCodMensaje("4445");
                    response.setMensaje(NO_EXISTE_DEPENDENCIA + documentoDTO.getSede());
                }
            } else {
                logger.info(NO_EXISTE_SEDE + documentoDTO.getSede());
                response.setCodMensaje("4444");
                response.setMensaje(NO_EXISTE_SEDE + documentoDTO.getSede());
            }*/

        } catch (CmisContentAlreadyExistsException ccaee) {
            logger.error(ECM_ERROR_DUPLICADO, ccaee);
            response.setCodMensaje("1111");
            response.setMensaje(ECM_ERROR_DUPLICADO);
        } catch (CmisConstraintException cce) {
            logger.error(ECM_ERROR, cce);
            response.setCodMensaje("2222");
            response.setMensaje(configuracion.getPropiedad(ECM_ERROR));
        } catch (Exception e) {
            logger.error(ERROR_TIPO_EXCEPTION, e);
            response.setCodMensaje("2222");
            response.setMensaje(e.getMessage());
            //response.setMensaje(configuracion.getPropiedad(ECM_ERROR));
        }
    }

    private Carpeta obtenerCarpetaPorCodigoDependencia(String codigoDependencia, Session session) {
        Carpeta folder = new Carpeta();
        try {
            String queryString = "SELECT * FROM cmcor:CM_Unidad_Administrativa" +
                    " WHERE " + CMCOR_DEP_CODIGO + " = '" + codigoDependencia + "'" +
                    " AND cmis:objectTypeId = 'F:cmcor:CM_Unidad_Administrativa'";
            ItemIterable<QueryResult> results = session.query(queryString, false);
            for (QueryResult qResult : results) {
                String objectId = qResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
                folder.setFolder((Folder) session.getObject(session.createObjectId(objectId)));
            }
        } catch (Exception e) {
            logger.error("*** Error al obtenerCarpetas *** ", e);
        }

        return folder;
    }

    private Carpeta obtenerCarpetaPorSede(String codigoSede, Session session) {
        Carpeta folder = new Carpeta();
        try {

            final String queryString = "SELECT * FROM cmcor:CM_Unidad_Administrativa" +
                    " WHERE " + CMCOR_DEP_CODIGO_UAP + " = '" + codigoSede + "'" +
                    " and cmis:objectTypeId = 'F:cmcor:CM_Unidad_Administrativa'";

            ItemIterable<QueryResult> results = session.query(queryString, false);
            for (QueryResult qResult : results) {
                String objectId = qResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
                folder.setFolder((Folder) session.getObject(session.createObjectId(objectId)));
            }
        } catch (Exception e) {
            logger.error("*** Error al obtenerCarpetas *** ", e);
        }
        return folder;
    }

    private Carpeta getCarpeta(String carpetaCrearBuscar, Optional<Carpeta> dependencia, int year, Optional<Carpeta> produccionDocumental) {
        Carpeta carpetaTarget = null;
        if (produccionDocumental.isPresent()) {
            logger.info(EXISTE_CARPETA + carpetaCrearBuscar + year);
            carpetaTarget = produccionDocumental.get();
        } else {
            logger.info("Se crea la Carpeta: " + carpetaCrearBuscar + year);
            if (dependencia.isPresent()) {
                carpetaTarget = crearCarpeta(dependencia.get(), carpetaCrearBuscar + year, "11", CLASE_SUBSERIE, dependencia.get(), null);
            }
        }
        return carpetaTarget;
    }

    /**
     * Metodo para buscar crear carpetas de radicacion de entrada
     *
     * @param session    Objeto session
     * @param documento  Objeto qeu contiene los metadatos
     * @param response   Mensaje de respuesta
     * @param properties propiedades de carpeta
     */
    private void buscarCrearCarpetaRadicacion(Session session, DocumentoDTO documento, MensajeRespuesta response, Map<String, Object> properties, String tipoComunicacion) {

        try {
            //Se obtiene la carpeta dentro del ECM al que va a ser subido el documento
            new Carpeta();
            Carpeta folderAlfresco;
            logger.info("### Se elige la carpeta donde se va a guardar el documento radicado..");
            logger.info("###------------ Se elige la sede donde se va a guardar el documento radicado..");
            folderAlfresco = obtenerCarpetaPorNombre(documento.getSede(), session);

            if (folderAlfresco.getFolder() != null) {
                logger.info("###------------------- Se obtienen todas las dependencias de la sede..");
                List<Carpeta> carpetasHijas = obtenerCarpetasHijasDadoPadre(folderAlfresco);

                String comunicacionOficial = "";
                String tipoComunicacionSelector;
                tipoComunicacionSelector = getTipoComunicacionSelector(tipoComunicacion);

                if ("EI".equals(tipoComunicacion) || "SI".equals(tipoComunicacion)) {
                    comunicacionOficial = TIPO_COMUNICACION_INTERNA;
                } else if ("EE".equals(tipoComunicacion) || "SE".equals(tipoComunicacion)) {
                    comunicacionOficial = TIPO_COMUNICACION_EXTERNA;
                }

                //Se busca si existe la dependencia
                Optional<Carpeta> dependencia = carpetasHijas.stream()
                        .filter(p -> p.getFolder().getName().equals(documento.getDependencia())).findFirst();

                logger.info("Se obtienen la dependencia referente a la sede: " + documento.getSede());
                if (dependencia.isPresent()) {

                    logger.info("Se busca si existe la carpeta de Comunicaciones Oficiales dentro de la dependencia " + documento.getDependencia());

                    List<Carpeta> carpetasDeLaDependencia = obtenerCarpetasHijasDadoPadre(dependencia.get());

                    //Obtengo la carpeta de comunicaciones oficiales si existe
                    Optional<Carpeta> comunicacionOficialFolder = carpetasDeLaDependencia.stream()
                            .filter(p -> p.getFolder().getName().contains("0231_COMUNICACIONES OFICIALES")).findFirst();

                    crearInsertarCarpetaRadicacion(documento, response, documento.getDocumento(), properties, comunicacionOficial, tipoComunicacionSelector, comunicacionOficialFolder);
                } else {
                    response.setMensaje(NO_EXISTE_DEPENDENCIA + documento.getDependencia());
                    response.setCodMensaje("4445");
                    logger.info(NO_EXISTE_DEPENDENCIA + documento.getDependencia());
                }
            } else {
                response.setMensaje(NO_EXISTE_SEDE + documento.getSede());
                response.setCodMensaje("4444");
                logger.info(NO_EXISTE_SEDE + documento.getSede());
            }
        } catch (
                CmisContentAlreadyExistsException ccaee)

        {
            logger.error(ECM_ERROR_DUPLICADO, ccaee);
            response.setCodMensaje("1111");
            response.setMensaje("El documento ya existe en el ECM");
        } catch (
                CmisConstraintException cce)

        {
            logger.error(ECM_ERROR, cce);
            response.setCodMensaje("2222");
            response.setMensaje(configuracion.getPropiedad(ECM_ERROR));
        } catch (
                Exception e)

        {
            logger.error(ERROR_TIPO_EXCEPTION, e);
            response.setCodMensaje("2222");
            response.setMensaje(configuracion.getPropiedad(ECM_ERROR));
        }
    }

    private String getTipoComunicacionSelector(String tipoComunicacion) {
        switch (tipoComunicacion) {
            case "EI":
                return COMUNICACIONES_INTERNAS_RECIBIDAS;

            case "SI":
                return COMUNICACIONES_INTERNAS_ENVIADAS;

            case "EE":
                return COMUNICACIONES_EXTERNAS_RECIBIDAS;

            case "SE":
                return COMUNICACIONES_EXTERNAS_ENVIADAS;
            default:
                return COMUNICACIONES_INTERNAS_RECIBIDAS;
        }
    }

    private void crearInsertarCarpetaRadicacion(DocumentoDTO documentoDTO, MensajeRespuesta response, byte[] bytes, Map<String, Object> properties, String comunicacionOficial, String tipoComunicacionSelector, Optional<Carpeta> comunicacionOficialFolder) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        List<DocumentoDTO> documentoDTOList = new ArrayList<>();
        Carpeta carpetaTarget = new Carpeta();
        String idDocumento;
        if (comunicacionOficialFolder.isPresent()) {
            logger.info(EXISTE_CARPETA + comunicacionOficialFolder.get().getFolder().getName());

            List<Carpeta> carpetasDeComunicacionOficial = obtenerCarpetasHijasDadoPadre(comunicacionOficialFolder.get());

            Optional<Carpeta> comunicacionOficialInOut = carpetasDeComunicacionOficial.stream()
                    .filter(p -> p.getFolder().getName().contains(comunicacionOficial)).findFirst();

            if (!comunicacionOficialInOut.isPresent()) {

                Carpeta carpetaCreada = crearCarpeta(comunicacionOficialFolder.get(), comunicacionOficial, "11", CLASE_SUBSERIE, comunicacionOficialFolder.get(), null);
                if (carpetaCreada != null) {
                    logger.info(EXISTE_CARPETA + carpetaCreada.getFolder().getName());
                    List<Carpeta> carpetasDeComunicacionOficialDentro = obtenerCarpetasHijasDadoPadre(carpetaCreada);

                    Optional<Carpeta> comunicacionOficialInOutDentro = carpetasDeComunicacionOficialDentro.stream()
                            .filter(p -> p.getFolder().getName().contains(tipoComunicacionSelector)).findFirst();
                    carpetaTarget = comunicacionOficialInOutDentro.orElseGet(() -> crearCarpeta(carpetaCreada, tipoComunicacionSelector + year, "11", CLASE_SUBSERIE, carpetaCreada, null));
                }

            } else {
                logger.info(EXISTE_CARPETA + comunicacionOficialInOut.get().getFolder().getName());

                List<Carpeta> carpetasDeComunicacionOficialDentro = obtenerCarpetasHijasDadoPadre(comunicacionOficialInOut.get());

                Optional<Carpeta> comunicacionOficialInOutDentro = carpetasDeComunicacionOficialDentro.stream()
                        .filter(p -> p.getFolder().getName().contains(tipoComunicacionSelector)).findFirst();
                carpetaTarget = comunicacionOficialInOutDentro.orElseGet(() -> crearCarpeta(comunicacionOficialInOut.get(), tipoComunicacionSelector + year, "11", CLASE_SUBSERIE, comunicacionOficialInOut.get(), null));
            }
            idDocumento = crearDocumentoDevolverId(documentoDTO, response, bytes, properties, documentoDTOList, carpetaTarget);
            //Creando el mensaje de respuesta
            response.setCodMensaje("0000");
            response.setMensaje("Documento añadido correctamente");
            logger.info(AVISO_CREA_DOC_ID + idDocumento);

        } else {
            response.setCodMensaje("3333");
            response.setMensaje("En esta sede y dependencia no esta permitido relaizar radicaciones");
        }
    }

    private String crearDocumentoDevolverId(DocumentoDTO documentoDTO, MensajeRespuesta response, byte[] bytes, Map<String, Object> properties, List<DocumentoDTO> documentoDTOList, Carpeta carpetaTarget) {
        String idDocumento;
        logger.info("Se llenan los metadatos del documento a crear");
        ContentStream contentStream = new ContentStreamImpl(documentoDTO.getNombreDocumento(), BigInteger.valueOf(bytes.length), documentoDTO.getTipoDocumento(), new ByteArrayInputStream(bytes));

        if (documentoDTO.getNroRadicado() != null) {
            properties.put(CMCOR_NRO_RADICADO, documentoDTO.getNroRadicado());
        }
        if (documentoDTO.getNombreRemitente() != null) {
            properties.put(CMCOR_NOMBRE_REMITENTE, documentoDTO.getNombreRemitente());
        }
        if (documentoDTO.getTipologiaDocumental() != null) {
            properties.put(CMCOR_TIPOLOGIA_DOCUMENTAL, documentoDTO.getTipologiaDocumental());
        }
        logger.info(AVISO_CREA_DOC);
        Document newDocument = carpetaTarget.getFolder().createDocument(properties, contentStream, VersioningState.MAJOR);

        idDocumento = newDocument.getId();
        String[] parts = idDocumento.split(";");
        idDocumento = parts[0];
        documentoDTO.setVersionLabel(newDocument.getVersionLabel());
        documentoDTO.setIdDocumento(idDocumento);
        documentoDTOList.add(documentoDTO);
        response.setDocumentoDTOList(documentoDTOList);
        return idDocumento;
    }

    /**
     * Metodo para modificar metadatos del documento de Alfresco
     *
     * @param session             Objeto de conexion a Alfresco
     * @param idDocumento         Nombre del documento que se va a crear
     * @param nroRadicado         Documento que se va a subir
     * @param tipologiaDocumental Tipo de comunicacion que puede ser Externa o Interna
     * @return Devuelve el id de la carpeta creada
     */
    @Override
    public MensajeRespuesta modificarMetadatosDocumento(Session session, String idDocumento, String nroRadicado, String tipologiaDocumental, String nombreRemitente) {
        logger.info("### Modificar documento: " + idDocumento);
        MensajeRespuesta response = new MensajeRespuesta();
        try {

            ObjectId idDoc = new ObjectIdImpl(idDocumento);

            Map<String, Object> updateProperties = new HashMap<>();
            updateProperties.put(CMCOR_NRO_RADICADO, nroRadicado);
            updateProperties.put(CMCOR_NOMBRE_REMITENTE, nombreRemitente);
            updateProperties.put(CMCOR_TIPOLOGIA_DOCUMENTAL, tipologiaDocumental);

            CmisObject object = session.getObject(idDoc);
            object.updateProperties(updateProperties);
            logger.info("### Modificados los metadatos de correctamente");
            response.setMensaje("OK");
            response.setCodMensaje("0000");

        } catch (CmisObjectNotFoundException e) {
            logger.error("*** Error al modificar el documento *** ", e);
            response.setMensaje("Documento no encontrado");
            response.setCodMensaje("00006");
        }
        return response;
    }

    /**
     * Metodo para llenar propiedades para crear carpeta
     *
     * @param tipoCarpeta tipo de carpeta
     * @param clase       clase de la carpeta
     * @param props       objeto de propiedades
     * @param codOrg      codigo
     */
    private void llenarPropiedadesCarpeta(String tipoCarpeta, String clase, Map<String, Object> props, String codOrg, Carpeta folderFather, String idOrgOfc) {

        props.put(PropertyIds.OBJECT_TYPE_ID, "F:cmcor:" + configuracion.getPropiedad(clase));
        props.put(PropertyIds.DESCRIPTION, configuracion.getPropiedad(clase));
        props.put(tipoCarpeta, codOrg);

        if (CMCOR_SS_CODIGO.equals(tipoCarpeta)) {
            if (folderFather != null) {
                props.put(CMCOR_DEP_CODIGO_UAP, folderFather.getFolder().getPropertyValue(CMCOR_SER_CODIGO));
                props.put(CMCOR_SER_CODIGO, folderFather.getFolder().getPropertyValue(CMCOR_SER_CODIGO));
                props.put(CMCOR_DEP_CODIGO, idOrgOfc);
            }
        } else if (CMCOR_SER_CODIGO.equals(tipoCarpeta)) {
            if (folderFather != null) {
                props.put(CMCOR_DEP_CODIGO_UAP, folderFather.getFolder().getPropertyValue(CMCOR_DEP_CODIGO));
                props.put(CMCOR_DEP_CODIGO, idOrgOfc);
            }
        } else {
            props.put(CMCOR_DEP_CODIGO_UAP, codOrg);
            props.put(CMCOR_DEP_CODIGO, codOrg);
        }
    }

    /**
     * Metodo para llenar propiedades para crear carpeta
     *
     * @param clase  clase de la carpeta
     * @param props  objeto de propiedades
     * @param codOrg codigo
     */
    private void llenarPropiedadesCarpeta(String clase, Map<String, Object> props, String codOrg) {
        props.put(PropertyIds.OBJECT_TYPE_ID, "F:cmcor:" + configuracion.getPropiedad(clase));
        props.put(PropertyIds.DESCRIPTION, configuracion.getPropiedad(clase));
        props.put(CMCOR_UB_CODIGO, codOrg);
    }

    /**
     * Eliminar documento del Alfresco
     *
     * @param idDoc   Identificador del documento a borrar
     * @param session Objeto de conexion al Alfresco
     * @return Retorna true si borró con exito y false si no
     */
    @Override
    public boolean eliminardocumento(String idDoc, Session session) {
        try {

            logger.info("Se buscan los documentos Anexos al documento que se va a borrar");
            DocumentoDTO documento = new DocumentoDTO();
            documento.setIdDocumento(idDoc);
            ItemIterable<QueryResult> resultsPrincipalAdjunto = getPrincipalAdjuntosQueryResults(session, documento);

            for (QueryResult qResult : resultsPrincipalAdjunto) {

                String[] parts = qResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID).toString().split(";");
                String idDocumento = parts[0];

                logger.info("Se procede a eliminar el documento: " + qResult.getPropertyByQueryName(PropertyIds.NAME).getValues().get(0).toString());
                ObjectId a = new ObjectIdImpl(idDocumento);
                CmisObject object = session.getObject(a);
                Document delDoc = (Document) object;
                //Se borra el documento pero no todas las versiones solo la ultima
                delDoc.delete(false);
                logger.info("Se logro eliminar el documento");
            }
            return Boolean.TRUE;


        } catch (CmisObjectNotFoundException e) {
            logger.error("No se pudo eliminar el documento :", e);
            return Boolean.FALSE;
        }
    }

    /**
     * Convierte la Interfaz Folder de opencemis a una UnidadDocumentalDTO
     *
     * @param folder Objeto a transformar
     * @return UnidadDocumentalDTO
     */
    private UnidadDocumentalDTO transformarUnidadDocumental(Folder folder) {

        final UnidadDocumentalDTO unidadDocumentalDTO = new UnidadDocumentalDTO();
        {
            unidadDocumentalDTO.setId(folder.getPropertyValue(CMCOR_UD_ID));
            unidadDocumentalDTO.setAccion(folder.getPropertyValue(CMCOR_UD_ACCION));
            unidadDocumentalDTO.setDescriptor2(folder.getPropertyValue(CMCOR_UD_DESCRIPTOR_2));
            unidadDocumentalDTO.setCerrada(folder.getPropertyValue(CMCOR_UD_CERRADA));
            unidadDocumentalDTO.setFechaCierre(folder.getPropertyValue(CMCOR_UD_FECHA_CIERRE));
            unidadDocumentalDTO.setFechaExtremaInicial(folder.getPropertyValue(CMCOR_UD_FECHA_INICIAL));
            unidadDocumentalDTO.setFechaExtremaFinal(folder.getPropertyValue(CMCOR_UD_FECHA_FINAL));
            unidadDocumentalDTO.setSoporte(folder.getPropertyValue(CMCOR_UD_SOPORTE));
            unidadDocumentalDTO.setInactivo(folder.getPropertyValue(CMCOR_UD_INACTIVO));
            unidadDocumentalDTO.setUbicacionTopografica(folder.getPropertyValue(CMCOR_UD_UBICACION_TOPOGRAFICA));
            unidadDocumentalDTO.setFaseArchivo(folder.getPropertyValue(CMCOR_UD_FASE_ARCHIVO));
            unidadDocumentalDTO.setDescriptor1(folder.getPropertyValue(CMCOR_UD_DESCRIPTOR_1));
            unidadDocumentalDTO.setCodigoUnidadDocumental(folder.getPropertyValue(CMCOR_UD_CODIGO));
            unidadDocumentalDTO.setCodigoSerie(folder.getPropertyValue(CMCOR_SER_CODIGO));
            unidadDocumentalDTO.setCodigoSubSerie(folder.getPropertyValue(CMCOR_SS_CODIGO));
            unidadDocumentalDTO.setCodigoDependencia(folder.getPropertyValue(CMCOR_DEP_CODIGO));
            unidadDocumentalDTO.setNombreUnidadDocumental(folder.getPropertyValue(PropertyIds.NAME));
            unidadDocumentalDTO.setObservaciones(folder.getPropertyValue(CMCOR_UD_OBSERVACIONES));
            unidadDocumentalDTO.setEstado(folder.getPropertyValue(CMCOR_UD_ESTADO));
            unidadDocumentalDTO.setDisposicion(folder.getPropertyValue(CMCOR_UD_DISPOSICION));
        }
        return unidadDocumentalDTO;
    }

    /**
     * Convierte la Interfaz Document de opencemis a un DocumentoDTO
     *
     * @param document Objeto a transformar
     * @return DocumentoDTO
     */
    private DocumentoDTO transformarDocumento(Document document) {

        DocumentoDTO documentoDTO = new DocumentoDTO();
        String idDoc = document.getId();
        documentoDTO.setIdDocumento(idDoc.contains(";") ? idDoc.substring(0, idDoc.indexOf(";")) : idDoc);
        documentoDTO.setTipoDocumento(document.getPropertyValue(CMCOR_TIPO_DOCUMENTO));
        documentoDTO.setNroRadicado(document.getPropertyValue(CMCOR_NRO_RADICADO));
        documentoDTO.setTipologiaDocumental(document.getPropertyValue(CMCOR_TIPOLOGIA_DOCUMENTAL));
        documentoDTO.setNombreRemitente(document.getPropertyValue(CMCOR_NOMBRE_REMITENTE));
        documentoDTO.setNombreDocumento(document.getName());
        documentoDTO.setTamano(document.getContentStreamLength() + "");
        final String nroRadicado = document.getPropertyValue(CMCOR_NUMERO_REFERIDO);

        if (!ObjectUtils.isEmpty(nroRadicado)) {
            String[] splitRadicado = nroRadicado.split(SEPARADOR);
            documentoDTO.setNroRadicadoReferido(splitRadicado);
        }
        return documentoDTO;
    }

    public Map<String, Object> obtenerPropiedadesDocumento(Document document) {
        final Map<String, Object> map = new HashMap<>();
        map.put(PropertyIds.NAME, document.getName());
        map.put(PropertyIds.OBJECT_TYPE_ID, "D:cmcor:CM_DocumentoPersonalizado");
        map.put(PropertyIds.OBJECT_ID, document.getId().split(";")[0]);
        map.put(CMCOR_ID_DOC_PRINCIPAL, document.getPropertyValue(CMCOR_ID_DOC_PRINCIPAL));
        map.put(CMCOR_NRO_RADICADO, document.getPropertyValue(CMCOR_NRO_RADICADO));
        map.put(CMCOR_NOMBRE_REMITENTE, document.getPropertyValue(CMCOR_NOMBRE_REMITENTE));
        map.put(CMCOR_TIPO_DOCUMENTO, document.getPropertyValue(CMCOR_TIPO_DOCUMENTO));
        map.put(CMCOR_NUMERO_REFERIDO, document.getPropertyValue(CMCOR_NUMERO_REFERIDO));
        return map;
    }

    @Override
    public void subirDocumentosCMISPrincipalAnexoUD(Folder folder, List<Document> documentos) {

        /*documentos.forEach(document -> {
            ContentStream contentStream = document.getContentStream();
            Map<String, Object> properties = obtenerPropiedadesDocumento(document);
            document.delete();
            folder.createDocument(properties, contentStream, VersioningState.MAJOR);
        });*/
        for (Document document :
                documentos) {
            ContentStream contentStream = document.getContentStream();
            Map<String, Object> properties = obtenerPropiedadesDocumento(document);
            document.delete();
            folder.createDocument(properties, contentStream, VersioningState.MAJOR);
        }
    }
}