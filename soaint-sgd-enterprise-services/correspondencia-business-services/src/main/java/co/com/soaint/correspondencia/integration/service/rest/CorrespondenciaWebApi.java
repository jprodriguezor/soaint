package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarCorrespondencia;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private static Logger logger = LogManager.getLogger(CorrespondenciaWebApi.class.getName());

    @Autowired
    private GestionarCorrespondencia boundary;

    public CorrespondenciaWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @POST
    @Path("/correspondencia")
    public ComunicacionOficialDTO radicarCorrespondencia(ComunicacionOficialDTO comunicacionOficialDTO) throws BusinessException, SystemException {
        logger.info("processing rest request - radicar correspondencia");
        return boundary.radicarCorrespondencia(comunicacionOficialDTO);
    }

    @GET
    @Path("/correspondencia/{nro_radicado}")
    public ComunicacionOficialDTO listarCorrespondenciaByNroRadicado(@PathParam("nro_radicado") final String nroRadicado) throws BusinessException, SystemException {
        logger.info("processing rest request - listar correspondencia by nro radicado");
        return boundary.listarCorrespondenciaByNroRadicado(nroRadicado);
    }

    @PUT
    @Path("/correspondencia/actualizar-estado")
    public void actualizarEstadoCorrespondencia(CorrespondenciaDTO correspondenciaDTO) throws BusinessException, SystemException {
        logger.info("processing rest request - actualizar estado correspondencia");
        boundary.actualizarEstadoCorrespondencia(correspondenciaDTO);
    }

    @PUT
    @Path("/correspondencia/actualizar-ide-instancia")
    public void actualizarIdeInstancia(CorrespondenciaDTO correspondenciaDTO) throws BusinessException, SystemException {
        logger.info("processing rest request - actualizar ide instancia");
        boundary.actualizarIdeInstancia(correspondenciaDTO);
    }

    @GET
    @Path("/correspondencia")
    public ComunicacionesOficialesDTO listarCorrespondenciaByPeriodoAndCodDependenciaAndCodEstadoAndNroRadicado(@QueryParam("fecha_ini") final String fechaIni,
                                                                                                  @QueryParam("fecha_fin") final String fechaFin,
                                                                                                  @QueryParam("cod_dependencia") final String codDependencia,
                                                                                                  @QueryParam("cod_estado") final String codEstado,
                                                                                                  @QueryParam("nro_radicado") final String nroRadicado) throws BusinessException, SystemException {
        logger.info("processing rest request - listar correspondencia by periodo and cod_dependencia and cod_estado");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date fechaInicial = dateFormat.parse(fechaIni);
            Date fechaFinal = dateFormat.parse(fechaFin);
            return boundary.listarCorrespondenciaByPeriodoAndCodDependenciaAndCodEstadoAndNroRadicado(fechaInicial, fechaFinal, codDependencia, codEstado, nroRadicado);
        }
        catch (ParseException ex){
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }
}
