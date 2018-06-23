package co.com.soaint.correspondencia.business.control;

import co.com.soaint.foundation.canonical.correspondencia.ConstanteDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
import static org.junit.Assert.*;

/**
 * Created by gyanet on 04/04/2018.
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
        List<ConstanteDTO> constanteDTOList = constantesControl.listarConstantesByEstado(estado);

        //then
        assertNotNull(constanteDTOList);
        assertEquals(constanteDTOList.size(), 2);
        assertThat(constanteDTOList.get(0).getCodigo(), is("CODIGO1"));
        assertThat(constanteDTOList.get(1).getCodigo(), is("CA-TID"));
    }

    @Test
    public void listarConstantesByCodigoAndEstado() throws SystemException {
        //given
        String estado = "ACTIVO";
        String codigo = "CA-TID";

        //when
        List<ConstanteDTO> constanteDTOList = constantesControl.listarConstantesByCodigoAndEstado(codigo,estado);

        //then
        assertNotNull(constanteDTOList);
        assertTrue(constanteDTOList.size()>0);
        assertEquals(constanteDTOList.size(), 1);
        assertThat(constanteDTOList.get(0).getCodigo(),is("CA-TID"));
    }

    @Test
    public void listarConstantesByCodPadreAndEstado() throws SystemException {
        //given
        String estado = "ACTIVO";
        String codigo = "CODIGO1";

        //when
        List<ConstanteDTO> constanteDTOList = constantesControl.listarConstantesByCodPadreAndEstado(codigo,estado);

        //then
        assertNotNull(constanteDTOList);
        assertTrue(constanteDTOList.size()>0);
        assertEquals(constanteDTOList.size(), 1);
        assertThat(constanteDTOList.get(0).getCodPadre(),is("CODIGO1"));
    }

    @Test
    public void listarConstantesByCodigo() throws SystemException {
        //given
        String[] codigos = {"CODIGO1","CODIGO2","CA-TID"};

        //when
        List<ConstanteDTO> constanteDTOList = constantesControl.listarConstantesByCodigo(codigos).getConstantes();

        List<String> codigosResult = new ArrayList<String>();
        constanteDTOList.forEach(constanteDTO -> {
            codigosResult.add(constanteDTO.getCodigo());
        });

        //then
        assertNotNull(constanteDTOList);
        assertTrue(constanteDTOList.size()>2);
        assertEquals(constanteDTOList.size(), 3);
        assertThat(codigos[0], isIn(codigosResult));
        assertThat(codigos[1], isIn(codigosResult));
        assertThat(codigos[2], isIn(codigosResult));
    }

    @Test
    public void consultarConstanteByCodigo() throws SystemException, BusinessException {
        //given
        String codigo = "CODIGO1";

        //when
        ConstanteDTO constante = constantesControl.consultarConstanteByCodigo(codigo);

        //then
        assertNotNull(constante);
        assertThat(constante.getCodigo(),is("CODIGO1"));
    }

    @Test
    public void consultarNombreConstanteByCodigo() {
        //given
        String codigo = "CA-TID";

        //when
        String nombre = constantesControl.consultarNombreConstanteByCodigo(codigo);

        //then
        assertNotNull(nombre);
        assertThat(nombre, is("CA-TID-NOM"));

    }
}