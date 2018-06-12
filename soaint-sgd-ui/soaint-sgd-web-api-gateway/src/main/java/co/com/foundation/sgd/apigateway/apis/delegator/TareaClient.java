package co.com.foundation.sgd.apigateway.apis.delegator;


import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.foundation.sgd.utils.SystemParameters;
import co.com.soaint.foundation.canonical.correspondencia.TareaDTO;
import lombok.extern.log4j.Log4j2;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@ApiDelegator
@Log4j2
public class TareaClient {

    private String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

    //private Client client = ClientBuilder.newClient();

    public TareaClient() {
        super();
    }

    public Response guardarEstadoTarea(TareaDTO tareaDTO){
        log.info("Funcionario - [trafic] - buscar Funcionarios with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/tarea-web-api/tarea/").request().buildPost(Entity.json(tareaDTO)).invoke();
    }

    public Response listarEstadoTarea(final String idInstanciaProceso, final String idTareaProceso) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/tarea-web-api/tarea/" + idInstanciaProceso + "/" + idTareaProceso).request().get();
    }

}
