package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.JPAHibernateTest;
import co.com.soaint.foundation.canonical.correspondencia.OrganigramaItemDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by gyanet on 28/03/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/META-INF/core-config.xml"})
@Log4j2
public class GestionarOrganigramaAdministrativoTest extends JPAHibernateTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private  GestionarOrganigramaAdministrativo gestionarOrganigramaAdministrativo;

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

    @Test
    public void test_consultarOrganigrama_success() throws SystemException, BusinessException{
        List<OrganigramaItemDTO> raiz = gestionarOrganigramaAdministrativo.consultarOrganigrama();
        assertEquals("1", raiz.get(0).getIdOrgaAdminPadre());
    }

    @Test
    public void test_listarElementosDeNivelInferior_success() throws SystemException, BusinessException{
        //given
        BigInteger id = new BigInteger("1");
        //when
        List<OrganigramaItemDTO> raiz = gestionarOrganigramaAdministrativo.listarElementosDeNivelInferior(id);
        //then
        assertEquals("1", raiz.get(0).getIdOrgaAdminPadre());
    }

    @Test
    public void test_consultarPadreDeSegundoNivel_failure() throws SystemException, BusinessException{
        //given
        BigInteger id = new BigInteger("1");
        //when
        try{
            OrganigramaItemDTO organigramaItemDTO = gestionarOrganigramaAdministrativo.consultarPadreDeSegundoNivel(id);
            //then
        } catch (Exception e){
            assertNotNull(e);
            log.error("GestionarOrganigramaTest - no existe padre de segundo nivel", e);
        }

    }
}
