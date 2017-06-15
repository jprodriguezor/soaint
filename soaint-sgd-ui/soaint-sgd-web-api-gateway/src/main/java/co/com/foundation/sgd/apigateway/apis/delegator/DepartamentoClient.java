package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@ApiDelegator
public class DepartamentoClient {

    @Value("${backapi.endpoint.url}")
    private String endpoint = "";

    public DepartamentoClient() {
        super();
    }

    public Response listarPorPais(String pais) {
        System.out.println("Departamento - [trafic] - listing Departamento with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/departamentos-web-api/departamentos/" + pais + "/A")
                .request()
                .get();
    }

}
