package co.com.soaint.foundation.canonical.ecm;

/**
 * Created by jrodriguez on 19/05/2017.
 */

import lombok.Builder;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created:4-May-2017
 * Author: jrodriguez
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Data
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/ecm/mensajeRespuesta/1.0.0")
public class MensajeRespuesta<T> {

    private String codMensaje;
    private String mensaje;
    private T content;

    public MensajeRespuesta(String mensaje, String codMensaje) {
        this.setMensaje (mensaje);
        this.setCodMensaje (codMensaje);
    }
}
