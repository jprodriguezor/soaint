package co.com.soaint.ecm.business.boundary.documentmanager;

import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Configuracion;
import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Utilities;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentControl;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.IRecordServices;
import co.com.soaint.ecm.domain.entity.*;
import co.com.soaint.ecm.util.ConstantesECM;
import co.com.soaint.ecm.util.SystemParameters;
import co.com.soaint.foundation.canonical.ecm.*;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.runtime.ObjectIdImpl;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.UnfileObject;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisBaseException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisContentAlreadyExistsException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.chemistry.opencmis.commons.impl.MimeTypes;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by Dasiel
 */
@Log4j2
@BusinessControl
public final class ContentControlAlfresco implements ContentControl {

    private static final long serialVersionUID = 1L;

    @Autowired
    private IRecordServices recordServices;

    @Autowired
    private Configuracion configuracion;

    @Autowired
    private ContentControlUtilities utilities;

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
            parameter.put(SessionParameter.ATOMPUB_URL, SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_ENDPOINT_ECM));
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
            File file = null;

            if (documentoDTO.getVersionLabel() != null) {
                List<Document> versions = doc.getAllVersions();
                //Filtrar la version correcta dentro de las versiones del documento para obtener el file
                Optional<Document> version = versions.stream()
                        .filter(p -> p.getVersionLabel().equals(documentoDTO.getVersionLabel())).findFirst();
                if (version.isPresent()) {
                    file = utilities.getFile(documentoDTO, versionesLista, version.get());
                }

            } else {
                file = utilities.convertInputStreamToFile(doc.getContentStream());
            }
            log.info("Se procede a devolver el documento" + documentoDTO.getNombreDocumento());
            if (null != file) {
                byte[] data = FileUtils.readFileToByteArray(file);
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
            final Optional<ContenidoDependenciaTrdDTO> optionalTrdDTO = utilities.getDependenciaTrdDTO(queryString, session);
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

        Optional<Folder> optionalFolder = utilities.crearUnidadDocumentalFolder(unidadDocumentalDTO, session);
        if (!optionalFolder.isPresent()) {
            log.error(ConstantesECM.NO_RESULT_MATCH);
            throw new SystemException(ConstantesECM.NO_RESULT_MATCH);
        }
        response.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
        response.setMensaje(ConstantesECM.OPERACION_COMPLETADA_SATISFACTORIAMENTE);
        response.getResponse().put("unidadDocumental", utilities.transformarUnidadDocumental(optionalFolder.get()));
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
        final List<UnidadDocumentalDTO> unidadDocumentalDTOS = utilities.listarUnidadesDocumentales(new ArrayList<>(), dto, session);
        final String messaje = unidadDocumentalDTOS.isEmpty() ? "El sistema no encuentra la unidad documental que está " +
                "buscando. Por favor, solicite su creación" : "Listado seleccionado correctamente";
        respuesta.setMensaje(messaje);
        respuesta.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
        Map<String, Object> map = new HashMap<>();
        map.put("unidadDocumental", unidadDocumentalDTOS);
        respuesta.setResponse(map);
        return respuesta;
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
            mapResponsonse.put("documentoDTO", utilities.transformarDocumento((Document) cmisObjectDocument));
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
            respuesta.getResponse().put("unidadDocumental", utilities.transformarUnidadDocumental(optionalFolder.get()));
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
                UnidadDocumentalDTO documentalDTO = utilities.transformarUnidadDocumental(unidadDocumentalFolder);
                List<DocumentoDTO> documentoDTOS = getDocumentsFromFolder(unidadDocumentalFolder);
                if (!fullDocuments) {
                    optionalFolder = recordServices.getRecordFolderByUdId(idUnidadDocumental);
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
    public Optional<UnidadDocumentalDTO> getUDById(String idUnidadDocumental, Session session) {
        log.info("Ejecutando UnidadDocumentalDTO getUDById(String idUnidadDocumental, Session session)");
        Optional<Folder> optionalFolder = getUDFolderById(idUnidadDocumental, session);
        if (optionalFolder.isPresent()) {
            Folder folder = optionalFolder.get();
            return Optional.of(utilities.transformarUnidadDocumental(folder));
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
            Map<String, Object> stringObjectMap = utilities.updateProperties(unidadDocumentalFolder, unidadDocumentalDTO);
            response = !stringObjectMap.isEmpty();
        }
        return response;
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

            carpetaF = utilities.obtenerCarpetaPorNombre(carpetaFuente, session);
            carpetaD = utilities.obtenerCarpetaPorNombre(carpetaDestino, session);
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

                        folderFather = utilities.chequearCapetaPadre(folder, organigrama.getCodOrg());
                        if (ObjectUtils.isEmpty(folderFather)) {
                            if (!ObjectUtils.isEmpty(utilities.obtenerCarpetaPorNombre(organigrama.getNomOrg(), conexion.getSession()).getFolder())) {
                                folderFather = utilities.obtenerCarpetaPorNombre(organigrama.getNomOrg(), conexion.getSession());
                                log.info("Organigrama -- ya existe la carpeta: " + folderFather.getFolder().getName());
                            } else {
                                log.info("Organigrama --  Creando folder: " + organigrama.getNomOrg());
                                folderFather = utilities.crearCarpeta(folder, organigrama.getNomOrg(), organigrama.getCodOrg(), ConstantesECM.CLASE_BASE, null, null);
                            }

                        } else {
                            log.info("Organigrama --  La carpeta ya esta creado: " + folderFather.getFolder().getName());
                            //Actualización de folder
                            if (!(organigrama.getNomOrg().equals(folderFather.getFolder().getName()))) {
                                log.info("Se debe actualizar al nombre: " + organigrama.getNomOrg());
                                utilities.actualizarNombreCarpeta(folderFather, organigrama.getNomOrg());
                            } else {
                                log.info("Organigrama --  El folder ya esta creado: " + organigrama.getNomOrg());
                            }
                        }
                        bandera++;
                        folderFatherContainer = folderFather;

                    } else {
                        folderSon = utilities.chequearCapetaPadre(folderFather, organigrama.getCodOrg());
                        if (ObjectUtils.isEmpty(folderSon)) {
                            log.info("Organigrama --  Creando folder: " + organigrama.getNomOrg());
                            folderSon = utilities.crearCarpeta(folderFather, organigrama.getNomOrg(), organigrama.getCodOrg(), ConstantesECM.CLASE_DEPENDENCIA, folderFather, null);
                        } else {
                            log.info("Organigrama --  El folder ya esta creado2: " + folderSon.getFolder().getName());
                            //Actualización de folder
                            if (!(organigrama.getNomOrg().equals(folderSon.getFolder().getName()))) {
                                log.info("Se debe actualizar al nombre: " + organigrama.getNomOrg());
                                utilities.actualizarNombreCarpeta(folderSon, organigrama.getNomOrg());
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
                    String nombreSerie = utilities.formatearNombre(dependenciasArray, "formatoNombreSerie");
                    folderSon = utilities.chequearCapetaPadre(folderFatherContainer, dependencias.getCodSerie());
                    if (ObjectUtils.isEmpty(folderSon)) {
                        if (!ObjectUtils.isEmpty(nombreSerie)) {
                            log.info("TRD --  Creando folder: " + nombreSerie);
                            folderSon = utilities.crearCarpeta(folderFatherContainer, nombreSerie, dependencias.getCodSerie(), ConstantesECM.CLASE_SERIE, folderFather, dependencias.getIdOrgOfc());
                        } else {
                            log.info("El formato para el nombre de la serie no es valido.");
                            break;
                        }
                    } else {
                        //Actualización de folder
                        if (!(nombreSerie.equals(folderSon.getFolder().getName()))) {
                            log.info("Se debe cambiar el nombre: " + nombreSerie);
                            utilities.actualizarNombreCarpeta(folderSon, nombreSerie);
                        } else {
                            log.info("TRD --  El folder ya esta creado: " + nombreSerie);
                        }
                    }
                    folderFather = folderSon;
                    if (!ObjectUtils.isEmpty(dependencias.getCodSubSerie())) {
                        String nombreSubserie = utilities.formatearNombre(dependenciasArray, "formatoNombreSubserie");
                        folderSon = utilities.chequearCapetaPadre(folderFather, dependencias.getCodSubSerie());
                        if (ObjectUtils.isEmpty(folderSon)) {
                            if (!ObjectUtils.isEmpty(nombreSubserie)) {
                                log.info("TRD --  Creando folder: " + nombreSubserie);
                                folderSon = utilities.crearCarpeta(folderFather, nombreSubserie, dependencias.getCodSubSerie(), ConstantesECM.CLASE_SUBSERIE, folderFather, dependencias.getIdOrgOfc());
                            } else {
                                log.info("El formato para el nombre de la subserie no es valido.");
                                break;
                            }
                        } else {

                            //Actualización de folder
                            if (!(nombreSubserie.equals(folderSon.getFolder().getName()))) {
                                log.info("Se debe cambiar el nombre: " + nombreSubserie);
                                utilities.actualizarNombreCarpeta(folderSon, nombreSubserie);
                            } else {
                                log.info("TRD --  El folder ya esta creado: " + nombreSubserie);
                            }
                        }
                        folderFather = folderSon;
                    }
                }
                bandera = 0;
                response.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
                response.setMensaje("OK");
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
            ItemIterable<QueryResult> resultsPrincipalAdjunto = utilities.getPrincipalAdjuntosQueryResults(session, documento);

            List<DocumentoDTO> documentosLista = new ArrayList<>();
            for (QueryResult qResult : resultsPrincipalAdjunto) {
                /*String objectId = qResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
                objectId = objectId.indexOf(';') != -1 ? objectId.split(";")[0] : objectId;

                Document document = (Document) session.getObject(session.createObjectId(objectId));
                if (document.isLatestVersion()) {

                    documentosLista.add(documentosLista.size(), utilities.transformarDocumento(document));
                }*/

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
                documentoDTO.setVersionLabel(document.getVersionLabel());

                documentoDTO.setDocumento(utilities.getDocumentBytes(document));
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
    public MensajeRespuesta subirDocumentoPrincipalAdjunto(Session session, DocumentoDTO documento, String selector) throws SystemException {
        log.info("Se entra al metodo subirDocumentoPrincipalAdjunto");
        final String idDocPrincipal = documento.getIdDocumentoPadre();
        if (StringUtils.isEmpty(idDocPrincipal)) {
            DocumentoDTO dto;
            final String nroRadicado = documento.getNroRadicado();
            if (!StringUtils.isEmpty(selector) && "PD".equals(selector.toUpperCase())) {
                dto = utilities.subirDocumentoPrincipalPD(documento, session);
            } else {
                if (StringUtils.isEmpty(nroRadicado)) {
                    throw new SystemException("No se ha especificado el numero de radicado");
                }
                SelectorType selectorType = SelectorType.getSelectorBy(nroRadicado);
                if (null == selectorType) {
                    throw new SystemException("El selector no valido '" + nroRadicado + "'");
                }
                dto = utilities.subirDocumentoPrincipalRadicacion(documento, selectorType, session);
            }
            final List<DocumentoDTO> documentoDTOS = new ArrayList<>();
            documentoDTOS.add(documentoDTOS.size(), dto);
            return MensajeRespuesta.newInstance()
                    .codMensaje(ConstantesECM.SUCCESS_COD_MENSAJE)
                    .mensaje("Documento añadido correctamente")
                    .documentoDTOList(documentoDTOS)
                    .build();
        }
        return subirDocumentoAnexo(documento, session);
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

        utilities.buscarCrearCarpeta(session, documento, response, documento.getDocumento(), properties, ConstantesECM.DOCUMENTOS_APOYO);

        log.info("Se sale del metodo crearLinkDocumentosApoyo");
        return response;
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
            log.error("El Folder introducido es null");
            return documentoDTOS;
        }
        for (CmisObject cmisObject :
                folder.getChildren()) {
            if (cmisObject instanceof Document && cmisObject.getType().getId().startsWith("D:cmcor:")) {
                documentoDTOS.add(documentoDTOS.size(), utilities.transformarDocumento((Document) cmisObject));
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
            for (DocumentoDTO documentoDTO : listaDocumentos) {
                final CmisObject documentObj = session.getObject(session.createObjectId(documentoDTO.getIdDocumento()));
                if (documentObj.getType().getId().equals("D:" + ConstantesECM.CMCOR + "CM_DocumentoPersonalizado")) {
                    final Document document = (Document) documentObj;
                    final Folder sourceFolder = utilities.getFolderFrom(document);
                    if (null != sourceFolder) {
                        final String docName = documentoDTO.getNombreDocumento();
                        if (!document.getName().equals(docName)) {
                            document.rename(docName);
                        }
                        document.move(sourceFolder, targetFolder);

                        /*final boolean isCommunication = folderName.startsWith(ConstantesECM.COMUNICACION_EXTERNA)
                                || folderName.startsWith(ConstantesECM.COMUNICACION_INTERNA);
                        final String docName = documentoDTO.getNombreDocumento();
                        if (!StringUtils.isEmpty(docName) && !docName.equals(document.getName())) {
                            document.rename(docName);
                        }
                        if (isCommunication) {
                            utilities.crearLink(targetFolder, document, session);
                        } else {
                            document.move(sourceFolder, targetFolder);
                            *//*final List<Folder> parents = document.getParents();
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
                            });*//*
                        }*/
                        final Map<String, Object> properties = new HashMap<>();
                        Calendar startDate = targetFolder.getPropertyValue(ConstantesECM.CMCOR_UD_FECHA_INICIAL);
                        if (ObjectUtils.isEmpty(startDate)) {
                            properties.put(ConstantesECM.CMCOR_UD_FECHA_INICIAL, GregorianCalendar.getInstance());
                        }
                        properties.put(ConstantesECM.CMCOR_UD_FECHA_FINAL, GregorianCalendar.getInstance());
                        targetFolder.updateProperties(properties);
                    }
                }
            }
            /*listaDocumentos.forEach(documentoDTO -> {

            });*/
            unidadDocumentalDTO = utilities.transformarUnidadDocumental(targetFolder);
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
            documento.setTipoDocumento(MimeTypes.getMIMEType("html"));
        } else {
            documento.setTipoDocumento(MimeTypes.getMIMEType("pdf"));
        }

        if (documento.getIdDocumento() == null) {

            //Se definen las propiedades del documento a subir

            properties.put(PropertyIds.OBJECT_TYPE_ID, "D:cmcor:CM_DocumentoPersonalizado");
            properties.put(PropertyIds.CONTENT_STREAM_MIME_TYPE, documento.getTipoDocumento());
            final String docName = !StringUtils.isEmpty(documento.getNombreDocumento()) ?
                    documento.getNombreDocumento().trim() : "";
            properties.put(PropertyIds.NAME, docName);

            if ("PD".equals(selector)) {
                utilities.buscarCrearCarpeta(session, documento, response, bytes, properties, ConstantesECM.PRODUCCION_DOCUMENTAL);
            } else {
                utilities.buscarCrearCarpetaRadicacion(session, documento, response, properties, selector);
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
     * Metodo para modificar metadatos del documento de Alfresco
     *
     * @param session Objeto de conexion a Alfresco
     * @param dto     Obj DocumentoDTO con las modificaciones
     */
    @Override
    public MensajeRespuesta modificarMetadatosDocumento(DocumentoDTO dto, Session session) {
        final MensajeRespuesta response = new MensajeRespuesta();
        final Map<String, Object> idResponse = new HashMap<>();
        idResponse.put("idECM", null);
        response.setCodMensaje(ConstantesECM.ERROR_COD_MENSAJE);
        response.setResponse(idResponse);
        if (ObjectUtils.isEmpty(dto)) {
            response.setMensaje("El documento introducido el nulo");
            return response;
        }
        final String idDocumento = dto.getIdDocumento();
        if (StringUtils.isEmpty(idDocumento)) {
            response.setMensaje("El ID del documento introducido el nulo");
            return response;
        }
        log.info("### Modificar documento: " + idDocumento);
        final String docName = dto.getNombreDocumento();
        if (!StringUtils.isEmpty(docName)) {
            try {
                final ObjectId idDocObject = new ObjectIdImpl(idDocumento);
                final CmisObject object = session.getObject(idDocObject);
                if (!(object instanceof Document)) {
                    response.setMensaje("El id especificado no coincide con el de un documento");
                    return response;
                }
                final Map<String, Object> updateProperties = new HashMap<>();
                final String nroRadicado = dto.getNroRadicado();
                final SelectorType selectorType = SelectorType.getSelectorBy(nroRadicado);
                if (null == selectorType) {
                    response.setMensaje("El selector no es valido '" + nroRadicado + "' para un numero de radicado");
                    return response;
                }
                updateProperties.put(ConstantesECM.CMCOR_NRO_RADICADO, nroRadicado);
                updateProperties.put(ConstantesECM.CMCOR_ID_DOC_PRINCIPAL, idDocumento);
                final String docType = object.getPropertyValue(ConstantesECM.CMCOR_TIPO_DOCUMENTO);
                if ("Anexo".equals(docType)) {
                    response.setMensaje("No se debe modificar el numero de radicado de un documento anexo");
                    return response;
                }
                final Folder sourceFolder = utilities.getFolderFrom((Document) object);
                if (null != sourceFolder) {
                    dto.setNroRadicado(null);
                    final Carpeta linkTargetFolder = utilities.crearCarpetaRadicacion(selectorType, ConstantesECM.DEPENDENCIA_RADICACION, session);
                    final String dependencyCode = sourceFolder.getPropertyValue(ConstantesECM.CMCOR_DEP_CODIGO);
                    final Carpeta targetFolder = utilities.crearCarpetaRadicacion(selectorType, dependencyCode, session);
                    final ItemIterable<QueryResult> principalAdjuntosQueryResults = utilities.getPrincipalAdjuntosQueryResults(session, dto);
                    for (QueryResult queryResult :
                            principalAdjuntosQueryResults) {
                        String objectId = queryResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
                        CmisObject tmpObject = session.getObject(session.createObjectId(objectId));
                        tmpObject = tmpObject.updateProperties(updateProperties);
                        Document document = (Document) tmpObject;
                        document.move(sourceFolder, targetFolder.getFolder());
                        document.refresh();
                        if (!ConstantesECM.DEPENDENCIA_RADICACION.equals(dependencyCode)) {
                            utilities.crearLink(linkTargetFolder.getFolder(), document, session);
                        }
                    }
                }
                final String nombreRemitente = dto.getNombreRemitente();
                if (!StringUtils.isEmpty(nombreRemitente)) {
                    updateProperties.put(ConstantesECM.CMCOR_NOMBRE_REMITENTE, nombreRemitente);
                }
                final String tipologiaDocumental = dto.getTipologiaDocumental();
                if (!StringUtils.isEmpty(tipologiaDocumental)) {
                    updateProperties.put(ConstantesECM.CMCOR_TIPOLOGIA_DOCUMENTAL, tipologiaDocumental);
                }
                updateProperties.put(ConstantesECM.CMCOR_ID_DOC_PRINCIPAL, "");
                CmisObject cmisObject = object.updateProperties(updateProperties);
                log.info("### Modificados los metadatos de correctamente");
                updateProperties.clear();
                updateProperties.put("idECM", cmisObject.getId().split(";")[0]);
                response.setMensaje("OK");
                response.setResponse(updateProperties);
                response.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
                return response;

            } catch (Exception e) {
                log.error("*** Error al modificar el documento *** ", e);
                response.setMensaje(e.getMessage());
                return response;
            }
        }
        dto.setIdDocumento(null);
        if (!StringUtils.isEmpty(dto.getNroRadicado())) {
            final ItemIterable<QueryResult> principalAdjuntosQueryResults = utilities.getPrincipalAdjuntosQueryResults(session, dto);
            for (QueryResult queryResult : principalAdjuntosQueryResults) {
                String objectId = queryResult.getPropertyValueById(PropertyIds.OBJECT_ID);
                CmisObject cmisObject = session.getObject(session.createObjectId(objectId));
                Document document = (Document) cmisObject;
                String docType = document.getPropertyValue(ConstantesECM.CMCOR_TIPO_DOCUMENTO);
                if (!StringUtils.isEmpty(docType) && "principal".equals(docType.toLowerCase())) {
                    dto.setIdDocumento(document.getId().split(";")[0]);
                    break;
                }
            }
        }
        response.setCodMensaje(ConstantesECM.SUCCESS_COD_MENSAJE);
        response.getResponse().put("idECM", dto.getIdDocumento());
        response.setMensaje(ConstantesECM.SUCCESS);
        return response;
    }

    /**
     * Eliminar documento del Alfresco
     *
     * @param idDoc   Identificador del documento a borrar
     * @param session Objeto de conexion al Alfresco
     * @return Retorna true si borró con exito y false si no
     */
    @Override
    public void eliminardocumento(String idDoc, Session session) throws SystemException {
        try {
            log.info("Se buscan los documentos Anexos al documento que se va a borrar");
            if (StringUtils.isEmpty(idDoc)) {
                throw new SystemException("No se especifico el ID del documento");
            }
            idDoc = idDoc.indexOf(';') != -1 ? idDoc.split(";")[0] : idDoc;
            ObjectId objectId = new ObjectIdImpl(idDoc);
            CmisObject object = session.getObject(objectId);
            Document delDoc = (Document) object;
            DocumentoDTO documentoDTO = utilities.transformarDocumento(delDoc);
            delDoc.delete(false);
            if ("Principal".equals(documentoDTO.getTipoPadreAdjunto())) {
                utilities.eliminarDocumentosAnexos(idDoc, session);
            }

        } catch (CmisObjectNotFoundException e) {
            log.error("Documento no encontrado :", e);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        } catch (Exception e) {
            log.error("No se pudo eliminar el documento :", e);
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
                lista.add(lista.size(), utilities.modificarUnidadDocumental(dto, session));
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
            final Optional<DocumentoDTO> optionalDocumentoDTO = utilities.subirDocumentoDtoTemp(documentoDTO, session);
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
        final Optional<DocumentoDTO> optionalDocumentoDTO = utilities.subirDocumentoDtoTemp(documentoDTO, session);
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
        final List<Folder> folderList = utilities.getUDListWithIDBy(codigoDependencia, session);
        final List<Map<String, Object>> mapList = new ArrayList<>();

        for (Folder folder : folderList) {
            final List<DocumentoDTO> documentsFromFolder = getDocumentsFromFolder(folder);
            final String idUnidadDocumental = folder.getPropertyValue(ConstantesECM.CMCOR_UD_ID);

            final String[] serieSubSerie = utilities.getSerieSubSerie(folder, session);
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
                    final Optional<ContenidoDependenciaTrdDTO> optionalTrdDTO = utilities.getDependenciaTrdDTO(query, session);
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
            mapResponse.put("sedes", utilities.getSedeList(session));
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
            final List<String> disposicionFinalList = disposicionFinalDTO.getDisposicionFinalList();
            final List<UnidadDocumentalDTO> unidadDocumentalDTOS = utilities.listarUnidadesDocumentales(disposicionFinalList, dto, session);
            formatoListaUnidadDocumental(unidadDocumentalDTOS, session);
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
            String disposicion = dto.getDisposicion();
            if (!StringUtils.isEmpty(disposicion)) {
                final FinalDispositionType dispositionType = FinalDispositionType.getDispositionBy(disposicion);
                if (dispositionType != null) {
                    final StateType stateType = StateType.getStateBy(dto.getEstado());
                    if (null != stateType) {
                        if (stateType == StateType.APPROVED && dispositionType == FinalDispositionType.E) {
                            eliminarUnidadDocumental(idUnidadDocumental, session);
                        } else {
                            Optional<Folder> optionalFolder = getUDFolderById(idUnidadDocumental, session);
                            if (optionalFolder.isPresent()) {
                                dto.setDisposicion(dispositionType.name());
                                utilities.updateProperties(optionalFolder.get(), dto);
                                Optional<Folder> recordFolderByUdId = recordServices.getRecordFolderByUdId(idUnidadDocumental);
                                recordFolderByUdId.ifPresent(folder -> {
                                    dto.setId(recordFolderByUdId.get().getId());
                                    recordServices.modificarRecordFolder(dto, false);
                                });
                            }
                        }
                    }
                }
            }
        }
        return MensajeRespuesta.newInstance()
                .codMensaje(ConstantesECM.SUCCESS_COD_MENSAJE)
                .mensaje("Operación realizada satisfactoriamente")
                .build();
    }

    @Override
    public MensajeRespuesta estamparEtiquetaRadicacion(DocumentoDTO documentoDTO, Session session) throws SystemException {
        log.info("processing rest request - Estampar la etiquta de radicacion");
        try {
            utilities.estamparEtiquetaRadicacion(documentoDTO, session);
            final Map<String, Object> mapResponse = new HashMap<>();
            mapResponse.put("documentoDto", documentoDTO);
            return MensajeRespuesta.newInstance()
                    .codMensaje(ConstantesECM.SUCCESS_COD_MENSAJE)
                    .mensaje("Imagen guardada satisfactoriamente")
                    .response(mapResponse)
                    .build();

        } catch (Exception e) {
            log.info("Ocurrio un error al estampar la etiqueta");
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    @Override
    public MensajeRespuesta subirDocumentoAnexo(DocumentoDTO documento, Session session) throws SystemException {
        if (ObjectUtils.isEmpty(documento)) {
            throw new SystemException("No se ha especificado el DocumentoDTO");
        }
        final byte[] bytes = documento.getDocumento();
        if (ObjectUtils.isEmpty(bytes)) {
            throw new SystemException("No se ha especificado el contenido del documento");
        }
        final String idDocPincipal = documento.getIdDocumentoPadre();
        if (StringUtils.isEmpty(idDocPincipal)) {
            throw new SystemException("No se ha especificado el ID del documento Principal");
        }
        final String nombreDoc = documento.getNombreDocumento();
        if (StringUtils.isEmpty(nombreDoc)) {
            throw new SystemException("No se ha especificado el nombre del documento");
        }
        try {
            ObjectId objectId = new ObjectIdImpl(idDocPincipal);
            CmisObject cmisObject = session.getObject(objectId);
            Document document = (Document) cmisObject;
            final DocumentoDTO docPrincipal = utilities.transformarDocumento(document);
            final String docType = docPrincipal.getTipoPadreAdjunto();
            if (StringUtils.isEmpty(docType) || !docType.equalsIgnoreCase("principal")) {
                throw new SystemException("El id proporcionado no coincide con el de un documento principal");
            }
            final String documentMimeType = StringUtils.isEmpty(documento.getTipoDocumento()) ?
                    MimeTypes.getMIMEType("pdf") : documento.getTipoDocumento();
            final ContentStream contentStream = new ContentStreamImpl(nombreDoc,
                    BigInteger.valueOf(bytes.length), documentMimeType, new ByteArrayInputStream(bytes));
            //final String nroRadicado
            final Map<String, Object> properties = new HashMap<>();
            properties.put(PropertyIds.NAME, nombreDoc);
            properties.put(PropertyIds.OBJECT_TYPE_ID, "D:cmcor:CM_DocumentoPersonalizado");
            properties.put(ConstantesECM.CMCOR_TIPO_DOCUMENTO, "Anexo");
            properties.put(ConstantesECM.CMCOR_ID_DOC_PRINCIPAL, idDocPincipal);
            properties.put(PropertyIds.CONTENT_STREAM_MIME_TYPE, documentMimeType);
            properties.put(ConstantesECM.CMCOR_NRO_RADICADO, docPrincipal.getNroRadicado());
            properties.put(ConstantesECM.CMCOR_NOMBRE_REMITENTE, documento.getNombreRemitente());
            Folder folder = utilities.getFolderFrom(document);
            if (null != folder) {
                document = folder.createDocument(properties, contentStream, VersioningState.MAJOR);
                if (null != document) {
                    final List<DocumentoDTO> response = new ArrayList<>();
                    response.add(response.size(), utilities.transformarDocumento(document));
                    return MensajeRespuesta.newInstance()
                            .codMensaje(ConstantesECM.SUCCESS_COD_MENSAJE)
                            .mensaje("Operacion completada satisfactoriamente")
                            .documentoDTOList(response)
                            .build();
                }
            }
            throw new SystemException("Ocurrio un error al anezar el documento");

        } catch (CmisContentAlreadyExistsException ccaee) {
            log.error(ConstantesECM.ECM_ERROR_DUPLICADO, ccaee);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("El documento ya existe en el ECM")
                    .withRootException(ccaee)
                    .buildSystemException();
        } catch (Exception e) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    @Override
    public void formatoListaUnidadDocumental(List<UnidadDocumentalDTO> unidadDocumentalDTOS, Session session) {
        unidadDocumentalDTOS.forEach(unidadDocumentalDTO -> {
            final String idUnidadDocumental = unidadDocumentalDTO.getId();
            final Optional<Folder> optionalFolder = getUDFolderById(idUnidadDocumental, session);
            optionalFolder.ifPresent(folder -> {
                final String[] serieSubserie = utilities.getSerieSubSerie(optionalFolder.get(), session);
                final String serieName = serieSubserie[0];
                final String subSerieName = serieSubserie[1];
                String currentFolderFatherName = !StringUtils.isEmpty(subSerieName) ? subSerieName : serieName;
                final int index = currentFolderFatherName.indexOf('_');
                currentFolderFatherName = index != -1 ? currentFolderFatherName.substring(index + 1) : currentFolderFatherName;
                unidadDocumentalDTO.setNombreSerie(currentFolderFatherName);
                unidadDocumentalDTO.setNombreSubSerie(currentFolderFatherName);
                final FinalDispositionType type = FinalDispositionType.getDispositionBy(unidadDocumentalDTO.getDisposicion());
                if (null != type) {
                    unidadDocumentalDTO.setDisposicion(type.name());
                }
            });
        });
    }

    @Override
    public Map<String, Object> obtenerPropiedadesDocumento(Document document) {
        return utilities.obtenerPropiedadesDocumento(document);
    }

    @Override
    public MensajeRespuesta getDocumentosPorArchivar(final String codigoDependencia, Session session) throws SystemException {
        log.info("Se buscan los documentos por Archivar");
        if (StringUtils.isEmpty(codigoDependencia)) {
            throw new SystemException("Especifique el codigo de la dependencia");
        }
        final String query = "SELECT * FROM " + ConstantesECM.CMCOR + configuracion.getPropiedad(ConstantesECM.CLASE_UNIDAD_DOCUMENTAL) +
                " WHERE " + ConstantesECM.CMCOR_DEP_CODIGO + " LIKE '" + codigoDependencia + "'" +
                " AND " + PropertyIds.NAME + " NOT LIKE '" + ConstantesECM.PRODUCCION_DOCUMENTAL + "%'";
        final ItemIterable<QueryResult> queryResults = session.query(query, false);
        final List<DocumentoDTO> dtos = new ArrayList<>();
        for (QueryResult queryResult : queryResults) {
            final String id = queryResult.getPropertyValueByQueryName(ConstantesECM.CMCOR_UD_ID);
            if (StringUtils.isEmpty(id) || id.trim().isEmpty()) {
                final String objectId = queryResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
                final Folder udFolder = (Folder) session.getObject(session.createObjectId(objectId));
                final String nameFolder = udFolder.getName();
                if (nameFolder.startsWith(ConstantesECM.COMUNICACION_EXTERNA)
                        || nameFolder.startsWith(ConstantesECM.COMUNICACION_INTERNA)) {
                    final ItemIterable<CmisObject> children = udFolder.getChildren();
                    for (CmisObject cmisObject : children) {
                        if (cmisObject.getType().getId().equals("D:" + ConstantesECM.CMCOR + "CM_DocumentoPersonalizado")) {
                            final Document document = (Document) cmisObject;
                            final DocumentoDTO documentoDTO = utilities.transformarDocumento(document);
                            documentoDTO.setCodigoDependencia(udFolder.getPropertyValue(ConstantesECM.CMCOR_DEP_CODIGO));
                            dtos.add(dtos.size(), documentoDTO);
                        }
                    }
                }
            }
        }
        return MensajeRespuesta.newInstance()
                .codMensaje(ConstantesECM.SUCCESS_COD_MENSAJE).documentoDTOList(dtos)
                .mensaje("Listado devuelto satisfactoriamente").build();
    }

    public MensajeRespuesta eliminarUnidadDocumental(String idUnidadDocumental, Session session) throws SystemException {
        recordServices.eliminarRecordFolder(idUnidadDocumental);
        Optional<Folder> optionalFolder = getUDFolderById(idUnidadDocumental, session);
        if (optionalFolder.isPresent()) {
            Folder folder = optionalFolder.get();
            folder.deleteTree(true, UnfileObject.DELETE, true);
            return MensajeRespuesta.newInstance()
                    .codMensaje(ConstantesECM.SUCCESS_COD_MENSAJE)
                    .mensaje(ConstantesECM.SUCCESS)
                    .build();
        } else {
            return MensajeRespuesta.newInstance()
                    .codMensaje(ConstantesECM.ERROR_COD_MENSAJE)
                    .mensaje("Ocurrio un error al eliminar la unidad documental '" + idUnidadDocumental + "'")
                    .build();
        }
    }
}