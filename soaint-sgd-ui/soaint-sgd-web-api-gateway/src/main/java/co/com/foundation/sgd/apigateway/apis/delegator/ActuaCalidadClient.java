package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@ApiDelegator
public class ActuaCalidadClient {

    @Value("${backapi.endpoint.url}")
    private String endpoint = "";

    @Value("${contants.actuaencalidad.value}")
    private String actuaCaldiadValue = "";

    public ActuaCalidadClient() {
        super();
    }

    public Response list() {
        System.out.println("Client request listing Actua en calidad with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/constantes-web-api/constantes/hijos/" + actuaCaldiadValue + "/A")
                .request()
                .get();
    }

}
