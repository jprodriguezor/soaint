package co.com.soaint.foundation.canonical.ecm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created:22-Feb-2018
 * Author: dotero
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/ecm/organigrama/1.0.0")
public class UnidadDocumentalDTO {

    private String ideUnidadDocumental;
    private String codigoUnidadDocumental;
    private String nombreUnidadDocumental;
    private String descriptor1;
    private String descriptor2;
    private String codigoSerie;
    private String nombreSerie;
    private String codigoSubSerie;
    private String nombreSubSerie;
    private String codigoSede;
    private String nombreSede;
    private String codigoDependencia;
    private String nombreDependencia;
    private String estado;
    private String accion;
}