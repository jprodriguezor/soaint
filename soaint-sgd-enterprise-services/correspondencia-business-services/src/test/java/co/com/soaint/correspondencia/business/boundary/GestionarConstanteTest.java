package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.JPAHibernateTest;
import co.com.soaint.correspondencia.business.control.ConstantesControl;
import co.com.soaint.foundation.canonical.correspondencia.ConstanteDTO;
import co.com.soaint.foundation.canonical.correspondencia.ConstantesDTO;
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
public class GestionarConstanteTest extends JPAHibernateTest {

    @Autowired
    private ConstantesControl constantesControl;

    @Autowired
    private GestionarConstantes gestionarConstantes;

    @Test
    public void testTvsConstantes_listarConstantesByEstado_success() throws SystemException {
        String estado = "ACTIVO";

            List<ConstanteDTO> constantes = constantesControl.listarConstantesByEstado(estado);
            assertEquals(2, constantes.size());
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
