package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.FuncionarioClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

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

        log.info("FuncionarioGatewayApi - [trafic] - listing Funcionario");
        Response response = client.listarFuncionarios(codigoDependencia);
        String responseContent = response.readEntity(String.class);
        log.info("FuncionarioGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/funcionarios/{cod_dependencia}/{rol}")
    @JWTTokenSecurity
    public Response listarFuncionariosByRol(@PathParam("cod_dependencia") final String codDependencia,
                                            @PathParam("rol") final String rol) {

        log.info("FuncionarioGatewayApi - [trafic] - listing Funcionario By Rol");
        Response response = client.listarFuncionariosPorRol(codDependencia, rol);
        String responseContent = response.readEntity(String.class);
        log.info("FuncionarioGatewayApi - [content] : " + responseContent);

        if (response.getStatus() == HttpStatus.OK.value()) {
            return Response.status(response.getStatus()).entity(responseContent).build();
        }

        return Response.status(HttpStatus.OK.value()).entity(new ArrayList<>()).build();
    }

    @GET
    @Path("/funcionarios/roles")
    @JWTTokenSecurity
    public Response listarFuncionariosRoles(){
        log.info("FuncionarioGatewayApi - [trafic] - listing roles funcionarios");
        Response response = client.listarFuncionarioRoles();
        String responseContent = response.readEntity(String.class);
        log.info("FuncionarioGatewayApi - [content] : " + responseContent);

        if (response.getStatus() == HttpStatus.OK.value())
            return Response.status(response.getStatus()).entity(responseContent).build();
        return Response.status(HttpStatus.OK.value()).entity(new ArrayList<>()).build();

    }

    @PUT
    @Path("/funcionarios")
    @JWTTokenSecurity
    public Response updateFuncionarioRole(FuncionarioDTO funcionarioDTO){
        log.info("processing rest request - actualizar funcionario");
        Response response = client.updateFuncionarioRoles(funcionarioDTO);
        String responseContent = response.readEntity(String.class);
        log.info("FuncionarioGatewayApi - [content] : " + responseContent);
        return response.getStatus() == HttpStatus.OK.value() ?
             Response.status(response.getStatus()).entity(responseContent).build() : response;
    }

    @POST
    @Path("/funcionarios/buscar")
    @JWTTokenSecurity
    public Response buscarFuncionarios(FuncionarioDTO funcionarioDTO){
        log.info("FuncionarioGatewayApi - [trafic] - buscar funcionarios");
        Response response = client.buscarFuncionario(funcionarioDTO);
        String responseContent = response.readEntity(String.class);
        log.info("FuncionarioGatewayApi - [content] : " + responseContent);
        return Response.status(response.getStatus()).entity(responseContent).build();
    }

}
