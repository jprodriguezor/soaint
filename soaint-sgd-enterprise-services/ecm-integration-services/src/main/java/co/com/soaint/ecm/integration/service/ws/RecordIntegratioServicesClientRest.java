package co.com.soaint.ecm.integration.service.ws;

import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.IRecordServices;
import co.com.soaint.ecm.util.ConstantesECM;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.canonical.ecm.UnidadDocumentalDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
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

@Log4j2
@Service
@Path("/record")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecordIntegratioServicesClientRest {
    
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
        log.info("processing rest request - Crear Estructura Record");
        try {
            return record.crearEstructuraRecord(structure);
        } catch (RuntimeException e) {
            log.error("Error servicio creando estructura ", e);
            throw e;
        }
    }

    /**
     * Crear carpeta en el Record
     *
     * @param idUnidadDocumental id de la UD a crear en el record
     * @return Mensaje de respuesta
     */
    @POST
    @Path("/crearCarpetaRecord/")
    public MensajeRespuesta crearCarpetaRecord(String idUnidadDocumental) throws SystemException {
        log.info("processing rest request - Crear carpeta Record");
        try {
            return record.crearCarpetaRecord(idUnidadDocumental);
        } catch (RuntimeException e) {
            log.error("Error en operacion - crearCarpetaRecord ", e);
            MensajeRespuesta respuesta = new MensajeRespuesta();
            respuesta.setCodMensaje(ConstantesECM.ERROR_COD_MENSAJE);
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
        log.info("Ejecutando metodo MensajeRespuesta gestionarUnidadDocumentalECM(UnidadDocumentalDTO idUnidadDocumental)");
        try {
            return record.gestionarUnidadDocumentalECM(unidadDocumentalDTO);
        } catch (Exception e) {
            log.error("Error en operacion - cerrarUnidadDocumentalECM ", e);
            MensajeRespuesta respuesta = new MensajeRespuesta();
            respuesta.setCodMensaje(ConstantesECM.ERROR_COD_MENSAJE);
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
        log.info("Ejecutando metodo MensajeRespuesta cerrarUnidadesDocumentalesECM(List<UnidadDocumentalDTO> unidadDocumentalDTOS)");
        try {
            return record.gestionarUnidadesDocumentalesECM(unidadDocumentalDTOS);
        } catch (Exception e) {
            log.error("Error en operacion - cerrarUnidadesDocumentalesECM ", e);
            MensajeRespuesta respuesta = new MensajeRespuesta();
            respuesta.setCodMensaje(ConstantesECM.ERROR_COD_MENSAJE);
            respuesta.setMensaje("Causa: " + e.getCause() + ", Mensaje: " + e.getMessage());
            return respuesta;
        }
    }

    /**
     * Operacion para devolver Las unidades documentales a Transferir
     *
     * @param tipoTransferencia Tipo de transferencia a realizar (Primaria o Secundaria)
     * @return MensajeRespuesta
     */
    @GET
    @Path("/obtenerUnidadesDocumentalesATransferir/{tipoTransferencia}")
    public MensajeRespuesta obtenerUnidadesDocumentalesATransferirECM(@PathParam("tipoTransferencia") final String tipoTransferencia) {
        log.info("processing rest request - Obtener Unidades Documentales a Transferir ECM");
        try {
            return record.obtenerUnidadesDocumentalesATransferirECM(tipoTransferencia);
        } catch (Exception e) {
            log.error("Error en operacion - obtenerUnidadesDocumentalesATransferirECM ", e);
            MensajeRespuesta respuesta = new MensajeRespuesta();
            respuesta.setCodMensaje(ConstantesECM.ERROR_COD_MENSAJE);
            respuesta.setMensaje(e.getMessage());
            throw e;
        }
    }

    /**
     * Metodo para cerrar una o varias unidades documentales
     *
     * @param unidadDocumentalDTOS   Lista Unidades Documentales para aprobar/rechazar
     * @return MensajeRespuesta
     */
    @PUT
    @Path("/aprobar-rechazar-transferencias/")
    public MensajeRespuesta aprobarRechazarTransferenciasDocumentalesECM(@RequestBody List<UnidadDocumentalDTO> unidadDocumentalDTOS) {
        log.info("Ejecutando metodo MensajeRespuesta aprobarRechazarTransferenciasDocumentalesECM(List<UnidadDocumentalDTO> unidadDocumentalDTOS)");
        try {
            return record.aprobarRechazarTransferenciasDocumentales(unidadDocumentalDTOS);
        } catch (Exception e) {
            log.error("Error en operacion - aprobarRechazarTransferenciasDocumentalesECM ", e);
            MensajeRespuesta respuesta = new MensajeRespuesta();
            respuesta.setCodMensaje(ConstantesECM.ERROR_COD_MENSAJE);
            respuesta.setMensaje(e.getMessage());
            throw e;
        }
    }
}
