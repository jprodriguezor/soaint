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
 * Created:6-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/cor-plan-agen/1.0.0")
public class PlanAgenDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private BigInteger idePlanAgen;
    private String estado;
    private String varPeso;
    private String varValor;
    private String varNumeroGuia;
    private Date fecObservacion;
    private String codNuevaSede;
    private String codNuevaDepen;
    private String observaciones;
    private String codCauDevo;
    private Date fecCarguePla;
    private BigInteger ideAgente;
    private BigInteger ideDocumento;
    private String nroRadicado;
    private ConstanteDTO tipoPersona;
    private String nit;
    private String nroDocuIdentidad;
    private String nombre;
    private String razonSocial;
    private Long folios;
    private Long anexos;

    public PlanAgenDTO(BigInteger idePlanAgen, String estado, String varPeso, String varValor, String varNumeroGuia, Date fecObservacion,
                       String codNuevaSede, String codNuevaDepen, String observaciones, String codCauDevo, Date fecCarguePla,
                       BigInteger ideAgente, BigInteger ideDocumento, String nroRadicado){
        this.idePlanAgen = idePlanAgen;
        this.estado = estado;
        this.varPeso = varPeso;
        this.varValor = varValor;
        this.varNumeroGuia = varNumeroGuia;
        this.fecObservacion = fecObservacion;
        this.codNuevaSede = codNuevaSede;
        this.codNuevaDepen = codNuevaDepen;
        this.observaciones = observaciones;
        this.codCauDevo = codCauDevo;
        this.fecCarguePla = fecCarguePla;
        this.ideAgente = ideAgente;
        this.ideDocumento = ideDocumento;
        this.nroRadicado = nroRadicado;
    }
}
