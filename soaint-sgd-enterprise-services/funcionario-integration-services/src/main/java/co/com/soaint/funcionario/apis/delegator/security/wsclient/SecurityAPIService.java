
package co.com.soaint.funcionario.apis.delegator.security.wsclient;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "SecurityAPIService", targetNamespace = "http://www.soaint.com/services/security-cartridge/1.0.0", wsdlLocation = "http://192.168.3.242:28080/Security-Cartridge/SecurityAPI?wsdl")
public class SecurityAPIService
    extends Service
{

    private final static URL SECURITYAPISERVICE_WSDL_LOCATION;
    private final static WebServiceException SECURITYAPISERVICE_EXCEPTION;
    private final static QName SECURITYAPISERVICE_QNAME = new QName("http://www.soaint.com/services/security-cartridge/1.0.0", "SecurityAPIService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://192.168.3.242:28080/Security-Cartridge/SecurityAPI?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        SECURITYAPISERVICE_WSDL_LOCATION = url;
        SECURITYAPISERVICE_EXCEPTION = e;
    }

    public SecurityAPIService() {
        super(__getWsdlLocation(), SECURITYAPISERVICE_QNAME);
    }

    public SecurityAPIService(WebServiceFeature... features) {
        super(__getWsdlLocation(), SECURITYAPISERVICE_QNAME, features);
    }

    public SecurityAPIService(URL wsdlLocation) {
        super(wsdlLocation, SECURITYAPISERVICE_QNAME);
    }

    public SecurityAPIService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, SECURITYAPISERVICE_QNAME, features);
    }

    public SecurityAPIService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SecurityAPIService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns SecurityAPI
     */
    @WebEndpoint(name = "SecurityAPIPort")
    public SecurityAPI getSecurityAPIPort() {
        return super.getPort(new QName("http://www.soaint.com/services/security-cartridge/1.0.0", "SecurityAPIPort"), SecurityAPI.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SecurityAPI
     */
    @WebEndpoint(name = "SecurityAPIPort")
    public SecurityAPI getSecurityAPIPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://www.soaint.com/services/security-cartridge/1.0.0", "SecurityAPIPort"), SecurityAPI.class, features);
    }

    private static URL __getWsdlLocation() {
        if (SECURITYAPISERVICE_EXCEPTION!= null) {
            throw SECURITYAPISERVICE_EXCEPTION;
        }
        return SECURITYAPISERVICE_WSDL_LOCATION;
    }

}
