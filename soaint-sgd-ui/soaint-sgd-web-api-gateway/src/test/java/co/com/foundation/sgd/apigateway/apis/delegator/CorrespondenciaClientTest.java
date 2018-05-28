package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.test.mocks.JaxRsUtils;
import co.com.foundation.test.rules.EnvironmentRule;
import co.com.foundation.sgd.utils.SystemParameters;
import co.com.soaint.foundation.canonical.correspondencia.*;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CorrespondenciaClientTest {

    @Rule
    public EnvironmentRule environmentRule = new EnvironmentRule();

    private String API_ENDPOINT = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

    private String DROOLS_ENDPOINT = SystemParameters.getParameter(SystemParameters.BACKAPI_DROOLS_SERVICE_ENDPOINT_URL);

    private String DROOLS_ACCESS_TOKEN = SystemParameters.getParameter(SystemParameters.BACKAPI_DROOLS_SERVICE_TOKEN);

    private CorrespondenciaClient correspondenciaClient;

    private WebTarget wt;

    private Client client;


    @Before
    public void setup() {
        correspondenciaClient = new CorrespondenciaClient();

        client = mock(Client.class);
        wt = mock(WebTarget.class);

        when(client.target(anyString())).thenReturn(wt);

        ReflectionTestUtils.setField(correspondenciaClient, "client", client);
    }

    @Test
    public void radicar() {
        // given
        String path = "/correspondencia-web-api/correspondencia";
        ComunicacionOficialDTO dto = mock(ComunicacionOficialDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, path);

        // when
        correspondenciaClient.radicar(dto);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<ComunicacionOficialDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }

    @Test
    public void radicarSalida() {
        // given
        String path = "/correspondencia-web-api/correspondencia/radicar-salida";
        ComunicacionOficialRemiteDTO dto = mock(ComunicacionOficialRemiteDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, path);

        // when
        correspondenciaClient.radicarSalida(dto);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<ComunicacionOficialRemiteDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }

    @Test
    public void listarComunicaciones() {
        // given
        String path = "/correspondencia-web-api/correspondencia";
        String FECHA_INI = "01-01-2000";
        String FECHA_FIN = "01-01-2000";
        String COD_DEPENDENCIA = "CD01";
        String COD_ESTADO = "CE01";
        String NO_RADICADO = "NR01";

        WebTarget requestTarget = JaxRsUtils.mockGetPath(wt, path);

        // when
        correspondenciaClient.listarComunicaciones(
                FECHA_INI, FECHA_FIN, COD_DEPENDENCIA,
                COD_ESTADO, NO_RADICADO);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        verify(requestTarget).queryParam("fecha_ini", FECHA_INI);
        verify(requestTarget).queryParam("fecha_fin", FECHA_FIN);
        verify(requestTarget).queryParam("cod_dependencia", COD_DEPENDENCIA);
        verify(requestTarget).queryParam("cod_estado", COD_ESTADO);
        verify(requestTarget).queryParam("nro_radicado", NO_RADICADO);
    }

    @Test
    public void listarObservaciones() {
        // given
        BigInteger ID_CORRESPONDENCIA = BigInteger.valueOf(100);
        String path = "/documento-web-api/documento/listar-observaciones/" + ID_CORRESPONDENCIA;
        JaxRsUtils.mockGetPath(wt, path);

        // when
        correspondenciaClient.listarObservaciones(ID_CORRESPONDENCIA);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);
    }

    @Test
    public void obtenerCorrespondenciaPorNroRadicado() {
        // given
        String NO_RADICADO = "100";
        String path = "/correspondencia-web-api/correspondencia/" + NO_RADICADO;
        JaxRsUtils.mockGetPath(wt, path);

        // when
        correspondenciaClient.obtenerCorrespondenciaPorNroRadicado(NO_RADICADO);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);
    }

    @Test
    public void obtenerCorrespondenciaFullPorNroRadicado() {
        // given
        String NO_RADICADO = "100";
        String path = "/correspondencia-web-api/correspondencia/full" + NO_RADICADO;
        JaxRsUtils.mockGetPath(wt, path);

        // when
        correspondenciaClient.obtenerCorrespondenciaFullPorNroRadicado(NO_RADICADO);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);
    }

    @Test
    public void asignarComunicaciones() {
        // given
        String path = "/asignacion-web-api/asignacion/asignar-funcionario";
        AsignacionTramiteDTO dto = mock(AsignacionTramiteDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, path);

        // when
        correspondenciaClient.asignarComunicaciones(dto);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<AsignacionTramiteDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }

    @Test
    public void obtenerFuncionarInfoParaReasignar() {
        // given
        BigInteger ID_AGENTE = BigInteger.valueOf(100);
        String path = "/asignacion-web-api/asignacion/re-asignacion/" + ID_AGENTE;
        JaxRsUtils.mockGetPath(wt, path);

        // when
        correspondenciaClient.obtenerFuncionarInfoParaReasignar(ID_AGENTE);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);
    }

    @Test
    public void redireccionarComunicaciones() {
        // given
        String path = "/agente-web-api/agente/redireccionar";
        RedireccionDTO dto = mock(RedireccionDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPutPath(wt, path);

        // when
        correspondenciaClient.redireccionarComunicaciones(dto);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<RedireccionDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).put(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }

    @Test
    public void devolverComunicaciones() {
        // given
        String path = "/agente-web-api/agente/devolver";
        DevolucionDTO dto = mock(DevolucionDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPutPath(wt, path);

        // when
        correspondenciaClient.devolverComunicaciones(dto);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<DevolucionDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).put(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }

    @Test
    public void obtenerContactoDestinatarioExterna() {
        // given
        String NO_RADICADO = "100";
        String path = "/agente-web-api/agente/remitente/" + NO_RADICADO;
        JaxRsUtils.mockGetPath(wt, path);

        // when
        correspondenciaClient.obtenerContactoDestinatarioExterna(NO_RADICADO);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);
    }

    @Test
    public void metricasTiempoDrools() {
        // given
        String path = "/regla";
        String payload = "PAYLOAD";
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, path);

        // when
        correspondenciaClient.metricasTiempoDrools(payload);

        // then
        verify(client).target(DROOLS_ENDPOINT);
        verify(wt).path(path);

        verify(requestBuilder).header("Authorization", "Basic " + DROOLS_ACCESS_TOKEN);
        verify(requestBuilder).header("X-KIE-ContentType", "json");
        verify(requestBuilder).header("Content-Type", "application/json");

        ArgumentCaptor<Entity<DevolucionDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(payload);
    }

    @Test
    public void verificarRedireccionesDrools() {
        // given
        String path = "/redireccion";
        String payload = "PAYLOAD";
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, path);

        // when
        correspondenciaClient.verificarRedireccionesDrools(payload);

        // then
        verify(client).target(DROOLS_ENDPOINT);
        verify(wt).path(path);

        verify(requestBuilder).header("Authorization", "Basic " + DROOLS_ACCESS_TOKEN);
        verify(requestBuilder).header("X-KIE-ContentType", "json");
        verify(requestBuilder).header("Content-Type", "application/json");

        ArgumentCaptor<Entity<DevolucionDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(payload);
    }

    @Test
    public void registrarObservacion() {
        // given
        String path = "/documento-web-api/documento/registrar-observacion";
        PpdTrazDocumentoDTO dto = mock(PpdTrazDocumentoDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, path);

        // when
        correspondenciaClient.registrarObservacion(dto);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<PpdTrazDocumentoDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }

    @Test
    public void obtnerContantesPorCodigo() {
        // given
        String CODIGOS = "100";
        String path = "/constantes-web-api/constantes";
        WebTarget requestTarget = JaxRsUtils.mockGetPath(wt, path);

        // when
        correspondenciaClient.obtnerContantesPorCodigo(CODIGOS);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);
        verify(requestTarget).queryParam("codigos", CODIGOS);
    }

    @Test
    public void listarDistribucion() {
        // given
        String path = "/correspondencia-web-api/correspondencia/listar-distribucion";
        String FECHA_INI = "01-01-2000";
        String FECHA_FIN = "01-01-2000";
        String COD_DEPENDENCIA = "CD01";
        String COD_TIPOLOGIA_DOCUMENTAL = "CTDE01";
        String NO_RADICADO = "NR01";

        WebTarget requestTarget = JaxRsUtils.mockGetPath(wt, path);

        // when
        correspondenciaClient.listarDistribucion(
                FECHA_INI, FECHA_FIN, COD_DEPENDENCIA,
                COD_TIPOLOGIA_DOCUMENTAL, NO_RADICADO);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        verify(requestTarget).queryParam("fecha_ini", FECHA_INI);
        verify(requestTarget).queryParam("fecha_fin", FECHA_FIN);
        verify(requestTarget).queryParam("cod_dependencia", COD_DEPENDENCIA);
        verify(requestTarget).queryParam("cod_tipologia_documental", COD_TIPOLOGIA_DOCUMENTAL);
        verify(requestTarget).queryParam("nro_radicado", NO_RADICADO);
    }

    @Test
    public void listarPlanillas() {
        // given
        String NO_PLANILLA = "100";
        String path = "/planillas-web-api/planillas/" + NO_PLANILLA;
        JaxRsUtils.mockGetPath(wt, path);

        // when
        correspondenciaClient.listarPlanillas(NO_PLANILLA);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);
    }

    @Test
    public void generarPlantilla() {
        // given
        String path = "/planillas-web-api/planillas";
        PlanillaDTO dto = mock(PlanillaDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, path);

        // when
        correspondenciaClient.generarPlantilla(dto);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<PlanillaDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }

    @Test
    public void cargarPlantilla() {
        // given
        String path = "/planillas-web-api/planillas";
        PlanillaDTO dto = mock(PlanillaDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPutPath(wt, path);

        // when
        correspondenciaClient.cargarPlantilla(dto);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<PlanillaDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).put(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }

    @Test
    public void exportarPlanilla() {
        // given
        String NO_PLANILLA = "100";
        String FORMATO = "100";
        String path = "/planillas-web-api/planillas/" + NO_PLANILLA + "/" + FORMATO;
        JaxRsUtils.mockGetPath(wt, path);

        // when
        correspondenciaClient.exportarPlanilla(NO_PLANILLA, FORMATO);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);
    }

    @Test
    public void restablecerCorrespondenciaEntrada() {
        // given
        String ID_PROCESO = "100";
        String ID_TAREA = "100";
        String path = "/tarea-web-api/tarea/" + ID_PROCESO + "/" + ID_TAREA;
        JaxRsUtils.mockGetPath(wt, path);

        // when
        correspondenciaClient.restablecerCorrespondenciaEntrada(ID_PROCESO, ID_TAREA);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);
    }

    @Test
    public void salvarCorrespondenciaEntrada() {
        // given
        String path = "/tarea-web-api/tarea";
        TareaDTO dto = mock(TareaDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, path);

        // when
        correspondenciaClient.salvarCorrespondenciaEntrada(dto);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<TareaDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }

    @Test
    public void actualizarComunicacion() {
        // given
        String path = "/correspondencia-web-api/correspondencia/actualizar-comunicacion";
        ComunicacionOficialDTO dto = mock(ComunicacionOficialDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPutPath(wt, path);

        // when
        correspondenciaClient.actualizarComunicacion(dto);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<ComunicacionOficialDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).put(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }

    @Test
    public void listarAnexos() {
        // given
        String NO_RADICADO = "100";
        String path = "correspondencia-web-api/anexo-web-api/anexo/" + NO_RADICADO;
        JaxRsUtils.mockGetPath(wt, path);

        // when
        correspondenciaClient.listarAnexos(NO_RADICADO);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);
    }

    @Test
    public void crearSolicitudUnidadDocuemntal() {
        // given
        String path = "/correspondencia-web-api/correspondencia/crear-solicitud-um";
        SolicitudesUnidadDocumentalDTO dto = mock(SolicitudesUnidadDocumentalDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, path);

        // when
        correspondenciaClient.crearSolicitudUnidadDocuemntal(dto);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<SolicitudesUnidadDocumentalDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }

    @Test
    public void listarSolicitudUnidadDocumentalNoTramitadas() {

        // given
        String path = "/correspondencia-web-api/correspondencia/obtener-solicitud-um-solicitante-sin-tramitar";
        String COD_SEDE = "CD01";
        String COD_DEPENDENCIA = "CD01";
        String ID_SOLICITANTE = "CTDE01";
        String FECHA_SOLICITUD = "01-01-2001";

        WebTarget requestTarget = JaxRsUtils.mockGetPath(wt, path);

        // when
        correspondenciaClient.listarSolicitudUnidadDocumentalNoTramitadas(
                COD_SEDE, COD_DEPENDENCIA,
                ID_SOLICITANTE, FECHA_SOLICITUD);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        verify(requestTarget).queryParam("cod_sede", COD_SEDE);
        verify(requestTarget).queryParam("cod_dependencia", COD_DEPENDENCIA);
        verify(requestTarget).queryParam("id_solicitante", ID_SOLICITANTE);
        verify(requestTarget).queryParam("fecha_in", FECHA_SOLICITUD);
    }

    @Test
    public void listarSolicitudUnidadDocumentalTramitadas() {

        // given
        String path = "/correspondencia-web-api/correspondencia/obtener-solicitud-um-solicitante";
        String COD_SEDE = "CD01";
        String COD_DEPENDENCIA = "CD01";
        String ID_SOLICITANTE = "CTDE01";

        WebTarget requestTarget = JaxRsUtils.mockGetPath(wt, path);

        // when
        correspondenciaClient.listarSolicitudUnidadDocumentalTramitadas(
                COD_SEDE, COD_DEPENDENCIA, ID_SOLICITANTE);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        verify(requestTarget).queryParam("cod_sede", COD_SEDE);
        verify(requestTarget).queryParam("cod_dependencia", COD_DEPENDENCIA);
        verify(requestTarget).queryParam("id_solicitante", ID_SOLICITANTE);
    }

    @Test
    public void actualizarSolicitudUnidadDcoumental() {
        // given
        String path = "/correspondencia-web-api/correspondencia/actualizar-solicitud-um";
        SolicitudUnidadDocumentalDTO dto = mock(SolicitudUnidadDocumentalDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPutPath(wt, path);

        // when
        correspondenciaClient.actualizarSolicitudUnidadDcoumental(dto);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<SolicitudUnidadDocumentalDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).put(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }
}