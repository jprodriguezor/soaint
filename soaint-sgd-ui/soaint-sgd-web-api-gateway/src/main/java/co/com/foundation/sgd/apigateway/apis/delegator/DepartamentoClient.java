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
public class DepartamentoClient {

    private String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

    private JerseyClient client = JerseyClientBuilder.createClient();

    public DepartamentoClient() {
        super();
    }

    public Response listarPorPais(String pais) {
        log.info("Departamento - [trafic] - listing Departamento with endpoint: " + endpoint);
        WebTarget wt = client.target(endpoint);
        return wt.path("/departamentos-web-api/departamentos/" + pais + "/A")
                .request()
                .get();
    }

    public Response listarDepartamentosActivos() {
        log.info("Departamento - [trafic] - listar departamentos activos  con endpoint: " + endpoint);
        WebTarget wt = client.target(endpoint);
        return wt.path("/departamentos-web-api/departamentos/A")
                .request()
                .get();
    }

}
