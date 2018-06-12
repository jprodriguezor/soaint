package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.ECMClient;
import co.com.foundation.sgd.apigateway.apis.delegator.ECMUtils;
import co.com.foundation.test.mocks.ApiUtils;
import co.com.foundation.test.mocks.JaxRsUtils;
import co.com.foundation.test.mocks.PartUtils;
import co.com.soaint.foundation.canonical.ecm.DocumentoDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import org.hamcrest.MatcherAssert;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.isJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DigitalizarDocumentoGatewayApiTest {

    private final String JSON_CONTENT = "{\"data\": 100}";

    private DigitalizarDocumentoGatewayApi gatewayApi;

    private ECMClient client;

    @Before
    public void setup() {
        gatewayApi = new DigitalizarDocumentoGatewayApi();

        client = mock(ECMClient.class);

        ReflectionTestUtils.setField(gatewayApi, "client", client);
    }


    @Test
    public void digitalizar() {
        // given
        String ASSOCIATE_FILENAME = "associate.pdf";
        String PRINCIPAL_FILENAME = "file.pdf";
        String DEPENDENCIA = "DP01";
        String SEDE = "SD01";
        String TIPO_COMUNICACION = "TC01";
        String NRO_RADICADO = "NR01";
        String REFERIDO_1 = "RF01";
        String REFERIDO_2 = "RF02";
        String NOMBRE_RADICADO = "NOMBRE";
        MultipartFormDataInput FORM = PartUtils.newMultiPart()
                .addPart("principalFileName", PRINCIPAL_FILENAME)
                .addPart("dependencia", DEPENDENCIA)
                .addPart("sede", SEDE)
                .addPart("tipoComunicacion", TIPO_COMUNICACION)
                .addPart("nroRadicado", NRO_RADICADO)
                .addPart("nombreRemitente", NOMBRE_RADICADO)
                .addPartList("referidoList", REFERIDO_1, REFERIDO_2)
                .addBinaryPart(PRINCIPAL_FILENAME, "FILE DATA")
                .addBinaryPart(ASSOCIATE_FILENAME, "FILE DATA")
                .build();

        String COD_MENSAJE = "0000";
        String ID_DOCUMENTO = "ID01";
        List<DocumentoDTO> DOCUMENTO_LIST = Arrays.asList(
                DocumentoDTO.newInstance().idDocumento(ID_DOCUMENTO).build()
        );
        MensajeRespuesta  respuesta = MensajeRespuesta.newInstance()
                .codMensaje(COD_MENSAJE)
                .documentoDTOList(DOCUMENTO_LIST)
                .build();
        when(client.uploadDocument(any(DocumentoDTO.class), anyString())).thenReturn(respuesta);

        String ID_DOCUMENTO_ASSOCIATES = "IDA01";
        List<MensajeRespuesta> associates = Arrays.asList(MensajeRespuesta.newInstance()
                .codMensaje(COD_MENSAJE)
                .documentoDTOList(Arrays.asList(
                        DocumentoDTO.newInstance().idDocumento(ID_DOCUMENTO_ASSOCIATES).build()
                ))
                .build());
        when(client.uploadDocumentsAsociates(anyString(), any(), anyString(), anyString(), anyString(), anyString(), any())).thenReturn(associates);

        // when
        Response response = gatewayApi.digitalizar(FORM);

        // then
        ApiUtils.assertThat(DigitalizarDocumentoGatewayApi.class, "digitalizar")
                .hasPostMapping("/digitalizar-documento-gateway-api")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.MULTIPART_FORM_DATA);

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(Arrays.asList(ID_DOCUMENTO, ID_DOCUMENTO_ASSOCIATES));

        ArgumentCaptor<DocumentoDTO> captor = ArgumentCaptor.forClass(DocumentoDTO.class);
        verify(client).uploadDocument(captor.capture(), anyString());

        DocumentoDTO principalDocumento = captor.getValue();

        assertThat(principalDocumento.getDocumento()).isNotNull();
        assertThat(principalDocumento.getDependencia()).isEqualTo(DEPENDENCIA);
        assertThat(principalDocumento.getSede()).isEqualTo(SEDE);
        assertThat(principalDocumento.getTipoDocumento()).isEqualTo("application/pdf");
        assertThat(principalDocumento.getNombreDocumento()).isEqualTo(PRINCIPAL_FILENAME);
        assertThat(principalDocumento.getNroRadicado()).isEqualTo(NRO_RADICADO);
        assertThat(principalDocumento.getNroRadicadoReferido()).containsOnly(REFERIDO_1, REFERIDO_2);
        assertThat(principalDocumento.getNombreRemitente()).isEqualTo(NOMBRE_RADICADO);

        Map<String, InputPart> files = ECMUtils.findFiles(FORM);
        files.remove(PRINCIPAL_FILENAME);

        verify(client).uploadDocumentsAsociates(ID_DOCUMENTO, files, SEDE, DEPENDENCIA, TIPO_COMUNICACION, NRO_RADICADO, new String[] {REFERIDO_1, REFERIDO_2});
    }

    @Test
    public void digitalizarFail() {
        // given
        String ASSOCIATE_FILENAME = "associate.pdf";
        String PRINCIPAL_FILENAME = "file.pdf";
        String DEPENDENCIA = "DP01";
        String SEDE = "SD01";
        String TIPO_COMUNICACION = "TC01";
        String NRO_RADICADO = "NR01";
        String REFERIDO_1 = "RF01";
        String REFERIDO_2 = "RF02";
        String NOMBRE_RADICADO = "NOMBRE";
        MultipartFormDataInput FORM = PartUtils.newMultiPart()
                .addPart("principalFileName", PRINCIPAL_FILENAME)
                .addPart("dependencia", DEPENDENCIA)
                .addPart("sede", SEDE)
                .addPart("tipoComunicacion", TIPO_COMUNICACION)
                .addPart("nroRadicado", NRO_RADICADO)
                .addPart("nombreRemitente", NOMBRE_RADICADO)
                .addPartList("referidoList", REFERIDO_1, REFERIDO_2)
                .addBinaryPart(PRINCIPAL_FILENAME, "FILE DATA")
                .addBinaryPart(ASSOCIATE_FILENAME, "FILE DATA")
                .build();

        when(client.uploadDocument(any(DocumentoDTO.class), anyString())).thenThrow(RuntimeException.class);

        // when
        Response response = gatewayApi.digitalizar(FORM);

        // then
        ApiUtils.assertThat(DigitalizarDocumentoGatewayApi.class, "digitalizar")
                .hasPostMapping("/digitalizar-documento-gateway-api")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.MULTIPART_FORM_DATA);

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(null);
    }

    @Test
    public void versionarDocumento() {
        // given
        String DEPENDENCIA = "DP01";
        String SEDE = "SD01";
        String NRO_RADICADO = "NR01";
        String SELECTOR = "SELECTOR";
        String ID_DOCUMENTO = "ID01";
        String NOMBRE_DOCUMENTO = "DOC";
        String TIPO_DOCUMENTO = "application/pdf";
        MultipartFormDataInput FORM = PartUtils.newMultiPart()
                .addPart("sede", SEDE)
                .addPart("dependencia", DEPENDENCIA)
                .addPart("selector", SELECTOR)
                .addPart("nroRadicado", NRO_RADICADO)
                .addPart("idDocumento", ID_DOCUMENTO)
                .addPart("nombreDocumento", NOMBRE_DOCUMENTO)
                .addPart("tipoDocumento", TIPO_DOCUMENTO)
                .addBinaryPart("documento", "FILE DATA")
                .build();

        MensajeRespuesta respuesta = mock(MensajeRespuesta.class);
        when(client.uploadVersionDocumento(any(DocumentoDTO.class), anyString())).thenReturn(respuesta);

        // when
        Response response = gatewayApi.versionarDocumento(FORM);

        // then
        ApiUtils.assertThat(DigitalizarDocumentoGatewayApi.class, "versionarDocumento")
                .hasPostMapping("/digitalizar-documento-gateway-api/versionar-documento")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.MULTIPART_FORM_DATA)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(respuesta);


        ArgumentCaptor<DocumentoDTO> captor = ArgumentCaptor.forClass(DocumentoDTO.class);
        verify(client).uploadVersionDocumento(captor.capture(), anyString());

        DocumentoDTO uploadedDocumento = captor.getValue();
        assertThat(uploadedDocumento.getDocumento()).isNotNull();
        assertThat(uploadedDocumento.getDependencia()).isEqualTo(DEPENDENCIA);
        assertThat(uploadedDocumento.getSede()).isEqualTo(SEDE);
        assertThat(uploadedDocumento.getTipoDocumento()).isEqualTo(TIPO_DOCUMENTO);
        assertThat(uploadedDocumento.getNombreDocumento()).isEqualTo(NOMBRE_DOCUMENTO);
        assertThat(uploadedDocumento.getNroRadicado()).isEqualTo(NRO_RADICADO);
    }

    @Test
    public void versionarDocumentoFail() {
        // given
        String DEPENDENCIA = "DP01";
        String SEDE = "SD01";
        String NRO_RADICADO = "NR01";
        String SELECTOR = "SELECTOR";
        String ID_DOCUMENTO = "ID01";
        String NOMBRE_DOCUMENTO = "DOC";
        String TIPO_DOCUMENTO = "application/pdf";
        MultipartFormDataInput FORM = PartUtils.newMultiPart()
                .addPart("sede", SEDE)
                .addPart("dependencia", DEPENDENCIA)
                .addPart("selector", SELECTOR)
                .addPart("nroRadicado", NRO_RADICADO)
                .addPart("idDocumento", ID_DOCUMENTO)
                .addPart("nombreDocumento", NOMBRE_DOCUMENTO)
                .addPart("tipoDocumento", TIPO_DOCUMENTO)
                .addBinaryPart("documento", "FILE DATA")
                .build();

        when(client.uploadVersionDocumento(any(DocumentoDTO.class), anyString())).thenThrow(RuntimeException.class);

        // when
        Response response = gatewayApi.versionarDocumento(FORM);

        // then
        ApiUtils.assertThat(DigitalizarDocumentoGatewayApi.class, "versionarDocumento")
                .hasPostMapping("/digitalizar-documento-gateway-api/versionar-documento")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.MULTIPART_FORM_DATA)
                .hasJWTTokenSecurity();

        String errorMessage = (String) response.getEntity();
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        MatcherAssert.assertThat(errorMessage, isJson());
        MatcherAssert.assertThat(errorMessage, hasJsonPath("$.codMensaje", equalTo("9999")));
    }

    @Test
    public void constantes() {
        // given
        String ID_DOCUMENTO = "ID01";
        InputStream DOC = mock(InputStream.class);
        Response theResponse = JaxRsUtils.mockResponse(InputStream.class, DOC);
        when(client.findByIdDocument(ID_DOCUMENTO)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.constantes(ID_DOCUMENTO);

        // then
        ApiUtils.assertThat(DigitalizarDocumentoGatewayApi.class, "constantes")
                .hasGetMapping("/digitalizar-documento-gateway-api/obtener-documento/{idDocumento}")
                .produces(MediaType.APPLICATION_OCTET_STREAM);

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(DOC);
    }

    @Test
    public void obtenerdocumentosasociadosID() {
        // given
        String ID_DOCUMENTO = "ID01";
        MensajeRespuesta respuesta = mock(MensajeRespuesta.class);
        when(client.findDocumentosAsociadosID(ID_DOCUMENTO)).thenReturn(respuesta);

        // when
        Response response = gatewayApi.obtenerdocumentosasociadosID(ID_DOCUMENTO);

        // then
        ApiUtils.assertThat(DigitalizarDocumentoGatewayApi.class, "obtenerdocumentosasociadosID")
                .hasGetMapping("/digitalizar-documento-gateway-api/obtener-documentos-asociados/{idDocumento}")
                .produces(MediaType.APPLICATION_JSON);

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(respuesta);
    }

    @Test
    public void obtenerdocumentosasociadosNroRadicado() {
        // given
        String ID_DOCUMENTO = "ID01";
        MensajeRespuesta respuesta = mock(MensajeRespuesta.class);
        when(client.findDocumentosAsociadosRadicado(ID_DOCUMENTO)).thenReturn(respuesta);

        // when
        Response response = gatewayApi.obtenerdocumentosasociadosNroRadicado(ID_DOCUMENTO);

        // then
        ApiUtils.assertThat(DigitalizarDocumentoGatewayApi.class, "obtenerdocumentosasociadosNroRadicado")
                .hasGetMapping("/digitalizar-documento-gateway-api/obtener-documentos-asociados-radicado/{nroRadicado}")
                .produces(MediaType.APPLICATION_JSON);

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(respuesta);
    }

    @Test
    public void deleteDocumentById() {
        // given
        String ID_DOCUMENTO = "ID01";
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.deleteDocumentById(ID_DOCUMENTO)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.deleteDocumentById(ID_DOCUMENTO);

        // then
        ApiUtils.assertThat(DigitalizarDocumentoGatewayApi.class, "deleteDocumentById")
                .hasPostMapping("/digitalizar-documento-gateway-api/eliminarprincipal/{documentId}")
                .produces(MediaType.APPLICATION_OCTET_STREAM);

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void estamparEtiquetaRadicacion() {
        // given
        String COD_DEPENDENCIA = "DP01";
        String NRO_RADICADO = "NR01";
        String ID_DOCUMENTO = "ID01";
        MultipartFormDataInput FORM = PartUtils.newMultiPart()
                .addPart("codigoDependencia", COD_DEPENDENCIA)
                .addPart("nroRadicado", NRO_RADICADO)
                .addPart("idDocumento", ID_DOCUMENTO)
                .addBinaryPart("documento", "FILE DATA")
                .build();

        MensajeRespuesta respuesta = mock(MensajeRespuesta.class);
        when(client.estamparEtiquetaRadicacion(any(DocumentoDTO.class))).thenReturn(respuesta);

        // when
        Response response = gatewayApi.estamparEtiquetaRadicacion(FORM);

        // then
        ApiUtils.assertThat(DigitalizarDocumentoGatewayApi.class, "estamparEtiquetaRadicacion")
                .hasPostMapping("/digitalizar-documento-gateway-api/estampar-etiqueta-radicacion")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.MULTIPART_FORM_DATA)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(respuesta);
        ArgumentCaptor<DocumentoDTO> captor = ArgumentCaptor.forClass(DocumentoDTO.class);

        verify(client).estamparEtiquetaRadicacion(captor.capture());

        DocumentoDTO uploadedDocumento = captor.getValue();
        assertThat(uploadedDocumento.getDocumento()).isNotNull();
        assertThat(uploadedDocumento.getCodigoDependencia()).isEqualTo(COD_DEPENDENCIA);
        assertThat(uploadedDocumento.getIdDocumento()).isEqualTo(ID_DOCUMENTO);
        assertThat(uploadedDocumento.getNroRadicado()).isEqualTo(NRO_RADICADO);
    }


    @Test
    public void estamparEtiquetaRadicacionNullForm() {
        // given
        MultipartFormDataInput FORM = null;
        // when
        Response response = gatewayApi.estamparEtiquetaRadicacion(FORM);

        // then
        ApiUtils.assertThat(DigitalizarDocumentoGatewayApi.class, "estamparEtiquetaRadicacion")
                .hasPostMapping("/digitalizar-documento-gateway-api/estampar-etiqueta-radicacion")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.MULTIPART_FORM_DATA)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
    }

    @Test
    public void estamparEtiquetaRadicacionFail() {
        // given
        String COD_DEPENDENCIA = "DP01";
        String NRO_RADICADO = "NR01";
        String ID_DOCUMENTO = "ID01";
        MultipartFormDataInput FORM = PartUtils.newMultiPart()
                .addPart("codigoDependencia", COD_DEPENDENCIA)
                .addPart("nroRadicado", NRO_RADICADO)
                .addPart("idDocumento", ID_DOCUMENTO)
                .addBinaryPart("documento", "FILE DATA")
                .build();

        when(client.estamparEtiquetaRadicacion(any(DocumentoDTO.class))).thenThrow(RuntimeException.class);

        // when
        Response response = gatewayApi.estamparEtiquetaRadicacion(FORM);

        // then
        ApiUtils.assertThat(DigitalizarDocumentoGatewayApi.class, "estamparEtiquetaRadicacion")
                .hasPostMapping("/digitalizar-documento-gateway-api/estampar-etiqueta-radicacion")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.MULTIPART_FORM_DATA)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        MensajeRespuesta respuesta = (MensajeRespuesta) response.getEntity();

        assertThat(respuesta.getCodMensaje()).isEqualTo("1223");
    }
}