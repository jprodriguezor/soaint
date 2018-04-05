package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.DigitalizarDocumentoClient;
import co.com.foundation.sgd.apigateway.apis.delegator.ECMClient;
import co.com.foundation.sgd.apigateway.apis.delegator.ECMUtils;
import co.com.soaint.foundation.canonical.ecm.DocumentoDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
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
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response digitalizar(MultipartFormDataInput formDataInput) {

        log.info("ProduccionDocumentalGatewayApi - [content] : ");

        List<String> ecmIds = new ArrayList<>();
        Map<String, InputPart> _files = ECMUtils.findFiles(formDataInput);
        DocumentoDTO documentoECMDTO = new DocumentoDTO();
        MensajeRespuesta parentResponse = null;

        try {
            String principalFileName = formDataInput.getFormDataPart("principalFileName", String.class, null);
            String dependencia = formDataInput.getFormDataPart("dependencia", String.class, null);
            String sede = formDataInput.getFormDataPart("sede", String.class, null);
            String tipoComunicacion = formDataInput.getFormDataPart("tipoComunicacion", String.class, null);
            String nroRadicado = formDataInput.getFormDataPart("nroRadicado", String.class, null);
            List<String> referidoList = new ArrayList<>();
            if (null != formDataInput.getFormDataMap().get("referidoList")) {
                for (InputPart referido : formDataInput.getFormDataMap().get("referidoList")) {
                    referidoList.add(referido.getBodyAsString());
                }
            }
            InputPart parent = _files.get(principalFileName);
            InputStream result = parent.getBody(InputStream.class, null);
            documentoECMDTO.setDocumento(IOUtils.toByteArray(result));
            documentoECMDTO.setDependencia(dependencia);
            documentoECMDTO.setSede(sede);
            documentoECMDTO.setTipoDocumento("application/pdf");
            documentoECMDTO.setNombreDocumento(principalFileName);
            documentoECMDTO.setNroRadicado(nroRadicado);
            documentoECMDTO.setNroRadicadoReferido(Arrays.copyOf(referidoList.toArray(), referidoList.size(), String[].class));
            documentoECMDTO.setNombreRemitente(formDataInput.getFormDataPart("nombreRemitente", String.class, null));
            parentResponse = client.uploadDocument(documentoECMDTO, tipoComunicacion);
            _files.remove(principalFileName);
            if ("0000".equals(parentResponse.getCodMensaje())) {
                List<DocumentoDTO> documentoDTO = parentResponse.getDocumentoDTOList();
                if (null != documentoDTO && !documentoDTO.isEmpty()) {
                    ecmIds.add(documentoDTO.get(0).getIdDocumento());
                    if (!_files.isEmpty()) {
                        client.uploadDocumentsAsociates(documentoDTO.get(0).getIdDocumento(), _files, sede, dependencia, tipoComunicacion, nroRadicado, documentoECMDTO.getNroRadicadoReferido()).forEach(mensajeRespuesta -> {
                            if ("0000".equals(mensajeRespuesta.getCodMensaje())) {
                                List<DocumentoDTO> documentoDTO1 = mensajeRespuesta.getDocumentoDTOList();
                                ecmIds.add(documentoDTO1.get(0).getIdDocumento());
                            }
                        });
                    }
                    return Response.status(Response.Status.OK).entity(ecmIds).build();
                }
            }
        } catch (Exception e) {

        }
        return Response.status(Response.Status.OK).entity(parentResponse).build();
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
