package co.com.soaint.foundation.canonical.bpm;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Map;
/**
 * Created by Yosova on 6/7/2018.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/respuestadigitalizar/1.0.0")
@ToString
public class RespuestaDigitalizarDTO {
    private String idDespliegue;
    private String idProceso;
    private Long instanciaProceso;
    private String nombreSennal;

    public RespuestaDigitalizarDTO(String idDespliegue,String idProceso,Long instanciaProceso){
        this(idDespliegue,idProceso,instanciaProceso,null);
    }
}
