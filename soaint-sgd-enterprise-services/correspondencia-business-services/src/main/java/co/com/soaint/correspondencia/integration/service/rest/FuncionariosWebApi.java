package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarFuncionarios;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionariosDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 15-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: SERVICE - rest services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Path("/funcionarios-web-api")
@Produces({"application/json", "application/xml"})
@Log4j2
public class FuncionariosWebApi {

    @Autowired
    private GestionarFuncionarios boundary;

    /**
     * Constructor
     */
    public FuncionariosWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     * @param loginName
     * @param estado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @GET
    @Path("/funcionarios/{login_name}/{estado}")
    public FuncionarioDTO listarFuncionarioByLoginNameAndEstado(@PathParam("login_name") final String loginName, @PathParam("estado") final String estado) throws BusinessException, SystemException {
        log.info("processing rest request - listar funcionarios por login_name");
        return boundary.listarFuncionarioByLoginNameAndEstado(loginName, estado);
    }

    /**
     * @param codDependencia
     * @param codEstado
     * @return
     * @throws SystemException
     */
    @GET
    @Path("/funcionarios/dependencia/{cod_dependencia}/{cod_estado}")
    public FuncionariosDTO listarFuncionariosByCodDependenciaAndCodEstado(@PathParam("cod_dependencia") final String codDependencia, @PathParam("cod_estado") final String codEstado) throws SystemException {
        log.info("processing rest request - listar funcionarios por dependencia");
        return boundary.listarFuncionariosByCodDependenciaAndCodEstado(codDependencia, codEstado);
    }

}
