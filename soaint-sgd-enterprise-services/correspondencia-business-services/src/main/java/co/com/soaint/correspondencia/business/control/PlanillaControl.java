package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.CorPlanAgen;
import co.com.soaint.correspondencia.domain.entity.CorPlanillas;
import co.com.soaint.foundation.canonical.correspondencia.PlanillaDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
public class PlanillaControl {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PlanAgenControl planAgenControl;

    public PlanillaDTO generarPlanilla(PlanillaDTO planilla)throws SystemException{
        try {
            CorPlanillas corPlanillas = corPlanillasTransform(planilla);
            planilla.getPlanAgenList().stream().forEach(planAgenDTO -> {
                CorPlanAgen corPlanAgen = planAgenControl.corPlanAgenTransform(planAgenDTO);
                corPlanAgen.setCorPlanillas(corPlanillas);
                corPlanillas.getCorPlanAgenList().add(corPlanAgen);
            });
            em.persist(corPlanillas);
            em.flush();
            return null;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public CorPlanillas corPlanillasTransform(PlanillaDTO planilla){
        return CorPlanillas.newInstance()
                .idePlanilla(planilla.getIdePlanilla())
                .nroPlanilla(planilla.getNroPlanilla())
                .fecGeneracion(planilla.getFecGeneracion())
                .codTipoPlanilla(planilla.getCodTipoPlanilla())
                .codFuncGenera(planilla.getCodFuncGenera())
                .codSedeOrigen(planilla.getCodSedeOrigen())
                .codDependenciaOrigen(planilla.getCodDependenciaOrigen())
                .codSedeDestino(planilla.getCodSedeDestino())
                .codDependenciaDestino(planilla.getCodDependenciaDestino())
                .codClaseEnvio(planilla.getCodClaseEnvio())
                .codModalidadEnvio(planilla.getCodModalidadEnvio())
                .build();
    }
}
