package co.com.soaint.ecm.business.boundary.documentmanager;

import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Configuracion;
import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Utilities;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentControl;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.RecordControl;
import co.com.soaint.ecm.domain.entity.Carpeta;
import co.com.soaint.ecm.domain.entity.Conexion;
import co.com.soaint.ecm.util.SystemParameters;
import co.com.soaint.foundation.canonical.ecm.*;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.runtime.ObjectIdImpl;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by Dasiel
 */
@BusinessControl
public class ContentControlAlfresco implements ContentControl {

    private static final Logger logger = LogManager.getLogger(ContentControlAlfresco.class.getName());

    @Value("${claseSubserie}")
    private static String aclaseSubserie;

    private final Configuracion configuracion;
    private final RecordControl recordControl;

    @Autowired
    public ContentControlAlfresco(Configuracion configuracion, RecordControl recordControl) {
        this.configuracion = configuracion;
        this.recordControl = recordControl;
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
    private Carpeta crearCarpeta(Carpeta folder, String nameOrg, String codOrg, String classDocumental, Carpeta folderFather, String idOrgOfc) {
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
                    Folder tmpFolder = folderFather.getFolder();
                    String depCode = "";
                    if (contienePropiedad(tmpFolder, CMCOR_DEP_CODIGO))
                        depCode = folder.getFolder().getPropertyValue(CMCOR_DEP_CODIGO);
                    String serieCode = "";
                    if (contienePropiedad(tmpFolder, CMCOR_SER_CODIGO))
                        serieCode = folder.getFolder().getPropertyValue(CMCOR_SER_CODIGO);
                    String subSerieCode = "";
                    if (contienePropiedad(tmpFolder, CMCOR_SS_CODIGO))
                        subSerieCode = folder.getFolder().getPropertyValue(CMCOR_SS_CODIGO);
                    String codUnidadAdminPadre = "";
                    if (contienePropiedad(tmpFolder, CMCOR_DEP_CODIGO_UAP))
                        codUnidadAdminPadre = folder.getFolder().getPropertyValue(CMCOR_DEP_CODIGO_UAP);
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
                    formatoFinal = null;
                    break;
                } else {
                    formatoFinal.append(aFormatoCadenaArray);
                }
            }
        } catch (Exception e) {
            logger.error("*** Error al formatear nombre *** ", e);
        }
        return formatoFinal != null ? formatoFinal.toString() : "";
    }

    /**
     * Metodo para devolver documento para su visualización
     *
     * @param documentoDTO Objeto que contiene los metadatos del documento dentro del ECM
     * @param session      Objeto de conexion
     * @return Objeto de tipo response que devuleve el documento
     */
    @Override
    public MensajeRespuesta descargarDocumento(DocumentoDTO documentoDTO, Session session) throws IOException {
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


                file = getFile(documentoDTO, versionesLista, version.get());

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
     * @throws IOException
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
            String queryString = "SELECT " + PropertyIds.OBJECT_ID +
                    " FROM cmis:folder WHERE " + PropertyIds.NAME + " = '" + nombreCarpeta + "'" +
                    " and (cmis:objectTypeId = 'F:cmcor:CM_Unidad_Base' or cmis:objectTypeId = 'F:cmcor:CM_Serie'" +
                    " or cmis:objectTypeId = 'F:cmcor:CM_SubSerie' or cmis:objectTypeId = 'F:cmcor:CM_Unidad_Administrativa')";
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
    public MensajeRespuesta devolverSerieSubSerie(ContenidoDependenciaTrdDTO dependenciaTrdDTO, Session session) {
        Carpeta folder = new Carpeta();
        ContenidoDependenciaTrdDTO dependenciaTrdDTOSalida;
        List<SerieDTO> serieLista = new ArrayList<>();
        List<SubSerieDTO> subSerieLista = new ArrayList<>();
        MensajeRespuesta respuesta = new MensajeRespuesta();

        try {
            String queryString = "SELECT cmis:objectId FROM cmis:folder WHERE (cmis:objectTypeId = 'F:cmcor:CM_Unidad_Base' or cmis:objectTypeId = 'F:cmcor:CM_Serie' or cmis:objectTypeId = 'F:cmcor:CM_SubSerie' or cmis:objectTypeId = 'F:cmcor:CM_Unidad_Administrativa')";
            if (dependenciaTrdDTO.getIdOrgOfc() != null && !"".equals(dependenciaTrdDTO.getIdOrgOfc())) {
                queryString = "SELECT cmis:objectId FROM cmcor:CM_Serie WHERE " + CMCOR_DEP_CODIGO + " =  '" + dependenciaTrdDTO.getIdOrgOfc() + "'";
            }
            if (dependenciaTrdDTO.getIdOrgOfc() != null && dependenciaTrdDTO.getCodSerie() != null && !"".equals(dependenciaTrdDTO.getCodSerie())) {
                queryString += " and " + CMCOR_DEP_CODIGO_UAP + " = '" + dependenciaTrdDTO.getCodSerie() + "'";
            }

            ItemIterable<QueryResult> results = session.query(queryString, false);
            if (results.getPageNumItems() > 0) {
                for (QueryResult qResult : results) {
                    SerieDTO serie = new SerieDTO();
                    SubSerieDTO subSerie = new SubSerieDTO();
                    List<ContenidoDependenciaTrdDTO> listaSerieSubSerie = new ArrayList<>();
                    String objectId = qResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
                    folder.setFolder((Folder) session.getObject(session.createObjectId(objectId)));
                    if (folder.getFolder().getPropertyValue(CMCOR_SER_CODIGO) != null) {
                        serie.setCodigoSerie(folder.getFolder().getPropertyValue(CMCOR_SER_CODIGO));
                        serie.setNombreSerie(folder.getFolder().getPropertyValue(PropertyIds.NAME));
                        serieLista.add(serie);

                    }
                    //aki explota
                    String value = folder.getFolder().getPropertyValue(CMCOR_SS_CODIGO);
                    if (!ObjectUtils.isEmpty(value)) {
                        subSerie.setCodigoSubSerie(folder.getFolder().getPropertyValue(CMCOR_SS_CODIGO));
                        subSerie.setNombreSubSerie(folder.getFolder().getPropertyValue(PropertyIds.NAME));
                        subSerieLista.add(subSerie);
                    }
                    dependenciaTrdDTOSalida = dependenciaTrdDTO;
                    dependenciaTrdDTOSalida.setListaSerie(serieLista);
                    dependenciaTrdDTOSalida.setListaSubSerie(subSerieLista);
                    listaSerieSubSerie.add(dependenciaTrdDTO);
                    respuesta.setCodMensaje("0000");
                    respuesta.setMensaje("Series o Subseries devueltas correctamente");
                    respuesta.setContenidoDependenciaTrdDTOS(listaSerieSubSerie);
                }
            } else {
                respuesta.setCodMensaje("1111");
                respuesta.setMensaje("No se obtuvieron Series o Subseries por no existir para esta dependencia");
            }
        } catch (Exception e) {
            logger.error("*** Error al obtener las series o subseries *** ", e);
            respuesta.setCodMensaje("2223");
            respuesta.setMensaje("Error al obtener las series o subseries");
        }

        return respuesta;
    }

    /**
     * Metodo que devuelve las carpetas hijas de una carpeta
     *
     * @param carpetaPadre Carpeta a la cual se le van a buscar las carpetas hijas
     * @return Lista de carpetas resultantes de la busqueda
     */
    private List<Carpeta> obtenerCarpetasHijasDadoPadre(Carpeta carpetaPadre) {
        logger.info("### Obtener Carpetas Hijas Dado Padre: " + carpetaPadre.getFolder().getName());
        List<Carpeta> listaCarpetas = null;

        try {
            ItemIterable<CmisObject> listaObjetos = carpetaPadre.getFolder().getChildren();

            listaCarpetas = new ArrayList<>();
            //Lista de carpetas hijas
            for (CmisObject contentItem : listaObjetos) {

                if (contentItem instanceof Folder && ("CM_Serie".equals(contentItem.getDescription()) || "CM_SubSerie".equals(contentItem.getDescription()) || "CM_Unidad_Administrativa".equals(contentItem.getDescription()))) {
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
        if (listaCarpeta != null) {
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
        if (aux.getFolder().getPropertyValue(CMCOR + configuracion.getPropiedad(metadato)) != null &&
                aux.getFolder().getPropertyValue(CMCOR + configuracion.getPropiedad(metadato)).equals(codFolder)) {
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

            new Carpeta();
            Carpeta carpetaF;
            new Carpeta();
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
                        if (folderFather == null) {
                            if (obtenerCarpetaPorNombre(organigrama.getNomOrg(), conexion.getSession()).getFolder() != null) {
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
                        if (folderSon == null) {
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
                    if (folderSon == null) {
                        if (!"".equals(nombreSerie)) {
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
                    if (dependencias.getCodSubSerie() != null && !"".equals(dependencias.getCodSubSerie())) {
                        String nombreSubserie = formatearNombre(dependenciasArray, "formatoNombreSubserie");
                        folderSon = chequearCapetaPadre(folderFather, dependencias.getCodSubSerie());
                        if (folderSon == null) {
                            if (!"".equals(nombreSubserie)) {
                                logger.info("TRD --  Creando folder: " + nombreSubserie);
                                folderSon = crearCarpeta(folderFather, nombreSubserie, dependencias.getCodSubSerie(), "claseSubserie", folderFather, dependencias.getIdOrgOfc());
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
     * @throws IOException Excepcion ante errores de entrada/salida
     */
    @Override
    public MensajeRespuesta obtenerDocumentosAdjuntos(Session session, DocumentoDTO documento) throws IOException {

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
                documentoDTO.setNombreDocumento(qResult.getPropertyValueByQueryName(PropertyIds.NAME));
                GregorianCalendar newGregCal = qResult.getPropertyValueByQueryName(PropertyIds.CREATION_DATE);
                documentoDTO.setFechaCreacion(newGregCal.getTime());
                documentoDTO.setTipoDocumento(qResult.getPropertyValueByQueryName(PropertyIds.CONTENT_STREAM_MIME_TYPE));
                documentoDTO.setTamano(qResult.getPropertyValueByQueryName(PropertyIds.CONTENT_STREAM_LENGTH).toString());
                documentoDTO.setNroRadicado(qResult.getPropertyValueByQueryName(CMCOR_NRO_RADICADO));
                String tipo = qResult.getPropertyValueByQueryName(CMCOR_TIPO_DOCUMENTO);
                documentoDTO.setTipologiaDocumental(ObjectUtils.isEmpty(tipo) ? "" : tipo);
                documentoDTO.setNombreRemitente(qResult.getPropertyValueByQueryName(CMCOR_NOMBRE_REMITENTE) != null ? qResult.getPropertyValueByQueryName(CMCOR_NOMBRE_REMITENTE).toString() : null);

                documentosLista.add(documentoDTO);

            }
            response.setCodMensaje("0000");
            response.setMensaje("success");
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

    /**
     * Metodo para realizar búsquedas de documentos por metadatos
     *
     * @param session   Objeto de conexion
     * @param documento Objeto Documento
     * @return QueryResult
     */
    private ItemIterable<QueryResult> getPrincipalAdjuntosQueryResults(Session session, DocumentoDTO documento) {
        //Obtener el documentosAdjuntos
        String query = "SELECT * FROM cmcor:CM_DocumentoPersonalizado  ";
        boolean where = false;

        if (!ObjectUtils.isEmpty(documento.getIdDocumento())) {
            where = true;
            query += " WHERE " + PropertyIds.OBJECT_ID + " = '" + documento.getIdDocumento() + "'";
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
     * @throws IOException Excepcion ante errores de entrada/salida
     */
    @Override
    public MensajeRespuesta obtenerVersionesDocumento(Session session, String idDoc) throws IOException {

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
            response.setMensaje("success");
            response.setDocumentoDTOList(versionesLista);
        } catch (Exception e) {
            response.setCodMensaje("2222");
            response.setMensaje("Error en la obtención de las versiones del documento: " + e.getMessage());
            response.setDocumentoDTOList(new ArrayList<DocumentoDTO>());
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
     * @throws IOException Excepcion ante errores de entrada/salida
     */
    @Override
    public MensajeRespuesta subirDocumentoPrincipalAdjunto(Session session, DocumentoDTO documento, String selector) throws IOException {

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
     * Metodo para subir/versionar documentos al Alfresco
     *
     * @param session   Objeto de conexion a Alfresco
     * @param documento Documento que se va a subir/versionar
     * @param selector  parametro que indica donde se va a guardar el documento
     * @return Devuelve el id de la carpeta creada
     * @throws IOException Excepcion ante errores de entrada/salida
     */
    @Override
    public MensajeRespuesta subirVersionarDocumentoGenerado(Session session, DocumentoDTO documento, String selector) throws IOException {

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

        if (ObjectUtils.isEmpty(documento.getIdDocumento())) {

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
            documento.setTipoPadreAdjunto(doc.getPropertyValue(CMCOR_TIPO_DOCUMENTO));
            documento.setTamano(String.valueOf(doc.getContentStreamLength()));
            GregorianCalendar calendar = doc.getPropertyValue(PropertyIds.CREATION_DATE);
            documento.setFechaCreacion(calendar.getTime());
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
            new Carpeta();
            Carpeta folderAlfresco;
            logger.info("### Se elige la carpeta donde se va a guardar el documento principal..");
            logger.info("###------------ Se elige la sede donde se va a guardar el documento principal..");
            folderAlfresco = obtenerCarpetaPorNombre(documentoDTO.getSede(), session);

            if (folderAlfresco.getFolder() != null) {
                logger.info("###------------------- Se obtienen todas las dependencias de la sede..");
                List<Carpeta> carpetasHijas = obtenerCarpetasHijasDadoPadre(folderAlfresco);

                //Se busca si existe la carpeta de Produccion documental para el año en curso dentro de la dependencia
                Optional<Carpeta> dependencia = carpetasHijas.stream()
                        .filter(p -> p.getFolder().getName().equals(documentoDTO.getDependencia())).findFirst();

                logger.info("Se obtienen la dependencia referente a la sede" + dependencia);
                if (dependencia.isPresent()) {

                    logger.info("Se busca si existe la carpeta de Produccion documental para el año en curso dentro de la dependencia " + documentoDTO.getDependencia());
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    List<Carpeta> carpetasDeLaDependencia = obtenerCarpetasHijasDadoPadre(dependencia.get());

                    Carpeta carpetaTarget;

                    Optional<Carpeta> produccionDocumental = carpetasDeLaDependencia.stream()
                            .filter(p -> p.getFolder().getName().equals(carpetaCrearBuscar + year)).findFirst();
                    carpetaTarget = getCarpeta(carpetaCrearBuscar, dependencia, year, produccionDocumental);

                    if (PRODUCCION_DOCUMENTAL.equals(carpetaCrearBuscar)){
                        idDocumento = crearDocumentoDevolverId(documentoDTO, response, bytes, properties, documentoDTOList, carpetaTarget);
                        //Creando el mensaje de respuesta
                        response.setCodMensaje("0000");
                        response.setMensaje("Documento añadido correctamente");
                        logger.info(AVISO_CREA_DOC_ID + idDocumento);
                    }else if (DOCUMENTOS_APOYO.equals(carpetaCrearBuscar)){
                        crearLink(session,documentoDTO.getIdDocumento(),carpetaTarget);
                        response.setCodMensaje("0000");
                        response.setMensaje("Link añadido correctamente");
                    }
                } else {
                    logger.info(NO_EXISTE_DEPENDENCIA + documentoDTO.getDependencia());
                    response.setCodMensaje("4445");
                    response.setMensaje(NO_EXISTE_DEPENDENCIA + documentoDTO.getSede());
                }
            } else {
                logger.info(NO_EXISTE_SEDE + documentoDTO.getSede());
                response.setCodMensaje("4444");
                response.setMensaje(NO_EXISTE_SEDE + documentoDTO.getSede());
            }

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
            response.setMensaje(configuracion.getPropiedad(ECM_ERROR));
        }
    }
    /*private void buscarCrearCarpeta(Session session, DocumentoDTO documentoDTO, MensajeRespuesta response, byte[] bytes, Map<String, Object> properties, String carpetaCrearBuscar) {
        logger.info("MetaDatos: " + documentoDTO.toString());
        String idDocumento;
        List<DocumentoDTO> documentoDTOList = new ArrayList<>();
        try {
            //Se obtiene la carpeta dentro del ECM al que va a ser subido el documento
            new Carpeta();
            Carpeta folderAlfresco;
            logger.info("### Se elige la carpeta donde se va a guardar el documento principal..");
            logger.info("###------------ Se elige la sede donde se va a guardar el documento principal..");
            folderAlfresco = obtenerCarpetaPorNombre(documentoDTO.getSede(), session);

            if (folderAlfresco.getFolder() != null) {
                logger.info("###------------------- Se obtienen todas las dependencias de la sede..");
                List<Carpeta> carpetasHijas = obtenerCarpetasHijasDadoPadre(folderAlfresco);

                //Se busca si existe la dependencia
                Optional<Carpeta> dependencia = carpetasHijas.stream()
                        .filter(p -> p.getFolder().getName().equals(documentoDTO.getDependencia())).findFirst();

                logger.info("Se obtienen la dependencia referente a la sede: " + documentoDTO.getSede());
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
                    Map<String, Object> idMap = new HashMap<>();
                    idMap.put("idDocumento", idDocumento);
                    response.setResponse(idMap);
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
            }

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
            response.setMensaje(configuracion.getPropiedad(ECM_ERROR));
        }
    }*/

    private Carpeta getCarpeta(String carpetaCrearBuscar, Optional<Carpeta> dependencia, int year, Optional<Carpeta> produccionDocumental) {
        Carpeta carpetaTarget;
        if (produccionDocumental.isPresent()) {
            logger.info(EXISTE_CARPETA + carpetaCrearBuscar + year);
            carpetaTarget = produccionDocumental.get();
        } else {
            logger.info("Se crea la Carpeta: " + carpetaCrearBuscar + year);
            carpetaTarget = crearCarpeta(dependencia.get(), carpetaCrearBuscar + year, "11", CLASE_UNIDAD_DOCUMENTAL, dependencia.get(), null);
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
        Carpeta carpetaTarget;
        String idDocumento;
        if (comunicacionOficialFolder.isPresent()) {
            logger.info(EXISTE_CARPETA + comunicacionOficialFolder.get().getFolder().getName());

            List<Carpeta> carpetasDeComunicacionOficial = obtenerCarpetasHijasDadoPadre(comunicacionOficialFolder.get());

            Optional<Carpeta> comunicacionOficialInOut = carpetasDeComunicacionOficial.stream()
                    .filter(p -> p.getFolder().getName().contains(comunicacionOficial)).findFirst();


            if (!comunicacionOficialInOut.isPresent()) {

                Carpeta carpetaCreada = crearCarpeta(comunicacionOficialFolder.get(), comunicacionOficial, "11", CLASE_UNIDAD_DOCUMENTAL, comunicacionOficialFolder.get(), null);
                logger.info(EXISTE_CARPETA + carpetaCreada.getFolder().getName());

                List<Carpeta> carpetasDeComunicacionOficialDentro = obtenerCarpetasHijasDadoPadre(carpetaCreada);

                Optional<Carpeta> comunicacionOficialInOutDentro = carpetasDeComunicacionOficialDentro.stream()
                        .filter(p -> p.getFolder().getName().contains(tipoComunicacionSelector)).findFirst();
                carpetaTarget = comunicacionOficialInOutDentro.orElseGet(() -> crearCarpeta(carpetaCreada, tipoComunicacionSelector + year, "11", CLASE_UNIDAD_DOCUMENTAL, carpetaCreada, null));

            } else {
                logger.info(EXISTE_CARPETA + comunicacionOficialInOut.get().getFolder().getName());

                List<Carpeta> carpetasDeComunicacionOficialDentro = obtenerCarpetasHijasDadoPadre(comunicacionOficialInOut.get());

                Optional<Carpeta> comunicacionOficialInOutDentro = carpetasDeComunicacionOficialDentro.stream()
                        .filter(p -> p.getFolder().getName().contains(tipoComunicacionSelector)).findFirst();
                carpetaTarget = comunicacionOficialInOutDentro.orElseGet(() -> crearCarpeta(comunicacionOficialInOut.get(), tipoComunicacionSelector + year, "11", CLASE_UNIDAD_DOCUMENTAL, comunicacionOficialInOut.get(), null));
            }
            idDocumento = crearDocumentoDevolverId(documentoDTO, response, bytes, properties, documentoDTOList, carpetaTarget);
            //Creando el mensaje de respuesta
            response.setCodMensaje("0000");
            response.setMensaje("Documento añadido correctamente");
            logger.info(AVISO_CREA_DOC_ID + idDocumento);

        } else {
            response.setCodMensaje("3333");
            response.setMensaje("En esta sede y dependencia no esta permitido realizar radicaciones");
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
        if (documentoDTO.getNroRadicadoReferido() != null) {
            //Se concatenan los numeros de radicado referidos para guardarlos como string porque Alfresco no permite salvar listas
            properties.put(CMCOR_NUMERO_REFERIDO, String.join(SEPARADOR, documentoDTO.getNroRadicadoReferido()));
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
     * @throws IOException Excepcion ante errores de entrada/salida
     */
    @Override
    public MensajeRespuesta modificarMetadatosDocumento(Session session, String idDocumento, String nroRadicado, String tipologiaDocumental, String nombreRemitente) throws IOException {
        logger.info("### Modificar documento: " + idDocumento);
        MensajeRespuesta response = new MensajeRespuesta();
        try {

            ObjectId idDoc = new ObjectIdImpl(idDocumento);

            Map<String, Object> updateProperties = new HashMap<>();
            updateProperties.put(CMCOR_NRO_RADICADO, nroRadicado);
            updateProperties.put(CMCOR_NOMBRE_REMITENTE, nombreRemitente);
            updateProperties.put(CMCOR_TIPO_DOCUMENTO, tipologiaDocumental);

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

        if (!ObjectUtils.isEmpty(folderFather) && !ObjectUtils.isEmpty(folderFather.getFolder())) {

            props.put(PropertyIds.OBJECT_TYPE_ID, "F:cmcor:" + configuracion.getPropiedad(clase));
            props.put(PropertyIds.DESCRIPTION, configuracion.getPropiedad(clase));
            props.put(tipoCarpeta, codOrg);

            switch (tipoCarpeta) {
                case CMCOR_SS_CODIGO:
                    props.put(CMCOR_DEP_CODIGO_UAP, folderFather.getFolder().getPropertyValue(CMCOR_SER_CODIGO));
                    props.put(CMCOR_SER_CODIGO, folderFather.getFolder().getPropertyValue(CMCOR_SER_CODIGO));
                    props.put(CMCOR_DEP_CODIGO, idOrgOfc);
                    break;
                case CMCOR_SER_CODIGO:
                    props.put(CMCOR_DEP_CODIGO_UAP, folderFather.getFolder().getPropertyValue(CMCOR_DEP_CODIGO));
                    props.put(CMCOR_DEP_CODIGO, idOrgOfc);
                    break;
                case CMCOR_DEP_CODIGO:
                    props.put(CMCOR_DEP_CODIGO_UAP, codOrg);
                    props.put(CMCOR_DEP_CODIGO, codOrg);
                    break;
                default:
                    break;
            }
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
     * Metodo que crea las unidades documentales del ECM
     *
     * @param unidadDocumentalDTO Objeto dependencia que contiene los datos necesarios para realizar la busqueda
     * @param session             Objeto de conexion
     * @return MensajeRespuesta
     */
    @Override
    public MensajeRespuesta crearUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO, Session session) {

        logger.info("Executing crearUnidadDocumental method");

        final MensajeRespuesta response = new MensajeRespuesta();

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
                if (cmisObject instanceof Folder && nombreUnidadDocumental.equals(cmisObject.getName())) {
                    logger.error("Ya existe una Carpeta con el nombre {}", nombreUnidadDocumental);
                    response.setCodMensaje("2222");
                    response.setMensaje("Ya existe una Carpeta con el nombre " + nombreUnidadDocumental + " en el ECM");
                    return response;
                }
            }

            final Map<String, Object> props = new HashMap<>();
            //Se define como nombre de la carpeta nameOrg

            logger.info("Creating props to make the folder");
            props.put(CMCOR_UD_ID, unidadDocumentalDTO.getId());
            props.put(CMCOR_UD_ACCION, unidadDocumentalDTO.getAccion());
            props.put(CMCOR_UD_DESCRIPTOR_2, unidadDocumentalDTO.getDescriptor2());
            props.put(CMCOR_UD_CERRADA, unidadDocumentalDTO.isCerrada());
            props.put(CMCOR_UD_FECHA_CIERRE, unidadDocumentalDTO.getFechaCierre());
            props.put(CMCOR_UD_FECHA_INICIAL, unidadDocumentalDTO.getFechaExtremaInicial());
            props.put(CMCOR_UD_FECHA_FINAL, unidadDocumentalDTO.getFechaExtremaFinal());
            props.put(CMCOR_UD_SOPORTE, unidadDocumentalDTO.getSoporte());
            props.put(CMCOR_UD_INACTIVO, unidadDocumentalDTO.isInactivo());
            props.put(CMCOR_UD_UBICACION_TOPOGRAFICA, unidadDocumentalDTO.getUbicacionTopografica());
            props.put(CMCOR_UD_FASE_ARCHIVO, unidadDocumentalDTO.getFaseArchivo());
            props.put(CMCOR_UD_DESCRIPTOR_1, unidadDocumentalDTO.getDescriptor1());
            props.put(CMCOR_UD_CODIGO, unidadDocumentalDTO.getCodigoUnidadDocumental());
            props.put(CMCOR_UD_OBSERVACIONES, unidadDocumentalDTO.getObservaciones());
            props.put(CMCOR_SER_CODIGO, codigoSerie);
            props.put(CMCOR_SS_CODIGO, codigoSubSerie);
            props.put(CMCOR_DEP_CODIGO, dependenciaCode);
            props.put(CMCOR_DEP_CODIGO_UAP, folder.getPropertyValue(CMCOR_DEP_CODIGO));

            props.put(PropertyIds.NAME, nombreUnidadDocumental);
            props.put(PropertyIds.DESCRIPTION, configuracion.getPropiedad(CLASE_UNIDAD_DOCUMENTAL));
            props.put(PropertyIds.OBJECT_TYPE_ID, "F:cmcor:" + configuracion.getPropiedad(CLASE_UNIDAD_DOCUMENTAL));

            logger.info("Making the folder!!!");
            final Folder newFolder = folder.createFolder(props);
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
    }

    /**
     * Listar las Unidades Documentales del ECM
     *
     * @return Mensaje de respuesta
     */
    @Override
    public MensajeRespuesta listarUnidadesDocumentales(final UnidadDocumentalDTO dto,
                                                       final Session session) {
        final MensajeRespuesta respuesta = new MensajeRespuesta();
        try {
            String query = "SELECT * FROM " + CMCOR + "" + configuracion.getPropiedad(CLASE_UNIDAD_DOCUMENTAL);
            boolean where = false;

            if (!ObjectUtils.isEmpty(dto.getId())) {
                query += " WHERE " + CMCOR_UD_ID + " = '" + dto.getId() + "'";
                where = true;
            }
            if (!ObjectUtils.isEmpty(dto.getDescriptor2())) {
                query += (!where ? " WHERE " : " AND ") + CMCOR_UD_DESCRIPTOR_2 + " LIKE '%" + dto.getDescriptor2() + "%'";
                where = true;
            }
            if (dto.getFechaCierre() != null) {
                query += (!where ? " WHERE " : " AND ") + CMCOR_UD_FECHA_CIERRE + " = '" + dto.getFechaCierre() + "'";
                where = true;
            }
            if (!ObjectUtils.isEmpty(dto.getSoporte())) {
                query += (!where ? " WHERE " : " AND ") + CMCOR_UD_SOPORTE + " = '" + dto.getSoporte() + "'";
                where = true;
            }
            if (!ObjectUtils.isEmpty(dto.isInactivo())) {
                query += (!where ? " WHERE " : " AND ") + CMCOR_UD_INACTIVO + " = " + dto.isInactivo();
                where = true;
            }
            if (!ObjectUtils.isEmpty(dto.getDescriptor1())) {
                query += (!where ? " WHERE " : " AND ") + CMCOR_UD_DESCRIPTOR_1 + " LIKE '%" + dto.getDescriptor1() + "%'";
                where = true;
            }
            if (!ObjectUtils.isEmpty(dto.isCerrada())) {
                query += (!where ? " WHERE " : " AND ") + CMCOR_UD_CERRADA + " = " + dto.isCerrada();
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
            if (dto.getFechaExtremaInicial() != null) {
                query += (!where ? " WHERE " : " AND ") + CMCOR_UD_FECHA_INICIAL + " >= '" + dto.getFechaExtremaInicial() + "'";
                where = true;
            }
            if (dto.getFechaExtremaFinal() != null) {
                query += (!where ? " WHERE " : " AND ") + CMCOR_UD_FECHA_FINAL + " <= '" + dto.getFechaExtremaFinal() + "'";
            }

            logger.info("Executing query {}", query);
            final ItemIterable<QueryResult> queryResults;
            queryResults = session.query(query, false);

            final List<UnidadDocumentalDTO> unidadDocumentalDTOS;
            unidadDocumentalDTOS = new ArrayList<>();

            queryResults.forEach(queryResult -> {

                String objectId = queryResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
                Folder folder = (Folder)
                        session.getObject(session.getObject(objectId));

                unidadDocumentalDTOS.add(unidadDocumentalDTOS.size(),
                        transformarUnidadDocumental(folder));

            });

            respuesta.setMensaje("Listado seleccionado correctamente");
            respuesta.setCodMensaje("00000");
            Map<String, Object> map = new HashMap<>();
            map.put("unidadDocumental", unidadDocumentalDTOS);
            respuesta.setResponse(map);
            return respuesta;

        } catch (Exception e) {
            respuesta.setMensaje("Error al Listar las Unidades Documentales");
            respuesta.setCodMensaje("111111");
            return respuesta;
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
    public MensajeRespuesta listaDocumentoDTO(String idUnidadDocumental, Session session) {
        logger.info("Ejecutando el metodo MensajeRespuesta listaDocumentoDTO(String idUnidadDocumental, Session session)");

        final MensajeRespuesta mensajeRespuesta = new MensajeRespuesta();
        {
            Map<String, Object> responseMap = new HashMap<>();
            mensajeRespuesta.setResponse(responseMap);
        }

        if (ObjectUtils.isEmpty(idUnidadDocumental)) {
            logger.info("El ID de la Unidad Documental esta vacio");
            mensajeRespuesta.setMensaje("El ID de la Unidad Documental esta vacio");
            mensajeRespuesta.setCodMensaje("11111");
            return mensajeRespuesta;
        }

        String queryString = "SELECT * FROM " + CMCOR + configuracion.getPropiedad(CLASE_UNIDAD_DOCUMENTAL) +
                " WHERE " + CMCOR_UD_ID + " = '" + idUnidadDocumental + "'";

        final ItemIterable<QueryResult> queryResults = session.query(queryString, false);
        final Iterator<QueryResult> iterator = queryResults.iterator();

        if (iterator.hasNext()) {

            QueryResult queryResult = iterator.next();
            String objectId = queryResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
            Folder udFolder = (Folder) session.getObject(session.getObject(objectId));

            final List<DocumentoDTO> documentoDTOS = new ArrayList<>();
            final ItemIterable<CmisObject> folderChildren = udFolder.getChildren();
            final UnidadDocumentalDTO dto = transformarUnidadDocumental(udFolder);
            dto.setListaDocumentos(documentoDTOS);

            folderChildren.forEach(cmisObject -> {
                if (cmisObject.getBaseTypeId() == BaseTypeId.CMIS_DOCUMENT &&
                        cmisObject.getType().getId().equals("D:cmcor:CM_DocumentoPersonalizado")) {
                    documentoDTOS.add(documentoDTOS.size(), transformarDocumento((Document) cmisObject));
                }
            });

            logger.info("Documentos devuelto satisfactoriamente {}", documentoDTOS);
            mensajeRespuesta.setCodMensaje("00000");
            mensajeRespuesta.setMensaje("OK");
            mensajeRespuesta.getResponse().put("unidadDocumentalDTO", dto);
            return mensajeRespuesta;
        }

        logger.warn("Ningun resultado coincide con el criterio de busqueda");
        mensajeRespuesta.setMensaje("Ningun resultado coincide con el criterio de busqueda");
        mensajeRespuesta.setCodMensaje("11111");
        return mensajeRespuesta;
    }

    /**
     * Metodo para listar los documentos de una Unidad Documental
     *
     * @param idDocumento Id Documento
     * @param session     Objeto conexion de Alfresco
     * @return MensajeRespuesta con los detalles del documento
     */
    @Override
    public MensajeRespuesta obtenerDetallesDocumentoDTO(String idDocumento, Session session) {
        logger.info("Ejecutando el metodo MensajeRespuesta obtenerDetallesDocumentoDTO(String idDocumento, Session session)");

        final MensajeRespuesta mensajeRespuesta = new MensajeRespuesta();

        if (ObjectUtils.isEmpty(idDocumento)) {
            logger.info("El ID del documento esta vacio");
            mensajeRespuesta.setMensaje("El ID del documento esta vacio");
            mensajeRespuesta.setCodMensaje("11111");
            return mensajeRespuesta;
        }

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
        mensajeRespuesta.setCodMensaje("00000");
        mensajeRespuesta.setResponse(mapResponsonse);

        return mensajeRespuesta;
    }

    @Override
    public MensajeRespuesta detallesUnidadDocumental(String idUnidadDocumental, Session session) {
        logger.info("Ejecutando el metodo detallesUnidadDocumental(String idUnidadDocumental, Session session)");

        UnidadDocumentalDTO dto = buscarUnidadDocumntalPorId(idUnidadDocumental, session);
        MensajeRespuesta respuesta = new MensajeRespuesta();

        if (ObjectUtils.isEmpty(dto)) {
            logger.error("Ningun resultado coincide con el id enviado {} ", idUnidadDocumental);
            respuesta.setCodMensaje("11111");
            respuesta.setMensaje("Ningun resultado coincide con el id enviado");
            return respuesta;
        }

        respuesta.setCodMensaje("0000");
        respuesta.setMensaje("Detalles Unidad Documental");
        Map<String, Object> map = new HashMap<>();
        map.put("unidadDocumental", dto);
        respuesta.setResponse(map);

        return respuesta;
    }

    /**
     * Metodo para abrir una unidad documental
     *
     * @param idUnidadDocumental Id Unidad Documental
     * @param session            Objeto Conexion de alfresco
     * @return MensajeRespuesta con la Unidad Documntal abierta
     */
    @Override
    public MensajeRespuesta abrirUnidadDocumental(String idUnidadDocumental, Session session) {
        logger.info("Ejecutando el metodo abrirUnidadDocumental(String idUnidadDocumental, Session session)");

        UnidadDocumentalDTO dto = buscarUnidadDocumntalPorId(idUnidadDocumental, session);
        MensajeRespuesta respuesta = new MensajeRespuesta();

        if (ObjectUtils.isEmpty(dto)) {
            logger.error("Ningun resultado coincide con el id enviado {}", idUnidadDocumental);
            respuesta.setCodMensaje("11111");
            respuesta.setMensaje("Ningun resultado coincide con el id enviado");
            return respuesta;
        }

        return null;
    }

    /**
     * Metodo para cerrar una unidad documental
     *
     * @param idUnidadDocumental     Id Unidad Documental
     * @param session Objeto Conexion de alfresco
     * @return MensajeRespuesta
     */
    @Override
    public MensajeRespuesta cerrarUnidadDocumental(String idUnidadDocumental, Session session) throws SystemException {

        final MensajeRespuesta listaDocumentoDTO = listaDocumentoDTO(idUnidadDocumental, session);
        final Map<String, Object> response = listaDocumentoDTO.getResponse();

        if (response.containsKey("unidadDocumentalDTO")) {
            UnidadDocumentalDTO dto = (UnidadDocumentalDTO) response.get("unidadDocumentalDTO");
            if (recordControl.cerrarUnidadDocumentalRecord(dto)) {
                return MensajeRespuesta.newInstance()
                        .codMensaje("0000")
                        .mensaje("Unidad Documental cerrada correctamente")
                        .build();
            }
        }
        return MensajeRespuesta.newInstance()
                .codMensaje("1111")
                .mensaje("Ocurrio un error al cerrar la unidad documental")
                .build();
    }

    /**
     * Metodo para reactivar una unidad documental
     *
     * @param idUnidadDocumental     Id Unidad Documental
     * @param session Objeto Conexion de alfresco
     * @return MensajeRespuesta
     */
    @Override
    public MensajeRespuesta reactivarUnidadDocumental(String idUnidadDocumental, Session session) {
        return null;
    }

    /**
     * Metodo para crear Link a un documento dentro de la carpeta Documentos de apoyo
     *
     * @param session        Objeto de conexion a Alfresco
     * @param documento      Objeto qeu contiene los datos del documento
     * @return
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
     * @param session     Objeto session
     * @param idDocumento Identificador del dcumento
     * @param carpetaLink Carpeta donde se va a crear el link
     * @return
     */

    private void crearLink(Session session, String idDocumento, Carpeta carpetaLink) {

        logger.info("Se entra al metodo crearLink");

        Map<String, Object> properties = new HashMap<>();
        properties.put(PropertyIds.BASE_TYPE_ID, BaseTypeId.CMIS_ITEM.value());

        // define a name and a description for the link
        properties.put(PropertyIds.NAME, "Name_for_the.link");
        properties.put("cmis:description", "test create link");
        properties.put(PropertyIds.OBJECT_TYPE_ID, "I:app:filelink");

        //define the destination node reference
        properties.put("cm:destination", "workspace://SpacesStore/" + idDocumento);
        //Se crea el link
        session.createItem(properties, carpetaLink.getFolder());

        logger.info("Se crea el link y se sale del método crearLink");
    }

    /**
     * Metodo que busca una Unidad Documental en el ECM
     *
     * @param idUnidadDocumental Id Documento
     * @param session            Objeto conexion de Alfresco
     * @return UnidadDocumentalDTO si existe null si no existe
     */
    private UnidadDocumentalDTO buscarUnidadDocumntalPorId(String idUnidadDocumental, Session session) {

        if (!ObjectUtils.isEmpty(idUnidadDocumental)) {

            String queryString = "SELECT * FROM " + CMCOR + configuracion.getPropiedad(CLASE_UNIDAD_DOCUMENTAL) +
                    " WHERE " + CMCOR_UD_ID + " = '" + idUnidadDocumental + "'";

            final ItemIterable<QueryResult> queryResults = session.query(queryString, false);
            final Iterator<QueryResult> iterator = queryResults.iterator();

            if (iterator.hasNext()) {
                QueryResult queryResult = iterator.next();
                String objectId = queryResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
                Folder udFolder = (Folder) session.getObject(session.getObject(objectId));
                return transformarUnidadDocumental(udFolder);
            }
        }
        return null;
    }

    /**
     * Convierte contentStream a File
     *
     * @param contentStream contentStream
     * @return Un objeto File
     * @throws IOException En caso de error
     */
    private static File convertInputStreamToFile(ContentStream contentStream) throws IOException {

        File file = File.createTempFile(contentStream.getFileName(), "");

        OutputStream out = null;
        try (InputStream inputStream = contentStream.getStream()) {
            out = new FileOutputStream(file);

            int read;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

        } catch (IOException e) {
            logger.error("Error al convertir el contentStream a File", e);
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }

        file.deleteOnExit();
        return file;
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

    /**
     * Busca la existencia de una propiedad en un Objeto CMIS
     *
     * @param cmisObject   Objeto cmis
     * @param propertyName Propiedad a buscar
     * @return boolean
     */
    private boolean contienePropiedad(CmisObject cmisObject, String propertyName) {
        List<Property<?>> properties = cmisObject.getProperties();
        final Optional<Property<?>> first = properties.stream().
                filter(property -> property.getQueryName().equals(propertyName)).findFirst();
        return first.isPresent();
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

                String[] parts = qResult.getPropertyValueByQueryName("cmis:objectId").toString().split(";");
                String idDocumento = parts[0];

                logger.info("Se procede a eliminar el documento: " + qResult.getPropertyByQueryName("cmis:name").getValues().get(0).toString());
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
}