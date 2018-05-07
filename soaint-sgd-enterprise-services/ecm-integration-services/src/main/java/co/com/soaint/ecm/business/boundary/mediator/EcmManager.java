package co.com.soaint.ecm.business.boundary.mediator;

import co.com.soaint.ecm.business.boundary.documentmanager.ContentManager;
import co.com.soaint.foundation.canonical.ecm.*;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.InfrastructureException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;


/**
 * Created by Dasiel
 */
@Service
public class EcmManager {

    private static final Logger logger = LogManager.getLogger(EcmManager.class.getName());
    private static final String ECM_ERROR = "### Error..------";

    @Autowired
    private
    ContentManager contentManager;

    /**
     * Metodo que llama el servicio para crear la estructura del ECM
     *
     * @param structure Objeto que contiene la estrucutra a crear en el ECM
     * @return Mensaje de respuesta(codigo y mensaje)
     * @throws InfrastructureException Excepcion ante errores del metodo
     */
    public MensajeRespuesta crearEstructuraECM(List<EstructuraTrdDTO> structure) {
        logger.info("### Creando estructura content..------");
        MensajeRespuesta response = new MensajeRespuesta();
        try {
            response = contentManager.crearEstructuraContent(structure);

        } catch (Exception e) {
            response.setCodMensaje("000005");
            response.setMensaje("Error al crear estructura");
            logger.error("### Error Creando estructura content..------", e);
        }

        return response;
    }

    /**
     * Metodo que llama el servicio para subir documentos de producción documental y los documentos adjuntos al ECM.
     *
     * @param documento              Documento que se va a subir
     * @param selector               Selector que dice donde se va a gauardar el documento
     * @return Identificador del documento creado
     * @throws InfrastructureException Excepcion ante errores del metodo
     */
    public MensajeRespuesta subirDocumentoPrincipalAdjunto( DocumentoDTO documento, String selector) throws IOException {
        logger.info("### Subiendo documento adjunto al content..");
        MensajeRespuesta response = new MensajeRespuesta();
        try {
            response = contentManager.subirDocumentoPrincipalAdjuntoContent(documento, selector);
        } catch (Exception e) {
            logger.error(ECM_ERROR, e);
            response.setCodMensaje("2222");
            response.setMensaje("Error ECM Subiendo documento adjunto al content");
            throw e;
        }

        return response;
    }

    /**
     * Metodo que llama el servicio para crear el link del documentos en la carpeta DOCUMENTOS DE APOYO en el ECM.
     *
     * @param documento              Documento que se va a subir
     * @return Identificador del documento creado
     * @throws InfrastructureException Excepcion ante errores del metodo
     */
    public MensajeRespuesta crearLinkDocumentosApoyo( DocumentoDTO documento) throws IOException {
        logger.info("### Creando link del documento en el content..");
        MensajeRespuesta response = new MensajeRespuesta();
        try {
            response = contentManager.crearLinkContent(documento);
        } catch (Exception e) {
            logger.error(ECM_ERROR, e);
            response.setCodMensaje("2222");
            response.setMensaje("Error ECM Creando link del documento en el content");
            throw e;
        }

        return response;
    }

    /**
     * Metodo que llama el servicio para subir versionar documentos generado de producción documental al ECM.
     *
     * @param documento              Documento que se va a subir
     * @param selector               parametro que indica donde se va a guardar el documento
     * @return Identificador del documento creado
     * @throws InfrastructureException Excepcion ante errores del metodo
     */
    public MensajeRespuesta subirVersionarDocumentoGenerado(DocumentoDTO documento, String selector) throws IOException {
        logger.info("### Subiendo/Versionando documento generado al content..");
        MensajeRespuesta response = new MensajeRespuesta();
        try {
            response = contentManager.subirVersionarDocumentoGeneradoContent(documento, selector);
        } catch (Exception e) {
            logger.error(ECM_ERROR, e);
            response.setCodMensaje("2222");
            response.setMensaje("Error ECM Subiendo/Versionando documento generado al content");
            throw e;
        }

        return response;
    }

    /**
     * Metodo que llama el servicio para buscar los documentos adjuntos al ECM dado el Id del documento Principal.
     *
     * @param documento DTO que contiene los datos de la búsqueda
     * @return Lista de objetos de documentos adjuntos.
     * @throws InfrastructureException Excepcion ante errores del metodo
     */
    public MensajeRespuesta obtenerDocumentosAdjuntos(DocumentoDTO documento) throws IOException {
        logger.info("### Obteniendo los documentos adjuntos dado id de doc Principal..");
        MensajeRespuesta response = new MensajeRespuesta();
        try {
            response = contentManager.obtenerDocumentosAdjuntosContent(documento);
        } catch (Exception e) {
            logger.error(ECM_ERROR, e);
            response.setCodMensaje("2222");
            response.setMensaje(ECM_ERROR);
            throw e;
        }

        return response;
    }

    /**
     * Metodo que llama el servicio para buscar las versiones del documentos en el ECM dado el Id del documento.
     *
     * @param idDoc Identificador del documento para obtener los documentos.
     * @return Lista de objetos de documentos adjuntos.
     * @throws InfrastructureException Excepcion ante errores del metodo
     */
    public MensajeRespuesta obtenerVersionesDocumentos(String idDoc) throws IOException {
        logger.info("### Obteniendo las versiones del documento dado id..");
        MensajeRespuesta response = new MensajeRespuesta();
        try {
            response = contentManager.obtenerVersionesDocumentoContent(idDoc);
        } catch (Exception e) {
            logger.error("### Error ECM..------", e);
            response.setCodMensaje("2222");
            response.setMensaje(ECM_ERROR);
            throw e;
        }

        return response;
    }

    /**
     * Metodo que llama el servicio para modificar metadatos del documento en el ECM
     *
     * @param metadatosDocumentos DTO que contiene los metadatos del documento
     * @return Identificador del documento creado
     * @throws InfrastructureException Excepcion ante errores del metodo
     */
    public MensajeRespuesta modificarMetadatosDocumento(DocumentoDTO metadatosDocumentos) throws IOException {
        logger.info("### Subiendo documento al content..");
        MensajeRespuesta response = new MensajeRespuesta();
        try {
            response = contentManager.modificarMetadatoDocumentoContent(metadatosDocumentos);
        } catch (Exception e) {
            logger.error(ECM_ERROR, e);
            response.setCodMensaje("2222");
            response.setMensaje(ECM_ERROR);
            throw e;
        }

        return response;
    }

    /**
     * Metodo que llama el servicio para mover documentos dentro del ECM
     *
     * @param documento      Nombre del documento a mover
     * @param carpetaFuente  Carpeta donde se encuentra el documento
     * @param carpetaDestino Carpeta a donde se va a mover el documento.
     * @return Mensaje de respuesta (codigo y mensaje)
     * @throws InfrastructureException Excepcion ante errores del metodo
     */
    public MensajeRespuesta moverDocumento(String documento, String carpetaFuente, String carpetaDestino) {
        logger.info("### Moviendo documento dentro del content..");
        MensajeRespuesta response = new MensajeRespuesta();
        try {

            response = contentManager.moverDocumento(documento, carpetaFuente, carpetaDestino);

        } catch (Exception e) {
            response.setCodMensaje("000002");
            response.setMensaje("Error al mover documento");
            logger.error(ECM_ERROR, e);
        }

        return response;
    }

    /**
     * Metodo generico para descargar los documentos del ECM
     *
     * @param documentoDTO Objeto que contiene los metadatos
     * @return Documento
     */
    public Response descargarDocumento(DocumentoDTO documentoDTO) {
        logger.info("### Descargando documento del content..");

        try {
            MensajeRespuesta mensajeRespuesta = contentManager.descargarDocumentoContent(documentoDTO);
            if ("0000".equals(mensajeRespuesta.getCodMensaje())) {
                logger.info("### Se devuelve el documento del content..");
                return Response.ok(mensajeRespuesta.getDocumentoDTOList().get(0).getDocumento())
                        .header("Content-Disposition", "attachment; filename=" + documentoDTO.getNombreDocumento()) //optional
                        .build();
            } else {
                logger.info("### Error al devolver documento del content..");
                return Response.serverError().build();
            }

        } catch (Exception e) {
            logger.error("Error descargando documento", e);
            return Response.serverError().build();
        }

    }

    /**
     * Metodo generico para eliminar los documentos del ECM
     *
     * @param idDoc Identificador del documento dentro del ECM
     * @return True en exito false en error
     */
    public boolean eliminarDocumentoECM(String idDoc) {
        logger.info("### Eliminando documento del content..");
        try {
            if (contentManager.eliminarDocumento(idDoc)) {
                logger.info("### Se elimina el documento del content..");
                return Boolean.TRUE;
            } else {
                logger.error("No se pudo eliminar el documento");
                return Boolean.FALSE;
            }
        } catch (Exception e) {
            logger.error("Error eliminando documento", e);
            return Boolean.FALSE;
        }
    }

    /**
     * Metodo para devolver las series o subseries
     * @param contenidoDependenciaTrdDTO Objeto que contiene los parametro de búsqueda
     * @return contenidoDependenciaTrdDTO
     */
    public MensajeRespuesta devolverSerieSubserie(ContenidoDependenciaTrdDTO contenidoDependenciaTrdDTO) throws Exception {
        logger.info("### Obteniendo las series o subseries dada la dependencia del content..");
        return contentManager.devolverSeriesSubseries(contenidoDependenciaTrdDTO);
    }

    /**
     * Metodo para devolver crear las unidades documentales
     * @param unidadDocumentalDTO DTO que contiene los parametro de búsqueda
     * @return MensajeRespuesta
     */
    public MensajeRespuesta crearUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO) throws BusinessException {
        logger.info("### Creando la unidad documental {} ..", unidadDocumentalDTO);
        return contentManager.crearUnidadDocumental(unidadDocumentalDTO);
    }

    /**
     * Listar las Unidades Documentales del ECM
     *
     * @return Mensaje de respuesta
     */
    public MensajeRespuesta listarUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO) throws BusinessException {
        logger.info("### Listando las Unidades Documentales");
        return contentManager.listarUnidadDocumental(unidadDocumentalDTO);
    }

    /**
     * Metodo para listar los documentos de una Unidad Documental
     *
     * @param idDocumento     Id Documento
     * @return MensajeRespuesta con los detalles del documento
     */
    public MensajeRespuesta obtenerDetallesDocumentoDTO(String idDocumento) throws Exception {
        logger.info("### Mostrando el documento con id {}", idDocumento);
        return contentManager.obtenerDetallesDocumentoDTO(idDocumento);
    }

    /**
     * Metodo para devolver la Unidad Documental
     *
     * @param idUnidadDocumental     Id Unidad Documental
     * @return MensajeRespuesta      Unidad Documntal
     */
    public MensajeRespuesta detallesUnidadDocumental(String idUnidadDocumental) throws Exception {
        logger.info("### Mostrando la Unidad Documental con id {}", idUnidadDocumental);
        logger.info("Ejecutando metodo MensajeRespuesta detallesUnidadDocumental(String idUnidadDocumental)");
        return contentManager.detallesUnidadDocumental(idUnidadDocumental);
    }

    /**
     * Metodo para devolver la Unidad Documental
     *
     * @param unidadDocumentalDTO     Obj Unidad Documental
     * @return MensajeRespuesta       Unidad Documental
     */
    public MensajeRespuesta subirDocumentosUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO) throws Exception {
        logger.info("Ejecutando metodo MensajeRespuesta subirDocumentosUnidadDocumentalECM(unidadDocumentalDTO, documentoDTO)");
        return contentManager.subirDocumentosUnidadDocumentalECM(unidadDocumentalDTO);
    }

    /**
     * Operacion para devolver los documentos por archivar
     */
    public MensajeRespuesta getDocumentosPorArchivar() throws Exception {
        logger.info("processing rest request - Obtener los documentos por archivar en el ECM");
        return contentManager.getDocumentosPorArchivar();
    }

    /**
     * Metodo para Modificar Unidades Documentales
     *
     * @param unidadDocumentalDTOS    Lista de unidades a modificar
     * @return MensajeRespuesta       Unidad Documental
     */
    public MensajeRespuesta modificarUnidadesDocumentales(List<UnidadDocumentalDTO> unidadDocumentalDTOS) throws Exception {
        logger.info("processing rest request - modificar las unidades documentales en el ECM");
        return contentManager.modificarUnidadesDocumentales(unidadDocumentalDTOS);
    }

    /**
     * Operacion para devolver series o subseries
     *
     * @param documentoDTOS Lista de documentos a archivar
     * @return MensajeRespuesta
     */
    public MensajeRespuesta subirDocumentosTemporalesUD(List<DocumentoDTO> documentoDTOS) {
        logger.info("processing rest request - Subir Documentos temporales ECM");
        return contentManager.subirDocumentosTemporalesUD(documentoDTOS);
    }
}
