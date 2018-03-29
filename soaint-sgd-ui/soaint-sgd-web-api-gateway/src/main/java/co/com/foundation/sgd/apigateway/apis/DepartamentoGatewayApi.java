package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.DepartamentoClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/departamento-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class DepartamentoGatewayApi {

    @Autowired
    private DepartamentoClient client;

    public DepartamentoGatewayApi() {
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("/{pais}")
    @JWTTokenSecurity
    public Response list(@PathParam("pais") String pais) {

        log.info("DepartamentoGatewayApi - [trafic] - listing Departamento");
        Response response = client.listarPorPais(pais);
        String responseContent = response.readEntity(String.class);
        log.info("DepartamentoGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/")
    @JWTTokenSecurity
    public Response listarDepartamentosActivos() {

        log.info("DepartamentoGatewayApi - [trafic] - listing todos los Departamentos activos");
        Response response = client.listarDepartamentosActivos();
        String responseContent = response.readEntity(String.class);
        log.info("DepartamentoGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

}
