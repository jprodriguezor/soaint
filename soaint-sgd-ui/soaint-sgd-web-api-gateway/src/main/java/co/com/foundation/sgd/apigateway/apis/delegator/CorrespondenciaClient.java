package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.soaint.foundation.canonical.correspondencia.AgentesDTO;
import co.com.soaint.foundation.canonical.correspondencia.AsignacionesDTO;
import co.com.soaint.foundation.canonical.correspondencia.ComunicacionOficialDTO;
import com.sun.jersey.multipart.MultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@ApiDelegator
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
        System.out.println("Correspondencia - [trafic] - radicar Correspondencia with endpoint: " + endpoint);
//        comunicacionOficialDTO.getCorrespondencia().setCodSede("01");
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/correspondencia-web-api/correspondencia")
                .request()
                .post(Entity.json(comunicacionOficialDTO));
    }

    public File convertFile(InputStream inputStream) {
        final File tempFile;
        try {
            tempFile = File.createTempFile("tempfile", ".tmp");
            tempFile.deleteOnExit();
            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                IOUtils.copy(inputStream, out);
            }
            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Response upload(InputStream fileInputStream) {
        System.out.println("Correspondencia - [trafic] - radicar Correspondencia with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(ecmEndpoint);
        MultiPart multiPart = new MultiPart();
        multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);
        FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file",
                convertFile(fileInputStream),
                MediaType.APPLICATION_OCTET_STREAM_TYPE);
        multiPart.bodyPart(fileDataBodyPart);
        return wt.request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(multiPart, multiPart.getMediaType()));
    }

    public Response listarComunicaciones(String fechaIni, String fechaFin, String codDependencia, String codEstado) {
        System.out.println("Correspondencia - [trafic] - radicar Correspondencia with endpoint: " + endpoint);
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
        System.out.println("Correspondencia - [trafic] - asignar Comunicaciones with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/asignacion-web-api/asignacion")
                .request()
                .post(Entity.json(asignacionesDTO));
    }

    public Response redireccionarComunicaciones(AgentesDTO agentesDTO) {
        System.out.println("Correspondencia - [trafic] - redireccionar Comunicaciones with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/asignacion-web-api/asignacion/redireccionar")
                .request()
                .put(Entity.json(agentesDTO));
    }

    public Response metricasTiempoDrools(String payload) {
        System.out.println("Correspondencia - [trafic] -metricas de Tiempo por Tipologia Regla: " + droolsEndpoint);
        WebTarget wt = ClientBuilder.newClient().target(droolsEndpoint);
        return wt.request()
                .post(Entity.json(payload));
    }
}
