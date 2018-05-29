package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.test.mocks.JaxRsUtils;
import co.com.foundation.test.rules.EnvironmentRule;
import co.com.foundation.sgd.utils.SystemParameters;
import co.com.soaint.foundation.canonical.correspondencia.CredencialesDTO;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class SecurityClientTest {

    @Rule
    public EnvironmentRule environmentRule = new EnvironmentRule();

    private String API_ENDPOINT = SystemParameters.getParameter(SystemParameters.BACKAPI_FUNCIONARIO_SERVICE_ENDPOINT_URL);

    private SecurityClient securityClient;

    private WebTarget wt;

    private Client client;

    @Before
    public void setup() {

        securityClient = new SecurityClient();

        client = mock(Client.class);
        wt = mock(WebTarget.class);

        when(client.target(anyString())).thenReturn(wt);

        ReflectionTestUtils.setField(securityClient, "client", client);
    }

    @Test
    public void verificarCredenciales() {
        // given
        String path = "/funcionarios-web-api/funcionarios/verificar-credenciales";
        CredencialesDTO dto = mock(CredencialesDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, path);

        // when
        securityClient.verificarCredenciales(dto);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<CredencialesDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);

    }
}