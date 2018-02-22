package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.ECMClient;
import co.com.foundation.sgd.apigateway.apis.delegator.ECMUtils;
import co.com.foundation.sgd.apigateway.apis.delegator.ProduccionDocumentalClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.canonical.ecm.MetadatosDocumentosDTO;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;

@Path("/produccion-documental-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class ProduccionDocumentalGatewayApi {

    private static final String CONTENT = "ProduccionDocumentalGatewayApi - [content] : ";

    @Autowired
    private ProduccionDocumentalClient client;

    @Autowired
    private ECMClient clientECM;


    public ProduccionDocumentalGatewayApi() {
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @POST
    @Path("/ejecutar-proyeccion-multiple")
    @JWTTokenSecurity
    public Response ejecutarProyeccionMultiple(EntradaProcesoDTO entrada) {
        Response response = client.ejecutarProyeccionMultiple(entrada);
        String responseObject = response.readEntity(String.class);
        if (response.getStatus() == HttpStatus.NO_CONTENT.value()) {
            return Response.status(HttpStatus.OK.value()).entity(new ArrayList<>()).build();
        }
        log.info("\n\rENDED");
        return Response.status(response.getStatus()).entity(responseObject).build();
    }

    @GET
    @Path("/datos-documento/{nro_radicado}")
    @JWTTokenSecurity
    public Response obtenerDatosDocumento(@PathParam("nro_radicado") String nro_radicado) {
        Response response = client.obtenerDatosDocumentoPorNroRadicado(nro_radicado);
        String responseObject = response.readEntity(String.class);
        if (response.getStatus() == HttpStatus.NO_CONTENT.value()) {
            return Response.status(HttpStatus.OK.value()).entity(new ArrayList<>()).build();
        }
        log.info("\n\rENDED");
        return Response.status(response.getStatus()).entity(responseObject).build();
    }

    @POST
    @Path("/versionar-documento")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response versionarDocumento(@RequestParam("nombreDocumento") String nombreDocumento, @RequestParam("idDocumento") String idDocumento,
                                @RequestParam("sede") String sede, @RequestParam("dependencia") String dependencia,
                                @RequestParam("nroRadicado") String nroRadicado, MultipartFormDataInput formDataInput) {
        log.info("ProduccionDocumentalGatewayApi - [content] : Subir version documento");

        Response response = null;

        Map<String, List<InputPart>> uploadForm = formDataInput.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("documento");

        for (InputPart inputPart : inputParts) {
            try {
                //convert the uploaded file to inputstream
                InputStream inputStream = inputPart.getBody(InputStream.class,null);
                byte [] bytes = IOUtils.toByteArray(inputStream);

                response = clientECM.uploadVersionDocumento(idDocumento,nombreDocumento,sede,dependencia,bytes);
            } catch (IOException ioe) {
                ioe.printStackTrace();
                return Response.status(200).entity(ioe.getMessage()).build();
            }
        }

        return response;
    }


}
