package co.com.foundation.sgd.infrastructure;

import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.junit.Test;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthenticatorTest {

    @Test
    public void filter() throws IOException {
        String USER = "USER";
        String PASSWORD = "PASSWORD";

        Authenticator authenticator = new Authenticator(USER, PASSWORD);

        ClientRequestContext context = mock(ClientRequestContext.class);

        MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<>();
        when(context.getHeaders()).thenReturn(headers);

        // when
        authenticator.filter(context);

        // then
        assertThat(headers.getFirst("Authorization").toString())
                .startsWith("BASIC");
    }
}