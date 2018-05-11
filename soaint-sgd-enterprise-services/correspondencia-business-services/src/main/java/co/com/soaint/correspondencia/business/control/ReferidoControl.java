package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.CorReferido;
import co.com.soaint.foundation.canonical.correspondencia.ReferidoDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 13-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
@Log4j2
public class ReferidoControl {

    @PersistenceContext
    private EntityManager em;

    /**
     *
     * @param idDocumento
     * @return
     * @throws SystemException
     */
    public List<ReferidoDTO> consultarReferidosByCorrespondencia(BigInteger idDocumento) throws SystemException {
        try {
            return em.createNamedQuery("CorReferido.findByIdeDocumento", ReferidoDTO.class)
                    .setParameter("IDE_DOCUMENTO", idDocumento)
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
     * @param referidoDTO
     * @return
     */
    public CorReferido corReferidoTransform(ReferidoDTO referidoDTO) {
        return CorReferido.newInstance()
                .nroRadRef(referidoDTO.getNroRadRef())
                .build();
    }

    /**
     * @param nroRadicado
     * @return
     * @throws SystemException
     */
    public String consultarNroRadicadoCorrespondenciaReferida(String nroRadicado) throws BusinessException, SystemException {
        try {
            return em.createNamedQuery("CorReferido.findByNroRadicadoCorrespodenciaReferida", String.class)
                    .setParameter("NRO_RAD", nroRadicado)
                    .getSingleResult();
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("CorReferido.findByNroRadicadoCorrespodenciaReferida")
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
}
