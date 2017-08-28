
package co.com.soaint.funcionario.apis.delegator.security.wsclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OperationPrincipalStatusContext complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OperationPrincipalStatusContext">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="successful" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="principalContext" type="{http://www.soaint.com/services/security-cartridge/1.0.0}PrincipalContext" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OperationPrincipalStatusContext", propOrder = {
    "successful",
    "message",
    "principalContext"
})
public class OperationPrincipalStatusContext {

    protected boolean successful;
    protected String message;
    protected PrincipalContext principalContext;

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

    /**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessage(String value) {
        this.message = value;
    }

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

}
