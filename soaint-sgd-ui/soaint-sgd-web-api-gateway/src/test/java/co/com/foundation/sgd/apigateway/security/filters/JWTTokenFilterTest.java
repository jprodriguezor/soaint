package co.com.foundation.sgd.apigateway.security.filters;

import co.com.foundation.sgd.infrastructure.KeyManager;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class JWTTokenFilterTest {

    @Test
    public void filter() throws IOException {
        // given
        String LOGIN = "LOGIN";
        String PASSWORD = "PASSWORD";

        String AUTHORIZATION_HEADER = "Bearer" + KeyManager.getInstance().issueToken(LOGIN, PASSWORD);

        ContainerRequestContext requestContext = mock(ContainerRequestContext.class);
        when(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION)).thenReturn(AUTHORIZATION_HEADER);

        JWTTokenFilter jwtTokenFilter = new JWTTokenFilter();

        // when
        jwtTokenFilter.filter(requestContext);

        // then
        verify(requestContext, times(0)).abortWith(any(Response.class));
    }


    @Test
    public void filterNoAuthorizationHeader() throws IOException {
        // given

        String AUTHORIZATION_HEADER = null;

        ContainerRequestContext requestContext = mock(ContainerRequestContext.class);
        when(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION)).thenReturn(AUTHORIZATION_HEADER);

        JWTTokenFilter jwtTokenFilter = new JWTTokenFilter();

        // when
        jwtTokenFilter.filter(requestContext);

        // then
        ArgumentCaptor<Response> captor = ArgumentCaptor.forClass(Response.class);

        verify(requestContext).abortWith(captor.capture());

        assertThat(captor.getValue().getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
    }

    @Test
    public void filterInvalidAuthorizationHeader() throws IOException {
        // given

        String AUTHORIZATION_HEADER = "INVALID TOKEN";

        ContainerRequestContext requestContext = mock(ContainerRequestContext.class);
        when(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION)).thenReturn(AUTHORIZATION_HEADER);

        JWTTokenFilter jwtTokenFilter = new JWTTokenFilter();

        // when
        jwtTokenFilter.filter(requestContext);

        // then
        ArgumentCaptor<Response> captor = ArgumentCaptor.forClass(Response.class);

        verify(requestContext).abortWith(captor.capture());

        assertThat(captor.getValue().getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
    }
}