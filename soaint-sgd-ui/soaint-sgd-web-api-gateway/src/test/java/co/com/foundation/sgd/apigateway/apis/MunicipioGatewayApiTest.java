package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.MunicipioClient;
import co.com.foundation.test.mocks.ApiUtils;
import co.com.foundation.test.mocks.JaxRsUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MunicipioGatewayApiTest {

    private final String JSON_CONTENT = "{\"data\": 100}";

    private MunicipioGatewayApi gatewayApi;

    private MunicipioClient client;

    @Before
    public void setup() {

        gatewayApi = new MunicipioGatewayApi();

        client = mock(MunicipioClient.class);

        ReflectionTestUtils.setField(gatewayApi, "municipioClient", client);
    }

    @Test
    public void listarMunicipiosActivos() {
        // given
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.listarMunicipiosActivos()).thenReturn(theResponse);

        // when
        Response response = gatewayApi.listarMunicipiosActivos();

        // then
        ApiUtils.assertThat(MunicipioGatewayApi.class, "listarMunicipiosActivos")
                .hasGetMapping("/municipio-gateway-api")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON);

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void list() {
        // given
        String DEPARTAMENTO = "DPTO10";
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.listarPorDepartamento(DEPARTAMENTO)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.list(DEPARTAMENTO);

        // then
        ApiUtils.assertThat(MunicipioGatewayApi.class, "list")
                .hasGetMapping("/municipio-gateway-api/{departamento}")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void listarMunicipiosByCodidos() {
        // given
        String CODIGOS = "CD01";
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.listarMunicipiosPorCodigo(CODIGOS)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.listarMunicipiosByCodidos(CODIGOS);

        // then
        ApiUtils.assertThat(MunicipioGatewayApi.class, "listarMunicipiosByCodidos")
                .hasGetMapping("/municipio-gateway-api/municipios")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasQueryParams("codigos");

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }
}