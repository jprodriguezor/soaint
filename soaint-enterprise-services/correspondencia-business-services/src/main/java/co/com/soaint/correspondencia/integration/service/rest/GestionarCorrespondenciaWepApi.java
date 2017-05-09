package co.com.soaint.correspondencia.integration.service.rest;

/**
 * Created by jrodriguez on 09/05/2017.
 */

import co.com.soaint.correspondencia.business.boundary.GestionarCorrespondencia;
import co.com.soaint.foundation.canonical.correspondencia.CorrespondenciaDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created: 4-May-2017
 * Author: jrodriguez
 * Type: JAVA class Artifact
 * Purpose: REST Integration Service
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Path("/correspondencia-web-api")
public class GestionarCorrespondenciaWepApi {

    private static Logger LOGGER = LogManager.getLogger(GestionarCorrespondenciaWepApi.class.getName());

    @Autowired
    private GestionarCorrespondencia boundary;

    // [constructor] ---------------------------

    public GestionarCorrespondenciaWepApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    // [service] -----------------------------

    @POST
    @Path("/")
    @Consumes({MediaType.APPLICATION_XML})
    public void crearCorrespondencia(final CorrespondenciaDTO correspondencia) throws SystemException, BusinessException {
        LOGGER.info("processing rest request - crear correspondencia : {}", correspondencia.toString());
        boundary.crearCorrespondencia(correspondencia);
    }


}
