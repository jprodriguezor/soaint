package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.foundation.sgd.utils.SystemParameters;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * Cliente web para Tipo de complemento
 */
@ApiDelegator
@Log4j2
public class TipoComplementoClient {

    private String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

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
        log.info("[trafic] - listing Tipo Complemento with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/constantes-web-api/constantes/hijos/" + tipoComplementoValue + "/A")
                .request()
                .get();
    }
}
