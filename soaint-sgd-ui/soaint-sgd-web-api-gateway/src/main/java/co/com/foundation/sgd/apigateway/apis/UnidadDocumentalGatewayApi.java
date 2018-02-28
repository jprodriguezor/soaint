package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.MunicipioClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import co.com.soaint.foundation.canonical.correspondencia.MunicipioDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/unidad-documental-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class UnidadDocumentalGatewayApi {

    @Autowired
    private UnidadDocumentalClient unidadDocumentalClient;
    
    @Autowired
    private ECMClient ECMClient;

    public UnidadDocumentalGatewayApi() {
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @POST
    @Path("/listado-serie")
    @JWTTokenSecurity
    public Response listarSerie(@QueryParam("idOrgOfc") String dependencia ) {

        log.info("UnidadDocumentalGatewayApi - [trafic] - listing series");
        Response response = ECMClient.listarSeriesPorDependencia(dependencia);
        String responseContent = response.readEntity(String.class);
        log.info("UnidadDocumentalGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @POST
    @Path("/listado-subserie")
    public Response listarSubserie(@QueryParam("idOrgOfc") String dependencia, @QueryParam("codSerie") String codSerie) {

        log.info("UnidadDocumentalGatewayApi - [trafic] - listing subseries");
        Response response = ECMClient.listarSubseriesPorSerie(codigos, codSerie);
        String responseContent = response.readEntity(String.class);
        log.info("UnidadDocumentalGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }
}
