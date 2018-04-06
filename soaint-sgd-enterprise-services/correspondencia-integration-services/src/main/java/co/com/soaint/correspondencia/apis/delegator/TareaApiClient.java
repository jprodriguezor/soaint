package co.com.soaint.correspondencia.apis.delegator;

import co.com.soaint.correspondencia.infrastructure.ApiDelegator;
import co.com.soaint.correspondencia.utils.SystemParameters;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * Created by esaliaga on 05/04/2018.
 */
@ApiDelegator
@Log4j2
@NoArgsConstructor
public class TareaApiClient {

    private String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

    public Response consultaDataTarea(String idproceso, String idtarea) {
        log.info("Correspondencia - [trafic] - Invocando Servicio Remoto SalvarCorrespondenciaEntrada: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/tarea-web-api/tarea/" + idproceso + "/" + idtarea)
                .request().get();
    }
}
