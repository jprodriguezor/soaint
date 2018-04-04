package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.CorAnexo;
import co.com.soaint.foundation.canonical.correspondencia.AnexoFullDTO;
import co.com.soaint.foundation.canonical.correspondencia.AnexoDTO;
import co.com.soaint.foundation.canonical.correspondencia.AnexosFullDTO;
import co.com.soaint.foundation.canonical.correspondencia.PpdDocumentoDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
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
public class AnexoControl {

    @PersistenceContext
    private EntityManager em;

    /**
     *
     * @param ppdDocumentoDTOList
     * @return
     */
    public List<AnexoDTO> consultarAnexosByPpdDocumentos(List<PpdDocumentoDTO> ppdDocumentoDTOList)throws SystemException{
        List<AnexoDTO> anexoList = new ArrayList<>();
        try {
            for (PpdDocumentoDTO ppdDocumentoDTO : ppdDocumentoDTOList) {
                em.createNamedQuery("CorAnexo.findByIdePpdDocumento", AnexoDTO.class)
                        .setParameter("IDE_PPD_DOCUMENTO", ppdDocumentoDTO.getIdePpdDocumento())
                        .getResultList()
                        .stream()
                        .forEach(anexoList::add);
            }
            return anexoList;
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
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public AnexosFullDTO listarAnexosByNroRadicado(String nroRadicado) throws SystemException {
        try {
            return AnexosFullDTO.newInstance().anexos(em.createNamedQuery("CorAnexo.findAnexosByNroRadicado", AnexoFullDTO.class)
                    .setParameter("NRO_RADICADO", nroRadicado)
                    .getResultList()).build();
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
     * @param anexoDTO
     * @return
     */
    public CorAnexo corAnexoTransform(AnexoDTO anexoDTO){
        return CorAnexo.newInstance()
                .codAnexo(anexoDTO.getCodAnexo())
                .descripcion(anexoDTO.getDescripcion())
                .codTipoSoporte(anexoDTO.getCodTipoSoporte())
                .build();
    }
}
