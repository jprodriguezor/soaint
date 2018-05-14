package co.com.soaint.foundation.canonical.correspondencia;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created:15-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Data
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/cor-agente/1.0.0")
@ToString
public class DependenciaDTO  implements Serializable {

    private static final long serialVersionUID = 1L;
    @JsonProperty("id")
    private BigInteger ideDependencia;
    @JsonProperty("codigo")
    private String codDependencia;
    @JsonProperty("nombre")
    private String nomDependencia;
    private BigInteger ideSede;
    private String codSede;
    private String nomSede;
    private String estado;
}
