package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.CargaMasivaClient;
import co.com.foundation.test.mocks.ApiUtils;
import co.com.foundation.test.mocks.JaxRsUtils;
import co.com.foundation.test.mocks.PartUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CargaMasivaGatewayApiTest {

    private final String JSON_CONTENT = "{\"data\": 100}";

    private CargaMasivaGatewayApi gatewayApi;

    private CargaMasivaClient client;

    @Before
    public void setup() {

        gatewayApi = new CargaMasivaGatewayApi();

        client = mock(CargaMasivaClient.class);

        ReflectionTestUtils.setField(gatewayApi, "client", client);
    }

    @Test
    public void listCargaMasiva() {

        // given
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.listCargaMasiva()).thenReturn(theResponse);

        // when
        Response response = gatewayApi.listCargaMasiva();

        // then
        ApiUtils.assertThat(CargaMasivaGatewayApi.class, "listCargaMasiva")
                .hasGetMapping("/carga-masiva-gateway-api/listadocargamasiva")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON);

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void listEstadoCargaMasiva() {

        // given
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.listEstadoCargaMasiva()).thenReturn(theResponse);

        // when
        Response response = gatewayApi.listEstadoCargaMasiva();

        // then
        ApiUtils.assertThat(CargaMasivaGatewayApi.class, "listEstadoCargaMasiva")
                .hasGetMapping("/carga-masiva-gateway-api/estadocargamasiva")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON);

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void listEstadoCargaMasivaDadoId() {

        // given
        String CARGA_MASIVA_ID = "CMI01";
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.listEstadoCargaMasivaDadoId(CARGA_MASIVA_ID)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.listEstadoCargaMasivaDadoId(CARGA_MASIVA_ID);

        // then
        ApiUtils.assertThat(CargaMasivaGatewayApi.class, "listEstadoCargaMasivaDadoId")
                .hasGetMapping("/carga-masiva-gateway-api/estadocargamasiva/{id}")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON);

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void cargarDocumento() {
        // given
        String FILE_NAME = "file.pdf";
        String DATA = "PDF/DATA";
        MultipartFormDataInput FILES = PartUtils.newMultiPart()
                .addBinaryPart("file", FILE_NAME, DATA)
                .build();

        String COD_SEDE = "CS01";
        String COD_DEPENDENCIA = "CD01";
        String COD_FUNCIONARIO_RADICA = "CFR01";

        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.cargarDocumento(any(InputPart.class), anyString(), anyString(), anyString(), anyString())).thenReturn(theResponse);

        // when
        Response response = gatewayApi.cargarDocumento(COD_SEDE, COD_DEPENDENCIA, COD_FUNCIONARIO_RADICA, FILES);

        // then
        verify(client).cargarDocumento(PartUtils.extractFrom(FILES, "file").get(0), COD_SEDE, COD_DEPENDENCIA, COD_FUNCIONARIO_RADICA, FILE_NAME);

        ApiUtils.assertThat(CargaMasivaGatewayApi.class, "cargarDocumento")
                .hasPostMapping("/carga-masiva-gateway-api/cargar-fichero/{codigoSede}/{codigoDependencia}/{codfunRadica}")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.MULTIPART_FORM_DATA)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }
}