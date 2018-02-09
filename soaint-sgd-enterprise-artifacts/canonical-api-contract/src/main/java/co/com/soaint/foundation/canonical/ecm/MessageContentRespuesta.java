package co.com.soaint.foundation.canonical.ecm;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author Alien GR.
 */
@Data
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/ecm/mensajeRespuesta/1.0.0")
public class MessageContentRespuesta<T extends Serializable> extends MensajeRespuesta {

    private T content;

    public MessageContentRespuesta() {
    }

    public MessageContentRespuesta(String mensaje, String codMensaje, T content) {
        super(mensaje, codMensaje);
        this.content = content;
    }
}
