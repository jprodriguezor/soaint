package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.DigitalizarDocumentoClient;
import co.com.foundation.sgd.apigateway.apis.delegator.ECMClient;
import co.com.foundation.sgd.apigateway.apis.delegator.ECMUtils;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import lombok.extern.log4j.Log4j2;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Path("/digitalizar-documento-gateway-api")
@Log4j2
public class DigitalizarDocumentoGatewayApi {

    @Autowired
    private DigitalizarDocumentoClient digitalizarDocumentoClient;

    @Autowired
    private ECMClient client;


    public DigitalizarDocumentoGatewayApi() {
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @POST
    @Path("/{tipoComunicacion}/{fileName}/{principalFileName}/{sede}/{dependencia}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response digitalizar(@PathParam("tipoComunicacion") String tipoComunicacion, @PathParam("fileName") String fileName,
                                @PathParam("principalFileName") String principalFileName, @PathParam("sede") String sede,
                                @PathParam("dependencia") String dependencia, MultipartFormDataInput file) {

        log.info("ProduccionDocumentalGatewayApi - [content] : ");
        List<String> ecmIds = new ArrayList<>();
        Map<String,InputPart> files = ECMUtils.findFiles(file);

        /* Subida del fichero principal */
        InputPart parent = files.get(principalFileName);
        Response response = client.uploadDocument(sede, dependencia, principalFileName, parent, "");

        MensajeRespuesta parentResponse = response.readEntity(MensajeRespuesta.class); files.remove(fileName);
        if (response.getStatus() == HttpStatus.OK.value() && "0000".equals(parentResponse.getCodMensaje())){

            ecmIds.add(parentResponse.getMensaje());
            ecmIds.addAll(client.uploadDocumentsAsociates(parentResponse.getMensaje(), files, sede, dependencia));

            return Response.status(Response.Status.OK).entity(ecmIds).build();
        }
        return response;
    }

    @GET
    @Path("/obtener-documento/{idDocumento}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response constantes(@PathParam("idDocumento") String idDocumento) {
        log.info("DigitalizarDocumentoGatewayApi - [trafic] - obteniendo Documento desde el ecm: " + idDocumento);
        Response response = client.findByIdDocument(idDocumento);
        InputStream responseObject = response.readEntity(InputStream.class);
//        response.ok(responseObject).header ("Content-Type", "application/pdf");
        return Response.status(Response.Status.OK).entity(responseObject).build();
    }

    @GET
    @Path("/obtenerdocumentosasociados/{idDocumento}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response obtenerdocumentosasociados(@PathParam("idDocumento") String idDocumento) {
        log.info("DigitalizarDocumentoGatewayApi - [trafic] - obteniendo Documento asociados desde el ecm: " + idDocumento);
       return client.findDocumentosAsociados(idDocumento);
    }
}
