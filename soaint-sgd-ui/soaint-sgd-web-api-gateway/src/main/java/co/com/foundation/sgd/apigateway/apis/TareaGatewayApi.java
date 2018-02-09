package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.TareaClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import co.com.soaint.foundation.canonical.correspondencia.TareaDTO;

import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.json.Json;
import javax.json.JsonString;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/tarea-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class TareaGatewayApi {

    @Autowired
    private TareaClient tareaClient;

    public TareaGatewayApi() {
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @POST
    @Path("/tarea")
    @JWTTokenSecurity
    public Response guardarEstadoTarea(TareaDTO tarea){
        log.info("TareaGatewayApi - [trafic] - listing Funcionario");
        Response response = tareaClient.guardarEstadoTarea(tarea);
        String responseContent = response.readEntity(String.class);
        log.info("TareaGatewayApi - [content] : " + responseContent);
        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/tarea/{id-instancia-proceso}/{id-tarea-proceso}")
    @JWTTokenSecurity
    public Response listarEstadoTarea(@PathParam("id-instancia-proceso")final String idInstanciaProceso,
                                      @PathParam("id-tarea-proceso")final String idTareaProceso) {
        log.info("TareaGatewayApi - [trafic] - listing Funcionario");
        Response response = tareaClient.listarEstadoTarea(idInstanciaProceso,idTareaProceso);
        String responseContent = response.readEntity(String.class);
        log.info("TareaGatewayApi - [content] : " + responseContent);
        return Response.status(response.getStatus()).entity(responseContent).build();
    }

}
