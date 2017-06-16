package co.com.soaint.foundation.canonical.correspondencia;

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
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/municipio/1.0.0")
public class MunicipioDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private BigInteger ideMunic;
    private String nombreMunic;
    private String codMunic;
    private String codDepar;

    public MunicipioDTO(){super();}

    public MunicipioDTO(BigInteger ideMunic, String nombreMunic, String codMunic, String codDepar){
        this.ideMunic = ideMunic;
        this.nombreMunic = nombreMunic;
        this.codMunic = codMunic;
        this.codDepar = codDepar;
    }
}
