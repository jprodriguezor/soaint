package co.com.soaint.ecm;

import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentControl;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.IRecordServices;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.impl.RecordServices;
import co.com.soaint.ecm.util.SystemParameters;
import co.com.soaint.foundation.canonical.ecm.UnidadDocumentalDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static co.com.soaint.ecm.business.boundary.documentmanager.interfaces.IRecordServices.RMA_IS_CLOSED;
import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/core-config.xml"})
public class RecordServicesTest {

    @Autowired
    private IRecordServices recordServices;
    @Autowired
    private ContentControl contentControl;
    String idSubCategoria = "";
    private String idPadre = "";
    @Value("${protocolo}")
    private String protocolo = "";
    @Value("${mensaje.error.sistema}")
    private String errorSistema = "";
    @Value("${mensaje.error.sistema.generico}")
    private String errorSistemaGenerico = "";
    @Value("${header.accept}")
    private String headerAccept = "";
    @Value("${header.authorization}")
    private String headerAuthorization = "";
    @Value("${header.value.application.type}")
    private String valueApplicationType = "";
    @Value("${header.value.authorization}")
    private String valueAuthorization = "";
    @Value("${mensaje.error.negocio.fallo}")
    private String errorNegocioFallo = "";
    @Value("${tokken.record}")
    private String encoding = "";
    @Value("${nodeType}")
    private String tipoNodo = "";
    @Value("${recordCategory}")
    private String recordCategoria = "";
    @Value("${tag.propiedades}")
    private String tagPropiedades = "";
    @Value("${recordCarpeta}")
    private String recordCarpeta = "";
    @Value("${id.sitio.record.manager}")
    private String idRecordManager = "";
    @Value("${api.service.alfresco}")
    private String apiServiceAlfresco = "";
    @Value("${tag.nombre}")
    private String nombre = "";
    @Value("${tag.descripcion}")
    private String descripcion = "";
    @Value("${tag.periodo}")
    private String periodo = "";
    @Value("${tag.localizacion}")
    private String localizacion = "";
    @Value("${tag.propiedad.periodo}")
    private String propiedadPeriodo = "";
    @Value("${tag.evento.completar}")
    private String eventoCompletar = "";
    @Value("${tag.evento}")
    private String evento = "";
    @Value("${valor.periodo}")
    private String valorPeriodo = "";
    @Value("${valor.mensaje.descripcion}")
    private String mensajeDescripcion = "";

    private String codigoOrgAUX = " ";
    private String idSerie = "";
    private Map<String, String> codigosRecord = new HashMap<>();
    private Map<String, String> propiedades = new HashMap<>();
    private Map<String, String> codigosSubseries = new HashMap<>();
    private Map<String, Object> disposicion = new HashMap<>();

    UnidadDocumentalDTO unidadDocumentalDTO =new UnidadDocumentalDTO();

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

        //Eliminar Record Folder
            WebTarget wt = ClientBuilder.newClient().target(SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_RECORD));
            wt.path("/record-folders/" + unidadDocumentalDTO.getId())
                    .request()
                    .header(headerAuthorization, valueAuthorization + " " + encoding)
                    .header(headerAccept, valueApplicationType)
                    .delete();
    }

    @Test
    public void crearEstructuraRecord() {
    }

    @Test
    public void crearCarpetaRecord() {

    }

    @Test
    public void gestionarUnidadDocumentalECM() {
    }

    @Test
    public void gestionarUnidadesDocumentalesECM() {
    }

    @Test
    public void obtenerRecordFolder() {
        final Optional<Folder> optionalDocumentalDTO;
        try {
            optionalDocumentalDTO = recordServices.obtenerRecordFolder(unidadDocumentalDTO.getId());
            optionalDocumentalDTO.ifPresent(unidadDocumentalDTO1 ->
                    assertNotNull(unidadDocumentalDTO1.getId()));
        } catch (SystemException e) {
            e.printStackTrace();
        }



    }
}