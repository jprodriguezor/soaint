package co.com.soaint.correspondencia;

import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

/**
 * Created by gyanet on 04/04/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"/META-INF/core-config.xml"}
)
@Log4j2
public class FuncionariosTest extends  JPAHibernateTest {

    @Test
    public void test_guardarEstadoTarea_success() throws SystemException {

        //given

        //when
//        tareaControl.guardarEstadoTarea(tareaDTO);
        //then

    }
}
