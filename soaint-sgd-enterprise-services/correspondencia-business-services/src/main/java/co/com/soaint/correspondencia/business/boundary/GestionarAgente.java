package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.AgenteControl;
import co.com.soaint.foundation.canonical.correspondencia.AgenteDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

    private static Logger logger = LogManager.getLogger(GestionarAsignacion.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Autowired
    AgenteControl agenteControl;
    // ----------------------

    public void actualizarEstadoAgente(AgenteDTO agenteDTO) throws BusinessException, SystemException {
        try {
            if (!agenteControl.verificarByIdeAgente(agenteDTO.getIdeAgente())){
                throw ExceptionBuilder.newBuilder()
                        .withMessage("agente.agente_not_exist_by_ideAgente")
                        .buildBusinessException();
            }
            em.createNamedQuery("CorAgente.updateEstado")
                    .setParameter("COD_ESTADO", agenteDTO.getCodEstado())
                    .setParameter("IDE_AGENTE", agenteDTO.getIdeAgente())
                    .executeUpdate();
        } catch (BusinessException e) {
            throw e;
        } catch (Exception ex) {
            logger.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }
}
