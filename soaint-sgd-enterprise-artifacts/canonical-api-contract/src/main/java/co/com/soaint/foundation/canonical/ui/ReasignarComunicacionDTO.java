package co.com.soaint.foundation.canonical.ui;

import co.com.soaint.foundation.canonical.correspondencia.AsignacionesDTO;
import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/reasignar-comunicacion/1.0.0")
public class ReasignarComunicacionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private AsignacionesDTO asignaciones;

    private String usuario;

    private String pass;
}
