package co.com.soaint.foundation.canonical.correspondencia;

import lombok.Builder;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigInteger;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created:2-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Data
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/cor-anexo/1.0.0")
public class AnexoDTO {
    private BigInteger ideAnexo;
    private String codAnexo;
    private String descripcion;

    public AnexoDTO(){super();}

    public AnexoDTO(BigInteger ideAnexo, String codAnexo, String descripcion){
        this.ideAnexo = ideAnexo;
        this.codAnexo = codAnexo;
        this.descripcion = descripcion;
    }
}
