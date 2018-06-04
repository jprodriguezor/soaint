package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.DepartamentoClient;
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

public class DepartamentoGatewayApiTest {

    private final String JSON_CONTENT = "{\"data\": 100}";

    private DepartamentoClient client;

    private DepartamentoGatewayApi gatewayApi;

    @Before
    public void setup() {
        gatewayApi = new DepartamentoGatewayApi();

        client = mock(DepartamentoClient.class);

        ReflectionTestUtils.setField(gatewayApi, "client", client);
    }

    @Test
    public void list() {
        // given
        String PAIS = "PAIS";
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.listarPorPais(PAIS)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.list(PAIS);

        // then
        ApiUtils.assertThat(DepartamentoGatewayApi.class, "list")
                .hasGetMapping("/departamento-gateway-api/{pais}")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void listarDepartamentosActivos() {
        // given
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.listarDepartamentosActivos()).thenReturn(theResponse);

        // when
        Response response = gatewayApi.listarDepartamentosActivos();

        // then
        ApiUtils.assertThat(DepartamentoGatewayApi.class, "listarDepartamentosActivos")
                .hasGetMapping("/departamento-gateway-api")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }
}