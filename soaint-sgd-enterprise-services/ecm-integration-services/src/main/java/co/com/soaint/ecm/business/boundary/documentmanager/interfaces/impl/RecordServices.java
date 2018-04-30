package co.com.soaint.ecm.business.boundary.documentmanager.interfaces.impl;

import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Utilities;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentControl;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.IRecordServices;
import co.com.soaint.ecm.domain.entity.AccionUsuario;
import co.com.soaint.ecm.domain.entity.Conexion;
import co.com.soaint.ecm.domain.entity.DiposicionFinalEnum;
import co.com.soaint.ecm.util.SystemParameters;
import co.com.soaint.foundation.canonical.ecm.*;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.util.*;

/**
 * Created by amartinez on 24/01/2018.
 */
@BusinessControl
@Log4j2
public class RecordServices implements IRecordServices {

    private static final String ENTRY = "entry";
    private static final int AUTO_CLOSE_NUM_DAYS = 8;

    @Autowired
    private ContentControl contentControl;
    String idSubCategoria = "";
    private String idPadre = "";
    @Value("${protocolo}")
    private String protocolo = "";
    @Value("${mensaje.error.sistema}")
    private String errorSistema = "";
    @Value("${mensaje.error.sistema.generico}")
    private String errorSistemaGenerico = "";
    @Value("${header.accept}")
    private String headerAccept = "";
    @Value("${header.authorization}")
    private String headerAuthorization = "";
    @Value("${header.value.application.type}")
    private String valueApplicationType = "";
    @Value("${header.value.authorization}")
    private String valueAuthorization = "";
    @Value("${mensaje.error.negocio.fallo}")
    private String errorNegocioFallo = "";
    @Value("${tokken.record}")
    private String encoding = "";
    @Value("${nodeType}")
    private String tipoNodo = "";
    @Value("${recordCategory}")
    private String recordCategoria = "";
    @Value("${tag.propiedades}")
    private String tagPropiedades = "";
    @Value("${recordCarpeta}")
    private String recordCarpeta = "";
    @Value("${id.sitio.record.manager}")
    private String idRecordManager = "";
    @Value("${api.service.alfresco}")
    private String apiServiceAlfresco = "";
    @Value("${tag.nombre}")
    private String nombre = "";
    @Value("${tag.descripcion}")
    private String descripcion = "";
    @Value("${tag.periodo}")
    private String periodo = "";
    @Value("${tag.localizacion}")
    private String localizacion = "";
    @Value("${tag.propiedad.periodo}")
    private String propiedadPeriodo = "";
    @Value("${tag.evento.completar}")
    private String eventoCompletar = "";
    @Value("${tag.evento}")
    private String evento = "";
    @Value("${valor.periodo}")
    private String valorPeriodo = "";
    @Value("${valor.mensaje.descripcion}")
    private String mensajeDescripcion = "";

    private String codigoOrgAUX = " ";
    private String idSerie = "";
    private Map<String, String> codigosRecord = new HashMap<>();
    private Map<String, String> propiedades = new HashMap<>();
    private Map<String, String> codigosSubseries = new HashMap<>();
    private Map<String, Object> disposicion = new HashMap<>();

    /**
     * Permite crear la estructura en record a partir de la informacion enviada de instrumento
     *
     * @param structure Objeto  que contiene la estructura
     * @return mesaje respuesta para notificar la correcta creacion
     * @throws SystemException
     */
    @Override
    public MensajeRespuesta crearEstructuraRecord(List<EstructuraTrdDTO> structure) throws SystemException {
        log.info("iniciar - Crear estructura en record: {}");
        try {
            Map<String, String> idNodosPadre = new HashMap<>();
            codigosRecord = new HashMap<>();
            for (EstructuraTrdDTO estructura : structure) {
                Utilities utils = new Utilities();
                utils.ordenarListaOrganigrama(estructura.getOrganigramaItemList());
                List<OrganigramaDTO> organigramaList = estructura.getOrganigramaItemList();
                List<ContenidoDependenciaTrdDTO> trdList = estructura.getContenidoDependenciaList();
                generarOrganigrama(organigramaList, idNodosPadre);
                generarDependencia(trdList);
            }
            idPadre = "";

            return MensajeRespuesta.newInstance()
                    .codMensaje("0000")
                    .mensaje("Estructura creada correctamente")
                    .build();
        } catch (Exception e) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("fin -  Crear estructura en record: ");
        }
    }

    /**
     * Permiete crear carpetas en el record
     *
     * @param entrada Objeto que contiende la informacion necesaria para buscar la carpeta con nombre de sede, dependeicia, serie
     *                y subserie ademas del nombre de la carpeta a crear
     * @return respuesta de la operacion satisfactria cuando se creo la carpeta
     * * @throws SystemException
     */
    @Override
    public MensajeRespuesta crearCarpetaRecord(EntradaRecordDTO entrada) throws SystemException {
        log.info("iniciar - Crear carpeta record: {}", entrada);
        try {
            Map<String, String> query = new HashMap<>();
            JSONObject parametro = new JSONObject();
            String queryPrincipal = "select * from rmc:rmarecordCategoryCustomProperties where rmc:xSeccion = '" + entrada.getDependencia() + "' and  rmc:xCodSerie = '" + entrada.getSerie() + "' ";
            if (!"".equals(entrada.getSubSerie())) {
                String condicionSubserie = " and  rmc:xCodSubSerie = '" + entrada.getSubSerie() + "' ";
                queryPrincipal = queryPrincipal.concat(condicionSubserie);
            }
            String codigoBusqueda = entrada.getDependencia().concat(".").concat(entrada.getSerie()).concat(".").concat(entrada.getSubSerie());
            query.put("query", queryPrincipal);
            query.put("language", "cmis");
            parametro.put("query", query);
            JSONObject carpeta = new JSONObject();
            carpeta.put("name", entrada.getNombreCarpeta());
            carpeta.put(tipoNodo, recordCarpeta);

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("idUnidadDocumental", crearNodo(carpeta, buscarRuta(parametro)));

            return MensajeRespuesta.newInstance()
                    .codMensaje("0000")
                    .mensaje("Carpeta creda en ".concat(codigoBusqueda))
                    .response(respuesta)
                    .build();
        } catch (Exception e) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("fin -  Crear carpeta record: ");
        }
    }

    /**
     * Metodo para abrir/cerrar una unidad documental
     *
     * @param idUnidadDocumental Id Unidad Documental
     * @param accionUsuario      accion a realizar
     * @return MensajeRespuesta
     */
    @Override
    public MensajeRespuesta abrirCerrarReactivarUnidadDocumental(final String idUnidadDocumental, AccionUsuario accionUsuario) throws BusinessException, SystemException {
        log.info("Ejecutando MensajeRespuesta cerrarUnidadDocumental(String idUnidadDocumental)");

        final MensajeRespuesta respuesta = new MensajeRespuesta();
        respuesta.setMensaje("Success");
        respuesta.setCodMensaje("0000");

        switch (accionUsuario) {
            case ABRIR:
                if (!esUnidadDocumentalAbierta(idUnidadDocumental)) {
                    respuesta.setCodMensaje("0006");
                    respuesta.setMensaje("Ocurrio un error al abrir la unidad documental");
                }
                break;
            case CERRAR:
                if (!esUnidadDocumentalCerrada(idUnidadDocumental)) {
                    respuesta.setCodMensaje("0006");
                    respuesta.setMensaje("Ocurrio un error al cerrar la unidad documental");
                }
                break;
            case REACTIVAR:
                if (!esUnidadDocumentalReactivada(idUnidadDocumental)) {
                    respuesta.setCodMensaje("0006");
                    respuesta.setMensaje("Ocurrio un error al reactivar la unidad documental");
                }
                break;
        }
        return respuesta;
    }

    private boolean esUnidadDocumentalReactivada(String idUnidadDocumental) throws SystemException, BusinessException {

        esUnidadDocumentalAbierta(idUnidadDocumental);

        final Folder recordFolder = obtenerRecordFolder(idUnidadDocumental);
        final Folder contentFolder = contentControl.getUDFolderById(idUnidadDocumental,
                contentControl.obtenerConexion().getSession());

        if (recordFolder != null && contentFolder != null) {
            final ItemIterable<CmisObject> children = recordFolder.getChildren();

            children.forEach(cmisObject -> {
                if (cmisObject.getBaseTypeId() == BaseTypeId.CMIS_DOCUMENT &&
                        cmisObject.getType().getId().startsWith("D:cmcor")) {
                    Document document = (Document) cmisObject;
                    ContentStream contentStream = document.getContentStream();
                    eliminarObjECM(BaseTypeId.CMIS_DOCUMENT, document.getId().split(";")[0]);
                    contentFolder.createDocument(contentControl.obtenerPropiedadesDocumento(document), contentStream, VersioningState.MAJOR);
                }
            });
            return eliminarObjECM(BaseTypeId.CMIS_FOLDER, recordFolder.getId());
        }
        return false;
    }

    /**
     * Metodo para abrir/cerrar una unidad documental
     *
     * @param unidadDocumentalDTOS Lista de Unidad Documental
     * @param accionUsuario        Accion a realizar
     * @return MensajeRespuesta
     */
    @Override
    public MensajeRespuesta abrirCerrarReactivarUnidadesDocumentales(List<UnidadDocumentalDTO> unidadDocumentalDTOS, AccionUsuario accionUsuario) throws SystemException, BusinessException {
        log.info("Ejecutando MensajeRespuesta abrirCerrarReactivarUnidadesDocumentales(List<UnidadDocumentalDTO>, AccionUsuario)");
        for (UnidadDocumentalDTO dto :
                unidadDocumentalDTOS) {
            switch (accionUsuario) {
                case ABRIR:
                    esUnidadDocumentalAbierta(dto.getId());
                    break;
                case CERRAR:
                    esUnidadDocumentalCerrada(dto.getId());
                    break;
                case REACTIVAR:
                    esUnidadDocumentalReactivada(dto.getId());
                    break;
            }
        }
        final MensajeRespuesta respuesta = new MensajeRespuesta();
        respuesta.setMensaje("Success");
        respuesta.setCodMensaje("0000");
        return respuesta;
    }

    @Override
    public Folder obtenerRecordFolder(String idUnidadDocumental) throws BusinessException {
        final Conexion conexion = contentControl.obtenerConexion();
        final UnidadDocumentalDTO unidadDocumentalDTO = contentControl.
                findUDById(idUnidadDocumental, conexion.getSession());
        Folder obtenerRecordCategory = obtenerRecordCategory(unidadDocumentalDTO, conexion.getSession());
        if (!ObjectUtils.isEmpty(obtenerRecordCategory)) {
            final ItemIterable<CmisObject> children = obtenerRecordCategory.getChildren();
            for (CmisObject cmisObject :
                    children) {
                if (cmisObject instanceof Folder) {
                    String dtoName = Utilities.reemplazarCaracteresRaros(unidadDocumentalDTO.getNombreUnidadDocumental());
                    String folderName = Utilities.reemplazarCaracteresRaros(cmisObject.getName());
                    if (dtoName.equals(folderName)) {
                        return (Folder) cmisObject;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Permite Abrir/Cerrar Record Folder
     *
     * @param documentalDTO abrir cerrar la unidad documental
     * @return identificador de la subserie creada
     * @throws SystemException SystemException
     */
    private boolean closeOrOpenUnidadDocumentalRecord(UnidadDocumentalDTO documentalDTO) throws SystemException {
        log.info("Se entra al metodo closeOrOpenUnidadDocumentalRecord para cerrar la unidad documental con id: {}", documentalDTO.getId());
        try {
            Response response = modificarRecordFolder(documentalDTO);
            if (response.getStatus() != HttpURLConnection.HTTP_OK) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage(errorNegocioFallo + response.getStatus() + response.getStatusInfo().toString())
                        .buildBusinessException();
            }
            return true;

        } catch (Exception ex) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(ex)
                    .buildSystemException();
        } finally {
            log.info("fin - Abrir o cerrar el record folder ");
        }
    }

    private Response modificarRecordFolder(UnidadDocumentalDTO documentalDTO) {

        JSONObject properties = new JSONObject();
        Map<String, Object> nombreMap = new HashMap<>();
        nombreMap.put("rma:isClosed", documentalDTO.getCerrada());

        properties.put("properties", nombreMap);

        WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_RECORD));
        return wt.path("/record-folders/" + documentalDTO.getId())
                .request()
                .header(headerAuthorization, valueAuthorization + " " + encoding)
                .header(headerAccept, valueApplicationType)
                .put(Entity.json(properties.toString()));
    }

    private synchronized boolean esUnidadDocumentalAbierta(String idUnidadDocumental) throws BusinessException, SystemException {

        final Folder udRecordFolder = obtenerRecordFolder(idUnidadDocumental);
        if (!ObjectUtils.isEmpty(udRecordFolder)) {
            final Calendar currentDay = GregorianCalendar.getInstance();
            final UnidadDocumentalDTO documentalDTO = new UnidadDocumentalDTO();
            documentalDTO.setId(udRecordFolder.getId());
            documentalDTO.setCerrada(false);
            documentalDTO.setInactivo(false);
            documentalDTO.setFechaAutoCierre(Utilities.sumarDiasAFecha(currentDay, AUTO_CLOSE_NUM_DAYS));

            boolean actionRecord = closeOrOpenUnidadDocumentalRecord(documentalDTO);
            documentalDTO.setId(idUnidadDocumental);
            return actionRecord && closeOrOpenUnidadDocumntalContent(documentalDTO);
        }
        return false;
    }

    private synchronized boolean esUnidadDocumentalCerrada(String idUnidadDocumental) throws BusinessException, SystemException {

        final Conexion conexion = contentControl.obtenerConexion();
        final UnidadDocumentalDTO unidadDocumentalDTO = contentControl.
                listarDocsDadoIdUD(idUnidadDocumental, conexion.getSession());

        if (!ObjectUtils.isEmpty(unidadDocumentalDTO)) {

            Folder udRecordFolder = obtenerRecordFolder(idUnidadDocumental);

            String idRecordFolder = (!ObjectUtils.isEmpty(udRecordFolder)) ?
                    udRecordFolder.getId() : createAndRetrieveId(unidadDocumentalDTO);

            for (DocumentoDTO documentoDTO : unidadDocumentalDTO.getListaDocumentos()) {
                //Se declara el record
                String s = declararRecord(documentoDTO.getIdDocumento());
                log.info("Declarando '{}' como record con id {}", documentoDTO.getNombreDocumento(), s);
                //Se completa el record
                String s1 = completeRecord(documentoDTO.getIdDocumento());
                log.info("Completando '{}' como record con id {}", documentoDTO.getNombreDocumento(), s1);
                //Se archiva el record
                String s2 = fileRecord(documentoDTO.getIdDocumento(), idRecordFolder);
                log.info("Archivando '{}' como record con id {}", documentoDTO.getNombreDocumento(), s2);
            }

            UnidadDocumentalDTO dto = new UnidadDocumentalDTO();
            dto.setId(idRecordFolder);
            dto.setCerrada(true);
            dto.setInactivo(true);
            boolean isUDRecordClosed = closeOrOpenUnidadDocumentalRecord(dto);
            dto.setId(idUnidadDocumental);
            dto.setFechaAutoCierre(GregorianCalendar.getInstance());
            return isUDRecordClosed && closeOrOpenUnidadDocumntalContent(dto);
        }
        return false;
    }

    /**
     * Permite Completar Records
     *
     * @param idRecord Identificador del documento en record
     * @return identificador de la subserie creada
     * @throws SystemException SystemException
     */
    private String completeRecord(String idRecord) throws SystemException {
        log.info("Se entra al metodo completeRecord para el record de id: {}", idRecord);
        try {
            if (!idRecord.isEmpty()) {

                WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_RECORD));
                Response response = wt.path("/records/" + idRecord + "/complete")
                        .request()
                        .header(headerAuthorization, valueAuthorization + " " + encoding)
                        .header(headerAccept, valueApplicationType)
                        .post(Entity.json(idRecord));
                if (response.getStatus() != 201) {
                    throw ExceptionBuilder.newBuilder()
                            .withMessage(errorNegocioFallo + response.getStatus() + response.getStatusInfo().toString())
                            .buildBusinessException();
                } else {
                    return obtenerIdPadre(new JSONObject(response.readEntity(String.class)));
                }
            }
            return null;

        } catch (BusinessException e) {
            log.error(e.getMessage());
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        } catch (Exception ex) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(ex)
                    .buildSystemException();
        } finally {
            log.info("fin - Completar record ");
        }
    }

    /**
     * Permite archivar Records
     *
     * @param idRecord       Identificador del documento en record
     * @param idRecordFolder identificador de la unidad Documental a donde se va a llevar el documento
     * @return identificador de la subserie creada
     * @throws SystemException SystemException
     */
    private String fileRecord(String idRecord, String idRecordFolder) throws SystemException {
        log.info("Se entra al metodo fileRecord para archivar el documento de id: {}", idRecord);
        try {
            JSONObject recordFolder = new JSONObject();
            if (!idRecord.isEmpty() && !idRecordFolder.isEmpty()) {
                recordFolder.put("targetParentId", idRecordFolder);
                WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_RECORD));
                Response response = wt.path("/records/" + idRecord + "/file")
                        .request()
                        .header(headerAuthorization, valueAuthorization + " " + encoding)
                        .header(headerAccept, valueApplicationType)
                        .post(Entity.json(recordFolder.toString()));
                if (response.getStatus() != 201) {
                    throw ExceptionBuilder.newBuilder()
                            .withMessage(errorNegocioFallo + response.getStatus() + response.getStatusInfo().toString())
                            .buildBusinessException();
                } else {
                    return obtenerIdPadre(new JSONObject(response.readEntity(String.class)));
                }
            }
            return null;

        } catch (BusinessException e) {
            log.error(e.getMessage());
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        } catch (Exception ex) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(ex)
                    .buildSystemException();
        } finally {
            log.info("fin - Guardar record en su record folder ");
        }
    }

    /**
     * Permite declarar un documento como record
     *
     * @param idDocumentoContent Identificador del documento dentro del content
     * @return el id del record creado
     * @throws SystemException SystemException
     */
    private String declararRecord(String idDocumentoContent) throws SystemException {
        log.info("iniciar - Declarar como record el documento con id: {}", idDocumentoContent);
        try {

            WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_RECORD));
            Response response = wt.path("/files/" + idDocumentoContent + "/declare")
                    .request()
                    .header(headerAuthorization, valueAuthorization + " " + encoding)
                    .header(headerAccept, valueApplicationType)
                    .post(Entity.json(idDocumentoContent));
            if (response.getStatus() != HttpURLConnection.HTTP_CREATED) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage(errorNegocioFallo + response.getStatus() + response.getStatusInfo().toString())
                        .buildBusinessException();
            } else {
                return obtenerIdPadre(new JSONObject(response.readEntity(String.class)));
            }

        } catch (BusinessException e) {
            log.error(e.getMessage());
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        } catch (Exception ex) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(ex)
                    .buildSystemException();
        } finally {
            log.info("fin - Declarar como record el documento ");
        }
    }

    private Folder obtenerRecordCategory(UnidadDocumentalDTO dto, Session session) throws BusinessException {

        String query = "SELECT * FROM rmc:rmarecordCategoryCustomProperties";
        boolean where = false;

        if (!ObjectUtils.isEmpty(dto.getCodigoDependencia())) {
            where = true;
            query += " WHERE rmc:xSeccion = '" + dto.getCodigoDependencia() + "'";
        }
        if (!ObjectUtils.isEmpty(dto.getCodigoSerie())) {
            query += (where ? " AND " : " WHERE ") + "rmc:xCodSerie = '" + dto.getCodigoSerie() + "'";
            where = true;
        }
        if (!ObjectUtils.isEmpty(dto.getCodigoSubSerie())) {
            query += (where ? " AND " : " WHERE ") + "rmc:xCodSubSerie = '" + dto.getCodigoSubSerie() + "'";
        }

        try {
            final ItemIterable<QueryResult> queryResults = session.query(query, false);
            final Iterator<QueryResult> iterator = queryResults.iterator();

            if (iterator.hasNext()) {

                QueryResult queryResult = iterator.next();
                String objectId = queryResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
                return (Folder) session.getObject(session.createObjectId(objectId));
            }
        } catch (Exception ex) {
            log.error(errorSistema);
            throw new BusinessException(errorSistemaGenerico);
        } finally {
            log.info("fin - obtenerRecordCategory ");
        }
        return null;
    }

    /**
     * Permite buscar la el id de la ruta necesaria de los nodos para poder realizar las operaciones en el record
     *
     * @param entrada objeto json con los parametros necesarios par apoder efectua la operacion
     * @return el id de la ruta que se esta buscando
     * @throws SystemException
     */
    private String buscarRuta(JSONObject entrada) throws SystemException {
        log.info("iniciar - buscar ruta: {}", entrada);
        try {

            WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter(SystemParameters.API_SEARCH_ALFRESCO));
            Response response = wt.path("")
                    .request()
                    .header(headerAuthorization, valueAuthorization + " " + encoding)
                    .header(headerAccept, valueApplicationType)
                    .post(Entity.json(entrada.toString()));

            if (response.getStatus() != 200) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage(errorNegocioFallo + response.getStatus() + response.getStatusInfo().toString())
                        .buildBusinessException();
            } else {
                return obtenerIdRuta(new JSONObject(response.readEntity(String.class)), "nodeType", "rma:recordCategory");
            }

        } catch (BusinessException e) {
            log.error(e.getMessage());
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        } catch (Exception ex) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(ex)
                    .buildSystemException();
        } finally {
            log.info("fin - buscar ruta ");
        }
    }

    /**
     * Permite obtener el id del plan de ficheros
     *
     * @return el id del plan de ficheros
     * @throws SystemException
     */
    private String obtenerIdFilePlan() throws SystemException {
        log.info("iniciar - obtener id file plan: {}");
        try {
            WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter(SystemParameters.API_CORE_ALFRESCO));
            Response response = wt.path("/sites/" + idRecordManager + "/containers")
                    .request()
                    .header(headerAuthorization, valueAuthorization + " " + encoding)
                    .header(headerAccept, valueApplicationType)
                    .get();

            if (response.getStatus() != 200) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage(errorNegocioFallo + response.getStatus() + response.getStatusInfo().toString())
                        .buildBusinessException();
            } else {
                return obtenerIdRuta(new JSONObject(response.readEntity(String.class)), "folderId", "documentLibrary");
            }
        } catch (BusinessException e) {
            log.error(e.getMessage());
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        } catch (Exception ex) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(ex)
                    .buildSystemException();
        } finally {
            log.info("fin - obtener id file plan ");
        }
    }

    /**
     * Permite crear el root category
     *
     * @param entrada objeto json con los paramtreos necesario para la creacion de la categoria
     * @return el id de la categoria
     * @throws SystemException
     */
    private String crearRootCategory(JSONObject entrada) throws SystemException {
        log.info("iniciar - Crear categoria padre: {}", entrada);
        try {

            WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_RECORD));
            Response response = wt.path("/file-plans/" + obtenerIdFilePlan() + "/categories")
                    .request()
                    .header(headerAuthorization, valueAuthorization + " " + encoding)
                    .header(headerAccept, valueApplicationType)
                    .post(Entity.json(entrada.toString()));

            if (response.getStatus() != 201) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage(errorNegocioFallo + response.getStatus() + response.getStatusInfo().toString())
                        .buildBusinessException();
            } else {
                return obtenerIdPadre(new JSONObject(response.readEntity(String.class)));
            }

        } catch (BusinessException e) {
            log.error(e.getMessage());
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        } catch (Exception ex) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(ex)
                    .buildSystemException();
        } finally {
            log.info("fin - crear categoria padre ");
        }
    }

    private String createAndRetrieveId(UnidadDocumentalDTO unidadDocumentalDTO) throws SystemException {
        EntradaRecordDTO entradaRecordDTO = new EntradaRecordDTO();
        entradaRecordDTO.setSede(unidadDocumentalDTO.getCodigoSede());
        entradaRecordDTO.setDependencia(unidadDocumentalDTO.getCodigoDependencia());
        entradaRecordDTO.setSerie(unidadDocumentalDTO.getCodigoSerie());
        entradaRecordDTO.setSubSerie(unidadDocumentalDTO.getCodigoSubSerie());
        entradaRecordDTO.setNombreCarpeta(unidadDocumentalDTO.getNombreUnidadDocumental());
        MensajeRespuesta mensajeRespuestaAux = crearCarpetaRecord(entradaRecordDTO);
        return (String) mensajeRespuestaAux.getResponse().get("idUnidadDocumental");
    }

    private boolean closeOrOpenUnidadDocumntalContent(UnidadDocumentalDTO documentalDTO) throws BusinessException {

        final Conexion conexion = contentControl.obtenerConexion();

        return contentControl.actualizarUnidadDocumental(documentalDTO, conexion.getSession());
    }

    /**
     * Permite crear nodo partiendo del tipo de categoria
     *
     * @param entrada objeto json con la informacion necesaria para crear el nodo
     * @param idSerie identificardor del nodo padre
     * @return el id del nodo creado
     * @throws SystemException
     */
    private String crearNodo(JSONObject entrada, String idSerie) throws SystemException {
        log.info("iniciar - Crear categoria hija: {}", entrada.toString());
        try {

            WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_RECORD));
            Response response = wt.path("/record-categories/" + idSerie + "/children")
                    .request()
                    .header(headerAuthorization, valueAuthorization + " " + encoding)
                    .header(headerAccept, valueApplicationType)
                    .post(Entity.json(entrada.toString()));
            if (response.getStatus() != 201) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage(errorNegocioFallo + response.getStatus() + response.getStatusInfo().toString())
                        .buildBusinessException();
            } else {
                return obtenerIdPadre(new JSONObject(response.readEntity(String.class)));
            }

        } catch (BusinessException e) {
            log.error(e.getMessage());
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        } catch (Exception ex) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(ex)
                    .buildSystemException();
        } finally {
            log.info("fin - crear categoria hija ");
        }
    }

    /**
     * Permite crear los tiempos de retencion asociado a series y subseries
     *
     * @param entrada objeto json con lo paramtros necesarios para crear las los tiempos
     * @param idPadre id del nodo al que se le aplicaran los tiempos de retencion
     * @return
     * @throws SystemException
     */
    private String crearTiempoRetencion(Map<String, Object> entrada, String idPadre) throws SystemException {
        log.info("iniciar - Crear tiempo retencion: {}", entrada.toString());
        try {

            JSONObject jsonPostDataretencion = new JSONObject();
            jsonPostDataretencion.put(nombre, entrada.get(nombre));
            jsonPostDataretencion.put(descripcion, entrada.get(descripcion));
            jsonPostDataretencion.put(periodo, entrada.get(periodo));
            jsonPostDataretencion.put(localizacion, entrada.get(localizacion));
            jsonPostDataretencion.put(propiedadPeriodo, entrada.get(propiedadPeriodo));
            jsonPostDataretencion.put(eventoCompletar, entrada.get(eventoCompletar));
            JSONArray events = new JSONArray();
            events.put(entrada.get("events"));
            jsonPostDataretencion.put(evento, events);

            WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter(SystemParameters.API_SERVICE_ALFRESCO));
            Response response = wt.path("/" + idPadre + "/dispositionschedule/dispositionactiondefinitions")
                    .request()
                    .header(headerAuthorization, valueAuthorization + " " + encoding)
                    .header(headerAccept, valueApplicationType)
                    .post(Entity.json(jsonPostDataretencion.toString()));
            if (response.getStatus() != 200) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage(errorNegocioFallo + response.getStatus() + response.getStatusInfo().toString())
                        .buildBusinessException();
            } else {
                return obtenerIdPadre(new JSONObject(response.readEntity(String.class)));
            }

        } catch (BusinessException e) {
            log.error(e.getMessage());
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        } catch (Exception ex) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(ex)
                    .buildSystemException();
        } finally {
            log.info("fin - crear categoria hija ");
        }
    }

    /**
     * Permite obtner el id del objeto json de respuesta
     *
     * @param respuestaJson objeto json que contiene el mensaje de repuesta para procesar
     * @return el valor del campo id en la respusta json
     */
    private String obtenerIdPadre(JSONObject respuestaJson) {
        String codigoId = "";
        Iterator keys = respuestaJson.keys();
        while (keys.hasNext()) {
            Object key = keys.next();
            if ("entry".equalsIgnoreCase(key.toString())) {
                JSONObject valor = respuestaJson.getJSONObject((String) key);
                codigoId = valor.getString("id");
            }
        }
        return codigoId;
    }

    /**
     * Permite generar el organigrama a partir de l estructura enviada
     *
     * @param organigramaList objeto que contiene la lista de organigrama
     * @param idNodosPadre    identificador del nodo padre
     * @throws SystemException
     */
    private void generarOrganigrama(List<OrganigramaDTO> organigramaList, Map<String, String> idNodosPadre) throws SystemException {
        propiedades = new HashMap<>();

        for (OrganigramaDTO organigrama : organigramaList) {

            String codigoOrg = organigrama.getCodOrg();
            JSONObject entrada = new JSONObject();
            entrada.put("name", organigrama.getNomOrg());
            entrada.put(tipoNodo, recordCategoria);
            String idOrganAdmin = String.valueOf(organigrama.getIdeOrgaAdmin());
            if ("P".equalsIgnoreCase(organigrama.getTipo()) && !idNodosPadre.containsKey(idOrganAdmin)) {
                propiedades.put("rmc:xFondo", organigrama.getCodOrg());
                entrada.put(tagPropiedades, propiedades);
                idPadre = crearRootCategory(entrada);
                codigosRecord.put(codigoOrg, idPadre);
                idNodosPadre.put(idOrganAdmin, idPadre);
                codigoOrgAUX = organigrama.getCodOrg();

            } else {
                if (!codigosRecord.containsKey(codigoOrg)) {
                    propiedades.put("rmc:xSeccion", organigrama.getCodOrg());
                    entrada.put(tagPropiedades, propiedades);
                    idSubCategoria = crearNodo(entrada, codigosRecord.get(codigoOrgAUX));
                    codigoOrgAUX = organigrama.getCodOrg();
                    codigosRecord.put(organigrama.getCodOrg(), idSubCategoria);
                    idNodosPadre.put(idOrganAdmin, idPadre);
                } else {
                    codigoOrgAUX = organigrama.getCodOrg();
                }
            }
        }
    }

    /**
     * Permite generar las dependencias
     *
     * @param trdList objeto que contiene la lista de dependencias
     * @throws SystemException
     */
    private void generarDependencia(List<ContenidoDependenciaTrdDTO> trdList) throws SystemException {
        codigosSubseries = new HashMap<>();
        Map<String, String> codigoSeries = new HashMap<>();
        String codigoSerieAUX = "";
        for (ContenidoDependenciaTrdDTO trd : trdList) {

            disposicion.put(nombre, DiposicionFinalEnum.RETENER.getNombre());
            disposicion.put(descripcion, mensajeDescripcion.concat(" ").concat(trd.getRetArchivoGestion().toString().concat(" años en Archivo Gestion")));
            disposicion.put(periodo, "immediately");
            disposicion.put(localizacion, trd.getNomSerie());
            disposicion.put(propiedadPeriodo, "rma:dispositionAsOf");
            disposicion.put(eventoCompletar, true);
            disposicion.put(evento, "case_closed");
            if (codigoOrgAUX.equalsIgnoreCase(trd.getIdOrgOfc()) && (trd.getCodSubSerie() == null || trd.getCodSubSerie().equals("")) && !codigoSeries.containsKey(trd.getIdOrgOfc())) {
                idSerie = crearSerie(trd);
                codigoSeries.put(trd.getIdOrgOfc(), idSerie);

            } else {
                if (codigoSerieAUX.equals("") || !codigoSerieAUX.equals(trd.getCodSerie())) {
                    idSerie = crearSerie(trd);
                    codigoSerieAUX = trd.getCodSerie();
                    codigoSeries.put(trd.getIdOrgOfc(), idSerie);
                }
                if (trd.getCodSubSerie() != null && !trd.getCodSubSerie().equals("")) {
                    crearSubserie(trd, idSerie);

                }
            }
        }
    }

    /**
     * Permite crear las series
     *
     * @param trd objeto que contiene los parametros necesarios para crear la series
     * @return identifador de la serie creada
     * @throws SystemException
     */
    private String crearSerie(ContenidoDependenciaTrdDTO trd) throws SystemException {
        JSONObject serie = new JSONObject();
        String nombreSerie = trd.getIdOrgOfc().concat(".").concat(trd.getCodSerie()).concat("_").concat(trd.getNomSerie());
        int archivoCentral = (int) (trd.getRetArchivoGestion() + trd.getRetArchivoCentral());
        propiedades.put("rmc:xSerie", nombreSerie);
        propiedades.put("rmc:xCodSerie", trd.getCodSerie());
        serie.put(tagPropiedades, propiedades);
        serie.put("name", nombreSerie);
        serie.put(tipoNodo, recordCategoria);
        if (trd.getCodSubSerie() == null || trd.getCodSubSerie().equals("")) {
            serie.put("aspectNames", "rma:scheduled");
            idSerie = crearNodo(serie, idSubCategoria);
            crearTiempoRetencion(disposicion, idSerie);
            disposicion.replace(periodo, valorPeriodo.concat(String.valueOf(trd.getRetArchivoGestion())));
            disposicion.replace(descripcion, mensajeDescripcion.concat(" ").concat(trd.getRetArchivoCentral().toString().concat(" años en Archivo Central")));
            crearTiempoRetencion(disposicion, idSerie);
            disposicion.replace(nombre, DiposicionFinalEnum.obtenerClave(String.valueOf(trd.getDiposicionFinal())).getNombre());
            disposicion.replace(periodo, valorPeriodo.concat(String.valueOf(archivoCentral)));
            disposicion.replace(descripcion, trd.getProcedimiento());
            crearTiempoRetencion(disposicion, idSerie);
        } else {
            idSerie = crearNodo(serie, idSubCategoria);
        }
        return idSerie;
    }

    /**
     * Permite crear subseries
     *
     * @param trd     objeto que contiene la informacion necesaria para crear las subseries
     * @param idSerie identificador de la series a la que esta asociada la subserie
     * @return identificador de la subserie creada
     * @throws SystemException
     */
    private String crearSubserie(ContenidoDependenciaTrdDTO trd, String idSerie) throws SystemException {
        String idSubSerie = "";
        int archivoCentral = (int) (trd.getRetArchivoGestion() + trd.getRetArchivoCentral());
        JSONObject subSerie = new JSONObject();
        if ((!codigosSubseries.containsKey(trd.getCodSubSerie()) || !codigosSubseries.get(trd.getCodSubSerie()).equalsIgnoreCase(trd.getNomSubSerie())) && !trd.getCodSubSerie().equals("")) {
            String nombreSubserie = trd.getIdOrgOfc().concat(".").concat(trd.getCodSerie()).concat(".").concat(trd.getCodSubSerie()).concat("_").concat(trd.getNomSubSerie());
            subSerie.put("name", nombreSubserie);
            subSerie.put(tipoNodo, recordCategoria);
            subSerie.put("aspectNames", "rma:scheduled");
            propiedades.put("rmc:xSubserie", nombreSubserie);
            propiedades.put("rmc:xCodSubSerie", trd.getCodSubSerie());
            subSerie.put(tagPropiedades, propiedades);
            codigosSubseries.put(trd.getCodSubSerie(), trd.getNomSubSerie());
        }
        idSubSerie = crearNodo(subSerie, idSerie);
        crearTiempoRetencion(disposicion, idSubSerie);
        disposicion.replace("period", "year|".concat(String.valueOf(trd.getRetArchivoGestion())));
        disposicion.replace("description", mensajeDescripcion.concat(" ").concat(trd.getRetArchivoCentral().toString().concat(" años en Archivo Central")));
        crearTiempoRetencion(disposicion, idSubSerie);
        disposicion.replace("name", DiposicionFinalEnum.obtenerClave(String.valueOf(trd.getDiposicionFinal())).getNombre());
        disposicion.replace("period", valorPeriodo.concat(String.valueOf(archivoCentral)));
        disposicion.replace("description", trd.getProcedimiento());
        crearTiempoRetencion(disposicion, idSubSerie);

        return idSubSerie;
    }

    /**
     * Permite obtener id de la ruta
     *
     * @param respuestaJson objeto json con las informacion necesaria para obtener el id de la ruta
     * @param nodo
     * @param nombreNodo
     * @return
     */
    private String obtenerIdRuta(JSONObject respuestaJson, String nodo, String nombreNodo) {
        String codigoId = "";
        Iterator keys = respuestaJson.keys();
        while (keys.hasNext()) {
            Object key = keys.next();
            if ("list".equalsIgnoreCase(key.toString())) {
                JSONObject valor = respuestaJson.getJSONObject((String) key);
                JSONArray listaNodosJson = valor.getJSONArray("entries");
                for (int i = 0; i < listaNodosJson.length(); i++) {
                    JSONObject valorJson = (JSONObject) listaNodosJson.get(i);
                    codigoId = obtenerIdNodo(valorJson, nodo, nombreNodo);
                }

            }
        }
        return codigoId;
    }

    /**
     * Permite obtener el id del nodo
     *
     * @param respuestaJson objeto json con los paramtros necesarios para obtener el id del nodo
     * @param nodo          tipo de nodo a manejar
     * @param nombreNodo    nombre del nodo
     * @return id del nodo
     */
    private String obtenerIdNodo(JSONObject respuestaJson, String nodo, String nombreNodo) {
        String nodoId = "";
        Iterator keys1 = respuestaJson.keys();
        while (keys1.hasNext()) {
            Object key1 = keys1.next();
            if ("entry".equalsIgnoreCase(key1.toString())) {
                JSONObject valor1 = respuestaJson.getJSONObject((String) key1);
                if (valor1.getString(nodo).equalsIgnoreCase(nombreNodo))
                    nodoId = valor1.getString("id");
            }
        }
        return nodoId;
    }

    private boolean eliminarObjECM(BaseTypeId type, String id) {
        final String path = ((type == BaseTypeId.CMIS_DOCUMENT) ? "/records/" : "/record-folders/") + id;
        WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_RECORD));
        Response response = wt.path(path)
                .request()
                .header(headerAuthorization, valueAuthorization + " " + encoding)
                .header(headerAccept, valueApplicationType)
                .delete();
        return response.getStatus() == HttpURLConnection.HTTP_NO_CONTENT;
    }
}