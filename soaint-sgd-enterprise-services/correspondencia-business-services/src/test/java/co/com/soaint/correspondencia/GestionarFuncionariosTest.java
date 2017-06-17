package co.com.soaint.correspondencia;

import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by esanchez on 6/17/2017.
 */
public class GestionarFuncionariosTest extends  JPAHibernateTest {
    @Test
    public void test_Funcionarios_findByLoginName_success(){
        String loginName = "LOGIN_NAME";
        FuncionarioDTO funcionarioDTO = em.createNamedQuery("Funcionarios.findByLoginName", FuncionarioDTO.class)
                .setParameter("LOGIN_NAME", loginName)
                .getSingleResult();
        assertEquals("NOM_FUNCIONARIO1",funcionarioDTO.getNomFuncionario());
    }
}
