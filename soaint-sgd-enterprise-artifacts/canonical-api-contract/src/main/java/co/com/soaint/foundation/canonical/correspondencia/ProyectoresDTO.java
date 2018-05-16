package co.com.soaint.foundation.canonical.correspondencia;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@Builder(builderMethodName="newInstance")
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/proyectores/1.0.0")
@ToString
public class ProyectoresDTO {

    private static final long serialVersionUID = 1L;

    List<ProyectorDTO> proyectores;

}
