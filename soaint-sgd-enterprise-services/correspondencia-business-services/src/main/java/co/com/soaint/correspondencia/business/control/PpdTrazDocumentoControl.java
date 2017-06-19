package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.PpdTrazDocumento;
import co.com.soaint.foundation.canonical.correspondencia.PpdTrazDocumentoDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import org.springframework.scheduling.annotation.Async;

import java.math.BigInteger;
import java.util.Date;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 15-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
public class PpdTrazDocumentoControl {
    public PpdTrazDocumento ppdTrazDocumentoTransform(PpdTrazDocumentoDTO ppdTrazDocumentoDTO) {
        return PpdTrazDocumento.newInstance()
                .fecTrazDocumento(ppdTrazDocumentoDTO.getFecTrazDocumento())
                .observacion(ppdTrazDocumentoDTO.getObservacion())
                .ideFunci(ppdTrazDocumentoDTO.getIdeFunci())
                .codEstado(ppdTrazDocumentoDTO.getCodEstado())
                .ideDocumento(ppdTrazDocumentoDTO.getIdeDocumento())
                .build();
    }
}
