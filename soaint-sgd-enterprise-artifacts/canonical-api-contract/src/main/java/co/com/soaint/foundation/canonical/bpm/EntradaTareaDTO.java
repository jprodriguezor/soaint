package co.com.soaint.foundation.canonical.bpm;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

/**
 * Created by Arce on 6/8/2017.
 */


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/entradatarea/1.0.0")
@ToString
public class EntradaTareaDTO {
    private Long idTarea;
    private String usuario;
    private Long idProceso;
    private Map<String, Object> parametros;

}
