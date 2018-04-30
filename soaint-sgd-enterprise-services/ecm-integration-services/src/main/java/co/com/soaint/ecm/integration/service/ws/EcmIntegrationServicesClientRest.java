package co.com.soaint.ecm.integration.service.ws;

import co.com.soaint.ecm.business.boundary.mediator.EcmManager;
import co.com.soaint.ecm.domain.entity.AccionUsuario;
import co.com.soaint.foundation.canonical.ecm.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

/**
 * Created by Dasiel
 */


@Path("/ecm")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Service
public class EcmIntegrationServicesClientRest {
    @Autowired
    private
    EcmManager fEcmManager;

    private static final Logger logger = LogManager.getLogger(EcmIntegrationServicesClientRest.class.getName());

    /**
     * Constructor de la clase
     */
    public EcmIntegrationServicesClientRest() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     * Crear estructura del ECM
     *
     * @param structure Estructura a crear
     * @return Mensaje de respuesta
     */
    @POST
    @Path("/crearEstructuraContent/")
    public MensajeRespuesta crearEstructuraContent(List<EstructuraTrdDTO> structure) {
        logger.info("processing rest request - Crear Estructura ECM");
        try {
            return fEcmManager.crearEstructuraECM(structure);
        } catch (RuntimeException e) {
            logger.error("Error servicio creando estructura ", e);
            throw e;
        }
    }

    /**
     * Subir documento Principal al ECM
     *
     * @param selector Selector que dice donde se va a gauardar el documento
     * @return identificador del documento en el ecm
     */
    @POST
    @Path("/subirDocumentoRelacionECM/{selector}")
    public MensajeRespuesta subirDocumentoPrincipalAdjuntoECM(@RequestBody DocumentoDTO documento,
                                                              @PathParam("selector") String selector) throws IOException {
        logger.info("processing rest request - Subir Documento Adjunto al ECM " + documento.getNombreDocumento());
        try {
            return fEcmManager.subirDocumentoPrincipalAdjunto(documento, selector);
        } catch (IOException e) {
            logger.error("Error en operacion - Subir Documento Adjunto ECM ", e);
            throw e;
        }
    }

    /**
     * Crear link de documento en el ECM
     *
     * @return identificador del documento en el ecm
     */
    @POST
    @Path("/crearLinkDocumento/")
    public MensajeRespuesta crearLinkDocumentosApoyo(@RequestBody DocumentoDTO documento) throws IOException {
        logger.info("processing rest request - Crear Link de Documento en la carpeta Documentos de Apoyo para el documento: {}", documento.getNombreDocumento());
        try {
            return fEcmManager.crearLinkDocumentosApoyo(documento);
        } catch (IOException e) {
            logger.error("Error en operacion - Crear Link de Documento en la carpeta Documentos de Apoyo ", e);
            throw e;
        }
    }

    /**
     * Subir versionar documento Generado al ECM
     *
     * @param documento documento a subir
     * @param selector  parametro que indica donde se va a guardar el documento
     * @return identificador del documento en el ecm
     */
    @POST
    @Path("/subirVersionarDocumentoGeneradoECM/{selector}")
    public MensajeRespuesta subirVersionarDocumentoGeneradoECM(@RequestBody DocumentoDTO documento,
                                                               @PathParam("selector") String selector) throws IOException {
        logger.info("processing rest request - Subir Versionar Documento Generado al ECM " + documento.getNombreDocumento());
        try {

            return fEcmManager.subirVersionarDocumentoGenerado(documento, selector);
        } catch (IOException e) {
            logger.error("Error en operacion - Subir Versionar Documento Generado al ECM ", e);
            throw e;
        }
    }

    /**
     * Obtener id documentos adjuntos dado id documento principal
     *
     * @param documento DTO que contiene los datos de la búsqueda
     * @return Lista de documentos adjuntos ECM
     */
    @POST
    @Path("/obtenerDocumentosAdjuntosECM/")
    public MensajeRespuesta obtenerDocumentoPrincipalAdjunto(@RequestBody DocumentoDTO documento) throws IOException {

        logger.info("processing rest request - Buscar Documento en el ECM: {}",documento);
        try {
            return fEcmManager.obtenerDocumentosAdjuntos(documento);
        } catch (IOException e) {
            logger.error("Error en operacion - Buscar Documento Adjunto en el ECM ", e);
            throw e;
        }
    }

    /**
     * Obtener versiones de un documento dado su id
     *
     * @param idDoc Identificador del documento
     * @return Lista de documentos adjuntos ECM
     */
    @POST
    @Path("/obtenerVersionesDocumentos/{idDoc}")
    public MensajeRespuesta obtenerVersionesDocumento(@PathParam("idDoc") String idDoc) throws IOException {
        logger.info("processing rest request - Buscar Versiones del Documento en el ECM dado id: " + idDoc);
        try {
            return fEcmManager.obtenerVersionesDocumentos(idDoc);
        } catch (IOException e) {
            logger.error("Error en operacion - Buscar Versiones de Documento en el ECM ", e);
            throw e;
        }
    }

    /**
     * Modificar metadatos a documento en el ECM
     *
     * @param metadatos metadatos del documento
     * @return identificador del documento en el ecm
     */
    @POST
    @Path("/modificarMetadatosDocumentoECM/")
    public MensajeRespuesta modificarMetadatosDocumentoECM(@RequestBody DocumentoDTO metadatos) throws IOException {
        logger.info("processing rest request - Subir Documento ECM " + metadatos.getIdDocumento());
        try {
            return fEcmManager.modificarMetadatosDocumento(metadatos);
        } catch (IOException e) {
            logger.error("Error en operacion - Modificar Metadatos Documento ECM ", e);
            throw e;
        }
    }

    /**
     * Modificar  documento en el ECM
     *
     * @param metadatos metadatos del documento
     * @return identificador del documento en el ecm
     */
    @POST
    @Path("/modificarDocumentoECM/")
    public MensajeRespuesta modificarDocumentoECM(@RequestBody DocumentoDTO metadatos) throws IOException {
        logger.info("processing rest request - Modificar Documento ECM " + metadatos.getIdDocumento());
        try {
            return fEcmManager.modificarMetadatosDocumento(metadatos);
        } catch (IOException e) {
            logger.error("Error en operacion - Modificar Documento ECM ", e);
            throw e;
        }
    }

    /**
     * Operacion para mover documentos
     *
     * @param moverDocumento nombre del documento
     * @param carpetaFuente  fuente
     * @param carpetaDestino destino
     * @return Respuesta
     */
    @POST
    @Path("/moverDocumentoECM/")
    public MensajeRespuesta moverDocumentoECM(@QueryParam("moverDocumento") final String moverDocumento,
                                              @QueryParam("carpetaFuente") final String carpetaFuente,
                                              @QueryParam("carpetaDestino") final String carpetaDestino) {
        logger.info("processing rest request - Mover Documento ECM");
        try {
            return fEcmManager.moverDocumento(moverDocumento, carpetaFuente, carpetaDestino);
        } catch (RuntimeException e) {
            logger.error("Error servicio moviendo documento ", e);
            throw e;
        }
    }

    /**
     * Operacion para descargar documentos
     *
     * @param identificadorDoc identificador del documento
     * @return Documento
     */
    @GET
    @Path("/descargarDocumentoECM/")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response descargarDocumentoECM(@QueryParam("identificadorDoc") final String identificadorDoc) {
        logger.info("processing rest request - Descargar Documento ECM");
        try {
            DocumentoDTO documentoDTO = new DocumentoDTO();
            documentoDTO.setIdDocumento(identificadorDoc);
            return fEcmManager.descargarDocumento(documentoDTO);
        } catch (RuntimeException e) {
            logger.error("Error servicio descargando documento ", e);
            throw e;
        }
    }

    /**
     * Operación para descargar una versión específica de un documento
     *
     * @param identificadorDoc identificador del documento
     * @param version          Versión que servirá de filtro para obtener el documento
     * @return Documento
     */
    @GET
    @Path("/descargarDocumentoVersionECM/")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response descargarDocumentoVersionECM(@QueryParam("identificadorDoc") final String identificadorDoc,
                                                 @QueryParam("version") final String version) {
        logger.info("processing rest request - Descargar Documento ECM");
        try {
            DocumentoDTO documentoDTO = new DocumentoDTO();
            documentoDTO.setIdDocumento(identificadorDoc);
            documentoDTO.setVersionLabel(version);
            return fEcmManager.descargarDocumento(documentoDTO);
        } catch (RuntimeException e) {
            logger.error("Error servicio descargando documento ", e);
            throw e;
        }
    }

    /**
     * Operacion para eliminar documentos
     *
     * @param idDocumento Identificador del documento
     * @return True en exito false en error
     */
    @DELETE
    @Path("/eliminarDocumentoECM/{idDocumento}")
    public boolean eliminarDocumentoECM(@PathParam("idDocumento") String idDocumento) {
        logger.info("processing rest request - Eliminar Documento ECM");
        try {
            boolean respuesta;
            respuesta = fEcmManager.eliminarDocumentoECM(idDocumento);
            if (respuesta)
                logger.info("Documento eliminado con exito");
            else
                logger.info("No se pudo eliminar el documento");
            return respuesta;
        } catch (RuntimeException e) {
            logger.error("Error servicio eliminando documento ", e);
            throw e;
        }
    }

    /**
     * Operacion para devolver series o subseries
     *
     * @param dependenciaTrdDTO Objeto que contiene los datos de filtrado
     * @return dependenciaTrdDTO
     */
    @POST
    @Path("/devolverSerieOSubserieECM/")
    public MensajeRespuesta devolverSerieSubserie(@RequestBody ContenidoDependenciaTrdDTO dependenciaTrdDTO)  {
        logger.info("processing rest request - Obtener las series o subseries de la dependencia con código " + dependenciaTrdDTO.getIdOrgOfc());
        try {
            return fEcmManager.devolverSerieSubserie(dependenciaTrdDTO);
        } catch (Exception e) {
            logger.error("Error en operacion - Devolver Serie Subserie ECM ", e);
            MensajeRespuesta respuesta = new MensajeRespuesta();
            respuesta.setCodMensaje("1112");
            respuesta.setMensaje("Error del Sistema: " + e.getMessage());
            return respuesta;
        }
    }


    /*
    * UNIDADES DOCUMENTALES
    */

    /**
     * Crear unidad documental en el ECM
     *
     * @param unidadDocumentalDTO Unidad a crear
     * @return Mensaje de respuesta
     */
    @POST
    @Path("/crearUnidadDocumentalECM/")
    public MensajeRespuesta crearUnidadDocumentalECM(@RequestBody UnidadDocumentalDTO unidadDocumentalDTO) {
        logger.info("processing rest request - Crear Unidad Documental ECM");
        try {
            return fEcmManager.crearUnidadDocumental(unidadDocumentalDTO);
        } catch (Exception e) {
            logger.error("Error en operacion - crearUnidadDocumentalECM ", e);
            MensajeRespuesta respuesta = new MensajeRespuesta();
            respuesta.setCodMensaje("11111");
            respuesta.setMensaje("Causa: " + e.getCause() + ", Mensaje: " + e.getMessage());
            return respuesta;
        }
    }

    /**
     * Listar las Unidades Documentales del ECM
     *
     * @param unidadDocumentalDTO Objeto que contiene el criterio de Busqueda
     * @return MensajeRespuesta Mensaje de respuesta
     */
    @POST
    @Path("/listarUnidadesDocumentalesECM/")
    public MensajeRespuesta listarUnidadDocumentalECM(@RequestBody UnidadDocumentalDTO unidadDocumentalDTO) {
        logger.info("processing rest request - Listar Unidades Documentales ECM");
        try {
            return fEcmManager.listarUnidadDocumental(unidadDocumentalDTO);
        } catch (Exception e) {
            logger.error("Error en operacion - listarUnidadDocumentalECM(unidadDocumentalDTO) ", e);
            MensajeRespuesta respuesta = new MensajeRespuesta();
            respuesta.setCodMensaje("11111");
            respuesta.setMensaje("Causa: " + e.getCause() + ", Mensaje: " + e.getMessage());
            return respuesta;
        }
    }

    /**
     * Metodo para listar los documentos de una Unidad Documental
     *
     * @param idDocumento     Id Documento
     * @return MensajeRespuesta con los detalles del documento
     */
    @GET
    @Path("/obtenerDetallesDocumentoECM/{idDoc}")
    public MensajeRespuesta obtenerDetallesDocumentoDTO(@PathParam("idDoc") String idDocumento) {
        logger.info("Ejecutando metodo MensajeRespuesta obtenerDetallesDocumentoDTO(String idDocumento)");
        try {
            return fEcmManager.obtenerDetallesDocumentoDTO(idDocumento);
        } catch (Exception e) {
            logger.error("Error en operacion - obtenerDetallesDocumentoDTO ", e);
            MensajeRespuesta respuesta = new MensajeRespuesta();
            respuesta.setCodMensaje("11111");
            respuesta.setMensaje("Causa: " + e.getCause() + ", Mensaje: " + e.getMessage());
            return respuesta;
        }
    }

    /**
     * Metodo para listar los documentos de una Unidad Documental
     *
     * @param idUnidadDocumental   Id de la unidad documental
     * @return MensajeRespuesta
     */
    @GET
    @Path("/listaDocumentosDTOUnidadDocumental/{idUnidadDocumental}")
    public MensajeRespuesta listaDocumentosDTOUnidadDocumental(@PathParam("idUnidadDocumental") String idUnidadDocumental) {
        logger.info("Ejecutando metodo MensajeRespuesta listaDocumentoDTO(UnidadDocumentalDTO dto)");
        try {
            return fEcmManager.listaDocumentosDTOUnidadDocumental(idUnidadDocumental);
        } catch (Exception e) {
            logger.error("Error en operacion - Devolver Listado de Documentos de una unidad documental ", e);
            MensajeRespuesta respuesta = new MensajeRespuesta();
            respuesta.setCodMensaje("11111");
            respuesta.setMensaje("Causa: " + e.getCause() + ", Mensaje: " + e.getMessage());
            return respuesta;
        }
    }

    /**
     * Metodo para devolver la Unidad Documental
     *
     * @param idUnidadDocumental     Id Unidad Documental
     * @return MensajeRespuesta      Unidad Documntal
     */
    @GET
    @Path("/verDetalleUnidadDocumentalECM/{idUnidadDocumental}")
    public MensajeRespuesta detallesUnidadDocumentalECM(@PathParam("idUnidadDocumental") String idUnidadDocumental) {
        logger.info("Ejecutando metodo MensajeRespuesta detallesUnidadDocumentalECM(String idUnidadDocumental)");
        try {
            return fEcmManager.detallesUnidadDocumental(idUnidadDocumental);
        } catch (Exception e) {
            logger.error("Error en operacion - detallesUnidadDocumentalECM ", e);
            MensajeRespuesta respuesta = new MensajeRespuesta();
            respuesta.setCodMensaje("11111");
            respuesta.setMensaje("Causa: " + e.getCause() + ", Mensaje: " + e.getMessage());
            return respuesta;
        }
    }

    /**
     * Metodo para devolver la Unidad Documental
     *
     * @param unidadDocumentalDTO     Obj Unidad Documental
     * @param documentoDTO            Documento a guardar
     * @return MensajeRespuesta       Unidad Documental
     */
    @POST
    @Path("/subirDocumentoUnidadDocumentalECM/")
    public MensajeRespuesta subirDocumentoUnidadDocumentalECM(@RequestBody UnidadDocumentalDTO unidadDocumentalDTO,
                                                              @RequestBody DocumentoDTO documentoDTO) {
        logger.info("Ejecutando metodo MensajeRespuesta subirDocumentoUnidadDocumentalECM(unidadDocumentalDTO, documentoDTO)");
        try {
            return fEcmManager.subirDocumentoUnidadDocumentalECM(unidadDocumentalDTO, documentoDTO);
        } catch (Exception e) {
            logger.error("Error en operacion - subirDocumentoUnidadDocumentalECM ", e);
            MensajeRespuesta respuesta = new MensajeRespuesta();
            respuesta.setCodMensaje("11111");
            respuesta.setMensaje("Causa: " + e.getCause() + ", Mensaje: " + e.getMessage());
            return respuesta;
        }
    }

    /**
     * Metodo para devolver la Unidad Documental
     *
     * @param unidadDocumentalDTO     Obj Unidad Documental
     * @param documentoDTOS           Lista de documentos a guardar
     * @return MensajeRespuesta       Unidad Documental
     */
    @POST
    @Path("/subirDocumentosUnidadDocumentalECM/")
    public MensajeRespuesta subirDocumentosUnidadDocumentalECM(@RequestBody UnidadDocumentalDTO unidadDocumentalDTO,
                                                               @RequestBody List<DocumentoDTO> documentoDTOS) {
        logger.info("Ejecutando metodo MensajeRespuesta subirDocumentoUnidadDocumentalECM(unidadDocumentalDTO, documentoDTOS)");
        try {
            return fEcmManager.subirDocumentosUnidadDocumental(unidadDocumentalDTO, documentoDTOS);
        } catch (Exception e) {
            logger.error("Error en operacion - subirDocumentosUnidadDocumentalECM ", e);
            MensajeRespuesta respuesta = new MensajeRespuesta();
            respuesta.setCodMensaje("11111");
            respuesta.setMensaje("Causa: " + e.getCause() + ", Mensaje: " + e.getMessage());
            return respuesta;
        }
    }
}

