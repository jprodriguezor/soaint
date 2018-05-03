package co.com.soaint.correspondencia;


import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.correspondencia.domain.entity.Funcionarios;


import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by yleon on 04/04/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"/META-INF/core-config.xml"}
)
@Log4j2
public class FuncionariosTest extends JPAHibernateContextTest {

    @Test
    public void test_FuncionariosfindAll_success() {
        //given
        String namedQuery = "Funcionarios.findAll";

        //when
        List<Funcionarios> funcionarios = createNamedQuery(namedQuery, Funcionarios.class)
                .getResultList();

        //then
        assertNotNull(funcionarios);
        assertTrue(funcionarios.size()>0);
    }
    @Test
    public void test_FuncionariosfindByLoginNameAndEstado_success() {
        //given
        String namedQuery = "Funcionarios.findByLoginNameAndEstado";
        String loginname="LOGIN_NAME";
        String estado="ACTIVO";

        //when
        FuncionarioDTO funcionarios = createNamedQuery(namedQuery, FuncionarioDTO.class)
                .setParameter("LOGIN_NAME",loginname)
                .setParameter("ESTADO",estado)
                .getSingleResult();

        //then
        assertNotNull(funcionarios);
        assertEquals(funcionarios.getLoginName(),loginname);

    }
    @Test
    public void test_FuncionariosfindByLoginNamList_success() {
        //given
        String namedQuery = "Funcionarios.findByLoginNamList";
        String loginname="LOGIN_NAME";


        //when
        List<FuncionarioDTO> funcionarios = createNamedQuery(namedQuery, FuncionarioDTO.class)
                .setParameter("LOGIN_NAMES",loginname)
                .getResultList();

        //then
        assertNotNull(funcionarios);
        assertTrue(funcionarios.size()>0);

    }

}
