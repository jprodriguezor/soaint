package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.CargaMasivaClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/Massive-Loader")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class CargaMasivaGatewayApi {

    @Autowired
    private CargaMasivaClient client;

    public CargaMasivaGatewayApi() {
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("/listadocargamasiva")
    @JWTTokenSecurity
    public Response listCargaMasiva() {
        log.info("CargaMasivaGatewayApi - [trafic] - listing Carga Masiva");
        Response response = client.listCargaMasiva ();
        String responseContent = response.readEntity(String.class);
        log.info("CargaMasivaGatewayApi - [content] : " + responseContent);

        return Response.status( response.getStatus() ).entity(responseContent).build();
    }

    @GET
    @Path("/estadocargamasiva")
    @JWTTokenSecurity
    public Response listEstadoCargaMasiva() {
        log.info("CargaMasivaGatewayApi - [trafic] - listing Estado Carga Masiva");
        Response response = client.listEstadoCargaMasiva ();
        String responseContent = response.readEntity(String.class);
        log.info("CargaMasivaGatewayApi - [content] : " + responseContent);

        return Response.status( response.getStatus() ).entity(responseContent).build();
    }

    @GET
    @Path("/estadocargamasiva/{id}")
    @JWTTokenSecurity
    public Response listEstadoCargaMasivaDadoId(@PathParam("id") String id) {
        log.info("CargaMasivaGatewayApi - [trafic] - listing Estado Carga Masiva dado Id");
        Response response = client.listEstadoCargaMasivaDadoId (id);
        String responseContent = response.readEntity(String.class);
        log.info("CargaMasivaGatewayApi - [content] : " + responseContent);

        return Response.status( response.getStatus() ).entity(responseContent).build();
    }
}

