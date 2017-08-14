package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@ApiDelegator
@Log4j2
public class DependeciaGrupoClient {


    @Value("${backapi.endpoint.url}")
    private String endpoint = "";

    public DependeciaGrupoClient() {
        super();
    }

    public Response listBySedeAdministrativa(String codigoSedeAdministrativa) {
        log.info("DependeciaGrupo - [trafic] - listing DependeciaGrupo with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/organigrama-web-api/organigrama/dependencias/" + codigoSedeAdministrativa)
                .request()
                .get();
    }

}
