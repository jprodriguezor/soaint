package co.com.soaint.correspondencia.domain.entity;

import co.com.soaint.correspondencia.JPAHibernateContextTest;
import co.com.soaint.foundation.canonical.correspondencia.PlanillaDTO;
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
 * Created by gyanet on 04/04/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"/META-INF/core-config.xml"}
)
@Log4j2
public class CorPlanillasTest extends JPAHibernateContextTest {

    @Test
    public void test_CorPlanillasfindByNroPlanilla_success() {

        //given
        String namedQuery = "CorPlanillas.findByNroPlanilla";
        String numPlanilla="104000000000001";

        PlanillaDTO planillas = createNamedQuery(namedQuery,PlanillaDTO.class)
                .setParameter("NRO_PLANILLA", numPlanilla)
                .getSingleResult();

        assertNotNull(planillas);
    }

    @Test
    public void test_CorPlanillasfindAll_success() {
        //given
        String namedQuery = "CorPlanillas.findAll";

        //when
        List<CorPlanillas> planillas = createNamedQuery(namedQuery, CorPlanillas.class)
                .getResultList();

        //then
        assertNotNull(planillas);
        assertEquals(2, planillas.size());
    }

    @Test
    public void test_CorPlanillasupdateReferenciaEcm_success() {
        //given
        String namedQueryUpdate = "CorPlanillas.updateReferenciaEcm";
        String namedQueryFindAll = "CorPlanillas.findByNroPlanilla";
        String IDE_ECM="sasa";
        BigInteger IDE_PLANILLA = new BigInteger("100");
        String numPlanilla="104000000000001";

        //when
        createNamedQuery(namedQueryUpdate)
                .setParameter("IDE_ECM", IDE_ECM)
                .setParameter("IDE_PLANILLA",IDE_PLANILLA)
                .executeUpdate();

        List<PlanillaDTO> planillas = createNamedQuery(namedQueryFindAll, PlanillaDTO.class)
                .setParameter("NRO_PLANILLA",numPlanilla)
                .getResultList();

        //then
        assertNotNull(planillas);
        planillas.forEach(pla -> assertEquals(IDE_PLANILLA, pla.getIdePlanilla()));
    }
}
