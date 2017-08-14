package co.com.foundation.sgd.apigateway.webservice.client;

import co.com.foundation.sgd.apigateway.webservice.proxy.securitycardbridge.AuthenticationResponseContext;
import co.com.foundation.sgd.apigateway.webservice.proxy.securitycardbridge.SecurityAPIService;
import co.com.foundation.sgd.apigateway.webservice.proxy.securitycardbridge.SystemException_Exception;
import co.com.foundation.sgd.infrastructure.ApiDelegator;
import org.springframework.beans.factory.annotation.Value;

import java.net.MalformedURLException;
import java.net.URL;

@ApiDelegator
public class SecurityCardbridgeClient {

    private SecurityAPIService service;

    public SecurityAPIService getService() {
        if (service == null) {
            URL url = null;
            try {
                url = new URL(securitycardbridgeWsdlEndpoint);
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
            service = new SecurityAPIService(url);
        }
        return service;
    }

    @Value("${proxy.securitycardbridge.endpoint}")
    private String securitycardbridgeWsdlEndpoint = "";

    public SecurityCardbridgeClient() {
        super();
    }

    public AuthenticationResponseContext verifyCredentials(String login, String password) throws SystemException_Exception {
        return getService().getSecurityAPIPort().verifyCredentials(login, password);
    }
}
