package co.com.soaint.correspondencia.domain.entity;

import co.com.soaint.correspondencia.JPAHibernateContextTest;
import co.com.soaint.foundation.canonical.correspondencia.PpdDocumentoDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by yleon on 03/05/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"/META-INF/core-config.xml"}
)
@Log4j2
public class PpdDocumentoTest extends JPAHibernateContextTest {

    @Test
    public void test_findAll_success() {
        //given
        String namedQuery = "PpdDocumento.findAll";
        //when
        List<PpdDocumento> ppdDocumentos = createNamedQuery(namedQuery, PpdDocumento.class)
                .getResultList();

        //then
        assertNotNull(ppdDocumentos);
        assertEquals(2, ppdDocumentos.size());
    }

    @Test
    public void test_findByIdeDocumento_success() {
        //given
        String namedQuery = "PpdDocumento.findByIdeDocumento";
        BigInteger idDocumento = BigInteger.valueOf(836) ;

        //when
        List<PpdDocumentoDTO> ppdDocumentos = createNamedQuery(namedQuery, PpdDocumentoDTO.class)
                .setParameter("IDE_DOCUMENTO", idDocumento)
                .getResultList();
        //then
        assertNotNull(ppdDocumentos);
        assertEquals(1, ppdDocumentos.size());
    }

    @Test
    public void test_findIdePpdDocumentoByIdeDocumento_success() {
        //given
        String namedQuery = "PpdDocumento.findIdePpdDocumentoByIdeDocumento";
        BigInteger idDocumento = BigInteger.valueOf(836) ;

        //when
        BigInteger id = createNamedQuery(namedQuery, BigInteger.class)
                .setParameter("IDE_DOCUMENTO", idDocumento)
                .getSingleResult();
        //then
        assertNotNull(id);
        assertEquals(BigInteger.valueOf(100), id);
    }

    @Test
    public void test_findIdePpdDocumentoByNroRadicado_success() {
        //given
        String namedQuery = "PpdDocumento.findIdePpdDocumentoByNroRadicado";
        String numRadicado = "1040TP-CMCOE2017000001" ;

        //when
        BigInteger id = createNamedQuery(namedQuery, BigInteger.class)
                .setParameter("NRO_RADICADO", numRadicado)
                .getSingleResult();
        //then
        assertNotNull(id);
        assertEquals(BigInteger.valueOf(100), id);
    }

    @Test
    public void test_findPpdDocumentoByNroRadicado_success() {
        //given
        String namedQuery = "PpdDocumento.findPpdDocumentoByNroRadicado";
        String numRadicado = "1040TP-CMCOE2017000001" ;

        //when
        List<PpdDocumentoDTO> ppdDocumentos = createNamedQuery(namedQuery, PpdDocumentoDTO.class)
                .setParameter("NRO_RADICADO", numRadicado)
                .getResultList();
        //then
        assertNotNull(ppdDocumentos);
        assertEquals(1, ppdDocumentos.size());
    }

    @Test
    public void test_updateIdEcm_success() {
        //given
        String namedQueryUpdate = "PpdDocumento.updateIdEcm";
        String namedQueryFindAll = "PpdDocumento.findAll";
        BigInteger idePpdDocumento = BigInteger.valueOf(100);
        String ideEcm = "newIDecm";

        //when
        createNamedQuery(namedQueryUpdate)
                .setParameter("IDE_ECM", ideEcm)
                .setParameter("IDE_PPDDOCUMENTO", idePpdDocumento)
                .executeUpdate();

        List<PpdDocumento> ppdDocumentos = createNamedQuery(namedQueryFindAll, PpdDocumento.class)
                .getResultList();

        //then
        assertNotNull(ppdDocumentos);
        for (PpdDocumento doc:
             ppdDocumentos) {
            if(doc.getIdePpdDocumento().equals(idePpdDocumento))
                assertEquals(ideEcm, doc.getIdeEcm());
        }
    }
}
