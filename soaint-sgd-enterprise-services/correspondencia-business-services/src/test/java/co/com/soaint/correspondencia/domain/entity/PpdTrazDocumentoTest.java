package co.com.soaint.correspondencia.domain.entity;

import co.com.soaint.correspondencia.JPAHibernateContextTest;
import co.com.soaint.foundation.canonical.correspondencia.PpdTrazDocumentoDTO;
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
public class PpdTrazDocumentoTest extends JPAHibernateContextTest {

    @Test
    public void test_findAll_success() {
        //given
        String namedQuery = "PpdTrazDocumento.findAll";
        //when
        List<PpdTrazDocumento> ppdTrazDocumentos = createNamedQuery(namedQuery, PpdTrazDocumento.class)
                .getResultList();

        //then
        assertNotNull(ppdTrazDocumentos);
        assertEquals(2, ppdTrazDocumentos.size());
    }

    @Test
    public void test_findAllByIdeDocumento_success() {
        //given
        String namedQuery = "PpdTrazDocumento.findAllByIdeDocumento";
        BigInteger idDocumento = BigInteger.valueOf(836) ;

        //when
        List<PpdTrazDocumentoDTO> ppdTrazDocumentos = createNamedQuery(namedQuery, PpdTrazDocumentoDTO.class)
                .setParameter("IDE_DOCUMENTO", idDocumento)
                .getResultList();
        //then
        assertNotNull(ppdTrazDocumentos);
        assertEquals(1, ppdTrazDocumentos.size());
    }
}
