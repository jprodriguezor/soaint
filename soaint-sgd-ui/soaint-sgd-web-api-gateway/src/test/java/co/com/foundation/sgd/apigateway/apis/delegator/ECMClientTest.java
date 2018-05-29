package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.test.mocks.JaxRsUtils;
import co.com.foundation.test.mocks.PartUtils;
import co.com.foundation.test.rules.EnvironmentRule;
import co.com.foundation.sgd.utils.SystemParameters;
import co.com.soaint.foundation.canonical.ecm.*;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ECMClientTest {

    @Rule
    public EnvironmentRule environmentRule = new EnvironmentRule();

    private String API_ENDPOINT = SystemParameters.getParameter(SystemParameters.BACKAPI_ECM_SERVICE_ENDPOINT_URL);

    private String RECORD_ENDPOINT = SystemParameters.getParameter(SystemParameters.BACKAPI_ECM_RECORD_SERVICE_ENDPOINT_URL);

    private String CORRESPONDENCIA_ENDPOINT = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

    private ECMClient ecmClient;

    private WebTarget wt;

    private Client client;

    @Before
    public void setup() {

        ecmClient = new ECMClient();

        client = mock(Client.class);
        wt = mock(WebTarget.class);

        when(client.target(anyString())).thenReturn(wt);

        ReflectionTestUtils.setField(ecmClient, "client", client);
    }

    @Test
    public void uploadVersionDocumento() {
        // given
        String SELECTOR = "select";
        String path = "/subirVersionarDocumentoGeneradoECM/" + SELECTOR;
        DocumentoDTO dto = mock(DocumentoDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, MensajeRespuesta.class, path);

        // when
        ecmClient.uploadVersionDocumento(dto, SELECTOR);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);
        verify(JaxRsUtils.getResponseMock()).readEntity(MensajeRespuesta.class);

        ArgumentCaptor<Entity<DocumentoDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }

    @Test
    public void obtenerVersionesDocumento() {
        // given
        String ID_DOCUMENTO = "ID01";
        String path = "/obtenerVersionesDocumentos/" + ID_DOCUMENTO;
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, MensajeRespuesta.class, path);

        // when
        ecmClient.obtenerVersionesDocumento(ID_DOCUMENTO);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);
        verify(JaxRsUtils.getResponseMock()).readEntity(MensajeRespuesta.class);

        ArgumentCaptor<Entity<String>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs("");
    }

    @Test
    public void eliminarVersionDocumento() {
        // given
        String ID_DOCUMENTO = "ID01";
        String path = "/eliminarDocumentoECM/" + ID_DOCUMENTO;
        JaxRsUtils.mockDeletePath(wt, Boolean.class, path);

        // when
        ecmClient.eliminarVersionDocumento(ID_DOCUMENTO);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);
        verify(JaxRsUtils.getResponseMock()).readEntity(Boolean.class);
    }

    @Test
    public void uploadDocument() {
        // given
        String TIPO_COMUNICACION = "TC01";
        String path = "/subirDocumentoRelacionECM/" + TIPO_COMUNICACION;
        DocumentoDTO dto = mock(DocumentoDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, MensajeRespuesta.class, path);

        // when
        ecmClient.uploadDocument(dto, TIPO_COMUNICACION);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);
        verify(JaxRsUtils.getResponseMock()).readEntity(MensajeRespuesta.class);

        ArgumentCaptor<Entity<DocumentoDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }

    @Test
    public void estamparEtiquetaRadicacion() {
        // given
        String path = "/estampar-etiqueta-radicacion/";
        DocumentoDTO dto = mock(DocumentoDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, MensajeRespuesta.class, path);

        // when
        ecmClient.estamparEtiquetaRadicacion(dto);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);
        verify(JaxRsUtils.getResponseMock()).readEntity(MensajeRespuesta.class);

        ArgumentCaptor<Entity<DocumentoDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }

    @Test
    public void uploadDocumentsAsociates() throws IOException {
        // given
        String PARENT_ID = "PI01";
        String NRO_RADICADO = "0123";
        String SEDE = "CS01";
        String DEPENDENCIA = "CD01";
        String TIPO_COMUNICACION = "TC01";
        String[] REFRIDO_LIST = {};
        Map<String, InputPart> FILES = new HashMap<>();

        byte[] FILE_BYTES = {10};

        InputPart part = mock(InputPart.class);
        when(part.getBody(InputStream.class, null)).thenAnswer(invocation -> new ByteArrayInputStream(FILE_BYTES));

        List<String> FILE_NAMES = Arrays.asList("file1", "file2", "file3");
        FILE_NAMES.forEach(filename -> FILES.put(filename, part));

        // spying the test class to capture inner call arguments
        ECMClient ecmClientSpy = spy(ecmClient);

        // mocking the inner call uploadDocument
        String path = "/subirDocumentoRelacionECM/" + TIPO_COMUNICACION;
        JaxRsUtils.mockPostPath(wt, MensajeRespuesta.class, path);


        // when
        List<MensajeRespuesta> respuestas = ecmClientSpy.uploadDocumentsAsociates(
                PARENT_ID, FILES, SEDE, DEPENDENCIA,
                TIPO_COMUNICACION, NRO_RADICADO, REFRIDO_LIST
        );

        // then
        assertThat(respuestas.size()).isEqualTo(FILES.size());

        ArgumentCaptor<DocumentoDTO> captor = ArgumentCaptor.forClass(DocumentoDTO.class);
        verify(ecmClientSpy, times(FILES.size())).uploadDocument(captor.capture(), anyString());

        assertThat(captor.getAllValues())
                .allSatisfy(documentoDTO -> {
                   assertThat(documentoDTO.getDependencia()).isEqualTo(DEPENDENCIA);
                   assertThat(documentoDTO.getSede()).isEqualTo(SEDE);
                   assertThat(documentoDTO.getDocumento()).isEqualTo(FILE_BYTES);
                   assertThat(documentoDTO.getTipoDocumento()).isEqualTo("application/pdf");
                   assertThat(documentoDTO.getNombreDocumento()).isIn(FILE_NAMES);
                   assertThat(documentoDTO.getIdDocumentoPadre()).isEqualTo(PARENT_ID);
                   assertThat(documentoDTO.getNroRadicado()).isEqualTo(NRO_RADICADO);
                   assertThat(documentoDTO.getNroRadicadoReferido()).isSameAs(REFRIDO_LIST);
                });
    }

    @Test
    public void uploadDocumentsAsociatesExceptionFromInputStream() throws IOException {
        // given
        Map<String, InputPart> FILES = new HashMap<>();
        String TIPO_COMUNICACION = "TC01";

        InputPart part = mock(InputPart.class);
        when(part.getBody(InputStream.class, null)).thenThrow(IOException.class);

        List<String> FILE_NAMES = Arrays.asList("file1", "file2", "file3");
        FILE_NAMES.forEach(filename -> FILES.put(filename, part));

        // spying the test class to capture inner call arguments
        ECMClient ecmClientSpy = spy(ecmClient);

        // mocking the inner call uploadDocument
        String path = "/subirDocumentoRelacionECM/" + TIPO_COMUNICACION;
        JaxRsUtils.mockPostPath(wt, MensajeRespuesta.class, path);


        // when
        List<MensajeRespuesta> respuestas = ecmClientSpy.uploadDocumentsAsociates(null, FILES, null, null, TIPO_COMUNICACION, null, null);

        // then
        assertThat(respuestas.size()).isEqualTo(0);
        verify(ecmClientSpy, times(0)).uploadDocument(any(DocumentoDTO.class), anyString());
    }

    @Test
    public void uploadDocumentsAsociatesExceptionFromUploadDocument() throws IOException {
        // given
        Map<String, InputPart> FILES = new HashMap<>();
        String TIPO_COMUNICACION = "TC01";

        InputPart part = mock(InputPart.class);
        when(part.getBody(InputStream.class, null)).thenThrow(IOException.class);

        List<String> FILE_NAMES = Arrays.asList("file1", "file2", "file3");
        FILE_NAMES.forEach(filename -> FILES.put(filename, part));

        // spying the test class to capture inner call arguments
        ECMClient ecmClientSpy = spy(ecmClient);

        // when
        List<MensajeRespuesta> respuestas = ecmClientSpy.uploadDocumentsAsociates(null, FILES, null, null, TIPO_COMUNICACION, null, null);

        // then
        assertThat(respuestas.size()).isEqualTo(0);
        verify(ecmClientSpy, times(0)).uploadDocument(any(DocumentoDTO.class), anyString());
    }

    @Test
    public void findByIdDocument() {
        // given
        String path = "/descargarDocumentoECM/";
        String ID_DOCUMENTO = "ID_DOC";

        WebTarget requestTarget = JaxRsUtils.mockGetPath(wt, path);

        // when
        ecmClient.findByIdDocument(ID_DOCUMENTO);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        verify(requestTarget).queryParam("identificadorDoc", ID_DOCUMENTO);
    }

    @Test
    public void deleteDocumentById() {
        // given
        String ID_DOCUMENTO = "ID01";
        String path = "/eliminarDocumentoECM/" + ID_DOCUMENTO;
        JaxRsUtils.mockDeletePath(wt, path);

        // when
        ecmClient.deleteDocumentById(ID_DOCUMENTO);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);
    }

    @Test
    public void findDocumentosAsociadosID() {
        // given
        String ID_DOCUMENTO = "ID01";
        String path = "/obtenerDocumentosAdjuntosECM";
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, MensajeRespuesta.class, path);
        ECMClient ecmClientSpy = spy(ecmClient);

        // when
        ecmClientSpy.findDocumentosAsociadosID(ID_DOCUMENTO);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);
        verify(JaxRsUtils.getResponseMock()).readEntity(MensajeRespuesta.class);

        ArgumentCaptor<Entity<DocumentoDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity().getIdDocumento()).isEqualTo(ID_DOCUMENTO);
    }

    @Test
    public void findDocumentosAsociadosRadicado() {
        // given
        String NRO_RADICADO = "NR01";
        String path = "/obtenerDocumentosAdjuntosECM";
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, MensajeRespuesta.class, path);
        ECMClient ecmClientSpy = spy(ecmClient);

        // when
        ecmClientSpy.findDocumentosAsociadosRadicado(NRO_RADICADO);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);
        verify(JaxRsUtils.getResponseMock()).readEntity(MensajeRespuesta.class);

        ArgumentCaptor<Entity<DocumentoDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity().getNroRadicado()).isEqualTo(NRO_RADICADO);
    }

    @Test
    public void listarSeriesSubseriePorDependencia() {
        // given
        String path = "/devolverSerieOSubserieECM";
        ContenidoDependenciaTrdDTO dto = mock(ContenidoDependenciaTrdDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, path);

        // when
        ecmClient.listarSeriesSubseriePorDependencia(dto);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<ContenidoDependenciaTrdDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }

    @Test
    public void crearUnidadDocumental() {
        // given
        String path = "/crearUnidadDocumentalECM";
        UnidadDocumentalDTO dto = mock(UnidadDocumentalDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, path);

        // when
        ecmClient.crearUnidadDocumental(dto);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<UnidadDocumentalDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }

    @Test
    public void listarUnidadesDocumentales() {
        // given
        String path = "/listarUnidadesDocumentalesECM";
        UnidadDocumentalDTO dto = mock(UnidadDocumentalDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, path);

        // when
        ecmClient.listarUnidadesDocumentales(dto);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<UnidadDocumentalDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }

    @Test
    public void listarUnidadesDocumentalesDisposicion() {
        // given
        String path = "/listar-unidades-documentales-disposicion";
        DisposicionFinalDTO dto = mock(DisposicionFinalDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, path);

        // when
        ecmClient.listarUnidadesDocumentalesDisposicion(dto);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<DisposicionFinalDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }


    @Test
    public void aprobarRechazarUnidadesDocumentalesDisposicion() {
        // given
        String path = "/aprobar-rechazar-disposiciones-finales";
        List<UnidadDocumentalDTO> dto = mock(List.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPutPath(wt, path);

        // when
        ecmClient.aprobarRechazarUnidadesDocumentalesDisposicion(dto);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<List<UnidadDocumentalDTO>>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).put(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }

    @Test
    public void abrirCerrarReactivarUnidadDocumental() {
        // given
        String path = "/abrirCerrarReactivarUnidadesDocumentalesECM";
        List<UnidadDocumentalDTO> dto = mock(List.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPutPath(wt, path);

        // when
        ecmClient.abrirCerrarReactivarUnidadDocumental(dto);

        // then
        verify(client).target(RECORD_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<List<UnidadDocumentalDTO>>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).put(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }

    @Test
    public void detalleUnidadDocumental() {
        // given
        String ID_UNIDAD_DOCUMENTAL = "IUD01";
        String path = "/verDetalleUnidadDocumentalECM/" + ID_UNIDAD_DOCUMENTAL;

        JaxRsUtils.mockGetPath(wt, path);

        // when
        ecmClient.detalleUnidadDocumental(ID_UNIDAD_DOCUMENTAL);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);
    }

    @Test
    public void documentosPorArchivar() {
        // given
        String COD_DEPENDENCIA = "CD01";
        String path = "/devolverDocumentosPorArchivarECM/" + COD_DEPENDENCIA;

        JaxRsUtils.mockGetPath(wt, path);

        // when
        ecmClient.documentosPorArchivar(COD_DEPENDENCIA);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);
    }

    @Test
    public void documentosArchivados() {
        // given
        String COD_DEPENDENCIA = "CD01";
        String path = "/obtenerDocumentosArchivadosECM/" + COD_DEPENDENCIA;

        JaxRsUtils.mockGetPath(wt, path);

        // when
        ecmClient.documentosArchivados(COD_DEPENDENCIA);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);
    }

    @Test
    public void modificarUnidadesDocumentales() {
        // given
        String path = "/modificarUnidadesDocumentalesECM";
        List<UnidadDocumentalDTO> dto = mock(List.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPutPath(wt, path);

        // when
        ecmClient.modificarUnidadesDocumentales(dto);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<List<UnidadDocumentalDTO>>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).put(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }

    @Test
    public void subirDocumentosUnidadDocumental() {
        // given
        String path = "/subirDocumentosUnidadDocumentalECM";
        UnidadDocumentalDTO dto = mock(UnidadDocumentalDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, path);

        // when
        ecmClient.subirDocumentosUnidadDocumental(dto);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<UnidadDocumentalDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }

    @Test
    public void subirDocumentosPorArchivar() {
        // given
        String path = "/subirDocumentosTemporalesECM";
        String FILE_1 = "FILE_1.PDF";
        String FILE_2 = "FILE_2.PDF";
        String COD_DEPENDENCIA = "CD01";

        MultipartFormDataInput files = PartUtils.newMultiPart()
                .addPart("codigoDependencia", COD_DEPENDENCIA)
                .addBinaryPart(FILE_1, "HELLO PDF 1")
                .addBinaryPart(FILE_2, "HELLO PDF 2")
                .build();

        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, path);

        // when
        ecmClient.subirDocumentosPorArchivar(files);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<List<DocumentoDTO>>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity()).allSatisfy(documentoDTO -> {
            assertThat(documentoDTO.getDocumento()).isNotNull();
            assertThat(documentoDTO.getCodigoDependencia()).isEqualTo(COD_DEPENDENCIA);
            assertThat(documentoDTO.getTipoDocumento()).isEqualTo("application/pdf");
            assertThat(documentoDTO.getNombreDocumento()).isIn(FILE_1, FILE_2);
        });
    }

    @Test
    public void subirDocumentosPorArchivarWithEmptyParams() {
        // given
        MultipartFormDataInput files = null;

        // when
        Response response = ecmClient.subirDocumentosPorArchivar(files);

        // then
        assertThat(response.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
    }

    @Test
    public void subirDocumentosPorArchivarWithErrorHandling() {
        // given
        String COD_MENSAJE = "1223";
        String FILE_1 = "FILE_1.PDF";
        String FILE_2 = "FILE_2.PDF";
        String COD_DEPENDENCIA = "CD01";

        MultipartFormDataInput files = PartUtils.newMultiPart()
                .addPart("codigoDependencia", COD_DEPENDENCIA)
                .addBinaryPart(FILE_1, "HELLO PDF 1")
                .addBinaryPart(FILE_2, "HELLO PDF 2")
                .build();

        // when
        Response response = ecmClient.subirDocumentosPorArchivar(files);

        // then
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        MensajeRespuesta respuesta = (MensajeRespuesta) response.getEntity();
        assertThat(respuesta.getCodMensaje()).isEqualTo(COD_MENSAJE);
    }

    @Test
    public void restablecerArchivarDocumentoTask() {
        // given
        String ID_PROCESO = "IP01";
        String ID_TAREA = "IT01";
        String path = "/tarea-web-api/tarea/" + ID_PROCESO + "/" + ID_TAREA;

        JaxRsUtils.mockGetPath(wt, path);

        // when
        ecmClient.restablecerArchivarDocumentoTask(ID_PROCESO, ID_TAREA);

        // then
        verify(client).target(CORRESPONDENCIA_ENDPOINT);
        verify(wt).path(path);
    }
}