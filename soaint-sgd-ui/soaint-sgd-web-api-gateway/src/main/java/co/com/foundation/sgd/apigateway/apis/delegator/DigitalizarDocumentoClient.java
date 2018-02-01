package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.foundation.sgd.utils.SystemParameters;
import lombok.extern.log4j.Log4j2;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;

@ApiDelegator
@Log4j2
public class DigitalizarDocumentoClient {

    private String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ECM_SERVICE_ENDPOINT_URL);

    public DigitalizarDocumentoClient() {
        super();
    }

    public Response digitalizarPrincipal(InputPart part, String fileName, String codSede, String codDependencia) {
        log.info("Municipios - [trafic] - listing Municipios with endpointEcm: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);

        MultipartFormDataOutput multipart = new MultipartFormDataOutput();

        InputStream inputStream = null;
        try {
            inputStream = part.getBody(InputStream.class, null);
        } catch (IOException e) {
            log.error("Se ha generado un error del tipo IO:", e);
        }
        multipart.addFormData("documento", inputStream, MediaType.MULTIPART_FORM_DATA_TYPE);


        GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(multipart) {
        };

        return wt.path("/subirDocumentoRelacionECM/" + fileName + "/" + codSede + "/" + codDependencia)
                .request()
                .post(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE));
    }

    public Response digitalizarAnexo(InputPart part, String fileName, String codSede, String codDependencia, String idDocPadre) {
        log.info("Municipios - [trafic] - digitalizar anexo with endpointEcm: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);

        MultipartFormDataOutput multipart = new MultipartFormDataOutput();

        InputStream inputStream = null;
        try {
            inputStream = part.getBody(InputStream.class, null);
        } catch (IOException e) {
            log.error("Se ha generado un error del tipo IO:", e);
        }
        multipart.addFormData("documento", inputStream, MediaType.MULTIPART_FORM_DATA_TYPE);


        GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(multipart) {
        };

        return wt.path("/subirDocumentoRelacionECM/" + fileName + "/" + codSede + "/" + codDependencia + "/" + idDocPadre)
                .request()
                .post(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE));
    }

    public Response obtenerDocumento(String idDocumento) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/descargarDocumentoECM/")
                .queryParam("identificadorDoc", idDocumento)
                .request()
                .get();
    }

    public String getPartFileName(InputPart part) {
        String FileName = "";
        MultivaluedMap<String, String> headers = part.getHeaders();
        String[] contentDispositionHeader = headers.getFirst("Content-Disposition").split(";");
        for (String name : contentDispositionHeader) {
            if ((name.trim().startsWith("filename"))) {
                String[] tmp = name.split("=");
                FileName = tmp[1].trim().replaceAll("\"", "");
            }
        }
        return FileName;
    }

}
