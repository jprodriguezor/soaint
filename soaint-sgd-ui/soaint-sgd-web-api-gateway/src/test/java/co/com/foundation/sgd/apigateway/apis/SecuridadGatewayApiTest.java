package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.SecurityClient;
import co.com.foundation.sgd.dto.AccountDTO;
import co.com.foundation.test.mocks.ApiUtils;
import co.com.foundation.test.mocks.JaxRsUtils;
import co.com.soaint.foundation.canonical.correspondencia.CredencialesDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.seguridad.UsuarioDTO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SecuridadGatewayApiTest {

    private SecuridadGatewayApi gatewayApi;

    private UriInfo uriInfo;

    private SecurityClient securityClient;

    @Before
    public void setup() {
        gatewayApi = new SecuridadGatewayApi();

        uriInfo = mock(UriInfo.class);

        securityClient = mock(SecurityClient.class);

        ReflectionTestUtils.setField(gatewayApi, "uriInfo", uriInfo);
        ReflectionTestUtils.setField(gatewayApi, "securityClient", securityClient);
    }

    @Test
    public void login() {

        // given
        String LOGIN = "LOGIN";
        String PASSWORD = "PASSWORD";
        UsuarioDTO dto = UsuarioDTO.newBuilder().login(LOGIN).password(PASSWORD).build();

        FuncionarioDTO funcionarioDTO = mock(FuncionarioDTO.class);
        Response theResponse = JaxRsUtils.mockResponse(FuncionarioDTO.class, funcionarioDTO);
        when(securityClient.verificarCredenciales(any(CredencialesDTO.class))).thenReturn(theResponse);
        when(uriInfo.getAbsolutePath()).thenReturn(URI.create("http://server.com"));

        // when
        Response response = gatewayApi.login(dto);

        // then
        ApiUtils.assertThat(SecuridadGatewayApi.class, "login")
                .hasPostMapping("/securidad-gateway-api/login")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON);

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getStringHeaders()).containsKeys(AUTHORIZATION);
        assertThat(response.getStringHeaders().getFirst(AUTHORIZATION)).startsWith("Bearer ");

        AccountDTO accountDTO = (AccountDTO) response.getEntity();

        assertThat(accountDTO.getToken()).isNotEmpty();
        assertThat(accountDTO.getProfile()).isSameAs(funcionarioDTO);

        ArgumentCaptor<CredencialesDTO> captor = ArgumentCaptor.forClass(CredencialesDTO.class);
        verify(securityClient).verificarCredenciales(captor.capture());

        CredencialesDTO credencialesDTO = captor.getValue();
        assertThat(credencialesDTO.getLoginName()).isEqualTo(LOGIN);
        assertThat(credencialesDTO.getPassword()).isEqualTo(PASSWORD);
    }

    @Test
    public void loginUnauthorized() {

        // given
        String LOGIN = "LOGIN";
        String PASSWORD = "PASSWORD";
        UsuarioDTO dto = UsuarioDTO.newBuilder().login(LOGIN).password(PASSWORD).build();

        FuncionarioDTO funcionarioDTO = mock(FuncionarioDTO.class);
        Response theResponse = JaxRsUtils.mockResponse(FuncionarioDTO.class, funcionarioDTO, Response.Status.UNAUTHORIZED);
        when(securityClient.verificarCredenciales(any(CredencialesDTO.class))).thenReturn(theResponse);
        when(uriInfo.getAbsolutePath()).thenReturn(URI.create("http://server.com"));

        // when
        Response response = gatewayApi.login(dto);

        // then
        ApiUtils.assertThat(SecuridadGatewayApi.class, "login")
                .hasPostMapping("/securidad-gateway-api/login")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON);

        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());

        ArgumentCaptor<CredencialesDTO> captor = ArgumentCaptor.forClass(CredencialesDTO.class);
        verify(securityClient).verificarCredenciales(captor.capture());

        CredencialesDTO credencialesDTO = captor.getValue();
        assertThat(credencialesDTO.getLoginName()).isEqualTo(LOGIN);
        assertThat(credencialesDTO.getPassword()).isEqualTo(PASSWORD);
    }

    @Test
    public void loginFail() {

        // given
        String LOGIN = "LOGIN";
        String PASSWORD = "PASSWORD";
        UsuarioDTO dto = UsuarioDTO.newBuilder().login(LOGIN).password(PASSWORD).build();

        when(securityClient.verificarCredenciales(any(CredencialesDTO.class))).thenThrow(RuntimeException.class);
        when(uriInfo.getAbsolutePath()).thenReturn(URI.create("http://server.com"));

        // when
        Response response = gatewayApi.login(dto);

        // then
        ApiUtils.assertThat(SecuridadGatewayApi.class, "login")
                .hasPostMapping("/securidad-gateway-api/login")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON);

        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());

        ArgumentCaptor<CredencialesDTO> captor = ArgumentCaptor.forClass(CredencialesDTO.class);
        verify(securityClient).verificarCredenciales(captor.capture());

        CredencialesDTO credencialesDTO = captor.getValue();
        assertThat(credencialesDTO.getLoginName()).isEqualTo(LOGIN);
        assertThat(credencialesDTO.getPassword()).isEqualTo(PASSWORD);
    }
}