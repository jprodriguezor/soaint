package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.ActuaCalidadClient;
import co.com.foundation.test.mocks.ApiUtils;
import co.com.foundation.test.mocks.JaxRsUtils;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ActuaCalidadGatewayApiTest {


    @Test
    public void list() {

        // given
        ActuaCalidadGatewayApi gatewayApi = new ActuaCalidadGatewayApi();
        String CONTENT = "{\"data\":100}";

        Response theResponse = JaxRsUtils.mockResponse(String.class, CONTENT, Response.Status.OK);

        ActuaCalidadClient actuaCalidadClient = mock(ActuaCalidadClient.class);
        when(actuaCalidadClient.list()).thenReturn(theResponse);

        ReflectionTestUtils.setField(gatewayApi, "actuaCalidadClienta", actuaCalidadClient);

        // when
        Response response = gatewayApi.list();

        // then
        ApiUtils.assertThat(ActuaCalidadGatewayApi.class, "list")
                .hasGetMapping("/actua-calidad-gateway-api")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(CONTENT);

    }
}