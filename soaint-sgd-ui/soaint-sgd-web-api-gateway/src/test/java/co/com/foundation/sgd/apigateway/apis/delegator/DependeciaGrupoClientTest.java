package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.test.mocks.JaxRsUtils;
import co.com.foundation.test.rules.EnvironmentRule;
import co.com.foundation.sgd.utils.SystemParameters;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyWebTarget;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DependeciaGrupoClientTest {

    @Rule
    public EnvironmentRule environmentRule = new EnvironmentRule();

    private String ENDPOINT = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

    private DependeciaGrupoClient dependeciaGrupoClient;

    private JerseyWebTarget wt;

    private JerseyClient client;

    @Before
    public void setup() {

        dependeciaGrupoClient = new DependeciaGrupoClient();

        client = mock(JerseyClient.class);
        wt = mock(JerseyWebTarget.class);

        when(client.target(anyString())).thenReturn(wt);

        ReflectionTestUtils.setField(dependeciaGrupoClient, "client", client);
    }

    @Test
    public void listBySedeAdministrativa() {
        // given
        String COD_SEDE = "CD01";
        String path = "/organigrama-web-api/organigrama/dependencias/" + COD_SEDE;
        JaxRsUtils.mockGetPath(wt, path);

        // when
        dependeciaGrupoClient.listBySedeAdministrativa(COD_SEDE);

        // then
        verify(client).target(ENDPOINT);
        verify(wt).path(path);
    }

    @Test
    public void obtenerPorCodigo() {
        // given
        String CODIGO = "CD01";
        String path = "/dependencia-web-api/dependencia/" + CODIGO;
        JaxRsUtils.mockGetPath(wt, path);

        // when
        dependeciaGrupoClient.obtenerPorCodigo(CODIGO);

        // then
        verify(client).target(ENDPOINT);
        verify(wt).path(path);
    }

    @Test
    public void obtenerPorDependencias() {
        // given
        String CODIGOS_DEPENDENCIA = "CD01";
        String path = "/dependencia-web-api/dependencia";
        WebTarget requestTarget = JaxRsUtils.mockGetPath(wt, path);

        // when
        dependeciaGrupoClient.obtenerPorDependencias(CODIGOS_DEPENDENCIA);

        // then
        verify(client).target(ENDPOINT);
        verify(wt).path(path);
        verify(requestTarget).queryParam("codigos", CODIGOS_DEPENDENCIA);
    }

    @Test
    public void listarDependencias() {
        // given
        String path = "/dependencia-web-api/dependencias";
        JaxRsUtils.mockGetPath(wt, path);

        // when
        dependeciaGrupoClient.listarDependencias();

        // then
        verify(client).target(ENDPOINT);
        verify(wt).path(path);
    }
}