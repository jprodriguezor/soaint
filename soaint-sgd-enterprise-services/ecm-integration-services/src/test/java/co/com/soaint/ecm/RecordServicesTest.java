package co.com.soaint.ecm;

import co.com.soaint.ecm.business.boundary.documentmanager.ContentControlAlfresco;
import co.com.soaint.ecm.business.boundary.documentmanager.ECMConnectionRule;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentControl;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.IRecordServices;
import co.com.soaint.foundation.canonical.ecm.*;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.enums.UnfileObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/core-config.xml"})
public class RecordServicesTest {


    @Rule
    public ECMConnectionRule localPropertiesRule = new ECMConnectionRule();

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


    }

    @After
    public void tearDown() throws Exception {
        if (null != mensajeRespuestaUnidadDocumentalContent) {
            UnidadDocumentalDTO unidadDocumentalDTO1 = (UnidadDocumentalDTO) mensajeRespuestaUnidadDocumentalContent.getResponse().get("unidadDocumental");
            contentControlAlfresco.eliminarUnidadDocumental(unidadDocumentalDTO1.getId(), contentControl.obtenerConexion().getSession());
        }

    }


    @Test
    public void testcrearCarpetaRecordSuccess() {
        try {
            UnidadDocumentalDTO unidadDocumentalDTOT = new UnidadDocumentalDTO();

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
            mensajeRespuesta = recordServices.crearCarpetaRecord(unidadDocumentalDTOTR.getId());

            assertEquals("0000", mensajeRespuesta.getCodMensaje());

            contentControlAlfresco.eliminarUnidadDocumental(unidadDocumentalDTOTR.getId(), contentControl.obtenerConexion().getSession());

        } catch (SystemException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testcrearCarpetaRecordIdUDFail() {
        try {
            UnidadDocumentalDTO unidadDocumentalDTOT = new UnidadDocumentalDTO();

            mensajeRespuesta = recordServices.crearCarpetaRecord(unidadDocumentalDTOT.getId());

            assertEquals("1224", mensajeRespuesta.getCodMensaje());


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
            assertEquals("No se ha especificado el Id de la Unidad Documental", e.getMessage());
        }
    }

    @Test
    public void testGestionarUnidadDocumentalECMNoAccionUnidadDocumentalFail() {
        UnidadDocumentalDTO unidadDocumentalDTOTest = new UnidadDocumentalDTO();
        unidadDocumentalDTOTest.setId("112233");
        try {
            recordServices.gestionarUnidadDocumentalECM(unidadDocumentalDTOTest);
        } catch (SystemException e) {
            assertEquals("No se ha especificado la accion a realizar", e.getMessage());
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
    public void testGestionarUnidadDocumentalECMCerrarUnidadDocumentalSuccess() {
        try {
            //Crear el objeto documento
            DocumentoDTO documentoDTO = localPropertiesRule.newDocumento("DocTestJUnittestObtenerDocumentosArchivadosSuccess");

            //Subo el documento a una UD temporal
            MensajeRespuesta mensajeRespuesta = contentControlAlfresco.subirDocumentoTemporalUD(documentoDTO, contentControl.obtenerConexion().getSession());

            //Obtengo el id del documento subido
            documentoDTO = (DocumentoDTO) mensajeRespuesta.getResponse().get("documento");

            //Creo el objeto de la unidad documental a cerrar
            UnidadDocumentalDTO unidadDocumentalDTO = localPropertiesRule.newUnidadDocumental();
            MensajeRespuesta mensajeRespuesta1 = contentControlAlfresco.crearUnidadDocumental(unidadDocumentalDTO, contentControl.obtenerConexion().getSession());

            unidadDocumentalDTO = (UnidadDocumentalDTO) mensajeRespuesta1.getResponse().get("unidadDocumental");
            List<DocumentoDTO> documentoDTOList = new ArrayList<>();
            documentoDTOList.add(documentoDTO);
            unidadDocumentalDTO.setListaDocumentos(documentoDTOList);
            //Subo el documento a la unidad documental que voy a cerrar
            contentControlAlfresco.subirDocumentosUnidadDocumentalECM(unidadDocumentalDTO, contentControl.obtenerConexion().getSession());

            //Procedo a cerrar la unidad documental para que ademas cree el record
            unidadDocumentalDTO.setAccion("CERRAR");

            assertEquals("0000", recordServices.gestionarUnidadDocumentalECM(unidadDocumentalDTO).getCodMensaje());

            contentControlAlfresco.eliminarUnidadDocumental(unidadDocumentalDTO.getId(), contentControl.obtenerConexion().getSession());


//            contentControlAlfresco.obtenerDocumentosArchivados("", contentControl.obtenerConexion().getSession());
        } catch (Exception e) {
            assertEquals("No se ha especificado el codigo de la dependencia", e.getMessage());
        }
    }

    @Test
    public void testGestionarUnidadesDocumentalesECMSuccess() {
        unidadDocumentalDTO.setAccion("ABRIR");
        try {
            List<UnidadDocumentalDTO> unidadDocumentalDTOList = new ArrayList<>();
            unidadDocumentalDTOList.add(unidadDocumentalDTO);
            assertEquals("0000", recordServices.gestionarUnidadesDocumentalesECM(unidadDocumentalDTOList).getCodMensaje());
        } catch (SystemException e) {

        }
    }

    @Test
    public void testGetRecordFolderByUdIdSuccess() {
        final Optional<Folder> optionalDocumentalDTO;
        try {


            mensajeRespuestaUnidadDocumentalContent = contentControl.crearUnidadDocumental(unidadDocumentalDTO, contentControl.obtenerConexion().getSession());
            UnidadDocumentalDTO unidadDocumentalDTOTest = (UnidadDocumentalDTO) mensajeRespuestaUnidadDocumentalContent.getResponse().get("unidadDocumental");
            unidadDocumentalDTOTest.setAccion("CERRAR");
            recordServices.gestionarUnidadDocumentalECM(unidadDocumentalDTOTest);

            optionalDocumentalDTO = recordServices.getRecordFolderByUdId(unidadDocumentalDTOTest.getId());
            optionalDocumentalDTO.ifPresent(unidadDocumentalDTO1 ->
                    assertNotNull(unidadDocumentalDTO1.getId()));
        } catch (SystemException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReactivarUDSuccess() {
        final Optional<Folder> optionalDocumentalDTO;
        try {


            mensajeRespuestaUnidadDocumentalContent = contentControl.crearUnidadDocumental(unidadDocumentalDTO, contentControl.obtenerConexion().getSession());
            UnidadDocumentalDTO unidadDocumentalDTOTest = (UnidadDocumentalDTO) mensajeRespuestaUnidadDocumentalContent.getResponse().get("unidadDocumental");
            unidadDocumentalDTOTest.setAccion("CERRAR");
            recordServices.gestionarUnidadDocumentalECM(unidadDocumentalDTOTest);
            unidadDocumentalDTOTest.setAccion("REACTIVAR");
            recordServices.gestionarUnidadDocumentalECM(unidadDocumentalDTOTest);

            optionalDocumentalDTO = recordServices.getRecordFolderByUdId(unidadDocumentalDTOTest.getId());
            optionalDocumentalDTO.ifPresent(unidadDocumentalDTO1 ->
                    assertNotNull(unidadDocumentalDTO1.getId()));
        } catch (SystemException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testcrearEstructuraRecordSuccess() {
        List<EstructuraTrdDTO> structure = new ArrayList<>();
        List<OrganigramaDTO> organigramaItemList1 = new ArrayList<>();
        List<ContenidoDependenciaTrdDTO> contenidoDependenciaList1 = new ArrayList<>();

        ContenidoDependenciaTrdDTO contenidoDependenciaTrdDTO1 = new ContenidoDependenciaTrdDTO();
        contenidoDependenciaTrdDTO1.setCodSerie("0025633");
        contenidoDependenciaTrdDTO1.setCodSubSerie("");
        contenidoDependenciaTrdDTO1.setDiposicionFinal(1);
        contenidoDependenciaTrdDTO1.setIdOrgAdm("900");
        contenidoDependenciaTrdDTO1.setIdOrgOfc("900910");
        contenidoDependenciaTrdDTO1.setNomSerie("CONTRATOS");
        contenidoDependenciaTrdDTO1.setNomSubSerie("");
        contenidoDependenciaTrdDTO1.setProcedimiento("Se deben conservar los documentos");
        contenidoDependenciaTrdDTO1.setRetArchivoCentral(7L);
        contenidoDependenciaTrdDTO1.setRetArchivoGestion(2L);

        contenidoDependenciaList1.add(contenidoDependenciaTrdDTO1);

        ContenidoDependenciaTrdDTO contenidoDependenciaTrdDTO2 = new ContenidoDependenciaTrdDTO();
        contenidoDependenciaTrdDTO2.setCodSerie("100");
        contenidoDependenciaTrdDTO2.setCodSubSerie("1");
        contenidoDependenciaTrdDTO2.setDiposicionFinal(2);
        contenidoDependenciaTrdDTO2.setIdOrgAdm("900");
        contenidoDependenciaTrdDTO2.setIdOrgOfc("900910");
        contenidoDependenciaTrdDTO2.setNomSerie("Créditos");
        contenidoDependenciaTrdDTO2.setNomSubSerie("Créditos Hipotecarios");
        contenidoDependenciaTrdDTO2.setProcedimiento("Eliminar");
        contenidoDependenciaTrdDTO2.setRetArchivoCentral(6L);
        contenidoDependenciaTrdDTO2.setRetArchivoGestion(6L);

        contenidoDependenciaList1.add(contenidoDependenciaTrdDTO2);

        OrganigramaDTO organigramaDTO1 = new OrganigramaDTO();
        organigramaDTO1.setCodOrg("000");
        organigramaDTO1.setIdeOrgaAdmin(46L);
        organigramaDTO1.setNomOrg("000_SOAINT1");
        organigramaDTO1.setTipo("P");
        organigramaItemList1.add(organigramaDTO1);

        OrganigramaDTO organigramaDTO2 = new OrganigramaDTO();
        organigramaDTO2.setCodOrg("900");
        organigramaDTO2.setIdeOrgaAdmin(77L);
        organigramaDTO2.setNomOrg("900_VICEPRESIDENCIA DE TALENTO HUMANO");
        organigramaDTO2.setTipo("H");
        organigramaItemList1.add(organigramaDTO2);

        OrganigramaDTO organigramaDTO3 = new OrganigramaDTO();
        organigramaDTO3.setCodOrg("900910");
        organigramaDTO3.setIdeOrgaAdmin(78L);
        organigramaDTO3.setNomOrg("900.910_GERENCIA NACIONAL DE GESTIÓN DEL TALENTO HUMANO");
        organigramaDTO3.setTipo("H");
        organigramaItemList1.add(organigramaDTO3);

        EstructuraTrdDTO estuEstructuraTrdDTO = new EstructuraTrdDTO();
        estuEstructuraTrdDTO.setContenidoDependenciaList(contenidoDependenciaList1);
        estuEstructuraTrdDTO.setOrganigramaItemList(organigramaItemList1);
        structure.add(estuEstructuraTrdDTO);

        try {

            assertEquals("0000", recordServices.crearEstructuraEcm(structure).getCodMensaje());

            Session session = localPropertiesRule.newConexion().getSession();

            final String query = "SELECT * FROM cmis:folder where cmis:name = '000_SOAINT1'";
            final ItemIterable<QueryResult> queryResults = session.query(query, false);

            queryResults.forEach(queryResult -> {
                String objectId = queryResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
                Folder folder = (Folder) session.getObject(objectId);
                folder.deleteTree(true, UnfileObject.DELETE, true);
            });
        } catch (SystemException e1) {
            e1.printStackTrace();
        }
    }

}