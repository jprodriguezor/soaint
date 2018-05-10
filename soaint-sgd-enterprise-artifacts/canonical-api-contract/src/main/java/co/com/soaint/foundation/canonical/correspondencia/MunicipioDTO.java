package co.com.soaint.foundation.canonical.correspondencia;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

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

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/municipio/1.0.0")
@ToString
public class MunicipioDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private BigInteger ideMunic;
    @JsonProperty("nombre")
    private String nombreMunic;
    @JsonProperty("codigo")
    private String codMunic;
    private DepartamentoDTO departamento;

    /**
     *
     * @param ideMunic
     * @param nombreMunic
     * @param codMunic
     */
    public MunicipioDTO(BigInteger ideMunic, String nombreMunic, String codMunic){
        this.ideMunic = ideMunic;
        this.nombreMunic = nombreMunic;
        this.codMunic = codMunic;
    }
}
