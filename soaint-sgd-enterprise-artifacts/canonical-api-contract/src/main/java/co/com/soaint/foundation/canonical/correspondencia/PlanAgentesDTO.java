package co.com.soaint.foundation.canonical.correspondencia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Generic Artifact
 * Created: 3
 * Author: esanchez
 * Type: JAVA class
 * Artifact Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@Data
@Builder(builderMethodName="newInstance")
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/pla-agentes/1.0.0")
public class PlanAgentesDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<PlanAgenDTO> agente;
}
