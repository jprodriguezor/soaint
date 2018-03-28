package co.com.soaint.correspondencia;

import co.com.soaint.correspondencia.business.boundary.GestionarAgente;
import co.com.soaint.correspondencia.business.control.AgenteControl;
import co.com.soaint.correspondencia.business.control.PpdTrazDocumentoControl;
import co.com.soaint.correspondencia.domain.entity.PpdDocumento;
import co.com.soaint.correspondencia.domain.entity.PpdTrazDocumento;
import co.com.soaint.foundation.canonical.correspondencia.AgenteDTO;
import co.com.soaint.foundation.canonical.correspondencia.PpdTrazDocumentoDTO;
import co.com.soaint.foundation.canonical.correspondencia.RedireccionDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
        // given
        BigInteger idAgente = new BigInteger("200");
        String estadoAgente = "DT";
        AgenteDTO agenteDTO = control.consultarAgenteByIdeAgente(idAgente);
        agenteDTO.setCodEstado(estadoAgente);

        // when
        boundary.actualizarEstadoAgente(agenteDTO);

        // then
        AgenteDTO agenteObtenido = control.consultarAgenteByIdeAgente(idAgente);
        assertEquals(estadoAgente, agenteObtenido.getCodEstado());
    }

    @Test
    @Transactional
    public void test_obtener_agente_por_id_failure() {
        BigInteger ideAgente = new BigInteger("150");
        try {
            AgenteDTO agenteDTO = control.consultarAgenteByIdeAgente(ideAgente);
        } catch (BusinessException e) {
            assertTrue(e.getCause() instanceof NoResultException);
        } catch (SystemException e) {
            assertTrue(e.getCause() instanceof SystemException);
        }

    }

    @Test
    @Transactional
    public void test_consultar_remitente_by_nro_radicado_success() throws SystemException, BusinessException {
        // given
        String nroRadicado = "1040TP-CMCOE2017000001";

        // when
        AgenteDTO agenteDTO = boundary.consultarRemitenteByNroRadicado(nroRadicado);

        // then
        assertNotNull(agenteDTO);
    }

    @Test
    @Transactional
    public void test_consultar_remitente_by_nro_radicado_failure(){
        String nroRadicado = "1040TC-CMCOE2017000001";
        try {
            AgenteDTO agenteDTO = boundary.consultarRemitenteByNroRadicado(nroRadicado);
//            AgenteDTO agenteDTO = control.consultarRemitenteByNroRadicado(nroRadicado);
        } catch (BusinessException e) {
            assertTrue(e.getCause() instanceof NoResultException);
        } catch (SystemException e) {
            assertTrue(e.getCause() instanceof SystemException);
        }
    }

//    @Test
//    @Transactional
//    public void test_redireccionar_correspondencia_success() {
//        // given
//
//        // when
//        try {
//            boundary.redireccionarCorrespondencia(RedireccionDTO.newInstance().build().setTraza());
//        } catch (SystemException e){
//            assertTrue(e.getCause() instanceof NoResultException);
//        }
//    }

    @Test
    @Transactional
    public void test_redireccionar_correspondencia_failure() throws SystemException, BusinessException {
        // given
        String nroRadicado = "1040TP-CMCOE2017000001";

        // when
        AgenteDTO agenteDTO = boundary.consultarRemitenteByNroRadicado(nroRadicado);

        // then
        assertNotNull(agenteDTO);
    }

}
