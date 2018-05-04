package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.SolicitudUnidadDocumentalControl;
import co.com.soaint.foundation.canonical.correspondencia.SolicitudUnidadDocumentalDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 15-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@NoArgsConstructor
@BusinessBoundary
public class GestionarSolicitudUnidadDocumental {
    // [fields] -----------------------------------

    @Autowired
    SolicitudUnidadDocumentalControl control;
    // ----------------------

    /**
     *
     * @param unidadDocumentalDTO
     * * @throws BusinessException
     * @throws SystemException
     */
    public SolicitudUnidadDocumentalDTO crearFuncionario(SolicitudUnidadDocumentalDTO unidadDocumentalDTO)throws SystemException, BusinessException{
       return control.crearSolicitudUnidadDocumental(unidadDocumentalDTO);
    }

}
