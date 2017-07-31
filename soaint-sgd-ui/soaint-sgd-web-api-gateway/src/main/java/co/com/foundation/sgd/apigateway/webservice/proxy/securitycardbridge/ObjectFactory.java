
package co.com.foundation.sgd.apigateway.webservice.proxy.securitycardbridge;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the co.com.foundation.sgd.apigateway.webserviceclients.securitycardbridge package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _VerifyCredentialsResponse_QNAME = new QName("http://www.soaint.com/services/security-cartridge/1.0.0", "verifyCredentialsResponse");
    private final static QName _SystemException_QNAME = new QName("http://www.soaint.com/services/security-cartridge/1.0.0", "SystemException");
    private final static QName _VerifyCredentials_QNAME = new QName("http://www.soaint.com/services/security-cartridge/1.0.0", "verifyCredentials");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: co.com.foundation.sgd.apigateway.webserviceclients.securitycardbridge
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SystemException }
     * 
     */
    public SystemException createSystemException() {
        return new SystemException();
    }

    /**
     * Create an instance of {@link VerifyCredentials }
     * 
     */
    public VerifyCredentials createVerifyCredentials() {
        return new VerifyCredentials();
    }

    /**
     * Create an instance of {@link VerifyCredentialsResponse }
     * 
     */
    public VerifyCredentialsResponse createVerifyCredentialsResponse() {
        return new VerifyCredentialsResponse();
    }

    /**
     * Create an instance of {@link AuthenticationResponseContext }
     * 
     */
    public AuthenticationResponseContext createAuthenticationResponseContext() {
        return new AuthenticationResponseContext();
    }

    /**
     * Create an instance of {@link PrincipalContext }
     * 
     */
    public PrincipalContext createPrincipalContext() {
        return new PrincipalContext();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VerifyCredentialsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.soaint.com/services/security-cartridge/1.0.0", name = "verifyCredentialsResponse")
    public JAXBElement<VerifyCredentialsResponse> createVerifyCredentialsResponse(VerifyCredentialsResponse value) {
        return new JAXBElement<VerifyCredentialsResponse>(_VerifyCredentialsResponse_QNAME, VerifyCredentialsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SystemException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.soaint.com/services/security-cartridge/1.0.0", name = "SystemException")
    public JAXBElement<SystemException> createSystemException(SystemException value) {
        return new JAXBElement<SystemException>(_SystemException_QNAME, SystemException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VerifyCredentials }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.soaint.com/services/security-cartridge/1.0.0", name = "verifyCredentials")
    public JAXBElement<VerifyCredentials> createVerifyCredentials(VerifyCredentials value) {
        return new JAXBElement<VerifyCredentials>(_VerifyCredentials_QNAME, VerifyCredentials.class, null, value);
    }

}
