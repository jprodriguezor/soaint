package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.test.rules.EnvironmentRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

public class FuncionarioClientTest {

    @Rule
    public EnvironmentRule environmentRule = new EnvironmentRule();

    private FuncionarioClient funcionarioClient;

    @Before
    public void setup() {
        funcionarioClient = new FuncionarioClient();
    }


    @Test
    public void obtenerFuncionario() {
        // given
        String LOGIN = "LOGIN";

        // when
        Response response = funcionarioClient.obtenerFuncionario(LOGIN);

        // then
        assertThat(response.getStatus(), not(404));
    }

    @Test
    public void listarFuncionarios() {
        // given
        String COD_DEPENDENCIA = "CD01";

        // when
        Response response = funcionarioClient.listarFuncionarios(COD_DEPENDENCIA);

        // then
        assertThat(response.getStatus(), not(404));
    }

    @Test
    public void listarFuncionariosByLoginnames() {
        // given
        String LOGIN_NAMES = "NAME1,NAME2";

        // when
        Response response = funcionarioClient.listarFuncionariosByLoginnames(LOGIN_NAMES);

        // then
        assertThat(response.getStatus(), not(404));
    }

    @Test
    public void listarFuncionariosPorRol() {
        // given
        String COD_DEPENDENCIA = "CD01";
        String ROL = "CD01";

        // when
        Response response = funcionarioClient.listarFuncionariosPorRol(COD_DEPENDENCIA, ROL);

        // then
        assertThat(response.getStatus(), not(404));
    }

    @Test
    public void listarFuncionarioRoles() {
        // given
        // when
        Response response = funcionarioClient.listarFuncionarioRoles();

        // then
        assertThat(response.getStatus(), not(404));
    }

}