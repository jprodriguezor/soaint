package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.AgenteControl;
import co.com.soaint.foundation.canonical.correspondencia.AgenteDTO;
import co.com.soaint.foundation.canonical.correspondencia.AgentesDTO;
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

    public void actualizarEstadoAgente(AgenteDTO agenteDTO) throws BusinessException, SystemException {
        control.actualizarEstadoAgente(agenteDTO);
    }

    public void redireccionarCorrespondencia(AgentesDTO agentesDTO) throws SystemException {
        control.redireccionarCorrespondencia(agentesDTO);
    }
}
