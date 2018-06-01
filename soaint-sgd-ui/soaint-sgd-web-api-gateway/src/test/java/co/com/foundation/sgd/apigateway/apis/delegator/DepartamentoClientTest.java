package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.test.rules.EnvironmentRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

public class DepartamentoClientTest {

    @Rule
    public EnvironmentRule environmentRule = new EnvironmentRule();

    private DepartamentoClient departamentoClient;

    @Before
    public void setup() {

        departamentoClient = new DepartamentoClient();
    }

    @Test
    public void listarPorPais() {
        // given
        String COD_PAIS = "P01";

        // when
        Response response = departamentoClient.listarPorPais(COD_PAIS);

        // then
        assertThat(response.getStatus(), not(404));
    }

    @Test
    public void listarDepartamentosActivos() {
        // given
        String path = "/departamentos-web-api/departamentos/A";

        // when
        Response response = departamentoClient.listarDepartamentosActivos();

        // then
        assertThat(response.getStatus(), not(404));
    }
}