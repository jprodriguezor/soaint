package co.com.soaint.foundation.canonical.correspondencia;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigInteger;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created:06-Sep-2017
 * Author: jrodriguez
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/tarea/1.0.0")
@ToString
public class TareaDTO {
    private BigInteger ideTarea;
    private String idInstanciaProceso;
    private String idTareaProceso;
    private Object payload;
}
