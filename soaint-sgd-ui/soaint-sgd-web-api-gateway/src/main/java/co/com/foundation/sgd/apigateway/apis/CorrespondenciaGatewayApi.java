package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.CorrespondenciaClient;
import co.com.foundation.sgd.apigateway.apis.delegator.ProcesoClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.bpm.EstadosEnum;
import co.com.soaint.foundation.canonical.correspondencia.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    @JWTTokenSecurity
    public Response listarComunicaciones(@QueryParam("fecha_ini") final String fechaIni,
                                         @QueryParam("fecha_fin") final String fechaFin,
                                         @QueryParam("cod_dependencia") final String codDependencia,
                                         @QueryParam("cod_estado") final String codEstado,
                                         @QueryParam("nro_radicado") final String nroRadicado) {

        log.info("CorrespondenciaGatewayApi - [trafic] - listing Correspondencia");
        Response response = client.listarComunicaciones(fechaIni, fechaFin, codDependencia, codEstado, nroRadicado);
        String responseContent = response.readEntity(String.class);
        log.info(CONTENT + responseContent);
        if (response.getStatus() != HttpStatus.OK.value()) {
            return Response.status(HttpStatus.OK.value()).entity(new ArrayList<>()).build();
        }
        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @POST
    @Path("/asignar")
    @JWTTokenSecurity
    public Response asignarComunicaciones(AsignacionesDTO asignacionesDTO) {
        log.info("CorrespondenciaGatewayApi - [trafic] - assinging Comunicaciones");
        Response response = client.asignarComunicaciones(asignacionesDTO);
        AsignacionesDTO responseObject = response.readEntity(AsignacionesDTO.class);


        List<EstadosEnum> estados = new ArrayList();
        estados.add(EstadosEnum.LISTO);
//        estados.add(EstadosEnum.RESERVADO);

        responseObject.getAsignaciones().forEach(asignacionDTO -> {
            EntradaProcesoDTO entradaProceso = new EntradaProcesoDTO();
            entradaProceso.setIdProceso("proceso.recibir-gestionar-doc");
            entradaProceso.setIdDespliegue("co.com.soaint.sgd.process:proceso-recibir-gestionar-doc:1.0.4-SNAPSHOT");
            entradaProceso.setEstados(estados);
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("idAsignacion", asignacionDTO.getIdeAsignacion().toString());
            parametros.put("idAgente", asignacionDTO.getIdeAgente().toString());
            parametros.put("usuario", asignacionDTO.getLoginName());
            parametros.put("idDocumento", asignacionDTO.getIdeDocumento().toString());
            parametros.put("numeroRadicado", asignacionDTO.getNroRadicado());
            if (asignacionDTO.getAlertaVencimiento() != null)
                parametros.put("fechaVencimiento", asignacionDTO.getAlertaVencimiento());
            entradaProceso.setParametros(parametros);
            this.procesoClient.iniciarTercero(entradaProceso);
        });
        log.info(CONTENT + responseObject);
        if (response.getStatus() != HttpStatus.OK.value()) {
            return Response.status(HttpStatus.OK.value()).entity(new ArrayList<>()).build();
        }
        return Response.status(response.getStatus()).entity(responseObject).build();
    }

    @POST
    @Path("/reasignar")
    @JWTTokenSecurity
    public Response reasignarComunicaciones(AsignacionesDTO asignacionesDTO) {
        log.info("CorrespondenciaGatewayApi - [trafic] - assinging Comunicaciones");
        Response response = client.asignarComunicaciones(asignacionesDTO);
        AsignacionesDTO responseObject = response.readEntity(AsignacionesDTO.class);
        responseObject.getAsignaciones().forEach(asignacionDTO -> {
            EntradaProcesoDTO entradaProceso = new EntradaProcesoDTO();
            entradaProceso.setIdProceso("proceso.recibir-gestionar-doc");
            entradaProceso.setIdDespliegue("co.com.soaint.sgd.process:proceso-recibir-gestionar-doc:1.0.4-SNAPSHOT");
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
    @JWTTokenSecurity
    public Response redireccionarComunicaciones(AgentesDTO agentesDTO) {
        log.info("CorrespondenciaGatewayApi - [trafic] - redirect Comunicaciones");
        Response response = client.redireccionarComunicaciones(agentesDTO);
        String responseObject = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseObject).build();
    }

    @GET
    @Path("/metricasTiempo")
    @JWTTokenSecurity
    public Response metricasTiempo(@QueryParam("payload") String payload) {
        log.info("CorrespondenciaGatewayApi - [trafic] - redirect Comunicaciones");
        Response response = client.metricasTiempoDrools(payload);
        String responseObject = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseObject).build();
    }

    @GET
    @Path("/obtener-comunicacion/{nro_radicado}")
    @JWTTokenSecurity
    public Response obtenerComunicacion(@PathParam("nro_radicado") String nroRadicado) {
        log.info("CorrespondenciaGatewayApi - [trafic] - redirect Comunicaciones");
        Response response = client.obtenerCorrespondenciaPorNroRadicado(nroRadicado);
        String responseObject = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseObject).build();
    }

    @GET
    @Path("/obtenerObservaciones/{idCorrespondencia}")
    @JWTTokenSecurity
    public Response obtenerObservaciones(@PathParam("idCorrespondencia") BigInteger idCorrespondencia) {
        log.info("CorrespondenciaGatewayApi - [trafic] - listing Observaciones for document: " + idCorrespondencia);
        Response response = client.listarObservaciones(idCorrespondencia);
        String responseObject = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseObject).build();
    }

    @POST
    @Path("/registrarObservacion")
    @JWTTokenSecurity
    public Response registrarObservacion(PpdTrazDocumentoDTO observacion) {
        log.info("CorrespondenciaGatewayApi - [trafic] - redirect Comunicaciones");
        Response response = client.registrarObservacion(observacion);
        String responseObject = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseObject).build();
    }

    @GET
    @Path("/constantes")
    @JWTTokenSecurity
    public Response constantes(@QueryParam("codigos") String codigos) {
        log.info("CorrespondenciaGatewayApi - [trafic] - obteniendo constantes por codigos: " + codigos);
        Response response = client.obtnerContantesPorCodigo(codigos);
        String responseObject = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseObject).build();
    }

    @GET
    @Path("/listar-distribucion")
    @JWTTokenSecurity
    public Response listarDistribucion(@QueryParam("fecha_ini") final String fechaIni,
                                       @QueryParam("fecha_fin") final String fechaFin,
                                       @QueryParam("cod_dependencia") final String codDependencia,
                                       @QueryParam("cod_tipologia_documental") final String codTipoDoc,
                                       @QueryParam("nro_radicado") final String nroRadicado) {
        log.info("CorrespondenciaGatewayApi - [trafic] - listando distribucion");
        Response response = client.listarDistribucion(fechaIni, fechaFin, codDependencia, codTipoDoc, nroRadicado);
        String responseObject = response.readEntity(String.class);
        if (response.getStatus() != HttpStatus.OK.value()) {
            return Response.status(HttpStatus.OK.value()).entity(new ArrayList<>()).build();
        }
        return Response.status(response.getStatus()).entity(responseObject).build();
    }

    @GET
    @Path("/listar-planillas")
    @JWTTokenSecurity
    public Response listarPlanillas(@QueryParam("fecha_ini") final String fechaIni,
                                    @QueryParam("fecha_fin") final String fechaFin,
                                    @QueryParam("cod_dependencia") final String codDependencia,
                                    @QueryParam("cod_tipologia_documental") final String codTipoDoc,
                                    @QueryParam("nro_planilla") final String nroPlanilla) {
        log.info("CorrespondenciaGatewayApi - [trafic] - listando planillas");
        Response response = client.listarPlanillas(nroPlanilla);
        String responseObject = response.readEntity(String.class);
        if (response.getStatus() != HttpStatus.OK.value()) {
            PlanillaDTO emptyPlanilla = new PlanillaDTO();
            PlanAgentesDTO planAgentesDTO = new PlanAgentesDTO();
            planAgentesDTO.setPAgente(new ArrayList<>());
            emptyPlanilla.setPAgentes(planAgentesDTO);
            return Response.status(HttpStatus.OK.value()).entity(emptyPlanilla).build();
        }
        return Response.status(response.getStatus()).entity(responseObject).build();
    }

    @POST
    @Path("/generar-plantilla")
    public Response generarPlanilla(@RequestBody PlanillaDTO planilla) {
        log.info("processing rest request - generar planilla distribucion");
        Response response = client.generarPlantilla(planilla);
        String responseObject = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseObject).build();
    }

    @POST
    @Path("/cargar-plantilla")
    public Response cargarPlanilla(@RequestBody PlanillaDTO planilla) {
        log.info("processing rest request - cargar planilla distribucion");
        Response response = client.cargarPlantilla(planilla);
        String responseObject = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseObject).build();
    }

    @POST
    @Path("/salvar-correspondencia-entrada")
    @JWTTokenSecurity
    public Response salvarCorrespondenciaEntrada(TareaDTO tarea) {
        log.info("CorrespondenciaGatewayApi - [trafic] - Salvando Correspondencia Entrada");
        Response response = client.salvarCorrespondenciaEntrada(tarea);
        String responseObject = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseObject).build();
    }

    @GET
    @Path("/restablecer_correspondencia_entrada/{proceso}/{tarea}")
    @JWTTokenSecurity
    public Response restablecerCorrespondenciaEntrada(@PathParam("proceso") final String idproceso, @PathParam("tarea") final String idtarea) {
        log.info("CorrespondenciaGatewayApi - [trafic] - Restableciendo Correspondencia Entrada");
        Response response = client.restablecerCorrespondenciaEntrada(idproceso, idtarea);
        String responseObject = response.readEntity(String.class);
        if (response.getStatus() != HttpStatus.OK.value()) {
            return Response.status(HttpStatus.OK.value()).entity(new ArrayList<>()).build();
        }
        return Response.status(response.getStatus()).entity(responseObject).build();
    }


}
