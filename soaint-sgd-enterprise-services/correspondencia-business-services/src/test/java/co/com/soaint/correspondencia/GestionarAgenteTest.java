package co.com.soaint.correspondencia;

import co.com.soaint.correspondencia.business.boundary.GestionarAgente;
import co.com.soaint.correspondencia.business.control.AgenteControl;
import co.com.soaint.correspondencia.business.control.PpdTrazDocumentoControl;
import co.com.soaint.foundation.canonical.correspondencia.AgenteDTO;
import co.com.soaint.foundation.canonical.correspondencia.DestinatarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.PpdTrazDocumentoDTO;
import co.com.soaint.foundation.canonical.correspondencia.RedireccionDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by eric.rodriguez on 14/02/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/META-INF/core-config.xml"})
public class GestionarAgenteTest {

    @Autowired
    private GestionarAgente boundary;

    @Autowired
    private AgenteControl control;

    @Autowired
    PpdTrazDocumentoControl ppdTrazDocumentoControl;

    @Test
    @Transactional
    public void test_actualizar_estado_agente_success() throws SystemException, BusinessException {
        AgenteDTO agenteDTO = control.consultarAgenteByIdeAgente(new BigInteger("200"));
        agenteDTO.setCodEstado("DT");
        boundary.actualizarEstadoAgente(agenteDTO);
        AgenteDTO agenteObtenido = control.consultarAgenteByIdeAgente(new BigInteger("200"));
        assertEquals("DT", agenteObtenido.getCodEstado());
    }

}
