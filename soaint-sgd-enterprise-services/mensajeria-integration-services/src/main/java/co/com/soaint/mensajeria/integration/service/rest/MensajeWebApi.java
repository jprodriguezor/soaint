package co.com.soaint.mensajeria.integration.service.rest;

import co.com.soaint.foundation.canonical.mensajeria.MensajeGenericoQueueDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import co.com.soaint.mensajeria.business.boundary.IGestionarMensaje;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 5-Mar-2018
 * Author: eric.rodriguez
 * Type: JAVA class Artifact
 * Purpose: SERVICE - rest services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Path("/mensaje-web-api")
@Produces({"application/json", "application/xml"})
@Consumes({"application/json", "application/xml"})
@Log4j2
@Api(value = "MensajeWebApi")
public class MensajeWebApi {

    //public static final String broker = SystemParameters.getParameter(SystemParameters.BROKER_IMPLEMENTATION);

    @Autowired
    @Qualifier("gestionarMensaje")
    IGestionarMensaje boundary;

    /**
     * Constructor
     */
    public MensajeWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     * @param mensajeGenericoQueueDTO
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @POST
    @Path("/mensaje/producir")
    public String producirMensaje(MensajeGenericoQueueDTO mensajeGenericoQueueDTO) throws SystemException {
        log.info("processing rest request - producir mensaje");
        return boundary.producirMensaje(mensajeGenericoQueueDTO);
    }
}
