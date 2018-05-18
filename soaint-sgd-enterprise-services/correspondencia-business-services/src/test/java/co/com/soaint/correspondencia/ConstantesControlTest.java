package co.com.soaint.correspondencia;

import co.com.soaint.correspondencia.business.control.ConstantesControl;
import co.com.soaint.foundation.canonical.correspondencia.ConstanteDTO;
import co.com.soaint.correspondencia.domain.entity.TvsConstantes;

import co.com.soaint.foundation.canonical.correspondencia.ConstantesDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by gyanet on 16/05/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"/META-INF/core-config.xml"}
)
@Log4j2
public class ConstantesControlTest {

    @Autowired
    private ConstantesControl constantesControl;

    @Test
    public void listarConstantesByEstado() throws SystemException {
        //given
        String estado = "ACTIVO";

        //when
        List<ConstanteDTO> constantes = constantesControl.listarConstantesByEstado(estado);

        //then
        assertNotNull(constantes);
        assertEquals(3,constantes.size());
    }

    @Test
    public void listarConstantesByCodigoAndEstado() throws SystemException {
        //given
        String codigo = "CODIGO2";
        String estado = "ACTIVO";

        //when
        List<ConstanteDTO> constantes = constantesControl.listarConstantesByEstado(estado);

        //then
        assertNotNull(constantes);
        assertEquals(3,constantes.size());
    }

    @Test
    public void listarConstantesByCodPadreAndEstado() throws SystemException {
        //given
        String codigo = "CODIGOPADRE1";
        String estado = "ACTIVO";

        //when
        List<ConstanteDTO> constantes = constantesControl.listarConstantesByCodPadreAndEstado(codigo,estado);

        //then
        assertNotNull(constantes);
        assertEquals(1,constantes.size());
    }

    @Test
    public void listarConstantesByCodigo() throws SystemException {
        String codigo = "CODIGOPADRE1";
        String[] codigos = {"CODIGOPADRE1"};

        //when
        ConstantesDTO constantes = constantesControl.listarConstantesByCodigo(codigos);

        //then
        assertNotNull(constantes);
        assertEquals(1,constantes.getConstantes().size());
    }

//    @Test
//    public void consultarConstanteByCodigo() {
//    }
//
//    @Test
//    public void consultarNombreConstanteByCodigo() {
//    }
}