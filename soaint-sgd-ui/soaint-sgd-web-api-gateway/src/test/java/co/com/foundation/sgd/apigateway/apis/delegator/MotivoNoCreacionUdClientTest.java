package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.test.rules.EnvironmentRule;
import co.com.foundation.test.rules.PropertiesLoaderRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.core.Response;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.is;

public class MotivoNoCreacionUdClientTest {
    @Rule
    public EnvironmentRule environmentRule = new EnvironmentRule();

    @Rule
    public PropertiesLoaderRule propertiesRule = PropertiesLoaderRule.from("sgd-service.properties");

    private String CODIGO_PADRE = propertiesRule.get("contants.motivonocreacionud.value");

    private MotivoNoCreacionUdClient motivoNoCreacionUdClient;

    @Before
    public void setup() {
        motivoNoCreacionUdClient = new MotivoNoCreacionUdClient();
        ReflectionTestUtils.setField(motivoNoCreacionUdClient, "tipoValue", CODIGO_PADRE);
    }

    @Test
    public void shouldGetResponseOfBisActivos() {

        // when
        Response response = motivoNoCreacionUdClient.list();

        // then
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
        String content = response.readEntity(String.class);
        assertThat(content, hasJsonPath("$.constantes[*]"));
        assertThat(content, hasJsonPath("$.constantes[*].codPadre", everyItem(is(CODIGO_PADRE))));
    }
}