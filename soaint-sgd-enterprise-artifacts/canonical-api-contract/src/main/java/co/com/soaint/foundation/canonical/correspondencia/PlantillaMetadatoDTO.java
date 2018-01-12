package co.com.soaint.foundation.canonical.correspondencia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigInteger;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created:11-Ene-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/plantilla-metadato/1.0.0")
public class PlantillaMetadatoDTO {
    private BigInteger idePlantillaMetadato;
    private String codMetadato;
    private String nombMetadato;
}
