package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.apigateway.apis.mocks.JaxRsUtils;
import co.com.foundation.sgd.apigateway.rules.EnvironmentRule;
import co.com.foundation.sgd.utils.SystemParameters;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class FuncionarioClientTest {

    @Rule
    public EnvironmentRule environmentRule = new EnvironmentRule();

    private String API_ENDPOINT = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

    private String FUNCIONARIO_ENDPOINT = SystemParameters.getParameter(SystemParameters.BACKAPI_FUNCIONARIO_SERVICE_ENDPOINT_URL);

    private FuncionarioClient funcionarioClient;

    private WebTarget wt;

    private Client client;

    @Before
    public void setup() {

        funcionarioClient = new FuncionarioClient();

        client = mock(Client.class);
        wt = mock(WebTarget.class);

        when(client.target(anyString())).thenReturn(wt);

        ReflectionTestUtils.setField(funcionarioClient, "client", client);
    }


    @Test
    public void obtenerFuncionario() {
        // given
        String LOGIN = "LOGIN";
        String path = "/funcionarios-web-api/funcionarios/" + LOGIN + "/A";

        JaxRsUtils.mockGetPath(wt, path);

        // when
        funcionarioClient.obtenerFuncionario(LOGIN);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);
    }

    @Test
    public void listarFuncionarios() {
        // given
        String COD_DEPENDENCIA = "CD01";
        String path = "/funcionarios-web-api/funcionarios/dependencia/" + COD_DEPENDENCIA + "/A";

        JaxRsUtils.mockGetPath(wt, path);

        // when
        funcionarioClient.listarFuncionarios(COD_DEPENDENCIA);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);
    }

    @Test
    public void listarFuncionariosByLoginnames() {
        // given
        String LOGIN_NAMES = "NAME1,NAME2";
        String path = "/funcionarios-web-api/funcionarios/listar-by-login-names/";

        WebTarget requestTarget = JaxRsUtils.mockGetPath(wt, path);

        // when
        funcionarioClient.listarFuncionariosByLoginnames(LOGIN_NAMES);

        // then
        verify(client).target(FUNCIONARIO_ENDPOINT);
        verify(wt).path(path);
        verify(requestTarget).queryParam("login_names", LOGIN_NAMES);
    }

    @Test
    public void listarFuncionariosPorRol() {
        // given
        String COD_DEPENDENCIA = "CD01";
        String ROL = "CD01";
        String path = "/funcionarios-web-api/funcionarios/" + COD_DEPENDENCIA + "/" + ROL + "/A";

        JaxRsUtils.mockGetPath(wt, path);

        // when
        funcionarioClient.listarFuncionariosPorRol(COD_DEPENDENCIA, ROL);

        // then
        verify(client).target(FUNCIONARIO_ENDPOINT);
        verify(wt).path(path);
    }

    @Test
    public void listarFuncionarioRoles() {
        // given
        String path = "/funcionarios-web-api/funcionarios/obtener_roles";

        JaxRsUtils.mockGetPath(wt, path);

        // when
        funcionarioClient.listarFuncionarioRoles();

        // then
        verify(client).target(FUNCIONARIO_ENDPOINT);
        verify(wt).path(path);
    }

    @Test
    public void updateFuncionarioRoles() {
        // given
        String path = "/funcionarios-web-api/funcionarios";
        FuncionarioDTO dto = mock(FuncionarioDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPutPath(wt, path);

        // when
        funcionarioClient.updateFuncionarioRoles(dto);

        // then
        verify(client).target(FUNCIONARIO_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<FuncionarioDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).buildPut(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }

    @Test
    public void buscarFuncionario() {
        // given
        String path = "/funcionarios-web-api/funcionarios/buscar-funcionarios";
        FuncionarioDTO dto = mock(FuncionarioDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, path);

        // when
        funcionarioClient.buscarFuncionario(dto);

        // then
        verify(client).target(FUNCIONARIO_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<FuncionarioDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).buildPost(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }
}