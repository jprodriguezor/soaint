package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.soaint.foundation.canonical.correspondencia.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.PathParam;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@ApiDelegator
@Log4j2
public class CorrespondenciaClient {


    @Value("${backapi.endpoint.url}")
    private String endpoint = "";

    @Value("${backapi.ecm.service.endpoint.url}")
    private String ecmEndpoint = "";

    @Value("${backapi.drools.service.endpoint.url}")
    private String droolsEndpoint = "";

    @Value("${backapi.drools.service.token}")
    private String droolsAuthToken = "";

    public CorrespondenciaClient() {
        super();
    }

    public Response radicar(ComunicacionOficialDTO comunicacionOficialDTO) {
        log.info("Correspondencia - [trafic] - radicar Correspondencia with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/correspondencia-web-api/correspondencia")
                .request()
                .post(Entity.json(comunicacionOficialDTO));
    }

    public Response listarComunicaciones(String fechaIni, String fechaFin, String codDependencia, String codEstado, String nroRadicado) {
        log.info("Correspondencia - [trafic] - radicar Correspondencia with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/correspondencia-web-api/correspondencia")
                .queryParam("fecha_ini", fechaIni)
                .queryParam("fecha_fin", fechaFin)
                .queryParam("cod_dependencia", codDependencia)
                .queryParam("cod_estado", codEstado)
                .queryParam("nro_radicado", nroRadicado)
                .request()
                .get();
    }

    public Response listarObservaciones(BigInteger idCorrespondencia) {
        log.info("Correspondencia - [trafic] - radicar Correspondencia with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/documento-web-api/documento/listar-observaciones/" + idCorrespondencia)
                .request()
                .get();
    }

    public Response obtenerCorrespondenciaPorNroRadicado(String nroRadicado) {
        log.info("Correspondencia - [trafic] - obtenet Correspondencia por nro de radicado with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/correspondencia-web-api/correspondencia/" + nroRadicado)
                .request()
                .get();
    }

    public Response asignarComunicaciones(AsignacionTramiteDTO asignacionTramiteDTO) {
        log.info("Correspondencia - [trafic] - asignar Comunicaciones with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/asignacion-web-api/asignacion/asignar-funcionario")
                .request()
                .post(Entity.json(asignacionTramiteDTO));
    }

    public Response obtenerFuncionarInfoParaReasignar(BigInteger idAgente) {
        log.info("Asignacion - [trafic] - obtener Hash del Funcionario with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/asignacion-web-api/asignacion/re-asignacion/" + idAgente)
                .request()
                .get();
    }

    public Response redireccionarComunicaciones(RedireccionDTO redireccionDTO) {
        log.info("Correspondencia - [trafic] - redireccionar Comunicaciones with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/agente-web-api/agente/redireccionar")
                .request()
                .put(Entity.json(redireccionDTO));
    }

    public Response metricasTiempoDrools(String payload) {
        log.info("Correspondencia - [trafic] -metricas de Tiempo por Tipologia Regla: " + droolsEndpoint);

        WebTarget wt = ClientBuilder.newClient().target(droolsEndpoint);
        return wt.path("/regla")
                .request()
                .header("Authorization", droolsAuthToken)
                .header("X-KIE-ContentType", "json")
                .header("Content-Type", "application/json")
                .post(Entity.json(payload));
    }

    public Response verificarRedireccionesDrools(String payload) {
        log.info("Correspondencia - [trafic] - verificar redirecciones Regla: " + droolsEndpoint);
        log.error("DROOLS TOKEN: " + droolsAuthToken);

        WebTarget wt = ClientBuilder.newClient().target(droolsEndpoint);
        return wt.path("/redireccion")
                .request()
                .header("Authorization", droolsAuthToken)
                .header("X-KIE-ContentType", "json")
                .header("Content-Type", "application/json")
                .post(Entity.json(payload));
    }

    public Response registrarObservacion(PpdTrazDocumentoDTO observacion) {
        log.info("Correspondencia - [trafic] -registrar observacion: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/documento-web-api/documento/registrar-observacion")
                .request()
                .post(Entity.json(observacion));
    }

    public Response obtnerContantesPorCodigo(String codigos) {
        log.info("Correspondencia - [trafic] -registrar observacion: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        WebTarget target = wt.path("/constantes-web-api/constantes").queryParam("codigos", codigos);

        return target.request().get();
    }

    public Response listarDistribucion(String fechaIni, String fechaFin, String codDependencia, String codTipoDoc, String nroRadicado) {
        log.info("Correspondencia - [trafic] - listar distribucion: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);

        WebTarget target = wt.path("/correspondencia-web-api/correspondencia/listar-distribucion")
                .queryParam("fecha_ini", fechaIni)
                .queryParam("fecha_fin", fechaFin)
                .queryParam("cod_dependencia", codDependencia)
                .queryParam("cod_tipologia_documental", codTipoDoc)
                .queryParam("nro_radicado", nroRadicado);
        return target.request().get();
    }

    public Response listarPlanillas(String nroPlanilla) {
        log.info("Correspondencia - [trafic] - listar planillas: " + nroPlanilla);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);

        WebTarget target = wt.path("/planillas-web-api/planillas/" + nroPlanilla);

        return target.request().get();
    }

    public Response generarPlantilla(PlanillaDTO planilla) {
        log.info("Correspondencia - [trafic] - generar planilla: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/planillas-web-api/planillas")
                .request()
                .post(Entity.json(planilla));
    }

    public Response cargarPlantilla(PlanillaDTO planilla) {
        log.info("Correspondencia - [trafic] - generar planilla: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/planillas-web-api/planillas")
                .request()
                .put(Entity.json(planilla));
    }

    public Response exportarPlanilla(String nroPlanilla, String formato) {
        log.info("Correspondencia - [trafic] - exportar planilla: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/planillas-web-api/planillas/" + nroPlanilla + "/" + formato)
                .request()
                .get();
    }

    public Response restablecerCorrespondenciaEntrada(String idproceso, String idtarea) {
        log.info("Correspondencia - [trafic] - Invocando Servicio Remoto SalvarCorrespondenciaEntrada: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/tarea-web-api/tarea/" + idproceso + "/" + idtarea)
                .request().get();
    }

    public Response salvarCorrespondenciaEntrada(TareaDTO tarea) {
        log.info("Correspondencia - [trafic] - generar planilla: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/tarea-web-api/tarea")
                .request()
                .post(Entity.json(tarea));
    }
}
