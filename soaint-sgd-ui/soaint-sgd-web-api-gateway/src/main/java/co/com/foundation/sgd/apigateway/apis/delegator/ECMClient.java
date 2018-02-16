package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.foundation.sgd.utils.SystemParameters;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import lombok.extern.log4j.Log4j2;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@ApiDelegator
@Log4j2
public class ECMClient {

    private String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ECM_SERVICE_ENDPOINT_URL);

    public ECMClient() {
        super();
    }

    public Response uploadDocument(String sede, String dependencia, String tipoComunicacion, String fileName, InputPart part, String parentId) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);

        MultipartFormDataOutput multipart = new MultipartFormDataOutput();
        InputStream inputStream = null;
        try {
            inputStream = part.getBody(InputStream.class, null);
        } catch (IOException e) {
            log.error("Se ha generado un error del tipo IO:", e);
        }
        multipart.addFormData("documento", inputStream, MediaType.MULTIPART_FORM_DATA_TYPE);
        GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(multipart) {};

        log.info("/subirDocumentoRelacionECM/" + fileName + "/" + sede + "/" + dependencia);

        return wt.path("/subirDocumentoRelacionECM/" + fileName + "/" + sede + "/" + dependencia  + "/" + tipoComunicacion +
                (parentId == null || "".equals(parentId) ? "" : "/" + parentId ))
                .request().post(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE));
    }

    public List<MensajeRespuesta> uploadDocumentsAsociates(String parentId, Map<String,InputPart> files, String sede, String dependencia, String tipoComunicacion){
        List<MensajeRespuesta> mensajeRespuestas = new ArrayList<>();
        try {
            files.forEach((key, part) -> {
                Response _response = this.uploadDocument(sede, dependencia, tipoComunicacion, key, part, parentId);
                MensajeRespuesta asociadoResponse = _response.readEntity(MensajeRespuesta.class);
                mensajeRespuestas.add(asociadoResponse);
            });
        }catch (Exception e){
            log.error("Se ha generado un error al subir los documentos asociados: ", e);
        }
        return mensajeRespuestas;
    }


    public Response findByIdDocument(String idDocumento) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/descargarDocumentoECM/")
                .queryParam("identificadorDoc", idDocumento)
                .request()
                .get();
    }

    public Response findDocumentosAsociados(String idDocumento) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/obtenerDocumentosAdjuntosECM/" + idDocumento).request().get();
    }

}