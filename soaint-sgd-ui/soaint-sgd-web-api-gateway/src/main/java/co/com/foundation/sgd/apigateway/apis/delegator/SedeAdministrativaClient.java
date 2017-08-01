package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@ApiDelegator
public class SedeAdministrativaClient {

    @Value("${backapi.endpoint.url}")
    private String endpoint = "";

    public SedeAdministrativaClient() {
        super();
    }

    public Response list() {
        System.out.println("SedeAdministrativa - [trafic] - listing SedeAdministrativa with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/organigrama-web-api/organigrama/sedes")
                .request()
                .get();
    }

}
