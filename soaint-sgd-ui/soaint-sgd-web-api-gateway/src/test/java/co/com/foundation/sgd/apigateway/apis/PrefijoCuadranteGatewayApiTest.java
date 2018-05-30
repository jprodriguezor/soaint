package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.PrefijoCuadranteClient;
import co.com.foundation.test.mocks.ApiUtils;
import co.com.foundation.test.mocks.JaxRsUtils;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PrefijoCuadranteGatewayApiTest {

    @Test
    public void list() {

        // given
        PrefijoCuadranteGatewayApi gatewayApi = new PrefijoCuadranteGatewayApi();
        String CONTENT = "{\"data\":100}";

        Response theResponse = JaxRsUtils.mockResponse(String.class, CONTENT, Response.Status.OK);

        PrefijoCuadranteClient prefijoCuadranteClient = mock(PrefijoCuadranteClient.class);
        when(prefijoCuadranteClient.list()).thenReturn(theResponse);

        ReflectionTestUtils.setField(gatewayApi, "client", prefijoCuadranteClient);

        // when
        Response response = gatewayApi.list();

        // then
        ApiUtils.assertThat(PrefijoCuadranteGatewayApi.class, "list")
                .hasGetMapping("/prefijo-cuadrante-gateway-api")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(CONTENT);
    }
}