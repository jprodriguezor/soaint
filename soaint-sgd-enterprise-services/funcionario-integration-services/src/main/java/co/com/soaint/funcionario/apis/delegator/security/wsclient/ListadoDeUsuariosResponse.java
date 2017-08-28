
package co.com.soaint.funcionario.apis.delegator.security.wsclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for listadoDeUsuariosResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="listadoDeUsuariosResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="listadoDeUsuarios" type="{http://www.soaint.com/services/security-cartridge/1.0.0}OperationPrincipalContextListStatus" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listadoDeUsuariosResponse", propOrder = {
    "listadoDeUsuarios"
})
public class ListadoDeUsuariosResponse {

    protected OperationPrincipalContextListStatus listadoDeUsuarios;

    /**
     * Gets the value of the listadoDeUsuarios property.
     * 
     * @return
     *     possible object is
     *     {@link OperationPrincipalContextListStatus }
     *     
     */
    public OperationPrincipalContextListStatus getListadoDeUsuarios() {
        return listadoDeUsuarios;
    }

    /**
     * Sets the value of the listadoDeUsuarios property.
     * 
     * @param value
     *     allowed object is
     *     {@link OperationPrincipalContextListStatus }
     *     
     */
    public void setListadoDeUsuarios(OperationPrincipalContextListStatus value) {
        this.listadoDeUsuarios = value;
    }

}
