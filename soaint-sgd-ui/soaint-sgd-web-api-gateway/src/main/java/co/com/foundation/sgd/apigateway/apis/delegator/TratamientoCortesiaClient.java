package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@ApiDelegator
public class TratamientoCortesiaClient {

    @Value("${backapi.endpoint.url}")
    private String endpoint = "";

    @Value("${contants.tratamientocortesia.value}")
    private String trataValue = "";

    public TratamientoCortesiaClient() {
        super();
    }

    public Response list() {
        System.out.println("Tipo Teléfono - [trafic] - listing Tratamiento Cortesia with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/constantes-web-api/constantes/hijos/" + trataValue + "/A")
                .request()
                .get();
    }

}