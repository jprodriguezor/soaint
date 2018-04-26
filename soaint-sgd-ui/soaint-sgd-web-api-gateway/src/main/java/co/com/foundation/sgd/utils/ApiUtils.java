package co.com.foundation.sgd.utils;

import co.com.foundation.sgd.apigateway.apis.delegator.ApiClient;
import lombok.extern.log4j.Log4j2;

import javax.ws.rs.core.Response;

@Log4j2
public class ApiUtils {

    public static Response getResponseClient(ApiClient apiClient) {
        Response response = apiClient.list();
        String responseContent = response.readEntity(String.class);
        log.info(" API - [content] : " + responseContent);
        return Response.status(response.getStatus()).entity(responseContent).build();
    }
}
