package co.com.soaint.foundation.canonical.correspondencia;

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
 * Created:11-Jul-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@Data
@Builder(builderMethodName = "newInstance")
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/dct-asignacion/1.0.0")
public class AsignacionDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private BigInteger ideAsignacion;
    private Date fecAsignacion;
    private BigInteger ideFunci;
    private String codDependencia;
    private String codTipAsignacion;
    private String observaciones;
    private String codTipCausal;
    private String codTipProceso;
    private BigInteger ideAsigUltimo;
    private String numRedirecciones;
    private Short nivLectura;
    private Short nivEscritura;
    private Date fechaVencimiento;
    private String idInstancia;
    private BigInteger ideAgente;
    private BigInteger ideDocumento;
    private String nroRadicado;
    private String loginName;

    public AsignacionDTO(BigInteger ideAsignacion, Date fecAsignacion, BigInteger ideFunci, String codDependencia,
                         String codTipAsignacion, String observaciones, String codTipCausal, String codTipProceso,
                         BigInteger ideAsigUltimo, String numRedirecciones, Short nivLectura, Short nivEscritura,
                         Date fechaVencimiento, String idInstancia, BigInteger ideAgente, BigInteger ideDocumento,
                         String nroRadicado){
        this.ideAsignacion = ideAsignacion;
        this.fecAsignacion = fecAsignacion;
        this.ideFunci = ideFunci;
        this.codDependencia = codDependencia;
        this.codTipAsignacion = codTipAsignacion;
        this.observaciones = observaciones;
        this.codTipCausal = codTipCausal;
        this.codTipProceso = codTipProceso;
        this.ideAsigUltimo = ideAsigUltimo;
        this.numRedirecciones = numRedirecciones;
        this.nivLectura = nivLectura;
        this.nivEscritura = nivEscritura;
        this.fechaVencimiento = fechaVencimiento;
        this.idInstancia = idInstancia;
        this.ideAgente = ideAgente;
        this.ideDocumento = ideDocumento;
        this.nroRadicado = nroRadicado;
    }

}