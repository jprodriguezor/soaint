package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarCorrespondencia;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.json.Json;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
@Api(value = "CorrespondenciaWebApi")
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
     * @param nroRadicado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @GET
    @Path("/correspondencia/obtener-ide-instancia-radicado/{nro_radicado}")
    public Response getIdeInstanciaPorRadicado(@PathParam("nro_radicado") final String nroRadicado) throws BusinessException, SystemException {
        log.info("processing rest request - consultarNroRadicadoCorrespondenciaReferida.");

        String ideInstancia = boundary.getIdeInstanciaPorRadicado(nroRadicado);
        return Response
                .status(Response.Status.CREATED)
                .entity(Json.createObjectBuilder().add("ideInstancia",ideInstancia).build())
                .build();
    }

    /**
     * @param nroRadicado
     * @throws BusinessException
     * @throws SystemException
     */
    @GET
    @Path("/correspondencia/obtener-ide-instancia-dependencia-radicado/{nro_radicado}")
    public CorrespondenciaDTO getCorrespondenciaInstanciaPorRadicado(@PathParam("nro_radicado") final String nroRadicado) throws SystemException, BusinessException {
         return boundary.getCorrespondenciaInstanciaPorRadicado(nroRadicado);
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
        log.info("fecha_ini - "+fechaIni);
        log.info("fecha_fin - "+fechaFin);
        log.info("cod_dependencia - "+codDependencia);
        log.info("cod_estado - "+codEstado);
        log.info("nro_radicado - "+nroRadicado);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date fechaInicial = (fechaIni!=null)?dateFormat.parse(fechaIni):null;
            Date fechaFinal = (fechaIni!=null)?dateFormat.parse(fechaFin):null;
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
     * @param fechaIni
     * @param fechaFin
     * @param codDependencia
     * @param modEnvio
     * @param claseEnvio
     * @param nroRadicado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @GET
    @Path("/correspondencia/listar-comunicacion-salida-distribucion-fisica")
    public ComunicacionesOficialesFullDTO listarComunicacionDeSalidaConDistribucionFisica(@QueryParam("fecha_ini") final String fechaIni,
                                                                                      @QueryParam("fecha_fin") final String fechaFin,
                                                                                      @QueryParam("mod_correo") final String modEnvio,
                                                                                      @QueryParam("clase_envio") final String claseEnvio,
                                                                                      @QueryParam("cod_dep") final String codDependencia,
                                                                                      @QueryParam("nro_radicado") final String nroRadicado) throws BusinessException, SystemException {
        log.info("processing rest request - listar comunicaciones distribucion fisica");
           try {
               return boundary.listarComunicacionDeSalidaConDistribucionFisica(fechaIni, fechaFin, modEnvio, claseEnvio, codDependencia, nroRadicado);
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

//    /**
//     * @param comunicacionOficialDTO
//     * @return
//     * @throws BusinessException
//     * @throws SystemException
//     */
//    @POST
//    @Path("/correspondencia/radicar-salida")
//    public ComunicacionOficialDTO radicarCorrespondenciaSalida(ComunicacionOficialDTO comunicacionOficialDTO) throws BusinessException, SystemException {
//        log.info("processing rest request - radicar correspondencia salida");
//        return boundary.radicarCorrespondenciaSalida(comunicacionOficialDTO);
//    }

    /**
     * @param comunicacionOficialDTO
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @POST
    @Path("/correspondencia/radicar-salida")
    public ComunicacionOficialDTO radicarCorrespondenciaSalidaRemitenteReferidoADestinatario(ComunicacionOficialRemiteDTO comunicacionOficialDTO)
                                                                                                throws BusinessException, SystemException {
        log.info("processing rest request - radicar correspondencia salida");
        return boundary.radicarCorrespondenciaSalidaRemitenteReferidoADestinatario(comunicacionOficialDTO);
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

    /**
     * @param solicitudUnidadDocumental
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @POST
    @Path("/correspondencia/crear-solicitud-um")
    public Response crearSolicitudUnidadDocumental(@Valid SolicitudesUnidadDocumentalDTO solicitudUnidadDocumental) throws BusinessException, SystemException {
        log.info("processing rest request - crearSolicitudUnidadDocumental");
        boundary.crearSolicitudUnidadDocumental(solicitudUnidadDocumental);
        return Response
                .status(Response.Status.CREATED)
                .entity(ResponseStatus.of(true))
                .build();
    }

    /**
     * @param codigoSede
     * @param codigoDependencia
     * @param fechaI
     * @param fechaF
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @GET
    @Path("/correspondencia/obtener-solicitud-um")
    public SolicitudesUnidadDocumentalDTO obtenerSolicitudUnidadDocumentalSedeDependenciaIntervalo(
            @QueryParam("cod_sede") final String codigoSede,
            @QueryParam("cod_dependencia") final String codigoDependencia,
            @QueryParam("fecha_ini") final String fechaI,
            @QueryParam("fecha_fin") final String fechaF) throws BusinessException, SystemException {
        log.info("processing rest request - crearSolicitudUnidadDocumental");

        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaInicial = fechaI == null ? null : dateFormat.parse(fechaI);
            Date fechaFinal = fechaF == null ? null : dateFormat.parse(fechaF);

            return boundary.obtenerSolicitudUnidadDocumentalSedeDependenciaIntervalo(fechaInicial,fechaFinal,codigoSede,codigoDependencia);

        } catch (ParseException ex) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param codigoSede
     * @param codigoDependencia
     * @param ideSolicitante
     * @param fechaI
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @GET
    @Path("/correspondencia/obtener-solicitud-um-solicitante")
    public SolicitudesUnidadDocumentalDTO obtenerSolicitudUnidadDocumentalSedeDependencialSolicitante(
                                                                                    @QueryParam("cod_sede") final String codigoSede,
                                                                                    @QueryParam("cod_dependencia") final String codigoDependencia,
                                                                                    @QueryParam("id_solicitante") final String ideSolicitante,
                                                                                    @QueryParam("fecha_in") final String fechaI) throws BusinessException, SystemException {
        log.info("processing rest request - crearSolicitudUnidadDocumental");

        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaInicial = fechaI == null ? null : dateFormat.parse(fechaI);

            return boundary.obtenerSolicitudUnidadDocumentalSedeDependencialSolicitante(fechaInicial,ideSolicitante,codigoSede,codigoDependencia);

        } catch (ParseException ex) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param codigoSede
     * @param codigoDependencia
     * @param ideSolicitante
     * @param fechaI
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @GET
    @Path("/correspondencia/obtener-solicitud-um-solicitante-sin-tramitar")
    public SolicitudesUnidadDocumentalDTO obtenerSolicitudUnidadDocumentalSedeDependencialSolicitanteSinTramitar(
                                                                                    @QueryParam("cod_sede") final String codigoSede,
                                                                                    @QueryParam("cod_dependencia") final String codigoDependencia,
                                                                                    @QueryParam("id_solicitante") final String ideSolicitante,
                                                                                    @QueryParam("fecha_in") final String fechaI) throws BusinessException, SystemException {
        log.info("processing rest request - crearSolicitudUnidadDocumental");

        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaInicial = fechaI == null ? null : dateFormat.parse(fechaI);

            return boundary.obtenerSolicitudUnidadDocumentalSedeDependencialSolicitanteSinTramitar(fechaInicial,ideSolicitante,codigoSede,codigoDependencia);

        } catch (ParseException ex) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param solicitudUnidadDocumentalDTO
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @PUT
    @Path("/correspondencia/actualizar-solicitud-um")
    public SolicitudUnidadDocumentalDTO actualizarSolicitudUnidadDocumental(SolicitudUnidadDocumentalDTO solicitudUnidadDocumentalDTO) throws BusinessException, SystemException {
        log.info("processing rest request - updateSolicitudUnidadDocumental");

        return boundary.actualizarSolicitudUnidadDocumental(solicitudUnidadDocumentalDTO);
    }

    /**
     * @param nroRadicado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @GET
    @Path("/correspondencia/obtener-correspondencia-referido/{nro_radicado}")
    public Response consultarNroRadicadoCorrespondenciaReferida(@PathParam("nro_radicado") final String nroRadicado) throws SystemException, BusinessException {
        log.info("processing rest request - consultarNroRadicadoCorrespondenciaReferida.");

        String nroRadicadoReferido = boundary.consultarNroRadicadoCorrespondenciaReferida(nroRadicado);
        return Response
                .status(Response.Status.CREATED)
                .entity(Json.createObjectBuilder().add("nroRadicado",nroRadicadoReferido).build())
                .build();
    }
}
