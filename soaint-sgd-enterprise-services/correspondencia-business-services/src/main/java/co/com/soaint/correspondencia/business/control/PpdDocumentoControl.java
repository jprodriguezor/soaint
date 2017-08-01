package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.PpdDocumento;
import co.com.soaint.foundation.canonical.correspondencia.PpdDocumentoDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
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
public class PpdDocumentoControl {

    @PersistenceContext
    private EntityManager em;

    public List<PpdDocumentoDTO> consultarPpdDocumentosByCorrespondencia(BigInteger idDocumento){
        return em.createNamedQuery("PpdDocumento.findByIdeDocumento", PpdDocumentoDTO.class)
                .setParameter("IDE_DOCUMENTO", idDocumento)
                .getResultList();
    }

    public List<BigInteger> consultarPpdDocumentosByNroRadicado(String nroRadicado){
        return em.createNamedQuery("PpdDocumento.findIdePpdDocumentoByNroRadicado", BigInteger.class)
                .setParameter("NRO_RADICADO", nroRadicado)
                .getResultList();
    }

    public PpdDocumento ppdDocumentoTransform(PpdDocumentoDTO ppdDocumentoDTO){
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
}
