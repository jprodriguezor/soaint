package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarCorrespondencia;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 24-May-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: SERVICE - rest services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@Path("/correspondencia-web-api")
@Produces({"application/json", "application/xml"})
@Consumes({"application/json", "application/xml"})
public class CorrespondenciaWebApi {
    private static Logger LOGGER = LogManager.getLogger(CorrespondenciaWebApi.class.getName());

    @Autowired
    private GestionarCorrespondencia boundary;

    public CorrespondenciaWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @POST
    @Path("/correspondencia")
    public ComunicacionOficialDTO radicarCorrespondencia(ComunicacionOficialDTO comunicacionOficialDTO) throws BusinessException, SystemException {
        LOGGER.info("processing rest request - radicar correspondencia");
        return boundary.radicarCorrespondencia(comunicacionOficialDTO);
    }

    @GET
    @Path("/correspondencia/{nro_radicado}")
    public ComunicacionOficialDTO listarCorrespondenciaByNroRadicado(@PathParam("nro_radicado") final String nroRadicado) throws BusinessException, SystemException {
        LOGGER.info("processing rest request - radicar correspondencia");
        return boundary.listarCorrespondenciaByNroRadicado(nroRadicado);
    }

    @PUT
    @Path("/correspondencia")
    public void actualizarEstadoCorrespondencia(CorrespondenciaDTO correspondenciaDTO) throws BusinessException, SystemException {
        LOGGER.info("processing rest request - actualizar estado correspondencia");
        boundary.actualizarEstadoCorrespondencia(correspondenciaDTO);
    }

}
