package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.AgenteControl;
import co.com.soaint.foundation.canonical.correspondencia.*;
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
public class GestionarAgente {
    // [fields] -----------------------------------

    @Autowired
    AgenteControl control;
    // ----------------------

    /**
     * @param agenteDTO
     * @throws BusinessException
     * @throws SystemException
     */
    public void actualizarEstadoAgente(AgenteDTO agenteDTO) throws BusinessException, SystemException {
        control.actualizarEstadoAgente(agenteDTO);
    }

    /**
     * @param redireccion
     * @throws SystemException
     */
    public void redireccionarCorrespondencia(RedireccionDTO redireccion) throws SystemException {
        control.redireccionarCorrespondencia(redireccion);
    }

    /**
     * @param devolucion
     * @throws SystemException
     */
    public void devolverCorrespondencia(DevolucionDTO devolucion) throws SystemException {
        control.devolverCorrespondencia(devolucion);
    }

    /**
     * @param destinatarioDTO
     * @throws BusinessException
     * @throws SystemException
     */
    public String actualizarDestinatario(DestinatarioDTO destinatarioDTO) throws SystemException {
        return control.actualizarDestinatario(destinatarioDTO);
    }

    /**
     * @param remitenteDTO
     * @throws BusinessException
     * @throws SystemException
     */
    public String actualizarRemitente(RemitenteDTO remitenteDTO) throws SystemException {
        return control.actualizarRemitente(remitenteDTO);
    }
}
