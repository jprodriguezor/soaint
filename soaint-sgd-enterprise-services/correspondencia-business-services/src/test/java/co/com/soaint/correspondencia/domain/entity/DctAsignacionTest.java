package co.com.soaint.correspondencia.domain.entity;

import co.com.soaint.correspondencia.JPAHibernateContextTest;
import co.com.soaint.foundation.canonical.correspondencia.DctAsignacionDTO;
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
public class DctAsignacionTest extends JPAHibernateContextTest {

    @Test
    public void test_findAll_success() {
        //given
        String namedQuery = "DctAsignacion.findAll";
        //when
        List<DctAsignacion> dctAsigs = createNamedQuery(namedQuery, DctAsignacion.class)
                .getResultList();

        //then
        assertNotNull(dctAsigs);
        assertEquals(2, dctAsigs.size());
    }

    @Test
    public void test_findByIdeAsignacion_success() {
        //given
        String namedQuery = "DctAsignacion.findByIdeAsignacion";
        BigInteger idAsignacion = BigInteger.valueOf(100);
        BigInteger id = BigInteger.valueOf(100);

        //when
        DctAsignacionDTO dctAsignacion = createNamedQuery(namedQuery, DctAsignacionDTO.class)
                .setParameter("IDE_ASIGNACION", idAsignacion)
                .getSingleResult();
        //then
        assertNotNull(dctAsignacion);
        assertEquals(id, dctAsignacion.getIdeAsignacion());
    }

    @Test
    public void test_findByIdeAgente_success() {
        //given
        String namedQuery = "DctAsignacion.findByIdeAgente";
        BigInteger idAgente = BigInteger.valueOf(100);
        BigInteger id = BigInteger.valueOf(100);

        //when
        DctAsignacionDTO dctAsignacion = createNamedQuery(namedQuery, DctAsignacionDTO.class)
                .setParameter("IDE_AGENTE", idAgente)
                .getSingleResult();
        //then
        assertNotNull(dctAsignacion);
        assertEquals(id, dctAsignacion.getIdeAsignacion());
    }

    @Test
    public void test_findByIdeAsigUltimo_success() {
        //given
        String namedQuery = "DctAsignacion.findByIdeAsigUltimo";
        BigInteger idAsignacion = BigInteger.valueOf(100);
        BigInteger id = BigInteger.valueOf(100);

        //when
        DctAsignacion dctAsignacion = createNamedQuery(namedQuery, DctAsignacion.class)
                .setParameter("IDE_ASIG_ULTIMO", idAsignacion)
                .getSingleResult();
        //then
        assertNotNull(dctAsignacion);
        assertEquals(id, dctAsignacion.getIdeAsignacion());
    }

    @Test
    public void test_asignarToFuncionario_success() {
        //given
        String namedQueryUpdate = "DctAsignacion.asignarToFuncionario";
        String namedQueryFindAll = "DctAsignacion.findAll";
        BigInteger idAsignacion = BigInteger.valueOf(100);
        BigInteger ideFunci = BigInteger.valueOf(100);

        //when
        createNamedQuery(namedQueryUpdate)
                .setParameter("IDE_FUNCI", ideFunci)
                .setParameter("IDE_ASIGNACION", idAsignacion)
                .executeUpdate();

        List<DctAsignacion> dctAsigs = createNamedQuery(namedQueryFindAll, DctAsignacion.class)
                .getResultList();

        //then
        assertNotNull(dctAsigs);
        for (DctAsignacion doc :
                dctAsigs) {
            if (doc.getIdeAsignacion().equals(idAsignacion))
                assertEquals(ideFunci, doc.getIdeFunci());
        }
    }
}
