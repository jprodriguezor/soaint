
package co.com.soaint.funcionario.apis.delegator.security.wsclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for verifyCredentialsResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="verifyCredentialsResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="verifyCredentials" type="{http://www.soaint.com/services/security-cartridge/1.0.0}AuthenticationResponseContext" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "verifyCredentialsResponse", propOrder = {
    "verifyCredentials"
})
public class VerifyCredentialsResponse {

    protected AuthenticationResponseContext verifyCredentials;

    /**
     * Gets the value of the verifyCredentials property.
     * 
     * @return
     *     possible object is
     *     {@link AuthenticationResponseContext }
     *     
     */
    public AuthenticationResponseContext getVerifyCredentials() {
        return verifyCredentials;
    }

    /**
     * Sets the value of the verifyCredentials property.
     * 
     * @param value
     *     allowed object is
     *     {@link AuthenticationResponseContext }
     *     
     */
    public void setVerifyCredentials(AuthenticationResponseContext value) {
        this.verifyCredentials = value;
    }

}
