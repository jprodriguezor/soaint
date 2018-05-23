package co.com.foundation.sgd.apigateway.apis;



import co.com.foundation.sgd.apigateway.apis.delegator.UpdateClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/update-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class UpdateGatewayApi {

    @Autowired
    private UpdateClient client;

    @GET
    @Path("/")
    @JWTTokenSecurity
    public Response getConstants(){
        log.info("UpdateGatewayApi - [trafic] - listing Constantes");
        Response response = client.GetContantsActives();
        String responseContent = response.readEntity(String.class);
        log.info("UpdateGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();

    }

}


