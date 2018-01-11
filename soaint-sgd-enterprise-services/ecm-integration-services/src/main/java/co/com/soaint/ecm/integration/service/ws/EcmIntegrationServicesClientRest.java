package co.com.soaint.ecm.integration.service.ws;

import co.com.soaint.ecm.business.boundary.mediator.EcmManager;
import co.com.soaint.foundation.canonical.correspondencia.MetadatosDocumentosDTO;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @param structure Estrcutura a crear
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
        } catch (RuntimeException e) {
            logger.error("Error en operacion - Subir Documento ECM ", e);
            throw e;

        } catch (IOException e) {
            logger.error("Error en operacion - Subir Documento ECM ", e);
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
        logger.info("processing rest request - Subir Documento ECM " + metadatos.getNombreDocumento());
        try {
            return fEcmManager.modificarMetadatosDocumento(metadatos);
        } catch (RuntimeException e) {
            logger.error("Error en operacion - Modificar Metadatos Documento ECM ", e);
            throw e;

        } catch (IOException e) {
            logger.error("Error en operacion - Modificar Metadatos Documento ECM ", e);
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
            return fEcmManager.descargarDocumento(identificadorDoc);
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
    @Path("/eliminarDocumentoECM/")
    public boolean eliminarDocumentoECM(@QueryParam("idDocumento") final String idDocumento) {

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

