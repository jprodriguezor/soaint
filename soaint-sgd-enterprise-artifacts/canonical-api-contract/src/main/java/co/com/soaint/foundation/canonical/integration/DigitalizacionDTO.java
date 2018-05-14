package co.com.soaint.foundation.canonical.integration;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created:2-Jun-2017
 * Author: jrodriguez
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/digitalizacion/1.0.0")
@ToString
public class DigitalizacionDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private String nroRadicado;
    private String fileName;
    private String fileType;
    private String encodedFile;
}
