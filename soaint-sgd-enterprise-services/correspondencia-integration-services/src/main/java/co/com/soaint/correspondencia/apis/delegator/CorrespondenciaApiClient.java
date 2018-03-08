package co.com.soaint.correspondencia.apis.delegator;

import co.com.soaint.correspondencia.infrastructure.ApiDelegator;
import co.com.soaint.correspondencia.utils.SystemParameters;
import co.com.soaint.foundation.canonical.correspondencia.ComunicacionOficialDTO;
import co.com.soaint.foundation.canonical.correspondencia.DependenciaDTO;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * Created by esaliaga on 06/03/2018.
 */
@ApiDelegator
@Log4j2
@NoArgsConstructor
public class CorrespondenciaApiClient {
    private String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

    public ComunicacionOficialDTO obtenerCorrespondenciaPorNroRadicado(String nroRadicado) throws SystemException{
        log.info("Correspondencia - [trafic] - obtenet Correspondencia por nro de radicado with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        try {
            Response respuesta = wt.path("/correspondencia-web-api/correspondencia/" + nroRadicado)
                    .request()
                    .get();
            if (Response.Status.OK.getStatusCode() == respuesta.getStatus()) {
                return respuesta.readEntity(ComunicacionOficialDTO.class);
            } else {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("correspondencia.error consultando servicio de negocio GestionarCorrespondencia")
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

    public DependenciaDTO consultarDependenciaByCodigo(String codigo)throws SystemException {
        log.info("Dependecia - [trafic] - Get by code with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        try {
            Response respuesta = wt.path("/dependencia-web-api/dependencia/".concat(codigo))
                    .request()
                    .get();
            if (Response.Status.OK.getStatusCode() == respuesta.getStatus()) {
                return respuesta.readEntity(DependenciaDTO.class);
            } else {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("correspondencia.error consultando servicio de negocio GestionarDependencias")
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
