
package co.com.soaint.funcionario.apis.delegator.security.wsclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for actualizarUsuario complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="actualizarUsuario">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="usuario" type="{http://www.soaint.com/services/security-cartridge/1.0.0}PrincipalContext" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "actualizarUsuario", propOrder = {
    "usuario"
})
public class ActualizarUsuario {

    protected PrincipalContext usuario;

    /**
     * Gets the value of the usuario property.
     * 
     * @return
     *     possible object is
     *     {@link PrincipalContext }
     *     
     */
    public PrincipalContext getUsuario() {
        return usuario;
    }

    /**
     * Sets the value of the usuario property.
     * 
     * @param value
     *     allowed object is
     *     {@link PrincipalContext }
     *     
     */
    public void setUsuario(PrincipalContext value) {
        this.usuario = value;
    }

}
