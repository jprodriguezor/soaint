package co.com.soaint.correspondencia.business.control;

import co.com.soaint.foundation.canonical.correspondencia.DocumentoDTO;
import co.com.soaint.foundation.canonical.correspondencia.ObservacionesDocumentoDTO;
import co.com.soaint.foundation.canonical.correspondencia.PpdTrazDocumentoDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
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
public class DocumentoControl {

    // [fields] -----------------------------------

    @PersistenceContext
    private EntityManager em;

    @Autowired
    PpdDocumentoControl ppdDocumentoControl;

    @Autowired
    PpdTrazDocumentoControl ppdTrazDocumentoControl;

    // ----------------------

    /**
     *
     * @param documentoDTO
     * @throws BusinessException
     * @throws SystemException
     */
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
            log.error("Business Control - a business error has occurred", e);
            throw e;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param ppdTrazDocumentoDTO
     */

    public void generarTrazaDocumento(PpdTrazDocumentoDTO ppdTrazDocumentoDTO) throws SystemException, BusinessException {
        ppdTrazDocumentoControl.generarTrazaDocumento(ppdTrazDocumentoDTO);
    }

    /**
     *
     * @param ideDocumento
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public ObservacionesDocumentoDTO listarObservacionesDocumento(BigInteger ideDocumento) throws BusinessException, SystemException{
        return ppdTrazDocumentoControl.listarTrazasDocumento(ideDocumento);
    }
}
