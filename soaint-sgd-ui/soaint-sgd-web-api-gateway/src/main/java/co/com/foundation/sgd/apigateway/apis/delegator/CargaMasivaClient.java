package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.foundation.sgd.utils.SystemParameters;
import lombok.extern.log4j.Log4j2;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;

@ApiDelegator
@Log4j2
public class CargaMasivaClient {

    private String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_CARGAMASIVA_ENDPOINT_URL);
    
    private WebTarget wt;

    public CargaMasivaClient() {
        super();
        wt = ClientBuilder.newClient().target(endpoint);
    }

    public Response listCargaMasiva() {
        log.info("Carga Masiva - [trafic] - listing Carga Masiva with endpoint: " + endpoint);
        return wt.path("/listadocargamasiva")
                .request()
                .get();
    }

    public Response listEstadoCargaMasiva() {
        log.info("Carga Masiva - [trafic] - listing Carga Masiva with endpoint: " + endpoint);
        return wt.path("/estadocargamasiva")
                .request()
                .get();
    }

    public Response listEstadoCargaMasivaDadoId(String id) {
        log.info("Carga Masiva - [trafic] - listing Carga Masiva dado Id with endpoint: " + endpoint);
        return wt.path("/estadocargamasiva/" + id)
                .request()
                .get();
    }

    public Response cargarDocumento(InputPart part, String codigoSede, String codigoDependencia, String codfunRadica, String fileName) {
        log.info("Carga Masiva - [trafic] - Subiendo fichero de carga masiva: ".concat(endpoint));

        MultipartFormDataOutput multipart = new MultipartFormDataOutput();

        InputStream inputStream = null;
        try {
            inputStream = part.getBody(InputStream.class, null);
            multipart.addFormData("file", inputStream, MediaType.APPLICATION_OCTET_STREAM_TYPE, fileName);
            multipart.addFormData("codigoSede", codigoSede, MediaType.TEXT_PLAIN_TYPE);
            multipart.addFormData("codigoDependencia", codigoDependencia, MediaType.TEXT_PLAIN_TYPE);
            multipart.addFormData("codfunRadica", codigoDependencia, MediaType.TEXT_PLAIN_TYPE);

        } catch (IOException e) {
            log.error("Se ha generado un error del tipo IO:", e);
        }


        GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(multipart) {
        };

        return wt.path("/cargar-fichero")
                .request()
                .post(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE));
    }


}
