package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.PpdDocumentoControl;
import co.com.soaint.foundation.canonical.correspondencia.DocumentoDTO;
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
import java.math.BigInteger;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 1-Ago-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessBoundary
@NoArgsConstructor
public class GestionarDocumento {
    // [fields] -----------------------------------

    private static Logger logger = LogManager.getLogger(GestionarAsignacion.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Autowired
    PpdDocumentoControl ppdDocumentoControl;

    // ----------------------

    public void actualizarReferenciaECM(DocumentoDTO documentoDTO) throws BusinessException, SystemException {
        try {
            List<BigInteger> idePpdDocumentoList = ppdDocumentoControl.consultarPpdDocumentosByNroRadicado(documentoDTO.getNroRadicado());
            if (idePpdDocumentoList.isEmpty()) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("correspondencia.ppdDocumento_not_exist_by_nroRadicado")
                        .buildBusinessException();
            }

            em.createNamedQuery("PpdDocumento.updateIdEcm")
                    .setParameter("IDE_PPDDOCUMENTO", idePpdDocumentoList.get(0))
                    .setParameter("IDE_ECM", documentoDTO.getIdeEcm())
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
