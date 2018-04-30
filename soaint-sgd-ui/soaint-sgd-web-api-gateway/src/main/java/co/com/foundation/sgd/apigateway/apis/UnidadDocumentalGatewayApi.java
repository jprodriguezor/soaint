package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.ECMClient;
import co.com.foundation.sgd.apigateway.apis.delegator.UnidadDocumentalClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import co.com.soaint.foundation.canonical.ecm.ContenidoDependenciaTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.canonical.ecm.UnidadDocumentalDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.websocket.server.PathParam;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/unidad-documental-gateway-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class UnidadDocumentalGatewayApi {

    @Autowired
    private UnidadDocumentalClient unidadDocumentalClient;

    @Autowired
    private ECMClient ecmClient;

    private String MensajeErrorGenerico = "Ocurrió un error inesperado con el servicio ECM";

    public UnidadDocumentalGatewayApi() {
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @POST
    @Path("/listado-serie-subserie")
    @JWTTokenSecurity
    public Response listarSerie(@RequestBody ContenidoDependenciaTrdDTO contenidoDependenciaTrdDTO ) {

        log.info("UnidadDocumentalGatewayApi - [trafic] - listing series");
        Response response = ecmClient.listarSeriesSubseriePorDependencia(contenidoDependenciaTrdDTO);
        String responseContent = response.readEntity(String.class);
        log.info("UnidadDocumentalGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @POST
    @Path("/crear-unidad-documental")
    @JWTTokenSecurity
    public Response crearUnidadDocumental(@RequestBody UnidadDocumentalDTO unidadDocumentalDTO) {

        log.info("UnidadDocumentalGatewayApi - [trafic] - creating unidad documental");
        Response response = ecmClient.crearUnidadDocumental(unidadDocumentalDTO);
        String responseContent = response.readEntity(String.class);
        log.info("UnidadDocumentalGatewayApi - [content] : " + responseContent);

        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @POST
    @Path("/listar-unidad-documental")
    @JWTTokenSecurity
    public Response listarUnidadDocumental(@RequestBody UnidadDocumentalDTO unidadDocumentalDTO ) {

        log.info("ListarUnidadesDocumentalesGatewayApi - [trafic] - listing unidades documentales");
        Response response = ecmClient.listarUnidadesDocumentales(unidadDocumentalDTO);
        String responseContent = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @POST
    @Path("/abrir-unidades-documentales")
    @JWTTokenSecurity
    public Response abrirUnidadesDocumentales(@RequestBody List<UnidadDocumentalDTO> unidadesDocumentalesDTO ) {

        try{
            log.info("AbrirUnidadesDocumentalesGatewayApi - [trafic] - abrir unidades documentales");
            Response response = ecmClient.abrirUnidadDocumental(unidadesDocumentalesDTO);
            String responseContent = response.readEntity(String.class);
            return Response.status(response.getStatus()).entity(responseContent).build();
        }
        catch (Exception ex) {
            MensajeRespuesta respuestaEntity = new MensajeRespuesta("11111", MensajeErrorGenerico, null, null);
            return Response.status(Response.Status.NOT_FOUND).entity(respuestaEntity).build();
            log.info(ex.getMessage());
        }

    }

    @POST
    @Path("/cerrar-unidades-documentales")
    @JWTTokenSecurity
    public Response cerrarUnidadesDocumentales(@RequestBody List<UnidadDocumentalDTO> unidadesDocumentalesDTO ) {
        try {
            log.info("CerrarUnidadesDocumentalesGatewayApi - [trafic] - cerrar unidades documentales");
            Response response = ecmClient.cerrarUnidadDocumental(unidadesDocumentalesDTO);
            String responseContent = response.readEntity(String.class);
            return Response.status(response.getStatus()).entity(responseContent).build();
        }
        catch (Exception ex) {
            MensajeRespuesta respuestaEntity = new MensajeRespuesta("11111", MensajeErrorGenerico, null, null);
            return Response.status(Response.Status.NOT_FOUND).entity(respuestaEntity).build();
            log.info(ex.getMessage());
        }
    }

    @POST
    @Path("/reactivar-unidades-documentales")
    @JWTTokenSecurity
    public Response reactivarUnidadesDocumentales(@RequestBody List<UnidadDocumentalDTO> unidadesDocumentalesDTO ) {

        try {
            log.info("ReactivarUnidadesDocumentalesGatewayApi - [trafic] - reactivar unidades documentales");
            Response response = ecmClient.reactivarUnidadDocumental(unidadesDocumentalesDTO);
            String responseContent = response.readEntity(String.class);
            return Response.status(response.getStatus()).entity(responseContent).build();
        }
        catch (Exception ex) {
            MensajeRespuesta respuestaEntity = new MensajeRespuesta("11111", MensajeErrorGenerico, null, null);
            return Response.status(Response.Status.NOT_FOUND).entity(respuestaEntity).build();
            log.info(ex.getMessage());
        }
    }

    @GET
    @Path("/detalle-unidad-documental/{id}")
    @JWTTokenSecurity
    public Response detalleUnidadDocumental(@PathParam("id") final String idUnidadDocumental) {

        log.info("DetalleUnidadDocumentalGatewayApi - [trafic] - detalle unidad documental");
        Response response = ecmClient.DetalleUnidadDocumental(idUnidadDocumental);
        String responseContent = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseContent).build();
    }
}
