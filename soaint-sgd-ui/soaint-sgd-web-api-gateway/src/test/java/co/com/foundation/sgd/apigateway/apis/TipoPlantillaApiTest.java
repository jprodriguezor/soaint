package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.TipoPlantillaClient;
import co.com.foundation.test.mocks.ApiUtils;
import co.com.foundation.test.mocks.JaxRsUtils;
import co.com.foundation.test.rules.PropertiesLoaderRule;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.isJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TipoPlantillaApiTest {

    private final String JSON_CONTENT = "{\"data\": 100}";

    @Rule
    public PropertiesLoaderRule properties = PropertiesLoaderRule.from("sgd-service-test.properties");

    private TipoPlantillaApi gatewayApi;

    private TipoPlantillaClient client;

    private String location_output = properties.get("locations.tiposplantilla.output");


    @Before
    public void setUp() {

        gatewayApi = new TipoPlantillaApi();

        client = mock(TipoPlantillaClient.class);

        ReflectionTestUtils.setField(gatewayApi, "client", client);
        ReflectionTestUtils.setField(gatewayApi, "location_output", location_output);
    }

    @Test
    public void get() {
        // given
        String COD_CLASIFICACION = "CC01";
        String ESTADO = "ESTADO";
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.get(COD_CLASIFICACION, ESTADO)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.get(COD_CLASIFICACION, ESTADO);

        // then
        ApiUtils.assertThat(TipoPlantillaApi.class, "get")
                .hasGetMapping("/tipo-plantilla-gateway-api/plantilla/{codClasificacion}/{estado}")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();


        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void read() throws IOException {
        // given
        String COD_CLASIFICACION = "CC01";
        String theResponse = JSON_CONTENT;
        when(client.readFromFile(COD_CLASIFICACION)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.read(COD_CLASIFICACION);

        // then
        ApiUtils.assertThat(TipoPlantillaApi.class, "read")
                .hasGetMapping("/tipo-plantilla-gateway-api/obtener/{codClasificacion}")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON);


        assertThat(response.getStatus()).isEqualTo(Response.Status.ACCEPTED.getStatusCode());
        assertThat(response.getEntity().toString()).satisfies(respuesta -> {
            MatcherAssert.assertThat(respuesta, isJson());
            MatcherAssert.assertThat(respuesta, hasJsonPath("$.text", equalTo(JSON_CONTENT)));
            MatcherAssert.assertThat(respuesta, hasJsonPath("$.success", equalTo(true)));
        });
    }

    @Test
    public void readFail() throws IOException {
        // given
        String COD_CLASIFICACION = "CC01";
        when(client.readFromFile(COD_CLASIFICACION)).thenThrow(RuntimeException.class);

        // when
        Response response = gatewayApi.read(COD_CLASIFICACION);

        // then
        ApiUtils.assertThat(TipoPlantillaApi.class, "read")
                .hasGetMapping("/tipo-plantilla-gateway-api/obtener/{codClasificacion}")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON);


        assertThat(response.getStatus()).isEqualTo(Response.Status.ACCEPTED.getStatusCode());
        assertThat(response.getEntity().toString()).satisfies(respuesta -> {
            MatcherAssert.assertThat(respuesta, isJson());
            MatcherAssert.assertThat(respuesta, hasJsonPath("$.success", equalTo(false)));
        });
    }
}