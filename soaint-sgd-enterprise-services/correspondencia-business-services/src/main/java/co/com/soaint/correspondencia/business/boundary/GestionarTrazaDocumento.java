package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.PpdTrazDocumentoControl;
import co.com.soaint.correspondencia.domain.entity.PpdDocumento;
import co.com.soaint.correspondencia.domain.entity.PpdTrazDocumento;
import co.com.soaint.foundation.canonical.correspondencia.PpdTrazDocumentoDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.Date;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 15-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessBoundary
public class GestionarTrazaDocumento {
    // [fields] -----------------------------------

    private static Logger LOGGER = LogManager.getLogger(GestionarTrazaDocumento.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PpdTrazDocumentoControl ppdTrazDocumentoControl;

    @Async
    public void generarTrazaDocumento(PpdTrazDocumentoDTO ppdTrazDocumentoDTO) throws BusinessException, SystemException {
        PpdTrazDocumento ppdTrazDocumento = ppdTrazDocumentoControl.ppdTrazDocumentoTransform(ppdTrazDocumentoDTO);

        BigInteger idePpdDocumento = em.createNamedQuery("PpdDocumento.findIdePpdDocumentoByIdeDocumento", BigInteger.class)
                .setParameter("IDE_DOCUMENTO", ppdTrazDocumentoDTO.getIdeDocumento())
                .getResultList()
                .get(0);
        ppdTrazDocumento.setPpdDocumento(PpdDocumento.newInstance()
                .idePpdDocumento(idePpdDocumento)
                .build());
        ppdTrazDocumento.setFecTrazDocumento(new Date());
        em.persist(ppdTrazDocumento);
        em.flush();
    }
}
