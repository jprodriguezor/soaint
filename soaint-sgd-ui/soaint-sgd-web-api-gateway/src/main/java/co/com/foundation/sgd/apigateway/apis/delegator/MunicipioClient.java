package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@ApiDelegator
public class MunicipioClient {

    @Value("${backapi.endpoint.url}")
    private String endpoint = "";

    public MunicipioClient() {
        super();
    }

    public Response listarPorDepartamento(String departamento) {
        System.out.println("Municipios - [trafic] - listing Municipios with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/municipios-web-api/municipios/" + departamento + "/A")
                .request()
                .get();
    }

}
