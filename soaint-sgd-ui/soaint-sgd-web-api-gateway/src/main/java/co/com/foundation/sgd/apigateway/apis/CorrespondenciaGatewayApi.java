package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.CorrespondenciaClient;
import co.com.foundation.sgd.apigateway.apis.delegator.ProcesoClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.correspondencia.AgentesDTO;
import co.com.soaint.foundation.canonical.correspondencia.AsignacionesDTO;
import co.com.soaint.foundation.canonical.correspondencia.ComunicacionOficialDTO;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class CorrespondenciaGatewayApi {

    private static final String CONTENT = "CorrespondenciaGatewayApi - [content] : ";
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
        
        log.info("CorrespondenciaGatewayApi - [trafic] - radicar Correspondencia");
        Response response = client.radicar(comunicacionOficial);
        String responseContent = response.readEntity(String.class);
        log.info(CONTENT + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/listar-comunicaciones")
    public Response listarComunicaciones(@QueryParam("fecha_ini") final String fechaIni,
                                         @QueryParam("fecha_fin") final String fechaFin,
                                         @QueryParam("cod_dependencia") final String codDependencia,
                                         @QueryParam("cod_estado") final String codEstado) {

        log.info("CorrespondenciaGatewayApi - [trafic] - listing Correspondencia");
        Response response = client.listarComunicaciones(fechaIni, fechaFin, codDependencia, codEstado);
        String responseContent = response.readEntity(String.class);
        log.info(CONTENT + responseContent);
        if (response.getStatus() != HttpStatus.OK.value()) {
            return Response.status(HttpStatus.OK.value()).entity(new ArrayList<>()).build();
        }
        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @POST
    @Path("/asignar")
    public Response asignarComunicaciones(AsignacionesDTO asignacionesDTO) {
        log.info("CorrespondenciaGatewayApi - [trafic] - assinging Comunicaciones");
        Response response = client.asignarComunicaciones(asignacionesDTO);
        AsignacionesDTO responseObject = response.readEntity(AsignacionesDTO.class);
        responseObject.getAsignaciones().forEach(asignacionDTO -> {
            EntradaProcesoDTO entradaProceso = new EntradaProcesoDTO();
            entradaProceso.setIdProceso("proceso.recibir-gestionar-doc");
            entradaProceso.setIdDespliegue("co.com.soaint.sgd.process:proceso-recibir-gestionar-doc:1.0.1-SNAPSHOT");
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("idAsignacion", asignacionDTO.getIdeAsignacion().toString());
            parametros.put("idAgente", asignacionDTO.getIdeAgente());
            parametros.put("usuario", asignacionDTO.getLoginName());
            parametros.put("idDocumento", asignacionDTO.getIdeDocumento());
            parametros.put("numeroRadicado", asignacionDTO.getNroRadicado());
            if (asignacionDTO.getFechaVencimiento() != null)
                parametros.put("fechaVencimiento", asignacionDTO.getFechaVencimiento().toString());
            entradaProceso.setParametros(parametros);
            this.procesoClient.iniciar(entradaProceso);
        });
        log.info(CONTENT + responseObject);
        if (response.getStatus() != HttpStatus.OK.value()) {
            return Response.status(HttpStatus.OK.value()).entity(new ArrayList<>()).build();
        }
        return Response.status(response.getStatus()).entity(responseObject).build();
    }

    @POST
    @Path("/reasignar")
    public Response reasignarComunicaciones(AsignacionesDTO asignacionesDTO) {
        log.info("CorrespondenciaGatewayApi - [trafic] - assinging Comunicaciones");
        Response response = client.asignarComunicaciones(asignacionesDTO);
        AsignacionesDTO responseObject = response.readEntity(AsignacionesDTO.class);
        responseObject.getAsignaciones().forEach(asignacionDTO -> {
            EntradaProcesoDTO entradaProceso = new EntradaProcesoDTO();
            entradaProceso.setIdProceso("proceso.recibir-gestionar-doc");
            entradaProceso.setIdDespliegue("co.com.soaint.sgd.process:proceso-recibir-gestionar-doc:1.0.1-SNAPSHOT");
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("usuarioReasignar", asignacionDTO.getLoginName());
            entradaProceso.setParametros(parametros);
            this.procesoClient.reasignarTarea(entradaProceso);
        });
        log.info(CONTENT + responseObject);
        if (response.getStatus() != HttpStatus.OK.value()) {
            return Response.status(HttpStatus.OK.value()).entity(new ArrayList<>()).build();
        }
        return Response.status(response.getStatus()).entity(responseObject).build();
    }

    @POST
    @Path("/redireccionar")
    public Response redireccionarComunicaciones(AgentesDTO agentesDTO) {
        log.info("CorrespondenciaGatewayApi - [trafic] - redirect Comunicaciones");
        Response response = client.redireccionarComunicaciones(agentesDTO);
        String responseObject = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseObject).build();
    }

    @GET
    @Path("/metricasTiempo")
    public Response metricasTiempo(@QueryParam("payload") String payload) {
        log.info("CorrespondenciaGatewayApi - [trafic] - redirect Comunicaciones");
        Response response = client.metricasTiempoDrools(payload);
        String responseObject = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseObject).build();
    }

}
