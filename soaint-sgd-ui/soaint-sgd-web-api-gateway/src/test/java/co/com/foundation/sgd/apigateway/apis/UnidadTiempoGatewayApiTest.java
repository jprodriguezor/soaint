package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.UnidadTiempoClient;
import co.com.foundation.test.mocks.ApiUtils;
import co.com.foundation.test.mocks.JaxRsUtils;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UnidadTiempoGatewayApiTest {

    @Test
    public void list() {
        // given
        UnidadTiempoGatewayApi gatewayApi = new UnidadTiempoGatewayApi();
        String CONTENT = "{\"data\":100}";

        Response theResponse = JaxRsUtils.mockResponse(String.class, CONTENT, Response.Status.OK);

        UnidadTiempoClient tratamientoCortesiaClient = mock(UnidadTiempoClient.class);
        when(tratamientoCortesiaClient.list()).thenReturn(theResponse);

        ReflectionTestUtils.setField(gatewayApi, "unidadTiempoClient", tratamientoCortesiaClient);

        // when
        Response response = gatewayApi.list();

        // then
        ApiUtils.assertThat(UnidadTiempoGatewayApi.class, "list")
                .hasGetMapping("/unidad-tiempo-gateway-api")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(CONTENT);
    }
}