package co.com.soaint.foundation.canonical.bpm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Arce on 6/7/2017.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/respuestatarea/1.0.0")
public class RespuestaTareaDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idTarea;
    private String nombre;
    private String estado;
    private Integer prioridad;
    private String idResponsable;
    private String idCreador;
    private Date fechaCreada;
    private Date tiempoActivacion;
    private Date tiempoExpiracion;
    private String idProceso;
    private Long idInstanciaProceso;
    private String idDespliegue;
    private Long idParent;

    public RespuestaTareaDTO(Long idTarea, String nombre, String estado, String idResponsable, Date fechaCreada, Date tiempoActivacion, Date tiempoExpiracion, Long idInstanciaProceso) {
        this.idTarea = idTarea;
        this.nombre = nombre;
        this.estado = estado;
        this.idResponsable = idResponsable;
        this.fechaCreada = fechaCreada;
        this.tiempoActivacion = tiempoActivacion;
        this.tiempoExpiracion = tiempoExpiracion;
        this.idInstanciaProceso = idInstanciaProceso;
    }
}

