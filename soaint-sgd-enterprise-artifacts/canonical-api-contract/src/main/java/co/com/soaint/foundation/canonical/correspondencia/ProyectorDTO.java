package co.com.soaint.foundation.canonical.correspondencia;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/proyector/1.0.0")
@ToString
public class ProyectorDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private ConstanteDTO sedeAdministrativa;
    private DependenciaDTO dependencia;
    private FuncionarioDTO funcionarioProyector;
    private ConstanteDTO tipoPlantilla;

}
