package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.TipoDestinatarioClient;
import co.com.foundation.test.mocks.ApiUtils;
import co.com.foundation.test.mocks.JaxRsUtils;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TipoDestinatarioGatewayApiTest {

    @Test
    public void list() {
        // given
        TipoDestinatarioGatewayApi gatewayApi = new TipoDestinatarioGatewayApi();
        String CONTENT = "{\"data\":100}";

        Response theResponse = JaxRsUtils.mockResponse(String.class, CONTENT, Response.Status.OK);

        TipoDestinatarioClient tipoAnexosClient = mock(TipoDestinatarioClient.class);
        when(tipoAnexosClient.list()).thenReturn(theResponse);

        ReflectionTestUtils.setField(gatewayApi, "client", tipoAnexosClient);

        // when
        Response response = gatewayApi.list();

        // then
        ApiUtils.assertThat(TipoDestinatarioGatewayApi.class, "list")
                .hasGetMapping("/tipo-destinatario-gateway-api")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(CONTENT);
    }
}