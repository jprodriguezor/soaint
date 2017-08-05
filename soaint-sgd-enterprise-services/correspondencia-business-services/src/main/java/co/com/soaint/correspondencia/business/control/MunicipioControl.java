package co.com.soaint.correspondencia.business.control;

import co.com.soaint.foundation.canonical.correspondencia.MunicipioDTO;
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
public class MunicipioControl {

    // [fields] -----------------------------------

    private static Logger logger = LogManager.getLogger(MunicipioControl.class.getName());

    @PersistenceContext
    private EntityManager em;

    // ----------------------

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<MunicipioDTO> listarMunicipiosByCodDeparAndEstado(String codDepar, String estado) throws SystemException {
        try {
            return em.createNamedQuery("TvsMunicipio.findAllByCodDeparAndEstado", MunicipioDTO.class)
                    .setParameter("COD_DEPAR", codDepar)
                    .setParameter("ESTADO", estado)
                    .getResultList();
        } catch (Exception ex) {
            logger.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<MunicipioDTO> listarMunicipiosByEstado(String estado) throws SystemException {
        try {
            return em.createNamedQuery("TvsMunicipio.findAll", MunicipioDTO.class)
                    .setParameter("ESTADO", estado)
                    .getResultList();
        } catch (Exception ex) {
            logger.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }
}
