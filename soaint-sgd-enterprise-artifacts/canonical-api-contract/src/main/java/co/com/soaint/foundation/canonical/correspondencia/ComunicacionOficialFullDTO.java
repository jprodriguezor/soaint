package co.com.soaint.foundation.canonical.correspondencia;

import lombok.*;

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
@ToString
public class ComunicacionOficialFullDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private CorrespondenciaFullDTO correspondencia;
    private List<AgenteFullDTO> agentes;
    private List<PpdDocumentoFullDTO> ppdDocumentos;
    private List<AnexoFullDTO> anexos;
    private List<ReferidoDTO> referidos;
    private List<DatosContactoFullDTO> datosContactos;
}
