package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.apigateway.rules.EnvironmentRule;
import lombok.extern.log4j.Log4j2;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.ws.rs.core.Response;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.is;

@Log4j2
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"/spring/core-config.xml"}

)
public class TipoViaClientTest {

    @Rule
    public EnvironmentRule environmentRule = new EnvironmentRule();

    @Autowired
    TipoViaClient tipoViaClient;

    @Value("${contants.tipovia.value}")
    String CODIGO_PADRE;


    @Test
    public void shouldGetResponseOfTipoViaActivos() {

        // when
        Response response = tipoViaClient.list();

        // then
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
        String content = response.readEntity(String.class);
        assertThat(content, hasJsonPath("$.constantes[*]"));
        assertThat(content, hasJsonPath("$.constantes[*].codPadre", everyItem(is(CODIGO_PADRE))));
    }
}