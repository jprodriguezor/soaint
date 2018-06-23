package co.com.soaint.correspondencia.domain.entity;


import co.com.soaint.correspondencia.JPAHibernateContextTest;
import co.com.soaint.foundation.canonical.correspondencia.PaisDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by gyanet on 04/04/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"/META-INF/core-config.xml"}
)
@Log4j2
public class TvsPaisTest extends JPAHibernateContextTest {

    @Test
    public void test_TvsPaisfindAll_success() {

        //given
        String namedQuery = "TvsPais.findAll";
        String estado = "ACTIVO";


        //when
        List<PaisDTO> paislist = createNamedQuery(namedQuery, PaisDTO.class)
                .setParameter("ESTADO", estado)
                .getResultList();
        int size = paislist.size();

        //then
        assertNotNull(paislist);
        assertEquals(2, size);
        assertTrue(paislist.size() > 0);
        //paislist.forEach(paisDTO -> assertEquals(paisNombre,paisDTO.getNombre()));

    }

    @Test
    public void test_TvsPaisfindByNombrePaisAndEstado_success() {

        //given
        String namedQuery = "TvsPais.findByNombrePaisAndEstado";
        String paisNombre = "CUBA";
        String estado = "ACTIVO";

        //when
        List<PaisDTO> paislist = createNamedQuery(namedQuery, PaisDTO.class)
                .setParameter("ESTADO", estado)
                .setParameter("NOMBRE_PAIS", paisNombre)
                .getResultList();
        int size = paislist.size();

        //then
        assertNotNull(paislist);
        //assertEquals(1,size);
        assertTrue(paislist.size() > 0);

    }

   /* @Test
    public void test_TvsPaisfindByCod_success() {

       //given
       String namedQuery = "TvsPais.findByCod";
       String codPais = "CUBA";

        //when
       PaisDTO pais = createNamedQuery(namedQuery, PaisDTO.class)
                .setParameter("COD_PAIS", codPais)
                .getSingleResult();
        //then
        assertNotNull(pais);
       assertEquals(codPais, pais.getCodigo());

   }*/

}
