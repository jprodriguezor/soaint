package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@ApiDelegator
@Log4j2
public class FuncionarioClient {



    @Value("${backapi.endpoint.url}")
    private String endpoint = "";

    public FuncionarioClient() {
        super();
    }

    public Response obtenerFuncionario(String login) {
        log.info("Funcionario - [trafic] - obtener Funcionario with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/funcionarios-web-api/funcionarios/" + login + "/A")
                .request()
                .get();
    }

    public Response listarFuncionarios(String codigoDependencia) {
        logger.info("Funcionario - [trafic] - obtener Funcionario with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/funcionarios-web-api/funcionarios/dependencia/" + codigoDependencia + "/A")
                .request()
                .get();
    }

}
