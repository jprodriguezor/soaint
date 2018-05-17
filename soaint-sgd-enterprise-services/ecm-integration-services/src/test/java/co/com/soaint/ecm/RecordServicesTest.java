package co.com.soaint.ecm;

import co.com.soaint.ecm.business.boundary.documentmanager.ContentControlAlfresco;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentControl;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.IRecordServices;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.impl.RecordServices;
import co.com.soaint.ecm.domain.entity.Conexion;
import co.com.soaint.ecm.util.SystemParameters;
import co.com.soaint.foundation.canonical.ecm.EntradaRecordDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.canonical.ecm.UnidadDocumentalDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
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
import java.util.*;

import static co.com.soaint.ecm.business.boundary.documentmanager.interfaces.IRecordServices.RMA_IS_CLOSED;
import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/core-config.xml"})
public class RecordServicesTest {

    @Autowired
    private IRecordServices recordServices;
    @Autowired
    private ContentControlAlfresco contentControlAlfresco;

    private Conexion conexion;
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
    private String urLecm = System.getProperty("URLecm");
    @Before
    public void setUp() throws Exception {

        conexion = new Conexion();
        //
        //crear conexion


        //Inicializar coneccion
        Map<String, String> parameter = new HashMap<>();
        // Credenciales del usuario
        parameter.put(SessionParameter.USER, "admin");
        parameter.put(SessionParameter.PASSWORD, "admin");

        // configuracion de conexion
        parameter.put(SessionParameter.ATOMPUB_URL, urLecm);
        parameter.put(SessionParameter.ATOMPUB_URL, "http://192.168.3.245:8080/alfresco/api/-default-/public/cmis/versions/1.1/atom");
        parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        parameter.put(SessionParameter.REPOSITORY_ID, "-default-");

        // Object factory de Alfresco
        parameter.put(SessionParameter.OBJECT_FACTORY_CLASS, "org.alfresco.cmis.client.impl.AlfrescoObjectFactoryImpl");

        // Crear Sesion
        SessionFactory factory = SessionFactoryImpl.newInstance();
        conexion.setSession(factory.getRepositories(parameter).get(0).createSession());


//Se llenan los datos de la unidad documental
        unidadDocumentalDTO = new UnidadDocumentalDTO();
        unidadDocumentalDTO.setInactivo(true);
        //Calendar calendar
        Calendar gregorianCalendar = GregorianCalendar.getInstance();
        unidadDocumentalDTO.setFechaCierre(gregorianCalendar);
        unidadDocumentalDTO.setId("1118");
        unidadDocumentalDTO.setFechaExtremaInicial(gregorianCalendar);
        unidadDocumentalDTO.setSoporte("electronico");
        unidadDocumentalDTO.setNombreUnidadDocumental("UnidadDocumentalTest");
        unidadDocumentalDTO.setFechaExtremaFinal(gregorianCalendar);
        unidadDocumentalDTO.setCerrada(true);
        unidadDocumentalDTO.setCodigoSubSerie("02312");
        unidadDocumentalDTO.setCodigoSerie("0231");
        unidadDocumentalDTO.setCodigoDependencia("10001040");
        unidadDocumentalDTO.setDescriptor1("3434");
        unidadDocumentalDTO.setDescriptor2("454545");
        unidadDocumentalDTO.setAccion("ABRIR");
        unidadDocumentalDTO.setInactivo(false);
        unidadDocumentalDTO.setCerrada(false);
        unidadDocumentalDTO.setEstado("Abierto");
        unidadDocumentalDTO.setDisposicion("Eliminar");

        MensajeRespuesta mensajeRespuesta = contentControlAlfresco.
                crearUnidadDocumental(unidadDocumentalDTO, conexion.getSession());

         unidadDocumentalDTO = (UnidadDocumentalDTO) mensajeRespuesta.getResponse().get("unidadDocumental");

    }

    @After
    public void tearDown() throws Exception {
        contentControlAlfresco.eliminarUnidadDocumental(unidadDocumentalDTO.getId(), conexion.getSession());
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
        EntradaRecordDTO entradaRecordDTO = new EntradaRecordDTO();
        try {
            entradaRecordDTO.setDependencia("1000.1040_GERENCIA NACIONAL DE GESTION DOCUMENTAL");
            entradaRecordDTO.setSede("1000_VICEPRESIDENCIA");
            entradaRecordDTO.setNombreCarpeta("RecordFolderTest");
            entradaRecordDTO.setSerie("0231");
            entradaRecordDTO.setSubSerie("02312");

            recordServices.crearCarpetaRecord(entradaRecordDTO);
        } catch (SystemException e) {
            e.printStackTrace();
        }
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