package co.com.foundation.sgd.apigateway.apis;


import co.com.foundation.sgd.apigateway.apis.delegator.ModalidadCorreoClient;
import co.com.foundation.sgd.apigateway.apis.delegator.MotivoNoCreacionUdClient;
import co.com.foundation.sgd.apigateway.apis.delegator.SoporteAnexosClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/motivo-no-creacion-ud-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2

public class MotivoNoCreacionUdGatewayApi {

    @Autowired
    private MotivoNoCreacionUdClient client;

    public  MotivoNoCreacionUdGatewayApi(){

        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("/")
    @JWTTokenSecurity
    public Response list() {

        log.info("Motivo No Creacion - [trafic] - listing Bis");
        Response response = client.list();
        String responseContent = response.readEntity(String.class);
        log.info("BisGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

}
