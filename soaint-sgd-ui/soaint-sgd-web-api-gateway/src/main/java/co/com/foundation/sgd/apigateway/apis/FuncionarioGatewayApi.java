package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.FuncionarioClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/funcionario-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class FuncionarioGatewayApi {

    @Autowired
    private FuncionarioClient client;

    public FuncionarioGatewayApi() {
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("/{userName}")
    @JWTTokenSecurity
    public Response get(@PathParam("userName") String userName) {
        //TODO: add trafic log
        log.info("FuncionarioGatewayApi - [trafic] - listing Funcionario");
        Response response = client.obtenerFuncionario(userName);
        String responseContent = response.readEntity(String.class);
        log.info("FuncionarioGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/funcionarios/{cod_dependencia}")
    @JWTTokenSecurity
    public Response listarFuncionarios(@PathParam("cod_dependencia") String codigoDependencia) {
        //TODO: add trafic log
        log.info("FuncionarioGatewayApi - [trafic] - listing Funcionario");
        Response response = client.listarFuncionarios(codigoDependencia);
        String responseContent = response.readEntity(String.class);
        log.info("FuncionarioGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

}
