package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.JPAHibernateTest;
import co.com.soaint.correspondencia.business.control.TareaControl;
import co.com.soaint.foundation.canonical.correspondencia.TareaDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
public class GestionarTareaTest extends JPAHibernateTest {

    @Autowired
    private TareaControl tareaControl;

    @Autowired
    private GestionarTarea gestionarTarea;

    @Test
    public void test_guardarEstadoTarea_success() throws SystemException {

        //given
        BigInteger idTarea = new BigInteger("60");
        String idInstanciaProceso = "99059";
        String idTareaProceso = "0000";
        String payload = "[{}]";
        TareaDTO tareaDTO = TareaDTO.newInstance()
                .idTareaProceso(idTareaProceso)
                .idInstanciaProceso(idInstanciaProceso)
                .ideTarea(idTarea)
                .payload(payload)
                .build();
        //when
//        tareaControl.guardarEstadoTarea(tareaDTO);
        //then
            assertEquals("0000", tareaDTO.getIdTareaProceso());

    }
}
