package co.com.soaint.foundation.canonical.ecm;

/**
 * Created by jrodriguez on 19/05/2017.
 */

import lombok.Builder;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

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
public class MensajeRespuesta {

    private String codMensaje;
    private String mensaje;
    private List<DocumentoDTO> documentoDTOList;
    private List<ContenidoDependenciaTrdDTO> contenidoDependenciaTrdDTOS;

    public MensajeRespuesta() {
        //Constructor por defecto de la clase
    }

    /**
     * Constructor para los parametros mensaje y codigo de mensaje
     *
     * @param mensaje    Mensaje que devuelve el servicio
     * @param codMensaje Codigo de mensaje que devuelve el servicio
     */
    public MensajeRespuesta(String mensaje, String codMensaje) {
        this.codMensaje = codMensaje;
        this.mensaje = mensaje;
        this.documentoDTOList = null;
        this.contenidoDependenciaTrdDTOS = null;
    }

    /**
     * Constructor para los parametros mensaje ,codigo de mensaje y contenido del objeto de respuesta
     *
     * @param codMensaje       Codigo del mensaje de respuesta
     * @param mensaje          Mensaje de respuesta
     * @param documentoDTOList Lista de objetos de metadatos
     */
    public MensajeRespuesta(String codMensaje, String mensaje, List<DocumentoDTO> documentoDTOList, List<ContenidoDependenciaTrdDTO> contenidoDependenciaTrdDTOS) {
        this.codMensaje = codMensaje;
        this.mensaje = mensaje;
        this.documentoDTOList = documentoDTOList;
        this.contenidoDependenciaTrdDTOS = contenidoDependenciaTrdDTOS;
    }
}
