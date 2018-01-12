package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarAsignacion;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import java.math.BigInteger;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 11-Jul-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: SERVICE - rest services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@Path("/asignacion-web-api")
@Produces({"application/json", "application/xml"})
@Consumes({"application/json", "application/xml"})
@Log4j2
@Api(value = "AsignacionWebApi", description = "")
public class AsignacionWebApi {

    @Autowired
    GestionarAsignacion boundary;

    /**
     * Constructor
     */
    public AsignacionWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     *
     * @param asignacionTramite
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @POST
    @Path("/asignacion/asignar-funcionario")
    public AsignacionesDTO asignarCorrespondencia(AsignacionTramiteDTO asignacionTramite) throws BusinessException, SystemException {
        log.info("processing rest request - asignar tramite a funcionario");
        return boundary.asignarCorrespondencia(asignacionTramite);
    }

    /**
     * @param asignacion
     * @throws BusinessException
     * @throws SystemException
     */
    @PUT
    @Path("/asignacion/actualizar-instancia")
    public void actualizarIdInstancia(AsignacionDTO asignacion) throws BusinessException, SystemException {
        log.info("processing rest request - actualizar instancia ultima asignacion");
        boundary.actualizarIdInstancia(asignacion);
    }

    /**
     *
     * @param asignacion
     * @throws SystemException
     */
    @PUT
    @Path("/asignacion/actualizar-tipo-proceso")
    public void actualizarTipoProceso(AsignacionDTO asignacion) throws SystemException {
        log.info("processing rest request - actualizar codigo tipo proceso");
        boundary.actualizarTipoProceso(asignacion);
    }

    /**
     * @param ideFunci
     * @param nroRadicado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @GET
    @Path("/asignacion")
    public AsignacionesDTO listarAsignacionesByFuncionarioAndNroRadicado(@QueryParam("ide_funci") final BigInteger ideFunci,
                                                                         @QueryParam("nro_radicado") final String nroRadicado) throws BusinessException, SystemException {
        log.info("processing rest request - listar asignaciones por funcionario y nroradicado");
        return boundary.listarAsignacionesByFuncionarioAndNroRadicado(ideFunci, nroRadicado);
    }

    /**
     *
     * @param ideAgente
     * @return
     * @throws SystemException
     */
    @GET
    @Path("/asignacion/re-asignacion/{ide_agente}")
    public FuncAsigDTO consultarAsignacionReasignarByIdeAgente(@PathParam("ide_agente")final BigInteger ideAgente) throws SystemException {
        log.info("processing rest request - re asignar tramite");
        return boundary.consultarAsignacionReasignarByIdeAgente(ideAgente);
    }

    /**
     *
     * @param correspondencia
     * @throws BusinessException
     * @throws SystemException
     */
    @POST
    @Path("/asignacion/asignar-documento")
    public void asignarDocumentoByNroRadicado(CorrespondenciaDTO correspondencia) throws BusinessException, SystemException {
        log.info("processing rest request - asignar correspondencia by nroRadicado");
        boundary.asignarDocumentoByNroRadicado(correspondencia.getNroRadicado());
    }
}
