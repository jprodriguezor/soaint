
package co.com.soaint.funcionario.apis.delegator.security.wsclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for eliminarUsuarioporNombreResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="eliminarUsuarioporNombreResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="eliminarUsuarioporNombre" type="{http://www.soaint.com/services/security-cartridge/1.0.0}OperationStatus" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "eliminarUsuarioporNombreResponse", propOrder = {
    "eliminarUsuarioporNombre"
})
public class EliminarUsuarioporNombreResponse {

    protected OperationStatus eliminarUsuarioporNombre;

    /**
     * Gets the value of the eliminarUsuarioporNombre property.
     * 
     * @return
     *     possible object is
     *     {@link OperationStatus }
     *     
     */
    public OperationStatus getEliminarUsuarioporNombre() {
        return eliminarUsuarioporNombre;
    }

    /**
     * Sets the value of the eliminarUsuarioporNombre property.
     * 
     * @param value
     *     allowed object is
     *     {@link OperationStatus }
     *     
     */
    public void setEliminarUsuarioporNombre(OperationStatus value) {
        this.eliminarUsuarioporNombre = value;
    }

}
