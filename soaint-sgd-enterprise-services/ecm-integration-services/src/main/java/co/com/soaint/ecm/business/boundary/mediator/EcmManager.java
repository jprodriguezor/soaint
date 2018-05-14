package co.com.soaint.ecm.business.boundary.mediator;

import co.com.soaint.ecm.business.boundary.documentmanager.ContentManager;
import co.com.soaint.foundation.canonical.ecm.*;
import co.com.soaint.foundation.framework.exceptions.InfrastructureException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.List;


/**
 * Created by Dasiel
 */
@Log4j2
@Service
public class EcmManager implements Serializable {

    private static final String ECM_ERROR = "### Error..------";

    @Autowired
    private ContentManager contentManager;

    /**
     * Metodo que llama el servicio para crear la estructura del ECM
     *
     * @param structure Objeto que contiene la estrucutra a crear en el ECM
     * @return Mensaje de respuesta(codigo y mensaje)
     * @throws InfrastructureException Excepcion ante errores del metodo
     */
    public MensajeRespuesta crearEstructuraECM(List<EstructuraTrdDTO> structure) {
        log.info("### Creando estructura content..------");
        return contentManager.crearEstructuraContent(structure);
    }

    /**
     * Metodo que llama el servicio para subir documentos de producción documental y los documentos adjuntos al ECM.
     *
     * @param documento Documento que se va a subir
     * @param selector  Selector que dice donde se va a gauardar el documento
     * @return Identificador del documento creado
     * @throws InfrastructureException Excepcion ante errores del metodo
     */
    public MensajeRespuesta subirDocumentoPrincipalAdjunto(DocumentoDTO documento, String selector) {
        log.info("### Subiendo documento adjunto al content..");
        return contentManager.subirDocumentoPrincipalAdjuntoContent(documento, selector);
    }

    /**
     * Metodo que llama el servicio para crear el link del documentos en la carpeta DOCUMENTOS DE APOYO en el ECM.
     *
     * @param documento Documento que se va a subir
     * @return Identificador del documento creado
     * @throws InfrastructureException Excepcion ante errores del metodo
     */
    public MensajeRespuesta crearLinkDocumentosApoyo(DocumentoDTO documento) {
        log.info("### Creando link del documento en el content..");
        return contentManager.crearLinkContent(documento);
    }

    /**
     * Metodo que llama el servicio para subir versionar documentos generado de producción documental al ECM.
     *
     * @param documento Documento que se va a subir
     * @param selector  parametro que indica donde se va a guardar el documento
     * @return Identificador del documento creado
     * @throws InfrastructureException Excepcion ante errores del metodo
     */
    public MensajeRespuesta subirVersionarDocumentoGenerado(DocumentoDTO documento, String selector) {
        log.info("### Subiendo/Versionando documento generado al content..");
        return contentManager.subirVersionarDocumentoGeneradoContent(documento, selector);
    }

    /**
     * Metodo que llama el servicio para buscar los documentos adjuntos al ECM dado el Id del documento Principal.
     *
     * @param documento DTO que contiene los datos de la búsqueda
     * @return Lista de objetos de documentos adjuntos.
     * @throws InfrastructureException Excepcion ante errores del metodo
     */
    public MensajeRespuesta obtenerDocumentosAdjuntos(DocumentoDTO documento) {
        log.info("### Obteniendo los documentos adjuntos dado id de doc Principal..");
        return contentManager.obtenerDocumentosAdjuntosContent(documento);
    }

    /**
     * Metodo que llama el servicio para buscar las versiones del documentos en el ECM dado el Id del documento.
     *
     * @param idDoc Identificador del documento para obtener los documentos.
     * @return Lista de objetos de documentos adjuntos.
     * @throws InfrastructureException Excepcion ante errores del metodo
     */
    public MensajeRespuesta obtenerVersionesDocumentos(String idDoc) {
        log.info("### Obteniendo las versiones del documento dado id..");
        return contentManager.obtenerVersionesDocumentoContent(idDoc);
    }

    /**
     * Metodo que llama el servicio para modificar metadatos del documento en el ECM
     *
     * @param metadatosDocumentos DTO que contiene los metadatos del documento
     * @return Identificador del documento creado
     * @throws InfrastructureException Excepcion ante errores del metodo
     */
    public MensajeRespuesta modificarMetadatosDocumento(DocumentoDTO metadatosDocumentos) {
        log.info("### Subiendo documento al content..");
        return contentManager.modificarMetadatoDocumentoContent(metadatosDocumentos);
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
        log.info("### Moviendo documento dentro del content..");
        return contentManager.moverDocumento(documento, carpetaFuente, carpetaDestino);
    }

    /**
     * Metodo generico para descargar los documentos del ECM
     *
     * @param documentoDTO Objeto que contiene los metadatos
     * @return Documento
     */
    public Response descargarDocumento(DocumentoDTO documentoDTO) throws SystemException {
        log.info("### Descargando documento del content..");
        MensajeRespuesta mensajeRespuesta = contentManager.descargarDocumentoContent(documentoDTO);
        if ("0000".equals(mensajeRespuesta.getCodMensaje())) {
            log.info("### Se devuelve el documento del content..");
            return Response.ok(mensajeRespuesta.getDocumentoDTOList().get(0).getDocumento())
                    .header("Content-Disposition", "attachment; filename=" + documentoDTO.getNombreDocumento()) //optional
                    .build();
        } else {
            log.info("### Error al devolver documento del content..");
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
        log.info("### Eliminando documento del content..");
        return contentManager.eliminarDocumento(idDoc);
    }

    /**
     * Metodo para devolver las series o subseries
     *
     * @param contenidoDependenciaTrdDTO Objeto que contiene los parametro de búsqueda
     * @return contenidoDependenciaTrdDTO
     */
    public MensajeRespuesta devolverSerieSubserie(ContenidoDependenciaTrdDTO contenidoDependenciaTrdDTO) throws SystemException {
        log.info("### Obteniendo las series o subseries dada la dependencia del content..");
        return contentManager.devolverSeriesSubseries(contenidoDependenciaTrdDTO);
    }

    /**
     * Metodo para devolver crear las unidades documentales
     *
     * @param unidadDocumentalDTO DTO que contiene los parametro de búsqueda
     * @return MensajeRespuesta
     */
    public MensajeRespuesta crearUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO) throws SystemException {
        log.info("### Creando la unidad documental {} ..", unidadDocumentalDTO);
        return contentManager.crearUnidadDocumental(unidadDocumentalDTO);
    }

    /**
     * Listar las Unidades Documentales del ECM
     *
     * @return Mensaje de respuesta
     */
    public MensajeRespuesta listarUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO) throws SystemException {
        log.info("### Listando las Unidades Documentales");
        return contentManager.listarUnidadDocumental(unidadDocumentalDTO);
    }

    /**
     * Metodo para listar los documentos de una Unidad Documental
     *
     * @param idDocumento Id Documento
     * @return MensajeRespuesta con los detalles del documento
     */
    public MensajeRespuesta obtenerDetallesDocumentoDTO(String idDocumento) throws Exception {
        log.info("### Mostrando el documento con id {}", idDocumento);
        return contentManager.obtenerDetallesDocumentoDTO(idDocumento);
    }

    /**
     * Metodo para devolver la Unidad Documental
     *
     * @param idUnidadDocumental Id Unidad Documental
     * @return MensajeRespuesta      Unidad Documntal
     */
    public MensajeRespuesta detallesUnidadDocumental(String idUnidadDocumental) throws Exception {
        log.info("### Mostrando la Unidad Documental con id {}", idUnidadDocumental);
        log.info("Ejecutando metodo MensajeRespuesta detallesUnidadDocumental(String idUnidadDocumental)");
        return contentManager.detallesUnidadDocumental(idUnidadDocumental);
    }

    /**
     * Metodo para devolver la Unidad Documental
     *
     * @param unidadDocumentalDTO Obj Unidad Documental
     * @return MensajeRespuesta       Unidad Documental
     */
    public MensajeRespuesta subirDocumentosUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO) throws SystemException {
        log.info("Ejecutando metodo MensajeRespuesta subirDocumentosUnidadDocumentalECM(unidadDocumentalDTO, documentoDTO)");
        return contentManager.subirDocumentosUnidadDocumentalECM(unidadDocumentalDTO);
    }

    /**
     * Operacion para devolver los documentos por archivar
     */
    public MensajeRespuesta getDocumentosPorArchivar(final String codigoDependencia) throws SystemException {
        log.info("processing rest request - Obtener los documentos por archivar en el ECM");
        return contentManager.getDocumentosPorArchivar(codigoDependencia);
    }

    /**
     * Metodo para Modificar Unidades Documentales
     *
     * @param unidadDocumentalDTOS Lista de unidades a modificar
     * @return MensajeRespuesta       Unidad Documental
     */
    public MensajeRespuesta modificarUnidadesDocumentales(List<UnidadDocumentalDTO> unidadDocumentalDTOS) throws SystemException {
        log.info("processing rest request - modificar las unidades documentales en el ECM");
        return contentManager.modificarUnidadesDocumentales(unidadDocumentalDTOS);
    }

    /**
     * Operacion para devolver series o subseries
     *
     * @param documentoDTOS Lista de documentos a archivar
     * @return MensajeRespuesta
     */
    public MensajeRespuesta subirDocumentosTemporalesUD(List<DocumentoDTO> documentoDTOS) throws SystemException {
        log.info("processing rest request - Subir Documentos temporales ECM");
        return contentManager.subirDocumentosTemporalesUD(documentoDTOS);
    }

    /**
     * Operacion para devolver series o subseries
     *
     * @param codigoDependencia Codigo de la dependencia
     * @return MensajeRespuesta
     */
    public MensajeRespuesta obtenerDocumentosArchivados(String codigoDependencia) throws SystemException {
        log.info("processing rest request - Obtener Documentos archivados ECM");
        return contentManager.obtenerDocumentosArchivados(codigoDependencia);
    }

    /**
     * Operacion para devolver sedes, dependencias, series o subseries
     *
     * @param dependenciaTrdDTO Objeto que contiene los datos de filtrado
     * @return MensajeRespuesta
     */
    public MensajeRespuesta listarDependenciaMultipleECM(ContenidoDependenciaTrdDTO dependenciaTrdDTO) throws SystemException {
        log.info("processing rest request - Obtener las sedes, dependencias, series o subseries");
        return contentManager.listarDependenciaMultiple(dependenciaTrdDTO);
    }

    /**
     * Crear carpeta en el Record
     *
     * @param disposicionFinalDTO Obj con el DTO Unidad Documental y l listado de las disposiciones
     * @return Mensaje de respuesta
     */
    public MensajeRespuesta listarUdDisposicionFinal(DisposicionFinalDTO disposicionFinalDTO) throws SystemException {
        log.info("processing rest request - Listar las unidades documentales que hay culminado su tiempo de retencion");
        return contentManager.listarUdDisposicionFinal(disposicionFinalDTO);
    }

    /**
     * Metodo para cerrar una o varias unidades documentales
     *
     * @param unidadDocumentalDTOS   Lista Unidades Documentales para aprobar/rechazar
     * @return MensajeRespuesta
     */
    public MensajeRespuesta aprobarRechazarDisposicionesFinales(List<UnidadDocumentalDTO> unidadDocumentalDTOS) throws SystemException {
        log.info("Ejecutando metodo MensajeRespuesta aprobarRechazarDisposicionesFinalesECM(List<UnidadDocumentalDTO> unidadDocumentalDTOS)");
        return contentManager.aprobarRechazarDisposicionesFinales(unidadDocumentalDTOS);
    }

    /**
     * Operacion para Subir documentos a una UD temporal ECM
     *
     * @param documentoDTO Obj de documento DTO a archivar
     * @return MensajeRespuesta
     */
    public MensajeRespuesta subirDocumentoTemporalUD(DocumentoDTO documentoDTO) throws SystemException {
        log.info("processing rest request - Subir Documento temporal ECM");
        return contentManager.subirDocumentoTemporalUD(documentoDTO);
    }
}
