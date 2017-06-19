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
        entradaProcesoDTO.setIdDespliegue("co.com.foundation.bpm.poc:pagos-empresariales-bpm-poc:1.0.0-SNAPSHOT");
        entradaProcesoDTO.setIdProceso("pagos.pago-impuesto-vehiculo");
        entradaProcesoDTO.setUsuario("krisv");
        entradaProcesoDTO.setPass("krisv");
        return wt.path("/bpm/proceso/listar")
                .request()
                .post(Entity.json(entradaProcesoDTO));
    }

    public Response iniciar(EntradaProcesoDTO entradaProcesoDTO) {
        System.out.println("Pais - [trafic] - listing Pais with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        entradaProcesoDTO.setUsuario("krisv");
        entradaProcesoDTO.setPass("krisv");
        return wt.path("/bpm/proceso/iniciar")
                .request()
                .post(Entity.json(entradaProcesoDTO));
    }

    public Response listarPorIdProceso(EntradaProcesoDTO entrada) {
        System.out.println("Pais - [trafic] - listing Pais with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        EntradaProcesoDTO entradaProcesoDTO = entrada;
        entradaProcesoDTO.setIdDespliegue("co.com.foundation.bpm.poc:pagos-empresariales-bpm-poc:1.0.0-SNAPSHOT");
        entradaProcesoDTO.setUsuario("krisv");
        entradaProcesoDTO.setPass("krisv");
        return wt.path("/bpm/tareas/listar/estados-instancia/")
                .request()
                .post(Entity.json(entradaProcesoDTO));
    }

    public Response listarTareas(EntradaProcesoDTO entrada) {
        System.out.println("Pais - [trafic] - listing Pais with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        EntradaProcesoDTO entradaProcesoDTO = entrada;
        //TODO remove next line
        entradaProcesoDTO.setIdProceso("proceso.correspondencia-entrada");
        entradaProcesoDTO.setUsuario("krisv");
        entradaProcesoDTO.setPass("krisv");
        return wt.path("/bpm/tareas/listar/estados/")
                .request()
                .post(Entity.json(entradaProcesoDTO));
    }

    public Response iniciarTarea(EntradaProcesoDTO entrada) {
        System.out.println("Task - [trafic] - start Task with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        entrada.setUsuario("krisv");
        entrada.setPass("krisv");
        return wt.path("/bpm/tareas/iniciar")
                .request()
                .post(Entity.json(entrada));
    }

    public Response listarIntanciasProceso() {
        System.out.println("Task - [trafic] - start Task with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        EntradaProcesoDTO entradaProcesoDTO = new EntradaProcesoDTO();
        entradaProcesoDTO.setIdProceso("proceso.correspondencia-entrada");
        entradaProcesoDTO.setUsuario("krisv");
        entradaProcesoDTO.setPass("krisv");
        return wt.path("/bpm/proceso/listar-instancias/")
                .request()
                .post(Entity.json(entradaProcesoDTO));
    }


}
