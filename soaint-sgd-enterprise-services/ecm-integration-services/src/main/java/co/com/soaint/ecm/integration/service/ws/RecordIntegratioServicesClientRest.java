package co.com.soaint.ecm.integration.service.ws;

import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.IRecordServices;
import co.com.soaint.ecm.domain.entity.AccionUsuario;
import co.com.soaint.foundation.canonical.ecm.EntradaRecordDTO;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.canonical.ecm.UnidadDocumentalDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by amartinez on 24/01/2018.
 */
@Path("/record")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Service
public class RecordIntegratioServicesClientRest {

    private static final Logger logger = LogManager.getLogger(EcmIntegrationServicesClientRest.class.getName());

    @Autowired
    private IRecordServices record;

    /**
     * Constructor de la clase
     */

    public RecordIntegratioServicesClientRest() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     * Crear estructura en el Record
     *
     * @param structure Estructura a crear
     * @return Mensaje de respuesta
     */
    @POST
    @Path("/crearEstructuraRecord/")
    public MensajeRespuesta crearEstructuraContent(List<EstructuraTrdDTO> structure) throws SystemException {
        logger.info("processing rest request - Crear Estructura Record");
        try {
            return record.crearEstructuraRecord(structure);
        } catch (RuntimeException e) {
            logger.error("Error servicio creando estructura ", e);
            throw e;
        }
    }

    /**
     * Crear carpeta en el Record
     *
     * @param entrada carpeta a crear
     * @return Mensaje de respuesta
     */
    @POST
    @Path("/crearCarpetaRecord/")
    public MensajeRespuesta crearCarpetaRecord(EntradaRecordDTO entrada) throws SystemException {
        logger.info("processing rest request - Crear carpeta Record");
        try {
            return record.crearCarpetaRecord(entrada);
        } catch (RuntimeException e) {
            logger.error("Error en operacion - crearCarpetaRecord ", e);
            MensajeRespuesta respuesta = new MensajeRespuesta();
            respuesta.setCodMensaje("11111");
            respuesta.setMensaje("Causa: " + e.getCause() + ", Mensaje: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Metodo para cerrar una unidad documental
     *
     * @param idUnidadDocumental     Id Unidad Documental
     * @return MensajeRespuesta
     */
    @POST
    @Path("/cerrarUnidadDocumentalECM/")
    public MensajeRespuesta cerrarUnidadDocumentalECM(@RequestParam("id") String idUnidadDocumental) {
        logger.info("Ejecutando metodo MensajeRespuesta cerrarUnidadDocumentalECM(String idUnidadDocumental)");
        try {
            return record.abrirCerrarReactivarUnidadDocumental(idUnidadDocumental, AccionUsuario.CERRAR);
        } catch (Exception e) {
            logger.error("Error en operacion - cerrarUnidadDocumentalECM ", e);
            MensajeRespuesta respuesta = new MensajeRespuesta();
            respuesta.setCodMensaje("11111");
            respuesta.setMensaje("Causa: " + e.getCause() + ", Mensaje: " + e.getMessage());
            return respuesta;
        }
    }

    /**
     * Metodo para abrir una unidad documental
     *
     * @param idUnidadDocumental     Id Unidad Documental
     * @return MensajeRespuesta
     */
    @POST
    @Path("/abrirUnidadDocumentalECM/")
    public MensajeRespuesta abrirUnidadDocumentalECM(@RequestParam("id") String idUnidadDocumental) {
        logger.info("Ejecutando metodo MensajeRespuesta abrirUnidadDocumentalECM(String idUnidadDocumental)");
        try {
            return record.abrirCerrarReactivarUnidadDocumental(idUnidadDocumental, AccionUsuario.ABRIR);
        } catch (Exception e) {
            logger.error("Error en operacion - abrirUnidadDocumentalECM ", e);
            MensajeRespuesta respuesta = new MensajeRespuesta();
            respuesta.setCodMensaje("11111");
            respuesta.setMensaje("Causa: " + e.getCause() + ", Mensaje: " + e.getMessage());
            return respuesta;
        }
    }

    /**
     * Metodo para reactivar una unidad documental
     *
     * @param idUnidadDocumental     Id Unidad Documental
     * @return MensajeRespuesta
     */
    @POST
    @Path("/reactivarUnidadDocumentalECM/")
    public MensajeRespuesta reactivarUnidadDocumentalECM(@RequestParam("id") String idUnidadDocumental) {
        logger.info("Ejecutando metodo MensajeRespuesta reactivarUnidadDocumentalECM(String idUnidadDocumental)");
        try {
            return record.abrirCerrarReactivarUnidadDocumental(idUnidadDocumental, AccionUsuario.REACTIVAR);
        } catch (Exception e) {
            logger.error("Error en operacion - reactivarUnidadDocumentalECM ", e);
            MensajeRespuesta respuesta = new MensajeRespuesta();
            respuesta.setCodMensaje("11111");
            respuesta.setMensaje("Causa: " + e.getCause() + ", Mensaje: " + e.getMessage());
            return respuesta;
        }
    }

    /**
     * Metodo para cerrar una o varias unidades documentales
     *
     * @param unidadDocumentalDTOS   Lista Unidades Documentales para cerrar
     * @return MensajeRespuesta
     */
    @POST
    @Path("/cerrarUnidadesDocumentalesECM/")
    public MensajeRespuesta cerrarUnidadesDocumentalesECM(@RequestBody List<UnidadDocumentalDTO> unidadDocumentalDTOS) {
        logger.info("Ejecutando metodo MensajeRespuesta cerrarUnidadesDocumentalesECM(List<UnidadDocumentalDTO> unidadDocumentalDTOS)");
        try {
            return record.abrirCerrarReactivarUnidadesDocumentales(unidadDocumentalDTOS, AccionUsuario.CERRAR);
        } catch (Exception e) {
            logger.error("Error en operacion - cerrarUnidadesDocumentalesECM ", e);
            MensajeRespuesta respuesta = new MensajeRespuesta();
            respuesta.setCodMensaje("11111");
            respuesta.setMensaje("Causa: " + e.getCause() + ", Mensaje: " + e.getMessage());
            return respuesta;
        }
    }

    /**
     * Metodo para abrir una o varias unidades documentales
     *
     * @param unidadDocumentalDTOS   Lista Unidades Documentales para abrir
     * @return MensajeRespuesta
     */
    @POST
    @Path("/abrirUnidadesDocumentalesECM/")
    public MensajeRespuesta abrirUnidadesDocumentalesECM(@RequestBody List<UnidadDocumentalDTO> unidadDocumentalDTOS) {
        logger.info("Ejecutando metodo MensajeRespuesta abrirUnidadesDocumentalesECM(List<UnidadDocumentalDTO> unidadDocumentalDTOS)");
        try {
            return record.abrirCerrarReactivarUnidadesDocumentales(unidadDocumentalDTOS, AccionUsuario.ABRIR);
        } catch (Exception e) {
            logger.error("Error en operacion - abrirUnidadesDocumentalesECM ", e);
            MensajeRespuesta respuesta = new MensajeRespuesta();
            respuesta.setCodMensaje("11111");
            respuesta.setMensaje("Causa: " + e.getCause() + ", Mensaje: " + e.getMessage());
            return respuesta;
        }
    }

    /**
     * Metodo para reactivar una o varias unidades documentales
     *
     * @param unidadDocumentalDTOS   Lista Unidades Documentales para abrir
     * @return MensajeRespuesta
     */
    @POST
    @Path("/reactivarUnidadesdDocumentalesECM/")
    public MensajeRespuesta reactivarUnidadesdDocumentalesECM(@RequestBody List<UnidadDocumentalDTO> unidadDocumentalDTOS) {
        logger.info("Ejecutando metodo MensajeRespuesta reactivarUnidaesdDocumentalesECM(List<UnidadDocumentalDTO> unidadDocumentalDTOS)");
        try {
            return record.abrirCerrarReactivarUnidadesDocumentales(unidadDocumentalDTOS, AccionUsuario.REACTIVAR);
        } catch (Exception e) {
            logger.error("Error en operacion - reactivarUnidaesdDocumentalesECM ", e);
            MensajeRespuesta respuesta = new MensajeRespuesta();
            respuesta.setCodMensaje("11111");
            respuesta.setMensaje("Causa: " + e.getCause() + ", Mensaje: " + e.getMessage());
            return respuesta;
        }
    }
}
