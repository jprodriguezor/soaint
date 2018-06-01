package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.test.rules.EnvironmentRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

public class TareaClientTest {


    @Rule
    public EnvironmentRule environmentRule = new EnvironmentRule();

    private TareaClient tareaClient;

    @Before
    public void setup() {
        tareaClient = new TareaClient();
    }

    @Test
    public void listarEstadoTarea() {
        // given
        String ID_INSTANCIA_PROCESO = "IIP01";
        String ID_TAREA_PROCESO = "ITP01";

        // when
        Response response = tareaClient.listarEstadoTarea(ID_INSTANCIA_PROCESO, ID_TAREA_PROCESO);

        // then
        assertThat(response.getStatus(), not(404));
    }
}