package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.DigitalizarDocumentoClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import com.sun.jersey.core.header.ContentDisposition;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.jboss.resteasy.plugins.providers.multipart.MultipartInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileInputStream;
import java.io.InputStream;

@Path("/digitalizar-documento-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes("multipart/form-data")
public class DigitalizarDocumentoGatewayApi {

    @Autowired
    private DigitalizarDocumentoClient digitalizarDocumentoClient;

    public DigitalizarDocumentoGatewayApi() {
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @POST
    @Path("/")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response digitalizar(@FormDataParam("file") InputStream documento) {
        //TODO: add trafic log
        System.out.println("DigitalizarDocumentoGatewayApi - [content] : ");
        Response response = digitalizarDocumentoClient.digitalizar(documento);
//        String responseContent = response.readEntity(String.class);

//        System.out.println("DigitalizarDocumentoGatewayApi - [content] : " + responseContent);

        return Response.ok("ok").build();
    }

}
