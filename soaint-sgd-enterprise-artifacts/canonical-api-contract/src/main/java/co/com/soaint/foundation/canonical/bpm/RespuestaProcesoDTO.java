package co.com.soaint.foundation.canonical.bpm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Arce on 6/7/2017.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/respuestaproceso/1.0.0")
public class RespuestaProcesoDTO {
    private String codigoProceso;
    private String estado;
    private String nombreProceso;

}
