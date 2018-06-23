package co.com.soaint.correspondencia.business.control;

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
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
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
        assertEquals("1", raiz.getCodOrg());
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
        //given
        List<OrganigramaItemDTO> itemDTOList = new ArrayList<>();
        itemDTOList.add(OrganigramaItemDTO.newInstance().ideOrgaAdmin(new BigInteger("1")).build());
        itemDTOList.add(OrganigramaItemDTO.newInstance().ideOrgaAdmin(new BigInteger("2")).build());
        itemDTOList.add(OrganigramaItemDTO.newInstance().ideOrgaAdmin(new BigInteger("3")).build());
        itemDTOList.add(OrganigramaItemDTO.newInstance().ideOrgaAdmin(new BigInteger("4")).build());

        //when
        List<OrganigramaItemDTO> organigramaItemDTOS = new ArrayList<>();
        organigramaAdministrativoControl.consultarElementosRecursivamente(itemDTOList,organigramaItemDTOS);

        //then
        assertNotNull(organigramaItemDTOS);
        assertEquals(6, organigramaItemDTOS.size());
        assertThat("10120141", is(organigramaItemDTOS.get(0).getCodOrg()));
        assertThat("1012014", is(organigramaItemDTOS.get(1).getCodOrg()));
        assertThat("10120142", is(organigramaItemDTOS.get(2).getCodOrg()));
        assertThat("10120142", is(organigramaItemDTOS.get(3).getCodOrg()));
        assertThat("1012014", is(organigramaItemDTOS.get(4).getCodOrg()));
        assertThat("10120142", is(organigramaItemDTOS.get(5).getCodOrg()));
    }

    @Test
    public void consultarPadreDeSegundoNivel() {
        //given
        BigInteger ideorgRaiz = new BigInteger("1");
        BigInteger ideorg2nivel = new BigInteger("3");
        BigInteger ideorg3nivel = new BigInteger("2");

        //when
        OrganigramaItemDTO orgRaiz = organigramaAdministrativoControl.consultarPadreDeSegundoNivel(ideorgRaiz);
        OrganigramaItemDTO org2Nivel = organigramaAdministrativoControl.consultarPadreDeSegundoNivel(ideorg2nivel);
        OrganigramaItemDTO org3Nivel = organigramaAdministrativoControl.consultarPadreDeSegundoNivel(ideorg3nivel);

        //then
        assertNull(orgRaiz);
        assertNotNull(org2Nivel);
        assertThat("10120141", is(org2Nivel.getCodOrg()));
        assertNotNull(org3Nivel);
        assertThat("10120141", is(org3Nivel.getCodOrg()));

    }

    @Test
    public void consultarElementosDeNivelInferior() {

        BigInteger ideorgRaiz = new BigInteger("1");
        BigInteger ideorg2nivel = new BigInteger("3");
        BigInteger ideorg3nivel = new BigInteger("2");

        //when
        List<OrganigramaItemDTO> orgRaiz =  organigramaAdministrativoControl.consultarElementosDeNivelInferior(ideorgRaiz);
        List<OrganigramaItemDTO> org2Nivel = organigramaAdministrativoControl.consultarElementosDeNivelInferior(ideorg2nivel);
        List<OrganigramaItemDTO> org3Nivel = organigramaAdministrativoControl.consultarElementosDeNivelInferior(ideorg3nivel);

        //then
        assertNotNull(orgRaiz);
        assertEquals(3, orgRaiz.size());
        assertThat("10120141", is(orgRaiz.get(0).getCodOrg()));
        assertThat("1012014", is(orgRaiz.get(1).getCodOrg()));
        assertThat("10120142", is(orgRaiz.get(2).getCodOrg()));
        assertNotNull(org2Nivel);
        assertEquals(2, org2Nivel.size());
        assertThat("1012014", is(org2Nivel.get(0).getCodOrg()));
        assertThat("10120142", is(org2Nivel.get(1).getCodOrg()));
        assertNotNull(org3Nivel);
        assertEquals(1,org3Nivel.size());
        assertThat("10120142", is(org3Nivel.get(0).getCodOrg()));
    }

    @Test
    public void consultarElementoByCodOrg() {
        //given
        String codOrg = "10120141";

        //when
        OrganigramaItemDTO organigramaItemDTO = organigramaAdministrativoControl.consultarElementoByCodOrg(codOrg);

        //then
        assertEquals("GIT GES JURIDICA", organigramaItemDTO.getNomOrg());
        assertEquals("GRUPO INTERNO DE TRABAJO DE GESTION JURIDICA", organigramaItemDTO.getDescOrg());
    }

    @Test
    public void consultarNombreElementoByCodOrg() {
        //given
        String codOrg = "10120141";
        String nomOrg = "GIT GES JURIDICA";

        //when
        String  nombreElementoByCodOrg = organigramaAdministrativoControl.consultarNombreElementoByCodOrg(codOrg);

        //then
        assertEquals(nomOrg, nombreElementoByCodOrg);
    }

    @Test
    public void consultarNombreFuncionarioByCodOrg() throws SystemException {
        //given
        String codOrg = "10120141";
        String nomFunc = "NOM_FUNCIONARIO1";

        //when
        List<String>  nombres = organigramaAdministrativoControl.consultarNombreFuncionarioByCodOrg(codOrg);

        //then
        assertNotNull(nombres);
        assertTrue(nombres.size()== 1);
        assertThat(nomFunc, isIn(nombres));
    }

    @Test
    public void consultarElementosByCodOrg() {
        //given
        String[] codsOrg = {"10120141","10120142"};

        //when
        List<OrganigramaItemDTO> orgItems = organigramaAdministrativoControl.consultarElementosByCodOrg(codsOrg);

        //then
        assertNotNull(orgItems);
        assertEquals(orgItems.size(), 2);
        assertThat(codsOrg[0].compareTo(orgItems.get(0).getCodOrg()),equalTo(0));
        assertThat(codsOrg[1].compareTo(orgItems.get(1).getCodOrg()),equalTo(0));
    }
}