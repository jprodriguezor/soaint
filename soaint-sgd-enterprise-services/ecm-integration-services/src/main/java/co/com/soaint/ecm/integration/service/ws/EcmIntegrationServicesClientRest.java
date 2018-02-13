package co.com.soaint.ecm.integration.service.ws;

import co.com.soaint.ecm.business.boundary.mediator.EcmManager;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.canonical.ecm.MetadatosDocumentosDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
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
     * Subir documento a ECM
     *
     * @param nombreDocumento  nombre documento
     * @param documento        documento a subir
     * @param tipoComunicacion tipo de documento
     * @return identificador del documento en el ecm
     */
    @POST
    @Path("/subirDocumentoECM/{nombreDocumento}/{tipoComunicacion}")
    @Consumes("multipart/form-data")
    public MensajeRespuesta subirDocumentoECM(@PathParam("nombreDocumento") String nombreDocumento,
                                              @RequestPart("documento") final MultipartFormDataInput documento,
                                              @PathParam("tipoComunicacion") String tipoComunicacion) throws IOException {
        logger.info("processing rest request - Subir Documento ECM " + nombreDocumento + " " + tipoComunicacion);
        try {
            return fEcmManager.subirDocumento(nombreDocumento, documento, tipoComunicacion);
        } catch (IOException e) {
            logger.error("Error en operacion - Subir Documento ECM ", e);
            throw e;
        }

    }

    /**
     * Subir documento Principal al ECM
     *
     * @param documento       documento a subir
     * @param nombreDocumento Nombre del documento
     * @param sede            Sede a la que pertenece el documento
     * @param dependencia     Dependencia a la que pertenece el documento
     * @param selector        Selector que dice donde se va a gauardar el documento
     * @return identificador del documento en el ecm
     */
    @POST
    @Path("/subirDocumentoRelacionECM/{nombreDocumento}/{sede}/{dependencia}/{selector}")
    @Consumes("multipart/form-data")
    public MensajeRespuesta subirDocumentoPrincipalAdjuntoECM(@RequestPart("documento") final MultipartFormDataInput documento,
                                                              @PathParam("nombreDocumento") String nombreDocumento,
                                                              @PathParam("sede") String sede,
                                                              @PathParam("dependencia") String dependencia,
                                                              @PathParam("selector") String selector
    ) throws IOException {

        logger.info("processing rest request - Subir Documento Adjunto al ECM " + nombreDocumento);
        try {
            MetadatosDocumentosDTO metadatosDocumentosDTO = new MetadatosDocumentosDTO();
            metadatosDocumentosDTO.setNombreDocumento(nombreDocumento);
            metadatosDocumentosDTO.setSede(sede);
            metadatosDocumentosDTO.setDependencia(dependencia);

            return fEcmManager.subirDocumentoPrincipalAdjunto(documento, metadatosDocumentosDTO, selector);
        } catch (IOException e) {
            logger.error("Error en operacion - Subir Documento Adjunto ECM ", e);
            throw e;
        }

    }

    /**
     * Subir documento Adjunto a ECM
     *
     * @param documento       documento a subir
     * @param nombreDocumento Nombre del documento
     * @param idDocPadre      Identificador del documento Principal al que esta asociado
     * @param sede            Sede a la que pertenece el documento
     * @param dependencia     Dependencia a la que pertenece el documento
     * @param selector        Selector que dice donde se va a gauardar el documento
     * @return identificador del documento en el ecm
     */
    @POST
    @Path("/subirDocumentoRelacionECM/{nombreDocumento}/{sede}/{dependencia}/{selector}/{idDocPadre}")
    @Consumes("multipart/form-data")
    public MensajeRespuesta subirDocumentoPrincipalAdjuntoECM(@RequestPart("documento") final MultipartFormDataInput documento,
                                                              @PathParam("nombreDocumento") String nombreDocumento,
                                                              @PathParam("sede") String sede,
                                                              @PathParam("dependencia") String dependencia,
                                                              @PathParam("idDocPadre") String idDocPadre,
                                                              @PathParam("selector") String selector) throws IOException {

        logger.info("processing rest request - Subir Documento Adjunto al ECM " + nombreDocumento);
        try {
            MetadatosDocumentosDTO metadatosDocumentosDTO = new MetadatosDocumentosDTO();
            metadatosDocumentosDTO.setNombreDocumento(nombreDocumento);
            metadatosDocumentosDTO.setIdDocumentoPadre(idDocPadre);
            metadatosDocumentosDTO.setSede(sede);
            metadatosDocumentosDTO.setDependencia(dependencia);
            metadatosDocumentosDTO.setIdDocumentoPadre(idDocPadre);

            return fEcmManager.subirDocumentoPrincipalAdjunto(documento, metadatosDocumentosDTO,selector);
        } catch (IOException e) {
            logger.error("Error en operacion - Subir Documento Adjunto ECM ", e);
            throw e;
        }

    }

    /**
     * Subir versionar documento Generado al ECM
     *
     * @param documento       documento a subir
     * @param nombreDocumento Nombre del documento
     * @param sede            Sede a la que pertenece el documento
     * @param selector        parametro que indica donde se va a guardar el documento
     * @param dependencia     Dependencia a la que pertenece el documento
     * @return identificador del documento en el ecm
     */
    @POST
    @Path("/subirVersionarDocumentoGeneradoECM/{nombreDocumento}/{sede}/{dependencia}/{tipoDocumento}/{selector}")
    @Consumes("multipart/form-data")
    public MensajeRespuesta subirVersionarDocumentoGeneradoECM(@RequestPart("documento") final MultipartFormDataInput documento,
                                                               @PathParam("nombreDocumento") String nombreDocumento,
                                                               @PathParam("sede") String sede,
                                                               @PathParam("dependencia") String dependencia,
                                                               @PathParam("tipoDocumento") String tipoDocumento,
                                                               @PathParam("selector") String selector
    ) throws IOException {

        logger.info("processing rest request - Subir Versionar Documento Generado al ECM " + nombreDocumento);
        try {
            MetadatosDocumentosDTO metadatosDocumentosDTO = new MetadatosDocumentosDTO();
            metadatosDocumentosDTO.setNombreDocumento(nombreDocumento);
            metadatosDocumentosDTO.setSede(sede);
            metadatosDocumentosDTO.setDependencia(dependencia);
            metadatosDocumentosDTO.setTipoDocumento(tipoDocumento);

            return fEcmManager.subirVersionarDocumentoGenerado(documento, metadatosDocumentosDTO, selector);
        } catch (IOException e) {
            logger.error("Error en operacion - Subir Versionar Documento Generado al ECM ", e);
            throw e;
        }

    }

    /**
     * Subir versionar documento Generado al ECM
     *
     * @param documento       documento a subir
     * @param nombreDocumento Nombre del documento
     * @param sede            Sede a la que pertenece el documento
     * @param dependencia     Dependencia a la que pertenece el documento
     * @param selector        parametro que indica donde se va a guardar el documento
     * @return identificador del documento en el ecm
     */
    @POST
    @Path("/subirVersionarDocumentoGeneradoECM/{nombreDocumento}/{sede}/{dependencia}/{tipoDocumento}/{idDoc}/{selector}")
    @Consumes("multipart/form-data")
    public MensajeRespuesta subirVersionarDocumentoGeneradoECM(@RequestPart("documento") final MultipartFormDataInput documento,
                                                               @PathParam("nombreDocumento") String nombreDocumento,
                                                               @PathParam("sede") String sede,
                                                               @PathParam("dependencia") String dependencia,
                                                               @PathParam("tipoDocumento") String tipoDocumento,
                                                               @PathParam("idDoc") String idDoc,
                                                               @PathParam("selector") String selector
    ) throws IOException {

        logger.info("processing rest request - Subir Versionar Documento Generado al ECM " + nombreDocumento);
        try {
            MetadatosDocumentosDTO metadatosDocumentosDTO = new MetadatosDocumentosDTO();
            metadatosDocumentosDTO.setNombreDocumento(nombreDocumento);
            metadatosDocumentosDTO.setSede(sede);
            metadatosDocumentosDTO.setDependencia(dependencia);
            metadatosDocumentosDTO.setTipoDocumento(tipoDocumento);
            metadatosDocumentosDTO.setIdDocumento(idDoc);

            return fEcmManager.subirVersionarDocumentoGenerado(documento, metadatosDocumentosDTO, selector);
        } catch (IOException e) {
            logger.error("Error en operacion - Subir Versionar Documento Generado al ECM ", e);
            throw e;
        }

    }

    /**
     * Obtener id documentos adjuntos dado id documento principal
     *
     * @param idDocPadre Identificador del documento Principal al que esta asociado
     * @return Lista de documentos adjuntos ECM
     */
    @POST
    @Path("/obtenerDocumentosAdjuntosECM/{idDocPadre}")
    public MensajeRespuesta obtenerDocumentoPrincipalAdjunto(@PathParam("idDocPadre") String idDocPadre) throws IOException {

        logger.info("processing rest request - Buscar Documento Adjunto en el ECM dado id: " + idDocPadre);
        try {
            return fEcmManager.obtenerDocumentosAdjuntos(idDocPadre);
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
    public MensajeRespuesta modificarMetadatosDocumentoECM(@RequestBody MetadatosDocumentosDTO metadatos) throws IOException {
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
    public MensajeRespuesta modificarDocumentoECM(@RequestBody MetadatosDocumentosDTO metadatos) throws IOException {
        logger.info("processing rest request - Modificar Documento ECM " + metadatos.getIdDocumento());
        try {
            return fEcmManager.modificarDocumento(metadatos);
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
            MetadatosDocumentosDTO metadatosDocumentosDTO = new MetadatosDocumentosDTO();
            metadatosDocumentosDTO.setIdDocumento(identificadorDoc);
            return fEcmManager.descargarDocumento(metadatosDocumentosDTO);
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
            MetadatosDocumentosDTO metadatosDocumentosDTO = new MetadatosDocumentosDTO();
            metadatosDocumentosDTO.setIdDocumento(identificadorDoc);
            metadatosDocumentosDTO.setVersionLabel(version);
            return fEcmManager.descargarDocumento(metadatosDocumentosDTO);
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
    @POST
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

}

