package co.com.soaint.correspondencia.business.control;

import co.com.soaint.foundation.canonical.correspondencia.OrganigramaItemDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 28-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
public class OrganigramaAdministrativoControl {

    @PersistenceContext
    private EntityManager em;

    public void consultarElementosRecursivamente(final List<OrganigramaItemDTO> data, final List<OrganigramaItemDTO> storage) {

        for (OrganigramaItemDTO item : data) {
            List<OrganigramaItemDTO> hijos = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarDescendientesDirectos", OrganigramaItemDTO.class)
                    .setParameter("ID_PADRE", String.valueOf(item.getIdeOrgaAdmin()))
                    .setHint("org.hibernate.cacheable", true)
                    .getResultList();
            storage.addAll(hijos);
            consultarElementosRecursivamente(new ArrayList<>(hijos), storage);
        }

    }

    public OrganigramaItemDTO consultarPadreDeSegundoNivel(BigInteger ideOrgaAdmin) {
        OrganigramaItemDTO organigramaItem = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarElementoByIdeOrgaAdmin", OrganigramaItemDTO.class)
                .setParameter("IDE_ORGA_ADMIN", ideOrgaAdmin)
                .setHint("org.hibernate.cacheable", true)
                .getSingleResult();

        if (organigramaItem.getIdOrgaAdminPadre() == null){
            return null;
        }

        Boolean esPadreSegundoNivel = false;

        while (!esPadreSegundoNivel) {
            OrganigramaItemDTO padre = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarElementoByIdeOrgaAdmin", OrganigramaItemDTO.class)
                    .setParameter("IDE_ORGA_ADMIN", BigInteger.valueOf(Long.parseLong(organigramaItem.getIdOrgaAdminPadre())))
                    .setHint("org.hibernate.cacheable", true)
                    .getSingleResult();
            if (padre.getIdOrgaAdminPadre() == null) {
                esPadreSegundoNivel = true;
            } else {
                organigramaItem = padre;
            }
        }
        return organigramaItem;
    }
}
