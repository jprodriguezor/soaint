package co.com.soaint.correspondencia;

import co.com.soaint.correspondencia.business.control.AgenteControl;
import co.com.soaint.foundation.canonical.correspondencia.AgenteDTO;
import co.com.soaint.foundation.canonical.correspondencia.AgenteFullDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by yleon on 08/05/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"/META-INF/core-config.xml"}
)
@Log4j2
public class AgenteControlTest extends JPAHibernateContextTest{

    @Autowired
    AgenteControl agenteControl;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void listarRemitentesByIdeDocumento() throws Exception {

        //given
        BigInteger ideDocumento = BigInteger.valueOf(836);

        //when
        List<AgenteDTO> agentes = agenteControl.listarRemitentesByIdeDocumento(ideDocumento);

        //then
        assertNotNull(agentes);
        assertNotEquals(0, agentes.size());
    }

    @Test
    public void listarRemitentesFullByIdeDocumento() throws Exception {
        //given
        BigInteger ideDocumento = BigInteger.valueOf(836);

        //when
        List<AgenteFullDTO> agentes = agenteControl.listarRemitentesFullByIdeDocumento(ideDocumento);

        //then
        assertNotNull(agentes);
        assertNotEquals(0, agentes.size());
        for (AgenteFullDTO agt :
                agentes) {
            assertNotNull(agt.getCodTipoRemite());
            assertNotNull(agt.getDescTipoRemite());
            assertNotNull(agt.getCodTipAgent());
            assertNotNull(agt.getDescTipAgent());
        }
    }

    @Test
    public void listarDestinatariosByIdeDocumentoAndCodDependenciaAndCodEstado() throws Exception {
        //given
        BigInteger ideDocumento = BigInteger.valueOf(837);
        String codDependencia = "CD";
        String codEstado = "AS";

        //when
        List<AgenteDTO> agentes = agenteControl.listarDestinatariosByIdeDocumentoAndCodDependenciaAndCodEstado(ideDocumento, codDependencia, codEstado);

        //then
        assertNotNull(agentes);
        assertNotEquals(0, agentes.size());
        for (AgenteDTO agt :
                agentes) {
            assertEquals(codDependencia, agt.getCodDependencia());
            assertEquals(codEstado, agt.getCodEstado());
        }
    }

    @Test
    public void listarDestinatariosByIdeDocumentoAndCodDependencia() throws Exception {
        //given
        BigInteger ideDocumento = BigInteger.valueOf(837);
        String codDependencia = "CD";

        //when
        List<AgenteDTO> agentes = agenteControl.listarDestinatariosByIdeDocumentoAndCodDependencia(ideDocumento, codDependencia);

        //then
        assertNotNull(agentes);
        assertNotEquals(0, agentes.size());
        for (AgenteDTO agt :
                agentes) {
            assertEquals(codDependencia, agt.getCodDependencia());
        }
    }

    @Test
    public void listarDestinatariosByIdeDocumento() throws Exception {
        //given
        BigInteger ideDocumento = BigInteger.valueOf(837);

        //when
        List<AgenteDTO> agentes = agenteControl.listarDestinatariosByIdeDocumento(ideDocumento);

        //then
        assertNotNull(agentes);
        assertNotEquals(0, agentes.size());
    }

    @Test
    public void consultarAgenteByIdeAgente() throws Exception {
        // given
        BigInteger idAgente = BigInteger.valueOf(100);
        //when
        AgenteDTO agente = agenteControl.consultarAgenteByIdeAgente(idAgente);

        //then
        assertNotNull(agente);
        assertEquals(idAgente, agente.getIdeAgente());
    }

    @Test
    public void verificarByIdeAgente() throws Exception {
        // given
        BigInteger idAgente = BigInteger.valueOf(200);
        //when
        Boolean agente = agenteControl.verificarByIdeAgente(idAgente);

        //then
        assertNotNull(agente);
        assertTrue(agente);

    }

    @Test
    public void asignarDatosContacto() throws Exception {
    }

    @Test
    public void consltarAgentesByCorrespondencia() throws Exception {
    }

    @Test
    public void agenteTransformToFull() throws Exception {
    }

    @Test
    public void agenteListTransformToFull() throws Exception {
    }

    @Test
    public void consultarAgentesFullByCorrespondencia() throws Exception {
    }

    @Test
    public void conformarAgentes() throws Exception {
    }

    @Test
    public void conformarAgentesSalida() throws Exception {
    }

    @Test
    public void actualizarEstadoDistribucion() throws Exception {
    }

    @Test
    public void corAgenteTransform() throws Exception {
    }

    @Test
    public void actualizarDestinatario() throws Exception {
    }

    @Test
    public void actualizarRemitente() throws Exception {
    }

    @Test
    public void consultarRemitenteByNroRadicado() throws Exception {
    }

}