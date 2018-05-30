package co.com.soaint.ecm;

import co.com.soaint.ecm.business.boundary.documentmanager.ContentManager;
import co.com.soaint.ecm.business.boundary.documentmanager.ECMConnectionRule;
import co.com.soaint.foundation.canonical.ecm.DocumentoDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/core-config.xml"})
public class ContentManagerTest {

    @Autowired
    private ContentManager contentManager;

    private static final Logger logger = LogManager.getLogger(ContentManager.class.getName());

    @Rule
    public ECMConnectionRule connectionRule = new ECMConnectionRule();

    @Before
    public void Setup() {
        connectionRule.usingContentManager(contentManager);
        // DocTestJUnit, TestMetodoSubirDoc1, TestMetodoSubirDoc2


    }

    @Test
    public void init() {
    }

    @Test
    public void crearEstructuraContent() {
    }

    @Test
    public void testsubirDocumentoPrincipalAdjuntoContentSuccess() {
        //Probar que sube documento EE correctemante
        DocumentoDTO documentoDTO = connectionRule.newDocumento("TestMetodoSubirDoc1");
        try {
            MensajeRespuesta mensajeRespuesta1 = contentManager.subirDocumentoPrincipalAdjuntoContent(documentoDTO, "EE");
            assertEquals("0000", mensajeRespuesta1.getCodMensaje());

            contentManager.eliminarDocumento(mensajeRespuesta1.getDocumentoDTOList().get(0).getIdDocumento());
        } catch (SystemException e) {
            logger.error("Error SystemException: {}", e);
        }
    }



    @Test
    public void subirVersionarDocumentoGeneradoContent() {
    }

    @Test
    public void obtenerDocumentosAdjuntosContent() {
    }

    @Test
    public void obtenerVersionesDocumentoContent() {
    }

    @Test
    public void modificarMetadatoDocumentoContent() {
    }

    @Test
    public void moverDocumento() {
    }

    @Test
    public void descargarDocumentoContent() {
    }

    @Test
    public void eliminarDocumento() {
    }

    @Test
    public void devolverSeriesSubseries() {
    }

    @Test
    public void crearUnidadDocumental() {
    }

    @Test
    public void listarUnidadDocumental() {
    }

    @Test
    public void obtenerDetallesDocumentoDTO() {
    }

    @Test
    public void detallesUnidadDocumental() {
    }

    @Test
    public void subirDocumentosUnidadDocumentalECM() {
    }

    @Test
    public void getDocumentosPorArchivar() {
    }

    @Test
    public void modificarUnidadesDocumentales() {
    }

    @Test
    public void subirDocumentosTemporalesUD() {
    }

    @Test
    public void obtenerDocumentosArchivados() {
    }

    @Test
    public void listarDependenciaMultiple() {
    }

    @Test
    public void listarUdDisposicionFinal() {
    }

    @Test
    public void aprobarRechazarDisposicionesFinales() {
    }

    @Test
    public void subirDocumentoTemporalUD() {
    }

    @Test
    public void estamparEtiquetaRadicacion() {
    }

    @Test
    public void subirDocumentoAnexo() {
    }

    @Test
    public void crearLinkContent() {
    }
}