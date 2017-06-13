package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.CorAnexo;
import co.com.soaint.foundation.canonical.correspondencia.AnexoDTO;
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
public class AnexoControl {
    public CorAnexo corAnexoTransform(AnexoDTO anexoDTO){
        CorAnexo corAnexo = CorAnexo.newInstance()
                .codAnexo(anexoDTO.getCodAnexo())
                .descripcion(anexoDTO.getDescripcion())
                .build();
        return corAnexo;
    }
}
