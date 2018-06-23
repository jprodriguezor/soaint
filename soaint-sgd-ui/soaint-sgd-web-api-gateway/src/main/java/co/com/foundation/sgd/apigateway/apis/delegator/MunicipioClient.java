package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.foundation.sgd.utils.SystemParameters;
import lombok.extern.log4j.Log4j2;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@ApiDelegator
@Log4j2
public class MunicipioClient {

    private String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

    //private Client client = ClientBuilder.newClient();

    public MunicipioClient() {
        super();
    }

    public Response listarPorDepartamento(String departamento) {
        log.info("Municipios - [trafic] - listing Municipios with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/municipios-web-api/municipios/" + departamento + "/A")
                .request()
                .get();
    }

    public Response listarMunicipiosPorCodigo(String codigos) {
        log.info("Municipios - [trafic] - listing Municipios by codes with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/municipios-web-api/municipios")
                .queryParam("codigos", codigos)
                .request()
                .get();
    }

    public Response listarMunicipiosActivos() {
        log.info("Municipios - [trafic] - listing Municipios with active status: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/municipios-web-api/municipios/A")
                .request()
                .get();
    }

}
