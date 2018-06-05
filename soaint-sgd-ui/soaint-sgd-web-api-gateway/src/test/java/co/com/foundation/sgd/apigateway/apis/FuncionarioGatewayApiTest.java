package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.FuncionarioClient;
import co.com.foundation.test.mocks.ApiUtils;
import co.com.foundation.test.mocks.JaxRsUtils;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FuncionarioGatewayApiTest {

    private final String JSON_CONTENT = "{\"data\": 100}";

    private FuncionarioGatewayApi gatewayApi;

    private FuncionarioClient client;

    @Before
    public void setUp() {

        gatewayApi = new FuncionarioGatewayApi();

        client = mock(FuncionarioClient.class);

        ReflectionTestUtils.setField(gatewayApi, "client", client);
    }

    @Test
    public void get() {
        // given
        String USERNAME = "USERNAME";
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.obtenerFuncionario(USERNAME)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.get(USERNAME);

        // then
        ApiUtils.assertThat(FuncionarioGatewayApi.class, "get")
                .hasGetMapping("/funcionario-gateway-api/{userName}")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void listarFuncionariosByLoginnames() {
        // given
        String LOGIN_NAMES = "USERNAME";
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.listarFuncionariosByLoginnames(LOGIN_NAMES)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.listarFuncionariosByLoginnames(LOGIN_NAMES);

        // then
        ApiUtils.assertThat(FuncionarioGatewayApi.class, "listarFuncionariosByLoginnames")
                .hasGetMapping("/funcionario-gateway-api/funcionarios/listar-by-loginnames")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasQueryParams("loginNames")
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void listarFuncionarios() {
        // given
        String COD_DEPENDENCIA = "CD01";
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.listarFuncionarios(COD_DEPENDENCIA)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.listarFuncionarios(COD_DEPENDENCIA);

        // then
        ApiUtils.assertThat(FuncionarioGatewayApi.class, "listarFuncionarios")
                .hasGetMapping("/funcionario-gateway-api/funcionarios/{cod_dependencia}")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void listarFuncionariosByRol() {
        // given
        String COD_DEPENDENCIA = "CD01";
        String ROL = "CD01";
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.listarFuncionariosPorRol(COD_DEPENDENCIA, ROL)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.listarFuncionariosByRol(COD_DEPENDENCIA, ROL);

        // then
        ApiUtils.assertThat(FuncionarioGatewayApi.class, "listarFuncionariosByRol")
                .hasGetMapping("/funcionario-gateway-api/funcionarios/{cod_dependencia}/{rol}")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void listarFuncionariosByRolFail() {
        // given
        String COD_DEPENDENCIA = "CD01";
        String ROL = "CD01";
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT, Response.Status.BAD_REQUEST);
        when(client.listarFuncionariosPorRol(COD_DEPENDENCIA, ROL)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.listarFuncionariosByRol(COD_DEPENDENCIA, ROL);

        // then
        ApiUtils.assertThat(FuncionarioGatewayApi.class, "listarFuncionariosByRol")
                .hasGetMapping("/funcionario-gateway-api/funcionarios/{cod_dependencia}/{rol}")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(new ArrayList<>());
    }

    @Test
    public void listarFuncionariosRoles() {
        // given
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.listarFuncionarioRoles()).thenReturn(theResponse);

        // when
        Response response = gatewayApi.listarFuncionariosRoles();

        // then
        ApiUtils.assertThat(FuncionarioGatewayApi.class, "listarFuncionariosRoles")
                .hasGetMapping("/funcionario-gateway-api/funcionarios/roles")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void listarFuncionariosRolesFail() {
        // given
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT, Response.Status.BAD_REQUEST);
        when(client.listarFuncionarioRoles()).thenReturn(theResponse);

        // when
        Response response = gatewayApi.listarFuncionariosRoles();

        // then
        ApiUtils.assertThat(FuncionarioGatewayApi.class, "listarFuncionariosRoles")
                .hasGetMapping("/funcionario-gateway-api/funcionarios/roles")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(new ArrayList<>());
    }

    @Test
    public void updateFuncionarioRole() {
        // given
        FuncionarioDTO dto = mock(FuncionarioDTO.class);
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.updateFuncionarioRoles(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.updateFuncionarioRole(dto);

        // then
        ApiUtils.assertThat(FuncionarioGatewayApi.class, "updateFuncionarioRole")
                .hasPutMapping("/funcionario-gateway-api/funcionarios")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void updateFuncionarioRoleFail() {
        // given
        FuncionarioDTO dto = mock(FuncionarioDTO.class);
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT, Response.Status.BAD_REQUEST);
        when(client.updateFuncionarioRoles(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.updateFuncionarioRole(dto);

        // then
        ApiUtils.assertThat(FuncionarioGatewayApi.class, "updateFuncionarioRole")
                .hasPutMapping("/funcionario-gateway-api/funcionarios")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response).isSameAs(theResponse);
    }

    @Test
    public void buscarFuncionarios() {
        // given
        FuncionarioDTO dto = mock(FuncionarioDTO.class);
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.buscarFuncionario(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.buscarFuncionarios(dto);

        // then
        ApiUtils.assertThat(FuncionarioGatewayApi.class, "buscarFuncionarios")
                .hasPostMapping("/funcionario-gateway-api/funcionarios/buscar")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }
}