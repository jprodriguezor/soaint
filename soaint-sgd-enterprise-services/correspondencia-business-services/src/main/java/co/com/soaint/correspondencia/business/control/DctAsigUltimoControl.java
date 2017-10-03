package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.DctAsigUltimo;
import co.com.soaint.foundation.canonical.correspondencia.DctAsigUltimoDTO;
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
public class DctAsigUltimoControl {
    /**
     *
     * @param dctAsigUltimoDTO
     * @return
     */
    public DctAsigUltimo dctAsigUltimoTransform(DctAsigUltimoDTO dctAsigUltimoDTO){
        return DctAsigUltimo.newInstance()
                .ideAsigUltimo(dctAsigUltimoDTO.getIdeAsigUltimo())
                .nivLectura(dctAsigUltimoDTO.getNivLectura())
                .nivEscritura(dctAsigUltimoDTO.getNivEscritura())
                .fechaVencimiento(dctAsigUltimoDTO.getFechaVencimiento())
                .idInstancia(dctAsigUltimoDTO.getIdInstancia())
                .codTipProceso(dctAsigUltimoDTO.getCodTipProceso())
                .build();
    }
}
