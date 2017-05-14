package co.com.soaint.foundation.canonical.correspondencia;


import lombok.Builder;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigInteger;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created:4-May-2017
 * Author: jrodriguez
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Data
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/pais/1.0.0")
public class PaisDTO {

    private BigInteger id;
    private String nombre;
    private String codigo;

    public PaisDTO(){
        super();
    }

    public PaisDTO(BigInteger id, String nombre, String codigo) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
    }
}
