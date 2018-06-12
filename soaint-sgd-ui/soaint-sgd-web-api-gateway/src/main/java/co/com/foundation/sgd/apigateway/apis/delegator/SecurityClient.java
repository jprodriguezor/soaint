package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.foundation.sgd.utils.SystemParameters;
import co.com.soaint.foundation.canonical.correspondencia.CredencialesDTO;
import lombok.extern.log4j.Log4j2;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@ApiDelegator
@Log4j2
public class SecurityClient {

    private String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_FUNCIONARIO_SERVICE_ENDPOINT_URL);

    //private Client client = ClientBuilder.newClient();

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
