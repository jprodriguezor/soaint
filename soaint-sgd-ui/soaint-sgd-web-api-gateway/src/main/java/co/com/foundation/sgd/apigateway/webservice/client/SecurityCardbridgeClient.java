package co.com.foundation.sgd.apigateway.webservice.client;

import co.com.foundation.sgd.apigateway.webservice.proxy.securitycardbridge.AuthenticationResponseContext;
import co.com.foundation.sgd.apigateway.webservice.proxy.securitycardbridge.SecurityAPI;
import co.com.foundation.sgd.apigateway.webservice.proxy.securitycardbridge.SecurityAPIService;
import co.com.foundation.sgd.apigateway.webservice.proxy.securitycardbridge.SystemException_Exception;

public class SecurityCardbridgeClient {

    private static SecurityAPIService service;

    static {
        service = new SecurityAPIService();
    }

    public static AuthenticationResponseContext verifyCredentials(String login, String password) throws SystemException_Exception {
        return service.getSecurityAPIPort().verifyCredentials(login, password);
    }
}
