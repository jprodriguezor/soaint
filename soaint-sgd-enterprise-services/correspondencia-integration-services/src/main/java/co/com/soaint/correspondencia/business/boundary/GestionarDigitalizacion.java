package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.DigitalizacionControl;
import co.com.soaint.foundation.canonical.integration.DigitalizacionDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 06-Mar-2018
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessBoundary
@NoArgsConstructor
public class GestionarDigitalizacion {

    @Autowired
    DigitalizacionControl control;

    public void digitalizarDocumento(DigitalizacionDTO digitalizacionDTO)throws SystemException{
        control.digitalizarDocumento(digitalizacionDTO);
    }
}
