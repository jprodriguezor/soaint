package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.SedeAdministrativaClient;
import co.com.foundation.test.mocks.ApiUtils;
import co.com.foundation.test.mocks.JaxRsUtils;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SedeAdministrativaGatewayApiTest {

    @Test
    public void list() {

        // given
        SedeAdministrativaGatewayApi gatewayApi = new SedeAdministrativaGatewayApi();
        String CONTENT = "{\"data\":100}";

        Response theResponse = JaxRsUtils.mockResponse(String.class, CONTENT, Response.Status.OK);

        SedeAdministrativaClient productosClient = mock(SedeAdministrativaClient.class);
        when(productosClient.list()).thenReturn(theResponse);

        ReflectionTestUtils.setField(gatewayApi, "client", productosClient);

        // when
        Response response = gatewayApi.list();

        // then
        ApiUtils.assertThat(SedeAdministrativaGatewayApi.class, "list")
                .hasGetMapping("/sede-administrativa-gateway-api")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(CONTENT);
    }
}