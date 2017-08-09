package co.com.foundation.sgd.apigateway.apis.delegator;
import co.com.foundation.sgd.infrastructure.ApiDelegator;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.springframework.beans.factory.annotation.Value;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApiDelegator
public class DigitalizarDocumentoClient {

    @Value("${backapi.ecm.service.endpoint.url}")
    private String endpoint = "";

    public DigitalizarDocumentoClient() {
        super();
    }

    public Response digitalizar(MultipartFormDataInput documento) {
        System.out.println("Municipios - [trafic] - listing Municipios with endpoint: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);

        MultipartFormDataOutput multipart = new MultipartFormDataOutput();
        multipart.addFormData("uploadedFile", documento, MediaType.MULTIPART_FORM_DATA_TYPE);

        return wt.path("/ecm/subirDocumentoECM")
                .request()
                .post(Entity.entity(multipart, MediaType.MULTIPART_FORM_DATA_TYPE));
    }

}
