package co.com.soaint.correspondencia.domain.entity;


import co.com.soaint.correspondencia.JPAHibernateContextTest;
import co.com.soaint.foundation.canonical.correspondencia.ConstanteDTO;
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
public class TvsConstantesTest extends JPAHibernateContextTest {

    @Test
    public void test_TvsConstantesfindAll_success() {

        //given
        String namedQuery = "TvsConstantes.findAll";
        String estado = "INACTIVO";


        //when
        List<ConstanteDTO> constanteslist = createNamedQuery(namedQuery, ConstanteDTO.class)
                .setParameter("ESTADO", estado)
                .getResultList();

        int size = constanteslist.size();

        //then
        assertNotNull(constanteslist);
        assertTrue(constanteslist.size() > 0);


    }
    @Test
    public void test_TvsConstantesfindAllByCodigoAndEstado_success() {

        //given
        String namedQuery = "TvsConstantes.findAllByCodigoAndEstado";
        String estado = "INACTIVO";
        String codigo = "CODIGO2";



        //when
        List<ConstanteDTO> constanteslist = createNamedQuery(namedQuery, ConstanteDTO.class)
                .setParameter("ESTADO", estado)
                .setParameter("CODIGO", codigo)
                .getResultList();

        //then
        assertNotNull(constanteslist);
        assertTrue(constanteslist.size() > 0);
    }
    @Test
    public void test_TvsConstantesfindAllByCodPadreAndEstado_success() {

        //given
        String namedQuery = "TvsConstantes.findAllByCodPadreAndEstado";
        String estado = "INACTIVO";
        String codigopadre = "CODIGOPADRE1";

        //when
        List<ConstanteDTO> constanteslist = createNamedQuery(namedQuery, ConstanteDTO.class)
                .setParameter("ESTADO", estado)
                .setParameter("COD_PADRE", codigopadre)
                .getResultList();

        //then
        assertNotNull(constanteslist);
        assertTrue(constanteslist.size() > 0);
    }

    @Test
    public void test_TvsConstantesfindAllByCodigo_success() {

        //given
        String namedQuery = "TvsConstantes.findAllByCodigo";
        String codigo = "CODIGO2";


        //when
        List<ConstanteDTO> constanteslist = createNamedQuery(namedQuery, ConstanteDTO.class)
                .setParameter("CODIGOS",codigo)
                .getResultList();

        //then
        assertNotNull(constanteslist);
       // assertTrue(constanteslist.size() > 0);
        assertEquals(1, constanteslist.size());
    }

    @Test
    public void test_TvsConstantesfindByCodigo_success() {

        //given
        String namedQuery = "TvsConstantes.findByCodigo";
        String codigo = "CODIGO2";
        //when
        ConstanteDTO constantes = createNamedQuery(namedQuery, ConstanteDTO.class)
                .setParameter("CODIGO",codigo)
                .getSingleResult();
        //then
        assertNotNull(constantes);
        // assertTrue(constanteslist.size() > 0);
        assertEquals(constantes.getCodigo(),codigo);
    }
    @Test
    public void test_TvsConstantesgetNombreByCodigo_success() {

        //given
        String namedQuery = "TvsConstantes.getNombreByCodigo";
        String codigo = "CODIGO1";
        String nombre="NOMBRE1";
        //when
        String constantes = createNamedQuery(namedQuery, String.class)
                .setParameter("CODIGO",codigo)
                .getSingleResult();
        //then
        assertNotNull(constantes);
        // assertTrue(constanteslist.size() > 0);
        assertEquals(constantes,nombre);
    }

}
