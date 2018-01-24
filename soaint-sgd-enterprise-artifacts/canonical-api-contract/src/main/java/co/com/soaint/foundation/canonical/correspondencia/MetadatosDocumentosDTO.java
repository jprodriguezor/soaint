package co.com.soaint.foundation.canonical.correspondencia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created:11-Ene-2018
 * Author: dasiel
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/cor-agente/1.0.0")
public class MetadatosDocumentosDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private String idDocumento;
    private String nroRadicado;
    private String tipologiaDocumental;
    private String nombreRemitente;
    private String sede;
    private String dependencia;
    private String nombreDocumento;
    private String idDocumentoPadre;

}
