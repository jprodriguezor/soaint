package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.CorAgente;
import co.com.soaint.foundation.canonical.correspondencia.AgenteDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
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
public class AgenteControl {

    @PersistenceContext
    private EntityManager em;

    public List<AgenteDTO> consltarAgentesByCorrespondencia(BigInteger idDocumento) {
        List<AgenteDTO> agenteDTOList = em.createNamedQuery("CorAgente.findByIdeDocumento", AgenteDTO.class)
                .setParameter("IDE_DOCUMENTO", idDocumento)
                .getResultList();

        return agenteDTOList;
    }

    public CorAgente corAgenteTransform(AgenteDTO agenteDTO) {
        return CorAgente.newInstance()
                .codTipoRemite(agenteDTO.getCodTipoRemite())
                .codTipoPers(agenteDTO.getCodTipoPers())
                .nombre(agenteDTO.getNombre())
                .nroDocumentoIden(agenteDTO.getNroDocumentoIden())
                .razonSocial(agenteDTO.getRazonSocial())
                .nit(agenteDTO.getNit())
                .codCortesia(agenteDTO.getCodCortesia())
                .codCargo(agenteDTO.getCodCargo())
                .codEnCalidad(agenteDTO.getCodEnCalidad())
                .codTipDocIdent(agenteDTO.getCodTipDocIdent())
                .nroDocuIdentidad(agenteDTO.getNroDocuIdentidad())
                .codSede(agenteDTO.getCodSede())
                .codDependencia(agenteDTO.getCodDependencia())
                .codFuncRemite(agenteDTO.getCodFuncRemite())
                .fecAsignacion(agenteDTO.getFecAsignacion())
                .ideContacto(agenteDTO.getIdeContacto())
                .codTipAgent(agenteDTO.getCodTipAgent())
                .indOriginal(agenteDTO.getIndOriginal())
                .tvsDatosContactoList(new ArrayList<>())
                .build();
    }
}
