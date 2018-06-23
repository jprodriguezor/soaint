package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.TipoComunicacionClient;
import co.com.foundation.test.mocks.ApiUtils;
import co.com.foundation.test.mocks.JaxRsUtils;
import co.com.foundation.test.rules.PropertiesLoaderRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class TipoComunicacionSalidaGatewayApiTest {

    @Rule
    public PropertiesLoaderRule properties = PropertiesLoaderRule.from("sgd-service.properties");


    @Test
    public void list() {
        // given
        TipoComunicacionSalidaGatewayApi gatewayApi = new TipoComunicacionSalidaGatewayApi();
        String CONTENT = "{\"data\":100}";
        String PARENT_CODE = properties.get("contants.tipocomunicacion.enviada.value");

        Response theResponse = JaxRsUtils.mockResponse(String.class, CONTENT, Response.Status.OK);

        TipoComunicacionClient tipoComunicacionClient = mock(TipoComunicacionClient.class);
        when(tipoComunicacionClient.list(anyString())).thenReturn(theResponse);

        ReflectionTestUtils.setField(gatewayApi, "tipoComunicacionClient", tipoComunicacionClient);
        ReflectionTestUtils.setField(gatewayApi, "tipoCoValue", PARENT_CODE);

        // when
        Response response = gatewayApi.list();

        // then
        ApiUtils.assertThat(TipoComunicacionSalidaGatewayApi.class, "list")
                .hasGetMapping("/tipo-comunicacion-salida-gateway-api")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        verify(tipoComunicacionClient).list(PARENT_CODE);
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(CONTENT);
    }
}