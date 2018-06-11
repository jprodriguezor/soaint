package co.com.soaint.correspondencia.business.control;

import co.com.soaint.foundation.canonical.correspondencia.PaisDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
@Log4j2
public class PaisControl {

    // [fields] -----------------------------------

    @PersistenceContext
    private EntityManager em;

    // ----------------------

    /**
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
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param nombrePais
     * @param estado
     * @return
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<PaisDTO> listarPaisesByNombrePaisAndEstado(String nombrePais, String estado) throws SystemException {
        try {
            return em.createNamedQuery("TvsPais.findByNombrePaisAndEstado", PaisDTO.class)
                    .setParameter("NOMBRE_PAIS", "%" + nombrePais + "%")
                    .setParameter("ESTADO", estado)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     *
     * @param codDepar
     * @return
     * @throws SystemException
     * @throws BusinessException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public PaisDTO consultarPaisByCodDepar(String codDepar) throws SystemException, BusinessException {
        try {
            return em.createNamedQuery("TvsPais.findByCodDepar", PaisDTO.class)
                    .setParameter("COD_DEPAR", codDepar)
                    .getSingleResult();
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("pais.pais_not_exist_by_codDepar")
                    .withRootException(n)
                    .buildBusinessException();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     *
     * @param codPais
     * @return
     * @throws SystemException
     * @throws BusinessException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public PaisDTO consultarPaisByCod(String codPais) throws SystemException, BusinessException {
        try {
            if (codPais == null) {
                return null;
            }
            return em.createNamedQuery("TvsPais.findByCod", PaisDTO.class)
                    .setParameter("COD_PAIS", codPais)
                    .getSingleResult();
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
//            throw ExceptionBuilder.newBuilder()
//                    .withMessage("pais.pais_not_exist_by_code")
//                    .withRootException(n)
//                    .buildBusinessException();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
//            throw ExceptionBuilder.newBuilder()
//                    .withMessage("system.generic.error")
//                    .withRootException(ex)
//                    .buildSystemException();
        }
        return  null;
    }
}
