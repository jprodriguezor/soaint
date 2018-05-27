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

    private static Response response;

    public static Response getResponseMock() {
        return response;
    }

    public static Invocation.Builder mockPostPath(WebTarget wt, String path) {
        return mockPostPath(wt, null, path);
    }

    public static Invocation.Builder mockPutPath(WebTarget wt, String path) {
        return mockPutPath(wt, null, path);
    }

    public static WebTarget mockGetPath(WebTarget wt, String path) {
        return mockGetPath(wt, null, path);
    }

    public static WebTarget mockDeletePath(WebTarget wt, String path) {
        return mockDeletePath(wt, null, path);
    }

    public static <T> Invocation.Builder mockPostPath(WebTarget wt, Class<T> responseClass, String path) {

        Response response = mockResponse(responseClass);
        Invocation.Builder requestBuilder = mockRequestBuilder();
        when(requestBuilder.post(any(Entity.class))).thenReturn(response);

        Invocation invocation = mock(Invocation.class);
        when(requestBuilder.buildPost(any(Entity.class))).thenReturn(invocation);
        when(invocation.invoke()).thenReturn(response);

        WebTarget webTarget = mockWebTarget(requestBuilder);
        when(wt.path(path)).thenReturn(webTarget);

        return requestBuilder;
    }

    public static <T> Invocation.Builder mockPutPath(WebTarget wt, Class<T> responseClass, String path) {

        Response response = mockResponse(responseClass);
        Invocation.Builder requestBuilder = mockRequestBuilder();
        when(requestBuilder.put(any(Entity.class))).thenReturn(response);

        Invocation invocation = mock(Invocation.class);
        when(requestBuilder.buildPut(any(Entity.class))).thenReturn(invocation);
        when(invocation.invoke()).thenReturn(response);

        WebTarget webTarget = mockWebTarget(requestBuilder);
        when(wt.path(path)).thenReturn(webTarget);

        return requestBuilder;
    }

    public static <T> WebTarget mockGetPath(WebTarget wt, Class<T> responseClass, String path) {

        Response response = mockResponse(responseClass);
        Invocation.Builder requestBuilder = mockRequestBuilder();
        when(requestBuilder.get()).thenReturn(response);

        WebTarget webTarget = mockWebTarget(requestBuilder);
        when(wt.path(path)).thenReturn(webTarget);

        return webTarget;
    }

    public static <T> WebTarget mockDeletePath(WebTarget wt, Class<T> responseClass, String path) {

        Response response = mockResponse(responseClass);
        Invocation.Builder requestBuilder = mockRequestBuilder();
        when(requestBuilder.delete()).thenReturn(response);

        WebTarget webTarget = mockWebTarget(requestBuilder);
        when(wt.path(path)).thenReturn(webTarget);

        return webTarget;
    }

    private static <T> Response mockResponse(Class<T> responseClass) {
        response = mock(Response.class);

        if(responseClass != null) {

            T responseEntity = mockResponseEntity(responseClass);
            when(response.readEntity(responseClass)).thenReturn(responseEntity);
        }

        return response;
    }

    private static <T> Invocation.Builder mockRequestBuilder() {

        Invocation.Builder requestBuilder = mock(Invocation.Builder.class);
        when(requestBuilder.header(anyString(), any())).thenReturn(requestBuilder);

        return requestBuilder;
    }

    private static WebTarget mockWebTarget(Invocation.Builder requestBuilder) {
        WebTarget webTarget = mock(WebTarget.class);
        when(webTarget.request()).thenReturn(requestBuilder);
        when(webTarget.queryParam(anyString(), any())).thenReturn(webTarget);
        return webTarget;
    }

    private static <T> T mockResponseEntity(Class<T> responseClass) {
        T returnedValue;
        if(responseClass.isAssignableFrom(Boolean.class)){
            returnedValue = (T) Boolean.FALSE;
        } else if(responseClass.isAssignableFrom(Integer.class)){
            returnedValue = (T) Integer.valueOf(0);
        } else
            returnedValue = mock(responseClass);
        return returnedValue;
    }
}
