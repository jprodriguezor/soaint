package co.com.soaint.foundation.canonical.correspondencia;

import lombok.*;

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
@ToString
public class PlanAgenSalidaDTO implements Serializable {

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
    private AgenteDTO agente;
    private CorrespondenciaDTO correspondencia;
    private ConstanteDTO tipoPersona;
    private String nit;
    private String nroDocuIdentidad;
    private String nombre;
    private String razonSocial;
    private ConstanteDTO tipologiaDocumental;
    private Long folios;
    private Long anexos;

    public PlanAgenSalidaDTO(BigInteger idePlanAgen, String estado, String varPeso, String varValor, String varNumeroGuia, Date fecObservacion,
                             String codNuevaSede, String codNuevaDepen, String observaciones, String codCauDevo, Date fecCarguePla,
                             AgenteDTO agente, CorrespondenciaDTO correspondencia){
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
        this.agente = agente;
        this.correspondencia = correspondencia;
    }
}
