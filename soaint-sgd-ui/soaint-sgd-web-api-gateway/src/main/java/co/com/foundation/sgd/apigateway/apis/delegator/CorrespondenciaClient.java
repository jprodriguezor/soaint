package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.soaint.foundation.canonical.correspondencia.AgentesDTO;
import co.com.soaint.foundation.canonical.correspondencia.AsignacionesDTO;
import co.com.soaint.foundation.canonical.correspondencia.ComunicacionOficialDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@ApiDelegator
@Log4j2
public class CorrespondenciaClient {


    @Value("${backapi.endpoint.url}")
    private String endpoint = "";

    @Value("${backapi.ecm.service.endpoint.url}")
    private String ecmEndpoint = "";

    @Value("${backapi.drools.service.endpoint.url}")
    private String droolsEndpoint = "";

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

    public Response listarComunicaciones(String fechaIni, String fechaFin, String codDependencia, String codEstado) {
        log.info("Correspondencia - [trafic] - radicar Correspondencia with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/correspondencia-web-api/correspondencia")
                .queryParam("fecha_ini", fechaIni)
                .queryParam("fecha_fin", fechaFin)
                .queryParam("cod_dependencia", codDependencia)
                .queryParam("cod_estado", codEstado)
                .request()
                .get();
    }

    public Response asignarComunicaciones(AsignacionesDTO asignacionesDTO) {
        log.info("Correspondencia - [trafic] - asignar Comunicaciones with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/asignacion-web-api/asignacion")
                .request()
                .post(Entity.json(asignacionesDTO));
    }

    public Response redireccionarComunicaciones(AgentesDTO agentesDTO) {
        log.info("Correspondencia - [trafic] - redireccionar Comunicaciones with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/asignacion-web-api/asignacion/redireccionar")
                .request()
                .put(Entity.json(agentesDTO));
    }

    public Response metricasTiempoDrools(String payload) {
        log.info("Correspondencia - [trafic] -metricas de Tiempo por Tipologia Regla: " + droolsEndpoint);

        WebTarget wt = ClientBuilder.newClient().target(droolsEndpoint);
        return wt.request()
                .header("Authorization","Basic a3Jpc3Y6a3Jpc3Y=")
                .header("X-KIE-ContentType", "json")
                .header("Content-Type", "application/json")
                .post(Entity.json(payload));
    }
}
