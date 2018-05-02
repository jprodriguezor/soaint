package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarCorrespondencia;
import co.com.soaint.foundation.canonical.correspondencia.ComunicacionOficialDTO;
import co.com.soaint.foundation.canonical.correspondencia.ComunicacionOficialFullDTO;
import co.com.soaint.foundation.canonical.correspondencia.ComunicacionesOficialesDTO;
import co.com.soaint.foundation.canonical.correspondencia.CorrespondenciaDTO;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
@Api(value = "CorrespondenciaWebApi", description = "")
public class CorrespondenciaWebApi {

    @Autowired
    private GestionarCorrespondencia boundary;

    /**
     * Constructor
     */
    public CorrespondenciaWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     * @param comunicacionOficialDTO
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @POST
    @Path("/correspondencia")
    public ComunicacionOficialDTO radicarCorrespondencia(ComunicacionOficialDTO comunicacionOficialDTO) throws BusinessException, SystemException {
        log.info("processing rest request - radicar correspondencia");
        return boundary.radicarCorrespondencia(comunicacionOficialDTO);
    }

    /**
     * @param nroRadicado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @GET
    @Path("/correspondencia/{nro_radicado}")
    public ComunicacionOficialDTO listarCorrespondenciaByNroRadicado(@PathParam("nro_radicado") final String nroRadicado) throws BusinessException, SystemException {
        log.info("processing rest request - listar correspondencia by nro radicado");
        return boundary.listarCorrespondenciaByNroRadicado(nroRadicado);
    }

    /**
     * @param nroRadicado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @GET
    @Path("/correspondencia/full/{nro_radicado}")
    public ComunicacionOficialFullDTO listarFullCorrespondenciaByNroRadicado(@PathParam("nro_radicado") final String nroRadicado) throws BusinessException, SystemException {
        log.info("processing rest request - listar full correspondencia by nro radicado");
        return boundary.listarFullCorrespondenciaByNroRadicado(nroRadicado);
    }

    /**
     * @param correspondenciaDTO
     * @throws BusinessException
     * @throws SystemException
     */
    @PUT
    @Path("/correspondencia/actualizar-estado")
    public void actualizarEstadoCorrespondencia(CorrespondenciaDTO correspondenciaDTO) throws BusinessException, SystemException {
        log.info("processing rest request - actualizar estado correspondencia");
        boundary.actualizarEstadoCorrespondencia(correspondenciaDTO);
    }

    /**
     * @param correspondenciaDTO
     * @throws BusinessException
     * @throws SystemException
     */
    @PUT
    @Path("/correspondencia/actualizar-ide-instancia")
    public void actualizarIdeInstancia(CorrespondenciaDTO correspondenciaDTO) throws BusinessException, SystemException {
        log.info("processing rest request - actualizar ide instancia");
        boundary.actualizarIdeInstancia(correspondenciaDTO);
    }

    /**
     * @param fechaIni
     * @param fechaFin
     * @param codDependencia
     * @param codEstado
     * @param nroRadicado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @GET
    @Path("/correspondencia")
    public ComunicacionesOficialesDTO listarCorrespondenciaByPeriodoAndCodDependenciaAndCodEstadoAndNroRadicado(@QueryParam("fecha_ini") final String fechaIni,
                                                                                                                @QueryParam("fecha_fin") final String fechaFin,
                                                                                                                @QueryParam("cod_dependencia") final String codDependencia,
                                                                                                                @QueryParam("cod_estado") final String codEstado,
                                                                                                                @QueryParam("nro_radicado") final String nroRadicado) throws BusinessException, SystemException {
        log.info("processing rest request - listar correspondencia by periodo and cod_dependencia and cod_estado");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date fechaInicial = dateFormat.parse(fechaIni);
            Date fechaFinal = dateFormat.parse(fechaFin);
            return boundary.listarCorrespondenciaByPeriodoAndCodDependenciaAndCodEstadoAndNroRadicado(fechaInicial, fechaFinal, codDependencia, codEstado, nroRadicado);
        } catch (ParseException ex) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @GET
    @Path("/correspondencia/listar-distribucion")
    public ComunicacionesOficialesDTO listarCorrespondenciaSinDistribuir(@QueryParam("fecha_ini") final String fechaIni,
                                                                         @QueryParam("fecha_fin") final String fechaFin,
                                                                         @QueryParam("cod_dependencia") final String codDependencia,
                                                                         @QueryParam("cod_tipologia_documental") final String codTipoDoc,
                                                                         @QueryParam("nro_radicado") final String nroRadicado) throws BusinessException, SystemException {
        log.info("processing rest request - listar correspondencia sin distribuir");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date fechaInicial = dateFormat.parse(fechaIni);
            Date fechaFinal = dateFormat.parse(fechaFin);
            return boundary.listarCorrespondenciaSinDistribuir(fechaInicial, fechaFinal, codDependencia, codTipoDoc, nroRadicado);
        } catch (ParseException ex) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param nroRadicado
     * @return
     * @throws SystemException
     */
    @GET
    @Path("/correspondencia/verificar-numero-radicado/{nro_radicado}")
    public Boolean verificarByNroRadicado(@PathParam("nro_radicado") final String nroRadicado) throws SystemException {
        log.info("processing rest request - verificar correspondencia por numeroRadicado");
        return boundary.verificarByNroRadicado(nroRadicado);
    }

    /**
     * @param comunicacionOficialDTO
     * @throws BusinessException
     * @throws SystemException
     */
    @PUT
    @Path("/correspondencia/actualizar-comunicacion")
    public String actualizarComunicacion(ComunicacionOficialDTO comunicacionOficialDTO) throws BusinessException, SystemException {
        log.info("processing rest request - actualizar comunicacion", comunicacionOficialDTO);
        return boundary.actualizarComunicacion(comunicacionOficialDTO);
    }

    /**
     * @param comunicacionOficialDTO
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @POST
    @Path("/correspondencia/radicar-salida")
    public ComunicacionOficialDTO radicarCorrespondenciaSalida(ComunicacionOficialDTO comunicacionOficialDTO) throws BusinessException, SystemException {
        log.info("processing rest request - radicar correspondencia salida");
        return boundary.radicarCorrespondenciaSalida(comunicacionOficialDTO);
    }

    /**
     * @param nroRadicado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @GET
    @Path("/correspondencia/enviar-correo/{nro_radicado}")
    public Boolean sendMail(@PathParam("nro_radicado") final String nroRadicado) throws BusinessException, SystemException {
        log.info("processing rest request - enviar correo radicar correspondencia");
        return boundary.sendMail(nroRadicado);
    }
}
