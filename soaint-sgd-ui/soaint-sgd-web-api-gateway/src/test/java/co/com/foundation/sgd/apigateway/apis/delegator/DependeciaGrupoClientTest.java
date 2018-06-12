package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.test.rules.EnvironmentRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

public class DependeciaGrupoClientTest {

    @Rule
    public EnvironmentRule environmentRule = new EnvironmentRule();

    private DependeciaGrupoClient dependeciaGrupoClient;


    @Before
    public void setup() {
        dependeciaGrupoClient = new DependeciaGrupoClient();
    }

    @Test
    public void listBySedeAdministrativa() {
        // given
        String COD_SEDE = "CD01";

        // when
        Response response = dependeciaGrupoClient.listBySedeAdministrativa(COD_SEDE);

        // then
        assertThat(response.getStatus(), not(404));
    }

    @Test
    public void obtenerPorCodigo() {
        // given
        String CODIGO = "CD01";

        // when
        Response response = dependeciaGrupoClient.obtenerPorCodigo(CODIGO);

        // then
        assertThat(response.getStatus(), not(404));
    }

    @Test
    public void obtenerPorDependencias() {
        // given
        String CODIGOS_DEPENDENCIA = "CD01";

        // when
        Response response = dependeciaGrupoClient.obtenerPorDependencias(CODIGOS_DEPENDENCIA);

        // then
        assertThat(response.getStatus(), not(404));
    }

    @Test
    public void listarDependencias() {
        // given

        // when
        Response response = dependeciaGrupoClient.listarDependencias();

        // then
        assertThat(response.getStatus(), not(404));
    }
}