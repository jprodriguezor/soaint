package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.soaint.foundation.canonical.correspondencia.CredencialesDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@ApiDelegator
@Log4j2
public class SecurityClient {



    @Value("${backapi.funcionario.service.endpoint.url}")
    private String endpoint = "";

    public SecurityClient() {
        super();
    }

    public Response verificarCredenciales(CredencialesDTO credenciales) {
        log.info("Funcionario - [trafic] - obtener Funcionario with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/funcionarios-web-api/funcionarios/verificar-credenciales")
                .request()
                .post(Entity.json(credenciales));
    }

}
