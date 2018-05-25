package co.com.soaint.correspondencia;

import co.com.soaint.correspondencia.business.control.*;
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

    AsignacionControl asignacionControl;

    // Dependencias
    EntityManager em = mock(EntityManager.class);
    DctAsignacionControl dctAsignacionControl = mock(DctAsignacionControl.class);
    DctAsigUltimoControl dctAsigUltimoControl = mock(DctAsigUltimoControl.class);
    CorrespondenciaControl correspondenciaControl = mock(CorrespondenciaControl.class);
    FuncionariosControl funcionariosControl = mock(FuncionariosControl.class);
    AgenteControl agenteControl = mock(AgenteControl.class);

    @Before
    public void setUp() throws Exception {

        asignacionControl = new AsignacionControl();

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
        AsignacionDTO asignacionDTO = AsignacionDTO.newInstance()
                .loginName("User")
                .ideAsignacion(new BigInteger("100"))
                .ideAgente(new BigInteger("100"))
                .ideDocumento(new BigInteger("836"))
                .ideFunci(new BigInteger("1"))
                .build();
        List<AsignacionDTO> asignacionDTOList=new ArrayList<>();
        asignacionDTOList.add(asignacionDTO);

        AsignacionTramiteDTO asignacionTramiteDTO = AsignacionTramiteDTO.newInstance()
                        .asignaciones(AsignacionesDTO.newInstance()
                        .asignaciones(asignacionDTOList).build())
                        .build();

        // asignacion
        DctAsignacionDTO asignacionDTOStub = DctAsignacionDTO.newInstance().ideAsignacion(BigInteger.valueOf(100)).build();
        TypedQuery<DctAsignacionDTO> asignacionTQ = mock(TypedQuery.class);
        when(asignacionTQ.setParameter(anyString(), any())).thenReturn(asignacionTQ);
        when(asignacionTQ.getSingleResult()).thenReturn(asignacionDTOStub);
        when(em.createNamedQuery("DctAsignacion.findByIdeAgente", DctAsignacionDTO.class)).thenReturn(asignacionTQ);

        // asignacion result
        TypedQuery<AsignacionDTO> asignacionFuncionarioTQ = mock(TypedQuery.class);
        when(asignacionFuncionarioTQ.setParameter(anyString(), any())).thenReturn(asignacionFuncionarioTQ);
        when(em.createNamedQuery("DctAsignacion.asignarToFuncionario")).thenReturn(asignacionFuncionarioTQ);

        // asignacion result
        AsignacionDTO asignacionDTOResultStub = AsignacionDTO.newInstance().build();
        TypedQuery<AsignacionDTO> asignacionResultTQ = mock(TypedQuery.class);
        when(asignacionResultTQ.setParameter(anyString(), any())).thenReturn(asignacionResultTQ);
        when(asignacionResultTQ.getSingleResult()).thenReturn(asignacionDTOResultStub);
        when(em.createNamedQuery("DctAsigUltimo.findByIdeAgenteAndCodEstado", AsignacionDTO.class)).thenReturn(asignacionResultTQ);


        when(correspondenciaControl.consultarFechaVencimientoByIdeDocumento(any(BigInteger.class))).thenReturn(new Date());
        // when
        AsignacionesDTO asignacionesDTO = asignacionControl.asignarCorrespondencia(asignacionTramiteDTO);

        // then
        AsignacionDTO result = asignacionesDTO.getAsignaciones().get(0);
        assertThat(result.getLoginName(), is("User"));
        assertThat(result.getAlertaVencimiento(), notNullValue());

        verify(asignacionTQ, times(asignacionDTOList.size())).setParameter("IDE_AGENTE", BigInteger.valueOf(100));
        verify(em, times(asignacionDTOList.size())).createNamedQuery("DctAsignacion.findByIdeAgente", DctAsignacionDTO.class);
        verify(em, times(asignacionDTOList.size())).createNamedQuery("DctAsignacion.asignarToFuncionario");

        verify(asignacionResultTQ, times(asignacionDTOList.size())).setParameter("IDE_AGENTE", BigInteger.valueOf(100));
        verify(asignacionResultTQ, times(asignacionDTOList.size())).setParameter("COD_ESTADO", EstadoAgenteEnum.ASIGNADO.getCodigo());
        verify(em, times(asignacionDTOList.size())).createNamedQuery("DctAsigUltimo.findByIdeAgenteAndCodEstado", AsignacionDTO.class);

        verify(agenteControl, times(asignacionDTOList.size())).actualizarEstadoAgente(any(AgenteDTO.class));
        verify(correspondenciaControl, times(asignacionDTOList.size())).consultarFechaVencimientoByIdeDocumento(any(BigInteger.class));
    }

    @Test(expected = BusinessException.class)
    public void asignarCorrespondencia_Fail_1() throws SystemException, BusinessException {
        AsignacionDTO asignacionDTO = AsignacionDTO.newInstance()
                .loginName("User")
                .ideAsignacion(new BigInteger("100"))
                .ideAgente(new BigInteger("100"))
                .ideDocumento(new BigInteger("836"))
                .ideFunci(new BigInteger("1"))
                .build();
        List<AsignacionDTO> asignacionDTOList=new ArrayList<>();
        asignacionDTOList.add(asignacionDTO);

        AsignacionTramiteDTO asignacionTramiteDTO = AsignacionTramiteDTO.newInstance()
                .asignaciones(AsignacionesDTO.newInstance()
                        .asignaciones(asignacionDTOList).build())
                .build();

        when(em.createNamedQuery("DctAsignacion.findByIdeAgente", DctAsignacionDTO.class)).thenThrow(BusinessException.class);

        asignacionControl.asignarCorrespondencia(asignacionTramiteDTO);
    }

    @Test
    public void asignarCorrespondencia_Fail_2() throws SystemException, BusinessException {
        AsignacionDTO asignacionDTO = AsignacionDTO.newInstance()
                .loginName("User")
                .ideAsignacion(new BigInteger("100"))
                .ideAgente(new BigInteger("100"))
                .ideDocumento(new BigInteger("836"))
                .ideFunci(new BigInteger("1"))
                .build();
        List<AsignacionDTO> asignacionDTOList=new ArrayList<>();
        asignacionDTOList.add(asignacionDTO);

        AsignacionTramiteDTO asignacionTramiteDTO = AsignacionTramiteDTO.newInstance()
                .asignaciones(AsignacionesDTO.newInstance()
                        .asignaciones(asignacionDTOList).build())
                .build();

        when(em.createNamedQuery("DctAsignacion.findByIdeAgente", DctAsignacionDTO.class)).thenThrow(Exception.class);

        fails.expect(SystemException.class);
        fails.expectMessage("system.generic.error");

        asignacionControl.asignarCorrespondencia(asignacionTramiteDTO);
    }

    @Test
    public void actualizarIdInstancia() throws Exception {
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

}