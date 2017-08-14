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
import java.io.IOException;
import java.io.InputStream;

@ApiDelegator
@Log4j2
public class DigitalizarDocumentoClient {


    @Value("${backapi.ecm.service.endpoint.url}")
    private String endpoint = "";

    public DigitalizarDocumentoClient() {
        super();
    }

    public Response digitalizar(InputPart part, String fileName, String tipoComunicacion) {
        log.info("Municipios - [trafic] - listing Municipios with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);

        MultipartFormDataOutput multipart = new MultipartFormDataOutput();

        InputStream inputStream = null;
        try {
            inputStream = part.getBody(InputStream.class, null);
        } catch (IOException e) {
            log.error("Se ha generado un error del tipo IO:",e);
        }
        multipart.addFormData("documento", inputStream, MediaType.MULTIPART_FORM_DATA_TYPE);


        GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(multipart) {
        };

        return wt.path("/ecm/subirDocumentoECM/" + fileName + "/" + tipoComunicacion)
                .request()
                .post(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE));
    }

}
