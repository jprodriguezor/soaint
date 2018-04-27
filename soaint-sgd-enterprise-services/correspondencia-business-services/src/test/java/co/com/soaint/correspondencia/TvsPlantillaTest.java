package co.com.soaint.correspondencia;

import co.com.soaint.foundation.canonical.correspondencia.PlantillaDTO;
import co.com.soaint.correspondencia.domain.entity.TvsPlantilla;

import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by gyanet on 04/04/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"/META-INF/core-config.xml"}
)
@Log4j2
public class TvsPlantillaTest extends JPAHibernateContextTest {

    @Test
    public void test_TvsPlantillafindByCodClasificacionAndEstado_success()  {

        //given
        String namedQuery ="TvsPlantilla.findByCodClasificacionAndEstado";
        String codigoDocumento = "CODIGO1";
        String estado = "A";

        //when
        PlantillaDTO plantilla= createNamedQuery(namedQuery, PlantillaDTO.class)

                .setParameter("COD_TIPO_DOC", codigoDocumento)
                .setParameter("ESTADO", estado)
                .getSingleResult();
        //then
        assertNotNull(plantilla);
        assertEquals(codigoDocumento, plantilla.getCodTipoDoc());

//        assertEquals(estado, plantilla.getEstado());

    }
    @Test
    public void test_TvsPlantillafindTipoDocByEstado_success() {

        //given
        String namedQuery ="TvsPlantilla.findTipoDocByEstado";
        String estado = "A";

        //when
        List<PlantillaDTO> plantillaByestado= createNamedQuery(namedQuery, PlantillaDTO.class)
           .setParameter("ESTADO", estado)
                .getResultList();

        //then
        assertNotNull(plantillaByestado);


    }


}
