package co.com.soaint.foundation.canonical.ecm;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

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
@ToString
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
    private Date fechaCreacion;
    private String tipoDocumento;
    private String tamano;
    private String tipoPadreAdjunto;
    private String versionLabel;

}
