package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.TipoComplementoClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import co.com.foundation.sgd.utils.ApiUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/tipo-complemento-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class TipoComplementoGatewayApi {

    @Autowired
    private TipoComplementoClient client;

    public TipoComplementoGatewayApi() {
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("/")
    @JWTTokenSecurity
    public Response list() {

        log.info("TipoComplementoGatewayApi - [trafic] - listing TipoComplemento");
        //Response response = client.list();
        //String responseContent = response.readEntity(String.class);
        //log.info("TipoComplementoGatewayApi - [content] : " + responseContent);
        return ApiUtils.getResponseClient(client);
        //return Response.status(response.getStatus()).entity(responseContent).build();
    }

}
