package co.com.soaint.foundation.canonical.correspondencia;

import lombok.Builder;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
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

@Data
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/cor-plan-agen/1.0.0")
public class CorPlanAgenDTO {
    private Long idePlanAgen;
    private String varPeso;
    private String varValor;
    private String varNumeroGuia;
    private Date fecObservacion;
    private String codNuevaSede;
    private String codNuevaDepen;
    private String observaciones;
    private String codCauDevo;
    private Date fecCarguePla;

    public CorPlanAgenDTO(){super();}

    public CorPlanAgenDTO(Long idePlanAgen, String varPeso, String varValor, String varNumeroGuia, Date fecObservacion,
                          String codNuevaSede, String codNuevaDepen, String observaciones, String codCauDevo, Date fecCarguePla){
        this.idePlanAgen = idePlanAgen;
        this.varPeso = varPeso;
        this.varValor = varValor;
        this.varNumeroGuia = varNumeroGuia;
        this.fecObservacion = fecObservacion;
        this.codNuevaSede = codNuevaSede;
        this.codNuevaDepen = codNuevaDepen;
        this.observaciones = observaciones;
        this.codCauDevo = codCauDevo;
        this.fecCarguePla = fecCarguePla;
    }
}
