package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.TipoPlantillaClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import co.com.foundation.sgd.utils.PdfConverter;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/tipo-plantilla-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class TipoPlantillaApi {

    @Autowired
    private TipoPlantillaClient client;

    @Value("${locations.tiposplantilla.output}")
    private String location_output = "";

    public TipoPlantillaApi() {
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("/plantilla/{codClasificacion}/{estado}")
    @JWTTokenSecurity
    public Response get(@PathParam("codClasificacion") final String codClasificacion, @PathParam("estado") final String estado) {

        log.info("TiposPlantillaGatewayApi - [trafic] - listing TiposPlantilla");
        Response response = client.get(codClasificacion, estado);
        String responseContent = response.readEntity(String.class);
        log.info("TiposPlantillaGatewayApi - [content] : " + responseContent);

        return Response.status( response.getStatus() ).entity(responseContent).build();
    }

    @GET
    @Path("/obtener/{codClasificacion}")
    //@JWTTokenSecurity
    public Response read(@PathParam("codClasificacion") final String codClasificacion) {
        JSONObject obj = new JSONObject();

        try {
            log.info("TiposPlantillaGatewayApi - [trafic] - reading from file");
            String response = client.readFromFile(codClasificacion);
            obj.put("text", response);
            obj.put("success",true);
        } catch (Exception ioe) {
            obj.put("error", ioe.getMessage());
            obj.put("success",false);
            log.error("TiposPlantillaGatewayApi - [error] - a api delegator error has occurred", ioe);
        }

        return Response.status( Response.Status.ACCEPTED ).entity(obj.toJSONString()).build();
    }

    @POST
    @Path("/generar-pdf")
    //@JWTTokenSecurity
    public Response generatePdf(@RequestBody String htmlContent) {
        JSONObject obj = new JSONObject();

        try {
            log.info("TiposPlantillaGatewayApi - [trafic] - generate PDF");
            PdfConverter pdfConverter = new PdfConverter(htmlContent);
            pdfConverter.setOutputFile(this.location_output.concat("generated.pdf"));
            pdfConverter.convert();
            obj.put("text", "PDF generado satisfactoriamente '".concat(this.location_output.concat("generated.pdf")).concat("'"));
            obj.put("success",true);
        } catch (Exception ioe) {
            obj.put("error", ioe.getMessage());
            obj.put("success",false);
            log.error("TiposPlantillaGatewayApi - [error] - a api delegator error has occurred", ioe);
        }

        return Response.status( Response.Status.ACCEPTED ).entity(obj.toJSONString()).build();
    }

}
