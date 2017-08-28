
package co.com.soaint.funcionario.apis.delegator.security.wsclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getRolesbyUserResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getRolesbyUserResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="getRolesbyUser" type="{http://www.soaint.com/services/security-cartridge/1.0.0}listadoRoles" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getRolesbyUserResponse", propOrder = {
    "getRolesbyUser"
})
public class GetRolesbyUserResponse {

    protected ListadoRoles getRolesbyUser;

    /**
     * Gets the value of the getRolesbyUser property.
     * 
     * @return
     *     possible object is
     *     {@link ListadoRoles }
     *     
     */
    public ListadoRoles getGetRolesbyUser() {
        return getRolesbyUser;
    }

    /**
     * Sets the value of the getRolesbyUser property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListadoRoles }
     *     
     */
    public void setGetRolesbyUser(ListadoRoles value) {
        this.getRolesbyUser = value;
    }

}
