package co.com.soaint.correspondencia;

import co.com.soaint.correspondencia.JPAHibernateContextTest;
import co.com.soaint.correspondencia.business.control.FuncionariosControl;
import co.com.soaint.correspondencia.business.control.DependenciaControl;
import co.com.soaint.foundation.canonical.correspondencia.AgenteDTO;
import co.com.soaint.foundation.canonical.correspondencia.DependenciaDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionariosDTO;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import lombok.extern.log4j.Log4j2;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigInteger;
import java.util.ArrayList;
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
        assertEquals(0, funcionario.getFuncionarios().size());
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
    public void crearFuncionario_failure() throws Exception {
        FuncionarioDTO fun;
        BigInteger id=new BigInteger("3");

        try{
            fun = new FuncionarioDTO(null,"COD_TIP_DOC_IDENT3","NRO_IDENTIFICACION3","NOM_FUNCIONARIO3","VAL_APELLIDO1","VAL_APELLIDO2","CORR_ELECTRONICO3","LOGIN_NAME3","ACTIVO");
            funcionariosControl.crearFuncionario(fun);
        } catch (Exception e){
            assertTrue(e.getCause() instanceof NullPointerException);
        }
//        assertEquals(fun,funcionariosControl.existFuncionarioByIdeFunci(id));
    }
    @Test
    public void crearFuncionario_success() throws Exception {
        FuncionarioDTO fun= new FuncionarioDTO();
        List<DependenciaDTO> dependencias = new ArrayList<DependenciaDTO>();

        DependenciaDTO dep= DependenciaDTO.newInstance().build();
        /*dep.setCodDependencia("10401040");
        dep.setCodSede("COD_SEDE");
        dep.setEstado("1");
        dep.setIdeSede(new BigInteger("10"));
        dep.setNomDependencia("1040.1042_GERENCIA NACIONAL DE LA GESTION DE RED BEPS");
        dep.setNomSede("1040.1042_GERENCIA NACIONAL DE LA GESTION DE RED BEPS");*/
        dependencias.add(dep);
        //dependencias.add(DependenciaDTO.newInstance().build());
        BigInteger id=new BigInteger("3");

        try{
            fun = new FuncionarioDTO(id,"COD_TIP_DOC_IDENT3","NRO_IDENTIFICACION3","NOM_FUNCIONARIO3","VAL_APELLIDO1","VAL_APELLIDO2","CORR_ELECTRONICO3","LOGIN_NAME3","ACTIVO",dependencias,null,"","");
            funcionariosControl.crearFuncionario(fun);
            assertEquals(fun,funcionariosControl.existFuncionarioByIdeFunci(id));

        } catch (Exception e){
            assertTrue(e.getCause() instanceof Exception);
        }
//
    }

    @Test
    public void actualizarFuncionario() throws Exception {
        FuncionarioDTO fun=funcionariosControl.listarFuncionarioByLoginName("LOGIN_NAME");

        fun.setCorrElectronico("sasa@sa.sa");
        String result=funcionariosControl.actualizarFuncionario(fun);
        FuncionarioDTO fun1=funcionariosControl.listarFuncionarioByLoginName("LOGIN_NAME");


        assertNotNull(result);
        assertNotEquals(0, result);
        assertEquals(result,"1");
        assertEquals(fun1.getCorrElectronico(),"sasa@sa.sa");

    }

    @Test
    public void buscarFuncionario() throws Exception {
        FuncionarioDTO fun=funcionariosControl.listarFuncionarioByLoginName("LOGIN_NAME");
        FuncionariosDTO result= funcionariosControl.buscarFuncionario(fun);

        assertNotNull(result);
        assertNotEquals(0, result);

    }



}