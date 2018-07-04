package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.*;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.canonical.correspondencia.constantes.EstadoAgenteEnum;
import co.com.soaint.foundation.canonical.correspondencia.constantes.EstadoCorrespondenciaEnum;
import co.com.soaint.foundation.canonical.correspondencia.constantes.TipoAgenteEnum;
import co.com.soaint.foundation.canonical.correspondencia.constantes.TipoRemitenteEnum;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * Created by gyanet on 15/06/2018.
 */

public class CorrespondenciaControlTest {

    @Rule
    public ExpectedException fails = ExpectedException.none();
    // Dependencias
    EntityManager em;
    DctAsignacionControl dctAsignacionControl;
    DctAsigUltimoControl dctAsigUltimoControl;
    CorrespondenciaControl correspondenciaControl;
    FuncionariosControl funcionariosControl;
    AgenteControl agenteControl;
    DatosContactoControl datosContactoControl;
    SolicitudUnidadDocumentalControl solicitudUnidadDocumentalControl;
    ReferidoControl referidoControl;
    AnexoControl anexoControl;
    OrganigramaAdministrativoControl organigramaAdministrativoControl;
    PpdDocumentoControl ppdDocumentoControl;
    DserialControl dserialControl;
    private ConstantesControl constanteControl;
    AsignacionControl asignacionControl;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

    @Before
    public void setUp() throws Exception {

        correspondenciaControl = new CorrespondenciaControl();

        em = mock(EntityManager.class);
        constanteControl = mock(ConstantesControl.class);
        dctAsignacionControl = mock(DctAsignacionControl.class);
        dctAsigUltimoControl = mock(DctAsigUltimoControl.class);
        agenteControl = mock(AgenteControl.class);
        funcionariosControl = mock(FuncionariosControl.class);
        datosContactoControl = mock(DatosContactoControl.class);
        solicitudUnidadDocumentalControl = mock(SolicitudUnidadDocumentalControl.class);
        referidoControl = mock(ReferidoControl.class);
        anexoControl = mock(AnexoControl.class);
        organigramaAdministrativoControl = mock(OrganigramaAdministrativoControl.class);
        ppdDocumentoControl = mock(PpdDocumentoControl.class);
        dserialControl = mock(DserialControl.class);
        asignacionControl = mock(AsignacionControl.class);

        ReflectionTestUtils.setField(correspondenciaControl, "em", em);
        ReflectionTestUtils.setField(correspondenciaControl, "agenteControl", agenteControl);
        ReflectionTestUtils.setField(correspondenciaControl, "constanteControl", constanteControl);
        ReflectionTestUtils.setField(correspondenciaControl, "funcionarioControl", funcionariosControl);
        ReflectionTestUtils.setField(correspondenciaControl, "organigramaAdministrativoControl", organigramaAdministrativoControl);
        ReflectionTestUtils.setField(correspondenciaControl, "ppdDocumentoControl", ppdDocumentoControl);
        ReflectionTestUtils.setField(correspondenciaControl, "anexoControl", anexoControl);
        ReflectionTestUtils.setField(correspondenciaControl, "datosContactoControl", datosContactoControl);
        ReflectionTestUtils.setField(correspondenciaControl, "referidoControl", referidoControl);
        ReflectionTestUtils.setField(correspondenciaControl, "dserialControl", dserialControl);
        ReflectionTestUtils.setField(correspondenciaControl, "asignacionControl", asignacionControl);
        ReflectionTestUtils.setField(correspondenciaControl, "solicitudUnidadDocumentalControl", solicitudUnidadDocumentalControl);
        ReflectionTestUtils.setField(correspondenciaControl, "rangoReservado", new String[]{"1000", "9999"});
        ReflectionTestUtils.setField(correspondenciaControl, "horarioLaboral", new String[]{"08:00", "17:00"});
        ReflectionTestUtils.setField(correspondenciaControl, "horarioLaboral", new String[]{"08:00", "17:00"});
        ReflectionTestUtils.setField(correspondenciaControl, "unidadTiempoHoras", "UNID-TIH");
        ReflectionTestUtils.setField(correspondenciaControl, "unidadTiempoDias", "UNID-TID");
//        ReflectionTestUtils.setField(correspondenciaControl, "cantHorasParaActivarAlertaVencimiento", 8);
        ReflectionTestUtils.setField(correspondenciaControl, "diaSiguienteHabil", "DSH");
        ReflectionTestUtils.setField(correspondenciaControl, "diasFestivos", new String[]{"01/01/2018", "08/01/2018", "19/03/2018", "29/03/2018", "30/03/2018",
                                                                                                "01/05/2018", "14/05/2018", "04/06/2018", "11/06/2018", "02/07/2018",
                                                                                                "20/07/2018", "07/08/2018", "20/08/2018", "15/10/2018", "05/11/2018",
                                                                                                "12/11/2018", "08/12/2018", "25/12/2018"});
        ReflectionTestUtils.setField(correspondenciaControl, "reqDistFisica", "1");
    }

    @After
    public void tearDown() throws Exception {
    }

//    private ComunicacionOficialDTO newComunicacionOficialDTO() {
//        ComunicacionOficialDTO comunicacionOficialDTO = newComunicacionOficialDTO();
////        List<AgenteDTO> agenteDTOList = new ArrayList<>();
////        agenteDTOList.add(agenteDTO);
//
////        return AsignacionTramiteDTO.newInstance()
////                .asignaciones(AsignacionesDTO.newInstance()
////                        .asignaciones(asignacionDTOList).build())
////                .build();
//    }

//    private CorrespondenciaDTO CorrespondenciaDTO() {
//        return CorrespondenciaDTO.newInstance()
//                .build();
//    }

    private <T> TypedQuery<T> getSingleResultMock(String namedQuery, Class<T> type, T dummyObject) {
        TypedQuery<T> typedQuery = mock(TypedQuery.class);
        when(em.createNamedQuery(namedQuery,  type)).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(dummyObject);
        return typedQuery;
    }
    private <T> TypedQuery<T> getResultListMock(String namedQuery, Class<T> type, List<T>list) {
        TypedQuery<T> typedQuery = mock(TypedQuery.class);
        when(em.createNamedQuery(namedQuery,  type)).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(list);
        return typedQuery;
    }

    private <T> TypedQuery<T> getExecuteUpdateMock(String namedQuery, Class<T> type) {
        TypedQuery<T> typedQuery = mock(TypedQuery.class);
        if(type == null)
            when(em.createNamedQuery(namedQuery)).thenReturn(typedQuery);
        else
            when(em.createNamedQuery(namedQuery,  type)).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        return typedQuery;
    }

    @Test
    public void radicarCorrespondencia() throws SystemException, BusinessException {
        List<DatosContactoDTO> datosContactoDTOS = new ArrayList<>();
        List<AgenteDTO> agenteDTOS = new ArrayList<>();
        List<PpdDocumentoDTO> ppdDocumentoDTOS = new ArrayList<>();

        Date FECHA = new Date();
        String REQ_DIST_FISICA = "1";
        String COD_FUNC_RADICA = "2";
        String NRO_RADICADO = "1040SE2017000001";
        String COD_SEDE = "1040";
        String COD_TIPO_CMC = "SE";
        String COD_DEPENDENCIA = "10401040";
        BigInteger IDE_DOCUMENTO = new BigInteger("1576");
        CorrespondenciaDTO CORRESPONDENCIA = CorrespondenciaDTO.newInstance()
                .reqDistFisica(REQ_DIST_FISICA)
                .codFuncRadica(COD_FUNC_RADICA)
                .nroRadicado(NRO_RADICADO)
                .codSede(COD_SEDE)
                .codTipoCmc(COD_TIPO_CMC)
                .codDependencia(COD_DEPENDENCIA)
                .ideDocumento(IDE_DOCUMENTO)
                .codEstado(EstadoCorrespondenciaEnum.RADICADO.getCodigo())
                .build();
        List<AgenteDTO> AGENTE_LIST = Arrays.asList(
                AgenteDTO.newInstance().build()
        );
        List<DatosContactoDTO> DATOS_CONTACTO = Arrays.asList(
                DatosContactoDTO.newInstance().build()
        );
        PpdDocumento PPD_DOCUMENTO_STUB = PpdDocumento.newInstance().idePpdDocumento(BigInteger.valueOf(1186))
                .codTipoDoc("TL-DOCDP")
                .fecDocumento(new Date())
                .asunto("Cualquier Mierda")
                .nroAnexos(Long.valueOf(2))
                .nroFolios(Long.valueOf(2))
                .ideEcm("45c2ef61-9cf3-4c99-a95a-638e58728cb7")
                .build();
        List<PpdDocumentoDTO> PPD_DOCUMENTO_LIST = Arrays.asList(
                PpdDocumentoDTO.newInstance().idePpdDocumento(BigInteger.valueOf(1186))
                        .codTipoDoc("TL-DOCDP")
                        .fecDocumento(new Date())
                        .asunto("Cualquier Mierda")
                        .nroAnexos(Long.valueOf(2))
                        .nroFolios(Long.valueOf(2))
                        .ideEcm("45c2ef61-9cf3-4c99-a95a-638e58728cb7")
                        .build()
        );
        BigInteger ID_ANEXO = BigInteger.valueOf(1);
        String COD_ANEXO = "AN-TP";
        String COD_TIPO_SOPORTE = "TP-SOP";
        String DESC_ANEXO = "Descripcion";
        List<AnexoDTO> ANEXO_LIST = Arrays.asList(
                AnexoDTO.newInstance()
                        .ideAnexo(ID_ANEXO)
                        .codAnexo(COD_ANEXO)
                        .codTipoSoporte(COD_TIPO_SOPORTE)
                        .descripcion(DESC_ANEXO)
                        .build()
        );
        List<ReferidoDTO> REFERIDO_LIST = Arrays.asList(
                ReferidoDTO.newInstance().build()
        );
        ComunicacionOficialDTO COMUNICACION_OFICIAL = ComunicacionOficialDTO.newInstance()
                .correspondencia(CORRESPONDENCIA)
                .agenteList(AGENTE_LIST).datosContactoList(DATOS_CONTACTO)
                .ppdDocumentoList(PPD_DOCUMENTO_LIST)
                .anexoList(ANEXO_LIST)
                .referidoList(REFERIDO_LIST)
                .build();

        List<CorAgente> COR_AGENTE_LIST = Arrays.asList(
                CorAgente.newInstance()
                        .codDependencia(COD_DEPENDENCIA)
                        .codTipAgent(TipoAgenteEnum.DESTINATARIO.getCodigo())
                        .build()
        );
        when(agenteControl.conformarAgentes(anyListOf(AgenteDTO.class), anyListOf(DatosContactoDTO.class), anyString())).thenReturn(COR_AGENTE_LIST);
        CorAnexo COR_ANEXO_STUB = CorAnexo.newInstance()
                .ideAnexo(ID_ANEXO)
                .codAnexo(COD_ANEXO)
                .codTipoSoporte(COD_TIPO_SOPORTE)
                .descripcion(DESC_ANEXO)
                .build();
        when(anexoControl.corAnexoTransform(any(AnexoDTO.class))).thenReturn(COR_ANEXO_STUB);

        when(ppdDocumentoControl.ppdDocumentoTransform(PPD_DOCUMENTO_LIST.get(0))).thenReturn(PPD_DOCUMENTO_STUB);
        CorReferido COR_REFERIDO = CorReferido.newInstance()
                .ideReferido(BigInteger.valueOf(254))
                .nroRadRef("1000EE2018000134")
                .build();
        when(referidoControl.corReferidoTransform(any(ReferidoDTO.class))).thenReturn(COR_REFERIDO);
        Long CANTIDAD_STUB = 0L;
//        when(correspondenciaControl.verificarByNroRadicado(NRO_RADICADO)).thenCallRealMethod();
        TypedQuery<Long> correspondenciaDTOTypedQuery = getSingleResultMock("CorCorrespondencia.countByNroRadicado", Long.class,CANTIDAD_STUB);
        List<CorrespondenciaDTO> CORRESPONDENCIA_LIST_STUB = new ArrayList<CorrespondenciaDTO>();
        String TP_CMC = "SE";
        CorrespondenciaDTO CORRESPONDENCIA_STUB = CorrespondenciaDTO.newInstance()
                .nroRadicado(NRO_RADICADO)
                .codSede(COD_SEDE)
                .codDependencia(COD_DEPENDENCIA)
                .fecRadicado(new Date())
                .codEstado(EstadoCorrespondenciaEnum.SIN_ASIGNAR.getCodigo())
                .codTipoCmc(TP_CMC)
                .ideDocumento(IDE_DOCUMENTO)
                .build();
        CORRESPONDENCIA_LIST_STUB.add(CORRESPONDENCIA_STUB);
        TypedQuery<CorrespondenciaDTO> consultarCorrespondenciaTypedQuery = getResultListMock("CorCorrespondencia.findByNroRadicado", CorrespondenciaDTO.class,CORRESPONDENCIA_LIST_STUB);

        // when
        ComunicacionOficialDTO comunicacionOficialDTO = correspondenciaControl.radicarCorrespondencia(COMUNICACION_OFICIAL);

        // then
        verify(em).createNamedQuery("CorCorrespondencia.countByNroRadicado", Long.class);
        verify(correspondenciaDTOTypedQuery).setParameter("NRO_RADICADO", NRO_RADICADO);
    }

    @Test
    public void listarCorrespondenciaByNroRadicado() {
    }

    @Test
    public void correspondenciaTransformToFull() {
    }

    @Test
    public void listarFullCorrespondenciaByNroRadicado() {
    }

    @Test
    public void actualizarEstadoCorrespondencia() {
    }

    @Test
    public void actualizarIdeInstancia() {
    }

    @Test
    public void getIdeInstanciaPorRadicado() {
    }

    @Test
    public void listarCorrespondenciaByPeriodoAndCodDependenciaAndCodEstadoAndNroRadicado() throws ParseException, SystemException, BusinessException {
        //given
        Date FECHA_INI = dateFormat.parse("2018/06/01");
        java.sql.Timestamp FEC_INI = new java.sql.Timestamp(FECHA_INI.getTime());
        Date FECHA_FIN = dateFormat.parse("2018/06/30");
        java.sql.Timestamp FEC_FIN = new java.sql.Timestamp(FECHA_FIN.getTime());

        Calendar cal = Calendar.getInstance();
        cal.setTime(FEC_FIN);
        cal.add(Calendar.DATE, 1);

        String COD_SEDE = "1040";
        String COD_DEPENDENCIA = "10401040";
        String COD_ESTADO = EstadoCorrespondenciaEnum.ASIGNACION.getCodigo();
        String NRO_RADICADO = "1040EE2017000001";
        String TP_CMC = "SE";
        BigInteger IDE_DOCUMENTO = BigInteger.valueOf(836);
        String COD_ESTADO_AGENTE = EstadoAgenteEnum.ASIGNADO.getCodigo();
        String COD_TIPO_AGENTE = TipoAgenteEnum.DESTINATARIO.getCodigo();

        List<AgenteDTO> AGENTE_LIST =  new ArrayList<AgenteDTO>();
        AgenteDTO AGENTE = AgenteDTO.newInstance()
                .codDependencia(COD_DEPENDENCIA)
                .codSede(COD_SEDE)
                .codTipAgent(TipoAgenteEnum.DESTINATARIO.getCodigo())
                .codTipoRemite(TipoRemitenteEnum.EXTERNO.getCodigo())
                .codEstado(EstadoAgenteEnum.SIN_ASIGNAR.getCodigo())
                .build();
        AGENTE_LIST.add(AGENTE);

        CorrespondenciaDTO correspondenciaDTOStub = CorrespondenciaDTO.newInstance()
                .ideDocumento(IDE_DOCUMENTO)

                .build();
        List<CorrespondenciaDTO> CORRESPONDENCIA_LIST_STUB = new ArrayList<CorrespondenciaDTO>();
        CorrespondenciaDTO CORRESPONDENCIA_STUB = CorrespondenciaDTO.newInstance()
                .nroRadicado(NRO_RADICADO)
                .codSede(COD_SEDE)
                .codDependencia(COD_DEPENDENCIA)
                .fecRadicado(new Date())
                .codEstado(EstadoCorrespondenciaEnum.SIN_ASIGNAR.getCodigo())
                .codTipoCmc(TP_CMC)
                .ideDocumento(IDE_DOCUMENTO)
                .build();
        CORRESPONDENCIA_LIST_STUB.add(CORRESPONDENCIA_STUB);

        TypedQuery<CorrespondenciaDTO> correspondenciaDTOTypedQuery = getResultListMock("CorCorrespondencia.findByPeriodoAndCodDependenciaAndCodEstadoAndNroRadicado", CorrespondenciaDTO.class, CORRESPONDENCIA_LIST_STUB);
        when(agenteControl.listarDestinatariosByIdeDocumentoAndCodDependenciaAndCodEstado(any(BigInteger.class), anyString(),anyString())).thenReturn(AGENTE_LIST);

        //when
        ComunicacionesOficialesDTO comunicacionesOficialesDTO =  correspondenciaControl.listarCorrespondenciaByPeriodoAndCodDependenciaAndCodEstadoAndNroRadicado(FEC_INI, FEC_FIN, COD_DEPENDENCIA,COD_ESTADO,NRO_RADICADO);

        //then
         verify(agenteControl, times(comunicacionesOficialesDTO.getComunicacionesOficiales().size())).listarDestinatariosByIdeDocumentoAndCodDependenciaAndCodEstado(IDE_DOCUMENTO,
                        COD_DEPENDENCIA,
                        COD_ESTADO);
        assertNotNull(comunicacionesOficialesDTO);
        assertTrue(comunicacionesOficialesDTO.getComunicacionesOficiales().size()>0);

        verify(em).createNamedQuery("CorCorrespondencia.findByPeriodoAndCodDependenciaAndCodEstadoAndNroRadicado", CorrespondenciaDTO.class);
        verify(correspondenciaDTOTypedQuery).setParameter("FECHA_INI", FEC_INI);
        verify(correspondenciaDTOTypedQuery).setParameter("FECHA_FIN", cal.getTime());
        verify(correspondenciaDTOTypedQuery).setParameter("COD_ESTADO", COD_ESTADO);
        verify(correspondenciaDTOTypedQuery).setParameter("COD_DEPENDENCIA", COD_DEPENDENCIA);
        verify(correspondenciaDTOTypedQuery).setParameter("COD_EST_AG", COD_ESTADO_AGENTE);
        verify(correspondenciaDTOTypedQuery).setParameter("COD_TIP_AGENT", COD_TIPO_AGENTE);
        verify(correspondenciaDTOTypedQuery).setParameter("NRO_RADICADO", "%"+NRO_RADICADO+"%");
        }

    @Test
    public void listarComunicacionDeSalidaConDistribucionFisica() {
    }

    @Test
    public void listarComunicacionDeSalidaConDistribucionFisicaEmplantillada() {
    }

    @Test
    public void listarCorrespondenciaSinDistribuir() {
    }

    @Test
    public void consultarComunicacionOficialByCorrespondencia() {
    }

    @Test
    public void consultarComunicacionOficialFullByCorrespondencia() {
    }

    @Test
    public void corCorrespondenciaTransform() {
    }

    @Test
    public void verificarByNroRadicado() throws SystemException {
        //given
        String NRO_RADICADO = "1040TP-CMCOE2017000001";

        //when
        Boolean resultado = correspondenciaControl.verificarByNroRadicado(NRO_RADICADO);

        //then
        assertTrue(resultado);
    }

    @Test
    public void calcularFechaVencimientoGestion() {
    }

    @Test
    public void horasHabilesDia() {
    }

    @Test
    public void calcularDiaHabilSiguiente() {
    }

    @Test
    public void esDiaHabil() {
    }

    @Test
    public void esDiaFestivo() {
    }

    @Test
    public void esFinSemana() {
    }

    @Test
    public void consultarCorrespondenciaByNroRadicado() {

    }

    @Test
    public void consultarCorrespondenciaByIdeAgente() {
    }

    @Test
    public void consultarCorrespondenciaByIdeDocumento() {
    }

    @Test
    public void consultarFechaVencimientoByIdeDocumento() {
    }

    @Test
    public void actualizarComunicacion() {
    }

    @Test
    public void radicarCorrespondenciaSalida() {
    }

    @Test
    public void radicarCorrespondenciaSalidaRemitenteReferidoADestinatario() {
    }

    @Test
    public void sendMail() {
    }

    @Test
    public void consultarNroRadicadoCorrespondenciaReferida() {
    }
}