package co.com.soaint.ecm.integration.service.ws;

import co.com.soaint.ecm.business.boundary.mediator.interfaces.EcmManagerMediator;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.InfrastructureException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

/**
 * Created by Dasiel on 19/06/2017.
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
    EcmManagerMediator fEcmManager;

    @POST
    @Path("/crearEstructuraContent/")
    public MensajeRespuesta crearEstructuraContent(List<EstructuraTrdDTO> structure) throws SystemException, BusinessException, IOException {
        LOGGER.info("processing rest request - Crear Estructura ECM");
        try {
            return fEcmManager.crearEstructuraECM (structure);
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }

    @POST
    @Path("/subirDocumentoECM/")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String subirDocumentoECM(@QueryParam("nombreDocumento") final String nombreDocumento,
                                    @QueryParam("documento") final MultipaportInput documento,
                                    @QueryParam("tipoComunicacion") final String tipoComunicacion) throws InfrastructureException, SystemException {

        LOGGER.info("processing rest request - Subir Documento ECM");
        try {
            return fEcmManager.subirDocumento (nombreDocumento,documento,tipoComunicacion );
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }

    @POST
    @Path("/moverDocumentoECM/")
    public MensajeRespuesta moverDocumentoECM(@QueryParam ("moverDocumento") final String moverDocumento,
                                              @QueryParam ("carpetaFuente") final String carpetaFuente,
                                              @QueryParam ("carpetaDestino") final String carpetaDestino) throws InfrastructureException, SystemException {

        LOGGER.info("processing rest request - Subir Documento ECM");
        try {
            return fEcmManager.moverDocumento (moverDocumento,carpetaFuente,carpetaDestino );
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }


}

