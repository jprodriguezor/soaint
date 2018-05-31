package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.test.mocks.JaxRsUtils;
import co.com.foundation.test.rules.EnvironmentRule;
import co.com.foundation.sgd.utils.SystemParameters;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.correspondencia.AgenteDTO;
import co.com.soaint.foundation.canonical.correspondencia.CorrespondenciaDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.ItemDevolucionDTO;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;

import java.math.BigInteger;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ProcesoClientTest {


    @Rule
    public EnvironmentRule environmentRule = new EnvironmentRule();

    private String API_ENDPOINT = SystemParameters.getParameter(SystemParameters.BACKAPI_ENTERPRISE_SERVICE_ENDPOINT_URL);

    private ProcesoClient procesoClient;

    private WebTarget wt;

    private Client client;

    @Before
    public void setup() {

        procesoClient = new ProcesoClient();

        client = mock(Client.class);
        wt = mock(WebTarget.class);

        when(client.target(anyString())).thenReturn(wt);

        ReflectionTestUtils.setField(procesoClient, "client", client);
    }


    @Test
    public void list() {
        // given
        String path = "/bpm/proceso/listar";
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, path);

        // when
        procesoClient.list();

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<FuncionarioDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity()).isInstanceOf(EntradaProcesoDTO.class);
    }

    @Test
    public void iniciar() {
        // given
        String path = "/bpm/proceso/iniciar";
        EntradaProcesoDTO dto = mock(EntradaProcesoDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, path);

        // when
        procesoClient.iniciar(dto);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<EntradaProcesoDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }

    @Test
    public void iniciarTercero() {
        // given
        String path = "/bpm/proceso/iniciar-tercero";
        EntradaProcesoDTO dto = mock(EntradaProcesoDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, path);

        // when
        procesoClient.iniciarTercero(dto);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<EntradaProcesoDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }

    @Test
    public void iniciarProcesoGestorDevoluciones() {
        // given
        String path = "/bpm/proceso/iniciar-tercero";
        JaxRsUtils.mockPostPath(wt, path);

        String NRO_RADICADO = "RD01";
        Integer CAUSA_DEVOLUCION = 100;
        String FUN_DEVUELVE = "FR01";
        Date FECHA_VENCIMIENTO = new Date();
        BigInteger ID_AGENTE = BigInteger.valueOf(101);
        String ESTADO_AGENTE = "EA01";
        String COD_DEPENDENCIA = "EA01";


        ItemDevolucionDTO devolucionDTO = ItemDevolucionDTO.newInstance()
                .causalDevolucion(CAUSA_DEVOLUCION)
                .funDevuelve(FUN_DEVUELVE)
                .correspondencia(CorrespondenciaDTO.newInstance()
                        .nroRadicado(NRO_RADICADO)
                        .fecVenGestion(FECHA_VENCIMIENTO)
                        .codDependencia(COD_DEPENDENCIA)
                        .build())
                .agente(AgenteDTO.newInstance()
                        .ideAgente(ID_AGENTE)
                        .codEstado(ESTADO_AGENTE)
                        .build())
                .build();

        EntradaProcesoDTO entradaProcesoDTO = new EntradaProcesoDTO();

        ProcesoClient procesoClientSpy = spy(procesoClient);

        // when
        procesoClientSpy.iniciarProcesoGestorDevoluciones(devolucionDTO, entradaProcesoDTO);

        // then
        ArgumentCaptor<EntradaProcesoDTO> captor = ArgumentCaptor.forClass(EntradaProcesoDTO.class);
        verify(procesoClientSpy).iniciarTercero(captor.capture());

        EntradaProcesoDTO captureEntradaProceso = captor.getValue();
        assertThat(captureEntradaProceso).isSameAs(entradaProcesoDTO);
        assertThat(captureEntradaProceso.getParametros())
                .containsOnly(
                        entry("numeroRadicado", NRO_RADICADO),
                        entry("causalD", CAUSA_DEVOLUCION),
                        entry("funDevuelve", FUN_DEVUELVE),
                        entry("fechaVencimiento", FECHA_VENCIMIENTO),
                        entry("idAgente", ID_AGENTE.toString()),
                        entry("estadoFinal", ESTADO_AGENTE),
                        entry("codDependencia", COD_DEPENDENCIA)
                );
    }

    @Test
    public void iniciarManual() {
        // given
        String path = "/bpm/proceso/iniciar/manual";
        EntradaProcesoDTO dto = mock(EntradaProcesoDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, path);

        // when
        procesoClient.iniciarManual(dto);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<EntradaProcesoDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }

    @Test
    public void listarPorIdProceso() {
        // given
        String path = "/bpm/tareas/listar/estados-instancia/";
        EntradaProcesoDTO dto = mock(EntradaProcesoDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, path);

        // when
        procesoClient.listarPorIdProceso(dto);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<EntradaProcesoDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }

    @Test
    public void listarPorUsuarioYPorIdProceso() {
        // given
        String path = "/bpm/tareas/listar/asignado/";
        EntradaProcesoDTO dto = mock(EntradaProcesoDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, path);

        // when
        procesoClient.listarPorUsuarioYPorIdProceso(dto);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<EntradaProcesoDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }

    @Test
    public void listarTareas() {
        // given
        String path = "/bpm/tareas/listar/estados/";
        EntradaProcesoDTO dto = mock(EntradaProcesoDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, path);

        // when
        procesoClient.listarTareas(dto);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<EntradaProcesoDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }

    @Test
    public void listarTareasCompletadas() {
        // given
        String path = "/bpm/tareas/listar/completadas";
        EntradaProcesoDTO dto = mock(EntradaProcesoDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, path);

        // when
        procesoClient.listarTareasCompletadas(dto);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<EntradaProcesoDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }

    @Test
    public void listarEstadisticasTareas() {
        // given
        String path = "/bpm/tareas/listar/usuario";
        EntradaProcesoDTO dto = mock(EntradaProcesoDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, path);

        // when
        procesoClient.listarEstadisticasTareas(dto);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<EntradaProcesoDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }

    @Test
    public void iniciarTarea() {
        // given
        String path = "/bpm/tareas/iniciar";
        EntradaProcesoDTO dto = mock(EntradaProcesoDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, path);

        // when
        procesoClient.iniciarTarea(dto);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<EntradaProcesoDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }

    @Test
    public void reservarTarea() {
        // given
        String path = "/bpm/tareas/reservar";
        EntradaProcesoDTO dto = mock(EntradaProcesoDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, path);

        // when
        procesoClient.reservarTarea(dto);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<EntradaProcesoDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }

    @Test
    public void completarTarea() {
        // given
        String path = "/bpm/tareas/completar";
        EntradaProcesoDTO dto = mock(EntradaProcesoDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, path);

        // when
        procesoClient.completarTarea(dto);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<EntradaProcesoDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }

    @Test
    public void abortarTarea() {
        // given
        String path = "/bpm/proceso/abortar";
        EntradaProcesoDTO dto = mock(EntradaProcesoDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, path);

        // when
        procesoClient.abortarTarea(dto);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<EntradaProcesoDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }

    @Test
    public void reasignarTarea() {
        // given
        String path = "/bpm/tareas/reasignar";
        EntradaProcesoDTO dto = mock(EntradaProcesoDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, path);

        // when
        procesoClient.reasignarTarea(dto);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<EntradaProcesoDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }

    @Test
    public void listarIntanciasProceso() {
        // given
        String path = "/bpm/proceso/listar-instancias/";
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, path);

        // when
        procesoClient.listarIntanciasProceso();

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<EntradaProcesoDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity().getIdProceso()).isEqualTo("proceso.correspondencia-entrada");
    }

    @Test
    public void obtenerVariablesTarea() {
        // given
        String path = "/bpm/proceso/listar-variables";
        EntradaProcesoDTO dto = mock(EntradaProcesoDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, path);

        // when
        procesoClient.obtenerVariablesTarea(dto);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<EntradaProcesoDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }
}