package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarDependencia;
import co.com.soaint.foundation.canonical.correspondencia.DependenciaDTO;
import co.com.soaint.foundation.canonical.correspondencia.DependenciasDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 23-Ago-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: SERVICE - rest services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@Path("/dependencia-web-api")
@Produces({"application/json", "application/xml"})
@Consumes({"application/json", "application/xml"})
@Log4j2
@Api(value = "DependenciasWebApi")
public class DependenciasWebApi {

    @Autowired
    GestionarDependencia boundary;

    /**
     * Constructor
     */
    public DependenciasWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     *
     * @param codOrg
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @GET
    @Path("/dependencia/{cod_org}")
    public DependenciaDTO listarDependenciaByCodigo(@PathParam("cod_org")final String codOrg)throws BusinessException, SystemException {
        log.info("processing rest request - listar dependencia by codOrg");
        return boundary.listarDependenciaByCodigo(codOrg);
    }

    /**
     *
     * @param codigos
     * @return
     * @throws SystemException
     */
    @GET
    @Path("/dependencia")
    public DependenciasDTO listarDependenciasByCodigo(@QueryParam("codigos")final String codigos)throws SystemException{
        log.info("processing rest request - listar dependencias by codigosOrg");
        return boundary.listarDependenciasByCodigo(codigos.split(","));
    }

    /**
     *
     * @return
     * @throws SystemException
     */
    @GET
    @Path("/dependencias")
    public DependenciasDTO listarDependencias() throws SystemException {
        log.info("processing rest request - listar dependencias");
        return boundary.listarDependencias();
    }
}
