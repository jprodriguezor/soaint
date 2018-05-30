package co.com.soaint.correspondencia;

import co.com.soaint.correspondencia.business.control.*;
import co.com.soaint.correspondencia.domain.entity.DctAsigUltimo;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.canonical.correspondencia.constantes.EstadoAgenteEnum;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class AsignacionControlTest  {

    @Rule
    public ExpectedException fails = ExpectedException.none();

    final String LOGIN_NAME = "User";
    final String ID_INSTANCIA = "II01";
    final BigInteger ID_ASIGNACION = BigInteger.valueOf(100);
    final BigInteger ID_AGENTE = BigInteger.valueOf(100);
    final BigInteger ID_DOCUMENTO = BigInteger.valueOf(836);
    final BigInteger ID_FUNCIONARIO = BigInteger.valueOf(1);


    AsignacionControl asignacionControl;

    // Dependencias
    EntityManager em;
    DctAsignacionControl dctAsignacionControl;
    DctAsigUltimoControl dctAsigUltimoControl;
    CorrespondenciaControl correspondenciaControl;
    FuncionariosControl funcionariosControl;
    AgenteControl agenteControl;

    @Before
    public void setUp() throws Exception {

        asignacionControl = new AsignacionControl();

        em = mock(EntityManager.class);
        dctAsignacionControl = mock(DctAsignacionControl.class);
        dctAsigUltimoControl = mock(DctAsigUltimoControl.class);
        correspondenciaControl = mock(CorrespondenciaControl.class);
        funcionariosControl = mock(FuncionariosControl.class);
        agenteControl = mock(AgenteControl.class);

        ReflectionTestUtils.setField(asignacionControl, "em", em);
        ReflectionTestUtils.setField(asignacionControl, "dctAsignacionControl", dctAsignacionControl);
        ReflectionTestUtils.setField(asignacionControl, "dctAsigUltimoControl", dctAsigUltimoControl);
        ReflectionTestUtils.setField(asignacionControl, "correspondenciaControl", correspondenciaControl);
        ReflectionTestUtils.setField(asignacionControl, "funcionariosControl", funcionariosControl);
        ReflectionTestUtils.setField(asignacionControl, "agenteControl", agenteControl);
        ReflectionTestUtils.setField(asignacionControl, "cantHorasParaActivarAlertaVencimiento", 8);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void asignarCorrespondencia() throws Exception {

        // given
        AsignacionTramiteDTO asignacionTramiteDTO = newAsignacionTramiteDTO();
        List<AsignacionDTO> asignacionDTOList = asignacionTramiteDTO.getAsignaciones().getAsignaciones();

        // asignacion
        DctAsignacionDTO asignacionDTOStub = DctAsignacionDTO.newInstance().ideAsignacion(ID_ASIGNACION).build();
        TypedQuery<DctAsignacionDTO> asignacionTQ = getSingleResultMock("DctAsignacion.findByIdeAgente", DctAsignacionDTO.class, asignacionDTOStub);

        // asignacion result update DUDA!!!!!!!!!!!!!!!!!!!!!!!!
        TypedQuery<AsignacionDTO> asignacionFuncionarioTQ = getExecuteUpdateMock("DctAsignacion.asignarToFuncionario", null);

        // asignacion result
        AsignacionDTO asignacionDTOResultStub = AsignacionDTO.newInstance().build();
        TypedQuery<AsignacionDTO> asignacionResultTQ = getSingleResultMock( "DctAsigUltimo.findByIdeAgenteAndCodEstado", AsignacionDTO.class, asignacionDTOResultStub);

        //DUDA!!!!!!!!!!!!!!!!!
        when(correspondenciaControl.consultarFechaVencimientoByIdeDocumento(any(BigInteger.class))).thenReturn(new Date());

        // when
        AsignacionesDTO asignacionesDTO = asignacionControl.asignarCorrespondencia(asignacionTramiteDTO);

        // then
        AsignacionDTO result = asignacionesDTO.getAsignaciones().get(0);
        assertThat(result.getLoginName(), is(LOGIN_NAME));
        assertThat(result.getAlertaVencimiento(), notNullValue());

        verify(em, times(asignacionDTOList.size())).createNamedQuery("DctAsignacion.findByIdeAgente", DctAsignacionDTO.class);
        verify(asignacionTQ, times(asignacionDTOList.size())).setParameter("IDE_AGENTE", ID_AGENTE);

        verify(em, times(asignacionDTOList.size())).createNamedQuery("DctAsignacion.asignarToFuncionario");
        verify(asignacionFuncionarioTQ, times(asignacionDTOList.size())).setParameter("IDE_FUNCI", ID_FUNCIONARIO);
        verify(asignacionFuncionarioTQ, times(asignacionDTOList.size())).setParameter("IDE_ASIGNACION", ID_ASIGNACION);

        verify(em, times(asignacionDTOList.size())).createNamedQuery("DctAsigUltimo.findByIdeAgenteAndCodEstado", AsignacionDTO.class);
        verify(asignacionResultTQ, times(asignacionDTOList.size())).setParameter("IDE_AGENTE", ID_AGENTE);
        verify(asignacionResultTQ, times(asignacionDTOList.size())).setParameter("COD_ESTADO", EstadoAgenteEnum.ASIGNADO.getCodigo());

        verify(agenteControl, times(asignacionDTOList.size())).actualizarEstadoAgente(any(AgenteDTO.class));
        verify(correspondenciaControl, times(asignacionDTOList.size())).consultarFechaVencimientoByIdeDocumento(any(BigInteger.class));
    }

    @Test(expected = BusinessException.class)
    public void asignarCorrespondencia_Fail_1() throws SystemException, BusinessException {
        AsignacionTramiteDTO asignacionTramiteDTO = newAsignacionTramiteDTO();
        List<AsignacionDTO> asignacionDTOList = asignacionTramiteDTO.getAsignaciones().getAsignaciones();

        when(em.createNamedQuery("DctAsignacion.findByIdeAgente", DctAsignacionDTO.class)).thenThrow(BusinessException.class);

        asignacionControl.asignarCorrespondencia(asignacionTramiteDTO);
    }

    @Test
    public void asignarCorrespondencia_Fail_2() throws SystemException, BusinessException {
        AsignacionTramiteDTO asignacionTramiteDTO = newAsignacionTramiteDTO();
        List<AsignacionDTO> asignacionDTOList = asignacionTramiteDTO.getAsignaciones().getAsignaciones();


        when(em.createNamedQuery("DctAsignacion.findByIdeAgente", DctAsignacionDTO.class)).thenThrow(Exception.class);

        fails.expect(SystemException.class);
        fails.expectMessage("system.generic.error");

        asignacionControl.asignarCorrespondencia(asignacionTramiteDTO);
    }

    @Test
    public void actualizarIdInstancia() throws Exception {
        // given
        AsignacionDTO asignacionDTO = newAsignacionDTO();

        // asignacion
        DctAsigUltimo dctAsigUltimoStub = DctAsigUltimo.newInstance().ideAsigUltimo(ID_ASIGNACION).build();

        TypedQuery<DctAsigUltimo> dctAsigUltimoTQ = getSingleResultMock("DctAsigUltimo.findByIdeAsignacion", DctAsigUltimo.class, dctAsigUltimoStub);
        TypedQuery<DctAsigUltimo> dctAsigUltimoTypedQuery = getExecuteUpdateMock("DctAsigUltimo.updateIdInstancia", null);


        // when
        asignacionControl.actualizarIdInstancia(asignacionDTO);

        // then

        verify(em).createNamedQuery("DctAsigUltimo.findByIdeAsignacion", DctAsigUltimo.class);//se ejecuta
        verify(dctAsigUltimoTQ).setParameter("IDE_ASIGNACION", ID_ASIGNACION);//con parametros

        verify(em).createNamedQuery("DctAsigUltimo.updateIdInstancia");
        verify(dctAsigUltimoTypedQuery).setParameter("IDE_ASIG_ULTIMO", ID_ASIGNACION);
        verify(dctAsigUltimoTypedQuery).setParameter("ID_INSTANCIA", ID_INSTANCIA);
    }

    @Test//(expected = BusinessException.class)
    public void actualizarIdInstancia_Fail_1() throws SystemException, BusinessException {
        AsignacionDTO asignacionDTO = newAsignacionDTO();

        when(em.createNamedQuery("DctAsigUltimo.findByIdeAsignacion", DctAsigUltimo.class)).thenThrow(Exception.class);

        fails.expect(SystemException.class);
        fails.expectMessage("system.generic.error");

        asignacionControl.actualizarIdInstancia(asignacionDTO);
    }

    @Test(expected = SystemException.class)
    public void actualizarIdInstancia_Fail_2() throws SystemException, BusinessException {

        AsignacionDTO asignacionDTO = newAsignacionDTO();


        when(em.createNamedQuery("DctAsigUltimo.findByIdeAsignacion", DctAsigUltimo.class)).thenThrow(BusinessException.class);

        asignacionControl.actualizarIdInstancia(asignacionDTO);
    }


    @Test
    public void listarAsignacionesByFuncionarioAndNroRadicado() throws Exception {

    }

    @Test
    public void actualizarTipoProceso() throws Exception {
    }

    @Test
    public void consultarAsignacionReasignarByIdeAgente() throws Exception {
    }

    @Test
    public void conformarAsignaciones() throws Exception {
    }

    @Test
    public void asignarDocumentoByNroRadicado() throws Exception {
    }

    @Test
    public void actualizarAsignacion() throws Exception {
    }

    @Test
    public void actualizarAsignacionFromDevolucion() throws Exception {
    }

    @Test
    public void getAsignacionUltimoByIdeAgente() throws Exception {
    }


    private AsignacionTramiteDTO newAsignacionTramiteDTO() {
        AsignacionDTO asignacionDTO = newAsignacionDTO();
        List<AsignacionDTO> asignacionDTOList = new ArrayList<>();
        asignacionDTOList.add(asignacionDTO);

        return AsignacionTramiteDTO.newInstance()
                .asignaciones(AsignacionesDTO.newInstance()
                        .asignaciones(asignacionDTOList).build())
                .build();
    }

    private AsignacionDTO newAsignacionDTO() {
        return AsignacionDTO.newInstance()
                .loginName(LOGIN_NAME)
                .ideAsignacion(ID_ASIGNACION)
                .ideAgente(ID_AGENTE)
                .ideDocumento(ID_DOCUMENTO)
                .ideFunci(ID_FUNCIONARIO)
                .idInstancia(ID_INSTANCIA)
                .build();
    }

    private <T> TypedQuery<T> getSingleResultMock(String namedQuery, Class<T> type, T dummyObject) {
        TypedQuery<T> typedQuery = mock(TypedQuery.class);
        when(em.createNamedQuery(namedQuery,  type)).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(dummyObject);
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
}