package co.com.soaint.foundation.canonical.correspondencia;

import lombok.Builder;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
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
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/departamento/1.0.0")
public class DepartamentoDTO {
    private BigInteger ideDepar;
    private String nombreDepar;
    private String codDepar;
    private String codPais;

    public DepartamentoDTO(){super();}

    public DepartamentoDTO(BigInteger ideDepar, String nombreDepar, String codDepar, String codPais){
        this.ideDepar = ideDepar;
        this.nombreDepar = nombreDepar;
        this.codDepar = codDepar;
        this.codPais = codPais;
    }
}
