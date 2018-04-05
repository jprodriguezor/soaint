package co.com.soaint.correspondencia.apis.delegator;

import co.com.soaint.correspondencia.infrastructure.ApiDelegator;
import co.com.soaint.correspondencia.utils.SystemParameters;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * Created by esaliaga on 04/04/2018.
 */
@ApiDelegator
@Log4j2
@NoArgsConstructor
public class ReglasNegocioApiClient {

    private String droolsEndpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_DROOLS_SERVICE_ENDPOINT_URL);

    private String droolsAuthToken = SystemParameters.getParameter(SystemParameters.BACKAPI_DROOLS_SERVICE_TOKEN);

    public Response consultarTiempoDuracionTramite(String payload) {
        log.info("Correspondencia - [trafic] -metricas de Tiempo por Tipologia Regla: " + droolsEndpoint);

        WebTarget wt = ClientBuilder.newClient().target(droolsEndpoint);
        return wt.path("/regla")
                .request()
                .header("Authorization", "Basic " + droolsAuthToken)
                .header("X-KIE-ContentType", "json")
                .header("Content-Type", "application/json")
                .post(Entity.json(payload));
    }
}
