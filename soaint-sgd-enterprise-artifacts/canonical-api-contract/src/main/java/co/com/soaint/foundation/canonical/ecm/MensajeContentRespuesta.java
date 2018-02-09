package co.com.soaint.foundation.canonical.ecm;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Alien GR.
 */
@Data
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/ecm/mensajeRespuesta/1.0.0")
public class MensajeContentRespuesta<T> extends MensajeRespuesta {

    private T content;

    public MensajeContentRespuesta() {
    }

    public MensajeContentRespuesta(String mensaje, String codMensaje, T content) {
        super(mensaje, codMensaje);
        this.content = content;
    }
}
