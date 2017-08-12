package co.com.soaint.foundation.canonical.correspondencia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
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
}
