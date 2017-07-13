package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.foundation.canonical.correspondencia.ConstanteDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
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
 * Created: 24-May-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessBoundary
public class GestionarConstantes {

    // [fields] -----------------------------------

    private static Logger LOGGER = LogManager.getLogger(GestionarConstantes.class.getName());

    @PersistenceContext
    private EntityManager em;


    // ----------------------

    public GestionarConstantes() {
        super();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<ConstanteDTO> listarConstantesByEstado(String estado) throws BusinessException, SystemException {
        try {
            return em.createNamedQuery("TvsConstantes.findAll", ConstanteDTO.class)
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

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<ConstanteDTO> listarConstantesByCodigoAndEstado(String codigo, String estado) throws BusinessException, SystemException {
        try {
            return em.createNamedQuery("TvsConstantes.findAllByCodigoAndEstado", ConstanteDTO.class)
                    .setParameter("CODIGO", codigo)
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

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<ConstanteDTO> listarConstantesByCodPadreAndEstado(String codPadre, String estado)throws BusinessException, SystemException{
        try {
            return em.createNamedQuery("TvsConstantes.findAllByCodPadreAndEstado", ConstanteDTO.class)
                .setParameter("COD_PADRE", codPadre)
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
