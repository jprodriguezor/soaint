package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.DependeciaGrupoClient;
import co.com.foundation.test.mocks.ApiUtils;
import co.com.foundation.test.mocks.JaxRsUtils;
import co.com.foundation.test.rules.EnvironmentRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DependenciaGrupoGatewayApiTest {

    private final String JSON_CONTENT = "{\"data\": 100}";

    @Rule
    public EnvironmentRule environmentRule = new EnvironmentRule();

    private DependenciaGrupoGatewayApi gatewayApi;

    private DependeciaGrupoClient client;


    @Before
    public void setup() {
        gatewayApi = new DependenciaGrupoGatewayApi();

        client = mock(DependeciaGrupoClient.class);

        ReflectionTestUtils.setField(gatewayApi, "client", client);
    }

    @Test
    public void listBySedeAdministrativa() {
        // given
        String COD_SEDE_ADMINISTRATIVA = "CSA01";
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.listBySedeAdministrativa(COD_SEDE_ADMINISTRATIVA)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.listBySedeAdministrativa(COD_SEDE_ADMINISTRATIVA);

        // then
        ApiUtils.assertThat(DependenciaGrupoGatewayApi.class, "listBySedeAdministrativa")
                .hasGetMapping("/dependencia-grupo-gateway-api/{cod-sede-administrativa}")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void obtnerPorCodigo() {
        // given
        String CODIGO = "C001";
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.obtenerPorCodigo(CODIGO)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.obtnerPorCodigo(CODIGO);

        // then
        ApiUtils.assertThat(DependenciaGrupoGatewayApi.class, "obtnerPorCodigo")
                .hasGetMapping("/dependencia-grupo-gateway-api/dependencias/{codigo}")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void obtnerPorDependencia() {
        // given
        String CODIGOS_DEPENDENCIA = "C001";
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.obtenerPorDependencias(CODIGOS_DEPENDENCIA)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.obtnerPorDependencia(CODIGOS_DEPENDENCIA);

        // then
        ApiUtils.assertThat(DependenciaGrupoGatewayApi.class, "obtnerPorDependencia")
                .hasGetMapping("/dependencia-grupo-gateway-api/dependencias")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasQueryParams("codigos")
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void listarDendencias() {
        // given
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.listarDependencias()).thenReturn(theResponse);

        // when
        Response response = gatewayApi.listarDendencias();

        // then
        ApiUtils.assertThat(DependenciaGrupoGatewayApi.class, "listarDendencias")
                .hasGetMapping("/dependencia-grupo-gateway-api/all-dependencias")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }
}