package co.com.soaint.correspondencia.business.control;

import co.com.soaint.foundation.canonical.correspondencia.PaisDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 03-Ago-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
public class PaisControl {

    // [fields] -----------------------------------

    private static Logger logger = LogManager.getLogger(PaisControl.class.getName());

    @PersistenceContext
    private EntityManager em;

    // ----------------------

    /**
     *
     * @param estado
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<PaisDTO> listarPaisesByEstado(String estado) throws SystemException {
        try {
            return em.createNamedQuery("TvsPais.findAll", PaisDTO.class)
                    .setParameter("ESTADO", estado)
                    .getResultList();
        } catch (Exception ex) {
            logger.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     *
     * @param nombrePais
     * @param estado
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<PaisDTO> listarPaisesByNombrePaisAndEstado(String nombrePais, String estado) throws SystemException{
        try {
            return em.createNamedQuery("TvsPais.findByNombrePaisAndEstado", PaisDTO.class)
                    .setParameter("NOMBRE_PAIS", "%" + nombrePais + "%")
                    .setParameter("ESTADO", estado)
                    .getResultList();
        } catch (Exception ex) {
            logger.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }
}
