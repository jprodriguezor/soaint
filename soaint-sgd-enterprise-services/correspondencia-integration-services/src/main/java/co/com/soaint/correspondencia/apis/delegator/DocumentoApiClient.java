package co.com.soaint.correspondencia.apis.delegator;

import co.com.soaint.correspondencia.infrastructure.ApiDelegator;
import co.com.soaint.correspondencia.utils.SystemParameters;
import co.com.soaint.foundation.canonical.correspondencia.DocumentoDTO;
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
public class DocumentoApiClient {

    private String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

    public Response actualizaReferenciaEcm(DocumentoDTO documentoDTO) {
        log.info("Correspondencia - [trafic] - actualizar referencia ecm with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/documento-web-api/documento/actualizar-referencia-ecm")
                .request()
                .put(Entity.json(documentoDTO));
    }

}
