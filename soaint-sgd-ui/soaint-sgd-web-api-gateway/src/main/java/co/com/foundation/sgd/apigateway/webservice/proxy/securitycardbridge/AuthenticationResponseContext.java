
package co.com.foundation.sgd.apigateway.webservice.proxy.securitycardbridge;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for authenticationResponseContext complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="authenticationResponseContext">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="principalContext" type="{http://www.soaint.com/services/security-cartridge/1.0.0}principalContext" minOccurs="0"/>
 *         &lt;element name="successful" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "authenticationResponseContext", propOrder = {
    "principalContext",
    "successful"
})
public class AuthenticationResponseContext {

    protected PrincipalContext principalContext;
    protected boolean successful;

    /**
     * Gets the value of the principalContext property.
     * 
     * @return
     *     possible object is
     *     {@link PrincipalContext }
     *     
     */
    public PrincipalContext getPrincipalContext() {
        return principalContext;
    }

    /**
     * Sets the value of the principalContext property.
     * 
     * @param value
     *     allowed object is
     *     {@link PrincipalContext }
     *     
     */
    public void setPrincipalContext(PrincipalContext value) {
        this.principalContext = value;
    }

    /**
     * Gets the value of the successful property.
     * 
     */
    public boolean isSuccessful() {
        return successful;
    }

    /**
     * Sets the value of the successful property.
     * 
     */
    public void setSuccessful(boolean value) {
        this.successful = value;
    }

}
