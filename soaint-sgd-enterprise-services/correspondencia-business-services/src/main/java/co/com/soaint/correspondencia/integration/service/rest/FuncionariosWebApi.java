package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarFuncionarios;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

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
public class FuncionariosWebApi {
    private static Logger LOGGER = LogManager.getLogger(FuncionariosWebApi.class.getName());

    @Autowired
    GestionarFuncionarios boundary;

    @GET
    @Path("/funcionarios/{login_name}/{estado}")
    public FuncionarioDTO listarFuncionarioByLoginNameAndEstado(@PathParam("login_name") final String loginName, @PathParam("estado")final String estado) throws BusinessException, SystemException{
        LOGGER.info("processing rest request - listar funcionarios por login_name");
        return boundary.listarFuncionarioByLoginNameAndEstado(loginName, estado);
    }
}
