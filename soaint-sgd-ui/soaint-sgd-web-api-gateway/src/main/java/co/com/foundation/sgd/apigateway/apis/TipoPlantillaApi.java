package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.TipoPlantillaClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/tipo-plantilla-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class TipoPlantillaApi {

    @Autowired
    private TipoPlantillaClient client;

    public TipoPlantillaApi() {
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("/plantilla/{codClasificacion}/{estado}")
    @JWTTokenSecurity
    public Response get(@PathParam("codClasificacion") final String codClasificacion, @PathParam("estado") final String estado) {

        log.info("TiposPlantillaGatewayApi - [trafic] - listing TiposPlantilla");
        Response response = client.get(codClasificacion, estado);
        String responseContent = response.readEntity(String.class);
        log.info("TiposPlantillaGatewayApi - [content] : " + responseContent);

        return Response.status( response.getStatus() ).entity(responseContent).build();
    }

}
