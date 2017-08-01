package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.PrefijoCuadranteClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/prefijo-cuadrante-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PrefijoCuadranteGatewayApi {

    @Autowired
    private PrefijoCuadranteClient client;

    public PrefijoCuadranteGatewayApi() {
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("/")
    @JWTTokenSecurity
    public Response list() {
        //TODO: add trafic log
        System.out.println("PrefijoCuadranteGatewayApi - [trafic] - listing PrefijoCuadrante");
        Response response = client.list();
        String responseContent = response.readEntity(String.class);
        System.out.println("PrefijoCuadranteGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

}
