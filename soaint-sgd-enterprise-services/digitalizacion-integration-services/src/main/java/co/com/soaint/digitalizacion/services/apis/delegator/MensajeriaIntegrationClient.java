package co.com.soaint.digitalizacion.services.apis.delegator;

import co.com.soaint.digitalizacion.services.infrastructure.ApiDelegator;
import co.com.soaint.digitalizacion.services.util.SystemParameters;
import co.com.soaint.foundation.canonical.mensajeria.MensajeGenericoQueueDTO;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * Created by amartinez on 08/03/2018.
 */
@ApiDelegator
@Log4j2
@NoArgsConstructor
public class MensajeriaIntegrationClient {

    private String endpoint = SystemParameters.getParameter(SystemParameters.MENSAJERIA_SERVICE_ENDPONT);

    public void digitalizarDocumento(MensajeGenericoQueueDTO mensajeDTO) throws SystemException {
        log.info("Correspondencia - [trafic] - digitalizar documento with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        try {
            Response respuesta = wt.path("/mensaje-web-api/mensaje/producir")
                    .request()
                    .post(Entity.json(mensajeDTO));
            if (Response.Status.OK.getStatusCode() == respuesta.getStatus() || Response.Status.NO_CONTENT.getStatusCode() == respuesta.getStatus()) {

            } else {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("digitalizar.error consultando servicio de integracion mensajeria")
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
