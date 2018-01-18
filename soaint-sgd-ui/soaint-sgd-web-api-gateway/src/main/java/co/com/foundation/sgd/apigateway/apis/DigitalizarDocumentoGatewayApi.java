package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.DigitalizarDocumentoClient;
import co.com.soaint.foundation.canonical.ui.DigitalizarDocumentoDTO;
import lombok.extern.log4j.Log4j2;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
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

    public DigitalizarDocumentoGatewayApi() {
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @POST
    @Path("/{tipoComunicacion}/{fileName}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response digitalizar(@PathParam("tipoComunicacion") String tipoComunicacion, @PathParam("fileName") String fileName, MultipartFormDataInput file) {

        List<String> ecmIds = new ArrayList<>();
        log.info("DigitalizarDocumentoGatewayApi - [content] : ");
        file.getFormDataMap().forEach((key, parts) -> parts.forEach((part) -> {
            String fileAuxName = digitalizarDocumentoClient.getPartFileName(part);
            Response response = digitalizarDocumentoClient.digitalizar(part, fileName, tipoComunicacion);
            if (response.getStatus() == HttpStatus.OK.value())
                ecmIds.add(response.readEntity(String.class));
        }));
        log.info("DigitalizarDocumentoGatewayApi - [content] : " + ecmIds);
        if (!ecmIds.isEmpty()) {
            DigitalizarDocumentoDTO docs = new DigitalizarDocumentoDTO(ecmIds);

            return Response.status(Response.Status.OK).entity(docs).build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }


    @GET
    @Path("/obtener-documento/{idDocumento}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response constantes(@PathParam("idDocumento") String idDocumento) {
        log.info("DigitalizarDocumentoGatewayApi - [trafic] - obteniendo Documento desde el ecm: " + idDocumento);
        Response response = digitalizarDocumentoClient.obtenerDocumento(idDocumento);
        InputStream responseObject = response.readEntity(InputStream.class);
//        response.ok(responseObject).header ("Content-Type", "application/pdf");
        return Response.status(Response.Status.OK).entity(responseObject).build();
    }
}
