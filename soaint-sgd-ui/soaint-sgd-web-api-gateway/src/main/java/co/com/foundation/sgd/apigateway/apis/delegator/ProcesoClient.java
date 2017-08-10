package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@ApiDelegator
public class ProcesoClient {

    @Value("${backapi.enterprise.service.endpoint.url}")
    private String endpoint = "";

    public ProcesoClient() {
        super();
    }

    public Response list() {
        System.out.println("Pais - [trafic] - listing Pais with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        EntradaProcesoDTO entradaProcesoDTO = new EntradaProcesoDTO();
        return wt.path("/bpm/proceso/listar")
                .request()
                .post(Entity.json(entradaProcesoDTO));
    }

    public Response iniciar(EntradaProcesoDTO entradaProcesoDTO) {
        System.out.println("Pais - [trafic] - listing Pais with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/bpm/proceso/iniciar")
                .request()
                .post(Entity.json(entradaProcesoDTO));
    }

    public Response iniciarManual(EntradaProcesoDTO entradaProcesoDTO) {
        System.out.println("Pais - [trafic] - listing Pais with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/bpm/proceso/iniciar/manual")
                .request()
                .post(Entity.json(entradaProcesoDTO));
    }

    public Response listarPorIdProceso(EntradaProcesoDTO entrada) {
        System.out.println("Pais - [trafic] - listing Pais with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        EntradaProcesoDTO entradaProcesoDTO = entrada;
//        entradaProcesoDTO.setIdDespliegue("co.com.foundation.bpm.poc:pagos-empresariales-bpm-poc:1.0.0-SNAPSHOT");
        return wt.path("/bpm/tareas/listar/estados-instancia/")
                .request()
                .post(Entity.json(entradaProcesoDTO));
    }

    public Response listarTareas(EntradaProcesoDTO entrada) {
        System.out.println("Pais - [trafic] - listing Pais with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        EntradaProcesoDTO entradaProcesoDTO = entrada;
        return wt.path("/bpm/tareas/listar/estados/")
                .request()
                .post(Entity.json(entradaProcesoDTO));
    }

    public Response iniciarTarea(EntradaProcesoDTO entrada) {
        System.out.println("Task - [trafic] - start Task with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/bpm/tareas/iniciar")
                .request()
                .post(Entity.json(entrada));
    }

    public Response completarTarea(EntradaProcesoDTO entrada) {
        System.out.println("Task - [trafic] - start Task with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/bpm/tareas/completar")
                .request()
                .post(Entity.json(entrada));
    }

    public Response reasignarTarea(EntradaProcesoDTO entrada) {
        System.out.println("Task - [trafic] - start Task with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/bpm/tareas/reasignar")
                .request()
                .post(Entity.json(entrada));
    }

    public Response listarIntanciasProceso() {
        System.out.println("Task - [trafic] - start Task with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        EntradaProcesoDTO entradaProcesoDTO = new EntradaProcesoDTO();
        entradaProcesoDTO.setIdProceso("proceso.correspondencia-entrada");
        return wt.path("/bpm/proceso/listar-instancias/")
                .request()
                .post(Entity.json(entradaProcesoDTO));
    }


}
