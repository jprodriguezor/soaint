package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.utils.SystemParameters;
import co.com.foundation.test.mocks.JaxRsUtils;
import co.com.foundation.test.rules.EnvironmentRule;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyWebTarget;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ProductosClientTest {


    @Rule
    public EnvironmentRule environmentRule = new EnvironmentRule();

    private String API_ENDPOINT = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

    private ProductosClient productosClient;

    private JerseyWebTarget wt;

    private JerseyClient client;

    @Before
    public void setup() {

        productosClient = new ProductosClient();

        client = mock(JerseyClient.class);
        wt = mock(JerseyWebTarget.class);

        when(client.target(anyString())).thenReturn(wt);

        ReflectionTestUtils.setField(productosClient, "client", client);
    }

    @Test
    public void list() {
        // given
        String path = "/productos-web-api";

        JaxRsUtils.mockGetPath(wt, path);

        // when
        productosClient.list();

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);
    }
}