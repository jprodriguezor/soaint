package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.business.control.AsignacionControl;
import co.com.soaint.correspondencia.domain.entity.DctAsigUltimo;
import co.com.soaint.foundation.canonical.correspondencia.AsignacionDTO;
import co.com.soaint.foundation.canonical.correspondencia.AsignacionTramiteDTO;
import co.com.soaint.foundation.canonical.correspondencia.AsignacionesDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncAsigDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.jfree.util.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by eric.rodriguez on 15/02/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/META-INF/core-config.xml"})
public class GestionarAsignacionesTest {

    @Autowired
    private GestionarAsignacion boundary;

    @Autowired
    private AsignacionControl control;

    @Test
    @Transactional
    public void test_asignar_correspondencia_success() throws SystemException, BusinessException {
        AsignacionTramiteDTO asignacionTramiteDTO = new AsignacionTramiteDTO();
        AsignacionDTO asignacionDTO = new AsignacionDTO();
        asignacionDTO.setIdeAgente(new BigInteger("100"));
        asignacionDTO.setIdeFunci(new BigInteger("1"));
        asignacionDTO.setLoginName("eric");
        asignacionDTO.setIdeDocumento(new BigInteger("836"));
        asignacionTramiteDTO.setAsignaciones(new AsignacionesDTO());
        asignacionTramiteDTO.getAsignaciones().setAsignaciones(new ArrayList<>());
        asignacionTramiteDTO.getAsignaciones().getAsignaciones().add(asignacionDTO);
        AsignacionesDTO asignacionesDTO = boundary.asignarCorrespondencia(asignacionTramiteDTO);
        assertEquals("eric", asignacionesDTO.getAsignaciones().get(0).getLoginName());
    }

    @Test
    @Transactional
    public void test_actualizar_id_instancia_success() throws SystemException, BusinessException {
        AsignacionDTO asignacionDTO = new AsignacionDTO();
        asignacionDTO.setIdeAsignacion(new BigInteger("100"));
        asignacionDTO.setIdeAsigUltimo(new BigInteger("100"));
        asignacionDTO.setIdInstancia("666");
        boundary.actualizarIdInstancia(asignacionDTO);
        DctAsigUltimo asignacionObtenida = control.getAsignacionUltimoByIdeAgente(new BigInteger("100"));
        assertEquals("666", asignacionObtenida.getIdInstancia());
    }

    @Test
    @Transactional
    public void test_actualizar_tipo_proceso_success() throws SystemException, BusinessException {
        AsignacionDTO asignacionDTO = new AsignacionDTO();
        asignacionDTO.setIdeAsignacion(new BigInteger("100"));
        asignacionDTO.setIdeAsigUltimo(new BigInteger("100"));
        asignacionDTO.setCodTipProceso("TP");
        boundary.actualizarTipoProceso(asignacionDTO);
        DctAsigUltimo asignacionObtenida = control.getAsignacionUltimoByIdeAgente(new BigInteger("100"));
        assertEquals("TP", asignacionObtenida.getCodTipProceso());
    }

    @Test
    @Transactional
    public void test_listar_asignaciones_by_funcionario_and_nro_radicado_success() throws SystemException, BusinessException {
        AsignacionesDTO asignacionesDTO = boundary.listarAsignacionesByFuncionarioAndNroRadicado(new BigInteger("2"), "1040TP-CMCOE2017000002");
        assertEquals(new BigInteger("200"), asignacionesDTO.getAsignaciones().get(0).getIdeAsignacion());
    }

    @Test
    @Transactional
    public void test_consultar_asignacion_reasignar_by_ide_agente_failure() throws SystemException, BusinessException {
        try{
            FuncAsigDTO funcAsigDTO = boundary.consultarAsignacionReasignarByIdeAgente(new BigInteger("100"));
//            assertEquals(new BigInteger("100"), funcAsigDTO.getAsignacion().getIdeAsignacion());
//            assertEquals("CREDENCIALES 1", funcAsigDTO.getCredenciales());
        } catch (Exception e){
            assertTrue(e.getCause() instanceof BusinessException);
            Log.error("GestionarAsignacionesTest - a business error has occurred", e);

        }

    }

    //La operacion asignarDocumentoByNroRadicado no devuelve nada ni persiste en BD.
}
