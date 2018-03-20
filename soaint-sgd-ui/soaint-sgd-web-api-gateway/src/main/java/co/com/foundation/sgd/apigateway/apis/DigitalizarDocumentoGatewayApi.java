package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.DigitalizarDocumentoClient;
import co.com.foundation.sgd.apigateway.apis.delegator.ECMClient;
import co.com.foundation.sgd.apigateway.apis.delegator.ECMUtils;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.canonical.ecm.DocumentoDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.validation.constraints.NotNull;
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
    @Path("/{tipoComunicacion}/{fileName}/{referidoList}/{principalFileName}/{sede}/{dependencia}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response digitalizar(@PathParam("tipoComunicacion") String tipoComunicacion, @PathParam("fileName") String fileName, @PathParam("referidoList") String[] referidoList,
                                @PathParam("principalFileName") String principalFileName, @PathParam("sede") String sede,
                                @PathParam("dependencia") String dependencia, MultipartFormDataInput files) {

        log.info("ProduccionDocumentalGatewayApi - [content] : ");

        List<String> ecmIds = new ArrayList<>();
        Map<String,InputPart> _files = ECMUtils.findFiles(files);
        InputPart parent = _files.get(principalFileName);
        DocumentoDTO documentoECMDTO = new DocumentoDTO();
        try {

            documentoECMDTO.setDependencia(dependencia);
            documentoECMDTO.setSede(sede);
            InputStream result = parent.getBody(InputStream.class, null);
            documentoECMDTO.setTipoDocumento("application/pdf");
            documentoECMDTO.setDocumento(IOUtils.toByteArray(result));
            documentoECMDTO.setNombreDocumento(principalFileName);
            documentoECMDTO.setNroRadicado(fileName);
            documentoECMDTO.setNroRadicadoReferido(referidoList);

        }catch (Exception e){
            log.info("Error generando el documento ",e);
        }


        MensajeRespuesta parentResponse = client.uploadDocument(documentoECMDTO, tipoComunicacion);
        _files.remove(fileName);
        if ("0000".equals(parentResponse.getCodMensaje())){
            List<DocumentoDTO> documentoDTO = (List<DocumentoDTO>) parentResponse.getDocumentoDTOList();
            if(null != documentoDTO && !documentoDTO.isEmpty()) {
                ecmIds.add(documentoDTO.get(0).getIdDocumento());
                if(!_files.isEmpty()){
                    client.uploadDocumentsAsociates(documentoDTO.get(0).getIdDocumento(),_files, sede, dependencia, tipoComunicacion, fileName,referidoList).forEach(mensajeRespuesta -> {
                        if("0000".equals(mensajeRespuesta.getCodMensaje())){
                            List<DocumentoDTO> documentoDTO1 =
                                    (List<DocumentoDTO>) mensajeRespuesta.getDocumentoDTOList();
                            ecmIds.add(documentoDTO1.get(0).getIdDocumento());
                        }
                    });
                }
                return Response.status(Response.Status.OK).entity(ecmIds).build();
            }
        }
        return Response.status(Response.Status.OK).entity(parentResponse).build();
    }

    @POST
    @Path("/versionar-documento")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @JWTTokenSecurity
    public Response versionarDocumento(MultipartFormDataInput formDataInput) {

        log.info("DigitalizarDocumentoComunicacionGatewayApi - [content] : Subir version documento");

        MensajeRespuesta clientResponse = null;
        DocumentoDTO documentoDTO = new DocumentoDTO();
        try {
            InputStream inputStream = formDataInput.getFormDataPart("documento", InputStream.class, null);
            documentoDTO.setDocumento(IOUtils.toByteArray(inputStream));

            if (null != formDataInput.getFormDataPart("idDocumento", String.class, null)) {
                documentoDTO.setIdDocumento(formDataInput.getFormDataPart("idDocumento", String.class, null));
            }

            documentoDTO.setNombreDocumento(formDataInput.getFormDataPart("nombreDocumento", String.class, null));
            documentoDTO.setTipoDocumento(formDataInput.getFormDataPart("tipoDocumento", String.class, null));
            documentoDTO.setSede(formDataInput.getFormDataPart("sede", String.class, null));
            documentoDTO.setDependencia(formDataInput.getFormDataPart("dependencia", String.class, null));
            String selector = formDataInput.getFormDataPart("selector", String.class, null);
            if (0 < formDataInput.getFormDataPart("nroRadicado", String.class, null).length()) {
                documentoDTO.setNroRadicado(formDataInput.getFormDataPart("nroRadicado", String.class, null));
            }

            clientResponse = this.client.uploadVersionDocumento(documentoDTO, selector);
            log.info(clientResponse);

        } catch (Exception ex) {
            return this.EcmErrorMessage(ex);
        }

        return Response.status(Response.Status.OK).entity(clientResponse).build();
    }
    private Response EcmErrorMessage(@NotNull Exception ex) {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("codMensaje","9999");
        jsonResponse.put("mensaje",ex.getMessage());

        return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse.toJSONString()).build();
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
    @Path("/obtener-documentos-asociados/{idDocumento}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerdocumentosasociados(@PathParam("idDocumento") String idDocumento) {
        log.info("DigitalizarDocumentoGatewayApi - [trafic] - obteniendo Documento asociados desde el ecm: " + idDocumento);
        MensajeRespuesta mensajeRespuesta = client.findDocumentosAsociados(idDocumento);
        return Response.status(Response.Status.OK).entity(mensajeRespuesta).build();
    }

    @POST
    @Path("/eliminarprincipal/{documentId}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response deleteDocumentById(@PathParam("documentId") String documentId) {
        Response response = client.deleteDocumentById(documentId);
        String removed = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(removed).build();
    }

}
