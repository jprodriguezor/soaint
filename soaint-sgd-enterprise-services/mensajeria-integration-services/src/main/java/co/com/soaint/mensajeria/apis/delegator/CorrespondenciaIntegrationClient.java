package co.com.soaint.mensajeria.apis.delegator;

import co.com.soaint.foundation.canonical.integration.DigitalizacionDTO;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import co.com.soaint.mensajeria.infrastructure.ApiDelegator;
import co.com.soaint.mensajeria.util.SystemParameters;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * Created by esaliaga on 07/03/2018.
 */
@ApiDelegator
@Log4j2
@NoArgsConstructor
public class CorrespondenciaIntegrationClient {

    private String endpoint = SystemParameters.getParameter(SystemParameters.CORRESPONDENCIA_INTEGRATION_SERVICE_ENDPOINT);

    public void digitalizarDocumento(DigitalizacionDTO digitalizacionDTO) throws SystemException {
        log.info("Correspondencia - [trafic] - digitalizar documento with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        try {
            Response respuesta = wt.path("/digitalizacion-web-api/digitalizacion")
                    .request()
                    .post(Entity.json(digitalizacionDTO));
            if (Response.Status.OK.getStatusCode() == respuesta.getStatus() || Response.Status.NO_CONTENT.getStatusCode() == respuesta.getStatus()) {

            } else {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("correspondencia.error consultando servicio de integracion ECM")
                        .buildSystemException();
            }
        } catch (SystemException e) {
            log.error("Api Delegator - a api delegator error has occurred", e);
            throw e;
        } catch (Exception ex) {
            log.error("Api Delegator - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }
}
