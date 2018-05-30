package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.TipoDocumentoClient;
import co.com.foundation.test.mocks.ApiUtils;
import co.com.foundation.test.mocks.JaxRsUtils;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TipoDocumentoGatewayApiTest {

    @Test
    public void list() {
        // given
        TipoDocumentoGatewayApi gatewayApi = new TipoDocumentoGatewayApi();
        String CONTENT = "{\"data\":100}";

        Response theResponse = JaxRsUtils.mockResponse(String.class, CONTENT, Response.Status.OK);

        TipoDocumentoClient tipoDocumentoClient = mock(TipoDocumentoClient.class);
        when(tipoDocumentoClient.list()).thenReturn(theResponse);

        ReflectionTestUtils.setField(gatewayApi, "client", tipoDocumentoClient);

        // when
        Response response = gatewayApi.list();

        // then
        ApiUtils.assertThat(TipoDocumentoGatewayApi.class, "list")
                .hasGetMapping("/tipo-documento-gateway-api")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(CONTENT);
    }
}