package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.CorAnexo;
import co.com.soaint.foundation.canonical.correspondencia.AnexoDTO;
import co.com.soaint.foundation.canonical.correspondencia.PpdDocumentoDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;

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
public class AnexoControl {

    @PersistenceContext
    private EntityManager em;

    public List<AnexoDTO> consultarAnexosByPpdDocumentos(List<PpdDocumentoDTO> ppdDocumentoDTOList){
        List<AnexoDTO> anexoList = new ArrayList<>();
        for (PpdDocumentoDTO ppdDocumentoDTO : ppdDocumentoDTOList) {
            em.createNamedQuery("CorAnexo.findByIdePpdDocumento", AnexoDTO.class)
                    .setParameter("IDE_PPD_DOCUMENTO", ppdDocumentoDTO.getIdePpdDocumento())
                    .getResultList()
                    .stream()
                    .forEach((anexoDTO) -> {
                        anexoList.add(anexoDTO);
                    });
        }
        return anexoList;
    }

    public CorAnexo corAnexoTransform(AnexoDTO anexoDTO){
        return CorAnexo.newInstance()
                .codAnexo(anexoDTO.getCodAnexo())
                .descripcion(anexoDTO.getDescripcion())
                .build();
    }
}
