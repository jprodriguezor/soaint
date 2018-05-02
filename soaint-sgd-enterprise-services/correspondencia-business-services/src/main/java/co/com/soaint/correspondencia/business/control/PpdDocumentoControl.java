package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.PpdDocumento;
import co.com.soaint.foundation.canonical.correspondencia.DatosContactoDTO;
import co.com.soaint.foundation.canonical.correspondencia.DatosContactoFullDTO;
import co.com.soaint.foundation.canonical.correspondencia.PpdDocumentoDTO;
import co.com.soaint.foundation.canonical.correspondencia.PpdDocumentoFullDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
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
public class PpdDocumentoControl {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    ConstantesControl constantesControl;

    /**
     * @param ppdDocumentoDTO
     * @return
     */
    public PpdDocumentoFullDTO datosContactoTransformToFull(PpdDocumentoDTO ppdDocumentoDTO) throws SystemException, BusinessException{
        try{
            return PpdDocumentoFullDTO.newInstance()
                    .asunto(ppdDocumentoDTO.getAsunto())
                    .codEstDoc(ppdDocumentoDTO.getCodEstDoc())
                    .descEstDoc(constantesControl.consultarNombreConstanteByCodigo(ppdDocumentoDTO.getCodEstDoc()))
                    .codTipoDoc(ppdDocumentoDTO.getCodTipoDoc())
                    .descTipoDoc(constantesControl.consultarNombreConstanteByCodigo(ppdDocumentoDTO.getCodTipoDoc()))
                    .fecDocumento(ppdDocumentoDTO.getFecDocumento())
                    .ideEcm(ppdDocumentoDTO.getIdeEcm())
                    .idePpdDocumento(ppdDocumentoDTO.getIdePpdDocumento())
                    .nroAnexos(ppdDocumentoDTO.getNroAnexos())
                    .nroFolios(ppdDocumentoDTO.getNroFolios())
                    .build();
            //pendiente construir transform de lista de contactoFullDTO
        } catch (Exception e){
            log.error("Business Control - a system error has occurred", e);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    /**
     * @param ppdDocumentoDTOList
     * @return
     */
    public List<PpdDocumentoFullDTO> ppdDocumentoListTransformToFull(List<PpdDocumentoDTO> ppdDocumentoDTOList) throws SystemException, BusinessException {
        try{
            List<PpdDocumentoFullDTO> ppdDocumentoFullDTOList = new ArrayList<>();
            for (PpdDocumentoDTO ppdDocumentoDTO:ppdDocumentoDTOList){
                ppdDocumentoFullDTOList.add(datosContactoTransformToFull(ppdDocumentoDTO));
            }

            return ppdDocumentoFullDTOList;

            //pendiente construir transform de lista de contactoFullDTO
        } catch (Exception e){
            log.error("Business Control - a system error has occurred", e);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(e)
                    .buildSystemException();
        }
    }

    /**
     * @param idDocumento
     * @return
     * @throws SystemException
     */
    public List<PpdDocumentoDTO> consultarPpdDocumentosByCorrespondencia(BigInteger idDocumento) throws SystemException {
        try {
            return em.createNamedQuery("PpdDocumento.findByIdeDocumento", PpdDocumentoDTO.class)
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
     * @param nroRadicado
     * @return
     * @throws SystemException
     */
    public List<BigInteger> consultarPpdDocumentosByNroRadicado(String nroRadicado) throws SystemException {
        try {
            return em.createNamedQuery("PpdDocumento.findIdePpdDocumentoByNroRadicado", BigInteger.class)
                    .setParameter("NRO_RADICADO", nroRadicado)
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
     * @param ppdDocumentoDTO
     * @return
     */
    public PpdDocumento ppdDocumentoTransform(PpdDocumentoDTO ppdDocumentoDTO) {
        Date fecha = new Date();
        return PpdDocumento.newInstance()
                .codTipoDoc(ppdDocumentoDTO.getCodTipoDoc())
                .fecDocumento(ppdDocumentoDTO.getFecDocumento())
                .asunto(ppdDocumentoDTO.getAsunto())
                .nroFolios(ppdDocumentoDTO.getNroFolios())
                .nroAnexos(ppdDocumentoDTO.getNroAnexos())
                .codEstDoc(ppdDocumentoDTO.getCodEstDoc())
                .ideEcm(ppdDocumentoDTO.getIdeEcm())
                .fecCreacion(fecha)
                .build();
    }

    /**
     * @param ideDocumento
     * @return
     * @throws SystemException
     * @throws BusinessException
     */
    public BigInteger consultarIdePpdDocumentoByIdeDocumento(BigInteger ideDocumento) throws SystemException, BusinessException {
        try {
            return em.createNamedQuery("PpdDocumento.findIdePpdDocumentoByIdeDocumento", BigInteger.class)
                    .setParameter("IDE_DOCUMENTO", ideDocumento)
                    .getSingleResult();
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("ppdDocumento.ppdDocumento_not_exist_by_ideDocumento")
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
     * @param nroRadicado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public PpdDocumentoDTO consultarDocumentoByNroRadicado(String nroRadicado) throws BusinessException, SystemException {
        try {
            List<PpdDocumentoDTO> ppdDocumentoDTOList = em.createNamedQuery("PpdDocumento.findPpdDocumentoByNroRadicado", PpdDocumentoDTO.class)
                    .setParameter("NRO_RADICADO", nroRadicado)
                    .getResultList();
            if (ppdDocumentoDTOList.isEmpty())
                throw ExceptionBuilder.newBuilder()
                        .withMessage("ppddocumento.documento_not_exist_by_nroRadicado")
                        .buildBusinessException();
            return ppdDocumentoDTOList.get(0);
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
}
