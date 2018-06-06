package co.com.soaint.correspondencia.domain.entity;

import co.com.soaint.correspondencia.JPAHibernateContextTest;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by gyanet on 04/04/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"/META-INF/core-config.xml"}
)
@Log4j2
public class TvsDserialTest extends JPAHibernateContextTest {


    @Test
    public void test_TvsDserialconsultarConsecutivoRadicado_success() {
        //given
        String namedQuery = "TvsDserial.consultarConsecutivoRadicado";
        String codigoSede = "1000";
        String codigoCmc = "EE";
        String anno = "2018";
        String concecutivoI = "1000";
        String concecutivoF = "9999";
        String max = "000019";

        //when
        List<String> Dserial = createNamedQuery(namedQuery, String.class)
                .setParameter("COD_SEDE", codigoSede)
                .setParameter("COD_CMC", codigoCmc)
                .setParameter("ANNO", anno)
                .setParameter("RESERVADO_I", concecutivoI)
                .setParameter("RESERVADO_F", concecutivoF)
                .getResultList();

        //then
        assertNotNull(Dserial);
        assertEquals(max, Dserial.get(0));

    }

    @Test
    public void test_TvsDserialfindAll_success() {
        //given
        String namedQuery = "TvsDserial.findAll";

        //when
        List<TvsDserial> Dserial = createNamedQuery(namedQuery, TvsDserial.class)
                .getResultList();

        //then
        assertNotNull(Dserial);
        assertEquals(4, Dserial.size());
    }


}

