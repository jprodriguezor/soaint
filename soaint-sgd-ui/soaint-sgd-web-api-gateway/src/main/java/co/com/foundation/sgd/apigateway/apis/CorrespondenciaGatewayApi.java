package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.CorrespondenciaClient;
import co.com.foundation.sgd.apigateway.apis.delegator.ProcesoClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import co.com.soaint.foundation.canonical.correspondencia.ComunicacionOficialDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/correspondencia-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CorrespondenciaGatewayApi {

    @Autowired
    private CorrespondenciaClient client;


    @Autowired
    private ProcesoClient procesoClient;

    public CorrespondenciaGatewayApi() {
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @POST
    @Path("/radicar")
    @JWTTokenSecurity
    public Response radicarComunicacion(@RequestBody ComunicacionOficialDTO comunicacionOficial) {
        //TODO: add trafic log
        System.out.println("BisGatewayApi - [trafic] - listing Bis");
        Response response = client.radicar(comunicacionOficial);
        String responseContent = response.readEntity(String.class);
        System.out.println("BisGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/listar-comunicaciones")
    @JWTTokenSecurity
    public Response listarComunicaciones(@RequestParam String id) {
        //TODO: add trafic log
        return null;
    }

}
