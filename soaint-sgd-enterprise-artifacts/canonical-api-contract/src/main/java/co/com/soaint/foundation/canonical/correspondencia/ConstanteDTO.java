package co.com.soaint.foundation.canonical.correspondencia;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created:25-May-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Data
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/constante/1.0.0")
public class ConstanteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private BigInteger ideConst;
    private String codigo;
    private String nombre;
    private String codPadre;

    public ConstanteDTO(){super();}

    public ConstanteDTO(BigInteger ideConst, String codigo, String nombre, String codPadre){
        this.ideConst = ideConst;
        this.codigo = codigo;
        this.nombre = nombre;
        this.codPadre = codPadre;
    }
}
