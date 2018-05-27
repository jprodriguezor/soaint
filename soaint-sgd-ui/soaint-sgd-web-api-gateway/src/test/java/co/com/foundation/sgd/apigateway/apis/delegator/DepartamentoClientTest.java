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

public class DepartamentoClientTest {

    @Rule
    public EnvironmentRule environmentRule = new EnvironmentRule();

    private String ENDPOINT = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

    private DepartamentoClient departamentoClient;

    private WebTarget wt;

    private Client client;

    @Before
    public void setup() {

        departamentoClient = new DepartamentoClient();

        client = mock(Client.class);
        wt = mock(WebTarget.class);

        when(client.target(anyString())).thenReturn(wt);

        ReflectionTestUtils.setField(departamentoClient, "client", client);
    }

    @Test
    public void listarPorPais() {
        // given
        String COD_PAIS = "P01";
        String path = "/departamentos-web-api/departamentos/" + COD_PAIS + "/A";
        JaxRsUtils.mockGetPath(wt, path);

        // when
        departamentoClient.listarPorPais(COD_PAIS);

        // then
        verify(client).target(ENDPOINT);
        verify(wt).path(path);
    }

    @Test
    public void listarDepartamentosActivos() {
        // given
        String path = "/departamentos-web-api/departamentos/A";
        JaxRsUtils.mockGetPath(wt, path);

        // when
        departamentoClient.listarDepartamentosActivos();

        // then
        verify(client).target(ENDPOINT);
        verify(wt).path(path);
    }
}