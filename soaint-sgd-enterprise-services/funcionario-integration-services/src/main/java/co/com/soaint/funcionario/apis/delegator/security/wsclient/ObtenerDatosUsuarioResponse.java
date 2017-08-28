
package co.com.soaint.funcionario.apis.delegator.security.wsclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for obtenerDatosUsuarioResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="obtenerDatosUsuarioResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="obtenerDatosUsuario" type="{http://www.soaint.com/services/security-cartridge/1.0.0}OperationPrincipalStatusContext" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "obtenerDatosUsuarioResponse", propOrder = {
    "obtenerDatosUsuario"
})
public class ObtenerDatosUsuarioResponse {

    protected OperationPrincipalStatusContext obtenerDatosUsuario;

    /**
     * Gets the value of the obtenerDatosUsuario property.
     * 
     * @return
     *     possible object is
     *     {@link OperationPrincipalStatusContext }
     *     
     */
    public OperationPrincipalStatusContext getObtenerDatosUsuario() {
        return obtenerDatosUsuario;
    }

    /**
     * Sets the value of the obtenerDatosUsuario property.
     * 
     * @param value
     *     allowed object is
     *     {@link OperationPrincipalStatusContext }
     *     
     */
    public void setObtenerDatosUsuario(OperationPrincipalStatusContext value) {
        this.obtenerDatosUsuario = value;
    }

}
