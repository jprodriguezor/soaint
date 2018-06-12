package co.com.soaint.correspondencia.domain.entity;

import co.com.soaint.correspondencia.JPAHibernateContextTest;
import co.com.soaint.foundation.canonical.correspondencia.PlanAgenDTO;
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
public class CorPlanAgenTest extends JPAHibernateContextTest {

    @Test
    public void test_findAll_success() {
        //given
        String namedQuery = "CorPlanAgen.findAll";
        //when
        List<CorPlanAgen> corPlanAgens = createNamedQuery(namedQuery, CorPlanAgen.class)
                .getResultList();

        //then
        assertNotNull(corPlanAgens);
        assertEquals(1, corPlanAgens.size());
    }

    @Test
    public void test_findByIdePlanilla_success() {
        //given
        String namedQuery = "CorPlanAgen.findByIdePlanilla";
        BigInteger idPlanilla = BigInteger.valueOf(100);
        BigInteger id = BigInteger.valueOf(100);

        //when
        PlanAgenDTO planAgen = createNamedQuery(namedQuery, PlanAgenDTO.class)
                .setParameter("IDE_PLANILLA", idPlanilla)
                .getSingleResult();
        //then
        assertNotNull(planAgen);
        assertEquals(id, planAgen.getIdePlanAgen());
    }

    @Test
    public void test_findByIdePlanillaAndEstado_success() {
        //given
        String namedQuery = "CorPlanAgen.findByIdePlanillaAndEstado";
        BigInteger idPlanilla = BigInteger.valueOf(100);
        String estado = "ACTIVO";


        //when
        List<PlanAgenDTO> planAgens = createNamedQuery(namedQuery, PlanAgenDTO.class)
                .setParameter("IDE_PLANILLA", idPlanilla)
                .setParameter("ESTADO", estado)
                .getResultList();
        //then
        assertNotNull(planAgens);
        assertEquals(1, planAgens.size());
    }

    @Test
    public void test_updateEstadoDistribucion_success() {
        //given
        String namedQueryUpdate = "CorPlanAgen.updateEstadoDistribucion";
        String namedQueryFindAll = "CorPlanAgen.findAll";
        BigInteger idePlanAgen = BigInteger.valueOf(100);
        String estado = "newEst";
        String peso = "newPeso";

        //when
        createNamedQuery(namedQueryUpdate)
                .setParameter("IDE_PLAN_AGEN", idePlanAgen)
                .setParameter("ESTADO", estado)
                .setParameter("VAR_PESO", peso)
                .setParameter("VAR_VALOR", null)
                .setParameter("VAR_NUMERO_GUIA", null)
                .setParameter("FEC_OBSERVACION", null)
                .setParameter("COD_NUEVA_SEDE", null)
                .setParameter("OBSERVACIONES", null)
                .setParameter("COD_CAU_DEVO", null)
                .setParameter("FEC_CARGUE_PLA", null)
                .setParameter("COD_NUEVA_DEPEN", null)
                .executeUpdate();

        List<CorPlanAgen> corPlanAgens = createNamedQuery(namedQueryFindAll, CorPlanAgen.class)
                .getResultList();

        //then
        assertNotNull(corPlanAgens);
        for (CorPlanAgen doc :
                corPlanAgens) {
            if (doc.getIdePlanAgen().equals(idePlanAgen)) {
                assertEquals(estado, doc.getEstado());
                assertEquals(peso, doc.getVarPeso());
            }
        }
    }
}
