package co.com.soaint.foundation.canonical.correspondencia;

import lombok.Builder;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

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
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/cor-referido/1.0.0")
public class CorReferidoDTO {
    private Long ideReferido;
    private String nroRadRef;

    public CorReferidoDTO(){super();}

    public CorReferidoDTO(Long ideReferido, String nroRadRef){
        this.ideReferido = ideReferido;
        this.nroRadRef = nroRadRef;
    }
}
