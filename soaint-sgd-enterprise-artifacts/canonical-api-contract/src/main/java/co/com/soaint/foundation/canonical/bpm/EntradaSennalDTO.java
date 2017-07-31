package co.com.soaint.foundation.canonical.bpm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

/**
 * Created by Ernesto on 2017-07-21.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/entradasennal/1.0.0")
public class EntradaSennalDTO {
    private String idDespliegue;
    private String idProceso;
    private Long instanciaProceso;
    private String nombreSennal;
    private String usuario;
    private String pass;
    private Map<String, Object> parametros;
}
