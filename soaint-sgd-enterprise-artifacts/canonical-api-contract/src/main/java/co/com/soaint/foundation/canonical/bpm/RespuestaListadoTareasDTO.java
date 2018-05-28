package co.com.soaint.foundation.canonical.bpm;


import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/respuestalistadotereas/1.0.0")
@ToString
public class RespuestaListadoTareasDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<RespuestaTareaDTO> tareas;
}
