package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.DctAsignacion;
import co.com.soaint.foundation.canonical.correspondencia.AsignacionDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 11-Jul-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
public class DctAsignacionControl {
    /**
     *
     * @param asignacionDTO
     * @return
     */
    public DctAsignacion dctAsignacionTransform(AsignacionDTO asignacionDTO){
        return DctAsignacion.newInstance()
                .ideAsignacion(asignacionDTO.getIdeAsignacion())
                .fecAsignacion(asignacionDTO.getFecAsignacion())
                .ideFunci(asignacionDTO.getIdeFunci())
                .codDependencia(asignacionDTO.getCodDependencia())
                .codTipAsignacion(asignacionDTO.getCodTipAsignacion())
                .observaciones(asignacionDTO.getObservaciones())
                .codTipCausal(asignacionDTO.getCodTipCausal())
                .codTipProceso(asignacionDTO.getCodTipProceso())
                .build();
    }
}
