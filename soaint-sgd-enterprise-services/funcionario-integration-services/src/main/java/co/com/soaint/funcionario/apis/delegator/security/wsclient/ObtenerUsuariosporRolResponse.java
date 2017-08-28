
package co.com.soaint.funcionario.apis.delegator.security.wsclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for obtenerUsuariosporRolResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="obtenerUsuariosporRolResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="obtenerUsuariosporRol" type="{http://www.soaint.com/services/security-cartridge/1.0.0}OperationPrincipalContextListStatus" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "obtenerUsuariosporRolResponse", propOrder = {
    "obtenerUsuariosporRol"
})
public class ObtenerUsuariosporRolResponse {

    protected OperationPrincipalContextListStatus obtenerUsuariosporRol;

    /**
     * Gets the value of the obtenerUsuariosporRol property.
     * 
     * @return
     *     possible object is
     *     {@link OperationPrincipalContextListStatus }
     *     
     */
    public OperationPrincipalContextListStatus getObtenerUsuariosporRol() {
        return obtenerUsuariosporRol;
    }

    /**
     * Sets the value of the obtenerUsuariosporRol property.
     * 
     * @param value
     *     allowed object is
     *     {@link OperationPrincipalContextListStatus }
     *     
     */
    public void setObtenerUsuariosporRol(OperationPrincipalContextListStatus value) {
        this.obtenerUsuariosporRol = value;
    }

}
