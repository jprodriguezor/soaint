package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.test.rules.EnvironmentRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

public class PaisClientTest {

    @Rule
    public EnvironmentRule environmentRule = new EnvironmentRule();

    private PaisClient paisClient;

    @Before
    public void setup() {
        paisClient = new PaisClient();
    }

    @Test
    public void list() {
        // given

        // when
        Response response = paisClient.list();

        // then
        assertThat(response.getStatus(), not(404));
    }
}