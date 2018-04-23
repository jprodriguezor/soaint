package co.com.soaint.foundation.canonical.ecm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Calendar;
import java.util.List;

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

    private String accion;
    private Boolean inactivo;
    private String ubicacionTopografica;
    private Calendar fechaCierre;
    private String id;
    private String faseArchivo;
    private Calendar fechaExtremaInicial;
    private Calendar fechaAutoCierre;
    private String soporte;
    private String codigoUnidadDocumental;
    private String nombreUnidadDocumental;
    private String descriptor2;
    private String descriptor1;
    private Calendar fechaExtremaFinal;
    private Boolean cerrada;

    //heredadas
    private String codigoSubSerie;
    private String nombreSubSerie;

    private String codigoSerie;
    private String nombreSerie;

    private String codigoDependencia;
    private String nombreDependencia;

    private String codigoSede;
    private String nombreSede;

    private String observaciones;

    //Agregacion
    private List<DocumentoDTO> listaDocumentos;
}