package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.ECMClient;
import co.com.foundation.sgd.apigateway.apis.delegator.ECMUtils;
import co.com.foundation.sgd.apigateway.apis.delegator.ProduccionDocumentalClient;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import lombok.Data;
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
    //@JWTTokenSecurity
    public Response ejecutarProyeccionMultiple(EntradaProcesoDTO entrada) {
        Response response = client.ejecutarProyeccionMultiple(entrada);
        String responseObject = response.readEntity(String.class);
        if (response.getStatus() == HttpStatus.NO_CONTENT.value()) {
            return Response.status(HttpStatus.OK.value()).entity(new ArrayList<>()).build();
        }
        log.info("\n\rENDED");
        return Response.status(response.getStatus()).entity(responseObject).build();
    }

    @POST
    @Path("/adjuntar/documento/{sede}/{dependencia}/{fileName}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response digitalizar(@PathParam("sede") String sede,  @PathParam("dependencia") String dependencia,
                                @PathParam("fileName") String fileName, MultipartFormDataInput file) {
        log.info("ProduccionDocumentalGatewayApi - [content] : ");
        Map<String,InputPart> files = ECMUtils.findFiles(file);
        List<String> ecmIds = new ArrayList<>();
        /* Subida del fichero principal */
        InputPart parent = files.get(fileName);
        Response response = clientECM.uploadDocument(sede, dependencia, fileName, parent, "");
        MensajeRespuesta parentResponse = response.readEntity(MensajeRespuesta.class); files.remove(fileName);
        if (response.getStatus() == HttpStatus.OK.value() && "0000".equals(parentResponse.getCodMensaje())){
            /*ecmIds.add(parentResponse.getMensaje());
            ecmIds.addAll(clientECM.uploadDocumentsAsociates(parentResponse.getMensaje(), files, sede, dependencia));
            return Response.status(Response.Status.OK).entity(ecmIds).build();*/
        }
        return response;
    }

    @POST
    @Path("/subir-version/{fileName}/{sede}/{dependencia}/{tipo}/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadVersionDocumento(@PathParam("fileName") String fileName,  @PathParam("sede") String sede,
                                           @PathParam("dependencia") String dependencia, @PathParam("tipo") String tipo,
                                           @PathParam("id") String id, MultipartFormDataInput file) {
        log.info("ProduccionDocumentalGatewayApi - [content] : Subir Version Documento");
        Map<String,InputPart> files = ECMUtils.findFiles(file);
        List<String> ecmIds = new ArrayList<>();
        /* Subida del fichero principal */
        InputPart parent = files.get(fileName);
        return clientECM.uploadDocument(sede, dependencia, fileName, parent, "");
    }

}
