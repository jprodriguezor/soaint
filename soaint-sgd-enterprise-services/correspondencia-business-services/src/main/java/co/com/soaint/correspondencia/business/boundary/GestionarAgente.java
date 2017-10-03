package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.AgenteControl;
import co.com.soaint.foundation.canonical.correspondencia.AgenteDTO;
import co.com.soaint.foundation.canonical.correspondencia.RedireccionDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;

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
public class GestionarAgente {
    // [fields] -----------------------------------

    @Autowired
    AgenteControl control;
    // ----------------------

    /**
     *
     * @param agenteDTO
     * @throws BusinessException
     * @throws SystemException
     */
    public void actualizarEstadoAgente(AgenteDTO agenteDTO) throws BusinessException, SystemException {
        control.actualizarEstadoAgente(agenteDTO);
    }

    /**
     *
     * @param redireccion
     * @throws SystemException
     */
    public void redireccionarCorrespondencia(RedireccionDTO redireccion) throws SystemException {
        control.redireccionarCorrespondencia(redireccion);
    }

    /**
     *
     * @param ideAgente
     * @throws BusinessException
     * @throws SystemException
     */
    public void actualizarNumDevoluciones(BigInteger ideAgente)throws BusinessException, SystemException{
        control.actualizarNumDevoluciones(ideAgente);
    }
}
