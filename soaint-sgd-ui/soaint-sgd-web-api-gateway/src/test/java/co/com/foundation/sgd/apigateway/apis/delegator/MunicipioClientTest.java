package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.apigateway.apis.mocks.JaxRsUtils;
import co.com.foundation.sgd.apigateway.rules.EnvironmentRule;
import co.com.foundation.sgd.utils.SystemParameters;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MunicipioClientTest {

    @Rule
    public EnvironmentRule environmentRule = new EnvironmentRule();

    private String API_ENDPOINT = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

    private MunicipioClient municipioClient;

    private WebTarget wt;

    private Client client;

    @Before
    public void setup() {

        municipioClient = new MunicipioClient();

        client = mock(Client.class);
        wt = mock(WebTarget.class);

        when(client.target(anyString())).thenReturn(wt);

        ReflectionTestUtils.setField(municipioClient, "client", client);
    }

    @Test
    public void listarPorDepartamento() {
        // given
        String DEPARTAMENTO = "DPTO";
        String path = "/municipios-web-api/municipios/" + DEPARTAMENTO + "/A";

        JaxRsUtils.mockGetPath(wt, path);

        // when
        municipioClient.listarPorDepartamento(DEPARTAMENTO);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);
    }

    @Test
    public void listarMunicipiosPorCodigo() {
        // given
        String CODIGOS = "CD01";
        String path = "/municipios-web-api/municipios";

        WebTarget requestTarget = JaxRsUtils.mockGetPath(wt, path);

        // when
        municipioClient.listarMunicipiosPorCodigo(CODIGOS);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);
        verify(requestTarget).queryParam("codigos", CODIGOS);
    }

    @Test
    public void listarMunicipiosActivos() {
        // given
        String path = "/municipios-web-api/municipios/A";

        JaxRsUtils.mockGetPath(wt, path);

        // when
        municipioClient.listarMunicipiosActivos();

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);
    }
}