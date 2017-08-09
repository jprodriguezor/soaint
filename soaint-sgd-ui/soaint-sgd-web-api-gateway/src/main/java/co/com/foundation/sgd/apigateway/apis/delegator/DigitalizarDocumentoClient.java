package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;

import com.sun.jersey.multipart.MultiPart;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

@ApiDelegator
public class DigitalizarDocumentoClient {

    @Value("${backapi.ecm.service.endpoint.url}")
    private String endpoint = "";

    public DigitalizarDocumentoClient() {
        super();
    }

    public Response digitalizar(InputStream pdf) {
        System.out.println("Municipios - [trafic] - listing Municipios with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);

        MultiPart multiPart = new MultiPart();
        multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);
//        multiPart.bodyPart(tipoComunicacion, MediaType.TEXT_PLAIN_TYPE);
//        multiPart.bodyPart(nombreDocumento, MediaType.TEXT_PLAIN_TYPE);
        multiPart.bodyPart(pdf, MediaType.MULTIPART_FORM_DATA_TYPE);

//        multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);
//        FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file",
//                convertFile(fileInputStream),
//                MediaType.APPLICATION_OCTET_STREAM_TYPE);
//        multiPart.bodyPart(objMultiPart);

        return wt.path("/ecm/subirDocumentoECM")
                .request()
                .post(Entity.entity(multiPart, multiPart.getMediaType()));
    }

}
