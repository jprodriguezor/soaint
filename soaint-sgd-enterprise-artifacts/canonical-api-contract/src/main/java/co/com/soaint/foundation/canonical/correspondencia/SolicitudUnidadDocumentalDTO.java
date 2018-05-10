package co.com.soaint.foundation.canonical.correspondencia;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created:03-May-2018
 * Author: gyanet
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/cor-solicitud-unidad-documental/1.0.0")
@ToString
public class SolicitudUnidadDocumentalDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private BigInteger idSolicitud;
    private String id;
    private String idConstante;
    private Date fechaHora;
    @NotNull(message = "{error.correspondencia.nombreunidad.notnull}")
    private String nombreUnidadDocumental;
    private String descriptor1;
    private String descriptor2;
    @NotNull(message = "{error.correspondencia.nro.notnull}")
    private String nro;
    private String codigoSerie;
    private String codigoSubSerie;
    @NotNull(message = "{error.correspondencia.codSede.notnull}")
    private String codigoSede;
    @NotNull(message = "{error.correspondencia.codDependencia.notnull}")
    private String codigoDependencia;
    private String idSolicitante;
    private String estado;
    private String accion;
    private String observaciones;
}