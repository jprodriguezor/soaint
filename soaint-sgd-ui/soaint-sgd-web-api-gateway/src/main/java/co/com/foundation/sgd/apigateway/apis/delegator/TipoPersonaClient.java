package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@ApiDelegator
public class TipoPersonaClient {

    @Value("${backapi.endpoint.url}")
    private String endpoint = "";

    public TipoPersonaClient() {
        super();
    }

    public Response list() {
        System.out.println("Tipo Persona - [trafic] - listing TipoPersona with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/constantes-web-api/constantes/hijos/TIP-PERS/A")
                .request()
                .get();
    }

}
