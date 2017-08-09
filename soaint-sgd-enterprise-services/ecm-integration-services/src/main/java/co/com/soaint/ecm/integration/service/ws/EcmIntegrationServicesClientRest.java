package co.com.soaint.ecm.integration.service.ws;

import co.com.soaint.ecm.business.boundary.mediator.interfaces.EcmManagerMediator;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.InfrastructureException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

/**
 * Created by Dasiel
 */


@Path("/ecm")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EcmIntegrationServicesClientRest {

    private static Logger LOGGER = LogManager.getLogger(EcmIntegrationServicesClientRest.class.getName());

    public EcmIntegrationServicesClientRest(){
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Autowired
    private
    EcmManagerMediator fEcmManager;

    @POST
    @Path("/crearEstructuraContent/")
    public MensajeRespuesta crearEstructuraContent(List<EstructuraTrdDTO> structure) throws SystemException, BusinessException, IOException {
       // LOGGER.info("processing rest request - Crear Estructura ECM");
        try {
            return fEcmManager.crearEstructuraECM (structure);
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }

    @POST
    @Path("/subirDocumentoECM/{nombreDocumento}/{tipoComunicacion}")
    @Consumes("multipart/form-data")
    public String subirDocumentoECM(@PathParam ("nombreDocumento")  String nombreDocumento,
                                    @RequestPart("documento") final MultipartFormDataInput documento,
                                    @PathParam ("tipoComunicacion")  String tipoComunicacion) throws InfrastructureException, SystemException {
        LOGGER.info("processing rest request - Subir Documento ECM "+ nombreDocumento+ " "+tipoComunicacion );
        try {

            return fEcmManager.subirDocumento (nombreDocumento, documento, tipoComunicacion);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("Error en operacion - Subir Documento ECM");
            throw e;

        }

    }

    @POST
    @Path("/moverDocumentoECM/")
    public MensajeRespuesta moverDocumentoECM(@QueryParam ("moverDocumento") final String moverDocumento,
                                              @QueryParam ("carpetaFuente") final String carpetaFuente,
                                              @QueryParam ("carpetaDestino") final String carpetaDestino) throws InfrastructureException, SystemException {

        LOGGER.info("processing rest request - Mover Documento ECM");
        try {
            return fEcmManager.moverDocumento (moverDocumento,carpetaFuente,carpetaDestino );
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }

}

