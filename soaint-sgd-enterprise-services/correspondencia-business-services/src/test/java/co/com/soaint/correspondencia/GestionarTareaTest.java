package co.com.soaint.correspondencia;

import co.com.soaint.correspondencia.business.boundary.GestionarConstantes;
import co.com.soaint.correspondencia.business.boundary.GestionarTarea;
import co.com.soaint.correspondencia.business.control.ConstantesControl;
import co.com.soaint.correspondencia.business.control.TareaControl;
import co.com.soaint.foundation.canonical.correspondencia.ConstanteDTO;
import co.com.soaint.foundation.canonical.correspondencia.ConstantesDTO;
import co.com.soaint.foundation.canonical.correspondencia.TareaDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by gyanet on 04/04/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"/META-INF/core-config.xml"}
)
@Log4j2
public class GestionarTareaTest extends  JPAHibernateTest {

    @Autowired
    private TareaControl tareaControl;

    @Autowired
    private GestionarTarea gestionarTarea;

    @Test
    public void test_guardarEstadoTarea_success() throws SystemException {

        //given
        String estado = "ACTIVO";
        String idInstanciaProceso = "ACTIVO";
        String idTareaProceso = "ACTIVO";
        String payload = "[{}]";
        TareaDTO tareaDTO = TareaDTO.newInstance()
                .idTareaProceso(idTareaProceso)
                .idInstanciaProceso(idInstanciaProceso)
                .payload(payload)
                .build();
        //when
        tareaControl.guardarEstadoTarea(tareaDTO);
        //then
            assertEquals(2, );
            assertEquals("CODIGO1", constantes.get(0).getCodigo());

    }

    @Test
    public void  testTvsConstantes_listarConstantesByCodigoAndEstado_success() throws SystemException {
        String estado = "ACTIVO";
        String codigo = "CODIGO1";

        List<ConstanteDTO> constantes = constantesControl.listarConstantesByCodigoAndEstado(codigo,estado);
        assertEquals(1, constantes.size());
        assertEquals("NOMBRE1", constantes.get(0).getNombre());

    }

    @Test
    public void  testTvsConstantes_listarConstantesByCodPadreAndEstado_success() throws SystemException {
        String estado = "ACTIVO";
        String codpadre = "CODIGOPADRE1";

        List<ConstanteDTO> constantes = constantesControl.listarConstantesByCodPadreAndEstado(codpadre,estado);
        assertEquals(1, constantes.size());
        assertEquals("NOMBRE1", constantes.get(0).getNombre());

    }

    @Test
    public void  testTvsConstantes_listarConstantesByCodigo_success() throws SystemException {
        String[] codigos = {"CA-TID"};

        ConstantesDTO constantes = constantesControl.listarConstantesByCodigo(codigos);
        assertEquals(1, constantes.getConstantes().size());
        assertEquals("CA-TID", constantes.getConstantes().get(0).getCodigo());

    }
}
