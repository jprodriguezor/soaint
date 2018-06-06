package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.JPAHibernateTest;
import co.com.soaint.correspondencia.business.control.AnexoControl;
import co.com.soaint.foundation.canonical.correspondencia.AnexoDTO;
import co.com.soaint.foundation.canonical.correspondencia.AnexoFullDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigInteger;

import static org.junit.Assert.assertTrue;

/**
 * Created by gyanet on 04/04/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"/META-INF/core-config.xml"}
)
@Log4j2
public class GestionarAnexoTest extends JPAHibernateTest {

    @Autowired
    private AnexoControl anexoControl;

    @Test
    public void test_anexo_transform_to_full_failure() throws BusinessException, SystemException {
        //given
        BigInteger ideAnexo = new BigInteger("100");
        String codAnexo = "CA-TID";
        String descripcion = "CA-TID-DESC";
        String codTipoSoporte = "CODIGO1";

        try {
            //when
            AnexoDTO anexoDTO = AnexoDTO.newInstance()
                    .codAnexo(codAnexo)
                    .ideAnexo(ideAnexo)
                    .descripcion(descripcion)
                    .codTipoSoporte(codTipoSoporte)
                    .build();

            AnexoFullDTO anexoFullDTO = anexoControl.anexoTransformToFull(anexoDTO);
        }catch (Exception e){
            //then
            assertTrue(e.getCause() instanceof NullPointerException);
            log.error("GestionarCorrespondenciaTest - a NullPointerException has occurred", e);
        }

//        assertEquals(anexoDTO.getIdeAnexo(), anexoFullDTO.getIdeAnexo());

    }
}
