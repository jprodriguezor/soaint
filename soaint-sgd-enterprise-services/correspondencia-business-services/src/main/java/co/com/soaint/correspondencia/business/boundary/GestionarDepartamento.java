package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.foundation.canonical.correspondencia.DepartamentoDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.common.MessageUtil;
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
 * Created: 25-May-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessBoundary
public class GestionarDepartamento {

    // [fields] -----------------------------------

    private static Logger LOGGER = LogManager.getLogger(GestionarDepartamento.class.getName());

    @PersistenceContext
    private EntityManager em;

    // ----------------------

    public GestionarDepartamento() {
        super();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<DepartamentoDTO> listarDepartamentosByEstado(String estado) throws BusinessException, SystemException {
        try {
            return em.createNamedQuery("TvsDepartamento.findAll", DepartamentoDTO.class)
                    .setParameter("ESTADO", estado)
                    .getResultList();
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(MessageUtil.getInstance("system.generic.error").getMessage("system.generic.error"))
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<DepartamentoDTO> listarDepartamentosByCodPaisAndEstado(String codPais, String estado) throws BusinessException, SystemException {
        try {
            return em.createNamedQuery("TvsDepartamento.findAllByCodPaisAndEstado", DepartamentoDTO.class)
                    .setParameter("COD_PAIS", codPais)
                    .setParameter("ESTADO", estado)
                    .getResultList();
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(MessageUtil.getInstance("system.generic.error").getMessage("system.generic.error"))
                    .withRootException(ex)
                    .buildSystemException();
        }
    }
}
