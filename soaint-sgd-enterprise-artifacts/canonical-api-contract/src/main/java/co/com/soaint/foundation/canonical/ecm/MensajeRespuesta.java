package co.com.soaint.foundation.canonical.ecm;

/**
 * Created by jrodriguez on 19/05/2017.
 */

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

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
@ToString
@NoArgsConstructor
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/ecm/mensajeRespuesta/1.0.0")
public class MensajeRespuesta implements Serializable {

    private static final long serialVersionUID = 1L;

    private String codMensaje;
    private String mensaje;
    private List<DocumentoDTO> documentoDTOList;
    private List<ContenidoDependenciaTrdDTO> contenidoDependenciaTrdDTOS;
    private Map<String, Object> response;

    public MensajeRespuesta(String codMensaje, String mensaje, List<DocumentoDTO> documentoDTOList,
                            List<ContenidoDependenciaTrdDTO> contenidoDependenciaTrdDTOS, Map<String, Object> response) {
        this.codMensaje = codMensaje;
        this.mensaje = mensaje;
        this.documentoDTOList = documentoDTOList;
        this.contenidoDependenciaTrdDTOS = contenidoDependenciaTrdDTOS;
        this.response = response;
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
