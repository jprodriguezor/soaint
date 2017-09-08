package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.PlanillasControl;
import co.com.soaint.foundation.canonical.correspondencia.PlanillaDTO;
import co.com.soaint.foundation.canonical.correspondencia.ReportDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 14-Jul-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessBoundary
@NoArgsConstructor
public class GestionarPlanillas {

    // [fields] -----------------------------------

    @Autowired
    PlanillasControl control;

    // ----------------------

    /**
     *
     * @param planilla
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public PlanillaDTO generarPlanilla(PlanillaDTO planilla) throws BusinessException, SystemException {
        return control.generarPlanilla(planilla);
    }

    /**
     *
     * @param planilla
     * @throws BusinessException
     * @throws SystemException
     */
    public void cargarPlanilla(PlanillaDTO planilla) throws BusinessException, SystemException {
        control.cargarPlanilla(planilla);
    }

    /**
     *
     * @param nroPlanilla
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public PlanillaDTO listarPlanillasByNroPlanilla(String nroPlanilla) throws BusinessException, SystemException {
        return control.listarPlanillasByNroPlanilla(nroPlanilla);
    }

    /**
     *
     * @param nroPlanilla
     * @param formato
     * @return
     * @throws SystemException
     */
    public ReportDTO exportarPlanilla(String nroPlanilla, String formato) throws SystemException {
        return control.exportarPlanilla(nroPlanilla, formato);
    }
}
