package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.ECMClient;
import co.com.foundation.test.mocks.ApiUtils;
import co.com.foundation.test.mocks.JaxRsUtils;
import co.com.soaint.foundation.canonical.ecm.ContenidoDependenciaTrdDTO;
import co.com.soaint.foundation.canonical.ecm.DisposicionFinalDTO;
import co.com.soaint.foundation.canonical.ecm.UnidadDocumentalDTO;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UnidadDocumentalGatewayApiTest {

    private final String JSON_CONTENT = "{\"data\": 100}";

    private UnidadDocumentalGatewayApi gatewayApi;

    private ECMClient ecmClient;


    @Before
    public void setUp() {

        gatewayApi = new UnidadDocumentalGatewayApi();

        ecmClient = mock(ECMClient.class);

        ReflectionTestUtils.setField(gatewayApi, "ecmClient", ecmClient);
    }

    @Test
    public void listarSerie() {
        // given
        ContenidoDependenciaTrdDTO dto = mock(ContenidoDependenciaTrdDTO.class);
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(ecmClient.listarSeriesSubseriePorDependencia(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.listarSerie(dto);

        //then
        ApiUtils.assertThat(UnidadDocumentalGatewayApi.class, "listarSerie")
                .hasPostMapping("/unidad-documental-gateway-api/listado-serie-subserie")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void crearUnidadDocumental() {
        // given
        UnidadDocumentalDTO dto = mock(UnidadDocumentalDTO.class);
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(ecmClient.crearUnidadDocumental(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.crearUnidadDocumental(dto);

        //then
        ApiUtils.assertThat(UnidadDocumentalGatewayApi.class, "crearUnidadDocumental")
                .hasPostMapping("/unidad-documental-gateway-api/crear-unidad-documental")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void listarUnidadDocumental() {
        // given
        UnidadDocumentalDTO dto = mock(UnidadDocumentalDTO.class);
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(ecmClient.listarUnidadesDocumentales(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.listarUnidadDocumental(dto);

        //then
        ApiUtils.assertThat(UnidadDocumentalGatewayApi.class, "listarUnidadDocumental")
                .hasPostMapping("/unidad-documental-gateway-api/listar-unidad-documental")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void abrirUnidadesDocumentales() {
        // given
        List<UnidadDocumentalDTO> dto = mock(List.class);
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(ecmClient.abrirCerrarReactivarUnidadDocumental(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.abrirUnidadesDocumentales(dto);

        //then
        ApiUtils.assertThat(UnidadDocumentalGatewayApi.class, "abrirUnidadesDocumentales")
                .hasPostMapping("/unidad-documental-gateway-api/gestionar-unidades-documentales")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void listarUnidadesDocumentalesDisposicion() {
        // given
        DisposicionFinalDTO dto = mock(DisposicionFinalDTO.class);
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(ecmClient.listarUnidadesDocumentalesDisposicion(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.listarUnidadesDocumentalesDisposicion(dto);

        //then
        ApiUtils.assertThat(UnidadDocumentalGatewayApi.class, "listarUnidadesDocumentalesDisposicion")
                .hasPostMapping("/unidad-documental-gateway-api/listar-unidades-documentales-disposicion")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void aprobarRechazarUnidadesDocumentalesDisposicion() {
        // given
        List<UnidadDocumentalDTO> dto = mock(List.class);
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(ecmClient.aprobarRechazarUnidadesDocumentalesDisposicion(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.aprobarRechazarUnidadesDocumentalesDisposicion(dto);

        //then
        ApiUtils.assertThat(UnidadDocumentalGatewayApi.class, "aprobarRechazarUnidadesDocumentalesDisposicion")
                .hasPostMapping("/unidad-documental-gateway-api/aprobar-rechazar-disposiciones-finales")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void detalleUnidadDocumental() {
        // given
        String ID_UNIDAD_DOCUMENTAL = "IUD01";
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(ecmClient.detalleUnidadDocumental(ID_UNIDAD_DOCUMENTAL)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.detalleUnidadDocumental(ID_UNIDAD_DOCUMENTAL);

        //then
        ApiUtils.assertThat(UnidadDocumentalGatewayApi.class, "detalleUnidadDocumental")
                .hasGetMapping("/unidad-documental-gateway-api/detalle-unidad-documental/{id}")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void documentosPorArchivar() {
        // given
        String CODIGO_DEPENDENCIA = "CD01";
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(ecmClient.documentosPorArchivar(CODIGO_DEPENDENCIA)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.documentosPorArchivar(CODIGO_DEPENDENCIA);

        //then
        ApiUtils.assertThat(UnidadDocumentalGatewayApi.class, "documentosPorArchivar")
                .hasGetMapping("/unidad-documental-gateway-api/listar-documentos-por-archivar/{codigoDependencia}")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void documentosArchivados() {
        // given
        String CODIGO_DEPENDENCIA = "CD01";
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(ecmClient.documentosArchivados(CODIGO_DEPENDENCIA)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.documentosArchivados(CODIGO_DEPENDENCIA);

        //then
        ApiUtils.assertThat(UnidadDocumentalGatewayApi.class, "documentosArchivados")
                .hasGetMapping("/unidad-documental-gateway-api/listar-documentos-archivados/{codigoDependencia}")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void subirDocumentosPorArchivar() {
        // given
        MultipartFormDataInput FORM = mock(MultipartFormDataInput.class);
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);

        when(ecmClient.subirDocumentosPorArchivar(FORM)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.subirDocumentosPorArchivar(FORM);

        //then
        ApiUtils.assertThat(UnidadDocumentalGatewayApi.class, "subirDocumentosPorArchivar")
                .hasPostMapping("/unidad-documental-gateway-api/subir-documentos-por-archivar")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.MULTIPART_FORM_DATA)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void modificarUnidadesDocumentales() {
        // given
        List<UnidadDocumentalDTO> dto = mock(List.class);
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(ecmClient.modificarUnidadesDocumentales(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.modificarUnidadesDocumentales(dto);

        //then
        ApiUtils.assertThat(UnidadDocumentalGatewayApi.class, "modificarUnidadesDocumentales")
                .hasPutMapping("/unidad-documental-gateway-api/modificar-unidades-documentales")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void subirDocumentosUnidadDocumental() {
        // given
        UnidadDocumentalDTO dto = mock(UnidadDocumentalDTO.class);
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(ecmClient.subirDocumentosUnidadDocumental(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.subirDocumentosUnidadDocumental(dto);

        //then
        ApiUtils.assertThat(UnidadDocumentalGatewayApi.class, "subirDocumentosUnidadDocumental")
                .hasPostMapping("/unidad-documental-gateway-api/subir-documentos-unidad-documental")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void restablecerArchivarDocumentoTask() {
        // given
        String PROCESO = "P01";
        String TAREA = "T01";
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(ecmClient.restablecerArchivarDocumentoTask(PROCESO, TAREA)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.restablecerArchivarDocumentoTask(PROCESO, TAREA);

        //then
        ApiUtils.assertThat(UnidadDocumentalGatewayApi.class, "restablecerArchivarDocumentoTask")
                .hasGetMapping("/unidad-documental-gateway-api/restablecer-archivar-documento-task/{proceso}/{tarea}")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void restablecerArchivarDocumentoTaskFail() {
        // given
        String PROCESO = "P01";
        String TAREA = "T01";
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT, Response.Status.INTERNAL_SERVER_ERROR);
        when(ecmClient.restablecerArchivarDocumentoTask(PROCESO, TAREA)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.restablecerArchivarDocumentoTask(PROCESO, TAREA);

        //then
        ApiUtils.assertThat(UnidadDocumentalGatewayApi.class, "restablecerArchivarDocumentoTask")
                .hasGetMapping("/unidad-documental-gateway-api/restablecer-archivar-documento-task/{proceso}/{tarea}")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(new ArrayList<>());
    }
}