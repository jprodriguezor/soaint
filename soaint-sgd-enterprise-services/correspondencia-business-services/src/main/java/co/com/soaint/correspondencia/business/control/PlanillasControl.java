package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.CorPlanAgen;
import co.com.soaint.correspondencia.domain.entity.CorPlanillas;
import co.com.soaint.foundation.canonical.correspondencia.PlanAgentesDTO;
import co.com.soaint.foundation.canonical.correspondencia.PlanillaDTO;
import co.com.soaint.foundation.canonical.correspondencia.constantes.EstadoAgenteEnum;
import co.com.soaint.foundation.canonical.correspondencia.constantes.EstadoPlanillaEnum;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;

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
public class PlanillasControl {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PlanAgenControl planAgenControl;

    /**
     * @param planilla
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public PlanillaDTO generarPlanilla(PlanillaDTO planilla) throws BusinessException, SystemException {
        try {
            CorPlanillas corPlanillas = corPlanillasTransform(planilla);
            corPlanillas.setFecGeneracion(new Date());
            corPlanillas.setNroPlanilla(generarNumeroPlanilla(corPlanillas.getCodSedeOrigen()));
            corPlanillas.setCorPlanAgenList(new ArrayList<>());
            planilla.getAgentes().getAgente().stream().forEach(planAgenDTO -> {
                CorPlanAgen corPlanAgen = planAgenControl.corPlanAgenTransform(planAgenDTO);
                corPlanAgen.setEstado(EstadoPlanillaEnum.DISTRIBUCION.getCodigo());
                corPlanAgen.setCorPlanillas(corPlanillas);
                corPlanillas.getCorPlanAgenList().add(corPlanAgen);
            });
            em.persist(corPlanillas);
            em.flush();
            return listarPlanillasByNroPlanilla(corPlanillas.getNroPlanilla());
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param nroPlanilla
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public PlanillaDTO listarPlanillasByNroPlanilla(String nroPlanilla) throws BusinessException, SystemException {
        try {
            PlanillaDTO planilla = em.createNamedQuery("CorPlanillas.findByNroPlanilla", PlanillaDTO.class)
                    .setParameter("NRO_PLANILLA", nroPlanilla)
                    .getSingleResult();
            planilla.setAgentes(PlanAgentesDTO.newInstance()
                    .agente(planAgenControl.listarAgentesByIdePlanilla(planilla.getIdePlanilla()))
                    .build());
            return planilla;
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("planillas.planilla_not_exist_by_nroPlanilla")
                    .withRootException(n)
                    .buildBusinessException();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param planilla
     * @return
     */
    public CorPlanillas corPlanillasTransform(PlanillaDTO planilla) {
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

    private String generarNumeroPlanilla(String codSede) {
        String nroPlanilla = em.createNamedQuery("CorPlanillas.findMaxNroPlanillaByCodSede", String.class)
                .setParameter("COD_SEDE", codSede)
                .getSingleResult();
        int consecutivo = 0;
        if (nroPlanilla != null)
            consecutivo = Integer.parseInt(nroPlanilla.substring(nroPlanilla.length() - codSede.length()));
        consecutivo++;
        return conformarNroPlanilla(codSede, consecutivo);
    }

    private String conformarNroPlanilla(String codSede, int consecutivo) {
        String nro = codSede;
        int relleno = 16 - (codSede.length() + String.valueOf(consecutivo).length());
        String formato = "%0"+relleno+"d";
        return nro.concat(String.format(formato, consecutivo));
    }
}
