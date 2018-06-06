package co.com.soaint.correspondencia.domain.entity;

import co.com.soaint.correspondencia.JPAHibernateContextTest;
import co.com.soaint.foundation.canonical.correspondencia.DepartamentoDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by yleon on 04/04/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"/META-INF/core-config.xml"}
)
@Log4j2
public class TvsDepartamentoTest extends JPAHibernateContextTest {

    @Test
    public void test_findAll_success() {
        //given
        String namedQuery = "TvsDepartamento.findAll";
        String estado = "ACTIVO";
        //when
        List<DepartamentoDTO> departamentos = createNamedQuery(namedQuery, DepartamentoDTO.class)
                .setParameter("ESTADO", estado)
                .getResultList();

        //then
        assertNotNull(departamentos);
        assertEquals(2, departamentos.size());
    }

    @Test
    public void test_findAllByCodPaisAndEstado_success() {
        //given
        String namedQuery = "TvsDepartamento.findAllByCodPaisAndEstado";
        String pais = "CUBA";
        String estado = "ACTIVO";

        //when
        List<DepartamentoDTO> departamentos = createNamedQuery(namedQuery, DepartamentoDTO.class)
                .setParameter("COD_PAIS", pais)
                .setParameter("ESTADO", estado)
                .getResultList();

        //then
        assertNotNull(departamentos);
        assertEquals(1, departamentos.size());
    }


    @Test
    public void test_existeDepartamentoByCodDep_success() {
        //given
        String namedQuery = "TvsDepartamento.existeDepartamentoByCodDep";
        String codigo = "CODIGODEPTO1";
        //when
        DepartamentoDTO departamento = createNamedQuery(namedQuery, DepartamentoDTO.class)
                .setParameter("COD_DEP", codigo)
                .getSingleResult();

        //then
        assertNotNull(departamento);
        assertEquals(codigo, departamento.getCodDepar());
    }

    @Test
    public void test_findByCodMunic_success() {
        //given
        String namedQuery = "TvsDepartamento.findByCodMunic";
        String codigo = "CODIGO1";
        //when
        DepartamentoDTO departamento = createNamedQuery(namedQuery, DepartamentoDTO.class)
                .setParameter("COD_MUNIC", codigo)
                .getSingleResult();

        //then
        assertNotNull(departamento);
    }

    @Test
    public void test_findByCodDep_success() {
        //given
        String namedQuery = "TvsDepartamento.findByCodDep";
        String codigo = "CODIGODEPTO1";
        //when
        DepartamentoDTO departamento = createNamedQuery(namedQuery, DepartamentoDTO.class)
                .setParameter("COD_DEP", codigo)
                .getSingleResult();

        //then
        assertNotNull(departamento);
        assertEquals(codigo, departamento.getCodDepar());
    }
}
