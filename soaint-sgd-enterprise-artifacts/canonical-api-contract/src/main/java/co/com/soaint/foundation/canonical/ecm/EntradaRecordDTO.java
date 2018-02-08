package co.com.soaint.foundation.canonical.ecm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by amartinez on 07/02/2018.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/entrada-record/1.0.0")
public class EntradaRecordDTO {

    private String sede;
    private String dependencia;
    private String serie;
    private String subSerie;
    private String nombreCarpeta;

}
