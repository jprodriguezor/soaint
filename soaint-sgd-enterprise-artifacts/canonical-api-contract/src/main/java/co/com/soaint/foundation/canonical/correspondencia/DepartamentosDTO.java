package co.com.soaint.foundation.canonical.correspondencia;

/**
 * Created by esanchez on 5/25/2017.
 */

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
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
@NoArgsConstructor
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/departamentos/1.0.0")
@ToString
public class DepartamentosDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<DepartamentoDTO> departamentos;
}
