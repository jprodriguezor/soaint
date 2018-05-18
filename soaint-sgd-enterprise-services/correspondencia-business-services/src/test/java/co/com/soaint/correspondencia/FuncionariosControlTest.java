package co.com.soaint.correspondencia;

import co.com.soaint.correspondencia.JPAHibernateContextTest;
import co.com.soaint.correspondencia.business.control.FuncionariosControl;
import co.com.soaint.foundation.canonical.correspondencia.AgenteDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionariosDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by yleon on 08/05/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"/META-INF/core-config.xml"}
)
@Log4j2
public class FuncionariosControlTest extends JPAHibernateContextTest {


    @Autowired
    FuncionariosControl funcionariosControl;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void listarFuncionarioByLoginNameAndEstado() throws Exception {
       String funcLogin="LOGIN_NAME";
       String funcEstado="ACTIVO";

        FuncionarioDTO funcionario = funcionariosControl.listarFuncionarioByLoginNameAndEstado(funcLogin,funcEstado);

        //then
        assertNotNull(funcionario);
        assertNotEquals(0, funcionario);
        assertEquals(funcEstado,funcionario.getEstado());
        assertEquals(funcLogin,funcionario.getLoginName());
    }

    @Test
    public void listarFuncionariosByCodDependenciaAndCodEstado() throws Exception {

        String funcCoddependencia="10401041";
        String funcEstado="ACTIVO";

        FuncionariosDTO funcionario = funcionariosControl.listarFuncionariosByCodDependenciaAndCodEstado(funcCoddependencia,funcEstado);

        //then
        assertNotNull(funcionario);
        assertNotEquals(0, funcionario);
        assertEquals(funcEstado,funcionario.getFuncionarios().get(0).getEstado());
    }

    @Test
    public void listarFuncionarioByLoginName() throws Exception {
        String funcLogin="LOGIN_NAME";

        FuncionarioDTO funcionario = funcionariosControl.listarFuncionarioByLoginName(funcLogin);
        //then
        assertNotNull(funcionario);
        assertNotEquals(0, funcionario);
        assertEquals(funcLogin,funcionario.getLoginName());
    }

    @Test
    public void listarFuncionariosByLoginNameList() throws Exception {

        String Loginnombres[]={"LOGIN_NAME","LOGIN_NAME1","LOGIN_NAME2","LOGIN_NAME4"};

        FuncionariosDTO funcionario = funcionariosControl.listarFuncionariosByLoginNameList(Loginnombres);

        //then
        assertNotNull(funcionario);
        assertNotEquals(0, funcionario);
    }

    @Test
    public void existFuncionarioByIdeFunci() throws Exception {
        BigInteger IdeFunci=new BigInteger("1");
        boolean flag=true;

        flag=funcionariosControl.existFuncionarioByIdeFunci(IdeFunci);

        if ( flag)
       assertTrue(flag);
    }

    @Test
    public void consultarFuncionarioByIdeFunci() throws Exception {

        BigInteger IdeFunci=new BigInteger("1");

        FuncionarioDTO funcionario = funcionariosControl.consultarFuncionarioByIdeFunci(IdeFunci);
        //then
        assertNotNull(funcionario);
        assertNotEquals(0, funcionario);
        assertEquals(IdeFunci,funcionario.getIdeFunci());
    }

    @Test
    public void consultarFuncionarioByNroIdentificacion() throws Exception {
        String nroIdentificacion="NRO_IDENTIFICACION";

        List<FuncionarioDTO> funcionario = funcionariosControl.consultarFuncionarioByNroIdentificacion(nroIdentificacion);
        //then
        assertNotNull(funcionario);
        assertNotEquals(0, funcionario);
        for (FuncionarioDTO fun :
                funcionario) {
            assertEquals(nroIdentificacion, fun.getNroIdentificacion());
        }
    }

    @Test
    public void consultarCredencialesByIdeFunci() throws Exception {
        BigInteger IdeFunci=new BigInteger("1");

        String funcionario = funcionariosControl.consultarCredencialesByIdeFunci(IdeFunci);
        //then
        assertNotNull(funcionario);
        assertNotEquals(0, funcionario);
        assertEquals("CREDENCIALES 1",funcionario);
    }

    @Test
    public void consultarLoginNameByIdeFunci() throws Exception {
        BigInteger IdeFunci=new BigInteger("1");

        String funcionario = funcionariosControl.consultarLoginNameByIdeFunci(IdeFunci);
        //then
        assertNotNull(funcionario);
        assertNotEquals(0, funcionario);
        assertEquals("LOGIN_NAME",funcionario);
    }

    @Test
    public void crearFuncionario() throws Exception {
    }

    @Test
    public void actualizarFuncionario() throws Exception {
    }

    @Test
    public void buscarFuncionario() throws Exception {
    }

    @Test
    public void funcionarioTransform() throws Exception {
    }

}