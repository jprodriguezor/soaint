package co.com.soaint.ecm.business.boundary.documentmanager;

import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Configuracion;
import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Utilities;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentControl;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.IRecordServices;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.impl.RecordServices;
import co.com.soaint.ecm.domain.entity.*;
import co.com.soaint.ecm.util.ConstantesECM;
import co.com.soaint.ecm.util.SystemParameters;
import co.com.soaint.foundation.canonical.ecm.*;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by Dasiel
 */
@Log4j2
@BusinessControl
public class ContentControlAlfresco implements ContentControl {

    private static final long serialVersionUID = 1L;

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
    private Optional<File> convertInputStreamToFile(ContentStream contentStream) throws SystemException {
        try {
            File file = File.createTempFile(contentStream.getFileName(), "");
            try (InputStream inputStream = contentStream.getStream(); OutputStream out = new FileOutputStream(file)) {
                int read;
                byte[] bytes = new byte[1024];
                while ((read = inputStream.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
            } catch (IOException e) {
                log.error("Error al convertir el contentStream a File", e);
            }
            file.deleteOnExit();
            return Optional.of(file);
        } catch (Exception e) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    /**
     * Metodo que obtiene el objeto de conexion que produce Alfresco en conexion
     *
     * @return Objeto de tipo Conexion en este caso devuevle un objeto Session
     */
    @Override
    public Conexion obtenerConexion() {
        Conexion conexion = new Conexion();

        log.info("*** obtenerConexion ***");

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
            log.error("*** Error al obtener conexion *** ", e);
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

            log.info("### Creando Carpeta.. con clase documental:" + classDocumental);
            Map<String, Object> props = new HashMap<>();
            //Se define como nombre de la carpeta nameOrg
            props.put(PropertyIds.NAME, nameOrg);

            //Se estable como codigo unidad administrativa Padre el codigo de la carpeta padre
            switch (classDocumental) {
                case ConstantesECM.CLASE_BASE:
                    llenarPropiedadesCarpeta(ConstantesECM.CLASE_BASE, props, codOrg);
                    break;
                case ConstantesECM.CLASE_DEPENDENCIA:
                    llenarPropiedadesCarpeta(ConstantesECM.CMCOR_DEP_CODIGO, ConstantesECM.CLASE_DEPENDENCIA, props, codOrg, folderFather, idOrgOfc);
                    break;
                case ConstantesECM.CLASE_SERIE:
                    llenarPropiedadesCarpeta(ConstantesECM.CMCOR_SER_CODIGO, ConstantesECM.CLASE_SERIE, props, codOrg, folderFather, idOrgOfc);
                    break;
                case ConstantesECM.CLASE_SUBSERIE:
                    llenarPropiedadesCarpeta(ConstantesECM.CMCOR_SS_CODIGO, ConstantesECM.CLASE_SUBSERIE, props, codOrg, folderFather, idOrgOfc);
                    break;
                case ConstantesECM.CLASE_UNIDAD_DOCUMENTAL:

                    final Folder tmpFolder = folderFather.getFolder();

                    final String depCodeValue = tmpFolder.getPropertyValue(ConstantesECM.CMCOR_DEP_CODIGO);
                    String depCode = !ObjectUtils.isEmpty(depCodeValue) ? depCodeValue : "";

                    final String serieCodeValue = tmpFolder.getPropertyValue(ConstantesECM.CMCOR_SER_CODIGO);
                    String serieCode = !ObjectUtils.isEmpty(serieCodeValue) ? serieCodeValue : "";

                    final String subSerieCodeValue = tmpFolder.getPropertyValue(ConstantesECM.CMCOR_SS_CODIGO);
                    String subSerieCode = !ObjectUtils.isEmpty(subSerieCodeValue) ? subSerieCodeValue : "";

                    final String codUnidadAdminPadreValue = tmpFolder.getPropertyValue(ConstantesECM.CMCOR_DEP_CODIGO_UAP);
                    String codUnidadAdminPadre = !ObjectUtils.isEmpty(codUnidadAdminPadreValue) ? codUnidadAdminPadreValue : "";

                    props.put(ConstantesECM.CMCOR_UD_ID, "");
                    props.put(ConstantesECM.CMCOR_UD_ACCION, "");
                    props.put(ConstantesECM.CMCOR_UD_DESCRIPTOR_2, "");
                    props.put(ConstantesECM.CMCOR_UD_CERRADA, false);
                    props.put(ConstantesECM.CMCOR_UD_SOPORTE, "");
                    props.put(ConstantesECM.CMCOR_UD_INACTIVO, false);
                    props.put(ConstantesECM.CMCOR_UD_UBICACION_TOPOGRAFICA, "");
                    props.put(ConstantesECM.CMCOR_UD_FASE_ARCHIVO, "");
                    props.put(ConstantesECM.CMCOR_UD_DESCRIPTOR_1, "");
                    props.put(ConstantesECM.CMCOR_UD_CODIGO, "");
                    props.put(ConstantesECM.CMCOR_DEP_CODIGO_UAP, !"".equals(subSerieCode) ? subSerieCode :
                            !"".equals(serieCode) ? serieCode :
                                    !"".equals(depCode) ? depCode : codUnidadAdminPadre);
                    props.put(ConstantesECM.CMCOR_DEP_CODIGO, depCode);
                    props.put(ConstantesECM.CMCOR_SER_CODIGO, serieCode);
                    props.put(ConstantesECM.CMCOR_SS_CODIGO, subSerieCode);
                    props.put(PropertyIds.DESCRIPTION, configuracion.getPropiedad(ConstantesECM.CLASE_UNIDAD_DOCUMENTAL));
                    props.put(PropertyIds.OBJECT_TYPE_ID, "F:cmcor:" + configuracion.getPropiedad(ConstantesECM.CLASE_UNIDAD_DOCUMENTAL));
                    break;
                default:
                    break;
            }
            //Se crea la carpeta dentro de la carpeta folder
            log.info("*** Se procede a crear la carpeta ***");
            newFolder = new Carpeta();
            newFolder.setFolder(folder.getFolder().createFolder(props));
            log.info("---------------------Carpeta: " + newFolder.getFolder().getName() + " creada--------------");
        } catch (Exception e) {
            log.error("*** Error al crear carpeta *** ", e);
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
                    log.info("El formato no cumple con los requerimientos.");
                    formatoFinal = new StringBuilder();
                    break;
                } else {
                    formatoFinal.append(aFormatoCadenaArray);
                }
            }
        } catch (Exception e) {
            log.error("*** Error al formatear nombre *** ", e);
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
    public MensajeRespuesta descargarDocumento(DocumentoDTO documentoDTO, Session session) throws SystemException {
        log.info(documentoDTO.toString());
        ArrayList<DocumentoDTO> versionesLista = new ArrayList<>();
        ArrayList<DocumentoDTO> documento = new ArrayList<>();
        DocumentoDTO documentoDTO1 = new DocumentoDTO();
        MensajeRespuesta mensajeRespuesta = new MensajeRespuesta();
        try {
            log.info("Se entra al metodo de descargar el documento");
            Document doc = (Document) session.getObject(documentoDTO.getIdDocumento());
            Optional<File> optionalFile = Optional.empty();

            if (documentoDTO.getVersionLabel() != null) {
                List<Document> versions = doc.getAllVersions();
                //Filtrar la version correcta dentro de las versiones del documento para obtener el file
                Optional<Document> version = versions.stream()
                        .filter(p -> p.getVersionLabel().equals(documentoDTO.getVersionLabel())).findFirst();
                if (version.isPresent()) {
                    optionalFile = getFile(documentoDTO, versionesLista, version.get());
                }

            } else {
                optionalFile = convertInputStreamToFile(doc.getContentStream());
            }
            log.info("Se procede a devolver el documento" + documentoDTO.getNombreDocumento());
            if (optionalFile.isPresent()) {
                byte[] data = FileUtils.readFileToByteArray(optionalFile.get());
                documentoDTO1.setDocumento(data);

                mensajeRespuesta.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
                mensajeRespuesta.setMensaje("Documento Retornado con exito");
                documento.add(documentoDTO1);
                mensajeRespuesta.setDocumentoDTOList(documento);
                return mensajeRespuesta;
            }
            throw new SystemException("Ocurrio un error al descargar el documento");

        } catch (Exception e) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
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
    private Optional<File> getFile(DocumentoDTO documentoDTO, ArrayList<DocumentoDTO> versionesLista, Document version) throws SystemException {
        if (version.getVersionLabel().equals(documentoDTO.getVersionLabel())) {
            documentoDTO.setNombreDocumento(version.getName());
            versionesLista.add(documentoDTO);
            return convertInputStreamToFile(version.getContentStream());
        }
        return Optional.empty();
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
            log.error("*** Error al obtenerCarpetas *** ", e);
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
    public MensajeRespuesta devolverSerieSubSerie(ContenidoDependenciaTrdDTO dependenciaTrdDTO, Session session) throws SystemException {
        MensajeRespuesta respuesta = new MensajeRespuesta();
        try {
            if (StringUtils.isEmpty(dependenciaTrdDTO.getIdOrgOfc())) {
                log.error("Oopss... No se ha especificado el codigo de la dependencia");
                throw new BusinessException("No se ha especificado el codigo de la dependencia");
            }
            String queryString = "SELECT * FROM cmcor:CM_Serie WHERE " + ConstantesECM.CMCOR_DEP_CODIGO + " LIKE '" + dependenciaTrdDTO.getIdOrgOfc() + "%'";
            queryString += (!StringUtils.isEmpty(dependenciaTrdDTO.getCodSerie())) ?
                    " AND " + ConstantesECM.CMCOR_SER_CODIGO + " LIKE '" + dependenciaTrdDTO.getCodSerie() + "%'" : "";
            final Optional<ContenidoDependenciaTrdDTO> optionalTrdDTO = getDependenciaTrdDTO(queryString, session);
            if (optionalTrdDTO.isPresent()) {
                dependenciaTrdDTO = optionalTrdDTO.get();
                respuesta.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
                respuesta.setMensaje("Series o Subseries devueltas correctamente");
                List<ContenidoDependenciaTrdDTO> listaSerieSubSerie = new ArrayList<>();
                listaSerieSubSerie.add(listaSerieSubSerie.size(), dependenciaTrdDTO);
                respuesta.setContenidoDependenciaTrdDTOS(listaSerieSubSerie);
            } else {
                respuesta.setCodMensaje("1111");
                respuesta.setMensaje("No se obtuvieron Series o Subseries por no existir para esta dependencia");
            }
        } catch (Exception e) {
            log.error("*** Error al obtener las series o subseries *** ", e);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
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
    public MensajeRespuesta crearUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO, Session session) throws SystemException {
        log.info("Executing crearUnidadDocumental method");

        final MensajeRespuesta response = new MensajeRespuesta();
        response.setResponse(new HashMap<>());

        Optional<Folder> optionalFolder = crearUnidadDocumentalFolder(unidadDocumentalDTO, session);
        if (!optionalFolder.isPresent()) {
            log.error(ConstantesECM.NO_RESULT_MATCH);
            throw new SystemException(ConstantesECM.NO_RESULT_MATCH);
        }
        response.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
        response.setMensaje(ConstantesECM.OPERACION_COMPLETADA_SATISFACTORIAMENTE);
        response.getResponse().put("unidadDocumental", transformarUnidadDocumental(optionalFolder.get()));
        return response;
    }

    /**
     * Listar las Unidades Documentales del ECM
     *
     * @return Mensaje de respuesta
     */
    @Override
    public MensajeRespuesta listarUnidadDocumental(final UnidadDocumentalDTO dto,
                                                   final Session session) throws SystemException {
        final MensajeRespuesta respuesta = new MensajeRespuesta();
        final List<UnidadDocumentalDTO> unidadDocumentalDTOS = listarUnidadesDocumentales(new ArrayList<>(), dto, session);
        final String messaje = unidadDocumentalDTOS.isEmpty() ? "El sistema no encuentra la unidad documental que está " +
                "buscando. Por favor, solicite su creación" : "Listado seleccionado correctamente";
        respuesta.setMensaje(messaje);
        respuesta.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
        Map<String, Object> map = new HashMap<>();
        map.put("unidadDocumental", unidadDocumentalDTOS);
        respuesta.setResponse(map);
        return respuesta;
    }

    private List<UnidadDocumentalDTO> listarUnidadesDocumentales(List<String> disposicionList, UnidadDocumentalDTO dto, Session session) throws SystemException {
        try {
            String query = "SELECT * FROM " + ConstantesECM.CMCOR + configuracion.getPropiedad(ConstantesECM.CLASE_UNIDAD_DOCUMENTAL);
            if (!StringUtils.isEmpty(dto.getCodigoDependencia()) || !StringUtils.isEmpty(dto.getCodigoSede())) {
                query += " WHERE " + ConstantesECM.CMCOR_DEP_CODIGO + " LIKE '" + dto.getCodigoDependencia() + "%'";
            }
            final String where = "WHERE";
            if (!StringUtils.isEmpty(dto.getCodigoSerie())) {
                query += (!query.contains(where) ? " WHERE " : " AND ") + ConstantesECM.CMCOR_SER_CODIGO + " LIKE '" + dto.getCodigoSerie() + "%'";
            }
            if (!StringUtils.isEmpty(dto.getCodigoSubSerie())) {
                query += (!query.contains(where) ? " WHERE " : " AND ") + ConstantesECM.CMCOR_SS_CODIGO + " LIKE '" + dto.getCodigoSubSerie() + "%'";
            }
            if (!StringUtils.isEmpty(dto.getDescriptor1())) {
                query += (!query.contains(where) ? " WHERE " : " AND ") + ConstantesECM.CMCOR_UD_DESCRIPTOR_1 + " LIKE '%" + dto.getDescriptor1() + "%'";
            }
            if (!StringUtils.isEmpty(dto.getDescriptor2())) {
                query += (!query.contains(where) ? " WHERE " : " AND ") + ConstantesECM.CMCOR_UD_DESCRIPTOR_2 + " LIKE '%" + dto.getDescriptor2() + "%'";
            }
            if (!StringUtils.isEmpty(dto.getNombreUnidadDocumental())) {
                query += (!query.contains(where) ? " WHERE " : " AND ") + PropertyIds.NAME + " LIKE '%" + dto.getNombreUnidadDocumental() + "%'";
            }
            if (!StringUtils.isEmpty(dto.getId())) {
                query += (!query.contains(where) ? " WHERE " : " AND ") + ConstantesECM.CMCOR_UD_ID + " LIKE '%" + dto.getId() + "%'";
            }
            if (!ObjectUtils.isEmpty(dto.getSoporte())) {
                query += (!query.contains(where) ? " WHERE " : " AND ") + ConstantesECM.CMCOR_UD_SOPORTE + " LIKE '" + dto.getSoporte() + "%'";
            }
            if (!StringUtils.isEmpty(dto.getAccion())) {
                AccionUsuario accionUsuario = AccionUsuario.valueOf(dto.getAccion().toUpperCase());
                return listarUnidadesDocumentalesPorAccion(accionUsuario, query, session);
            }
            if (!ObjectUtils.isEmpty(dto.getInactivo())) {
                query += (!query.contains(where) ? " WHERE " : " AND ") + ConstantesECM.CMCOR_UD_INACTIVO + " = '" + dto.getInactivo() + "'";
            }
            if (!ObjectUtils.isEmpty(dto.getCerrada())) {
                query += (!query.contains(where) ? " WHERE " : " AND ") + ConstantesECM.CMCOR_UD_CERRADA + " = '" + dto.getCerrada() + "'";
            }
            if (!ObjectUtils.isEmpty(dto.getEstado())) {
                query += (!query.contains(where) ? " WHERE " : " AND ") + ConstantesECM.CMCOR_UD_ESTADO + " = '" + dto.getEstado() + "'";
            }
            if (!ObjectUtils.isEmpty(disposicionList)) {
                final int lenght = disposicionList.size();
                final StringBuilder inSentence = new StringBuilder();
                for (int i = 0; i < lenght; i++) {
                    final String disposition = disposicionList.get(i).toLowerCase();
                    final String comma = (i == lenght - 1) ? "" : ";";
                    inSentence.append("'").append(disposition).append("'").append(comma);
                }
                query += (!query.contains(where) ? " WHERE " : " AND ") + ConstantesECM.CMCOR_UD_DISPOSICION + " IN (" + inSentence.toString() + ")";
                query += " AND " + ConstantesECM.CMCOR_UD_INACTIVO + " = 'true'" +
                        " AND " + ConstantesECM.CMCOR_UD_FASE_ARCHIVO + " = '" + PhaseType.ARCHIVO_CENTRAL.getTexto() + "'";
                log.info("Ejecutar consulta {}", query);
            }
            return listarUnidadesDocumentales(query, dto, session);

        } catch (Exception e) {
            log.error("Error al Listar las Unidades Documentales");
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    private List<UnidadDocumentalDTO> listarUnidadesDocumentales(String query, UnidadDocumentalDTO dto, Session session) {
        log.info("Executing query {}", query);
        final ItemIterable<QueryResult> queryResults = session.query(query, false);
        final List<UnidadDocumentalDTO> unidadDocumentalDTOS = new ArrayList<>();
        for (QueryResult queryResult :
                queryResults) {
            final String idUnidadDocumental = queryResult.getPropertyValueByQueryName(ConstantesECM.CMCOR_UD_ID);
            if (!StringUtils.isEmpty(idUnidadDocumental) && !idUnidadDocumental.trim().isEmpty()) {
                final String objectId = queryResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
                final Folder folder = (Folder) session.getObject(session.getObject(objectId));
                if (ObjectUtils.isEmpty(dto) || hasDatesInRange(folder, dto)) {
                    unidadDocumentalDTOS.add(unidadDocumentalDTOS.size(), transformarUnidadDocumental(folder));
                }
            }
        }
        return unidadDocumentalDTOS;
    }

    /**
     * Listar las Unidades Documentales del ECM
     *
     * @param accionUsuario accion a realizar(abrir, cerrar, reactivar)
     * @param query         una previa consulta de seleccion de valores del dto UD
     * @param session       Obj coneccion de Alfresco
     * @return List<UnidadDocumentalDTO> Lst
     */
    private List<UnidadDocumentalDTO> listarUnidadesDocumentalesPorAccion(AccionUsuario accionUsuario, String query, Session session) throws BusinessException {
        try {
            query += (!query.contains("WHERE") ? " WHERE " : " AND ") + ConstantesECM.CMCOR_UD_FECHA_INICIAL + " IS NOT NULL";
            switch (accionUsuario) {
                case ABRIR:
                case REACTIVAR:
                    query += " AND " + ConstantesECM.CMCOR_UD_CERRADA + " = 'true' AND " + ConstantesECM.CMCOR_UD_FECHA_FINAL + " IS NOT NULL" +
                            " AND " + ConstantesECM.CMCOR_UD_FECHA_CIERRE + " IS NOT NULL AND " + ConstantesECM.CMCOR_UD_INACTIVO + " = 'true'";
                    break;
                case CERRAR:
                    query += " AND " + ConstantesECM.CMCOR_UD_CERRADA + " = 'false' AND " + ConstantesECM.CMCOR_UD_INACTIVO + " = 'false'";
                    break;
            }
            final List<UnidadDocumentalDTO> unidadDocumentalDTOS = listarUnidadesDocumentales(query, null, session);
            final List<UnidadDocumentalDTO> udTmp = new ArrayList<>();
            unidadDocumentalDTOS.forEach(unidadDocumentalDTO -> {
                final String soporte = unidadDocumentalDTO.getSoporte();
                if (!StringUtils.isEmpty(soporte) && !soporte.trim().isEmpty()) {
                    udTmp.add(udTmp.size(), unidadDocumentalDTO);
                }
            });
            return udTmp;

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
    public MensajeRespuesta obtenerDetallesDocumentoDTO(String idDocumento, Session session) throws SystemException {
        log.info("Ejecutando el metodo MensajeRespuesta obtenerDetallesDocumentoDTO(String idDocumento, Session session)");

        final MensajeRespuesta mensajeRespuesta = new MensajeRespuesta();

        if (ObjectUtils.isEmpty(idDocumento)) {
            log.info("El ID del documento esta vacio");
            mensajeRespuesta.setMensaje("El ID del documento esta vacio");
            mensajeRespuesta.setCodMensaje("11111");
            return mensajeRespuesta;
        }

        try {
            CmisObject cmisObjectDocument = session.getObject(session.getObject(idDocumento));

            if (!(cmisObjectDocument instanceof Document)) {
                log.info(ConstantesECM.NO_RESULT_MATCH);
                mensajeRespuesta.setMensaje(ConstantesECM.NO_RESULT_MATCH);
                mensajeRespuesta.setCodMensaje("11111");
                return mensajeRespuesta;
            }

            Map<String, Object> mapResponsonse = new HashMap<>();
            mapResponsonse.put("documentoDTO", transformarDocumento((Document) cmisObjectDocument));
            mensajeRespuesta.setMensaje("Documento devuelto correctamente");
            mensajeRespuesta.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
            mensajeRespuesta.setResponse(mapResponsonse);

            return mensajeRespuesta;
        } catch (CmisObjectNotFoundException ex) {
            log.error("Error al buscar el documento {}", ex.getMessage());
            throw new SystemException(ConstantesECM.NO_RESULT_MATCH);
        } catch (Exception e) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    /**
     * Metodo para devolver la Unidad Documental
     *
     * @param idUnidadDocumental Id Unidad Documental
     * @return MensajeRespuesta      Unidad Documntal
     */
    @Override
    public MensajeRespuesta detallesUnidadDocumental(String idUnidadDocumental, Session session) throws SystemException {
        log.info("Ejecutando el metodo detallesUnidadDocumental(String idUnidadDocumental, Session session)");

        final Optional<Folder> optionalFolder = getUDFolderById(idUnidadDocumental, session);
        if (optionalFolder.isPresent()) {
            MensajeRespuesta respuesta = new MensajeRespuesta();
            respuesta.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
            respuesta.setMensaje("Detalles Unidad Documental");
            respuesta.setResponse(new HashMap<>());
            respuesta.getResponse().put("unidadDocumental", transformarUnidadDocumental(optionalFolder.get()));
            return respuesta;
        }
        throw new SystemException("Unidad Documental no encontrada con ID: '" + idUnidadDocumental + "'");
    }

    /**
     * Metodo que busca una Unidad Documental en el ECM
     *
     * @param idUnidadDocumental Id de la Unidad Documental
     * @param session            Objeto conexion de Alfresco
     * @return UnidadDocumentalDTO si existe, null si no existe
     */
    @Override
    public Optional<UnidadDocumentalDTO> getUDById(String idUnidadDocumental, boolean fullDocuments, Session session) {
        log.info("Ejecutando UnidadDocumentalDTO listarDocsDadoIdUD(String idUnidadDocumental, Session session)");

        Optional<Folder> optionalFolder = getUDFolderById(idUnidadDocumental, session);
        try {
            if (optionalFolder.isPresent()) {
                Folder unidadDocumentalFolder = optionalFolder.get();
                UnidadDocumentalDTO documentalDTO = transformarUnidadDocumental(unidadDocumentalFolder);
                List<DocumentoDTO> documentoDTOS = getDocumentsFromFolder(unidadDocumentalFolder);
                if (!fullDocuments) {
                    optionalFolder = recordServices.obtenerRecordFolder(idUnidadDocumental);
                    List<DocumentoDTO> documentosRecord = optionalFolder.isPresent() ?
                            getDocumentsFromFolder(optionalFolder.get()) : new ArrayList<>();
                    for (DocumentoDTO documentoDTO :
                            documentosRecord) {
                        documentoDTOS.remove(documentoDTO);
                    }
                }
                documentalDTO.setListaDocumentos(documentoDTOS);
                return Optional.of(documentalDTO);
            }
        } catch (Exception e) {
            log.error("Error ocurrido: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Folder> getUDFolderById(String idUnidadDocumental, Session session) {
        log.info("Ejecutando Folder getUDFolderById(String idProperty, Session session)");
        Optional<Folder> response = Optional.empty();
        if (!ObjectUtils.isEmpty(idUnidadDocumental)) {
            final String queryString = "SELECT * FROM " + ConstantesECM.CMCOR + configuracion.getPropiedad(ConstantesECM.CLASE_UNIDAD_DOCUMENTAL) +
                    " WHERE " + ConstantesECM.CMCOR_UD_ID + " = '" + idUnidadDocumental + "'";
            try {
                final ItemIterable<QueryResult> queryResults = session.query(queryString, false);
                final Iterator<QueryResult> iterator = queryResults.iterator();
                if (iterator.hasNext()) {
                    QueryResult queryResult = iterator.next();
                    String objectId = queryResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
                    Folder folder = (Folder) session.getObject(session.getObject(objectId));
                    response = ObjectUtils.isEmpty(folder) ? Optional.empty() : Optional.of(folder);
                }
            } catch (Exception ex) {
                log.error("Ocurrio un error en el Servidor");
                return Optional.empty();
            }
        }
        return response;
    }

    @Override
    public boolean actualizarUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO, Session session) throws SystemException {

        Optional<Folder> optionalFolder = getUDFolderById(unidadDocumentalDTO.getId(), session);
        boolean response = false;
        if (optionalFolder.isPresent()) {
            Folder unidadDocumentalFolder = optionalFolder.get();
            Map<String, Object> stringObjectMap = updateProperties(unidadDocumentalFolder, unidadDocumentalDTO);
            response = !stringObjectMap.isEmpty();
        }
        return response;
    }

    private Map<String, Object> updateProperties(Folder folder, UnidadDocumentalDTO unidadDocumentalDTO) throws SystemException {
        log.info("Creating props to make the folder");
        final Map<String, Object> props = new HashMap<>();
        boolean isEmptyFolder = ObjectUtils.isEmpty(folder);

        props.put(ConstantesECM.CMCOR_UD_ID, unidadDocumentalDTO.getId());
        props.put(PropertyIds.DESCRIPTION, configuracion.getPropiedad(ConstantesECM.CLASE_UNIDAD_DOCUMENTAL));

        String nameFolder = !ObjectUtils.isEmpty(unidadDocumentalDTO.getNombreUnidadDocumental()) ?
                Utilities.reemplazarCaracteresRaros(unidadDocumentalDTO.getNombreUnidadDocumental()) : "";
        props.put(PropertyIds.NAME, ((!isEmptyFolder && !ObjectUtils.isEmpty(nameFolder)) || isEmptyFolder) ?
                nameFolder : folder.getPropertyValue(PropertyIds.NAME));

        String accion = !ObjectUtils.isEmpty(unidadDocumentalDTO.getAccion()) ? unidadDocumentalDTO.getAccion() : "";
        props.put(ConstantesECM.CMCOR_UD_ACCION, (!isEmptyFolder && !ObjectUtils.isEmpty(accion) || isEmptyFolder) ?
                accion : folder.getPropertyValue(ConstantesECM.CMCOR_UD_ACCION));

        Calendar fechaInicial = !ObjectUtils.isEmpty(unidadDocumentalDTO.getFechaExtremaInicial()) ? unidadDocumentalDTO.getFechaExtremaInicial() : null;
        props.put(ConstantesECM.CMCOR_UD_FECHA_INICIAL, (!isEmptyFolder && !ObjectUtils.isEmpty(fechaInicial) || isEmptyFolder) ?
                fechaInicial : folder.getPropertyValue(ConstantesECM.CMCOR_UD_FECHA_INICIAL));

        Boolean inactivo = !ObjectUtils.isEmpty(unidadDocumentalDTO.getInactivo()) ? unidadDocumentalDTO.getInactivo() : null;
        props.put(ConstantesECM.CMCOR_UD_INACTIVO, (!isEmptyFolder && !ObjectUtils.isEmpty(inactivo) || isEmptyFolder) ?
                inactivo : folder.getPropertyValue(ConstantesECM.CMCOR_UD_INACTIVO));

        String ubicacionTopografica = !ObjectUtils.isEmpty(unidadDocumentalDTO.getUbicacionTopografica()) ? unidadDocumentalDTO.getUbicacionTopografica() : "";
        props.put(ConstantesECM.CMCOR_UD_UBICACION_TOPOGRAFICA, (!isEmptyFolder && !ObjectUtils.isEmpty(ubicacionTopografica) || isEmptyFolder) ?
                ubicacionTopografica : folder.getPropertyValue(ConstantesECM.CMCOR_UD_UBICACION_TOPOGRAFICA));

        Calendar fechaFinal = !ObjectUtils.isEmpty(unidadDocumentalDTO.getFechaExtremaFinal()) ? unidadDocumentalDTO.getFechaExtremaFinal() : null;
        props.put(ConstantesECM.CMCOR_UD_FECHA_FINAL, (!isEmptyFolder && !ObjectUtils.isEmpty(fechaFinal) || isEmptyFolder) ?
                fechaFinal : folder.getPropertyValue(ConstantesECM.CMCOR_UD_FECHA_FINAL));

        Calendar fechaCierre = !ObjectUtils.isEmpty(unidadDocumentalDTO.getFechaCierre()) ? unidadDocumentalDTO.getFechaCierre() : null;
        props.put(ConstantesECM.CMCOR_UD_FECHA_CIERRE, (!isEmptyFolder && !ObjectUtils.isEmpty(fechaCierre) || isEmptyFolder) ?
                fechaCierre : folder.getPropertyValue(ConstantesECM.CMCOR_UD_FECHA_CIERRE));

        String estado = !ObjectUtils.isEmpty(unidadDocumentalDTO.getEstado()) ? unidadDocumentalDTO.getEstado() : "";
        props.put(ConstantesECM.CMCOR_UD_ESTADO, (!isEmptyFolder && !ObjectUtils.isEmpty(estado) || isEmptyFolder) ?
                estado : folder.getPropertyValue(ConstantesECM.CMCOR_UD_ESTADO));

        String faseArchivo = !ObjectUtils.isEmpty(unidadDocumentalDTO.getFaseArchivo()) ? unidadDocumentalDTO.getFaseArchivo() : "";
        props.put(ConstantesECM.CMCOR_UD_FASE_ARCHIVO, (!isEmptyFolder && !ObjectUtils.isEmpty(faseArchivo) || isEmptyFolder) ?
                faseArchivo : folder.getPropertyValue(ConstantesECM.CMCOR_UD_FASE_ARCHIVO));

        String soporte = !ObjectUtils.isEmpty(unidadDocumentalDTO.getSoporte()) ? unidadDocumentalDTO.getSoporte() : "";
        props.put(ConstantesECM.CMCOR_UD_SOPORTE, (!isEmptyFolder && !ObjectUtils.isEmpty(soporte) || isEmptyFolder) ?
                soporte : folder.getPropertyValue(ConstantesECM.CMCOR_UD_SOPORTE));

        String descriptor2 = !ObjectUtils.isEmpty(unidadDocumentalDTO.getDescriptor2()) ?
                Utilities.reemplazarCaracteresRaros(unidadDocumentalDTO.getDescriptor2()) : "";
        props.put(ConstantesECM.CMCOR_UD_DESCRIPTOR_2, (!isEmptyFolder && !ObjectUtils.isEmpty(descriptor2) || isEmptyFolder) ?
                descriptor2 : folder.getPropertyValue(ConstantesECM.CMCOR_UD_DESCRIPTOR_2));

        String observaciones = !ObjectUtils.isEmpty(unidadDocumentalDTO.getObservaciones()) ? unidadDocumentalDTO.getObservaciones() : "";
        props.put(ConstantesECM.CMCOR_UD_OBSERVACIONES, (!isEmptyFolder && !ObjectUtils.isEmpty(observaciones) || isEmptyFolder) ?
                observaciones : folder.getPropertyValue(ConstantesECM.CMCOR_UD_OBSERVACIONES));

        String disposicion = !ObjectUtils.isEmpty(unidadDocumentalDTO.getDisposicion()) ? unidadDocumentalDTO.getDisposicion() : "";
        props.put(ConstantesECM.CMCOR_UD_DISPOSICION, (!isEmptyFolder && !ObjectUtils.isEmpty(disposicion) || isEmptyFolder) ?
                disposicion : folder.getPropertyValue(ConstantesECM.CMCOR_UD_DISPOSICION));

        String descriptor1 = !ObjectUtils.isEmpty(unidadDocumentalDTO.getDescriptor1()) ?
                Utilities.reemplazarCaracteresRaros(unidadDocumentalDTO.getDescriptor1()) : "";
        props.put(ConstantesECM.CMCOR_UD_DESCRIPTOR_1, (!isEmptyFolder && !ObjectUtils.isEmpty(descriptor1) || isEmptyFolder) ?
                descriptor1 : folder.getPropertyValue(ConstantesECM.CMCOR_UD_DESCRIPTOR_1));

        Boolean cerrada = !ObjectUtils.isEmpty(unidadDocumentalDTO.getCerrada()) ? unidadDocumentalDTO.getCerrada() : null;
        props.put(ConstantesECM.CMCOR_UD_CERRADA, (!isEmptyFolder && !ObjectUtils.isEmpty(cerrada) || isEmptyFolder) ?
                cerrada : folder.getPropertyValue(ConstantesECM.CMCOR_UD_CERRADA));

        String udCodigo = !ObjectUtils.isEmpty(unidadDocumentalDTO.getCodigoUnidadDocumental()) ? unidadDocumentalDTO.getCodigoUnidadDocumental() : "";
        props.put(ConstantesECM.CMCOR_UD_CODIGO, (!isEmptyFolder && !ObjectUtils.isEmpty(udCodigo) || isEmptyFolder) ?
                udCodigo : folder.getPropertyValue(ConstantesECM.CMCOR_UD_CODIGO));

        String serCodigo = !ObjectUtils.isEmpty(unidadDocumentalDTO.getCodigoSerie()) ? unidadDocumentalDTO.getCodigoSerie() : "";
        props.put(ConstantesECM.CMCOR_SER_CODIGO, (!isEmptyFolder && !ObjectUtils.isEmpty(serCodigo) || isEmptyFolder) ?
                serCodigo : folder.getPropertyValue(ConstantesECM.CMCOR_SER_CODIGO));

        String ssCodigo = !ObjectUtils.isEmpty(unidadDocumentalDTO.getCodigoSubSerie()) ? unidadDocumentalDTO.getCodigoSubSerie() : "";
        props.put(ConstantesECM.CMCOR_SS_CODIGO, (!isEmptyFolder && !ObjectUtils.isEmpty(ssCodigo) || isEmptyFolder) ?
                ssCodigo : folder.getPropertyValue(ConstantesECM.CMCOR_SS_CODIGO));

        String depCodigo = !ObjectUtils.isEmpty(unidadDocumentalDTO.getCodigoDependencia()) ? unidadDocumentalDTO.getCodigoDependencia() : "";
        props.put(ConstantesECM.CMCOR_DEP_CODIGO, (!isEmptyFolder && !ObjectUtils.isEmpty(depCodigo) || isEmptyFolder) ?
                depCodigo : folder.getPropertyValue(ConstantesECM.CMCOR_DEP_CODIGO));

        String sedeCodigo = !ObjectUtils.isEmpty(unidadDocumentalDTO.getCodigoSede()) ? unidadDocumentalDTO.getCodigoSede() : "";
        props.put(ConstantesECM.CMCOR_DEP_CODIGO_UAP, (!isEmptyFolder && !ObjectUtils.isEmpty(sedeCodigo) || isEmptyFolder) ?
                sedeCodigo : folder.getPropertyValue(ConstantesECM.CMCOR_DEP_CODIGO_UAP));

        try {
            if (!isEmptyFolder && !props.isEmpty()) {
                folder.updateProperties(props);
            }
        } catch (Exception e) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
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
    private boolean hasDatesInRange(Folder folder, UnidadDocumentalDTO dto) {

        Calendar dtoFechaCierre = dto.getFechaCierre();
        Calendar folderFechaCierre = folder.getPropertyValue(ConstantesECM.CMCOR_UD_FECHA_CIERRE);

        Calendar dtoFechaInicial = dto.getFechaExtremaInicial();
        Calendar folderFechaInicial = folder.getPropertyValue(ConstantesECM.CMCOR_UD_FECHA_INICIAL);

        Calendar dtoFechaFinal = dto.getFechaExtremaFinal();
        Calendar folderFechaFinal = folder.getPropertyValue(ConstantesECM.CMCOR_UD_FECHA_FINAL);

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
        log.info("### Obtener Carpetas Hijas Dado Padre: " + carpetaPadre.getFolder().getName());
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
            log.error("*** Error al obtener Carpetas Hijas dado padre*** ", e);
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
        log.info("### Actualizando nombre folder: " + nombre);
        try {
            carpeta.getFolder().rename(nombre);
        } catch (Exception e) {
            log.error("*** Error al actualizar nombre folder *** ", e);
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
                if (description.equals(configuracion.getPropiedad(ConstantesECM.CLASE_DEPENDENCIA))) {
                    folderReturn = getCarpeta(codFolder, aux, "metadatoCodDependencia", folderReturn);
                } else if (description.equals(configuracion.getPropiedad(ConstantesECM.CLASE_SERIE))) {
                    folderReturn = getCarpeta(codFolder, aux, "metadatoCodSerie", folderReturn);
                } else if (description.equals(configuracion.getPropiedad(ConstantesECM.CLASE_SUBSERIE))) {
                    log.info("Entro a clase subserie cargando los valores");
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
        final Object propertyValue = aux.getFolder().getPropertyValue(ConstantesECM.CMCOR + configuracion.getPropiedad(metadato));
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
        log.info("### Mover documento: " + documento);
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
            response.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);

        } catch (CmisObjectNotFoundException e) {
            log.error("*** Error al mover el documento *** ", e);
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
        log.info("### Generando arbol");
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
                                log.info("Organigrama -- ya existe la carpeta: " + folderFather.getFolder().getName());
                            } else {
                                log.info("Organigrama --  Creando folder: " + organigrama.getNomOrg());
                                folderFather = crearCarpeta(folder, organigrama.getNomOrg(), organigrama.getCodOrg(), ConstantesECM.CLASE_BASE, null, null);
                            }

                        } else {
                            log.info("Organigrama --  La carpeta ya esta creado: " + folderFather.getFolder().getName());
                            //Actualización de folder
                            if (!(organigrama.getNomOrg().equals(folderFather.getFolder().getName()))) {
                                log.info("Se debe actualizar al nombre: " + organigrama.getNomOrg());
                                actualizarNombreCarpeta(folderFather, organigrama.getNomOrg());
                            } else {
                                log.info("Organigrama --  El folder ya esta creado: " + organigrama.getNomOrg());
                            }
                        }
                        bandera++;
                        folderFatherContainer = folderFather;

                    } else {
                        folderSon = chequearCapetaPadre(folderFather, organigrama.getCodOrg());
                        if (ObjectUtils.isEmpty(folderSon)) {
                            log.info("Organigrama --  Creando folder: " + organigrama.getNomOrg());
                            folderSon = crearCarpeta(folderFather, organigrama.getNomOrg(), organigrama.getCodOrg(), ConstantesECM.CLASE_DEPENDENCIA, folderFather, null);
                        } else {
                            log.info("Organigrama --  El folder ya esta creado2: " + folderSon.getFolder().getName());
                            //Actualización de folder
                            if (!(organigrama.getNomOrg().equals(folderSon.getFolder().getName()))) {
                                log.info("Se debe actualizar al nombre: " + organigrama.getNomOrg());
                                actualizarNombreCarpeta(folderSon, organigrama.getNomOrg());
                            } else {
                                log.info("Organigrama --  El folder ya esta creado: " + organigrama.getNomOrg());
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
                            log.info("TRD --  Creando folder: " + nombreSerie);
                            folderSon = crearCarpeta(folderFatherContainer, nombreSerie, dependencias.getCodSerie(), ConstantesECM.CLASE_SERIE, folderFather, dependencias.getIdOrgOfc());
                        } else {
                            log.info("El formato para el nombre de la serie no es valido.");
                            break;
                        }
                    } else {
                        //Actualización de folder
                        if (!(nombreSerie.equals(folderSon.getFolder().getName()))) {
                            log.info("Se debe cambiar el nombre: " + nombreSerie);
                            actualizarNombreCarpeta(folderSon, nombreSerie);
                        } else {
                            log.info("TRD --  El folder ya esta creado: " + nombreSerie);
                        }
                    }
                    folderFather = folderSon;
                    if (!ObjectUtils.isEmpty(dependencias.getCodSubSerie())) {
                        String nombreSubserie = formatearNombre(dependenciasArray, "formatoNombreSubserie");
                        folderSon = chequearCapetaPadre(folderFather, dependencias.getCodSubSerie());
                        if (ObjectUtils.isEmpty(folderSon)) {
                            if (!ObjectUtils.isEmpty(nombreSubserie)) {
                                log.info("TRD --  Creando folder: " + nombreSubserie);
                                folderSon = crearCarpeta(folderFather, nombreSubserie, dependencias.getCodSubSerie(), ConstantesECM.CLASE_SUBSERIE, folderFather, dependencias.getIdOrgOfc());
                            } else {
                                log.info("El formato para el nombre de la subserie no es valido.");
                                break;
                            }
                        } else {

                            //Actualización de folder
                            if (!(nombreSubserie.equals(folderSon.getFolder().getName()))) {
                                log.info("Se debe cambiar el nombre: " + nombreSubserie);
                                actualizarNombreCarpeta(folderSon, nombreSubserie);
                            } else {
                                log.info("TRD --  El folder ya esta creado: " + nombreSubserie);
                            }
                        }
                        folderFather = folderSon;
                    }
                }
                bandera = 0;
                response.setCodMensaje("OK");
                response.setMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
            }
        } catch (Exception e) {
            log.error("Error al crear arbol content ", e);
            response.setCodMensaje("Error al crear el arbol");
            response.setMensaje("111112");
        }
        log.info("### Estructura creada------..");
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

        log.info("Se entra al metodo obtenerDocumentosAdjuntos");

        MensajeRespuesta response = new MensajeRespuesta();
        try {
            ItemIterable<QueryResult> resultsPrincipalAdjunto = getPrincipalAdjuntosQueryResults(session, documento);

            ArrayList<DocumentoDTO> documentosLista = new ArrayList<>();
            for (QueryResult qResult : resultsPrincipalAdjunto) {

                DocumentoDTO documentoDTO = new DocumentoDTO();

                String[] parts = qResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID).toString().split(";");
                String idDocumento = parts[0];

                documentoDTO.setIdDocumento(idDocumento);
                if (qResult.getPropertyValueByQueryName(ConstantesECM.CMCOR_ID_DOC_PRINCIPAL) != null) {
                    documentoDTO.setIdDocumentoPadre(documento.getIdDocumento());
                    documentoDTO.setTipoPadreAdjunto(qResult.getPropertyValueByQueryName(ConstantesECM.CMCOR_TIPO_DOCUMENTO).toString());
                } else {
                    documentoDTO.setTipoPadreAdjunto("Principal");
                }
                Document document = (Document) session.getObject(session.createObjectId(idDocumento));
                documentoDTO.setNombreDocumento(qResult.getPropertyValueByQueryName(PropertyIds.NAME));
                GregorianCalendar newGregCal = qResult.getPropertyValueByQueryName(PropertyIds.CREATION_DATE);
                documentoDTO.setFechaCreacion(newGregCal.getTime());
                documentoDTO.setTipologiaDocumental(qResult.getPropertyValueByQueryName(ConstantesECM.CMCOR_TIPOLOGIA_DOCUMENTAL));
                documentoDTO.setTipoDocumento(qResult.getPropertyValueByQueryName(PropertyIds.CONTENT_STREAM_MIME_TYPE).toString());
                documentoDTO.setTamano(qResult.getPropertyValueByQueryName(PropertyIds.CONTENT_STREAM_LENGTH).toString());


                documentoDTO.setDocumento(getDocumentBytes(document));
                String nroRadicado = qResult.getPropertyValueByQueryName(ConstantesECM.CMCOR_NRO_RADICADO);
                if (ObjectUtils.isEmpty(nroRadicado))
                    nroRadicado = "";
                documentoDTO.setNroRadicado(nroRadicado);
                documentoDTO.setNombreRemitente(qResult.getPropertyValueByQueryName(ConstantesECM.CMCOR_NOMBRE_REMITENTE) != null ? qResult.getPropertyValueByQueryName(ConstantesECM.CMCOR_NOMBRE_REMITENTE).toString() : null);

                documentosLista.add(documentoDTO);
            }
            response.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
            response.setMensaje("OK");
            response.setDocumentoDTOList(documentosLista);

        } catch (Exception e) {
            response.setCodMensaje("2222");
            response.setMensaje("Error en la obtención de los documentos adjuntos: " + e.getMessage());
            log.error("Error en la obtención de los documentos adjuntos: ", e);
            response.setDocumentoDTOList(new ArrayList<>());
        }
        log.info("Se sale del metodo obtenerDocumentosAdjuntos con respuesta: " + response.toString());
        return response;
    }

    private byte[] getDocumentBytes(Document document) throws SystemException {
        try {
            final Optional<File> optionalFile = convertInputStreamToFile(document.getContentStream());
            if (optionalFile.isPresent()) {
                return FileUtils.readFileToByteArray(optionalFile.get());
            }
            throw new SystemException("Error al obtener el documento en Bytes");
        } catch (Exception e) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    private ItemIterable<QueryResult> getPrincipalAdjuntosQueryResults(Session session, DocumentoDTO documento) {
        //Obtener el documentosAdjuntos
        String query = "SELECT * FROM cmcor:CM_DocumentoPersonalizado";
        boolean where = false;

        if (!ObjectUtils.isEmpty(documento.getIdDocumento())) {
            where = true;

            query += " WHERE " + PropertyIds.OBJECT_ID + " = '" + documento.getIdDocumento() + "'" +
                    " OR " + ConstantesECM.CMCOR_ID_DOC_PRINCIPAL + " = '" + documento.getIdDocumento() + "'";
        }
        if (!ObjectUtils.isEmpty(documento.getNroRadicado())) {

            query += (where ? " AND " : " WHERE ") + ConstantesECM.CMCOR_NRO_RADICADO + " = '" + documento.getNroRadicado() + "'";
            where = true;
        }
        if (!ObjectUtils.isEmpty(documento.getNroRadicadoReferido())) {
            String[] nroRadicadoReferidoArray = documento.getNroRadicadoReferido();
            StringBuilder radicadoReferido = new StringBuilder();
            String separator = ConstantesECM.SEPARADOR;
            for (int i = 0; i < nroRadicadoReferidoArray.length; i++) {
                if (i == nroRadicadoReferidoArray.length - 1) {
                    separator = "";
                }
                radicadoReferido.append(nroRadicadoReferidoArray[i]).append(separator);
            }
            query += (where ? " AND " : " WHERE ") + "(" + ConstantesECM.CMCOR_NUMERO_REFERIDO + " LIKE '%" + radicadoReferido + ConstantesECM.SEPARADOR + "%'";

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

        log.info("Se entra al metodo obtenerVersionesDocumento");

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
            response.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
            response.setMensaje("OK");
            response.setDocumentoDTOList(versionesLista);
        } catch (Exception e) {
            response.setCodMensaje("2222");
            response.setMensaje("Error en la obtención de las versiones del documento: " + e.getMessage());
            response.setDocumentoDTOList(new ArrayList<>());
            log.error("Error en la obtención de las versiones del documento: ", e);
        }
        log.info("Se devuelven las versiones del documento: ", versionesLista.toString());
        log.info("Se sale del metodo obtenerVersionesDocumento con respuesta: " + response.toString());
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

        log.info("Se entra al metodo subirDocumentoPrincipalAdjunto");

        MensajeRespuesta response = new MensajeRespuesta();
        //Se definen las propiedades del documento a subir
        Map<String, Object> properties = new HashMap<>();
        properties.put(PropertyIds.OBJECT_TYPE_ID, "D:cmcor:CM_DocumentoPersonalizado");
        //En caso de que sea documento adjunto se le pone el id del documento principal dentro del parametro cmcor:xIdentificadorDocPrincipal
        if (!StringUtils.isEmpty(documento.getIdDocumentoPadre())) {
            properties.put(ConstantesECM.CMCOR_ID_DOC_PRINCIPAL, documento.getIdDocumentoPadre());
            properties.put(ConstantesECM.CMCOR_TIPO_DOCUMENTO, "Anexo");
        }

        final String docName = !StringUtils.isEmpty(documento.getNombreDocumento()) ?
                documento.getNombreDocumento().trim() : "";
        properties.put(PropertyIds.NAME, docName);

        if ("PD".equals(selector)) {
            buscarCrearCarpeta(session, documento, response, documento.getDocumento(), properties, ConstantesECM.PRODUCCION_DOCUMENTAL);
        } else {
            buscarCrearCarpetaRadicacion(session, documento, response, properties, selector);
        }

        log.info("Se sale del metodo subirDocumentoPrincipalAdjunto");
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
        log.info("Se entra al metodo crearLinkDocumentosApoyo");

        MensajeRespuesta response = new MensajeRespuesta();

        Map<String, Object> properties = new HashMap<>();

        buscarCrearCarpeta(session, documento, response, documento.getDocumento(), properties, ConstantesECM.DOCUMENTOS_APOYO);

        log.info("Se sale del metodo crearLinkDocumentosApoyo");
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

        log.info("Se entra al metodo crearLink");

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

        log.info("Se crea el link y se sale del método crearLink");
    }

    /**
     * Metodo que busca una Unidad Documental en el ECM
     *
     * @param folder Obj ECm
     * @return List<DocumentoDTO> Lista de los documentos de la carpeta
     */
    @Override
    public List<DocumentoDTO> getDocumentsFromFolder(Folder folder) throws SystemException {
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
     * @return MensajeRespuesta       Unidad Documental
     */
    @Override
    public MensajeRespuesta subirDocumentosUnidadDocumentalECM(UnidadDocumentalDTO unidadDocumentalDTO, Session session) throws SystemException {
        log.info("Se entra al metodo subirDocumentosUnidadDocumentalECM");
        if (ObjectUtils.isEmpty(unidadDocumentalDTO) || StringUtils.isEmpty(unidadDocumentalDTO.getId())) {
            throw new SystemException("No se ha identificado el id de la Unidad Documental");
        }
        try {
            final Optional<Folder> optionalFolder = getUDFolderById(unidadDocumentalDTO.getId(), session);
            if (!optionalFolder.isPresent()) {
                throw new SystemException("No existe la unidad documental con id " + unidadDocumentalDTO.getId());
            }
            final Folder targetFolder = optionalFolder.get();
            final List<DocumentoDTO> listaDocumentos = unidadDocumentalDTO.getListaDocumentos();
            if (ObjectUtils.isEmpty(listaDocumentos)) {
                throw new SystemException("No se han especificado los documentos para archivar en la Unidad Documental");
            }
            listaDocumentos.forEach(documentoDTO -> {
                CmisObject documentObj = session.getObject(new ObjectIdImpl(documentoDTO.getIdDocumento()));
                if (documentObj.getType().getId().startsWith("D:cmcor:")) {
                    Document document = (Document) documentObj;
                    final List<Folder> parents = document.getParents();
                    final Optional<Folder> sourceTarget = parents.parallelStream()
                            .filter(folder -> folder.getType().getId().startsWith("F:cmcor:CM_Unidad_Documental") &&
                                    StringUtils.isEmpty(folder.getPropertyValue(ConstantesECM.CMCOR_UD_ID))).findFirst();
                    sourceTarget.ifPresent(folder -> {
                        document.move(folder, targetFolder);
                        final Map<String, Object> properties = new HashMap<>();
                        Calendar startDate = targetFolder.getPropertyValue(ConstantesECM.CMCOR_UD_FECHA_INICIAL);
                        if (ObjectUtils.isEmpty(startDate)) {
                            properties.put(ConstantesECM.CMCOR_UD_FECHA_INICIAL, GregorianCalendar.getInstance());
                        }
                        properties.put(ConstantesECM.CMCOR_UD_FECHA_FINAL, GregorianCalendar.getInstance());
                        targetFolder.updateProperties(properties);
                    });
                }
            });
            unidadDocumentalDTO = transformarUnidadDocumental(targetFolder);
            final List<DocumentoDTO> documentsFromFolder = getDocumentsFromFolder(targetFolder);
            unidadDocumentalDTO.setListaDocumentos(documentsFromFolder);
            Map<String, Object> response = new HashMap<>();
            response.put("unidadDocumental", unidadDocumentalDTO);
            return MensajeRespuesta.newInstance()
                    .codMensaje(ConstantesECM.SUCCESS_COD_MENSAJE)
                    .response(response)
                    .mensaje("Operacion realizada satisfactoriamente").build();
        } catch (Exception e) {
            log.error("Error al subir los documentos a la unidad documental");
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
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
        log.info("Se entra al metodo subirVersionarDocumentoGenerado");
        MensajeRespuesta response = new MensajeRespuesta();
        List<DocumentoDTO> documentoDTOList = new ArrayList<>();
        Map<String, Object> properties = new HashMap<>();

        byte[] bytes = documento.getDocumento();
        if ("html".equals(documento.getTipoDocumento())) {
            documento.setTipoDocumento("text/html");
        } else {
            documento.setTipoDocumento(ConstantesECM.APPLICATION_PDF);
        }

        if (documento.getIdDocumento() == null) {

            //Se definen las propiedades del documento a subir

            properties.put(PropertyIds.OBJECT_TYPE_ID, "D:cmcor:CM_DocumentoPersonalizado");
            properties.put(PropertyIds.CONTENT_STREAM_MIME_TYPE, documento.getTipoDocumento());
            final String docName = !StringUtils.isEmpty(documento.getNombreDocumento()) ?
                    documento.getNombreDocumento().trim() : "";
            properties.put(PropertyIds.NAME, docName);

            if ("PD".equals(selector)) {
                buscarCrearCarpeta(session, documento, response, bytes, properties, ConstantesECM.PRODUCCION_DOCUMENTAL);
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
                response.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
                response.setMensaje("Documento versionado correctamente");
                documento.setVersionLabel(docAux.getVersionLabel());
                documentoDTOList.add(documento);
                response.setDocumentoDTOList(documentoDTOList);
                log.info("Documento versionado correctamente con metadatos: " + documento);
            } catch (CmisBaseException e) {
                log.error("checkin failed, trying to cancel the checkout", e);
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
        log.info("MetaDatos: {}", documentoDTO.toString());
        String idDocumento;
        List<DocumentoDTO> documentoDTOList = new ArrayList<>();
        try {
            //Se obtiene la carpeta dentro del ECM al que va a ser subido el documento
            log.info("### Se elige la carpeta donde se va a guardar el documento principal..");
            log.info("###------------ Se elige la sede donde se va a guardar el documento principal..");

            if (ObjectUtils.isEmpty(documentoDTO.getCodigoDependencia())) {
                throw new BusinessException("No se ha identificado el codigo de la Dependencia");
            }
            final Optional<Carpeta> optionalCarpeta = getFolderBy(ConstantesECM.CLASE_DEPENDENCIA, ConstantesECM.CMCOR_DEP_CODIGO,
                    documentoDTO.getCodigoDependencia(), session);

            if (optionalCarpeta.isPresent()) {
                Carpeta folderAlfresco = optionalCarpeta.get();
                log.info("Se busca si existe la carpeta de Produccion documental para el año en curso dentro de la dependencia " + documentoDTO.getDependencia());
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                List<Carpeta> carpetasDeLaDependencia = obtenerCarpetasHijasDadoPadre(folderAlfresco);

                Carpeta carpetaTarget;

                Optional<Carpeta> produccionDocumental = carpetasDeLaDependencia.stream()
                        .filter(p -> p.getFolder().getName().equals(carpetaCrearBuscar + year)).findFirst();
                carpetaTarget = getCarpeta(carpetaCrearBuscar, Optional.of(folderAlfresco), year, produccionDocumental);

                idDocumento = crearDocumentoDevolverId(documentoDTO, response, bytes, properties, documentoDTOList, carpetaTarget);
                //Creando el mensaje de respuesta
                response.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
                response.setMensaje("Documento añadido correctamente");
                log.info(ConstantesECM.AVISO_CREA_DOC_ID + idDocumento);
            } else {
                log.info(ConstantesECM.NO_EXISTE_DEPENDENCIA + documentoDTO.getDependencia());
                response.setCodMensaje("4444");
                response.setMensaje(ConstantesECM.NO_EXISTE_DEPENDENCIA + documentoDTO.getDependencia());
            }

        } catch (CmisContentAlreadyExistsException ccaee) {
            log.error(ConstantesECM.ECM_ERROR_DUPLICADO, ccaee);
            response.setCodMensaje("1111");
            response.setMensaje(ConstantesECM.ECM_ERROR_DUPLICADO);
        } catch (CmisConstraintException cce) {
            log.error(ConstantesECM.ECM_ERROR, cce);
            response.setCodMensaje("2222");
            response.setMensaje("El nombre del documento('" + documentoDTO.getNombreDocumento() + "') no supera la convencion proporcionada por el ECM");
        } catch (Exception e) {
            log.error(ConstantesECM.ERROR_TIPO_EXCEPTION, e);
            response.setCodMensaje("2222");
            response.setMensaje(e.getMessage());
        }
    }

    private Carpeta getCarpeta(String carpetaCrearBuscar, Optional<Carpeta> dependencia, int year, Optional<Carpeta> produccionDocumental) {
        Carpeta carpetaTarget = null;
        if (produccionDocumental.isPresent()) {
            log.info(ConstantesECM.EXISTE_CARPETA + carpetaCrearBuscar + year);
            carpetaTarget = produccionDocumental.get();
        } else {
            log.info("Se crea la Carpeta: " + carpetaCrearBuscar + year);
            if (dependencia.isPresent()) {
                carpetaTarget = crearCarpeta(dependencia.get(), carpetaCrearBuscar + year, "11", ConstantesECM.CLASE_UNIDAD_DOCUMENTAL, dependencia.get(), null);
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
            log.info("### Se elige la carpeta donde se va a guardar el documento radicado..");
            log.info("###------------ Se elige la sede donde se va a guardar el documento radicado..");
            folderAlfresco = obtenerCarpetaPorNombre(documento.getSede(), session);

            if (folderAlfresco.getFolder() != null) {
                log.info("###------------------- Se obtienen todas las dependencias de la sede..");
                List<Carpeta> carpetasHijas = obtenerCarpetasHijasDadoPadre(folderAlfresco);

                String comunicacionOficial = "";
                String tipoComunicacionSelector;
                tipoComunicacionSelector = getTipoComunicacionSelector(tipoComunicacion);

                if ("EI".equals(tipoComunicacion) || "SI".equals(tipoComunicacion)) {
                    comunicacionOficial = ConstantesECM.TIPO_COMUNICACION_INTERNA;
                } else if ("EE".equals(tipoComunicacion) || "SE".equals(tipoComunicacion)) {
                    comunicacionOficial = ConstantesECM.TIPO_COMUNICACION_EXTERNA;
                }

                //Se busca si existe la dependencia
                Optional<Carpeta> dependencia = carpetasHijas.stream()
                        .filter(p -> p.getFolder().getName().equals(documento.getDependencia())).findFirst();

                log.info("Se obtienen la dependencia referente a la sede: " + documento.getSede());
                if (dependencia.isPresent()) {

                    log.info("Se busca si existe la carpeta de Comunicaciones Oficiales dentro de la dependencia " + documento.getDependencia());

                    List<Carpeta> carpetasDeLaDependencia = obtenerCarpetasHijasDadoPadre(dependencia.get());

                    //Obtengo la carpeta de comunicaciones oficiales si existe
                    Optional<Carpeta> comunicacionOficialFolder = carpetasDeLaDependencia.stream()
                            .filter(p -> p.getFolder().getName().contains("0231_COMUNICACIONES OFICIALES")).findFirst();

                    crearInsertarCarpetaRadicacion(documento, response, documento.getDocumento(), properties, comunicacionOficial, tipoComunicacionSelector, comunicacionOficialFolder);
                } else {
                    response.setMensaje(ConstantesECM.NO_EXISTE_DEPENDENCIA + documento.getDependencia());
                    response.setCodMensaje("4445");
                    log.info(ConstantesECM.NO_EXISTE_DEPENDENCIA + documento.getDependencia());
                }
            } else {
                response.setMensaje(ConstantesECM.NO_EXISTE_SEDE + documento.getSede());
                response.setCodMensaje("4444");
                log.info(ConstantesECM.NO_EXISTE_SEDE + documento.getSede());
            }
        } catch (CmisContentAlreadyExistsException ccaee) {
            log.error(ConstantesECM.ECM_ERROR_DUPLICADO, ccaee);
            response.setCodMensaje("1111");
            response.setMensaje("El documento ya existe en el ECM");
        } catch (CmisConstraintException cce) {
            log.error(ConstantesECM.ECM_ERROR, cce);
            response.setCodMensaje("2222");
            response.setMensaje(configuracion.getPropiedad(ConstantesECM.ECM_ERROR));
        } catch (Exception e) {
            log.error(ConstantesECM.ERROR_TIPO_EXCEPTION, e);
            response.setCodMensaje("2222");
            response.setMensaje(configuracion.getPropiedad(ConstantesECM.ECM_ERROR));
        }
    }

    private String getTipoComunicacionSelector(String tipoComunicacion) {
        switch (tipoComunicacion) {
            case "EI":
                return ConstantesECM.COMUNICACIONES_INTERNAS_RECIBIDAS;

            case "SI":
                return ConstantesECM.COMUNICACIONES_INTERNAS_ENVIADAS;

            case "EE":
                return ConstantesECM.COMUNICACIONES_EXTERNAS_RECIBIDAS;

            case "SE":
                return ConstantesECM.COMUNICACIONES_EXTERNAS_ENVIADAS;
            default:
                return ConstantesECM.COMUNICACIONES_INTERNAS_RECIBIDAS;
        }
    }

    private void crearInsertarCarpetaRadicacion(DocumentoDTO documentoDTO, MensajeRespuesta response, byte[] bytes, Map<String, Object> properties, String comunicacionOficial, String tipoComunicacionSelector, Optional<Carpeta> comunicacionOficialFolder) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        List<DocumentoDTO> documentoDTOList = new ArrayList<>();
        Carpeta carpetaTarget = new Carpeta();
        String idDocumento;
        if (comunicacionOficialFolder.isPresent()) {
            log.info(ConstantesECM.EXISTE_CARPETA + comunicacionOficialFolder.get().getFolder().getName());

            List<Carpeta> carpetasDeComunicacionOficial = obtenerCarpetasHijasDadoPadre(comunicacionOficialFolder.get());

            Optional<Carpeta> comunicacionOficialInOut = carpetasDeComunicacionOficial.stream()
                    .filter(p -> p.getFolder().getName().contains(comunicacionOficial)).findFirst();

            if (!comunicacionOficialInOut.isPresent()) {

                Carpeta carpetaCreada = crearCarpeta(comunicacionOficialFolder.get(), comunicacionOficial, "11", ConstantesECM.CLASE_SUBSERIE, comunicacionOficialFolder.get(), null);
                if (carpetaCreada != null) {
                    log.info(ConstantesECM.EXISTE_CARPETA + carpetaCreada.getFolder().getName());
                    List<Carpeta> carpetasDeComunicacionOficialDentro = obtenerCarpetasHijasDadoPadre(carpetaCreada);

                    Optional<Carpeta> comunicacionOficialInOutDentro = carpetasDeComunicacionOficialDentro.stream()
                            .filter(p -> p.getFolder().getName().contains(tipoComunicacionSelector)).findFirst();
                    carpetaTarget = comunicacionOficialInOutDentro.orElseGet(() -> crearCarpeta(carpetaCreada, tipoComunicacionSelector + year, "11", ConstantesECM.CLASE_UNIDAD_DOCUMENTAL, carpetaCreada, null));
                }

            } else {
                log.info(ConstantesECM.EXISTE_CARPETA + comunicacionOficialInOut.get().getFolder().getName());

                List<Carpeta> carpetasDeComunicacionOficialDentro = obtenerCarpetasHijasDadoPadre(comunicacionOficialInOut.get());

                Optional<Carpeta> comunicacionOficialInOutDentro = carpetasDeComunicacionOficialDentro.stream()
                        .filter(p -> p.getFolder().getName().contains(tipoComunicacionSelector)).findFirst();
                carpetaTarget = comunicacionOficialInOutDentro.orElseGet(() -> crearCarpeta(comunicacionOficialInOut.get(), tipoComunicacionSelector + year, "11", ConstantesECM.CLASE_SUBSERIE, comunicacionOficialInOut.get(), null));
            }
            idDocumento = crearDocumentoDevolverId(documentoDTO, response, bytes, properties, documentoDTOList, carpetaTarget);
            //Creando el mensaje de respuesta
            response.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
            response.setMensaje("Documento añadido correctamente");
            log.info(ConstantesECM.AVISO_CREA_DOC_ID + idDocumento);

        } else {
            response.setCodMensaje("3333");
            response.setMensaje("En esta sede y dependencia no esta permitido realizar radicaciones");
        }
    }

    private String crearDocumentoDevolverId(DocumentoDTO documentoDTO, MensajeRespuesta response, byte[] bytes, Map<String, Object> properties, List<DocumentoDTO> documentoDTOList, Carpeta carpetaTarget) {
        String idDocumento;
        log.info("Se llenan los metadatos del documento a crear");
        ContentStream contentStream = new ContentStreamImpl(documentoDTO.getNombreDocumento(), BigInteger.valueOf(bytes.length), documentoDTO.getTipoDocumento(), new ByteArrayInputStream(bytes));

        if (documentoDTO.getNroRadicado() != null) {
            properties.put(ConstantesECM.CMCOR_NRO_RADICADO, documentoDTO.getNroRadicado());
        }
        if (documentoDTO.getNombreRemitente() != null) {
            properties.put(ConstantesECM.CMCOR_NOMBRE_REMITENTE, documentoDTO.getNombreRemitente());
        }
        if (documentoDTO.getTipologiaDocumental() != null) {
            properties.put(ConstantesECM.CMCOR_TIPOLOGIA_DOCUMENTAL, documentoDTO.getTipologiaDocumental());
        }
        log.info(ConstantesECM.AVISO_CREA_DOC);
        final String docName = StringUtils.isEmpty(documentoDTO.getNombreDocumento()) ?
                "" : documentoDTO.getNombreDocumento().trim();
        properties.put(PropertyIds.NAME, docName);
        documentoDTO.setNombreDocumento(docName);
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
        log.info("### Modificar documento: " + idDocumento);
        MensajeRespuesta response = new MensajeRespuesta();
        try {

            ObjectId idDoc = new ObjectIdImpl(idDocumento);

            Map<String, Object> updateProperties = new HashMap<>();
            updateProperties.put(ConstantesECM.CMCOR_NRO_RADICADO, nroRadicado);
            updateProperties.put(ConstantesECM.CMCOR_NOMBRE_REMITENTE, nombreRemitente);
            updateProperties.put(ConstantesECM.CMCOR_TIPOLOGIA_DOCUMENTAL, tipologiaDocumental);

            CmisObject object = session.getObject(idDoc);
            object.updateProperties(updateProperties);
            log.info("### Modificados los metadatos de correctamente");
            response.setMensaje("OK");
            response.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);

        } catch (CmisObjectNotFoundException e) {
            log.error("*** Error al modificar el documento *** ", e);
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

        if (ConstantesECM.CMCOR_SS_CODIGO.equals(tipoCarpeta)) {
            if (folderFather != null) {
                props.put(ConstantesECM.CMCOR_DEP_CODIGO_UAP, folderFather.getFolder().getPropertyValue(ConstantesECM.CMCOR_SER_CODIGO));
                props.put(ConstantesECM.CMCOR_SER_CODIGO, folderFather.getFolder().getPropertyValue(ConstantesECM.CMCOR_SER_CODIGO));
                props.put(ConstantesECM.CMCOR_DEP_CODIGO, idOrgOfc);
            }
        } else if (ConstantesECM.CMCOR_SER_CODIGO.equals(tipoCarpeta)) {
            if (folderFather != null) {
                props.put(ConstantesECM.CMCOR_DEP_CODIGO_UAP, folderFather.getFolder().getPropertyValue(ConstantesECM.CMCOR_DEP_CODIGO));
                props.put(ConstantesECM.CMCOR_DEP_CODIGO, idOrgOfc);
            }
        } else {
            props.put(ConstantesECM.CMCOR_DEP_CODIGO_UAP, codOrg);
            props.put(ConstantesECM.CMCOR_DEP_CODIGO, codOrg);
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
        props.put(ConstantesECM.CMCOR_UB_CODIGO, codOrg);
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

            log.info("Se buscan los documentos Anexos al documento que se va a borrar");
            DocumentoDTO documento = new DocumentoDTO();
            documento.setIdDocumento(idDoc);
            ItemIterable<QueryResult> resultsPrincipalAdjunto = getPrincipalAdjuntosQueryResults(session, documento);

            for (QueryResult qResult : resultsPrincipalAdjunto) {

                String[] parts = qResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID).toString().split(";");
                String idDocumento = parts[0];

                log.info("Se procede a eliminar el documento: " + qResult.getPropertyByQueryName(PropertyIds.NAME).getValues().get(0).toString());
                ObjectId a = new ObjectIdImpl(idDocumento);
                CmisObject object = session.getObject(a);
                Document delDoc = (Document) object;
                //Se borra el documento pero no todas las versiones solo la ultima
                delDoc.delete(false);
                log.info("Se logro eliminar el documento");
            }
            return Boolean.TRUE;

        } catch (CmisObjectNotFoundException e) {
            log.error("No se pudo eliminar el documento :", e);
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
        unidadDocumentalDTO.setId(folder.getPropertyValue(ConstantesECM.CMCOR_UD_ID));
        unidadDocumentalDTO.setAccion(folder.getPropertyValue(ConstantesECM.CMCOR_UD_ACCION));
        unidadDocumentalDTO.setDescriptor2(folder.getPropertyValue(ConstantesECM.CMCOR_UD_DESCRIPTOR_2));
        unidadDocumentalDTO.setCerrada(folder.getPropertyValue(ConstantesECM.CMCOR_UD_CERRADA));
        unidadDocumentalDTO.setFechaCierre(folder.getPropertyValue(ConstantesECM.CMCOR_UD_FECHA_CIERRE));
        unidadDocumentalDTO.setFechaExtremaInicial(folder.getPropertyValue(ConstantesECM.CMCOR_UD_FECHA_INICIAL));
        unidadDocumentalDTO.setFechaExtremaFinal(folder.getPropertyValue(ConstantesECM.CMCOR_UD_FECHA_FINAL));
        unidadDocumentalDTO.setSoporte(folder.getPropertyValue(ConstantesECM.CMCOR_UD_SOPORTE));
        unidadDocumentalDTO.setInactivo(folder.getPropertyValue(ConstantesECM.CMCOR_UD_INACTIVO));
        unidadDocumentalDTO.setUbicacionTopografica(folder.getPropertyValue(ConstantesECM.CMCOR_UD_UBICACION_TOPOGRAFICA));
        unidadDocumentalDTO.setFaseArchivo(folder.getPropertyValue(ConstantesECM.CMCOR_UD_FASE_ARCHIVO));
        unidadDocumentalDTO.setDescriptor1(folder.getPropertyValue(ConstantesECM.CMCOR_UD_DESCRIPTOR_1));
        unidadDocumentalDTO.setCodigoUnidadDocumental(folder.getPropertyValue(ConstantesECM.CMCOR_UD_CODIGO));
        unidadDocumentalDTO.setCodigoSerie(folder.getPropertyValue(ConstantesECM.CMCOR_SER_CODIGO));
        unidadDocumentalDTO.setCodigoSubSerie(folder.getPropertyValue(ConstantesECM.CMCOR_SS_CODIGO));
        unidadDocumentalDTO.setCodigoDependencia(folder.getPropertyValue(ConstantesECM.CMCOR_DEP_CODIGO));
        unidadDocumentalDTO.setNombreUnidadDocumental(folder.getPropertyValue(PropertyIds.NAME));
        unidadDocumentalDTO.setObservaciones(folder.getPropertyValue(ConstantesECM.CMCOR_UD_OBSERVACIONES));
        unidadDocumentalDTO.setEstado(folder.getPropertyValue(ConstantesECM.CMCOR_UD_ESTADO));
        unidadDocumentalDTO.setDisposicion(folder.getPropertyValue(ConstantesECM.CMCOR_UD_DISPOSICION));
        return unidadDocumentalDTO;
    }

    /**
     * Convierte la Interfaz Document de opencemis a un DocumentoDTO
     *
     * @param document Objeto a transformar
     * @return DocumentoDTO
     */
    private DocumentoDTO transformarDocumento(Document document) throws SystemException {
        final DocumentoDTO documentoDTO = new DocumentoDTO();
        String idDoc = document.getId();
        try {
            documentoDTO.setIdDocumento(idDoc.contains(";") ? idDoc.substring(0, idDoc.indexOf(';')) : idDoc);
            documentoDTO.setNroRadicado(document.getPropertyValue(ConstantesECM.CMCOR_NRO_RADICADO));
            documentoDTO.setTipologiaDocumental(document.getPropertyValue(ConstantesECM.CMCOR_TIPOLOGIA_DOCUMENTAL));
            documentoDTO.setNombreRemitente(document.getPropertyValue(ConstantesECM.CMCOR_NOMBRE_REMITENTE));
            documentoDTO.setNombreDocumento(document.getName());
            documentoDTO.setIdDocumentoPadre(document.getPropertyValue(ConstantesECM.CMCOR_ID_DOC_PRINCIPAL));
            GregorianCalendar calendar = document.getPropertyValue(PropertyIds.CREATION_DATE);
            documentoDTO.setFechaCreacion(calendar.getTime());
            documentoDTO.setTipoDocumento(document.getPropertyValue(PropertyIds.CONTENT_STREAM_MIME_TYPE));
            documentoDTO.setTamano(document.getContentStreamLength() + "");
            documentoDTO.setTipoPadreAdjunto(document.getPropertyValue(ConstantesECM.CMCOR_TIPO_DOCUMENTO));
            documentoDTO.setDocumento(getDocumentBytes(document));

            final String nroReferido = document.getPropertyValue(ConstantesECM.CMCOR_NUMERO_REFERIDO);

            if (!StringUtils.isEmpty(nroReferido)) {
                String[] splitRadicado = nroReferido.split(ConstantesECM.SEPARADOR);
                documentoDTO.setNroRadicadoReferido(splitRadicado);
            }
            return documentoDTO;
        } catch (Exception e) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    @Override
    public MensajeRespuesta modificarUnidadesDocumentales(List<UnidadDocumentalDTO> unidadDocumentalDTOS, Session session) throws SystemException {
        log.info("Se procede a modificar las uniddes documentales");
        if (!ObjectUtils.isEmpty(unidadDocumentalDTOS)) {
            final List<UnidadDocumentalDTO> lista = new ArrayList<>();
            for (UnidadDocumentalDTO dto :
                    unidadDocumentalDTOS) {
                lista.add(lista.size(), modificarUnidadDocumental(dto, session));
            }
            final MensajeRespuesta respuesta = new MensajeRespuesta();
            respuesta.setResponse(new HashMap<>());
            respuesta.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
            respuesta.setMensaje("Actualizacion realizada satisfactoriamente");
            respuesta.getResponse().put("unidadesDocumentales", lista);
            return respuesta;
        }
        throw new SystemException("No se ha introducido la unidad documental a modificar");
    }

    @Override
    public MensajeRespuesta subirDocumentosTemporalesUD(List<DocumentoDTO> documentoDTOS, Session session) throws SystemException {
        log.info("Se procede a modificar las uniddes documentales");
        if (ObjectUtils.isEmpty(documentoDTOS)) {
            throw new SystemException("La lista de documentos esta vacia");
        }
        final List<DocumentoDTO> dtos = new ArrayList<>();
        for (DocumentoDTO documentoDTO :
                documentoDTOS) {
            final Optional<DocumentoDTO> optionalDocumentoDTO = subirDocumentoDtoTemp(documentoDTO, session);
            optionalDocumentoDTO.ifPresent(documentoDTO1 -> dtos.add(dtos.size(), documentoDTO1));
        }
        return MensajeRespuesta.newInstance()
                .documentoDTOList(dtos)
                .codMensaje(ConstantesECM.SUCCESS_COD_MENSAJE)
                .mensaje(ConstantesECM.SUCCESS)
                .build();
    }

    /**
     * Operacion para Subir documentos a una UD temporal ECM
     *
     * @param documentoDTO Obj de documento DTO a archivar
     * @return MensajeRespuesta
     */
    @Override
    public MensajeRespuesta subirDocumentoTemporalUD(DocumentoDTO documentoDTO, Session session) throws SystemException {
        log.info("processing rest request - Subir Documento temporal ECM");
        final Optional<DocumentoDTO> optionalDocumentoDTO = subirDocumentoDtoTemp(documentoDTO, session);
        if (optionalDocumentoDTO.isPresent()) {
            documentoDTO = optionalDocumentoDTO.get();
            final Map<String, Object> mapResponse = new HashMap<>();
            mapResponse.put("documento", documentoDTO);
            return MensajeRespuesta.newInstance()
                    .response(mapResponse)
                    .codMensaje(ConstantesECM.SUCCESS_COD_MENSAJE)
                    .mensaje(ConstantesECM.SUCCESS)
                    .build();
        }
        return MensajeRespuesta.newInstance()
                .codMensaje(ConstantesECM.ERROR_COD_MENSAJE)
                .mensaje("Ocurrio un error al subir el documento")
                .build();
    }

    @Override
    public MensajeRespuesta obtenerDocumentosArchivados(String codigoDependencia, Session session) throws SystemException {
        log.info("Se procede a obtener los documentos archivados");
        if (StringUtils.isEmpty(codigoDependencia)) {
            throw new SystemException("No se ha especificado el codigo de la dependencia");
        }
        final List<Folder> folderList = getUDListByDependencyCode(codigoDependencia, session);
        final List<Map<String, Object>> mapList = new ArrayList<>();

        for (Folder folder :
                folderList) {
            final List<DocumentoDTO> documentsFromFolder = getDocumentsFromFolder(folder);
            final String idUnidadDocumental = folder.getPropertyValue(ConstantesECM.CMCOR_UD_ID);

            final String[] serieSubSerie = getSerieSubSerie(folder, session);
            final String serieName = serieSubSerie[0];
            final String subSerieName = serieSubSerie[1];

            documentsFromFolder.forEach(documentoDTO -> {
                Map<String, Object> item = new HashMap<>();
                item.put("id", idUnidadDocumental);
                item.put("nombreUnidadDocumental", folder.getName());
                item.put("serieName", serieName);
                item.put("subSerieName", subSerieName);
                item.put("currentFatherName", StringUtils.isEmpty(subSerieName) ? serieName : subSerieName);
                item.put("fechaCreacion", documentoDTO.getFechaCreacion());
                item.put("idDocumento", StringUtils.isEmpty(documentoDTO.getNroRadicado()) ?
                        documentoDTO.getIdDocumento() : documentoDTO.getNroRadicado());
                item.put("tipologiaDocumental", documentoDTO.getTipologiaDocumental());
                item.put("nombreDocumento", documentoDTO.getNombreDocumento());

                mapList.add(mapList.size(), item);
            });
        }
        final Map<String, Object> response = new HashMap<>();
        response.put("documentos", mapList);
        return MensajeRespuesta
                .newInstance()
                .response(response)
                .codMensaje(ConstantesECM.SUCCESS_COD_MENSAJE)
                .mensaje(ConstantesECM.SUCCESS).build();
    }

    @Override
    public MensajeRespuesta listarDependenciaMultiple(ContenidoDependenciaTrdDTO dependenciaTrdDTO, Session session) throws SystemException {
        log.info("Se procede a listar todas las dependencias");
        final MensajeRespuesta respuesta = MensajeRespuesta.newInstance()
                .codMensaje(ConstantesECM.SUCCESS_COD_MENSAJE)
                .mensaje(ConstantesECM.SUCCESS)
                .build();
        final Map<String, Object> mapResponse = new HashMap<>();
        try {
            String query = "";
            if (!ObjectUtils.isEmpty(dependenciaTrdDTO)) {
                final String codigoSede = dependenciaTrdDTO.getCodSede();
                final String codigoDependencia = dependenciaTrdDTO.getIdOrgOfc();
                final String codigoSerie = dependenciaTrdDTO.getCodSerie();
                if (!StringUtils.isEmpty(codigoSerie)) {
                    query = "SELECT * FROM cmcor:CM_Serie WHERE " + ConstantesECM.CMCOR_SER_CODIGO + " LIKE '" + codigoSerie + "%'";
                } else if (!StringUtils.isEmpty(codigoDependencia)) {
                    query = "SELECT * FROM cmcor:CM_Serie WHERE " + ConstantesECM.CMCOR_DEP_CODIGO + " LIKE '" + codigoDependencia + "%'";
                } else if (!StringUtils.isEmpty(codigoSede)) {
                    query = "SELECT * FROM cmcor:CM_Unidad_Administrativa" +
                            " WHERE " + ConstantesECM.CMCOR_DEP_CODIGO + " LIKE '" + codigoSede + "%'" +
                            " AND " + PropertyIds.NAME + " LIKE '" + codigoSede + "%.%'";
                }
                if (!"".equals(query)) {
                    final Optional<ContenidoDependenciaTrdDTO> optionalTrdDTO = getDependenciaTrdDTO(query, session);
                    if (optionalTrdDTO.isPresent()) {
                        dependenciaTrdDTO = optionalTrdDTO.get();
                        mapResponse.put("series", dependenciaTrdDTO.getListaSerie());
                        mapResponse.put("subSeries", dependenciaTrdDTO.getListaSubSerie());
                        mapResponse.put("dependencias", dependenciaTrdDTO.getListaDependencia());
                    }
                }
            } else {
                mapResponse.put("series", new ArrayList<>());
                mapResponse.put("subSeries", new ArrayList<>());
                mapResponse.put("dependencias", new ArrayList<>());
            }
            mapResponse.put("sedes", getSedeList(session));
            respuesta.setResponse(mapResponse);
            return respuesta;

        } catch (Exception e) {
            log.error("Ocurrio un error al listar todas las dependencias");
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    @Override
    public MensajeRespuesta listarUdDisposicionFinal(DisposicionFinalDTO disposicionFinalDTO, Session session) throws SystemException {
        log.info("Se procede a listar las unidades documentales por tipo de disposicion");
        if (ObjectUtils.isEmpty(disposicionFinalDTO)) {
            throw new SystemException("No se ha identificado la disposicion de la Unidad Documental");
        }
        UnidadDocumentalDTO dto = disposicionFinalDTO.getUnidadDocumentalDTO();
        if (ObjectUtils.isEmpty(dto)) {
            throw new SystemException("No se ha identificado la Unidad Documental");
        }
        try {
            List<String> disposicionFinalList = disposicionFinalDTO.getDisposicionFinalList();
            List<UnidadDocumentalDTO> unidadDocumentalDTOS = listarUnidadesDocumentales(disposicionFinalList, dto, session);
            unidadDocumentalDTOS.forEach(unidadDocumentalDTO -> {
                final String idUnidadDocumental = unidadDocumentalDTO.getId();
                Optional<Folder> optionalFolder = getUDFolderById(idUnidadDocumental, session);
                optionalFolder.ifPresent(folder -> {
                    final String[] serieSubserie = getSerieSubSerie(optionalFolder.get(), session);
                    final String serieName = serieSubserie[0];
                    final String subSerieName = serieSubserie[1];
                    String currentFolderFatherName = StringUtils.isEmpty(serieName) ? subSerieName : serieName;
                    final int index = currentFolderFatherName.indexOf('_');
                    currentFolderFatherName = index != -1 ? currentFolderFatherName.substring(index + 1) : currentFolderFatherName;
                    unidadDocumentalDTO.setNombreSerie(currentFolderFatherName);
                    unidadDocumentalDTO.setNombreSubSerie(currentFolderFatherName);
                    String disposicion = unidadDocumentalDTO.getDisposicion().toLowerCase();
                    disposicion = Utilities.reemplazarCaracteresRaros(disposicion);
                    if (FinalDispositionType.CONSERVACION_TOTAL.getTexto().equals(disposicion)) {
                        disposicion = "CT";
                    } else if (FinalDispositionType.DIGITALIZAR.getTexto().equals(disposicion)) {
                        disposicion = "D";
                    } else if (FinalDispositionType.ELIMINAR.getTexto().equals(disposicion)) {
                        disposicion = "E";
                    } else if (FinalDispositionType.MICROFILMAR.getTexto().equals(disposicion)) {
                        disposicion = "M";
                    } else {
                        disposicion = "S";
                    }
                    unidadDocumentalDTO.setDisposicion(disposicion);
                });
            });
            final Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("unidadesDocumentales", unidadDocumentalDTOS);
            return MensajeRespuesta.newInstance()
                    .response(responseMap)
                    .codMensaje(ConstantesECM.SUCCESS_COD_MENSAJE)
                    .mensaje(ConstantesECM.SUCCESS).build();
        } catch (Exception e) {
            log.error("Ocurrio un error al listar las unidades documentales por tipo de disposicion");
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("Fin del metodo listarUdDisposicionFinal");
        }
    }

    @Override
    public MensajeRespuesta aprobarRechazarDisposicionesFinales(List<UnidadDocumentalDTO> unidadDocumentalDTOS, Session session) throws SystemException {
        log.info("Ejecutando metodo MensajeRespuesta aprobarRechazarDisposicionesFinalesECM(List<UnidadDocumentalDTO> unidadDocumentalDTOS)");
        if (ObjectUtils.isEmpty(unidadDocumentalDTOS)) {
            throw new SystemException("La lista de unidades documentales enviada esta vacia");
        }
        for (UnidadDocumentalDTO dto : unidadDocumentalDTOS) {
            final String idUnidadDocumental = dto.getId();
            final String disposicion = (StringUtils.isEmpty(dto.getDisposicion()) ? "" : dto.getDisposicion()).toLowerCase();
            final String estado = (StringUtils.isEmpty(dto.getEstado()) ? "" : dto.getEstado()).toLowerCase();
            if ("aprobado".equals(estado) && FinalDispositionType.ELIMINAR.getTexto().equals(disposicion)) {
                eliminarUnidadDocumental(idUnidadDocumental, session);
            } else {
                Optional<Folder> optionalFolder = getUDFolderById(idUnidadDocumental, session);
                if (optionalFolder.isPresent()) {
                    updateProperties(optionalFolder.get(), dto);
                }
            }
        }
        return MensajeRespuesta.newInstance()
                .codMensaje(ConstantesECM.SUCCESS_COD_MENSAJE)
                .mensaje("Operación realizada satisfactoriamente")
                .build();
    }

    @Override
    public Map<String, Object> obtenerPropiedadesDocumento(Document document) {
        final Map<String, Object> map = new HashMap<>();
        map.put(PropertyIds.NAME, document.getName());
        map.put(PropertyIds.OBJECT_TYPE_ID, "D:cmcor:CM_DocumentoPersonalizado");
        map.put(PropertyIds.OBJECT_ID, document.getId().split(";")[0]);
        map.put(ConstantesECM.CMCOR_ID_DOC_PRINCIPAL, document.getPropertyValue(ConstantesECM.CMCOR_ID_DOC_PRINCIPAL));
        map.put(ConstantesECM.CMCOR_NRO_RADICADO, document.getPropertyValue(ConstantesECM.CMCOR_NRO_RADICADO));
        map.put(ConstantesECM.CMCOR_NOMBRE_REMITENTE, document.getPropertyValue(ConstantesECM.CMCOR_NOMBRE_REMITENTE));
        map.put(ConstantesECM.CMCOR_TIPO_DOCUMENTO, document.getPropertyValue(ConstantesECM.CMCOR_TIPO_DOCUMENTO));
        map.put(ConstantesECM.CMCOR_NUMERO_REFERIDO, document.getPropertyValue(ConstantesECM.CMCOR_NUMERO_REFERIDO));
        return map;
    }

    @Override
    public MensajeRespuesta getDocumentosPorArchivar(final String codigoDependencia, Session session) throws SystemException {
        log.info("Se buscan los documentos por Archivar");
        if (StringUtils.isEmpty(codigoDependencia)) {
            throw new SystemException("Especifique el codigo de la dependencia");
        }
        final String query = "SELECT * FROM " + ConstantesECM.CMCOR + configuracion.getPropiedad(ConstantesECM.CLASE_UNIDAD_DOCUMENTAL) +
                " WHERE " + ConstantesECM.CMCOR_DEP_CODIGO + " = '" + codigoDependencia + "'";
        final ItemIterable<QueryResult> queryResults = session.query(query, false);
        final List<DocumentoDTO> dtos = new ArrayList<>();
        for (QueryResult queryResult :
                queryResults) {
            final String id = queryResult.getPropertyValueByQueryName(ConstantesECM.CMCOR_UD_ID);
            if (StringUtils.isEmpty(id) || id.trim().isEmpty()) {
                final String objectId = queryResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
                final Folder udFolder = (Folder) session.getObject(session.createObjectId(objectId));
                final ItemIterable<CmisObject> children = udFolder.getChildren();
                for (CmisObject cmisObject :
                        children) {
                    if (cmisObject.getType().getId().contains("D:" + ConstantesECM.CMCOR)) {
                        DocumentoDTO documentoDTO = transformarDocumento((Document) cmisObject);
                        documentoDTO.setCodigoDependencia(udFolder.getPropertyValue(ConstantesECM.CMCOR_DEP_CODIGO));
                        dtos.add(dtos.size(), documentoDTO);
                    }
                }
            }
        }
        return MensajeRespuesta.newInstance()
                .codMensaje(ConstantesECM.SUCCESS_COD_MENSAJE).documentoDTOList(dtos)
                .mensaje("Listado devuelto satisfactoriamente").build();
    }

    void eliminarUnidadDocumental(String idUnidadDocumental, Session session) throws SystemException {
        recordServices.eliminarRecordFolder(idUnidadDocumental);
        Optional<Folder> optionalFolder = getUDFolderById(idUnidadDocumental, session);
        if (optionalFolder.isPresent()) {
            Folder folder = optionalFolder.get();
            folder.deleteTree(true, UnfileObject.DELETE, true);
        } else {
            throw new SystemException("Ocurrio un error al eliminar la unidad documental '" + idUnidadDocumental + "'");
        }
    }

    private UnidadDocumentalDTO modificarUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO, Session session) throws SystemException {
        final String id = unidadDocumentalDTO.getId();
        if (StringUtils.isEmpty(id)) {
            throw new SystemException("Especifique el id de la Unidad Documental");
        }
        final Optional<Folder> optionalFolder = getUDFolderById(unidadDocumentalDTO.getId(), session);
        if (!optionalFolder.isPresent()) {
            throw new SystemException(ConstantesECM.NO_RESULT_MATCH);
        }
        Folder folder = optionalFolder.get();
        final CmisObject object = folder.updateProperties(updateProperties(folder, unidadDocumentalDTO));
        return transformarUnidadDocumental((Folder) object);
    }

    private Optional<DocumentoDTO> subirDocumentoDtoTemp(DocumentoDTO documentoDTO, Session session) throws SystemException {
        if (ObjectUtils.isEmpty(documentoDTO)) {
            throw new SystemException("No se ha especificado el documento");
        }
        if (StringUtils.isEmpty(documentoDTO.getNombreDocumento())) {
            throw new SystemException("No se ha especificado el nombre del documento");
        }
        final String codigoDependencia = documentoDTO.getCodigoDependencia();
        if (StringUtils.isEmpty(codigoDependencia)) {
            throw new SystemException("No se ha especificado el codigo de la dependencia");
        }
        if (ObjectUtils.isEmpty(documentoDTO.getDocumento())) {
            throw new SystemException("El documento no contiene informacion");
        }
        final Optional<Carpeta> optionalCarpeta = getFolderBy(ConstantesECM.CLASE_DEPENDENCIA, ConstantesECM.CMCOR_DEP_CODIGO, codigoDependencia, session);
        if (optionalCarpeta.isPresent()) {
            Folder tmpFolder = optionalCarpeta.get().getFolder();
            if (ObjectUtils.isEmpty(tmpFolder)) {
                throw new SystemException(ConstantesECM.NO_EXISTE_DEPENDENCIA + "'" + codigoDependencia + "'");
            }
            final String docName = documentoDTO.getNombreDocumento();
            final int index = docName.lastIndexOf('.');
            documentoDTO.setNombreDocumento((index > 0) ? docName.split(".")[0] : docName);
            final ItemIterable<CmisObject> children = tmpFolder.getChildren();
            final Iterator<CmisObject> iterator = children.iterator();

            Folder folder = null;
            while (iterator.hasNext()) {
                final CmisObject next = iterator.next();
                if (ConstantesECM.DOCUMENTOS_POR_ARCHIVAR.equals(next.getName())) {
                    folder = (Folder) next;
                    break;
                }
            }
            if (ObjectUtils.isEmpty(folder)) {
                UnidadDocumentalDTO dto = new UnidadDocumentalDTO();
                dto.setNombreUnidadDocumental(ConstantesECM.DOCUMENTOS_POR_ARCHIVAR);
                dto.setCodigoDependencia(codigoDependencia);
                final Optional<Folder> optionalFolder = crearUnidadDocumentalFolder(dto, session);
                if (optionalFolder.isPresent()) {
                    folder = optionalFolder.get();
                }
            }
            if (!ObjectUtils.isEmpty(folder)) {
                byte[] bytes = documentoDTO.getDocumento();
                String documento = Arrays.toString(bytes);
                log.info("Document Name: {}", docName);
                log.info("Document Content: {}", documento);
                Map<String, Object> properties = new HashMap<>();
                properties.put(PropertyIds.OBJECT_TYPE_ID, "D:cmcor:CM_DocumentoPersonalizado");
                properties.put(PropertyIds.NAME, documentoDTO.getNombreDocumento());
                properties.put(PropertyIds.CONTENT_STREAM_MIME_TYPE, documentoDTO.getTipoDocumento());
                ContentStream contentStream = new ContentStreamImpl(documentoDTO.getNombreDocumento(), BigInteger.valueOf(bytes.length), documentoDTO.getTipoDocumento(), new ByteArrayInputStream(bytes));
                Document document = folder.createDocument(properties, contentStream, VersioningState.MAJOR);
                documentoDTO = transformarDocumento(document);
                final String idDoc = document.getPropertyValue(PropertyIds.OBJECT_ID);
                documentoDTO.setNroRadicado(idDoc.split(";")[0]);
                return Optional.of(documentoDTO);
            }
        }
        return Optional.empty();
    }

    private List<Folder> getUDListByDependencyCode(final String dependencyCode, final Session session) {
        final List<Folder> folderList = new ArrayList<>();
        final String query = "SELECT * FROM cmcor:CM_Unidad_Documental" +
                " WHERE " + ConstantesECM.CMCOR_DEP_CODIGO + " = '" + dependencyCode + "'";
        final ItemIterable<QueryResult> queryResults = session.query(query, false);
        for (QueryResult queryResult :
                queryResults) {
            final String objectId = queryResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
            final Folder udFolder = (Folder) session.getObject(session.createObjectId(objectId));
            folderList.add(folderList.size(), udFolder);
        }
        return folderList;
    }

    private Optional<Folder> crearUnidadDocumentalFolder(UnidadDocumentalDTO unidadDocumentalDTO, Session session) throws SystemException {
        Optional<Folder> optionalFolder = getUDFolderById(unidadDocumentalDTO.getId(), session);
        if (optionalFolder.isPresent()) {
            log.error("Ya existe una unidad documental con el id {}", unidadDocumentalDTO.getId());
            throw new SystemException("Ya existe la unidad documental con id " + unidadDocumentalDTO.getId());
        }

        final String dependenciaCode = unidadDocumentalDTO.getCodigoDependencia();

        if (ObjectUtils.isEmpty(dependenciaCode)) {
            log.error("La Unidad Documental {} no contiene el codigo de la Dependencia", unidadDocumentalDTO);
            throw new SystemException("La Unidad Documental no contiene el codigo de la Dependencia");
        }

        final String nombreUnidadDocumental = unidadDocumentalDTO.getNombreUnidadDocumental();

        if (ObjectUtils.isEmpty(nombreUnidadDocumental)) {
            log.error("La Unidad Documental no contiene Nombre");
            throw new SystemException("La Unidad Documental no contiene Nombre");
        }

        String query = "SELECT * FROM " + ConstantesECM.CMCOR + "" + configuracion.getPropiedad(ConstantesECM.CLASE_DEPENDENCIA) +
                " WHERE " + ConstantesECM.CMCOR_DEP_CODIGO + " = '" + dependenciaCode + "'" +
                " AND " + PropertyIds.OBJECT_TYPE_ID + " = 'F:" + ConstantesECM.CMCOR + "" + configuracion.getPropiedad(ConstantesECM.CLASE_DEPENDENCIA) + "'";

        final String codigoSerie = unidadDocumentalDTO.getCodigoSerie();
        final String codigoSubSerie = unidadDocumentalDTO.getCodigoSubSerie();

        ItemIterable<QueryResult> queryResults;
        boolean flag = false;

        if (ObjectUtils.isEmpty(codigoSerie) && ObjectUtils.isEmpty(codigoSubSerie)) {

            queryResults = session.query(query, false);

            if (queryResults.getPageNumItems() == 0) {
                log.error("No existe la dependencia {} en el ECM", dependenciaCode);
                throw new SystemException(ConstantesECM.NO_EXISTE_DEPENDENCIA + "'" + dependenciaCode + "' en el ECM");
            }
            flag = true;
        }
        if (!flag && !ObjectUtils.isEmpty(codigoSerie) && ObjectUtils.isEmpty(codigoSubSerie)) {

            query = "SELECT * FROM " +
                    ConstantesECM.CMCOR + "" + configuracion.getPropiedad(ConstantesECM.CLASE_SERIE) + " " +
                    "WHERE " + ConstantesECM.CMCOR_DEP_CODIGO + " = '" + dependenciaCode + "' " +
                    "AND " + PropertyIds.OBJECT_TYPE_ID + " = 'F:" + ConstantesECM.CMCOR + "" + configuracion.getPropiedad(ConstantesECM.CLASE_SERIE) + "' " +
                    "AND " + ConstantesECM.CMCOR_SER_CODIGO + " = '" + codigoSerie + "'";
            flag = true;
        }
        if (!flag && !ObjectUtils.isEmpty(codigoSerie) && !ObjectUtils.isEmpty(codigoSubSerie)) {

            query = "SELECT * FROM " +
                    ConstantesECM.CMCOR + "" + configuracion.getPropiedad(ConstantesECM.CLASE_SUBSERIE) + " " +
                    "WHERE " + ConstantesECM.CMCOR_DEP_CODIGO + " = '" + dependenciaCode + "' " +
                    "AND " + PropertyIds.OBJECT_TYPE_ID + " = 'F:" + ConstantesECM.CMCOR + "" + configuracion.getPropiedad(ConstantesECM.CLASE_SUBSERIE) + "' " +
                    "AND " + ConstantesECM.CMCOR_SER_CODIGO + " = '" + codigoSerie + "' " +
                    "AND " + ConstantesECM.CMCOR_SS_CODIGO + " = '" + codigoSubSerie + "'";
            flag = true;
        }
        if (!flag) {
            throw new SystemException("Se debe especificar el codigo de la serie cuando se especifica el de la sub serie");
        }

        queryResults = session.query(query, false);
        final Iterator<QueryResult> iterator = queryResults.iterator();
        if (iterator.hasNext()) {
            QueryResult queryResult = iterator.next();
            String objectId = queryResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
            Folder tmpFolder = (Folder) session.getObject(session.getObject(objectId));

            final ItemIterable<CmisObject> children = tmpFolder.getChildren();

            for (CmisObject cmisObject :
                    children) {
                String udName = Utilities.reemplazarCaracteresRaros(nombreUnidadDocumental);
                String folderName = Utilities.reemplazarCaracteresRaros(cmisObject.getName());
                if (cmisObject instanceof Folder && udName.equals(folderName)) {
                    log.error("Ya existe una Carpeta con el nombre {}", nombreUnidadDocumental);
                    throw new SystemException("Ya existe la unidad documental de nombre " + nombreUnidadDocumental + " en el ECM");
                }
            }

            final Map<String, Object> props = updateProperties(null, unidadDocumentalDTO);
            props.put(PropertyIds.OBJECT_TYPE_ID, "F:cmcor:" + configuracion.getPropiedad(ConstantesECM.CLASE_UNIDAD_DOCUMENTAL));
            log.info("Making the tmpFolder!!!");
            final Folder folder = tmpFolder.createFolder(props);
            log.info("Make success");
            return Optional.of(folder);
        }
        return Optional.empty();
    }

    private Optional<Carpeta> getFolderBy(final String classType, final String propertyName,
                                          final String value, final Session session) {
        String query = "SELECT * FROM";
        switch (classType) {
            case ConstantesECM.CLASE_BASE:
                query += " cmcor:CM_Unidad_Base WHERE " + propertyName + " = '" + value + "'" +
                        "  AND cmis:objectTypeId = 'F:cmcor:CM_Unidad_Base'";
                break;
            case ConstantesECM.CLASE_SEDE:
            case ConstantesECM.CLASE_DEPENDENCIA:
                query += " cmcor:CM_Unidad_Administrativa WHERE " + propertyName + " = '" + value + "'" +
                        "  AND cmis:objectTypeId = 'F:cmcor:CM_Unidad_Administrativa'";
                break;
            case ConstantesECM.CLASE_SERIE:
                query += " cmcor:CM_Serie WHERE " + propertyName + " = '" + value + "'" +
                        "  AND cmis:objectTypeId = 'F:cmcor:CM_Serie'";
                break;
            case ConstantesECM.CLASE_SUBSERIE:
                query += " cmcor:CM_Subserie WHERE " + propertyName + " = '" + value + "'" +
                        "  AND cmis:objectTypeId = 'F:cmcor:CM_Subserie'";
                break;
            default:
                return Optional.empty();
        }
        final ItemIterable<QueryResult> queryResults = session.query(query, false);
        final Iterator<QueryResult> iterator = queryResults.iterator();
        if (iterator.hasNext()) {
            final QueryResult next = iterator.next();
            final String objectId = next.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
            final CmisObject cmisObject = session.getObject(session.createObjectId(objectId));
            final Carpeta carpeta = new Carpeta();
            carpeta.setFolder((Folder) cmisObject);
            return Optional.of(carpeta);
        }
        return Optional.empty();
    }

    private List<SedeDTO> getSedeList(final Session session) throws SystemException {
        try {
            final Optional<Carpeta> optionalCarpeta = getFolderBy(ConstantesECM.CLASE_BASE, ConstantesECM.CMCOR_UB_CODIGO, "000", session);
            final List<SedeDTO> sedeDTOS = new ArrayList<>();
            if (optionalCarpeta.isPresent()) {
                Folder folder = optionalCarpeta.get().getFolder();
                final ItemIterable<CmisObject> children = folder.getChildren();
                children.forEach(objectChild -> {
                    final String dependencyCode = objectChild.getPropertyValue(ConstantesECM.CMCOR_DEP_CODIGO);
                    SedeDTO baseDTO = new SedeDTO();
                    baseDTO.setNombreBase(objectChild.getName());
                    baseDTO.setCodigoBase(dependencyCode);
                    baseDTO.setCodigoSede(dependencyCode);
                    baseDTO.setNombreSede(objectChild.getName());
                    sedeDTOS.add(sedeDTOS.size(), baseDTO);
                });
            }
            return sedeDTOS;
        } catch (Exception e) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    private Optional<ContenidoDependenciaTrdDTO> getDependenciaTrdDTO(String query, Session session) {
        final ItemIterable<QueryResult> results = session.query(query, false);
        final List<SerieDTO> serieLista = new ArrayList<>();
        final List<SubSerieDTO> subSerieLista = new ArrayList<>();
        final List<OrganigramaDTO> organigramaDTOS = new ArrayList<>();
        results.forEach(queryResult -> {
            final String objectId = queryResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
            final Folder folder = (Folder) session.getObject(session.createObjectId(objectId));
            final String name = folder.getName();
            if ("F:cmcor:CM_Subserie".equals(folder.getType().getId())) {
                final String code = folder.getPropertyValue(ConstantesECM.CMCOR_SS_CODIGO);
                final SubSerieDTO subSerie = SubSerieDTO.newInstance()
                        .codigoSubSerie(code).nombreSubSerie(name)
                        .codigoBase(code).nombreBase(name).build();
                subSerieLista.add(subSerieLista.size(), subSerie);
            } else if ("F:cmcor:CM_Serie".equals(folder.getType().getId())) {
                final String code = folder.getPropertyValue(ConstantesECM.CMCOR_SER_CODIGO);
                final SerieDTO serieDTO = SerieDTO.newInstance()
                        .codigoSerie(code).nombreSerie(name).codigoBase(code).nombreBase(name).build();
                serieLista.add(serieLista.size(), serieDTO);
            } else if ("F:cmcor:CM_Unidad_Administrativa".equals(folder.getType().getId())) {
                final String code = folder.getPropertyValue(ConstantesECM.CMCOR_DEP_CODIGO);
                final OrganigramaDTO organigramaDTO = OrganigramaDTO.newInstance()
                        .codOrg(code).nomOrg(name).codigoBase(code).nombreBase(name).build();
                organigramaDTOS.add(organigramaDTOS.size(), organigramaDTO);
            }
        });
        if (serieLista.isEmpty() && subSerieLista.isEmpty() && organigramaDTOS.isEmpty()) {
            return Optional.empty();
        }
        final ContenidoDependenciaTrdDTO dependenciaTrdDTO = ContenidoDependenciaTrdDTO.newInstance()
                .listaSerie(serieLista).listaSubSerie(subSerieLista).listaDependencia(organigramaDTOS)
                .listaSede(new ArrayList<>()).build();
        return Optional.of(dependenciaTrdDTO);
    }

    private String[] getSerieSubSerie(Folder folder, Session session) {
        final String subSerieCode = folder.getPropertyValue(ConstantesECM.CMCOR_SS_CODIGO);
        Optional<Carpeta> folderBySubSerieCode = Optional.empty();
        if (!StringUtils.isEmpty(subSerieCode)) {
            folderBySubSerieCode = getFolderBy(ConstantesECM.CLASE_SUBSERIE, ConstantesECM.CMCOR_SS_CODIGO, subSerieCode, session);
        }
        final String serieCode = folder.getPropertyValue(ConstantesECM.CMCOR_SER_CODIGO);
        Optional<Carpeta> folderBySerieCode = Optional.empty();
        if (!StringUtils.isEmpty(serieCode)) {
            folderBySerieCode = getFolderBy(ConstantesECM.CLASE_SERIE, ConstantesECM.CMCOR_SER_CODIGO, serieCode, session);
        }
        final String subSerieName = folderBySubSerieCode.isPresent() ?
                folderBySubSerieCode.get().getFolder().getName() : "";
        final String serieName = folderBySerieCode.isPresent() ?
                folderBySerieCode.get().getFolder().getName() : "";
        return new String[] {serieName, subSerieName};
    }
}