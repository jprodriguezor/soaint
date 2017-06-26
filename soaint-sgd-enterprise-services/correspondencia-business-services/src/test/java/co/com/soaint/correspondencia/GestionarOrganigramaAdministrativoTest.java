package co.com.soaint.correspondencia;

import co.com.soaint.foundation.canonical.correspondencia.OrganigramaItemDTO;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by esanchez on 6/22/2017.
 */
public class GestionarOrganigramaAdministrativoTest extends JPAHibernateTest {
    @Test
    public void consultarElementoRaiz_success() {
        OrganigramaItemDTO raiz = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarElementoRayz", OrganigramaItemDTO.class)
                .getSingleResult();
        assertEquals(null, raiz.getIdOrgaAdminPadre());
    }

    @Test
    public void consultarDescendientesDirectos_success() {
        OrganigramaItemDTO raiz = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarElementoRayz", OrganigramaItemDTO.class)
                .getSingleResult();
        List<OrganigramaItemDTO> descendientesDirectos = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarDescendientesDirectos", OrganigramaItemDTO.class)
                .setParameter("ID_PADRE", String.valueOf(raiz.getIdeOrgaAdmin()))
                .getResultList();
        assertEquals(1, descendientesDirectos.size());
    }
}
