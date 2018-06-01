package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.test.rules.EnvironmentRule;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

public class ProductosClientTest {


    @Rule
    public EnvironmentRule environmentRule = new EnvironmentRule();

    private ProductosClient productosClient;


    @Before
    public void setup() {
        productosClient = new ProductosClient();
    }

    @Test @Ignore
    public void list() {
        // given

        // when
        Response response = productosClient.list();

        // then
        assertThat(response.getStatus(), not(404));
    }
}