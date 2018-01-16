package co.com.foundation.sgd.apigateway.apis.delegator;


import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.foundation.sgd.utils.SystemParameters;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@ApiDelegator
@Log4j2
public class TipoPlantillaClient {

    private String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

    @Value("${contants.tipoplantilla.value}")
    private String tipoDestValue = "";

    public TipoPlantillaClient() {
        super();
    }

    public Response get(String codClasificacion, String estado) {
        log.info("TipoPlantilla - [trafic] - listing TipoPlantilla with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/plantilla-web-api/plantilla/" + codClasificacion + "/" +estado)
                .request()
                .get();
    }

}
