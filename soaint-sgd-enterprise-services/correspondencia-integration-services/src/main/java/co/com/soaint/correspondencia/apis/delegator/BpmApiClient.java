package co.com.soaint.correspondencia.apis.delegator;

import co.com.soaint.correspondencia.infrastructure.ApiDelegator;
import co.com.soaint.correspondencia.utils.SystemParameters;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * Created by esaliaga on 06/03/2018.
 */
@ApiDelegator
@Log4j2
@NoArgsConstructor
public class BpmApiClient {
    private String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ENTERPRISE_SERVICE_ENDPOINT_URL);

    public void enviarSennal(EntradaProcesoDTO entradaProceso) throws SystemException {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        try {
            Response respuesta = wt.path("/bpm/proceso/sennal/")
                    .request()
                    .post(Entity.json(entradaProceso));
            if (Response.Status.OK.getStatusCode() != respuesta.getStatus())
                throw ExceptionBuilder.newBuilder()
                        .withMessage("correspondencia.error consultando servicio de integracion BPM")
                        .buildSystemException();
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
