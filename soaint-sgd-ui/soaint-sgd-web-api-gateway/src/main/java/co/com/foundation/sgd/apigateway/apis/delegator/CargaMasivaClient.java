package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import lombok.extern.log4j.Log4j2;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@ApiDelegator
@Log4j2
public class CargaMasivaClient {
    @Value("${backapi.cargamasiva.endpoint.url}")
    private String endpoint = "";

    public CargaMasivaClient() {
        super();
    }

    public Response listCargaMasiva() {
        log.info("Carga Masiva - [trafic] - listing Carga Masiva with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/listadocargamasiva")
                .request()
                .get();
    }

    public Response listEstadoCargaMasiva() {
        log.info("Carga Masiva - [trafic] - listing Carga Masiva with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/estadocargamasiva")
                .request()
                .get();
    }

    public Response listEstadoCargaMasivaDadoId(String id) {
        log.info("Carga Masiva - [trafic] - listing Carga Masiva dado Id with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/estadocargamasiva/"+id)
                .request()
                .get();
    }

    public Response cargarDocumento(InputPart part, String codigoSede, String codigoDependencia) {
        log.info("Carga Masiva - [trafic] - Subiendo fichero de carga masiva: " .concat(endpoint) );
        WebTarget wt = ClientBuilder.newClient().target(endpoint);

        MultipartFormDataOutput multipart = new MultipartFormDataOutput();

        InputStream inputStream = null;
        try {
            inputStream = part.getBody(InputStream.class, null);
            String result = new BufferedReader(new InputStreamReader(inputStream))
                    .lines().collect(Collectors.joining("\n"));
            log.info("Obteniendo documento....\r\n".concat(result));
        } catch (IOException e) {
            log.error("Se ha generado un error del tipo IO:", e);
        }
        multipart.addFormData("file", inputStream, MediaType.APPLICATION_OCTET_STREAM_TYPE);

        GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(multipart) {
        };

        return wt.path("/cargar-fichero")
                .request()
                .post(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE));
    }


}
