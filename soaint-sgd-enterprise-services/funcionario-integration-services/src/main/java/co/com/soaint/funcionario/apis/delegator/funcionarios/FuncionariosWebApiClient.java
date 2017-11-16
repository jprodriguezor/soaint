package co.com.soaint.funcionario.apis.delegator.funcionarios;

import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionariosDTO;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import co.com.soaint.funcionario.infrastructure.ApiDelegator;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * Created by esanchez on 8/28/2017.
 */
@ApiDelegator
@Log4j2
@NoArgsConstructor
public class FuncionariosWebApiClient {

    @Value("${backapi.endpoint.url}")
    private String endpoint = "";

    /**
     * @param codDependencia
     * @param codEstado
     * @return
     * @throws SystemException
     */
    public FuncionariosDTO listarFuncionariosByDependenciaAndEstado(String codDependencia, String codEstado) throws SystemException {
        log.info("Funcionarios - [trafic] - listing Funcionarios with endpoint: " + endpoint);
        try {
            WebTarget wt = getWebTarget();
            Response respuesta = wt.path("/funcionarios-web-api/funcionarios/dependencia/" + codDependencia + "/" + codEstado)
                    .request()
                    .get();
            if (Response.Status.OK.getStatusCode() == respuesta.getStatus()) {
                return respuesta.readEntity(FuncionariosDTO.class);
            } else {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("funcionarios.error consultando servicio de negocio GestionarFuncionarios")
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

    /**
     *
     * @param loginName
     * @return
     * @throws SystemException
     */
    public FuncionarioDTO listarFuncionarioByLoginName(String loginName)throws SystemException {
        try {
            WebTarget wt = getWebTarget();
            Response respuesta = wt.path("/funcionarios-web-api/funcionarios/" + loginName)
                    .request()
                    .get();
            if (Response.Status.OK.getStatusCode() == respuesta.getStatus()) {
                return respuesta.readEntity(FuncionarioDTO.class);
            }
            else
                throw ExceptionBuilder.newBuilder()
                        .withMessage("funcionarios.error consultando servicio de negocio GestionarFuncionarios")
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

    /**
     *
     * @param funcionario
     * @throws SystemException
     */
    public void crearFuncionario(FuncionarioDTO funcionario)throws SystemException{
        try {
            WebTarget wt = getWebTarget();
            wt.path("/funcionarios-web-api/funcionarios")
                    .request()
                    .post(Entity.json(funcionario));
        } catch (Exception ex) {
            log.error("Api Delegator - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    private WebTarget getWebTarget(){
        return ClientBuilder.newClient().target(endpoint);
    }
}
