package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.foundation.sgd.utils.SystemParameters;
import lombok.extern.log4j.Log4j2;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@ApiDelegator
@Log4j2
public class SedeAdministrativaClient {

    private String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

    private JerseyClient client = JerseyClientBuilder.createClient();

    public SedeAdministrativaClient() {
        super();
    }

    public Response list() {
        log.info("SedeAdministrativa - [trafic] - listing SedeAdministrativa with endpoint: " + endpoint);
        WebTarget wt = client.target(endpoint);
        return wt.path("/organigrama-web-api/organigrama/sedes")
                .request()
                .get();
    }

}
