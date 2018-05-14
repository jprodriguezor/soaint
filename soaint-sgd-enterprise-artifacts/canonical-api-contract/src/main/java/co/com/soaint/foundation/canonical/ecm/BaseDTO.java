package co.com.soaint.foundation.canonical.ecm;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "nombreBase")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/ecm/base/1.0.0")
class BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    String codigoBase;
    String nombreBase;
}
