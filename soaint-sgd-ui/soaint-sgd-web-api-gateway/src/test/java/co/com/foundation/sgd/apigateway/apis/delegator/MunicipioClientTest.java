package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.test.rules.EnvironmentRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

public class MunicipioClientTest {

    @Rule
    public EnvironmentRule environmentRule = new EnvironmentRule();

    private MunicipioClient municipioClient;

    @Before
    public void setup() {
        municipioClient = new MunicipioClient();
    }

    @Test
    public void listarPorDepartamento() {
        // given
        String DEPARTAMENTO = "DPTO";

        // when
        Response response = municipioClient.listarPorDepartamento(DEPARTAMENTO);

        // then
        assertThat(response.getStatus(), not(404));
    }

    @Test
    public void listarMunicipiosPorCodigo() {
        // given
        String CODIGOS = "CD01";

        // when
        Response response = municipioClient.listarMunicipiosPorCodigo(CODIGOS);

        // then
        assertThat(response.getStatus(), not(404));
    }

    @Test
    public void listarMunicipiosActivos() {
        // given

        // when
        Response response = municipioClient.listarMunicipiosActivos();

        // then
        assertThat(response.getStatus(), not(404));
    }
}