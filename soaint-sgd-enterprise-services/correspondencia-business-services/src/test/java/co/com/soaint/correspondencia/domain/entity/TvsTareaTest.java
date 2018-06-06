package co.com.soaint.correspondencia.domain.entity;

import co.com.soaint.correspondencia.JPAHibernateContextTest;
import co.com.soaint.foundation.canonical.correspondencia.TareaDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by gyanet on 04/04/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"/META-INF/core-config.xml"}
)
@Log4j2
public class TvsTareaTest extends JPAHibernateContextTest {

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

    @Test
    public void test_findByIdInstanciaProcesoAndIdTareaProceso_success() {
        //given
        String namedQuery = "TvsTarea.findByIdInstanciaProcesoAndIdTareaProceso";
        String instanciaProceso = "99059";
        String tareaProceso = "0000";

        //when
        TvsTarea tarea = createNamedQuery(namedQuery, TvsTarea.class)
                .setParameter("ID_INSTANCIA_PROCESO", instanciaProceso)
                .setParameter("ID_TAREA_PROCESO", tareaProceso)
                .getSingleResult();

        //then
        assertNotNull(tarea);
        assertEquals(tarea.getIdInstanciaProceso(), "99059");
        assertEquals(tarea.getIdTareaProceso(), "0000");
    }

    @Test
    public void test_findAll_success() {
        //given
        String namedQuery = "TvsTarea.findAll";

        //when
        List<TvsTarea> tareas = createNamedQuery(namedQuery, TvsTarea.class)
                .getResultList();

        //then
        assertNotNull(tareas);
        assertEquals(3, tareas.size());
    }

    @Test
    public void test_existByIdInstanciaProcesoAndIdTareaProceso_success() {
        //given
        String namedQuery = "TvsTarea.existByIdInstanciaProcesoAndIdTareaProceso";
        String instanciaProceso = "99059";
        String tareaProceso = "0000";

        //when
        Long count = createNamedQuery(namedQuery,Long.class)
                .setParameter("ID_INSTANCIA_PROCESO", instanciaProceso)
                .setParameter("ID_TAREA_PROCESO", tareaProceso)
                .getSingleResult();

        //then
        assertNotNull(count);
        assertTrue(count > 0);
    }

    @Test
    public void test_updatePayloadByIdInstanciaProcesoAndIdTareaProceso_success() {
        //given
        String namedQueryUpdate = "TvsTarea.updatePayloadByIdInstanciaProcesoAndIdTareaProceso";
        String namedQueryFindAll = "TvsTarea.findByIdInstanciaProcesoAndIdTareaProceso";
        String instanciaProceso = "99059";
        String tareaProceso = "0000";
        String payload = "newPayload";

        //when
        createNamedQuery(namedQueryUpdate)
                .setParameter("ID_INSTANCIA_PROCESO", instanciaProceso)
                .setParameter("ID_TAREA_PROCESO", tareaProceso)
                .setParameter("PAYLOAD", payload)
                .executeUpdate();

        List<TvsTarea> tareas = createNamedQuery(namedQueryFindAll, TvsTarea.class)
                .setParameter("ID_INSTANCIA_PROCESO", instanciaProceso)
                .setParameter("ID_TAREA_PROCESO", tareaProceso)
                .getResultList();

        //then
        assertNotNull(tareas);
        tareas.forEach(tarea -> assertEquals(payload, tarea.getPayload()));
    }
}
