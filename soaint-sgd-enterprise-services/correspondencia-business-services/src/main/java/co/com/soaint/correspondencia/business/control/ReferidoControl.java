package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.CorReferido;
import co.com.soaint.foundation.canonical.correspondencia.ReferidoDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 13-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
public class ReferidoControl {
    public CorReferido corReferidoTransform(ReferidoDTO referidoDTO){
        return CorReferido.newInstance()
                .nroRadRef(referidoDTO.getNroRadRef())
                .build();
    }
}
