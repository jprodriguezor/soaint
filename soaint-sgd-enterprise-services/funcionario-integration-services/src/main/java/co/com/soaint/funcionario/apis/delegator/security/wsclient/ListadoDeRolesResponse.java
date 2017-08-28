
package co.com.soaint.funcionario.apis.delegator.security.wsclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for listadoDeRolesResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="listadoDeRolesResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="listadoDeRoles" type="{http://www.soaint.com/services/security-cartridge/1.0.0}OperationRolesListStatus" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listadoDeRolesResponse", propOrder = {
    "listadoDeRoles"
})
public class ListadoDeRolesResponse {

    protected OperationRolesListStatus listadoDeRoles;

    /**
     * Gets the value of the listadoDeRoles property.
     * 
     * @return
     *     possible object is
     *     {@link OperationRolesListStatus }
     *     
     */
    public OperationRolesListStatus getListadoDeRoles() {
        return listadoDeRoles;
    }

    /**
     * Sets the value of the listadoDeRoles property.
     * 
     * @param value
     *     allowed object is
     *     {@link OperationRolesListStatus }
     *     
     */
    public void setListadoDeRoles(OperationRolesListStatus value) {
        this.listadoDeRoles = value;
    }

}
