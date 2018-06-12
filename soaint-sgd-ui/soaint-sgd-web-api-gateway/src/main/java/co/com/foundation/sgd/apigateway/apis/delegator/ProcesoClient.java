package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.foundation.sgd.utils.SystemParameters;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.correspondencia.ItemDevolucionDTO;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Cliente para servicios relacionados a los procesos del bpm
 */
@ApiDelegator
@Log4j2
@NoArgsConstructor
public class ProcesoClient {

    private String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ENTERPRISE_SERVICE_ENDPOINT_URL);

    //private Client client = ClientBuilder.newClient();

    public Response list() {
        log.info("Proccess - [trafic] - listing Proccess with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        EntradaProcesoDTO entradaProcesoDTO = new EntradaProcesoDTO();
        return wt.path("/bpm/proceso/listar")
                .request()
                .post(Entity.json(entradaProcesoDTO));
    }

    public Response iniciar(EntradaProcesoDTO entradaProcesoDTO) {
        log.info("Proccess - [trafic] - starting Proccess with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/bpm/proceso/iniciar")
                .request()
                .post(Entity.json(entradaProcesoDTO));
    }

    public Response iniciarTercero(EntradaProcesoDTO entradaProcesoDTO) {
        log.info("Proccess - [trafic] - starting Proccess with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/bpm/proceso/iniciar-tercero")
                .request()
                .post(Entity.json(entradaProcesoDTO));
    }
    public Response iniciarProcesoGestorDevoluciones(ItemDevolucionDTO itemDevolucion, EntradaProcesoDTO entradaProceso) {
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("numeroRadicado", itemDevolucion.getCorrespondencia().getNroRadicado());
        parametros.put("causalD", itemDevolucion.getCausalDevolucion());
        parametros.put("funDevuelve", itemDevolucion.getFunDevuelve());
        parametros.put("fechaVencimiento", itemDevolucion.getCorrespondencia().getFecVenGestion());
        parametros.put("idAgente", itemDevolucion.getAgente().getIdeAgente().toString());
        parametros.put("estadoFinal", itemDevolucion.getAgente().getCodEstado());
        parametros.put("codDependencia", itemDevolucion.getCorrespondencia().getCodDependencia());
        entradaProceso.setParametros(parametros);
        return this.iniciarTercero(entradaProceso);
    }

    public Response iniciarManual(EntradaProcesoDTO entradaProcesoDTO) {
        log.info("Proccess - [trafic] - manual starting Proccess with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/bpm/proceso/iniciar/manual")
                .request()
                .post(Entity.json(entradaProcesoDTO));
    }

    public Response listarPorIdProceso(EntradaProcesoDTO entrada) {
        log.info("Task - [trafic] - listing Tasks By Id Proccess with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        EntradaProcesoDTO entradaProcesoDTO = entrada;
        return wt.path("/bpm/tareas/listar/estados-instancia/")
                .request()
                .post(Entity.json(entradaProcesoDTO));
    }

    public Response listarPorUsuarioYPorIdProceso(EntradaProcesoDTO entrada) {
        log.info("Task - [trafic] - listing Tasks By Usuario y Id Proccess with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        EntradaProcesoDTO entradaProcesoDTO = entrada;
        return wt.path("/bpm/tareas/listar/asignado/")
                .request()
                .post(Entity.json(entradaProcesoDTO));
    }

    public Response listarTareas(EntradaProcesoDTO entrada) {
        log.info("Task - [trafic] - listing Task with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/bpm/tareas/listar/estados/")
                .request()
                .post(Entity.json(entrada));
    }

    public Response listarTareasCompletadas(EntradaProcesoDTO entrada) {
        log.info("Task - [trafic] - listing Task with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/bpm/tareas/listar/completadas")
                .request()
                .post(Entity.json(entrada));
    }

    public Response listarEstadisticasTareas(EntradaProcesoDTO entrada) {
        log.info("Listando Estadisticas de Tareas " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/bpm/tareas/listar/usuario")
                .request()
                .post(Entity.json(entrada));
    }

    public Response iniciarTarea(EntradaProcesoDTO entrada) {
        log.info("Task - [trafic] - start Task with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/bpm/tareas/iniciar")
                .request()
                .post(Entity.json(entrada));
    }

    public Response reservarTarea(EntradaProcesoDTO entrada) {
        log.info("Task - [trafic] - reserve Task with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/bpm/tareas/reservar")
                .request()
                .post(Entity.json(entrada));
    }

    public Response completarTarea(EntradaProcesoDTO entrada) {
        log.info("Task - [trafic] - complete Task with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/bpm/tareas/completar")
                .request()
                .post(Entity.json(entrada));
    }

    public Response abortarTarea(EntradaProcesoDTO entrada) {
        log.info("Task - [trafic] - abort Task with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/bpm/proceso/abortar")
                .request()
                .post(Entity.json(entrada));
    }

    public Response reasignarTarea(EntradaProcesoDTO entrada) {
        log.info("Task - [trafic] - reasign Task with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/bpm/tareas/reasignar")
                .request()
                .post(Entity.json(entrada));
    }

    public Response listarIntanciasProceso() {
        log.info("Task - [trafic] - list process instances with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        EntradaProcesoDTO entradaProcesoDTO = new EntradaProcesoDTO();
        entradaProcesoDTO.setIdProceso("proceso.correspondencia-entrada");
        return wt.path("/bpm/proceso/listar-instancias/")
                .request()
                .post(Entity.json(entradaProcesoDTO));
    }

    public Response obtenerVariablesTarea(EntradaProcesoDTO entradaProcesoDTO) {
        log.info("Task - [trafic] - get task variables with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/bpm/proceso/listar-variables")
                .request()
                .post(Entity.json(entradaProcesoDTO));
    }


}
