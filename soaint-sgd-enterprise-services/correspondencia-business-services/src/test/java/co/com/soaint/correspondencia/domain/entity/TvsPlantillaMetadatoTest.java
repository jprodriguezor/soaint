package co.com.soaint.correspondencia.domain.entity;

import co.com.soaint.correspondencia.JPAHibernateContextTest;
import co.com.soaint.foundation.canonical.correspondencia.PlantillaMetadatoDTO;
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
public class TvsPlantillaMetadatoTest extends JPAHibernateContextTest {


    @Test
    public void test_TvsPlantillaMestadatofindByIdePlantilla_succes(){
        //given
        String namedQuery = "TvsPlantillaMestadato.findByIdePlantilla";
        BigInteger IdProceso = BigInteger.ONE;

        //when
         List<PlantillaMetadatoDTO>  plantillaMetadato = createNamedQuery(namedQuery, PlantillaMetadatoDTO.class)
                 .setParameter("IDE_PLANTILLA", IdProceso)
                 .getResultList();

        //then
        assertNotNull(plantillaMetadato);
        assertEquals(1,plantillaMetadato.size());
        assertEquals(plantillaMetadato.get(0).getIdePlantillaMetadato(), BigInteger.ONE);


    }

}
