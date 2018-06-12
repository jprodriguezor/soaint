package co.com.foundation.test.mocks;

import org.glassfish.jersey.client.JerseyInvocation;
import org.glassfish.jersey.client.JerseyWebTarget;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
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

    public static Invocation.Builder mockPostPath(JerseyWebTarget wt, String path) {
        return mockPostPath(wt, null, path);
    }

    public static Invocation.Builder mockPutPath(JerseyWebTarget wt, String path) {
        return mockPutPath(wt, null, path);
    }

    public static JerseyWebTarget mockGetPath(JerseyWebTarget wt, String path) {
        return mockGetPath(wt, null, path);
    }

    public static JerseyWebTarget mockDeletePath(JerseyWebTarget wt, String path) {
        return mockDeletePath(wt, null, path);
    }

    public static <T> Invocation.Builder mockPostPath(JerseyWebTarget wt, Class<T> responseClass, String path) {

        Response response = mockResponse(responseClass);
        JerseyInvocation.Builder requestBuilder = mockRequestBuilder();
        when(requestBuilder.post(any(Entity.class))).thenReturn(response);

        JerseyInvocation invocation = mock(JerseyInvocation.class);
        when(requestBuilder.buildPost(any(Entity.class))).thenReturn(invocation);
        when(invocation.invoke()).thenReturn(response);

        JerseyWebTarget webTarget = mockJerseyWebTarget(requestBuilder);
        when(wt.path(path)).thenReturn(webTarget);

        return requestBuilder;
    }

    public static <T> Invocation.Builder mockPutPath(JerseyWebTarget wt, Class<T> responseClass, String path) {

        Response response = mockResponse(responseClass);
        JerseyInvocation.Builder requestBuilder = mockRequestBuilder();
        when(requestBuilder.put(any(Entity.class))).thenReturn(response);

        JerseyInvocation invocation = mock(JerseyInvocation.class);
        when(requestBuilder.buildPut(any(Entity.class))).thenReturn(invocation);
        when(invocation.invoke()).thenReturn(response);

        JerseyWebTarget webTarget = mockJerseyWebTarget(requestBuilder);
        when(wt.path(path)).thenReturn(webTarget);

        return requestBuilder;
    }

    public static <T> JerseyWebTarget mockGetPath(JerseyWebTarget wt, Class<T> responseClass, String path) {

        Response response = mockResponse(responseClass);
        JerseyInvocation.Builder requestBuilder = mockRequestBuilder();
        when(requestBuilder.get()).thenReturn(response);

        JerseyWebTarget webTarget = mockJerseyWebTarget(requestBuilder);
        when(wt.path(path)).thenReturn(webTarget);

        return webTarget;
    }

    public static <T> JerseyWebTarget mockDeletePath(JerseyWebTarget wt, Class<T> responseClass, String path) {

        Response response = mockResponse(responseClass);
        JerseyInvocation.Builder requestBuilder = mockRequestBuilder();
        when(requestBuilder.delete()).thenReturn(response);

        JerseyWebTarget webTarget = mockJerseyWebTarget(requestBuilder);
        when(wt.path(path)).thenReturn(webTarget);

        return webTarget;
    }

    public static <T> Response mockResponse(Class<T> responseClass) {

        if(responseClass != null) {
            T responseEntity = mockResponseEntity(responseClass);
            response = mockResponse(responseClass, responseEntity);
        } else
            response = mock(Response.class);

        return response;
    }

    public static <T> Response mockResponse(Class<T> responseClass, T returnValue) {

        return mockResponse(responseClass, returnValue, Response.Status.OK);
    }

    public static <T> Response mockResponse(Class<T> responseClass, T returnValue, Response.Status status) {

        response = mock(Response.class);

        when(response.readEntity(responseClass)).thenReturn(returnValue);
        when(response.getStatus()).thenReturn(status.getStatusCode());

        return response;
    }

    public static <T> Response mockGenericResponse(Class<T> responseClass, T returnValue) {

        return mockGenericResponse(responseClass, returnValue, Response.Status.OK);
    }

    public static <T> Response mockGenericResponse(Class<T> responseClass, T returnValue, Response.Status status) {

        response = mock(Response.class);

        when(response.readEntity(any(GenericType.class))).thenReturn(returnValue);
        when(response.getStatus()).thenReturn(status.getStatusCode());

        return response;
    }

    private static <T> JerseyInvocation.Builder mockRequestBuilder() {

        JerseyInvocation.Builder requestBuilder = mock(JerseyInvocation.Builder.class);
        when(requestBuilder.header(anyString(), any())).thenReturn(requestBuilder);

        return requestBuilder;
    }

    private static JerseyWebTarget mockJerseyWebTarget(JerseyInvocation.Builder requestBuilder) {
        JerseyWebTarget webTarget = mock(JerseyWebTarget.class);
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
