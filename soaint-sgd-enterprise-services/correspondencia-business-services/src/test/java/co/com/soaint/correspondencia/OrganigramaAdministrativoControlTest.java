package co.com.soaint.correspondencia;

import co.com.soaint.foundation.canonical.correspondencia.OrganigramaItemDTO;
import co.com.soaint.correspondencia.business.control.OrganigramaAdministrativoControl;
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
import java.util.List;

import static org.junit.Assert.*;

import static org.junit.Assert.*;

/**
 * Created by gyanet on 04/04/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"/META-INF/core-config.xml"}
)
@Log4j2
public class OrganigramaAdministrativoControlTest {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private OrganigramaAdministrativoControl organigramaAdministrativoControl;

    @Test
    public void listarElementoRayz() throws SystemException, BusinessException {
        //when
        OrganigramaItemDTO raiz = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarElementoRayz", OrganigramaItemDTO.class)
                .getSingleResult();
        //then
        assertNotNull(raiz);
        assertEquals("10120141", raiz.getCodOrg());
    }

    @Test
    public void listarDescendientesDirectosDeElementoRayz() throws SystemException, BusinessException {
        //when
        List<OrganigramaItemDTO> itemDTOList = organigramaAdministrativoControl.listarDescendientesDirectosDeElementoRayz();

        //then
        assertNotNull(itemDTOList);
        assertEquals(1, itemDTOList.size());
    }

    @Test
    public void consultarElementosRecursivamente() {
    }

    @Test
    public void consultarPadreDeSegundoNivel() {
    }

    @Test
    public void consultarElementosDeNivelInferior() {
    }

    @Test
    public void consultarElementoByCodOrg() {
    }

    @Test
    public void consultarNombreElementoByCodOrg() {
    }

    @Test
    public void consultarNombreFuncionarioByCodOrg() {
    }

    @Test
    public void consultarElementosByCodOrg() {
    }
}