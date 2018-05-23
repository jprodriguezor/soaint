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

//    static {
//        System.setProperty(SystemParameters.API_SEARCH_ALFRESCO,"http://192.168.3.245:8080/alfresco/api/-default-/public/search/versions/1/search");
//    }
//    static {
//        System.setProperty(SystemParameters.BUSINESS_PLATFORM_RECORD,"http://192.168.3.245:8080/alfresco/api/-default-/public/gs/versions/1");
//    }
//    static {
//        System.setProperty(SystemParameters.BUSINESS_PLATFORM_PASS,"admin");
//    }
//    static {
//        System.setProperty(SystemParameters.BUSINESS_PLATFORM_USER,"admin");
//    }
//    static {
//        System.setProperty(SystemParameters.BUSINESS_PLATFORM_ENDPOINT,"http://192.168.3.245:8080/alfresco/api/-default-/public/cmis/versions/1.1/atom");
//    }
    @Autowired
    private IRecordServices recordServices;
    @Autowired
    private ContentControl contentControl;
    @Autowired
    private ContentControlAlfresco contentControlAlfresco;

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


    UnidadDocumentalDTO unidadDocumentalDTO;
    MensajeRespuesta mensajeRespuesta;
    MensajeRespuesta mensajeRespuestaUnidadDocumentalContent;

    @Before
    public void setUp() throws Exception {




//Se llenan los datos de la unidad documental
        unidadDocumentalDTO = new UnidadDocumentalDTO();

        unidadDocumentalDTO.setInactivo(true);
        //Calendar calendar
        Calendar gregorianCalendar = GregorianCalendar.getInstance();
        unidadDocumentalDTO.setFechaCierre(gregorianCalendar);
        unidadDocumentalDTO.setId("111122223333");
        unidadDocumentalDTO.setFechaExtremaInicial(gregorianCalendar);
        unidadDocumentalDTO.setSoporte("electronico");
        unidadDocumentalDTO.setNombreUnidadDocumental("RecordFolderTest");
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

//        MensajeRespuesta mensajeRespuesta = contentControlAlfresco.
//                crearUnidadDocumental(unidadDocumentalDTO, conexion.getSession());
//
//        unidadDocumentalDTOResultante = (UnidadDocumentalDTO) mensajeRespuesta.getResponse().get("unidadDocumental");

    }

    @After
    public void tearDown() throws Exception {
        if (null!=mensajeRespuestaUnidadDocumentalContent){
            UnidadDocumentalDTO unidadDocumentalDTO1=(UnidadDocumentalDTO)mensajeRespuestaUnidadDocumentalContent.getResponse().get("unidadDocumental");
        contentControlAlfresco.eliminarUnidadDocumental(unidadDocumentalDTO1.getId(), contentControl.obtenerConexion().getSession());
        }

    }

    @Test
    public void crearEstructuraRecord() {
    }

    @Test
    public void crearCarpetaRecord() {
        try {
            UnidadDocumentalDTO unidadDocumentalDTOT=new UnidadDocumentalDTO();

            unidadDocumentalDTOT.setInactivo(true);
            //Calendar calendar
            Calendar gregorianCalendar = GregorianCalendar.getInstance();
            unidadDocumentalDTOT.setFechaCierre(gregorianCalendar);
            unidadDocumentalDTOT.setId("111122223333");
            unidadDocumentalDTOT.setFechaExtremaInicial(gregorianCalendar);
            unidadDocumentalDTOT.setSoporte("electronico");
            unidadDocumentalDTOT.setNombreUnidadDocumental("RecordFolderTest1");
            unidadDocumentalDTOT.setFechaExtremaFinal(gregorianCalendar);
            unidadDocumentalDTOT.setCerrada(false);
            unidadDocumentalDTOT.setCodigoSubSerie("02312");
            unidadDocumentalDTOT.setCodigoSerie("0231");
            unidadDocumentalDTOT.setCodigoDependencia("10001040");
            unidadDocumentalDTOT.setDescriptor1("3434");
            unidadDocumentalDTOT.setDescriptor2("454545");
            unidadDocumentalDTOT.setAccion("ABRIR");
            unidadDocumentalDTOT.setInactivo(false);
            unidadDocumentalDTOT.setCerrada(false);
            unidadDocumentalDTOT.setEstado("Abierto");
            unidadDocumentalDTOT.setDisposicion("Eliminar");

            MensajeRespuesta mensajeRespuestaT = contentControlAlfresco.
                crearUnidadDocumental(unidadDocumentalDTO, contentControl.obtenerConexion().getSession());

        UnidadDocumentalDTO unidadDocumentalDTOTR = (UnidadDocumentalDTO) mensajeRespuestaT.getResponse().get("unidadDocumental");
            mensajeRespuesta= recordServices.crearCarpetaRecord(unidadDocumentalDTOTR.getId());

            assertEquals("0000",mensajeRespuesta.getCodMensaje());

            contentControlAlfresco.eliminarUnidadDocumental(unidadDocumentalDTOTR.getId(), contentControl.obtenerConexion().getSession());

        } catch (SystemException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGestionarUnidadDocumentalECMNoIdUnidadDocumentalFail() {
        UnidadDocumentalDTO unidadDocumentalDTOTest = new UnidadDocumentalDTO();
        try {
            recordServices.gestionarUnidadDocumentalECM(unidadDocumentalDTOTest);
        } catch (SystemException e) {
            assertEquals("No se ha especificado el Id de la Unidad Documental",e.getMessage());
        }
    }
    @Test
    public void testGestionarUnidadDocumentalECMNoAccionUnidadDocumentalFail() {
        UnidadDocumentalDTO unidadDocumentalDTOTest = new UnidadDocumentalDTO();
        unidadDocumentalDTOTest.setId("112233");
        try {
            recordServices.gestionarUnidadDocumentalECM(unidadDocumentalDTOTest);
        } catch (SystemException e) {
            assertEquals("No se ha especificado la accion a realizar",e.getMessage());
        }
    }
    @Test
    public void testGestionarUnidadDocumentalECMAbrirUnidadDocumentalSuccess() {
        unidadDocumentalDTO.setAccion("ABRIR");
        try {
          assertEquals("0000", recordServices.gestionarUnidadDocumentalECM(unidadDocumentalDTO).getCodMensaje());
        } catch (SystemException e) {

        }
    }

    @Test
    public void gestionarUnidadesDocumentalesECM() {
    }

    @Test
    public void testGetRecordFolderByUdIdSuccess() {
        final Optional<Folder> optionalDocumentalDTO;
        try {


            mensajeRespuestaUnidadDocumentalContent= contentControl.crearUnidadDocumental(unidadDocumentalDTO,contentControl.obtenerConexion().getSession());
            UnidadDocumentalDTO unidadDocumentalDTOTest = (UnidadDocumentalDTO)mensajeRespuestaUnidadDocumentalContent.getResponse().get("unidadDocumental");
            unidadDocumentalDTOTest.setAccion("CERRAR");
            recordServices.gestionarUnidadDocumentalECM(unidadDocumentalDTOTest);

            optionalDocumentalDTO = recordServices.getRecordFolderByUdId(unidadDocumentalDTOTest.getId());
            optionalDocumentalDTO.ifPresent(unidadDocumentalDTO1 ->
                    assertNotNull(unidadDocumentalDTO1.getId()));
        } catch (SystemException e) {
            e.printStackTrace();
        }



    }
}