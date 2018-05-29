package co.com.foundation.sgd.apigateway.security.filters;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

import static org.mockito.Mockito.*;

public class CORSFilterTest {


    @Test
    public void classMetadata() {

        Assertions.assertThat(CORSFilter.class.getInterfaces()).contains(ContainerResponseFilter.class);

        Assertions.assertThat(CORSFilter.class.isAnnotationPresent(Provider.class)).isTrue();
    }

    @Test
    public void filter() throws IOException {

        // given
        String REQUEST_HEADER = "header";

        CORSFilter corsFilter = new CORSFilter();

        MultivaluedMap<String, Object> headers = mock(MultivaluedMap.class);

        ContainerRequestContext requestContext = mock(ContainerRequestContext.class);
        ContainerResponseContext responseContext = mock(ContainerResponseContext.class);

        when(requestContext.getHeaderString("Access-Control-Request-Headers")).thenReturn(REQUEST_HEADER);
        when(responseContext.getHeaders()).thenReturn(headers);

        // when
        corsFilter.filter(requestContext, responseContext);

        // then
        verify(headers).putSingle("Access-Control-Allow-Origin", "*");
        verify(headers).putSingle("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        verify(headers).putSingle("Access-Control-Allow-Headers", REQUEST_HEADER);
    }
}