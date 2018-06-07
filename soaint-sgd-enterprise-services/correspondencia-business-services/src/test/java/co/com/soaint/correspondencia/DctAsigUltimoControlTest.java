package co.com.soaint.correspondencia;

import co.com.soaint.correspondencia.business.control.DctAsigUltimoControl;
import org.junit.Test;
import co.com.soaint.correspondencia.domain.entity.DctAsigUltimo;
import co.com.soaint.foundation.canonical.correspondencia.DctAsigUltimoDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;

import java.math.BigInteger;

import static org.junit.Assert.*;

public class DctAsigUltimoControlTest {
    DctAsigUltimoControl dctAsigUltimoControl=new DctAsigUltimoControl();

    @Test
    public void dctAsigUltimoTransform() throws Exception {
        DctAsigUltimoDTO dctAsigUltimoDTO= DctAsigUltimoDTO.newInstance().ideAsigUltimo(BigInteger.ONE).build();
        DctAsigUltimo dctAsigUltimo= dctAsigUltimoControl.dctAsigUltimoTransform(dctAsigUltimoDTO);

        assertNotNull(dctAsigUltimo);
        assertEquals(dctAsigUltimo.getIdeAsigUltimo(),BigInteger.ONE);
    }

}