package co.com.soaint.correspondencia.domain.entity;

import co.com.soaint.correspondencia.JPAHibernateContextTest;
import co.com.soaint.foundation.canonical.correspondencia.AgenteDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigInteger;
import java.util.Date;
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
public class CorAgenteTest extends JPAHibernateContextTest {

    @Test
    public void test_CorAgentefindAll_success() {
        //given
        String namedQuery = "CorAgente.findAll";

        //when
        List<CorAgente> agentes = createNamedQuery(namedQuery, CorAgente.class)
                .getResultList();

        //then
        assertNotNull(agentes);
        assertEquals(2, agentes.size());
    }

    @Test
    public void test_CorAgentefindByIdeDocumento_success() {
        //given
        String namedQuery = "CorAgente.findByIdeDocumento";
        BigInteger IDE_DOCUMENTO = new BigInteger("836");

        //when
        AgenteDTO agentes = createNamedQuery(namedQuery, AgenteDTO.class)
                .setParameter("IDE_DOCUMENTO", IDE_DOCUMENTO)
                .getSingleResult();

        //then
        assertNotNull(agentes);
        assertEquals(IDE_DOCUMENTO,new BigInteger("836"));
    }

    @Test
    public void test_CorAgentecountByIdeAgente_success() {
        //given
        String namedQuery = "CorAgente.countByIdeAgente";
        BigInteger IDE_AGENTE = new BigInteger("100");

        //when
        Long agentes = createNamedQuery(namedQuery, Long.class)
                .setParameter("IDE_AGENTE", IDE_AGENTE)
                .getSingleResult();


        //then
        assertNotNull(agentes);
        assertEquals(new Long("1"),agentes);
    }

    @Test
    public void test_CorAgenteupdateAsignacion_success() {
        //given
        String namedQueryUpdate = "CorAgente.updateAsignacion";
        String namedQueryFindAll = "CorAgente.findAll";
        Date FECHA_ASIGNACION = new Date();
        String COD_ESTADO = "AS";
        BigInteger IDE_AGENTE = new BigInteger("100");

        //when
        createNamedQuery(namedQueryUpdate)
                .setParameter("FECHA_ASIGNACION", FECHA_ASIGNACION)
                .setParameter("COD_ESTADO", COD_ESTADO)
                .setParameter("IDE_AGENTE",IDE_AGENTE)
                .executeUpdate();

        List<CorAgente> agentes = createNamedQuery(namedQueryFindAll, CorAgente.class)
                .getResultList();

        //then
        assertNotNull(agentes);
        for (CorAgente agt :
                agentes) {
            if (agt.getIdeAgente().equals(IDE_AGENTE))
                assertEquals(COD_ESTADO, agt.getCodEstado());
        }
    }
    @Test
    public void test_CorAgenteredireccionarCorrespondencia_success() {
        //given
        String namedQueryUpdate = "CorAgente.redireccionarCorrespondencia";
        String namedQueryFindAll = "CorAgente.findAll";
        String COD_SEDE = "CS";
        String COD_DEPENDENCIA = "CD";
        String COD_ESTADO = "AS";
        String ESTADO_DISTRIBUCION = "DIST";
        BigInteger IDE_AGENTE = new BigInteger("100");

        //when
        createNamedQuery(namedQueryUpdate)
                .setParameter("COD_SEDE", COD_SEDE)
                .setParameter("COD_DEPENDENCIA", COD_DEPENDENCIA)
                .setParameter("COD_ESTADO", COD_ESTADO)
                .setParameter("ESTADO_DISTRIBUCION", ESTADO_DISTRIBUCION)
                .setParameter("IDE_AGENTE",IDE_AGENTE)
                .executeUpdate();

        List<CorAgente> agentes = createNamedQuery(namedQueryFindAll, CorAgente.class)
                .getResultList();

        //then
        assertNotNull(agentes);
        for (CorAgente agt :
                agentes) {
            if (agt.getIdeAgente().equals(IDE_AGENTE))
                assertEquals(ESTADO_DISTRIBUCION, agt.getEstadoDistribucion());
        }
    }

    @Test
    public void test_CorAgenteupdateEstado_success() {
        //given
        String namedQueryUpdate = "CorAgente.updateEstado";
        String namedQueryFindAll = "CorAgente.findAll";
        String COD_ESTADO = "AS";
        BigInteger IDE_AGENTE = new BigInteger("100");

        //when
        createNamedQuery(namedQueryUpdate)
                .setParameter("COD_ESTADO", COD_ESTADO)
                .setParameter("IDE_AGENTE",IDE_AGENTE)
                .executeUpdate();

        List<CorAgente> agentes = createNamedQuery(namedQueryFindAll, CorAgente.class)
                .getResultList();

        //then
        assertNotNull(agentes);
        for (CorAgente agt :
                agentes) {
            if (agt.getIdeAgente().equals(IDE_AGENTE))
                assertEquals(COD_ESTADO, agt.getCodEstado());
        }
    }
    @Test
    public void test_CorAgenteupdateEstadoDistribucion_success() {
        //given
        String namedQueryUpdate = "CorAgente.updateEstadoDistribucion";
        String namedQueryFindAll = "CorAgente.findAll";
        String ESTADO_DISTRIBUCION = "DIST";
        BigInteger IDE_AGENTE = new BigInteger("100");

        //when
        createNamedQuery(namedQueryUpdate)
                .setParameter("ESTADO_DISTRIBUCION", ESTADO_DISTRIBUCION)
                .setParameter("IDE_AGENTE",IDE_AGENTE)
                .executeUpdate();

        List<CorAgente> agentes = createNamedQuery(namedQueryFindAll, CorAgente.class)
                .getResultList();

        //then
        assertNotNull(agentes);
        for (CorAgente agt :
                agentes) {
            if (agt.getIdeAgente().equals(IDE_AGENTE))
                assertEquals(ESTADO_DISTRIBUCION, agt.getEstadoDistribucion());
        }
    }
}
