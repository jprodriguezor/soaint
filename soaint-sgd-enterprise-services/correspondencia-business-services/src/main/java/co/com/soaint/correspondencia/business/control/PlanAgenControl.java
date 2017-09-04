package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.CorAgente;
import co.com.soaint.correspondencia.domain.entity.CorCorrespondencia;
import co.com.soaint.correspondencia.domain.entity.CorPlanAgen;
import co.com.soaint.foundation.canonical.correspondencia.PlanAgenDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import lombok.extern.log4j.Log4j2;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 04-Sep-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
@Log4j2
public class PlanAgenControl {
    public CorPlanAgen corPlanAgenTransform(PlanAgenDTO planAgenDTO){
        return CorPlanAgen.newInstance()
                .idePlanAgen(planAgenDTO.getIdePlanAgen())
                .varPeso(planAgenDTO.getVarPeso())
                .varValor(planAgenDTO.getVarValor())
                .varNumeroGuia(planAgenDTO.getVarNumeroGuia())
                .fecObservacion(planAgenDTO.getFecObservacion())
                .codNuevaSede(planAgenDTO.getCodNuevaSede())
                .codNuevaDepen(planAgenDTO.getCodNuevaDepen())
                .observaciones(planAgenDTO.getObservaciones())
                .codCauDevo(planAgenDTO.getCodCauDevo())
                .fecCarguePla(planAgenDTO.getFecCarguePla())
                .corAgente(CorAgente.newInstance().ideAgente(planAgenDTO.getIdeAgente()).build())
                .corCorrespondencia(CorCorrespondencia.newInstance().ideDocumento(planAgenDTO.getIdeDocumento()).build())
                .build();
    }
}
