package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.CargaMasivaClient;
import lombok.extern.log4j.Log4j2;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/carga-masiva-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class CargaMasivaGatewayApi {
    private static final String CONTENT = "CargaMasivaGatewayApi - [content] : ";
    @Autowired
    private CargaMasivaClient client;

    public CargaMasivaGatewayApi() {
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("/listadocargamasiva")
//    @JWTTokenSecurity
    public Response listCargaMasiva() {
        log.info("CargaMasivaGatewayApi - [trafic] - listing Carga Masiva");
        Response response = client.listCargaMasiva ();
        String responseContent = response.readEntity(String.class);
        log.info(CONTENT + responseContent);

        return Response.status( response.getStatus() ).entity(responseContent).build();
    }

    @GET
    @Path("/estadocargamasiva")
//    @JWTTokenSecurity
    public Response listEstadoCargaMasiva() {
        log.info("CargaMasivaGatewayApi - [trafic] - listing Estado Carga Masiva");
        Response response = client.listEstadoCargaMasiva ();
        String responseContent = response.readEntity(String.class);
        log.info(CONTENT + responseContent);

        return Response.status( response.getStatus() ).entity(responseContent).build();
    }

    @GET
    @Path("/estadocargamasiva/{id}")
//    @JWTTokenSecurity
    public Response listEstadoCargaMasivaDadoId(@PathParam("id") String id) {
        log.info("CargaMasivaGatewayApi - [trafic] - listing Estado Carga Masiva dado Id");
        Response response = client.listEstadoCargaMasivaDadoId (id);
        String responseContent = response.readEntity(String.class);
        log.info(CONTENT + responseContent);

        return Response.status( response.getStatus() ).entity(responseContent).build();
    }


    @POST
    @Path("/cargar-fichero/{codigoSede}/{codigoDependencia}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
//    @JWTTokenSecurity
    public Response cargarDocumento( @PathParam("codigoSede") String codigoSede, @PathParam("codigoDependencia") String codigoDependencia, MultipartFormDataInput file) {
        final String[] responseContent = {""};
        final int[] estadoRespuesta = {0};
        log.info("CargaMasivaGatewayApi - [trafic] - carga masiva");
        file.getFormDataMap().forEach((key, parts) -> {
            parts.forEach((part) -> {
                Response response = client.cargarDocumento (part, codigoSede, codigoDependencia);
                responseContent[0] = response.readEntity(String.class);
                estadoRespuesta[0] =response.getStatus ();

            });
        });
        log.info (responseContent[0] );
        log.info (estadoRespuesta[0] );
        log.info(CONTENT + responseContent[0]);

        return Response.status( estadoRespuesta[0]).entity(responseContent[0]).build();
    }

}

