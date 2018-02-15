package co.com.soaint.correspondencia;

import co.com.soaint.correspondencia.business.boundary.GestionarAsignacion;
import co.com.soaint.correspondencia.business.control.AsignacionControl;
import co.com.soaint.foundation.canonical.correspondencia.AsignacionDTO;
import co.com.soaint.foundation.canonical.correspondencia.PlanillaDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by eric.rodriguez on 15/02/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/META-INF/core-config.xml"})
public class GestionarAsignacionesTest {

    @Autowired
    private GestionarAsignacion boundary;

    //TODO: test_asignar_correspondencia_success Pendiente de Agente y Correspondencia
    /*@Test
    @Transactional
    public void test_asignar_correspondencia_success() throws SystemException, BusinessException {
        PlanillaDTO planillaObtenida = boundary.listarPlanillasByNroPlanilla("104000000000002");
        assertEquals(new BigInteger("200"), planillaObtenida.getIdePlanilla());
    }*/

    @Test
    @Transactional
    public void test_actualizarIdInstancia_success() throws SystemException, BusinessException {
        AsignacionDTO asignacionDTO = new AsignacionDTO();
        asignacionDTO.setIdeAsignacion(new BigInteger("100"));
        asignacionDTO.setIdeAsigUltimo(new BigInteger("200"));
        asignacionDTO.setIdInstancia("666");
        boundary.actualizarIdInstancia(asignacionDTO);
        AsignacionDTO asignacionObtenida = boundary.listarAsignacionesByFuncionarioAndNroRadicado(BigInteger.ONE, "").getAsignaciones().get(0);
        assertEquals("666", asignacionObtenida.getIdInstancia());
    }

    /*
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
    }*/
}
