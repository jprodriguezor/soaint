package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.DigitalizarDocumentoClient;
import co.com.soaint.foundation.canonical.ui.DigitalizarDocumentoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Path("/digitalizar-documento-gateway-api")
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
        //TODO: add trafic log
        List<String> ecmIds = new ArrayList<>();
        System.out.println("DigitalizarDocumentoGatewayApi - [content] : ");
        file.getFormDataMap().forEach((key, parts) -> {
            parts.forEach((part) -> {
                /*String fileName = "";
                String[] contentDispositionHeader = part.getHeaders().getFirst("Content-Disposition").split(";");
                for (String name : contentDispositionHeader) {
                    if ((name.trim().startsWith("filename"))) {
                        String[] tmp = name.split("=");
                        fileName = tmp[1].trim().replaceAll("\"", "");
                    }
                }*/

                Response response = digitalizarDocumentoClient.digitalizar(part, fileName, tipoComunicacion);
                ecmIds.add(response.readEntity(String.class));
            });
        });
        System.out.println("DigitalizarDocumentoGatewayApi - [content] : " + ecmIds);

        DigitalizarDocumentoDTO docs = new DigitalizarDocumentoDTO(ecmIds);

        return Response.status(Response.Status.OK).entity(docs).build();
    }

}
