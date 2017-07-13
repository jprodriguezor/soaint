package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarAsignacion;
import co.com.soaint.foundation.canonical.correspondencia.AgentesDTO;
import co.com.soaint.foundation.canonical.correspondencia.AsignacionesDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
public class AsignacionWebApi {
    private static Logger LOGGER = LogManager.getLogger(AsignacionWebApi.class.getName());

    @Autowired
    GestionarAsignacion boundary;

    public AsignacionWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @POST
    @Path("/asignacion")
    public AsignacionesDTO asignarCorrespondencia(AsignacionesDTO asignacionesDTO)throws BusinessException, SystemException{
        LOGGER.info("processing rest request - asignar correspondencia");
        return boundary.asignarCorrespondencia(asignacionesDTO);
    }

    @PUT
    @Path("/asignacion/{ide_asignacion}/{id_instancia}")
    public void actualizarIdInstancia(@PathParam("ide_asignacion")final Long ideAsignacion, @PathParam("id_instancia")final String idInstancia)throws BusinessException, SystemException{
        LOGGER.info("processing rest request - actualizar instancia ultima asignacion");
        boundary.actualizarIdInstancia(ideAsignacion, idInstancia);
    }

    @PUT
    @Path("/asignacion/redireccionar")
    public void redireccionarCorrespondencia(AgentesDTO agentesDTO) throws BusinessException, SystemException{
        LOGGER.info("processing rest request - redireccionar correspondencia");
        boundary.redireccionarCorrespondencia(agentesDTO);
    }

    @GET
    @Path("/asignacion")
    public AsignacionesDTO listarAsignacionesByFuncionarioAndNroRadicado(@QueryParam("ide_funci")final BigInteger ideFunci,
                                                                         @QueryParam("nro_radicado")final String nroRadicado)throws  BusinessException, SystemException{
        LOGGER.info("processing rest request - listar asignaciones por funcionario y nroradicado");
        return boundary.listarAsignacionesByFuncionarioAndNroRadicado(ideFunci, nroRadicado);
    }
}
