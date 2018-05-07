package co.com.soaint.foundation.canonical.correspondencia;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class SolicitudUnidadDocumentalDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private BigInteger idSolicitud;
    private String id;
    private String idConstante;
    private Date fechaHora;
    private String nombreUnidadDocumental;
    private String descriptor1;
    private String descriptor2;
    private String nro;
    private String codigoSerie;
    private String codigoSubSerie;
    private String codigoSede;
    private String codigoDependencia;
    private String idSolicitante;
    private String estado;
    private String accion;
    private String observaciones;
}