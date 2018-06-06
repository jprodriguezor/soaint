package co.com.soaint.correspondencia.domain.entity;

import co.com.soaint.correspondencia.JPAHibernateContextTest;
import co.com.soaint.foundation.canonical.correspondencia.ReferidoDTO;
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
 * Created by yleon on 07/05/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"/META-INF/core-config.xml"}
)
@Log4j2
public class CorReferidoTest extends JPAHibernateContextTest {

    @Test
    public void test_findAll_success() {
        //given
        String namedQuery = "CorReferido.findAll";
        //when
        List<CorReferido> corReferidos = createNamedQuery(namedQuery, CorReferido.class)
                .getResultList();

        //then
        assertNotNull(corReferidos);
        assertEquals(2, corReferidos.size());
    }

    @Test
    public void test_findByIdeDocumento_success() {
        //given
        String namedQuery = "CorReferido.findByIdeDocumento";
        BigInteger idDocumento = BigInteger.valueOf(836);

        //when
        List<ReferidoDTO> referidos = createNamedQuery(namedQuery, ReferidoDTO.class)
                .setParameter("IDE_DOCUMENTO", idDocumento)
                .getResultList();
        //then
        assertNotNull(referidos);
        assertEquals(1, referidos.size());
    }
}
