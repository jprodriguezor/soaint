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

    public Response iniciar(String idProceso) {
        System.out.println("Pais - [trafic] - listing Pais with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        EntradaProcesoDTO entradaProcesoDTO = new EntradaProcesoDTO();
        entradaProcesoDTO.setIdDespliegue("co.com.foundation.bpm.poc:pagos-empresariales-bpm-poc:1.0.0-SNAPSHOT");
        entradaProcesoDTO.setIdProceso(idProceso);
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

}
