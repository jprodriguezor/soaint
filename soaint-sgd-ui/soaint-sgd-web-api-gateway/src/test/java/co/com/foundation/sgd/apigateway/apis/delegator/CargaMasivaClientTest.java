package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.test.rules.EnvironmentRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

public class CargaMasivaClientTest {

    @Rule
    public EnvironmentRule environmentRule = new EnvironmentRule();

    private CargaMasivaClient cargaMasivaClient;


    @Before
    public void setup() {
        cargaMasivaClient = new CargaMasivaClient();
    }

    @Test
    public void listCargaMasiva() {
        // given

        // when
        Response response = cargaMasivaClient.listCargaMasiva();

        // then
        String content = response.readEntity(String.class);
        assertThat(content, hasJsonPath("$.cargaMasiva[*]"));
    }

    @Test
    public void listEstadoCargaMasiva() {
        // given

        // when
        Response response = cargaMasivaClient.listEstadoCargaMasiva();

        // then
        assertThat(response.getStatus(), not(404));
    }

    @Test
    public void listEstadoCargaMasivaDadoId() {
        // given
        Integer ID_CARGA_MASIVA = Integer.valueOf(151);

        // when
        Response response = cargaMasivaClient.listEstadoCargaMasivaDadoId(ID_CARGA_MASIVA.toString());

        // then
        assertThat(response.getStatus(), not(404));

    }
}