package co.com.soaint.ecm.business.boundary.documentmanager;

import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Configuracion;
import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Utilities;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentControl;
import co.com.soaint.ecm.domain.entity.Carpeta;
import co.com.soaint.ecm.domain.entity.Conexion;
import co.com.soaint.ecm.uti.SystemParameters;
import co.com.soaint.foundation.canonical.ecm.*;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import lombok.NoArgsConstructor;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.runtime.ObjectIdImpl;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisBaseException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConstraintException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisContentAlreadyExistsException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.*;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by Dasiel
 */

@BusinessControl
@NoArgsConstructor
public class ContentControlAlfresco implements ContentControl {
    @Autowired
    Configuracion configuracion;


    @Value("${claseSubserie}")
    private static String aclaseSubserie;

    private static final Logger logger = LogManager.getLogger(ContentControlAlfresco.class.getName());

    private static final String CLASE_BASE = "claseBase";
    private static final String CLASE_DEPENDENCIA = "claseDependencia";
    private static final String CLASE_SERIE = "claseSerie";
    private static final String CLASE_SUBSERIE = "claseSubserie";
    private static final String CMCOR = "cmcor:";
    private static final String CMCOR_CODIGOUNIDADAMINPADRE = "cmcor:CodigoUnidadAdminPadre";
    private static final String CMCOR_CODIGODEPENDENCIA = "cmcor:CodigoDependencia";
    private static final String ECM_ERROR = "ECM_ERROR";
    private static final String ECM_ERROR_DUPLICADO = "ECM ERROR DUPLICADO";
    private static final String EXISTE_CARPETA = "Existe la Carpeta: ";
    private static final String COMUNICACIONES_INTERNAS_RECIBIDAS = "Comunicaciones Oficiales Internas Recibidas ";
    private static final String COMUNICACIONES_INTERNAS_ENVIADAS = "Comunicaciones Oficiales Internas Enviadas ";
    private static final String COMUNICACIONES_EXTERNAS_RECIBIDAS = "Comunicaciones Oficiales Externas Recibidas ";
    private static final String COMUNICACIONES_EXTERNAS_ENVIADAS = "Comunicaciones Oficiales Externas Enviadas ";
    private static final String TIPO_COMUNICACION_INTERNA = "0231.02311_Comunicaciones Oficiales Internas";
    private static final String TIPO_COMUNICACION_EXTERNA = "0231.02312_Comunicaciones Oficiales Externas";
    private static final String ERROR_TIPO_EXCEPTION = "### Error tipo Exception----------------------------- :";
    private static final String ERROR_TIPO_IO = "### Error tipo IO----------------------------- :";
    private static final String CONTENT_DISPOSITION = "Content-Disposition";
    private static final String DOCUMENTO = "documento";
    private static final String APPLICATION_PDF = "application/pdf";
    private static final String PRODUCCION_DOCUMENTAL = "PRODUCCION DOCUMENTAL ";
    private static final String AVISO_CREA_DOC = "### Se va a crear el documento..";
    private static final String AVISO_CREA_DOC_ID = "### Documento creado con id ";


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
    private Carpeta crearCarpeta(Carpeta folder, String nameOrg, String codOrg, String classDocumental, Carpeta folderFather) {
        Carpeta newFolder = null;
        try {

            logger.info("### Creando Carpeta.. con clase documental:" + classDocumental);
            Map<String, String> props = new HashMap<>();
            //Se define como nombre de la carpeta nameOrg
            props.put(PropertyIds.NAME, nameOrg);

            //Se estable como codigo unidad administrativa Padre el codigo de la carpeta padre

            switch (classDocumental) {
                case CLASE_BASE:
                    llenarPropiedadesCarpeta("cmcor:CodigoBase", CLASE_BASE, props, codOrg);
                    break;
                case CLASE_DEPENDENCIA:
                    llenarPropiedadesCarpeta(CMCOR_CODIGODEPENDENCIA, CLASE_DEPENDENCIA, props, codOrg, folderFather);
                    break;
                case CLASE_SERIE:
                    llenarPropiedadesCarpeta("cmcor:CodigoSerie", CLASE_SERIE, props, codOrg, folderFather);
                    break;
                case CLASE_SUBSERIE:
                    llenarPropiedadesCarpeta("cmcor:CodigoSubserie", CLASE_SUBSERIE, props, codOrg, folderFather);

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
     * @param metadatosDocumentosDTO Objeto que contiene los metadatos del documento dentro del ECM
     * @param session                Objeto de conexion
     * @return Objeto de tipo response que devuleve el documento
     */
    @Override
    public Response descargarDocumento(MetadatosDocumentosDTO metadatosDocumentosDTO, Session session) throws IOException {


        MensajeRespuesta response = new MensajeRespuesta();
        ArrayList<MetadatosDocumentosDTO> versionesLista = new ArrayList<>();
        try {

            logger.info("Se entra al metodo de descargar el documento");
            Document doc = (Document) session.getObject(metadatosDocumentosDTO.getIdDocumento());
            File file = null;
            if (metadatosDocumentosDTO.getVersionLabel() != null) {
                List<Document> versions = doc.getAllVersions();
                for (Document version : versions) {
                    logger.info("Se procede a devolver el documento" + metadatosDocumentosDTO.getNombreDocumento());
                    file = getFile(metadatosDocumentosDTO, versionesLista, version);

                }
                return Response.ok(file)
                        .header(CONTENT_DISPOSITION, "attachment; filename=" + metadatosDocumentosDTO.getNombreDocumento()) //optional
                        .build();
            } else {
                file = convertInputStreamToFile(doc.getContentStream());
                return Response.ok(file)
                        .header(CONTENT_DISPOSITION, "attachment; filename=" + metadatosDocumentosDTO.getNombreDocumento()) //optional
                        .build();
            }
        } catch (Exception e) {
            response.setCodMensaje("2222");
            response.setMensaje("Error en la obtención del documento: " + e.getMessage());
            logger.error("Error en la obtención del documento: ", e);

            return Response.serverError().build();
        }
    }

    /**
     * Metodo para retornal el archivo
     *
     * @param metadatosDocumentosDTO Objeto que contiene los metadatos
     * @param versionesLista         Listado por el que se va a buscar
     * @param version                Version del documento que se esta buscando
     * @return Objeto file
     * @throws IOException
     */
    private File getFile(MetadatosDocumentosDTO metadatosDocumentosDTO, ArrayList<MetadatosDocumentosDTO> versionesLista, Document version) throws IOException {
        File fileAux = null;
        if (version.getVersionLabel().equals(metadatosDocumentosDTO.getVersionLabel())) {
            metadatosDocumentosDTO.setNombreDocumento(version.getName());
            versionesLista.add(metadatosDocumentosDTO);

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
            String queryString = "SELECT cmis:objectId FROM cmis:folder WHERE cmis:name = '" + nombreCarpeta + "'" + " and (cmis:objectTypeId = 'F:cmcor:CM_Unidad_Base' or cmis:objectTypeId = 'F:cmcor:CM_Serie' or cmis:objectTypeId = 'F:cmcor:CM_Subserie' or cmis:objectTypeId = 'F:cmcor:CM_Unidad_Administrativa')";
            ItemIterable<QueryResult> results = session.query(queryString, false);
            for (QueryResult qResult : results) {
                String objectId = qResult.getPropertyValueByQueryName("cmis:objectId");
                folder.setFolder((Folder) session.getObject(session.createObjectId(objectId)));
            }
        } catch (Exception e) {
            logger.error("*** Error al obtenerCarpetas *** ", e);
        }

        return folder;

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

                if (contentItem instanceof Folder && ("CM_Serie".equals(contentItem.getDescription()) || "CM_Subserie".equals(contentItem.getDescription()) || "CM_Unidad_Administrativa".equals(contentItem.getDescription()))) {
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
        List<Carpeta> listaCarpeta;
        Conexion conexion = obtenerConexion();
        listaCarpeta = obtenerCarpetasHijasDadoPadre(folderFather);
        Iterator<Carpeta> iterator;
        if (listaCarpeta != null) {
            iterator = listaCarpeta.iterator();
            while (iterator.hasNext()) {
                Carpeta aux = iterator.next();

                Carpeta carpeta = obtenerCarpetaPorNombre(aux.getFolder().getName(), conexion.getSession());
                String description = carpeta.getFolder().getDescription();
                if (description.equals(configuracion.getPropiedad(CLASE_DEPENDENCIA))) {
                    if (aux.getFolder().getPropertyValue(CMCOR + configuracion.getPropiedad("metadatoCodDependencia")) != null &&
                            aux.getFolder().getPropertyValue(CMCOR + configuracion.getPropiedad("metadatoCodDependencia")).equals(codFolder)) {
                        folderReturn = aux;
                    }
                } else if (description.equals(configuracion.getPropiedad(CLASE_SERIE))) {
                    if (aux.getFolder().getPropertyValue(CMCOR + configuracion.getPropiedad("metadatoCodSerie")) != null &&
                            aux.getFolder().getPropertyValue(CMCOR + configuracion.getPropiedad("metadatoCodSerie")).equals(codFolder)) {
                        folderReturn = aux;
                    }
                } else if (description.equals(configuracion.getPropiedad(CLASE_SUBSERIE))) {
                    logger.info("Entro a clase subserie cargando los valores");
                    if (aux.getFolder().getPropertyValue(CMCOR + configuracion.getPropiedad("metadatoCodSubserie")) != null &&
                            aux.getFolder().getPropertyValue(CMCOR + configuracion.getPropiedad("metadatoCodSubserie")).equals(codFolder)) {
                        folderReturn = aux;
                    }
                }
            }
        }
        return folderReturn;
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
                                folderFather = crearCarpeta(folder, organigrama.getNomOrg(), organigrama.getCodOrg(), CLASE_BASE, null);
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
                            folderSon = crearCarpeta(folderFather, organigrama.getNomOrg(), organigrama.getCodOrg(), CLASE_DEPENDENCIA, folderFather);
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
                            folderSon = crearCarpeta(folderFatherContainer, nombreSerie, dependencias.getCodSerie(), CLASE_SERIE, folderFather);
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
                                folderSon = crearCarpeta(folderFather, nombreSubserie, dependencias.getCodSubSerie(), "claseSubserie", folderFather);
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
     * Metodo para subir documentos al Alfresco
     *
     * @param session          Objeto de conexion a Alfresco
     * @param nombreDocumento  Nombre del documento que se va a crear
     * @param documento        Documento que se va a subir
     * @param tipoComunicacion Tipo de comunicacion que puede ser Externa o Interna
     * @return Devuelve el id de la carpeta creada
     * @throws IOException Excepcion ante errores de entrada/salida
     */
    @Override
    public MensajeRespuesta subirDocumento(Session session, String nombreDocumento, MultipartFormDataInput documento, String tipoComunicacion) throws IOException {

        logger.info("Se entra al metodo subirDocumento");

        MensajeRespuesta response = new MensajeRespuesta();
        String idDocumento;
        Map<String, List<InputPart>> uploadForm = documento.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get(DOCUMENTO);

        String fileName;
        MetadatosDocumentosDTO metadatosDocumentosDTO = new MetadatosDocumentosDTO();
        metadatosDocumentosDTO.setTipoDocumento(APPLICATION_PDF);
        for (InputPart inputPart : inputParts) {

            // Retrieve headers, read the Content-Disposition header to obtain the original name of the file
            MultivaluedMap<String, String> headers = inputPart.getHeaders();
            String[] contentDispositionHeader = headers.getFirst(CONTENT_DISPOSITION).split(";");
            for (String name : contentDispositionHeader) {
                if (name.trim().startsWith("filename")) {
                    String[] tmp = name.split("=");
                    fileName = tmp[1].trim().replaceAll("\"", "");
                    logger.info("El nombre del fichero es: " + fileName);
                }
            }
            InputStream inputStream = null;

            try {
                inputStream = inputPart.getBody(InputStream.class, null);
            } catch (IOException e) {
                logger.error(ERROR_TIPO_IO, e);
            }

            assert inputStream != null;
            byte[] bytes = IOUtils.toByteArray(inputStream);

            //Se definen las propiedades del documento a subir
            Map<String, Object> properties = new HashMap<>();

            properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document,P:cm:titled");
            properties.put(PropertyIds.NAME, nombreDocumento);

            buscarCrearCarpeta(session, metadatosDocumentosDTO, response, bytes, properties, PRODUCCION_DOCUMENTAL);

            try {
                //Se obtiene la carpeta dentro del ECM al que va a ser subido el documento
                new Carpeta();
                Carpeta folderAlfresco;
                logger.info("### Se elige la carpeta donde se va a guardar el documento a radicar..");

                if (configuracion.getPropiedad("acomunicacionExterna").equals(tipoComunicacion)) {

                    folderAlfresco = obtenerCarpetaPorNombre("100101.00302_COMUNICACION_EXTERNA", session);
                } else if (configuracion.getPropiedad("acomunicacioninterna").equals(tipoComunicacion)) {

                    folderAlfresco = obtenerCarpetaPorNombre("100100.00301_COMUNICACION_INTERNA", session);
                } else {
                    folderAlfresco = obtenerCarpetaPorNombre("100100.00303_PLANTILLAS", session);
                }
                logger.info("### Se elige la carpeta donde se va a guardar el documento a radicar.." + folderAlfresco.getFolder().getName());
                VersioningState vs = VersioningState.MAJOR;
                ContentStream contentStream = new ContentStreamImpl(nombreDocumento, BigInteger.valueOf(bytes.length), metadatosDocumentosDTO.getTipoDocumento(), new ByteArrayInputStream(bytes));
                //Se crea el documento
                logger.info(AVISO_CREA_DOC);
                Document newDocument = folderAlfresco.getFolder().createDocument(properties, contentStream, vs);

                idDocumento = newDocument.getId();
                String[] parts = idDocumento.split(";");
                idDocumento = parts[0];
                response.setCodMensaje("0000");
                response.setMensaje(idDocumento);

                logger.info(AVISO_CREA_DOC_ID + idDocumento);
            } catch (CmisContentAlreadyExistsException ccaee) {
                logger.error(ECM_ERROR_DUPLICADO, ccaee);
                response.setCodMensaje("1111");
                response.setMensaje(configuracion.getPropiedad(ECM_ERROR_DUPLICADO));
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

        return response;
    }


    /**
     * Metodo para obtener documentos asociados a un documento principal en Alfresco
     *
     * @param session    Objeto de conexion a Alfresco
     * @param idDocPadre Documento que se va a subir
     * @return Devuelve el listado de documentos asociados al id de documento padre
     * @throws IOException Excepcion ante errores de entrada/salida
     */
    @Override
    public MensajeRespuesta obtenerDocumentosAdjuntos(Session session, String idDocPadre) throws IOException {

        logger.info("Se entra al metodo obtenerDocumentosAdjuntos");

        MensajeRespuesta response = new MensajeRespuesta();
        try {

            //Obtener el documentosAdjuntos
            String principalAdjuntos = "SELECT * FROM cmcor:CM_DocumentoPersonalizado" +
                    " WHERE( cmis:objectId = '" + idDocPadre + "'" +
                    " OR cmcor:xIdentificadorDocPrincipal = '" + idDocPadre + "')";

            ItemIterable<QueryResult> resultsPrincipalAdjunto = session.query(principalAdjuntos, false);
            ArrayList<MetadatosDocumentosDTO> documentosLista = new ArrayList<>();
            for (QueryResult qResult : resultsPrincipalAdjunto) {

                MetadatosDocumentosDTO metadatosDocumentosDTO = new MetadatosDocumentosDTO();

                String[] parts = qResult.getPropertyValueByQueryName("cmis:objectId").toString().split(";");
                String idDocumento = parts[0];

                metadatosDocumentosDTO.setIdDocumento(idDocumento);
                if (qResult.getPropertyValueByQueryName("cmcor:xIdentificadorDocPrincipal") != null) {
                    metadatosDocumentosDTO.setIdDocumentoPadre(idDocPadre);
                    metadatosDocumentosDTO.setTipoPadreAdjunto(qResult.getPropertyValueByQueryName("cmcor:xTipo").toString());
                } else {
                    metadatosDocumentosDTO.setTipoPadreAdjunto("Principal");
                }
                metadatosDocumentosDTO.setNombreDocumento(qResult.getPropertyValueByQueryName("cmis:name"));
                GregorianCalendar newGregCal = qResult.getPropertyValueByQueryName("cmis:creationDate");
                metadatosDocumentosDTO.setFechaCreacion(newGregCal.getTime());
                metadatosDocumentosDTO.setTipoDocumento(qResult.getPropertyValueByQueryName("cmis:contentStreamMimeType").toString());
                metadatosDocumentosDTO.setTamano(qResult.getPropertyValueByQueryName("cmis:contentStreamLength").toString());

                documentosLista.add(metadatosDocumentosDTO);

            }
            response.setCodMensaje("0000");
            response.setMensaje("success");
            response.setMetadatosDocumentosDTOList(documentosLista);

        } catch (Exception e) {
            response.setCodMensaje("2222");
            response.setMensaje("Error en la obtención de los documentos adjuntos: " + e.getMessage());
            logger.error("Error en la obtención de los documentos adjuntos: ", e);
            response.setMetadatosDocumentosDTOList(new ArrayList<MetadatosDocumentosDTO>());
        }
        logger.info("Se sale del metodo obtenerDocumentosAdjuntos con respuesta: " + response.toString());
        return response;


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

        ArrayList<MetadatosDocumentosDTO> versionesLista = new ArrayList<>();
        try {
            //Obtener documento dado id
            Document doc = (Document) session.getObject(idDoc);
            List<Document> versions = doc.getAllVersions();
            for (Document version : versions) {
                MetadatosDocumentosDTO metadatosDocumentosDTO = new MetadatosDocumentosDTO();
                metadatosDocumentosDTO.setNombreDocumento(version.getName());
                metadatosDocumentosDTO.setVersionLabel(version.getVersionLabel());
                metadatosDocumentosDTO.setTamano(String.valueOf(version.getContentStreamLength()));
                metadatosDocumentosDTO.setIdDocumento(idDoc);
                metadatosDocumentosDTO.setTipoDocumento(version.getContentStreamMimeType());
                versionesLista.add(metadatosDocumentosDTO);

            }

            response.setCodMensaje("0000");
            response.setMensaje("success");
            response.setMetadatosDocumentosDTOList(versionesLista);

        } catch (Exception e) {
            response.setCodMensaje("2222");
            response.setMensaje("Error en la obtención de las versiones del documento: " + e.getMessage());
            response.setMetadatosDocumentosDTOList(new ArrayList<MetadatosDocumentosDTO>());
            logger.error("Error en la obtención de las versiones del documento: ", e);
        }
        logger.info("Se sale del metodo obtenerVersionesDocumento con respuesta: " + response.toString());
        return response;


    }

    /**
     * Metodo para subir documentos al Alfresco
     *
     * @param session                Objeto de conexion a Alfresco
     * @param documento              Documento que se va a subir
     * @param metadatosDocumentosDTO Objeto que contiene los metadatos de los documentos.
     * @param selector               Selector que dice donde se va a gauardar el documento
     * @return Devuelve el id de la carpeta creada
     * @throws IOException Excepcion ante errores de entrada/salida
     */
    @Override
    public MensajeRespuesta subirDocumentoPrincipalAdjunto(Session session, MultipartFormDataInput documento, MetadatosDocumentosDTO metadatosDocumentosDTO, String selector) throws IOException {

        logger.info("Se entra al metodo subirDocumentoPrincipalAdjunto");

        MensajeRespuesta response = new MensajeRespuesta();
        Map<String, List<InputPart>> uploadForm = documento.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get(DOCUMENTO);
        String fileName;
        metadatosDocumentosDTO.setTipoDocumento(APPLICATION_PDF);
        for (InputPart inputPart : inputParts) {

            // Retrieve headers, read the Content-Disposition header to obtain the original name of the file
            MultivaluedMap<String, String> headers = inputPart.getHeaders();
            String[] contentDispositionHeader = headers.getFirst(CONTENT_DISPOSITION).split(";");
            for (String name : contentDispositionHeader) {
                if (name.trim().startsWith("filename")) {
                    String[] tmp = name.split("=");
                    fileName = tmp[1].trim().replaceAll("\"", "");
                    logger.info("El nombre del fichero principal/adjunto es: " + fileName);
                }
            }

            InputStream inputStream = null;
            try {
                inputStream = inputPart.getBody(InputStream.class, null);

            } catch (IOException e) {
                logger.error(ERROR_TIPO_IO, e);
            }

            assert inputStream != null;
            byte[] bytes = IOUtils.toByteArray(inputStream);
            //Se definen las propiedades del documento a subir
            Map<String, Object> properties = new HashMap<>();
            properties.put(PropertyIds.OBJECT_TYPE_ID, "D:cmcor:CM_DocumentoPersonalizado");
            //En caso de que sea documento adjunto se le pone el id del documento principal dentro del parametro cmcor:xIdentificadorDocPrincipal
            if (metadatosDocumentosDTO.getIdDocumentoPadre() != null) {
                properties.put("cmcor:xIdentificadorDocPrincipal", metadatosDocumentosDTO.getIdDocumentoPadre());
                properties.put("cmcor:xTipo", "Anexo");
            }

            properties.put(PropertyIds.NAME, metadatosDocumentosDTO.getNombreDocumento());

            if ("PD".equals(selector)) {
                buscarCrearCarpeta(session, metadatosDocumentosDTO, response, bytes, properties, PRODUCCION_DOCUMENTAL);
            } else {
                buscarCrearCarpetaRadicacion(session, metadatosDocumentosDTO, response, bytes, properties, selector);
            }
        }

        return response;
    }

    /**
     * Metodo para subir/versionar documentos al Alfresco
     *
     * @param session                Objeto de conexion a Alfresco
     * @param documento              Documento que se va a subir/versionar
     * @param metadatosDocumentosDTO Objeto que contiene los metadatos de los documentos.
     * @param selector               parametro que indica donde se va a guardar el documento
     * @return Devuelve el id de la carpeta creada
     * @throws IOException Excepcion ante errores de entrada/salida
     */
    @Override
    public MensajeRespuesta subirVersionarDocumentoGenerado(Session session, MultipartFormDataInput documento, MetadatosDocumentosDTO metadatosDocumentosDTO, String selector) throws IOException {

        logger.info("Se entra al metodo subirVersionarDocumentoGenerado");

        MensajeRespuesta response = new MensajeRespuesta();
        List<MetadatosDocumentosDTO> metadatosDocumentosDTOList = new ArrayList<>();
        Map<String, List<InputPart>> uploadForm = documento.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get(DOCUMENTO);
        for (InputPart inputPart : inputParts) {

            InputStream inputStream = null;
            try {
                inputStream = inputPart.getBody(InputStream.class, null);

            } catch (IOException e) {
                logger.error(ERROR_TIPO_IO, e);
            }

            assert inputStream != null;
            byte[] bytes = IOUtils.toByteArray(inputStream);
            if ("html".equals(metadatosDocumentosDTO.getTipoDocumento())) {
                metadatosDocumentosDTO.setTipoDocumento("text/html");
            } else {
                metadatosDocumentosDTO.setTipoDocumento(APPLICATION_PDF);
            }

            if ("none".equals(metadatosDocumentosDTO.getIdDocumento())) {

                //Se definen las propiedades del documento a subir
                Map<String, Object> properties = new HashMap<>();
                properties.put(PropertyIds.OBJECT_TYPE_ID, "D:cmcor:CM_DocumentoPersonalizado");
                properties.put(PropertyIds.CONTENT_STREAM_MIME_TYPE, metadatosDocumentosDTO.getTipoDocumento());
                properties.put(PropertyIds.NAME, metadatosDocumentosDTO.getNombreDocumento());

                if ("PD".equals(selector)) {
                    buscarCrearCarpeta(session, metadatosDocumentosDTO, response, bytes, properties, PRODUCCION_DOCUMENTAL);
                } else {
                    buscarCrearCarpetaRadicacion(session, metadatosDocumentosDTO, response, bytes, properties, selector);
                }
            } else {
                //Obtener documento dado id
                Document doc = (Document) session.getObject(metadatosDocumentosDTO.getIdDocumento());
                //Obtener el PWC (Private Working copy)
                Document pwc = (Document) session.getObject(doc.checkOut());

                ContentStream contentStream = new ContentStreamImpl(metadatosDocumentosDTO.getNombreDocumento(), BigInteger.valueOf(bytes.length), metadatosDocumentosDTO.getTipoDocumento(), new ByteArrayInputStream(bytes));

                // Check in the pwc
                try {
                    pwc.checkIn(false, null, contentStream, "nueva version");

                    response.setCodMensaje("0000");
                    response.setMensaje("Documento versionado correctamente");
                    metadatosDocumentosDTO.setVersionLabel(doc.getVersionLabel());
                    metadatosDocumentosDTOList.add(metadatosDocumentosDTO);
                    response.setMetadatosDocumentosDTOList(metadatosDocumentosDTOList);
                } catch (CmisBaseException e) {
                    logger.error("checkin failed, trying to cancel the checkout", e);
                    pwc.cancelCheckOut();
                    response.setCodMensaje("222222");
                    response.setMensaje("Error versionando documento: " + e);
                }
            }

        }

        return response;
    }

    /**
     * Metodo para buscar crear carpetas
     *
     * @param session                Objeto session
     * @param metadatosDocumentosDTO Objeto qeu contiene los metadatos
     * @param response               Mensaje de respuesta
     * @param bytes                  Contenido del documento
     * @param properties             propiedades de carpeta
     * @param carpetaCrearBuscar     Carpeta
     */
    private void buscarCrearCarpeta(Session session, MetadatosDocumentosDTO metadatosDocumentosDTO, MensajeRespuesta response, byte[] bytes, Map<String, Object> properties, String carpetaCrearBuscar) {
        String idDocumento;
        List<MetadatosDocumentosDTO> metadatosDocumentosDTOList = new ArrayList<>();
        try {
            //Se obtiene la carpeta dentro del ECM al que va a ser subido el documento
            new Carpeta();
            Carpeta folderAlfresco;
            logger.info("### Se elige la carpeta donde se va a guardar el documento principal..");
            logger.info("###------------ Se elige la sede donde se va a guardar el documento principal..");
            folderAlfresco = obtenerCarpetaPorNombre(metadatosDocumentosDTO.getSede(), session);
            logger.info("###------------------- Se obtienen todas las dependencias de la sede..");
            List<Carpeta> carpetasHijas = obtenerCarpetasHijasDadoPadre(folderAlfresco);

            for (Carpeta carpetaP : carpetasHijas) {
                logger.info("Se obtienen la dependencia referente a la sede" + carpetaP.getFolder().getName());
                if (carpetaP.getFolder().getName().equals(metadatosDocumentosDTO.getDependencia())) {
                    logger.info("Se busca si existe la carpeta de Produccion documental para el año en curso dentro de la dependencia " + metadatosDocumentosDTO.getDependencia());

                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);

                    logger.info("Se obtienen todas las carpetas dentro de la dependencia " + metadatosDocumentosDTO.getDependencia());
                    List<Carpeta> carpetasDeLaDependencia = obtenerCarpetasHijasDadoPadre(carpetaP);

                    boolean existe = false;
                    Carpeta carpetaTarget = new Carpeta();
                    for (Carpeta carpetaDependencia : carpetasDeLaDependencia) {
                        if (carpetaDependencia.getFolder().getName().equals(carpetaCrearBuscar + year)) {
                            logger.info(EXISTE_CARPETA + carpetaCrearBuscar + year);
                            carpetaTarget = carpetaDependencia;
                            existe = true;
                        }
                    }
                    if (!existe) {
                        logger.info("Se crea la Carpeta: " + carpetaCrearBuscar + year);
                        carpetaTarget = crearCarpeta(carpetaP, carpetaCrearBuscar + year, "11", CLASE_SUBSERIE, carpetaP);//TODO ver lo del codOrg
                    }

                    logger.info("Se llenan los metadatos del documento a crear");
                    VersioningState vs = VersioningState.MAJOR;
                    ContentStream contentStream = new ContentStreamImpl(metadatosDocumentosDTO.getNombreDocumento(), BigInteger.valueOf(bytes.length), metadatosDocumentosDTO.getTipoDocumento(), new ByteArrayInputStream(bytes));
                    logger.info(AVISO_CREA_DOC);
                    Document newDocument = carpetaTarget.getFolder().createDocument(properties, contentStream, vs);

                    idDocumento = newDocument.getId();
                    String[] parts = idDocumento.split(";");
                    idDocumento = parts[0];
                    metadatosDocumentosDTO.setVersionLabel(newDocument.getVersionLabel());
                    metadatosDocumentosDTO.setIdDocumento(idDocumento);
                    metadatosDocumentosDTOList.add(metadatosDocumentosDTO);
                    response.setMetadatosDocumentosDTOList(metadatosDocumentosDTOList);
                    //Creando el mensaje de respuesta
                    response.setCodMensaje("0000");
                    response.setMensaje("Documento añadido correctamente");
                    logger.info(AVISO_CREA_DOC_ID + idDocumento);
                }
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

    /**
     * Metodo para buscar crear carpetas de radicacion de entrada
     *
     * @param session                Objeto session
     * @param metadatosDocumentosDTO Objeto qeu contiene los metadatos
     * @param response               Mensaje de respuesta
     * @param bytes                  Contenido del documento
     * @param properties             propiedades de carpeta
     */
    private void buscarCrearCarpetaRadicacion(Session session, MetadatosDocumentosDTO metadatosDocumentosDTO, MensajeRespuesta response, byte[] bytes, Map<String, Object> properties, String tipoComunicacion) {
        String idDocumento;
        List<MetadatosDocumentosDTO> metadatosDocumentosDTOList = new ArrayList<>();
        try {
            //Se obtiene la carpeta dentro del ECM al que va a ser subido el documento
            new Carpeta();
            Carpeta folderAlfresco;
            logger.info("### Se elige la carpeta donde se va a guardar el documento radicado..");
            logger.info("###------------ Se elige la sede donde se va a guardar el documento radicado..");
            folderAlfresco = obtenerCarpetaPorNombre(metadatosDocumentosDTO.getSede(), session);
            logger.info("###------------------- Se obtienen todas las dependencias de la sede..");
            List<Carpeta> carpetasHijas = obtenerCarpetasHijasDadoPadre(folderAlfresco);

            String comunicacionOficial = "";
            String tipoComunicacionSelector;
            switch (tipoComunicacion) {
                case "IR": {
                    tipoComunicacionSelector = COMUNICACIONES_INTERNAS_RECIBIDAS;
                    comunicacionOficial = TIPO_COMUNICACION_INTERNA;
                    break;
                }
                case "IE": {
                    tipoComunicacionSelector = COMUNICACIONES_INTERNAS_ENVIADAS;
                    comunicacionOficial = TIPO_COMUNICACION_INTERNA;
                    break;
                }
                case "ER": {
                    tipoComunicacionSelector = COMUNICACIONES_EXTERNAS_RECIBIDAS;
                    comunicacionOficial = TIPO_COMUNICACION_EXTERNA;
                    break;
                }
                case "EE": {
                    tipoComunicacionSelector = COMUNICACIONES_EXTERNAS_ENVIADAS;
                    comunicacionOficial = TIPO_COMUNICACION_EXTERNA;
                    break;
                }
                default:
                    tipoComunicacionSelector = COMUNICACIONES_INTERNAS_RECIBIDAS;
                    break;
            }

            boolean existe = false;
            boolean existeAux = false;
            for (Carpeta carpetaP : carpetasHijas) {
                logger.info("Se obtienen la dependencia referente a la sede" + carpetaP.getFolder().getName());
                if (carpetaP.getFolder().getName().equals(metadatosDocumentosDTO.getDependencia())) {
                    logger.info("Se busca si existe la carpeta de COMUNICACIONES OFICIALES para el año en curso dentro de la dependencia " + metadatosDocumentosDTO.getDependencia());

                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);

                    logger.info("Se obtienen todas las carpetas dentro de la dependencia " + metadatosDocumentosDTO.getDependencia());
                    List<Carpeta> carpetasDeLaDependencia = obtenerCarpetasHijasDadoPadre(carpetaP);
                    logger.info("Cantidad de carpetas dentro de la dependencia: " + carpetasDeLaDependencia.size() + " dentro de la carpeta: "+carpetaP.getFolder().getName());

                    Carpeta carpetaTarget = new Carpeta();
                    for (Carpeta carpetaDependencia : carpetasDeLaDependencia) {
                        if (carpetaDependencia.getFolder().getName().contains("0231_COMUNICACIONES OFICIALES")) {
                            logger.info(EXISTE_CARPETA + carpetaDependencia.getFolder().getName());
                            carpetaTarget = carpetaDependencia;
                            existeAux = true;
                        }
                    }
                    if (existeAux) {
                        logger.info("Se obtienen todas las carpetas dentro de la carpeta " + carpetaTarget);
                        List<Carpeta> carpetasDeComunicacionOficial = obtenerCarpetasHijasDadoPadre(carpetaTarget);


                        for (Carpeta carpetaComuncacionOficial : carpetasDeComunicacionOficial) {
                            if (carpetaComuncacionOficial.getFolder().getName().contains(comunicacionOficial)) {
                                logger.info(EXISTE_CARPETA + carpetaComuncacionOficial.getFolder().getName());
                                carpetaTarget = carpetaComuncacionOficial;
                                existe = true;
                            }
                        }

                        int bandera = 0;
                        if (existe) {
                            logger.info("Se obtienen todas las carpetas dentro de la carpeta " + carpetaTarget.getFolder().getName());
                            List<Carpeta> carpetasDeComunicacionOficial1 = obtenerCarpetasHijasDadoPadre(carpetaTarget);
                            if (!carpetasDeComunicacionOficial1.isEmpty()) {
                                for (Carpeta carpetaComuncacionOficial : carpetasDeComunicacionOficial1) {
                                    if (carpetaComuncacionOficial.getFolder().getName().contains(tipoComunicacionSelector + year)) {
                                        logger.info(EXISTE_CARPETA + carpetaComuncacionOficial.getFolder().getName());
                                        carpetaTarget = carpetaComuncacionOficial;
                                        bandera++;
                                    }
                                }
                                if (bandera == 0) {
                                    carpetaTarget = crearCarpeta(carpetaTarget, tipoComunicacionSelector + year, "11", CLASE_SUBSERIE, carpetaP);
                                }
                            } else {
                                carpetaTarget = crearCarpeta(carpetaTarget, tipoComunicacionSelector + year, "11", CLASE_SUBSERIE, carpetaP);
                            }
                        }


                        logger.info("Se llenan los metadatos del documento a crear");
                        VersioningState vs = VersioningState.MAJOR;
                        ContentStream contentStream = new ContentStreamImpl(metadatosDocumentosDTO.getNombreDocumento(), BigInteger.valueOf(bytes.length), metadatosDocumentosDTO.getTipoDocumento(), new ByteArrayInputStream(bytes));
                        logger.info(AVISO_CREA_DOC);
                        Document newDocument = carpetaTarget.getFolder().createDocument(properties, contentStream, vs);

                        idDocumento = newDocument.getId();
                        String[] parts = idDocumento.split(";");
                        idDocumento = parts[0];
                        metadatosDocumentosDTO.setVersionLabel(newDocument.getVersionLabel());
                        metadatosDocumentosDTO.setIdDocumento(idDocumento);
                        metadatosDocumentosDTOList.add(metadatosDocumentosDTO);
                        response.setMetadatosDocumentosDTOList(metadatosDocumentosDTOList);
                        //Creando el mensaje de respuesta
                        response.setCodMensaje("0000");
                        response.setMensaje("Documento añadido correctamente");
                        logger.info(AVISO_CREA_DOC_ID + idDocumento);
                    }
                    response.setCodMensaje("0000");
                    response.setMensaje("Documento Creado Correctamente");
                }
            }
        } catch (CmisContentAlreadyExistsException ccaee) {
            logger.error(ECM_ERROR_DUPLICADO, ccaee);
            response.setCodMensaje("1111");
            response.setMensaje("El documento ya existe en el ECM");
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
            updateProperties.put("cmcor:NroRadicado", nroRadicado);
            updateProperties.put("cmcor:NombreRemitente", nombreRemitente);
            updateProperties.put("cmcor:TipologiaDocumental", tipologiaDocumental);

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
    private void llenarPropiedadesCarpeta(String tipoCarpeta, String clase, Map<String, String> props, String codOrg, Carpeta folderFather) {
        props.put(PropertyIds.OBJECT_TYPE_ID, "F:cmcor:" + configuracion.getPropiedad(clase));
        props.put(PropertyIds.DESCRIPTION, configuracion.getPropiedad(clase));
        props.put(tipoCarpeta, codOrg);

        if ("cmcor:CodigoSubserie".equals(tipoCarpeta)) {
            if (folderFather != null) {
                props.put(CMCOR_CODIGOUNIDADAMINPADRE, folderFather.getFolder().getPropertyValue("cmcor:CodigoSerie"));
            }
        } else {
            if (folderFather != null) {
                props.put(CMCOR_CODIGOUNIDADAMINPADRE, folderFather.getFolder().getPropertyValue(CMCOR_CODIGODEPENDENCIA));
            } else {
                props.put(CMCOR_CODIGOUNIDADAMINPADRE, codOrg);
            }
        }
    }

    /**
     * Metodo para llenar propiedades para crear carpeta
     *
     * @param tipoCarpeta tipo de carpeta
     * @param clase       clase de la carpeta
     * @param props       objeto de propiedades
     * @param codOrg      codigo
     */
    private void llenarPropiedadesCarpeta(String tipoCarpeta, String clase, Map<String, String> props, String codOrg) {
        props.put(PropertyIds.OBJECT_TYPE_ID, "F:cmcor:" + configuracion.getPropiedad(clase));
        props.put(PropertyIds.DESCRIPTION, configuracion.getPropiedad(clase));
        props.put(tipoCarpeta, codOrg);
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
            logger.info("Se procede a eliminar");
            ObjectId a = new ObjectIdImpl(idDoc);
            CmisObject object = session.getObject(a);
            Document delDoc = (Document) object;
            //Se borra el documento pero no todas las versiones solo la ultima
            delDoc.delete(false);
            logger.info("Se logro eliminar el documento");
            return Boolean.TRUE;
        } catch (CmisObjectNotFoundException e) {
            logger.error("No se pudo eliminar el documento :", e);
            return Boolean.FALSE;
        }
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
}




