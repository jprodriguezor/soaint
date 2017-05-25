package co.com.soaint.foundation.canonical.correspondencia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Generic Artifact
 * Created: 25-May-2017
 * Author: esanchez
 * Type: JAVA class
 * Artifact Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Data
@Builder(builderMethodName="newInstance")
@AllArgsConstructor
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/constantes/1.0.0")
public class ConstantesDTO {
    private List<ConstanteDTO> constantes;

    public ConstantesDTO(){}
}
