package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.ProductosClient;
import co.com.foundation.test.mocks.ApiUtils;
import co.com.foundation.test.mocks.JaxRsUtils;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductosGatewayApiTest {

    @Test
    public void list() {

        // given
        ProductosGatewayApi gatewayApi = new ProductosGatewayApi();
        String CONTENT = "{\"data\":100}";

        Response theResponse = JaxRsUtils.mockResponse(String.class, CONTENT, Response.Status.OK);

        ProductosClient productosClient = mock(ProductosClient.class);
        when(productosClient.list()).thenReturn(theResponse);

        ReflectionTestUtils.setField(gatewayApi, "productosClient", productosClient);

        // when
        Response response = gatewayApi.list();

        // then
        ApiUtils.assertThat(ProductosGatewayApi.class, "list")
                .hasGetMapping("/productos-gateway-api")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(CONTENT);
    }
}