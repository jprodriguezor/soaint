package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.MunicipioClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/municipio-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MunicipioGatewayApi {

    @Autowired
    private MunicipioClient municipioClient;

    public MunicipioGatewayApi() {
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("/{departamento}")
    @JWTTokenSecurity
    public Response list(@PathParam("departamento") String departamento) {
        //TODO: add trafic log
        System.out.println("MunicipioGatewayApi - [trafic] - listing Departamento");
        Response response = municipioClient.listarPorDepartamento(departamento);
        String responseContent = response.readEntity(String.class);
        System.out.println("MunicipioGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

}
