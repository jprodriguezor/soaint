package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.apigateway.rules.EnvironmentRule;
import co.com.foundation.sgd.apigateway.rules.PropertiesLoaderRule;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.core.Response;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.is;

public class UnidadTiempoClientTest {

    @Rule
    public EnvironmentRule environmentRule = new EnvironmentRule();

    @Rule
    public PropertiesLoaderRule propertiesRule = PropertiesLoaderRule.from("sgd-service.properties");

    private String CODIGO_PADRE = propertiesRule.get("contants.unidadtiempo.value");

    private UnidadTiempoClient unidadTiempoClient;

    @Before
    public void setup() {
        unidadTiempoClient = new UnidadTiempoClient();
        ReflectionTestUtils.setField(unidadTiempoClient, "unidadTiempoValue", CODIGO_PADRE);
    }

    @Test
    public void shouldGetResponseOfUnidadTiempoActivos() {

        // when
        Response response = unidadTiempoClient.list();

        // then
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
        String content = response.readEntity(String.class);
        assertThat(content, hasJsonPath("$.constantes[*]"));
        assertThat(content, hasJsonPath("$.constantes[*].codPadre", everyItem(is(CODIGO_PADRE))));
    }
}