package co.com.soaint.correspondencia;

import co.com.soaint.correspondencia.business.boundary.GestionarPlanillas;
import co.com.soaint.foundation.canonical.correspondencia.PlanAgentesDTO;
import co.com.soaint.foundation.canonical.correspondencia.PlanillaDTO;
import co.com.soaint.foundation.canonical.correspondencia.ReportDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by eric.rodriguez on 13/02/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/META-INF/core-config.xml"})
public class GestionarPlanillasTest {

    @Autowired
    private GestionarPlanillas boundary;

    @Test
    @Transactional
    public void test_planillas_listar_planillas_by_nro_planilla() throws SystemException, BusinessException {
        PlanillaDTO planillaObtenida = boundary.listarPlanillasByNroPlanilla("104000000000002");
        assertEquals(new BigInteger("200"), planillaObtenida.getIdePlanilla());
    }

    @Test
    @Transactional
    public void test_planillas_generar_planillas() throws SystemException, BusinessException {
        PlanillaDTO planillaDTO = new PlanillaDTO();
        PlanAgentesDTO planAgentesDTO = new PlanAgentesDTO();
        planAgentesDTO.setPAgente(new ArrayList<>());
        planillaDTO.setPAgentes(planAgentesDTO);
        planillaDTO.setCodSedeOrigen("1040");
        PlanillaDTO planillaGenerada = boundary.generarPlanilla(planillaDTO);
        PlanillaDTO planillaObtenida = boundary.listarPlanillasByNroPlanilla(planillaGenerada.getNroPlanilla());
        assertEquals(planillaGenerada.getNroPlanilla(), planillaObtenida.getNroPlanilla());
    }

    @Test
    @Transactional
    public void test_planillas_cargar_planilla() throws SystemException, BusinessException {
        PlanillaDTO planillaDTO = new PlanillaDTO();
        PlanAgentesDTO planAgentesDTO = new PlanAgentesDTO();
        planAgentesDTO.setPAgente(new ArrayList<>());
        planillaDTO.setPAgentes(planAgentesDTO);
        planillaDTO.setIdeEcm("1234");
        planillaDTO.setIdePlanilla(new BigInteger("100"));
        planillaDTO.setNroPlanilla("104000000000001");
        boundary.cargarPlanilla(planillaDTO);
        PlanillaDTO planillaObtenida = boundary.listarPlanillasByNroPlanilla(planillaDTO.getNroPlanilla());
        assertEquals("1234", planillaObtenida.getIdeEcm());
    }
}