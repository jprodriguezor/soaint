package co.com.foundation.sgd.apigateway.webservice.client;

import co.com.foundation.sgd.apigateway.webservice.proxy.securitycardbridge.AuthenticationResponseContext;
import co.com.foundation.sgd.apigateway.webservice.proxy.securitycardbridge.SecurityAPIService;
import co.com.foundation.sgd.apigateway.webservice.proxy.securitycardbridge.SystemException;
import co.com.foundation.sgd.apigateway.webservice.proxy.securitycardbridge.SystemExceptionException;
import co.com.foundation.sgd.infrastructure.ApiDelegator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;

import java.net.MalformedURLException;
import java.net.URL;

@ApiDelegator
@Log4j2
public class SecurityCardbridgeClient {

    private SecurityAPIService service;

    @Value("${proxy.securitycardbridge.endpoint}")
    private String securitycardbridgeWsdlEndpoint = "";

    /**
     * Contructor
     */
    public SecurityCardbridgeClient() {
        super();
    }

    private SecurityAPIService getService() throws SystemExceptionException {
        if (service == null) {
            URL url = null;
            try {
                log.debug(securitycardbridgeWsdlEndpoint);
                url = new URL(securitycardbridgeWsdlEndpoint);
                service = new SecurityAPIService(url);
            } catch (MalformedURLException ex) {
                log.error(ex);
                SystemException exception = new SystemException();
                exception.setMessage(ex.getMessage());
                throw new SystemExceptionException(ex.getMessage(), exception, ex);
            }
        }
        return service;
    }

    public AuthenticationResponseContext verifyCredentials(String login, String password) throws SystemExceptionException {
        return getService().getSecurityAPIPort().verifyCredentials(login, password);
    }
}
