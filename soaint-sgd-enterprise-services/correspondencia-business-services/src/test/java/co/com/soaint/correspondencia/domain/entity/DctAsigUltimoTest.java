package co.com.soaint.correspondencia.domain.entity;

import co.com.soaint.correspondencia.JPAHibernateContextTest;
import co.com.soaint.foundation.canonical.correspondencia.AsignacionDTO;
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
 * Created by yleon on 04/05/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"/META-INF/core-config.xml"}
)
@Log4j2
public class DctAsigUltimoTest extends JPAHibernateContextTest {

    @Test
    public void test_findAll_success() {
        //given
        String namedQuery = "DctAsigUltimo.findAll";
        //when
        List<DctAsigUltimo> dctAsigUltimos = createNamedQuery(namedQuery, DctAsigUltimo.class)
                .getResultList();

        //then
        assertNotNull(dctAsigUltimos);
        assertEquals(2, dctAsigUltimos.size());
    }

    @Test
    public void test_findByIdeAgente_success() {
        //given
        String namedQuery = "DctAsigUltimo.findByIdeAgente";
        BigInteger idAgente = BigInteger.valueOf(100);
        BigInteger id = BigInteger.valueOf(100);

        //when
        DctAsigUltimo dctAsigUltimo = createNamedQuery(namedQuery, DctAsigUltimo.class)
                .setParameter("IDE_AGENTE", idAgente)
                .getSingleResult();
        //then
        assertNotNull(dctAsigUltimo);
        assertEquals(id, dctAsigUltimo.getIdeAsigUltimo());
    }

    @Test
    public void test_findByIdeAsignacion_success() {
        //given
        String namedQuery = "DctAsigUltimo.findByIdeAsignacion";
        BigInteger idAsignaicon = BigInteger.valueOf(100);
        BigInteger id = BigInteger.valueOf(100);

        //when
        DctAsigUltimo dctAsigUltimo = createNamedQuery(namedQuery, DctAsigUltimo.class)
                .setParameter("IDE_ASIGNACION", idAsignaicon)
                .getSingleResult();
        //then
        assertNotNull(dctAsigUltimo);
        assertEquals(id, dctAsigUltimo.getIdeAsigUltimo());
    }

    @Test
    public void test_findByIdeFunciAndNroRadicado_success() {
        //given
        String namedQuery = "DctAsigUltimo.findByIdeFunciAndNroRadicado";
        BigInteger idFunc = BigInteger.valueOf(2);
        String nroRadicado = null;

        //when
        List<AsignacionDTO> asignaicones = createNamedQuery(namedQuery, AsignacionDTO.class)
                .setParameter("IDE_FUNCI", idFunc)
                .setParameter("NRO_RADICADO", nroRadicado)
                .getResultList();
        //then
        assertNotNull(asignaicones);
        assertEquals(1, asignaicones.size());
    }

    @Test
    public void test_findByIdeAgenteAndCodEstado_success() {
        //given
        String namedQuery = "DctAsigUltimo.findByIdeAgenteAndCodEstado";
        BigInteger idAgente = BigInteger.valueOf(100);
        String codEstado = "AS";
        BigInteger id = BigInteger.valueOf(100);
        //when
        AsignacionDTO asignaicon = createNamedQuery(namedQuery, AsignacionDTO.class)
                .setParameter("IDE_AGENTE", idAgente)
                .setParameter("COD_ESTADO", codEstado)
                .getSingleResult();
        //then
        assertNotNull(asignaicon);
        assertEquals(id, asignaicon.getIdeAsigUltimo());
    }

    @Test
    public void test_consultarByIdeAgente_success() {
        //given
        String namedQuery = "DctAsigUltimo.consultarByIdeAgente";
        BigInteger idAgente = BigInteger.valueOf(100);
        BigInteger id = BigInteger.valueOf(100);
        //when
        AsignacionDTO asignaicon = createNamedQuery(namedQuery, AsignacionDTO.class)
                .setParameter("IDE_AGENTE", idAgente)
                .getSingleResult();
        //then
        assertNotNull(asignaicon);
        assertEquals(id, asignaicon.getIdeAsigUltimo());
    }

    @Test
    public void test_updateIdInstancia_success() {
        //given
        String namedQueryUpdate = "DctAsigUltimo.updateIdInstancia";
        String namedQueryFindAll = "DctAsigUltimo.findAll";
        BigInteger ideAsigUltimo = BigInteger.valueOf(100);
        String idInstancia = "newIdIns";

        //when
        createNamedQuery(namedQueryUpdate)
                .setParameter("ID_INSTANCIA", idInstancia)
                .setParameter("IDE_ASIG_ULTIMO", ideAsigUltimo)
                .executeUpdate();

        List<DctAsigUltimo> dctAsigUltimos = createNamedQuery(namedQueryFindAll, DctAsigUltimo.class)
                .getResultList();

        //then
        assertNotNull(dctAsigUltimos);
        for (DctAsigUltimo doc :
                dctAsigUltimos) {
            if (doc.getIdeAsigUltimo().equals(ideAsigUltimo))
                assertEquals(idInstancia, doc.getIdInstancia());
        }
    }

    @Test
    public void test_updateTipoProceso_success() {
        //given
        String namedQueryUpdate = "DctAsigUltimo.updateTipoProceso";
        String namedQueryFindAll = "DctAsigUltimo.findAll";
        BigInteger ideAsigUltimo = BigInteger.valueOf(100);
        String codTipProceso = "newCod";

        //when
        createNamedQuery(namedQueryUpdate)
                .setParameter("COD_TIPO_PROCESO", codTipProceso)
                .setParameter("IDE_ASIG_ULTIMO", ideAsigUltimo)
                .executeUpdate();

        List<DctAsigUltimo> dctAsigUltimos = createNamedQuery(namedQueryFindAll, DctAsigUltimo.class)
                .getResultList();

        //then
        assertNotNull(dctAsigUltimos);
        for (DctAsigUltimo doc :
                dctAsigUltimos) {
            if (doc.getIdeAsigUltimo().equals(ideAsigUltimo))
                assertEquals(codTipProceso, doc.getCodTipProceso());
        }
    }

    @Test
    public void test_updateNumRedirecciones_success() {
        //given
        String namedQueryUpdate = "DctAsigUltimo.updateNumRedirecciones";
        String namedQueryFindAll = "DctAsigUltimo.findAll";
        BigInteger ideAsigUltimo = BigInteger.valueOf(100);

        //when

        List<DctAsigUltimo> dctAsigUltimos = createNamedQuery(namedQueryFindAll, DctAsigUltimo.class)
                .getResultList();

        createNamedQuery(namedQueryUpdate)
                .setParameter("IDE_ASIG_ULTIMO", ideAsigUltimo)
                .executeUpdate();

        List<DctAsigUltimo> dctAsigUltimos1 = createNamedQuery(namedQueryFindAll, DctAsigUltimo.class)
                .getResultList();

        //then
        assertNotNull(dctAsigUltimos);
        for (DctAsigUltimo doc :
                dctAsigUltimos) {
            if (doc.getIdeAsigUltimo().equals(ideAsigUltimo))
                for (DctAsigUltimo doc1 :
                        dctAsigUltimos1) {
                    if (doc1.getIdeAsigUltimo().equals(ideAsigUltimo))
                        assertEquals(doc.getNumRedirecciones() + 1, doc1.getNumRedirecciones() + 1);
                }
        }
    }

    @Test
    public void test_updateNumDevoluciones_success() {
        //given
        String namedQueryUpdate = "DctAsigUltimo.updateNumDevoluciones";
        String namedQueryFindAll = "DctAsigUltimo.findAll";
        BigInteger ideAsigUltimo = BigInteger.valueOf(100);

        //when

        List<DctAsigUltimo> dctAsigUltimos = createNamedQuery(namedQueryFindAll, DctAsigUltimo.class)
                .getResultList();

        createNamedQuery(namedQueryUpdate)
                .setParameter("IDE_ASIG_ULTIMO", ideAsigUltimo)
                .executeUpdate();

        List<DctAsigUltimo> dctAsigUltimos1 = createNamedQuery(namedQueryFindAll, DctAsigUltimo.class)
                .getResultList();

        //then
        assertNotNull(dctAsigUltimos);
        for (DctAsigUltimo doc :
                dctAsigUltimos) {
            if (doc.getIdeAsigUltimo().equals(ideAsigUltimo))
                for (DctAsigUltimo doc1 :
                        dctAsigUltimos1) {
                    if (doc1.getIdeAsigUltimo().equals(ideAsigUltimo))
                        assertEquals(doc.getNumDevoluciones() + 1, doc1.getNumDevoluciones() + 1);
                }
        }
    }
}
