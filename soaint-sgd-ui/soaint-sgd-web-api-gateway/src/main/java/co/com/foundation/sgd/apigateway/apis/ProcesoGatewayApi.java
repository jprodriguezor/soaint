package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.ProcesoClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/proceso-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class ProcesoGatewayApi {

    private static final String CONTENT = "ProcesoGatewayApi - [content] : ";
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

        log.info("ProcesoGatewayApi - [trafic] - listing Procesos");
        Response response = procesoClient.list();
        String responseContent = response.readEntity(String.class);
        log.info(CONTENT + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    /**
     * @param entrada
     * @return Response
     */
    @POST
    @Path("/iniciar")
    @JWTTokenSecurity
    public Response iniciarProceso(EntradaProcesoDTO entrada) {

        log.info("ProcesoGatewayApi - [trafic] - starting Process");
        Response response = procesoClient.iniciarManual(entrada);
        String responseContent = response.readEntity(String.class);
        log.info(CONTENT + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @POST
    @Path("/listar/estados-instancia")
    @JWTTokenSecurity
    public Response listTareasIdProceso(EntradaProcesoDTO entrada) {

        log.info("ProcesoGatewayApi - [trafic] - listing Precess");
        Response response = procesoClient.listarPorIdProceso(entrada);
        String responseContent = response.readEntity(String.class);
        log.info(CONTENT + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @POST
    @Path("/tareas/listar/estados")
    @JWTTokenSecurity
    public Response listTareas(EntradaProcesoDTO entrada) {

        log.info("ProcesoGatewayApi - [trafic] - listing Tasks");
        Response response = procesoClient.listarTareas(entrada);
        String responseContent = response.readEntity(String.class);
        log.info(CONTENT + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @POST
    @Path("/tareas/iniciar")
    @JWTTokenSecurity
    public Response iniciarTarea(EntradaProcesoDTO entrada) {

        log.info("ProcesoGatewayApi - [trafic] - start Task");
        Response response = procesoClient.iniciarTarea(entrada);
        String responseContent = response.readEntity(String.class);
        log.info(CONTENT + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @POST
    @Path("/tareas/completar")
    @JWTTokenSecurity
    public Response completarTarea(EntradaProcesoDTO entrada) {

        log.info("ProcesoGatewayApi - [trafic] - start Task");
        Response response = procesoClient.completarTarea(entrada);
        String responseContent = response.readEntity(String.class);
        log.info(CONTENT + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/proceso/listar-instancias")
    @JWTTokenSecurity
    public Response listarIntanciasProceso() {

        log.info("ProcesoGatewayApi - [trafic] - listing Process Instances");
        Response response = procesoClient.listarIntanciasProceso();
        String responseContent = response.readEntity(String.class);
        log.info(CONTENT + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @POST
    @Path("/tareas/obtener-variables")
    @JWTTokenSecurity
    public Response obtenerVaraiblesTarea(EntradaProcesoDTO entrada) {

        log.info("ProcesoGatewayApi - [trafic] - get task variables");
        Response response = procesoClient.obtenerVariablesTarea(entrada);
        String responseContent = response.readEntity(String.class);
        log.info(CONTENT + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

}
