package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@ApiDelegator
@Log4j2
public class MediosRecepcionClient {


    @Value("${backapi.endpoint.url}")
    private String endpoint = "";

    @Value("${contants.mediorecepcion.value}")
    private String medRecValue = "";

    public MediosRecepcionClient() {
        super();
    }

    public Response list() {
        log.info("MediosRecepcion - [trafic] - listing MediosRecepcion with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/constantes-web-api/constantes/hijos/" + medRecValue + "/A")
                .request()
                .get();
    }

}
