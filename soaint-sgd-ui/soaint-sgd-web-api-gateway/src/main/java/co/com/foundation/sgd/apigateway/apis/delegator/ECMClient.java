package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.infrastructure.ApiDelegator;
import co.com.foundation.sgd.utils.SystemParameters;
import co.com.soaint.foundation.canonical.ecm.*;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@ApiDelegator
@Log4j2
public class ECMClient {

    private String endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ECM_SERVICE_ENDPOINT_URL);
    private String record_endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ECM_RECORD_SERVICE_ENDPOINT_URL);
    private String corresponencia_endpoint = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

    public ECMClient() {
        super();
    }

    public MensajeRespuesta uploadVersionDocumento(DocumentoDTO documentoDTO, String selector) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);

        Response response = wt.path("/subirVersionarDocumentoGeneradoECM/" + selector)
                .request()
                .post(Entity.json(documentoDTO));

        return response.readEntity(MensajeRespuesta.class);
    }

    public MensajeRespuesta obtenerVersionesDocumento(String documentId) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        Response response = wt.path("/obtenerVersionesDocumentos/" + documentId).request()
                .post(Entity.json(""));

        return response.readEntity(MensajeRespuesta.class);
    }

    public boolean eliminarVersionDocumento(String idDocumento) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        Response response = wt.path("/eliminarDocumentoECM/" + idDocumento).request()
                .delete();

        return response.readEntity(Boolean.class);
    }

    public MensajeRespuesta uploadDocumentoAnexo(DocumentoDTO documentoDTO) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        Response response = wt.path("/subirDocumentoAnexoECM/").request()
                .post(Entity.json(documentoDTO));

        return response.readEntity(MensajeRespuesta.class);
    }

    public MensajeRespuesta uploadDocument(DocumentoDTO documentoDTO, String tipoComunicacion) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);

        Response response = wt.path("/subirDocumentoRelacionECM/" + tipoComunicacion)
                .request()
                .post(Entity.json(documentoDTO));

        return response.readEntity(MensajeRespuesta.class);
    }

    public MensajeRespuesta estamparEtiquetaRadicacion(DocumentoDTO documentoDTO) {
        log.info("Digitalizar Documento - [trafic] - Invocando Servicio Remoto Estampar Documento Radicacion Salida: " + endpoint);
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        Response response = wt.path("/estampar-etiqueta-radicacion/")
                .request().post(Entity.json(documentoDTO));
        return response.readEntity(MensajeRespuesta.class);
    }

    public List<MensajeRespuesta> uploadDocumentsAsociates(String parentId, Map<String, InputPart> files, String sede, String dependencia, String tipoComunicacion, String numero, String[] referidoList) {
        List<MensajeRespuesta> mensajeRespuestas = new ArrayList<>();
        try {
            files.forEach((key, part) -> {

                DocumentoDTO documentoAsociadoECMDTO = new DocumentoDTO();
                try {
                    documentoAsociadoECMDTO.setDependencia(dependencia);
                    documentoAsociadoECMDTO.setSede(sede);
                    InputStream result = part.getBody(InputStream.class, null);
                    documentoAsociadoECMDTO.setDocumento(IOUtils.toByteArray(result));
                    documentoAsociadoECMDTO.setTipoDocumento("application/pdf");
                    documentoAsociadoECMDTO.setNombreDocumento(key);
                    documentoAsociadoECMDTO.setIdDocumentoPadre(parentId);
                    documentoAsociadoECMDTO.setNroRadicado(numero);
                    documentoAsociadoECMDTO.setNroRadicadoReferido(referidoList);

                } catch (Exception e) {
                    log.info("Error generando el documento ", e);
                }

                MensajeRespuesta asociadoResponse = this.uploadDocument(documentoAsociadoECMDTO, tipoComunicacion);
                mensajeRespuestas.add(asociadoResponse);

            });
        } catch (Exception e) {
            log.error("Se ha generado un error al subir los documentos asociados: ", e);
        }
        return mensajeRespuestas;
    }


    public Response findByIdDocument(String idDocumento) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/descargarDocumentoECM/")
                .queryParam("identificadorDoc", idDocumento)
                .request()
                .get();
    }

    public Response deleteDocumentById(String documentId) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/eliminarDocumentoECM/" + documentId).request()
                .delete();
    }

    public MensajeRespuesta findDocumentosAsociadosID(String idDocumento) {
        DocumentoDTO dto = DocumentoDTO.newInstance().idDocumento(idDocumento).build();
        Response response = getDocumentosAsociados(dto);
        return response.readEntity(MensajeRespuesta.class);
    }

    public MensajeRespuesta findDocumentosAsociadosRadicado(String nroRadicado) {
        DocumentoDTO dto = DocumentoDTO.newInstance().nroRadicado(nroRadicado).build();
        Response response = getDocumentosAsociados(dto);
        return response.readEntity(MensajeRespuesta.class);
    }

    public Response listarSeriesSubseriePorDependencia(ContenidoDependenciaTrdDTO contenidoDependenciaTrdDTO) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/devolverSerieOSubserieECM")
                .request()
                .post(Entity.json(contenidoDependenciaTrdDTO));
    }

    public Response crearUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/crearUnidadDocumentalECM")
                .request()
                .post(Entity.json(unidadDocumentalDTO));
    }

    public Response listarUnidadesDocumentales(UnidadDocumentalDTO unidadDocumentalDTO) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/listarUnidadesDocumentalesECM")
                .request()
                .post(Entity.json(unidadDocumentalDTO));
    }

    public Response listarUnidadesDocumentalesDisposicion(DisposicionFinalDTO disposicionFinal) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        Response response = wt.path("/listar-unidades-documentales-disposicion")
                            .request()
                            .post(Entity.json(disposicionFinal));
        return response;
    }

    public Response aprobarRechazarUnidadesDocumentalesDisposicion(List<UnidadDocumentalDTO> unidadesDocumentales) {
         WebTarget wt = ClientBuilder.newClient().target(endpoint);
         Response response = wt.path("/aprobar-rechazar-disposiciones-finales")
                             .request()
                             .put(Entity.json(unidadesDocumentales));
         return response;
    }

    public Response abrirCerrarReactivarUnidadDocumental(List<UnidadDocumentalDTO> dtoList) {
        log.info("AbrirCerrarReactivarUnidadesDocumentalesECMClient - [trafic] - cerrar unidades documentales");
        WebTarget wt = ClientBuilder.newClient().target(record_endpoint);
        return wt.path("/abrirCerrarReactivarUnidadesDocumentalesECM")
                .request()
                .put(Entity.json(dtoList));
    }

    public Response detalleUnidadDocumental(String idUnidadDocumental) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/verDetalleUnidadDocumentalECM/" + idUnidadDocumental)
                .request()
                .get();
    }

    public Response documentosPorArchivar(final String codigoDependencia) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/devolverDocumentosPorArchivarECM/" + codigoDependencia)
                .request()
                .get();
    }

    public Response documentosArchivados(String codigoDependencia) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/obtenerDocumentosArchivadosECM/" + codigoDependencia)
                .request()
                .get();
    }

    public Response modificarUnidadesDocumentales(List<UnidadDocumentalDTO> unidadesDocumentalesDTO) {
        log.info("ModificarUnidadesDocumentalesGatewayApi - [trafic] - Modificar Unidades Documentales");
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/modificarUnidadesDocumentalesECM")
                .request()
                .put(Entity.json(unidadesDocumentalesDTO));
    }

    public Response subirDocumentosUnidadDocumental(UnidadDocumentalDTO unidadDocumentalDTO) {
        log.info("SubirDocumentosUnidadDocumentalGatewayApi - [trafic] - Subir Documentos a Unidades Documentales");
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/subirDocumentosUnidadDocumentalECM")
                .request()
                .post(Entity.json(unidadDocumentalDTO));
    }

    public Response subirDocumentosPorArchivar(MultipartFormDataInput formDataInput) {
        log.info("SubirDocumentosPorArchivarGatewayApi - [trafic] - Subir documentos por archivar");
        if (null == formDataInput) {
            log.error("Esta vacia la lista Multipart");
            return Response.serverError().build();
        }
        try {
            log.info("Procesando la informacion del multipart");
            final Map<String, InputPart> _files = ECMUtils.findFiles(formDataInput);
            log.info("Devolviendo Mapa de Documentos");
            _files.forEach((fileName, inputPart) -> log.info("Nombre Archivo: {}, => documento: {}", fileName, inputPart));
            final String dependencyCode = formDataInput.getFormDataPart("codigoDependencia", String.class, null);
            log.info("Codigo de Dependencia: {}", dependencyCode);
            final List<DocumentoDTO> documentoDTOS = new ArrayList<>();
            final Collection<InputPart> values = _files.values();
            log.info("Cantidad de Documentos: {}", values.size());

            for (InputPart inputPart:
                    values) {
                final DocumentoDTO tmpDto = new DocumentoDTO();
                InputStream result = inputPart.getBody(InputStream.class, null);
                tmpDto.setDocumento(IOUtils.toByteArray(result));
                tmpDto.setCodigoDependencia(dependencyCode);
                tmpDto.setTipoDocumento("application/pdf");
                tmpDto.setNombreDocumento(ECMUtils.findName(inputPart));
                documentoDTOS.add(documentoDTOS.size(), tmpDto);
            }
            log.info("Cantidad de Documentos DTOs: {}", documentoDTOS.size());
            final WebTarget wt = ClientBuilder.newClient().target(endpoint);
            return wt.path("/subirDocumentosTemporalesECM")
                    .request()
                    .post(Entity.json(documentoDTOS));

        } catch (Exception e) {
            log.error("Error del Sistema {}", e.getMessage());
            MensajeRespuesta respuesta = MensajeRespuesta.newInstance()
                    .codMensaje("1223")
                    .mensaje(e.getMessage())
                    .build();
            return Response.status(Response.Status.OK).entity(respuesta).build();
        }
    }

    public Response restablecerArchivarDocumentoTask(String idproceso, String idtarea) {
        log.info("Unidad Documental - [trafic] - Invocando Servicio Remoto Salvar Tarea Archivar Documento: " + corresponencia_endpoint);
        WebTarget wt = ClientBuilder.newClient().target(corresponencia_endpoint);
        return wt.path("/tarea-web-api/tarea/" + idproceso + "/" + idtarea)
                .request().get();
    }

    private Response getDocumentosAsociados(DocumentoDTO documentoDTO) {
        WebTarget wt = ClientBuilder.newClient().target(endpoint);
        return wt.path("/obtenerDocumentosAdjuntosECM").request().post(Entity.json(documentoDTO));
    }
}
