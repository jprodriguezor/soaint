package co.com.soaint.correspondencia.domain.entity;

import co.com.soaint.correspondencia.JPAHibernateContextTest;
import co.com.soaint.foundation.canonical.correspondencia.DatosContactoDTO;
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
public class TvsDatosContactoTest extends JPAHibernateContextTest {

    @Test
    public void test_findAll_success() {
        //given
        String namedQuery = "TvsDatosContacto.findAll";
        //when
        List<TvsDatosContacto> datosContactos = createNamedQuery(namedQuery, TvsDatosContacto.class)
                .getResultList();

        //then
        assertNotNull(datosContactos);
        assertEquals(2, datosContactos.size());
    }

    @Test
    public void test_findByIdeAgente_success() {
        //given
        String namedQuery = "TvsDatosContacto.findByIdeAgente";
        BigInteger idAgente = BigInteger.valueOf(100) ;

        //when
        List<DatosContactoDTO> datosContactos = createNamedQuery(namedQuery, DatosContactoDTO.class)
                .setParameter("IDE_AGENTE", idAgente)
                .getResultList();
        //then
        assertNotNull(datosContactos);
        assertEquals(2, datosContactos.size());
    }
}
