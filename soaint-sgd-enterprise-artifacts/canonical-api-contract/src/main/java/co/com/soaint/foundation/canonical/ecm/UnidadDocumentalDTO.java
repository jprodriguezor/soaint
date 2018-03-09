package co.com.soaint.foundation.canonical.ecm;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

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

    private String id;
    private String descriptor2;

    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date fechaCierre;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date fechaExtremaInicial;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date fechaExtremaFinal;

    private String soporte;
    private String estado;
    private String ubicacionTopografica;
    private String faseArchivo;
    private String descriptor1;
    private String codigoSubSerie;
    private String nombreSubSerie;
    private String codigoSerie;
    private String nombreSerie;
    private String nombreUnidadDocumental;
    private String codigoUnidadDocumental;
    private String codigoDependencia;
    private String nombreDependencia;
    private String codigoSede;
    private String nombreSede;
    private boolean cerrada;

    private String accion;
}