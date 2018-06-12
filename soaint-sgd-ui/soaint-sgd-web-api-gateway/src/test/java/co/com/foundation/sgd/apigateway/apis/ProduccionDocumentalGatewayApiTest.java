package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.ECMClient;
import co.com.foundation.sgd.apigateway.apis.delegator.ProduccionDocumentalClient;
import co.com.foundation.test.mocks.ApiUtils;
import co.com.foundation.test.mocks.JaxRsUtils;
import co.com.foundation.test.mocks.PartUtils;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.ecm.DocumentoDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import org.hamcrest.MatcherAssert;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.isJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

public class ProduccionDocumentalGatewayApiTest {

    private final String JSON_CONTENT = "{\"data\": 100}";

    private ProduccionDocumentalGatewayApi gatewayApi;

    private ProduccionDocumentalClient client;

    private ECMClient clientECM;

    @Before
    public void setUp() {

        gatewayApi = new ProduccionDocumentalGatewayApi();

        client = mock(ProduccionDocumentalClient.class);
        clientECM = mock(ECMClient.class);

        ReflectionTestUtils.setField(gatewayApi, "client", client);
        ReflectionTestUtils.setField(gatewayApi, "clientECM", clientECM);
    }

    @Test
    public void ejecutarProyeccionMultiple() {

        // given
        EntradaProcesoDTO dto = mock(EntradaProcesoDTO.class);
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.ejecutarProyeccionMultiple(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.ejecutarProyeccionMultiple(dto);

        // then
        ApiUtils.assertThat(ProduccionDocumentalGatewayApi.class, "ejecutarProyeccionMultiple")
                .hasPostMapping("/produccion-documental-gateway-api/ejecutar-proyeccion-multiple")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void ejecutarProyeccionMultipleFail() {

        // given
        EntradaProcesoDTO dto = mock(EntradaProcesoDTO.class);
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT, Response.Status.NO_CONTENT);
        when(client.ejecutarProyeccionMultiple(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.ejecutarProyeccionMultiple(dto);

        // then
        ApiUtils.assertThat(ProduccionDocumentalGatewayApi.class, "ejecutarProyeccionMultiple")
                .hasPostMapping("/produccion-documental-gateway-api/ejecutar-proyeccion-multiple")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(new ArrayList<>());
    }

    @Test
    public void obtenerDatosDocumento() {

        // given
        String NRO_RADICADO = "NRO01";
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.obtenerDatosDocumentoPorNroRadicado(NRO_RADICADO)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.obtenerDatosDocumento(NRO_RADICADO);

        // then
        ApiUtils.assertThat(ProduccionDocumentalGatewayApi.class, "obtenerDatosDocumento")
                .hasGetMapping("/produccion-documental-gateway-api/datos-documento/{nro_radicado}")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);

    }


    @Test
    public void obtenerDatosDocumentoFail() {

        // given
        String NRO_RADICADO = "NRO01";
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT, Response.Status.NO_CONTENT);
        when(client.obtenerDatosDocumentoPorNroRadicado(NRO_RADICADO)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.obtenerDatosDocumento(NRO_RADICADO);

        // then
        ApiUtils.assertThat(ProduccionDocumentalGatewayApi.class, "obtenerDatosDocumento")
                .hasGetMapping("/produccion-documental-gateway-api/datos-documento/{nro_radicado}")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(new ArrayList<>());

    }

    @Test
    public void versionarDocumento() {
        // given
        String COD_DEPENDENCIA = "CD01";
        String FILE_NAME = "file.pdf";
        String DATA = "PDF/DATA";
        String ID_DOCUMENTO = "ID01";
        String SEDE = "SEDE";
        String DEPENDENCIA = "DEP01";
        String SELECTOR = "SEL01";
        String NRO_RADICADO = "NR01";

        MultipartFormDataInput FILES = PartUtils.newMultiPart()
                .addPart("idDocumento", ID_DOCUMENTO)
                .addPart("nombreDocumento", FILE_NAME)
                .addPart("tipoDocumento", "application/json")
                .addPart("sede", SEDE)
                .addPart("codigoDependencia", COD_DEPENDENCIA)
                .addPart("dependencia", DEPENDENCIA)
                .addPart("selector", SELECTOR)
                .addPart("nroRadicado", NRO_RADICADO)
                .addBinaryPart("documento", FILE_NAME, DATA)
                .build();

        MensajeRespuesta theResponse = mock(MensajeRespuesta.class);
        when(clientECM.uploadVersionDocumento(any(DocumentoDTO.class), anyString())).thenReturn(theResponse);

        // when
        Response response = gatewayApi.versionarDocumento(FILES);

        // then
        ApiUtils.assertThat(ProduccionDocumentalGatewayApi.class, "versionarDocumento")
                .hasPostMapping("/produccion-documental-gateway-api/versionar-documento")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.MULTIPART_FORM_DATA)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(theResponse);

        ArgumentCaptor<DocumentoDTO> captor = ArgumentCaptor.forClass(DocumentoDTO.class);
        verify(clientECM).uploadVersionDocumento(captor.capture(), anyString());

        DocumentoDTO uploadedDocument = captor.getValue();

        assertThat(uploadedDocument.getDocumento()).isNotNull();
        assertThat(uploadedDocument.getIdDocumento()).isEqualTo(ID_DOCUMENTO);
        assertThat(uploadedDocument.getNombreDocumento()).isEqualTo(FILE_NAME);
        assertThat(uploadedDocument.getSede()).isEqualTo(SEDE);
        assertThat(uploadedDocument.getCodigoDependencia()).isEqualTo(COD_DEPENDENCIA);
        assertThat(uploadedDocument.getDependencia()).isEqualTo(DEPENDENCIA);
        assertThat(uploadedDocument.getNroRadicado()).isEqualTo(NRO_RADICADO);
    }

    @Test
    public void versionarDocumentoFail() {
        // given
        String COD_DEPENDENCIA = "CD01";
        String FILE_NAME = "file.pdf";
        String DATA = "PDF/DATA";
        String ID_DOCUMENTO = "ID01";
        String SEDE = "SEDE";
        String DEPENDENCIA = "DEP01";
        String SELECTOR = "SEL01";
        String NRO_RADICADO = "NR01";

        MultipartFormDataInput FILES = PartUtils.newMultiPart()
                .addPart("idDocumento", ID_DOCUMENTO)
                .addPart("nombreDocumento", FILE_NAME)
                .addPart("tipoDocumento", "application/json")
                .addPart("sede", SEDE)
                .addPart("codigoDependencia", COD_DEPENDENCIA)
                .addPart("dependencia", DEPENDENCIA)
                .addPart("selector", SELECTOR)
                .addPart("nroRadicado", NRO_RADICADO)
                .addBinaryPart("documento", FILE_NAME, DATA)
                .build();

        when(clientECM.uploadVersionDocumento(any(DocumentoDTO.class), anyString())).thenThrow(RuntimeException.class);

        // when
        Response response = gatewayApi.versionarDocumento(FILES);

        // then
        ApiUtils.assertThat(ProduccionDocumentalGatewayApi.class, "versionarDocumento")
                .hasPostMapping("/produccion-documental-gateway-api/versionar-documento")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.MULTIPART_FORM_DATA)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        assertThat(response.getEntity().toString()).satisfies(respuesta -> {
            MatcherAssert.assertThat(respuesta, isJson());
            MatcherAssert.assertThat(respuesta, hasJsonPath("$.codMensaje", equalTo("9999")));
        });
    }

    @Test
    public void agregarAnexo() {
        // given
        String COD_DEPENDENCIA = "CD01";
        String FILE_NAME = "file.pdf";
        String DATA = "PDF/DATA";
        String ID_DOCUMENTO_PADRE = "ID01";
        String SEDE = "SEDE";
        String DEPENDENCIA = "DEP01";
        String NRO_RADICADO = "NR01";

        MultipartFormDataInput FILES = PartUtils.newMultiPart()
                .addPart("idDocumentoPadre", ID_DOCUMENTO_PADRE)
                .addPart("nombreDocumento", FILE_NAME)
                .addPart("tipoDocumento", "application/json")
                .addPart("sede", SEDE)
                .addPart("codigoDependencia", COD_DEPENDENCIA)
                .addPart("dependencia", DEPENDENCIA)
                .addPart("nroRadicado", NRO_RADICADO)
                .addBinaryPart("documento", FILE_NAME, DATA)
                .build();

        MensajeRespuesta theResponse = mock(MensajeRespuesta.class);
        when(clientECM.uploadDocumentoAnexo(any(DocumentoDTO.class))).thenReturn(theResponse);

        // when
        Response response = gatewayApi.agregarAnexo(FILES);

        // then
        ApiUtils.assertThat(ProduccionDocumentalGatewayApi.class, "agregarAnexo")
                .hasPostMapping("/produccion-documental-gateway-api/agregar-anexo")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.MULTIPART_FORM_DATA)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(theResponse);

        ArgumentCaptor<DocumentoDTO> captor = ArgumentCaptor.forClass(DocumentoDTO.class);
        verify(clientECM).uploadDocumentoAnexo(captor.capture());

        DocumentoDTO uploadedDocument = captor.getValue();

        assertThat(uploadedDocument.getDocumento()).as("Documento").isNotNull();
        assertThat(uploadedDocument.getIdDocumentoPadre()).as("Id documento").isEqualTo(ID_DOCUMENTO_PADRE);
        assertThat(uploadedDocument.getNombreDocumento()).as("Nombre Documento").isEqualTo(FILE_NAME);
        assertThat(uploadedDocument.getSede()).as("Sede").isEqualTo(SEDE);
        assertThat(uploadedDocument.getCodigoDependencia()).as("Codigo dependencia").isEqualTo(COD_DEPENDENCIA);
        assertThat(uploadedDocument.getDependencia()).as("dependencia").isEqualTo(DEPENDENCIA);
        assertThat(uploadedDocument.getNroRadicado()).as("Numero radicado").isEqualTo(NRO_RADICADO);
    }

    @Test
    public void agregarAnexoFail() {
        // given
        String COD_DEPENDENCIA = "CD01";
        String FILE_NAME = "file.pdf";
        String DATA = "PDF/DATA";
        String ID_DOCUMENTO_PADRE = "ID01";
        String SEDE = "SEDE";
        String DEPENDENCIA = "DEP01";
        String NRO_RADICADO = "NR01";

        MultipartFormDataInput FILES = PartUtils.newMultiPart()
                .addPart("idDocumentoPadre", ID_DOCUMENTO_PADRE)
                .addPart("nombreDocumento", FILE_NAME)
                .addPart("tipoDocumento", "application/json")
                .addPart("sede", SEDE)
                .addPart("codigoDependencia", COD_DEPENDENCIA)
                .addPart("dependencia", DEPENDENCIA)
                .addPart("nroRadicado", NRO_RADICADO)
                .addBinaryPart("documento", FILE_NAME, DATA)
                .build();

        when(clientECM.uploadDocumentoAnexo(any(DocumentoDTO.class))).thenThrow(RuntimeException.class);

        // when
        Response response = gatewayApi.agregarAnexo(FILES);

        // then
        ApiUtils.assertThat(ProduccionDocumentalGatewayApi.class, "agregarAnexo")
                .hasPostMapping("/produccion-documental-gateway-api/agregar-anexo")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.MULTIPART_FORM_DATA)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        assertThat(response.getEntity().toString()).satisfies(respuesta -> {
            MatcherAssert.assertThat(respuesta, isJson());
            MatcherAssert.assertThat(respuesta, hasJsonPath("$.codMensaje", equalTo("9999")));
        });
    }

    @Test
    public void eliminarVersionDocumento() {

        // given
        String ID_DOCUMENTO = "ID01";
        Boolean theResponse = true;
        when(clientECM.eliminarVersionDocumento(ID_DOCUMENTO)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.eliminarVersionDocumento(ID_DOCUMENTO);

        // then
        ApiUtils.assertThat(ProduccionDocumentalGatewayApi.class, "eliminarVersionDocumento")
                .hasDeleteMapping("/produccion-documental-gateway-api/eliminar-version/{identificadorDoc}")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity().toString()).satisfies(respuesta -> {
            MatcherAssert.assertThat(respuesta, isJson());
            MatcherAssert.assertThat(respuesta, hasJsonPath("$.codMensaje", equalTo("0000")));
            MatcherAssert.assertThat(respuesta, hasJsonPath("$.mensaje", equalTo("El documento fue eliminado satisfactoriamente")));
        });
    }

    @Test
    public void eliminarVersionDocumentoFail() {

        // given
        String ID_DOCUMENTO = "ID01";
        when(clientECM.eliminarVersionDocumento(ID_DOCUMENTO)).thenThrow(RuntimeException.class);

        // when
        Response response = gatewayApi.eliminarVersionDocumento(ID_DOCUMENTO);

        // then
        ApiUtils.assertThat(ProduccionDocumentalGatewayApi.class, "eliminarVersionDocumento")
                .hasDeleteMapping("/produccion-documental-gateway-api/eliminar-version/{identificadorDoc}")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        assertThat(response.getEntity().toString()).satisfies(respuesta -> {
            MatcherAssert.assertThat(respuesta, isJson());
            MatcherAssert.assertThat(respuesta, hasJsonPath("$.codMensaje", equalTo("9999")));
        });
    }

    @Test
    public void eliminarAnexo() {

        // given
        String ID_DOCUMENTO = "ID01";
        Boolean theResponse = true;
        when(clientECM.eliminarVersionDocumento(ID_DOCUMENTO)).thenReturn(theResponse);

        ProduccionDocumentalGatewayApi spyGatewayApi = spy(gatewayApi);

        // when
        spyGatewayApi.eliminarAnexo(ID_DOCUMENTO);

        // then
        ApiUtils.assertThat(ProduccionDocumentalGatewayApi.class, "eliminarAnexo")
                .hasDeleteMapping("/produccion-documental-gateway-api/eliminar-anexo/{identificadorDoc}")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        verify(spyGatewayApi).eliminarVersionDocumento(ID_DOCUMENTO);
    }

    @Test
    public void obtenerVersionesDocumento() {
        // given
        String ID_DOCUMENTO = "ID01";
        MensajeRespuesta respuesta = mock(MensajeRespuesta.class);
        when(clientECM.obtenerVersionesDocumento(ID_DOCUMENTO)).thenReturn(respuesta);

        // when
        Response response = gatewayApi.obtenerVersionesDocumento(ID_DOCUMENTO);

        // then
        ApiUtils.assertThat(ProduccionDocumentalGatewayApi.class, "obtenerVersionesDocumento")
                .hasGetMapping("/produccion-documental-gateway-api/obtener-versiones-documento/{identificadorDoc}")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(respuesta);
    }

    @Test
    public void obtenerVersionesDocumentoFail() {
        // given
        String ID_DOCUMENTO = "ID01";
        when(clientECM.obtenerVersionesDocumento(ID_DOCUMENTO)).thenThrow(RuntimeException.class);

        // when
        Response response = gatewayApi.obtenerVersionesDocumento(ID_DOCUMENTO);

        // then
        ApiUtils.assertThat(ProduccionDocumentalGatewayApi.class, "obtenerVersionesDocumento")
                .hasGetMapping("/produccion-documental-gateway-api/obtener-versiones-documento/{identificadorDoc}")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        assertThat(response.getEntity().toString()).satisfies(respuesta -> {
            MatcherAssert.assertThat(respuesta, isJson());
            MatcherAssert.assertThat(respuesta, hasJsonPath("$.codMensaje", equalTo("9999")));
        });
    }
}