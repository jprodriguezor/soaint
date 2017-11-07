package co.com.foundation.sgd.apigateway.apis.delegator;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import co.com.foundation.sgd.utils.SystemParameters;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;

import co.com.foundation.sgd.infrastructure.ApiDelegator;

@ApiDelegator
@Log4j2
public class ProductosClient {

    private String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

    public ProductosClient() {
        super();
    }

    public Response list() {
        log.info("ProductosClient - [trafic] - listing products with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/productos-web-api")
                .request()
                .get();
    }

}
