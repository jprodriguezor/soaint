package co.com.soaint.foundation.canonical.ecm;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/disposicion-final/1.0.0")
public class DisposicionFinalDTO implements Serializable {

    private static final Long serialVersionUID = 1L;

    private UnidadDocumentalDTO unidadDocumentalDTO;
    private List<String> disposicionFinalList;
}
