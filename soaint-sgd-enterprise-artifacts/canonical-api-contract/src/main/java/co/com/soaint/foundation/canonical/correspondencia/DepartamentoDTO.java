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
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/departamento/1.0.0")
@ToString
public class DepartamentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private BigInteger ideDepar;
    @JsonProperty("nombre")
    private String nombreDepar;
    @JsonProperty("codigo")
    private String codDepar;
    private PaisDTO pais;

    /**
     *
     * @param ideDepar
     * @param nombreDepar
     * @param codDepar
     */
    public DepartamentoDTO(BigInteger ideDepar, String nombreDepar, String codDepar){
        this.ideDepar = ideDepar;
        this.nombreDepar = nombreDepar;
        this.codDepar = codDepar;
    }
}
