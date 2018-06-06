package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.jfree.util.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * Created by esanchez on 6/17/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/META-INF/core-config.xml"})
public class GestionarFuncionariosTest {

    @Autowired
    private GestionarFuncionarios boundary;

    @Test
    public void test_Funcionarios_findByLoginNameAndEstado_failure() throws SystemException, BusinessException {
        try{
            String loginName = "LOGIN_NAME";
            String estado = "ACTIVO";
            FuncionarioDTO funcionarioDTO = boundary.listarFuncionarioByLoginNameAndEstado(loginName, estado);
            assertEquals("NOM_FUNCIONARIO1",funcionarioDTO.getNomFuncionario());
        } catch (Exception e){
            System.out.println(e.getMessage());
//            assertTrue(e.getCause() instanceof NullPointerException);
            Log.error("GestionarFuncionariosTest - a business error has occurred", e);
        }

    }
}
