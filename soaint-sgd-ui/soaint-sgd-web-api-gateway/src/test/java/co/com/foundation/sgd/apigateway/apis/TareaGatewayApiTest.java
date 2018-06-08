package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.TareaClient;
import co.com.foundation.test.mocks.ApiUtils;
import co.com.foundation.test.mocks.JaxRsUtils;
import co.com.soaint.foundation.canonical.correspondencia.TareaDTO;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TareaGatewayApiTest {

    private final String JSON_CONTENT = "{\"data\": 100}";

    private TareaGatewayApi gatewayApi;

    private TareaClient tareaClient;


    @Before
    public void setUp() {

        gatewayApi = new TareaGatewayApi();

        tareaClient = mock(TareaClient.class);

        ReflectionTestUtils.setField(gatewayApi, "tareaClient", tareaClient);
    }

    @Test
    public void guardarEstadoTarea() {
        // given
        TareaDTO dto = mock(TareaDTO.class);
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(tareaClient.guardarEstadoTarea(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.guardarEstadoTarea(dto);

        //then
        ApiUtils.assertThat(TareaGatewayApi.class, "guardarEstadoTarea")
                .hasPostMapping("/tarea-gateway-api/tarea")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void listarEstadoTarea() {
        // given
        String ID_TAREA = "IT01";
        String ID_INSTANCIA = "II01";
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(tareaClient.listarEstadoTarea(ID_INSTANCIA, ID_TAREA)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.listarEstadoTarea(ID_INSTANCIA, ID_TAREA);

        //then
        ApiUtils.assertThat(TareaGatewayApi.class, "listarEstadoTarea")
                .hasGetMapping("/tarea-gateway-api/tarea/{id-instancia-proceso}/{id-tarea-proceso}")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }
}