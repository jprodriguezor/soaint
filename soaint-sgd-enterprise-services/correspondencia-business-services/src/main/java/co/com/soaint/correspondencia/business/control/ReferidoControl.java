package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.CorReferido;
import co.com.soaint.foundation.canonical.correspondencia.ReferidoDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;

import javax.persistence.EntityManager;
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
public class ReferidoControl {

    @PersistenceContext
    private EntityManager em;

    /**
     *
     * @param idDocumento
     * @return
     */
    public List<ReferidoDTO> consultarReferidosByCorrespondencia(BigInteger idDocumento){
        return em.createNamedQuery("CorReferido.findByIdeDocumento", ReferidoDTO.class)
                .setParameter("IDE_DOCUMENTO", idDocumento)
                .getResultList();
    }

    /**
     *
     * @param referidoDTO
     * @return
     */
    public CorReferido corReferidoTransform(ReferidoDTO referidoDTO){
        return CorReferido.newInstance()
                .nroRadRef(referidoDTO.getNroRadRef())
                .build();
    }
}
