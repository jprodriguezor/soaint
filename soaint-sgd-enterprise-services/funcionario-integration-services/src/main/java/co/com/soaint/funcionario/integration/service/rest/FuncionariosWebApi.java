package co.com.soaint.funcionario.integration.service.rest;

import co.com.soaint.foundation.canonical.correspondencia.CredencialesDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionariosDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import co.com.soaint.funcionario.business.boundary.GestionarFuncionarios;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;

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
@Consumes({"application/json", "application/xml"})
@Produces({"application/json", "application/xml"})
@Log4j2
public class FuncionariosWebApi {

    @Autowired
    GestionarFuncionarios boundary;

    /**
     * Constructor
     */
    public FuncionariosWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     *
     * @param codDependencia
     * @param rol
     * @param codEstado
     * @return
     * @throws SystemException
     */
    @GET
    @Path("/funcionarios/{cod_dependencia}/{rol}/{cod_estado}")
    public FuncionariosDTO listarFuncionariosByDependenciaAndRolAndEstado(@PathParam("cod_dependencia")final String codDependencia,
                                                                          @PathParam("rol")final String rol,
                                                                          @PathParam("cod_estado")final String codEstado) throws BusinessException, SystemException {
        log.info("processing rest request - listar funcionarios por dependencia, rol y estado");
        return boundary.listarFuncionariosByDependenciaAndRolAndEstado(codDependencia, rol, codEstado);
    }

    /**
     *
     * @param credenciales
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @POST
    @Path("/funcionarios/verificar-credenciales")
    public FuncionarioDTO verificarCredenciales(CredencialesDTO credenciales) throws BusinessException, SystemException {
        log.info("processing rest request - verificar credenciales");
        return boundary.verificarCredenciales(credenciales);
    }

    @POST
    @Path("/funcionarios")
    public void crearFuncionario(FuncionarioDTO funcionario)throws SystemException{
        log.info("processing rest request - crear funcionario");
        boundary.crearFuncionario(funcionario);
    }
}
