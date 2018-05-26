package co.com.foundation.sgd.apigateway.apis.mocks;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class JaxRsUtils {

    public static Invocation.Builder mockPostPath(WebTarget wt, String PATH) {
        WebTarget webTarget = mock(WebTarget.class);
        Invocation.Builder requestBuilder = mock(Invocation.Builder.class);
        when(requestBuilder.header(anyString(), any())).thenReturn(requestBuilder);
        when(requestBuilder.post(any(Entity.class))).thenReturn(mock(Response.class));
        when(webTarget.request()).thenReturn(requestBuilder);
        when(wt.path(PATH)).thenReturn(webTarget);

        return requestBuilder;
    }

    public static Invocation.Builder mockPutPath(WebTarget wt, String PATH) {
        WebTarget webTarget = mock(WebTarget.class);
        Invocation.Builder requestBuilder = mock(Invocation.Builder.class);
        when(requestBuilder.header(anyString(), any())).thenReturn(requestBuilder);
        when(requestBuilder.put(any(Entity.class))).thenReturn(mock(Response.class));
        when(webTarget.request()).thenReturn(requestBuilder);
        when(wt.path(PATH)).thenReturn(webTarget);

        return requestBuilder;
    }

    public static WebTarget mockGetPath(WebTarget wt, String path) {
        Response response = mock(Response.class);
        WebTarget webTarget = mock(WebTarget.class);
        Invocation.Builder requestBuilder = mock(Invocation.Builder.class);
        when(requestBuilder.get()).thenReturn(response);
        when(requestBuilder.header(anyString(), any())).thenReturn(requestBuilder);
        when(webTarget.queryParam(anyString(), any())).thenReturn(webTarget);
        when(webTarget.request()).thenReturn(requestBuilder);
        when(wt.path(path)).thenReturn(webTarget);

        return webTarget;
    }
}
