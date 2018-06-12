package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.test.rules.EnvironmentRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

public class ProduccionDocumentalClientTest {


    @Rule
    public EnvironmentRule environmentRule = new EnvironmentRule();

    private ProduccionDocumentalClient produccionDocumentalClient;


    @Before
    public void setup() {
        produccionDocumentalClient = new ProduccionDocumentalClient();
    }

    @Test
    public void obtenerDatosDocumentoPorNroRadicado() {
        // given
        String NRO_RADICADO = "NR01";

        // when
        Response response = produccionDocumentalClient.obtenerDatosDocumentoPorNroRadicado(NRO_RADICADO);

        // then
        assertThat(response.getStatus(), not(404));
    }
}