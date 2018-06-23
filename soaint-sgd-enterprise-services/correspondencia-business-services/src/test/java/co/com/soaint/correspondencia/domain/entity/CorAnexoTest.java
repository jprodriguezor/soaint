package co.com.soaint.correspondencia.domain.entity;

import co.com.soaint.correspondencia.JPAHibernateContextTest;
import co.com.soaint.foundation.canonical.correspondencia.AnexoDTO;
import co.com.soaint.foundation.canonical.correspondencia.AnexoFullDTO;
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
public class CorAnexoTest extends JPAHibernateContextTest {

    @Test
    public void test_findAll_success() {
        //given
        String namedQuery = "CorAnexo.findAll";
        //when
        List<CorAnexo> corAnexos = createNamedQuery(namedQuery, CorAnexo.class)
                .getResultList();

        //then
        assertNotNull(corAnexos);
        assertEquals(2, corAnexos.size());
    }

    @Test
    public void test_findByIdePpdDocumento_success() {
        //given
        String namedQuery = "CorAnexo.findByIdePpdDocumento";
        BigInteger idePpdDocumento = BigInteger.valueOf(100);

        //when
        List<AnexoDTO> anexos = createNamedQuery(namedQuery, AnexoDTO.class)
                .setParameter("IDE_PPD_DOCUMENTO", idePpdDocumento)
                .getResultList();
        //then
        assertNotNull(anexos);
        assertEquals(1, anexos.size());    }

    @Test
    public void test_findAnexosByNroRadicado_success() {
        //given
        String namedQuery = "CorAnexo.findAnexosByNroRadicado";
        String nroRadicado = "1040TP-CMCOE2017000001";

        //when
        List<AnexoFullDTO> anexos = createNamedQuery(namedQuery, AnexoFullDTO.class)
                .setParameter("NRO_RADICADO", nroRadicado)
                .getResultList();
        //then
        assertNotNull(anexos);
        assertEquals(1, anexos.size());    }
}
