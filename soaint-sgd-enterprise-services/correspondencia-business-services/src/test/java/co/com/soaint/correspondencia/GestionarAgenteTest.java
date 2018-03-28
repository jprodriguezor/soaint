package co.com.soaint.correspondencia;

import co.com.soaint.correspondencia.business.boundary.GestionarAgente;
import co.com.soaint.correspondencia.business.control.AgenteControl;
import co.com.soaint.correspondencia.business.control.PpdTrazDocumentoControl;
import co.com.soaint.foundation.canonical.correspondencia.AgenteDTO;
import co.com.soaint.foundation.canonical.correspondencia.PpdTrazDocumentoDTO;
import co.com.soaint.foundation.canonical.correspondencia.RedireccionDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import co.com.soaint.foundation.canonical.correspondencia.constantes.EstadoAgenteEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by eric.rodriguez on 14/02/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/META-INF/core-config.xml"})
@Log4j2
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
            log.error("GestionarAgenteTest - a business error has occurred", e);
        } catch (SystemException e) {
            assertTrue(e.getCause() instanceof SystemException);
            log.error("GestionarAgenteTest - a system error has occurred", e);
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
        } catch (BusinessException e) {
            assertTrue(e.getCause() instanceof NoResultException);
            log.error("GestionarAgenteTest - a business error has occurred", e);
        } catch (SystemException e) {
            assertTrue(e.getCause() instanceof SystemException);
            log.error("GestionarAgenteTest - a business error has occurred", e);
        }
    }

    @Test
    @Transactional
    public void test_redireccionar_correspondencia_success() {
        // given
        List<AgenteDTO> agenteDTOList = new ArrayList<>();
        agenteDTOList.add(AgenteDTO.newInstance()
                .ideAgente(new BigInteger("100"))
                .codEstado(EstadoAgenteEnum.DEVUELTO.getCodigo())
                .build());
        agenteDTOList.add(AgenteDTO.newInstance()
                .ideAgente(new BigInteger("200"))
                .codEstado(EstadoAgenteEnum.DEVUELTO.getCodigo())
                .build());
        PpdTrazDocumentoDTO ppdTrazaDocumento = PpdTrazDocumentoDTO.newInstance()
                .ideDocumento(new BigInteger("836"))
                .ideTrazDocumento(new BigInteger("100"))
                .ideFunci(new BigInteger("2"))
                .codEstado("")
                .observacion("Nueva observacion")
                .codOrgaAdmin("10120140")
                .build();
        // when
        try {
            boundary.redireccionarCorrespondencia(RedireccionDTO.newInstance()
                    .agentes(agenteDTOList)
                    .traza(ppdTrazaDocumento)
                    .build());
        } catch (SystemException e){
//            assertTrue(e.getCause() instanceof NoResultException);
            log.error("GestionarAgenteTest - a system error has occurred", e);
        }
    }

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
