package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.DocumentoControl;
import co.com.soaint.foundation.canonical.correspondencia.DocumentoDTO;
import co.com.soaint.foundation.canonical.correspondencia.ObservacionesDocumentoDTO;
import co.com.soaint.foundation.canonical.correspondencia.PpdTrazDocumentoDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;

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

    @Autowired
    DocumentoControl control;

    // ----------------------

    /**
     *
     * @param documentoDTO
     * @throws BusinessException
     * @throws SystemException
     */
    public void actualizarReferenciaECM(DocumentoDTO documentoDTO) throws BusinessException, SystemException {
        control.actualizarReferenciaECM(documentoDTO);
    }

    /**
     *
     * @param ppdTrazDocumentoDTO
     * @throws SystemException
     * @throws BusinessException
     */
    public void generarTrazaDocumento(PpdTrazDocumentoDTO ppdTrazDocumentoDTO) throws SystemException, BusinessException {
        control.generarTrazaDocumento(ppdTrazDocumentoDTO);
    }

    /**
     *
     * @param ideDocumento
     * @return
     * @throws SystemException
     */
    public ObservacionesDocumentoDTO listarObservacionesDocumento(BigInteger ideDocumento) throws SystemException{
        return control.listarObservacionesDocumento(ideDocumento);
    }
}
