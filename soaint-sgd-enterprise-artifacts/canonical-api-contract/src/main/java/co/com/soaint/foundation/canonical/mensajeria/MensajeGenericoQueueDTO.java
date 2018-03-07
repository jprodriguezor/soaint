package co.com.soaint.foundation.canonical.mensajeria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Map;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created:01-Mar-2018
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/mensaje-generico-queue/1.0.0")
public class MensajeGenericoQueueDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    String colaEntrada;
    String colaSalida;
    Map<String, Object> mensajeData;
}
