package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.ProcesoClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/proceso-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProcesoGatewayApi {

    @Autowired
    private ProcesoClient procesoClient;

    public ProcesoGatewayApi() {
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("/")
    @JWTTokenSecurity
    public Response list() {
        //TODO: add trafic log
        System.out.println("ProcesoGatewayApi - [trafic] - listing Procesos");
        Response response = procesoClient.list();
        String responseContent = response.readEntity(String.class);
        System.out.println("ProcesoGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @POST
    @Path("/iniciar")
    @JWTTokenSecurity
    public Response iniciarProceso(EntradaProcesoDTO entrada) {
        //TODO: add trafic log
        System.out.println("ProcesoGatewayApi - [trafic] - starting Process");
        Response response = procesoClient.iniciarManual(entrada);
        String responseContent = response.readEntity(String.class);
        System.out.println("ProcesoGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @POST
    @Path("/listar/estados-instancia")
    @JWTTokenSecurity
    public Response listTareasIdProceso(EntradaProcesoDTO entrada) {
        //TODO: add trafic log
        System.out.println("ProcesoGatewayApi - [trafic] - listing Precess");
        Response response = procesoClient.listarPorIdProceso(entrada);
        String responseContent = response.readEntity(String.class);
        System.out.println("ProcesoGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @POST
    @Path("/tareas/listar/estados")
    @JWTTokenSecurity
    public Response listTareas(EntradaProcesoDTO entrada) {
        //TODO: add trafic log
        System.out.println("ProcesoGatewayApi - [trafic] - listing Tasks");
        Response response = procesoClient.listarTareas(entrada);
        String responseContent = response.readEntity(String.class);
        System.out.println("ProcesoGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @POST
    @Path("/tareas/iniciar")
    @JWTTokenSecurity
    public Response iniciarTarea(EntradaProcesoDTO entrada) {
        //TODO: add trafic log
        System.out.println("ProcesoGatewayApi - [trafic] - start Task");
        Response response = procesoClient.iniciarTarea(entrada);
        String responseContent = response.readEntity(String.class);
        System.out.println("ProcesoGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @POST
    @Path("/tareas/completar")
    @JWTTokenSecurity
    public Response completarTarea(EntradaProcesoDTO entrada) {
        //TODO: add trafic log
        System.out.println("ProcesoGatewayApi - [trafic] - start Task");
        Response response = procesoClient.completarTarea(entrada);
        String responseContent = response.readEntity(String.class);
        System.out.println("ProcesoGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/proceso/listar-instancias")
    @JWTTokenSecurity
    public Response listarIntanciasProceso() {
        //TODO: add trafic log
        System.out.println("ProcesoGatewayApi - [trafic] - listing Process Instances");
        Response response = procesoClient.listarIntanciasProceso();
        String responseContent = response.readEntity(String.class);
        System.out.println("ProcesoGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

}