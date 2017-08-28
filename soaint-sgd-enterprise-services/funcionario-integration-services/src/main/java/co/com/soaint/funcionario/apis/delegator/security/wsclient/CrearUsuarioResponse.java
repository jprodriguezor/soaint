
package co.com.soaint.funcionario.apis.delegator.security.wsclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for crearUsuarioResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="crearUsuarioResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="crearUsuario" type="{http://www.soaint.com/services/security-cartridge/1.0.0}OperationPrincipalStatusContext" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "crearUsuarioResponse", propOrder = {
    "crearUsuario"
})
public class CrearUsuarioResponse {

    protected OperationPrincipalStatusContext crearUsuario;

    /**
     * Gets the value of the crearUsuario property.
     * 
     * @return
     *     possible object is
     *     {@link OperationPrincipalStatusContext }
     *     
     */
    public OperationPrincipalStatusContext getCrearUsuario() {
        return crearUsuario;
    }

    /**
     * Sets the value of the crearUsuario property.
     * 
     * @param value
     *     allowed object is
     *     {@link OperationPrincipalStatusContext }
     *     
     */
    public void setCrearUsuario(OperationPrincipalStatusContext value) {
        this.crearUsuario = value;
    }

}
