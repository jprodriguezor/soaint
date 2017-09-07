package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@ApiDelegator
@Log4j2
public class CargaMasivaClient {
    @Value("${backapi.cargamasiva.endpoint.url}")
    private String endpoint = "";

    public CargaMasivaClient() {
        super();
    }

    public Response listCargaMasiva() {
        log.info("Carga Masiva - [trafic] - listing Carga Masiva with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/listadocargamasiva")
                .request()
                .get();
    }

    public Response listEstadoCargaMasiva() {
        log.info("Carga Masiva - [trafic] - listing Carga Masiva with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/estadocargamasiva")
                .request()
                .get();
    }

    public Response listEstadoCargaMasivaDadoId(String id) {
        log.info("Carga Masiva - [trafic] - listing Carga Masiva dado Id with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/estadocargamasiva/"+id)
                .request()
                .get();
    }
}
