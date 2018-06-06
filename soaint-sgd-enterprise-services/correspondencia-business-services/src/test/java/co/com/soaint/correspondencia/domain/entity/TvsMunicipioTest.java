package co.com.soaint.correspondencia.domain.entity;

import co.com.soaint.correspondencia.JPAHibernateContextTest;
import co.com.soaint.foundation.canonical.correspondencia.MunicipioDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by yleon on 04/04/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"/META-INF/core-config.xml"}
)
@Log4j2
public class TvsMunicipioTest extends JPAHibernateContextTest {

    @Test
    public void test_findAll_success() {
        //given
        String namedQuery = "TvsMunicipio.findAll";
        String estado = "ACTIVO";
        //when
        List<MunicipioDTO> municipios = createNamedQuery(namedQuery, MunicipioDTO.class)
                .setParameter("ESTADO", estado)
                .getResultList();

        //then
        assertNotNull(municipios);
        assertEquals(2, municipios.size());
    }

    @Test
    public void test_findAllByCodDeparAndEstado_success() {
        //given
        String namedQuery = "TvsMunicipio.findAllByCodDeparAndEstado";
        String departamento = "CODIGODEPTO1";
        String estado = "ACTIVO";

        //when
        List<MunicipioDTO> municipios  = createNamedQuery(namedQuery, MunicipioDTO.class)
                .setParameter("COD_DEPAR", departamento)
                .setParameter("ESTADO", estado)
                .getResultList();

        //then
        assertNotNull(municipios);
        assertEquals(1, municipios.size());
    }


    @Test
    public void test_findAllByCodigos_success() {
        //given
        String namedQuery = "TvsMunicipio.findAllByCodigos";
        List<String> codigos = new ArrayList<>();
        codigos.add("CODIGO1");
        //when
        List<MunicipioDTO> municipios = createNamedQuery(namedQuery,MunicipioDTO.class)
                .setParameter("CODIGOS", codigos)
                .getResultList();

        //then
        assertNotNull(municipios);
        assertTrue(municipios.size() > 0);
    }

    @Test
    public void test_findByCodMun_success() {
        //given
        String namedQuery = "TvsMunicipio.findByCodMun";
        String codigo = "CODIGO1";
        //when
        MunicipioDTO municipio = createNamedQuery(namedQuery,MunicipioDTO.class)
                .setParameter("COD_MUN", codigo)
                .getSingleResult();

        //then
        assertNotNull(municipio);
        assertEquals("CODIGO1", municipio.getCodMunic());
    }
}
