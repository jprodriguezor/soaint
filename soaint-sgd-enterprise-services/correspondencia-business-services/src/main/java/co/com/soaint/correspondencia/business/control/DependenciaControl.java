package co.com.soaint.correspondencia.business.control;

import co.com.soaint.foundation.canonical.correspondencia.DependenciaDTO;
import co.com.soaint.foundation.canonical.correspondencia.OrganigramaItemDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 21-Jul-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
public class DependenciaControl {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    OrganigramaAdministrativoControl organigramaAdministrativoControl;

    /**
     *
     * @param dependencia
     * @param sede
     * @return
     */
    public DependenciaDTO dependenciaDTOTransform(OrganigramaItemDTO dependencia, OrganigramaItemDTO sede){
        return DependenciaDTO.newInstance()
                .ideDependencia(dependencia.getIdeOrgaAdmin())
                .codDependencia(dependencia.getCodOrg())
                .nomDependencia(dependencia.getNomOrg())
                .estado(dependencia.getEstado())
                .ideSede(sede.getIdeOrgaAdmin())
                .codSede(sede.getCodOrg())
                .nomSede(sede.getNomOrg())
                .build();
    }

    /**
     *
     * @param ideFunci
     * @return
     */
    public List<DependenciaDTO> obtenerDependenciasByFuncionario(BigInteger ideFunci){
        List<DependenciaDTO> dependenciaDTOList = new ArrayList<>();
        List<String> codOrgaAdmiList = em.createNamedQuery("TvsOrgaAdminXFunciPk.findCodOrgaAdmiByIdeFunci")
                .setParameter("IDE_FUNCI", ideFunci)
                .getResultList();
        if (!codOrgaAdmiList.isEmpty()){
            em.createNamedQuery("TvsOrganigramaAdministrativo.consultarElementosByCodOrgList", OrganigramaItemDTO.class)
                    .setParameter("COD_ORG_LIST", codOrgaAdmiList)
                    .getResultList()
                    .stream()
                    .forEach(organigramaItemDTO ->
                        dependenciaDTOList.add(dependenciaDTOTransform(organigramaItemDTO, organigramaAdministrativoControl.consultarPadreDeSegundoNivel(organigramaItemDTO.getIdeOrgaAdmin())))
                    );

        }
        return dependenciaDTOList;
    }
}
