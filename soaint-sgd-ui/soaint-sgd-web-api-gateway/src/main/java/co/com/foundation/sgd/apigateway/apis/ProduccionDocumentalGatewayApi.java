package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.ProduccionDocumentalClient;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
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
    @Path("/{nombreDocumento}/{idDocPadre}/{sede}/{dependencia}/{fileName}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response digitalizar(@PathParam("nombreDocumento") String nombreDocumento,
                                @PathParam("sede") String sede,  @PathParam("dependencia") String dependencia,
                                @PathParam("fileName") String fileName, MultipartFormDataInput file) {
        List<String> ecmIds = new ArrayList<String>();
        log.info("ProduccionDocumentalGatewayApi - [content] : ");

        Map<String,InputPart> files = new HashMap<String, InputPart>();
        Collection<List<InputPart>> inputParts = file.getFormDataMap().values();
        inputParts.stream().forEach(parts -> parts.forEach(part -> {
            String name = findName(part); if(!"".equals(name)) files.put(name,part);
        }));

        InputPart parent = files.get(nombreDocumento);
        Response response = client.producirDocumento(nombreDocumento, "undefined", sede, dependencia, fileName, parent);
        ResponseECM parentResponse = response.readEntity(ResponseECM.class);
        if (response.getStatus() == HttpStatus.OK.value() && "0000".equals(parentResponse.getCodMensaje())){
            Map<String,String> docs = new HashMap<String,String>();
            files.forEach((key, part) -> {
                if(!nombreDocumento.equalsIgnoreCase(key)) {
                    String parentId = parentResponse.getMensaje();
                    Response _response = client.producirDocumento(
                            nombreDocumento, parentResponse.getMensaje(), sede, dependencia, fileName, part);
                    ResponseECM asociadoResponse = _response.readEntity(ResponseECM.class);
                    if (_response.getStatus() == HttpStatus.OK.value()
                            && "0000".equals(asociadoResponse.getCodMensaje())) {
                        docs.put("id-" + parentResponse.getMensaje(),nombreDocumento);
                    }
                }
            });
            return Response.status(Response.Status.OK).entity(docs).build();
        }
        return response;
    }

    @Data
    private final class ResponseECM {
           private String codMensaje;
           private String mensaje;
    }

    public String findName(InputPart part) {
        String fileName = "";
        MultivaluedMap<String, String> headers = part.getHeaders();
        String[] contentDispositionHeader = headers.getFirst("Content-Disposition").split(";");
        for (String name : contentDispositionHeader) {
            if ((name.trim().startsWith("filename"))) {
                String[] tmp = name.split("=");
                fileName = tmp[1].trim().replaceAll("\"", "");
            }
        }
        return fileName;
    }

}
