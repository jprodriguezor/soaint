package co.com.soaint.foundation.canonical.ecm;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by amartinez on 07/02/2018.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/entrada-record/1.0.0")
@ToString
public class EntradaRecordDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String sede;
    private String dependencia;
    private String serie;
    private String subSerie;
    private String nombreCarpeta;

}
