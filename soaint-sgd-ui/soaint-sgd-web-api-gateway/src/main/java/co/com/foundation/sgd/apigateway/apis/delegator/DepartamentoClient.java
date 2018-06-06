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
public class DepartamentoClient {

    private String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

    //private Client client = ClientBuilder.newClient();

    public DepartamentoClient() {
        super();
    }

    public Response listarPorPais(String pais) {
        log.info("Departamento - [trafic] - listing Departamento with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/departamentos-web-api/departamentos/" + pais + "/A")
                .request()
                .get();
    }

    public Response listarDepartamentosActivos() {
        log.info("Departamento - [trafic] - listar departamentos activos  con endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/departamentos-web-api/departamentos/A")
                .request()
                .get();
    }

}
