package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.DctAsignacion;
import co.com.soaint.foundation.canonical.correspondencia.DctAsignacionDTO;
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
    public DctAsignacion dctAsignacionTransform(DctAsignacionDTO dctAsignacionDTO){
        return DctAsignacion.newInstance()
                .ideAsignacion(dctAsignacionDTO.getIdeAsignacion())
                .fecAsignacion(dctAsignacionDTO.getFecAsignacion())
                .ideFunci(dctAsignacionDTO.getIdeFunci())
                .codDependencia(dctAsignacionDTO.getCodDependencia())
                .codTipAsignacion(dctAsignacionDTO.getCodTipAsignacion())
                .observaciones(dctAsignacionDTO.getObservaciones())
                .codTipCausal(dctAsignacionDTO.getCodTipCausal())
                .codTipProceso(dctAsignacionDTO.getCodTipProceso())
                .build();
    }
}
