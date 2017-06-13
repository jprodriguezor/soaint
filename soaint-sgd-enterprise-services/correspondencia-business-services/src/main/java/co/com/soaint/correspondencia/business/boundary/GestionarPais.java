package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.foundation.canonical.correspondencia.PaisDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.common.MessageUtil;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 23-May-2017
 * Author: jprodriguezor
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessBoundary
public class GestionarPais {

    // [fields] -----------------------------------

    private static Logger LOGGER = LogManager.getLogger(GestionarPais.class.getName());

    @PersistenceContext
    private EntityManager em;
    // ----------------------

    public GestionarPais() {
        super();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<PaisDTO> listarPaisesByEstado(String estado) throws BusinessException, SystemException{
        try {
            return em.createNamedQuery("TvsPais.findAll", PaisDTO.class)
                .setParameter("ESTADO", estado)
                .getResultList();
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

}
