package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.FuncionarioClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/funcionario-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FuncionarioGatewayApi {

    @Autowired
    private FuncionarioClient client;

    public FuncionarioGatewayApi() {
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("/")
    @JWTTokenSecurity
    public Response get() {
        //TODO: add trafic log
        System.out.println("FuncionarioGatewayApi - [trafic] - listing Funcionario");
        Response response = client.obtenerFuncionario("daniel.barrios");
        String responseContent = response.readEntity(String.class);
        System.out.println("FuncionarioGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/funcionarios/{cod_dependencia}")
    @JWTTokenSecurity
    public Response listarFuncionarios(@PathParam("cod_dependencia") String codigoDependencia) {
        //TODO: add trafic log
        System.out.println("FuncionarioGatewayApi - [trafic] - listing Funcionario");
        Response response = client.listarFuncionarios(codigoDependencia);
        String responseContent = response.readEntity(String.class);
        System.out.println("FuncionarioGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

}
