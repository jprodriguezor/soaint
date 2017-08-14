package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Cliente web para Tipo de complemento
 */
@ApiDelegator
public class TipoComplementoClient {

    private static final Logger LOGGER = Logger.getLogger(TipoComplementoClient.class.getName());

    @Value("${backapi.endpoint.url}")
    private String endpoint = "";

    @Value("${contants.tipocomplemento.value}")
    private String tipoComplementoValue = "";

    /**
     * Constructor
     */
    public TipoComplementoClient() {
        super();
    }

    /**
     * Listar tipos de complemento
     *
     * @return Response
     */
    public Response list() {
        LOGGER.log(Level.INFO, "[trafic] - listing Tipo Complemento with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/constantes-web-api/constantes/hijos/" + tipoComplementoValue + "/A")
                .request()
                .get();
    }
}
