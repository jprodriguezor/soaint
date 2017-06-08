package co.com.soaint.foundation.canonical.bpm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Created by Arce on 6/7/2017.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/respuestatarea/1.0.0")
public class RespuestaTareaDTO {

    private Long id;
    private String nombre;
    private String estado;
    private Integer prioridad;
    private String idResponsable;
    private String idCreador;
    private  Date fechaCreada;
    private Date tiempoActivacion;
    private Date tiempoExpiracion;
    private String idProceso;
    private Long idInstanciaPorceso;
    private String idDespliegue;
    private Long idParent;

}
