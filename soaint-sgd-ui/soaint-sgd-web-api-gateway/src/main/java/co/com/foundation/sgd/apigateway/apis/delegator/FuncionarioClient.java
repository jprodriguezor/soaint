package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.foundation.sgd.utils.SystemParameters;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import lombok.extern.log4j.Log4j2;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@ApiDelegator
@Log4j2
public class FuncionarioClient {

    private String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

    private String funcionarioEndpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_FUNCIONARIO_SERVICE_ENDPOINT_URL);

    private Client client = ClientBuilder.newClient();

    public FuncionarioClient() {
        super();
    }

    public Response obtenerFuncionario(String login) {
        log.info("Funcionario - [trafic] - obtener Funcionario with endpoint: " + endpoint);
        WebTarget wt = client.target(endpoint);
        return wt.path("/funcionarios-web-api/funcionarios/" + login + "/A")
                .request()
                .get();
    }

    public Response listarFuncionarios(String codigoDependencia) {
        log.info("Funcionario - [trafic] - obtener Funcionario with endpoint: " + endpoint);
        WebTarget wt = client.target(endpoint);
        return wt.path("/funcionarios-web-api/funcionarios/dependencia/" + codigoDependencia + "/A")
                .request()
                .get();
    }

    public Response listarFuncionariosByLoginnames(String loginNames) {
        log.info("Funcionario - [trafic] - obtener Funcionario with endpoint: " + funcionarioEndpoint);
        WebTarget wt = client.target(funcionarioEndpoint);
        return wt.path("/funcionarios-web-api/funcionarios/listar-by-login-names/")
                .queryParam("login_names", loginNames)
                .request()
                .get();
    }

    public Response listarFuncionariosPorRol(String codigoDependencia, String rol) {
        log.info("Funcionario - [trafic] - obtener Funcionario with endpoint: " + endpoint);
        WebTarget wt = client.target(funcionarioEndpoint);
        return wt.path("/funcionarios-web-api/funcionarios/" + codigoDependencia + "/" + rol + "/A")
                .request()
                .get();
    }

    public Response listarFuncionarioRoles(){
        log.info("Funcionario - [trafic] - obtener Funcionario Roles with endpoint: " + endpoint);
        WebTarget wt = client.target(funcionarioEndpoint);
        return wt.path("/funcionarios-web-api/funcionarios/obtener_roles").request().get();
    }

    public Response updateFuncionarioRoles(FuncionarioDTO funcionarioDTO){
        log.info("Funcionario - [trafic] - update Funcionario Roles with endpoint: " + endpoint);
        WebTarget wt = client.target(funcionarioEndpoint);
        return wt.path("/funcionarios-web-api/funcionarios").request().buildPut(Entity.json(funcionarioDTO)).invoke();
    }

    public Response buscarFuncionario(FuncionarioDTO funcionarioDTO){
        log.info("Funcionario - [trafic] - buscar Funcionarios with endpoint: " + endpoint);
        WebTarget wt = client.target(funcionarioEndpoint);
        return wt.path("/funcionarios-web-api/funcionarios/buscar-funcionarios").request().buildPost(Entity.json(funcionarioDTO)).invoke();
    }

}
