package co.com.soaint.foundation.canonical.digitalizar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

/**
 * Created by amartinez on 12/03/2018.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/mensaje-generico-digitalizar/1.0.0")

public class MensajeGenericoDigitalizarDTO {
    String dirProcesar;
    String dirProcesados;
    Map<String, Object> mensajeData;

    }
