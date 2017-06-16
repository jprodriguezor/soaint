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
 * Soaint Generic Artifact
 * Created:12-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Data
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/correspondencia/1.0.0")
public class ComunicacionOficialDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private CorrespondenciaDTO correspondencia;
    private List<AgenteDTO> agenteList;
    private PpdDocumentoDTO ppdDocumento;
    private List<AnexoDTO> anexoList;
    private List<ReferidoDTO> referidoList;
}
