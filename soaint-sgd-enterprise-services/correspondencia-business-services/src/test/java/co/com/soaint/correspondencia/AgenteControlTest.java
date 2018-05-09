package co.com.soaint.correspondencia;

import co.com.soaint.correspondencia.business.control.AgenteControl;
import co.com.soaint.foundation.canonical.correspondencia.AgenteDTO;
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

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

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
    }

    @Test
    public void listarDestinatariosByIdeDocumentoAndCodDependenciaAndCodEstado() throws Exception {
    }

    @Test
    public void listarDestinatariosByIdeDocumentoAndCodDependencia() throws Exception {
    }

    @Test
    public void listarDestinatariosByIdeDocumento() throws Exception {
    }

    @Test
    public void consultarAgenteByIdeAgente() throws Exception {
    }

    @Test
    public void actualizarEstadoAgente() throws Exception {
    }

    @Test
    public void redireccionarCorrespondencia() throws Exception {
    }

    @Test
    public void devolverCorrespondencia() throws Exception {
    }

    @Test
    public void verificarByIdeAgente() throws Exception {
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