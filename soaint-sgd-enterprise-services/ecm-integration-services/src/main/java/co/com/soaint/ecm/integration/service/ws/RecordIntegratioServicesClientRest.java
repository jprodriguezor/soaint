package co.com.soaint.ecm.integration.service.ws;

import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.IRecordServices;
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
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
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
    public static final String COD_MENSAJE = "11111";

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
            respuesta.setCodMensaje(COD_MENSAJE);
            respuesta.setMensaje(e.getMessage());
            throw e;
        }
    }

    /**
     * Metodo para cerrar una unidad documental
     *
     * @param unidadDocumentalDTO  Obj Unidad Documental
     * @return MensajeRespuesta
     */
    @PUT
    @Path("/abrirCerrarReactivarUnidadDocumentalECM/")
    public MensajeRespuesta gestionarUnidadDocumentalECM(@RequestBody UnidadDocumentalDTO unidadDocumentalDTO) {
        logger.info("Ejecutando metodo MensajeRespuesta gestionarUnidadDocumentalECM(UnidadDocumentalDTO idUnidadDocumental)");
        try {
            return record.gestionarUnidadDocumentalECM(unidadDocumentalDTO);
        } catch (Exception e) {
            logger.error("Error en operacion - cerrarUnidadDocumentalECM ", e);
            MensajeRespuesta respuesta = new MensajeRespuesta();
            respuesta.setCodMensaje(COD_MENSAJE);
            respuesta.setMensaje(e.getMessage());
            return respuesta;
        }
    }

    /**
     * Metodo para cerrar una o varias unidades documentales
     *
     * @param unidadDocumentalDTOS   Lista Unidades Documentales para cerrar
     * @return MensajeRespuesta
     */
    @PUT
    @Path("/abrirCerrarReactivarUnidadesDocumentalesECM/")
    public MensajeRespuesta gestionarUnidadesDocumentalesECM(@RequestBody List<UnidadDocumentalDTO> unidadDocumentalDTOS) {
        logger.info("Ejecutando metodo MensajeRespuesta cerrarUnidadesDocumentalesECM(List<UnidadDocumentalDTO> unidadDocumentalDTOS)");
        try {
            return record.gestionarUnidadesDocumentalesECM(unidadDocumentalDTOS);
        } catch (Exception e) {
            logger.error("Error en operacion - cerrarUnidadesDocumentalesECM ", e);
            MensajeRespuesta respuesta = new MensajeRespuesta();
            respuesta.setCodMensaje(COD_MENSAJE);
            respuesta.setMensaje("Causa: " + e.getCause() + ", Mensaje: " + e.getMessage());
            return respuesta;
        }
    }
}
