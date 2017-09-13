package co.com.soaint.foundation.canonical.correspondencia;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created:08-Sep-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/sede-administrativa/1.0.0")
public class SedeAdministrativaDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    @JsonProperty("id")
    private BigInteger idSedeAdministrativa;
    @JsonProperty("codigo")
    private String codSede;
    @JsonProperty("nombre")
    private String nomSede;
}
