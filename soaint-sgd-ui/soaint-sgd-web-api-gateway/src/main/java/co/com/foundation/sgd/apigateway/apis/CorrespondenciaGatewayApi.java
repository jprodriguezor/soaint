package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.CorrespondenciaClient;
import co.com.foundation.sgd.apigateway.apis.delegator.ProcesoClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.correspondencia.AsignacionesDTO;
import co.com.soaint.foundation.canonical.correspondencia.ComunicacionOficialDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Path("/correspondencia-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CorrespondenciaGatewayApi {

    @Autowired
    private CorrespondenciaClient client;


    @Autowired
    private ProcesoClient procesoClient;

    public CorrespondenciaGatewayApi() {
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @POST
    @Path("/radicar")
    @JWTTokenSecurity
    public Response radicarComunicacion(@RequestBody ComunicacionOficialDTO comunicacionOficial) {
        //TODO: add trafic log
        System.out.println("CorrespondenciaGatewayApi - [trafic] - radicar Correspondencia");
        Response response = client.radicar(comunicacionOficial);
        String responseContent = response.readEntity(String.class);
        System.out.println("CorrespondenciaGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/listar-comunicaciones")
    public Response listarComunicaciones(@QueryParam("fecha_ini") final String fechaIni,
                                         @QueryParam("fecha_fin") final String fechaFin,
                                         @QueryParam("cod_dependencia") final String codDependencia,
                                         @QueryParam("cod_estado") final String codEstado) {
        //TODO: add trafic log
        System.out.println("CorrespondenciaGatewayApi - [trafic] - listing Correspondencia");
        Response response = client.listarComunicaciones(fechaIni, fechaFin, codDependencia, codEstado);
        String responseContent = response.readEntity(String.class);
        System.out.println("CorrespondenciaGatewayApi - [content] : " + responseContent);
        if (response.getStatus() != HttpStatus.OK.value()) {
            return Response.status(HttpStatus.OK.value()).entity(new ArrayList<>()).build();
        }
        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @POST
    @Path("/asignar")
    public Response asignarComunicaciones(AsignacionesDTO asignacionesDTO) {
        System.out.println("CorrespondenciaGatewayApi - [trafic] - assinging Comunicaciones");
        Response response = client.asignarComunicaciones(asignacionesDTO);
        AsignacionesDTO responseObject = response.readEntity(AsignacionesDTO.class);
        //TODO llenar los datos para iniciar proceso
        /*responseObject.getAsignaciones().forEach(asignacionDTO -> {
            EntradaProcesoDTO entradaProceso = new EntradaProcesoDTO();
            entradaProceso.setIdProceso("");
            entradaProceso.setUsuario("");
            entradaProceso.setIdDespliegue("");
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("", "");
            entradaProceso.setParametros(parametros);
            this.procesoClient.iniciarManual(entradaProceso);
        });*/
        System.out.println("CorrespondenciaGatewayApi - [content] : " + responseObject);
        if (response.getStatus() != HttpStatus.OK.value()) {
            return Response.status(HttpStatus.OK.value()).entity(new ArrayList<>()).build();
        }
        return Response.status(response.getStatus()).entity(responseObject).build();
    }

}
