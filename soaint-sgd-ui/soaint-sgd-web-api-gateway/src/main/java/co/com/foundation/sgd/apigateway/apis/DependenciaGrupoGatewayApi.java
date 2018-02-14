package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.DependeciaGrupoClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/dependencia-grupo-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class DependenciaGrupoGatewayApi {

    @Autowired
    private DependeciaGrupoClient client;

    public DependenciaGrupoGatewayApi() {
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("/{cod-sede-administrativa}")
    @JWTTokenSecurity
    public Response listBySedeAdministrativa(@PathParam("cod-sede-administrativa") String codSedeAdministrativa) {

        log.info("DependenciaGrupoGatewayApi - [trafic] - listing Dependencia");
        Response response = client.listBySedeAdministrativa(codSedeAdministrativa);
        String responseContent = response.readEntity(String.class);
        log.info("DependenciaGrupoGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/dependencias/{codigo}")
    @JWTTokenSecurity
    public Response obtnerPorCodigo(@PathParam("codigo") String codigo) {

        log.info("DependenciaGrupoGatewayApi - [trafic] - get Dependencia");
        Response response = client.obtenerPorCodigo(codigo);
        String responseContent = response.readEntity(String.class);
        log.info("DependenciaGrupoGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/dependencias")
    @JWTTokenSecurity
    public Response obtnerPorDependencia(@QueryParam("codigos") String codigosDependencia) {

        log.info("DependenciaGrupoGatewayApi - [trafic] - get Dependencia");
        Response response = client.obtenerPorDependencias(codigosDependencia);
        String responseContent = response.readEntity(String.class);
        log.info("DependenciaGrupoGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/all-dependencias")
    @JWTTokenSecurity
    public Response listarDendencias() {

        log.info("DependenciaGrupoGatewayApi - [trafic] - listar Dependencias");
        Response response = client.listarDependencias();
        String responseContent = response.readEntity(String.class);
        log.info("DependenciaGrupoGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

}
