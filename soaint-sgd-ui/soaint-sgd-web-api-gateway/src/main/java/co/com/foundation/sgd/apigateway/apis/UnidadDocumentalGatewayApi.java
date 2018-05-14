package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.ECMClient;
import co.com.foundation.sgd.apigateway.apis.delegator.UnidadDocumentalClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import co.com.soaint.foundation.canonical.ecm.ContenidoDependenciaTrdDTO;
import co.com.soaint.foundation.canonical.ecm.DisposicionFinalDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.canonical.ecm.UnidadDocumentalDTO;
import lombok.extern.log4j.Log4j2;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
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
    @Path("/gestionar-unidades-documentales")
    @JWTTokenSecurity
    public Response abrirUnidadesDocumentales(@RequestBody List<UnidadDocumentalDTO> unidadesDocumentalesDTO ) {
         log.info("AbrirUnidadesDocumentalesGatewayApi - [trafic] - abrir unidades documentales");
         Response response = ecmClient.abrirCerrarReactivarUnidadDocumental(unidadesDocumentalesDTO);
         String responseContent = response.readEntity(String.class);
         return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @POST
    @Path("/listar-unidades-documentales-disposicion")
    @JWTTokenSecurity
    public Response listarUnidadesDocumentalesDisposicion(@RequestBody DisposicionFinalDTO disposicionFinal ) {
        log.info("ListarrUnidadesDocumentalesGatewayApi - [trafic] - listar unidades documentales disposicion final");
        Response response = ecmClient.listarUnidadesDocumentalesDisposicion(disposicionFinal);
        String responseContent = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseContent).build();;
    }

    @POST
    @Path("/aprobar-rechazar-disposiciones-finales")
    @JWTTokenSecurity
    public Response aprobarRechazarUnidadesDocumentalesDisposicion(@RequestBody List<UnidadDocumentalDTO> unidadDocumental ) {
        log.info("Aprobar/Rechazar UnidadesDocumentalesGatewayApi - [trafic] - aprobar rechazar unidades documentales disposicion");
        Response response = ecmClient.aprobarRechazarUnidadesDocumentalesDisposicion(unidadDocumental);
        String responseContent = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/detalle-unidad-documental/{id}")
    @JWTTokenSecurity
    public Response detalleUnidadDocumental(@PathParam("id") final String idUnidadDocumental) {
        log.info("DetalleUnidadDocumentalGatewayApi - [trafic] - detalle unidad documental");
        Response response = ecmClient.detalleUnidadDocumental(idUnidadDocumental);
        String responseContent = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/listar-documentos-por-archivar/{codigoDependencia}")
    @JWTTokenSecurity
    public Response documentosPorArchivar(@PathParam("codigoDependencia") final String codigoDependencia) {
        log.info("DocumentosPorArchivarGatewayApi - [trafic] - Listar documentos por archivar");
        Response response = ecmClient.documentosPorArchivar(codigoDependencia);
        String responseContent = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/listar-documentos-archivados/{codigoDependencia}")
    @JWTTokenSecurity
    public Response documentosArchivados(@PathParam("codigoDependencia") final String codigoDependencia) {
        log.info("DocumentosArchivadosGatewayApi - [trafic] - Listar documentos archivados");
        Response response = ecmClient.documentosArchivados(codigoDependencia);
        String responseContent = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @POST
    @Path("/subir-documentos-por-archivar")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @JWTTokenSecurity
    public Response subirDocumentosPorArchivar(MultipartFormDataInput formDataInputs) {
        log.info("SubirDocumentosPorArchivarGatewayApi - [trafic] - Subir documentos por archivar");

        Response response = ecmClient.subirDocumentosPorArchivar(formDataInputs);
        String responseContent = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @PUT
    @Path("/modificar-unidades-documentales")
    @JWTTokenSecurity
    public Response modificarUnidadesDocumentales(@RequestBody List<UnidadDocumentalDTO> unidadesDocumentalesDTO) {
        log.info("ModificarUnidadesDocumentalesGatewayApi - [trafic] - Modificar Unidades Documentales");
        Response response = ecmClient.modificarUnidadesDocumentales(unidadesDocumentalesDTO);
        String responseContent = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @POST
    @Path("/subir-documentos-unidad-documental")
    @JWTTokenSecurity
    public Response subirDocumentosUnidadDocumental(@RequestBody UnidadDocumentalDTO unidadDocumentalDTO) {
        log.info("SubirDocumentosUnidadDocumentalGatewayApi - [trafic] - Subir Documentos a Unidades Documentales");
        Response response = ecmClient.subirDocumentosUnidadDocumental(unidadDocumentalDTO);
        String responseContent = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(responseContent).build();
    }

    @GET
    @Path("/restablecer-archivar-documento-task/{proceso}/{tarea}")
    @JWTTokenSecurity
    public Response restablecerArchivarDocumentoTask(@PathParam("proceso") final String idproceso, @PathParam("tarea") final String idtarea) {
        log.info("UnidadDocumentalGatewayApi - [trafic] - Restableciendo Correspondencia Entrada. proceso:" + idproceso + " tarea :" + idtarea);
        Response response = ecmClient.restablecerArchivarDocumentoTask(idproceso, idtarea);
        String responseObject = response.readEntity(String.class);
        if (response.getStatus() == HttpStatus.NO_CONTENT.value() || response.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            return Response.status(HttpStatus.OK.value()).entity(new ArrayList<>()).build();
        }
        return Response.status(response.getStatus()).entity(responseObject).build();

    }
}
