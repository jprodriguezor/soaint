package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.mocks.JaxRsUtils;
import co.com.foundation.rules.EnvironmentRule;
import co.com.foundation.sgd.utils.SystemParameters;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class SedeAdministrativaClientTest {

    @Rule
    public EnvironmentRule environmentRule = new EnvironmentRule();

    private String API_ENDPOINT = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

    private SedeAdministrativaClient sedeAdministrativaClient;

    private WebTarget wt;

    private Client client;

    @Before
    public void setup() {

        sedeAdministrativaClient = new SedeAdministrativaClient();

        client = mock(Client.class);
        wt = mock(WebTarget.class);

        when(client.target(anyString())).thenReturn(wt);

        ReflectionTestUtils.setField(sedeAdministrativaClient, "client", client);
    }

    @Test
    public void list() {
        // given
        String path = "/organigrama-web-api/organigrama/sedes";
        JaxRsUtils.mockGetPath(wt, path);

        // when
        sedeAdministrativaClient.list();

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);
    }
}