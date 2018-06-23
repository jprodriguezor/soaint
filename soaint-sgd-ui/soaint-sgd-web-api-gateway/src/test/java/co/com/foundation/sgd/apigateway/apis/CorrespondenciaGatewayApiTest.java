package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.CorrespondenciaClient;
import co.com.foundation.sgd.apigateway.apis.delegator.ProcesoClient;
import co.com.foundation.test.mocks.ApiUtils;
import co.com.foundation.test.mocks.JaxRsUtils;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.bpm.EstadosEnum;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.canonical.ui.ReasignarComunicacionDTO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CorrespondenciaGatewayApiTest {

    private final String JSON_CONTENT = "{\"data\": 100}";

    private CorrespondenciaGatewayApi gatewayApi;

    private CorrespondenciaClient client;

    private ProcesoClient procesoClient;

    @Before
    public void setup() {

        gatewayApi = new CorrespondenciaGatewayApi();

        client = mock(CorrespondenciaClient.class);
        procesoClient = mock(ProcesoClient.class);

        ReflectionTestUtils.setField(gatewayApi, "client", client);
        ReflectionTestUtils.setField(gatewayApi, "procesoClient", procesoClient);
    }


    @Test
    public void listarComunicaciones() {

        // given
        String FECHA_INI = "01/05/2018";
        String FECHA_FIN = "01/06/2018";
        String COD_DEPENDENCIA = "CD01";
        String COD_ESTADO = "CE01";
        String NRO_RADICADO = "CR01";

        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.listarComunicaciones(FECHA_INI, FECHA_FIN, COD_DEPENDENCIA, COD_ESTADO, NRO_RADICADO)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.listarComunicaciones(FECHA_INI, FECHA_FIN, COD_DEPENDENCIA, COD_ESTADO, NRO_RADICADO);

        // then
        ApiUtils.assertThat(CorrespondenciaGatewayApi.class, "listarComunicaciones")
                .hasGetMapping("/correspondencia-gateway-api/listar-comunicaciones")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasQueryParams("fecha_ini", "fecha_fin", "cod_dependencia", "cod_estado", "nro_radicado")
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void listarComunicacionesFail() {

        // given
        String FECHA_INI = "01/05/2018";
        String FECHA_FIN = "01/06/2018";
        String COD_DEPENDENCIA = "CD01";
        String COD_ESTADO = "CE01";
        String NRO_RADICADO = "CR01";

        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT, Response.Status.BAD_REQUEST);
        when(client.listarComunicaciones(FECHA_INI, FECHA_FIN, COD_DEPENDENCIA, COD_ESTADO, NRO_RADICADO)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.listarComunicaciones(FECHA_INI, FECHA_FIN, COD_DEPENDENCIA, COD_ESTADO, NRO_RADICADO);

        // then
        ApiUtils.assertThat(CorrespondenciaGatewayApi.class, "listarComunicaciones")
                .hasGetMapping("/correspondencia-gateway-api/listar-comunicaciones")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasQueryParams("fecha_ini", "fecha_fin", "cod_dependencia", "cod_estado", "nro_radicado")
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(new ArrayList<>());
    }

    @Test
    public void radicarComunicacion() {

        // given
        ComunicacionOficialDTO dto = mock(ComunicacionOficialDTO.class);
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.radicar(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.radicarComunicacion(dto);

        // then
        ApiUtils.assertThat(CorrespondenciaGatewayApi.class, "radicarComunicacion")
                .hasPostMapping("/correspondencia-gateway-api/radicar")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();


        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void radicarSalida() {

        // given
        ComunicacionOficialRemiteDTO dto = mock(ComunicacionOficialRemiteDTO.class);
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.radicarSalida(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.radicarSalida(dto);

        // then
        ApiUtils.assertThat(CorrespondenciaGatewayApi.class, "radicarSalida")
                .hasPostMapping("/correspondencia-gateway-api/radicar_salida")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void asignarComunicaciones() {

        // given
        BigInteger ID_ASIGNACION = BigInteger.valueOf(100);
        BigInteger ID_AGENTE = BigInteger.valueOf(200);
        BigInteger ID_DOCUMENTO = BigInteger.valueOf(300);
        String LOGIN_NAME = "LOGIN_NAME";
        String NRO_RADICADO = "NR01";
        Date FECHA_RADICADO = new Date();
        String COD_DEPENDENCIA = "CD01";
        String ALERTA_VENCIMIENTO = "00:00";

        AsignacionTramiteDTO dto = AsignacionTramiteDTO.newInstance().build();

        AsignacionesDTO ASIGNACIONES = AsignacionesDTO.newInstance()
                .asignaciones(Arrays.asList(
                        AsignacionDTO.newInstance()
                                .ideAsignacion(ID_ASIGNACION)
                                .ideAgente(ID_AGENTE)
                                .loginName(LOGIN_NAME)
                                .ideDocumento(ID_DOCUMENTO)
                                .nroRadicado(NRO_RADICADO)
                                .fecRadicado(FECHA_RADICADO)
                                .codDependencia(COD_DEPENDENCIA)
                                .alertaVencimiento(ALERTA_VENCIMIENTO)
                                .build()
                ))
                .build();

        Response theResponse = JaxRsUtils.mockResponse(AsignacionesDTO.class, ASIGNACIONES);
        when(client.asignarComunicaciones(dto)).thenReturn(theResponse);
        when(procesoClient.iniciarTercero(any(EntradaProcesoDTO.class))).thenReturn(mock(Response.class));

        // when
        Response response = gatewayApi.asignarComunicaciones(dto);

        // then
        ApiUtils.assertThat(CorrespondenciaGatewayApi.class, "asignarComunicaciones")
                .hasPostMapping("/correspondencia-gateway-api/asignar")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(ASIGNACIONES);

        ArgumentCaptor<EntradaProcesoDTO> captor = ArgumentCaptor.forClass(EntradaProcesoDTO.class);
        verify(procesoClient, times(ASIGNACIONES.getAsignaciones().size()))
                .iniciarTercero(captor.capture());

        List<EntradaProcesoDTO> procesoDTOs = captor.getAllValues();

        assertThat(procesoDTOs).allSatisfy(proceso -> {
           assertThat(proceso.getIdProceso()).isEqualTo("proceso.recibir-gestionar-doc");
           assertThat(proceso.getIdDespliegue()).isEqualTo("co.com.soaint.sgd.process:proceso-recibir-gestionar-doc:1.0.4-SNAPSHOT");
           assertThat(proceso.getEstados()).containsOnly(EstadosEnum.LISTO);
           assertThat(proceso.getEstados()).containsOnly(EstadosEnum.LISTO);

           assertThat(proceso.getParametros()).containsOnly(
             entry("idAsignacion", ID_ASIGNACION.toString()),
             entry("idAgente", ID_AGENTE.toString()),
             entry("usuario", LOGIN_NAME),
             entry("idDocumento", ID_DOCUMENTO.toString()),
             entry("numeroRadicado", NRO_RADICADO),
             entry("fechaRadicacion", FECHA_RADICADO),
             entry("codDependencia", COD_DEPENDENCIA),
             entry("fechaVencimiento", ALERTA_VENCIMIENTO)
           );
        });
    }

    @Test
    public void asignarComunicacionesFail() {

        // given
        List<AsignacionDTO> EMPTY_LIST = new ArrayList<>();
        AsignacionTramiteDTO dto = AsignacionTramiteDTO.newInstance().build();

        AsignacionesDTO ASIGNACIONES = AsignacionesDTO.newInstance()
                .asignaciones(EMPTY_LIST)
                .build();

        Response theResponse = JaxRsUtils.mockResponse(AsignacionesDTO.class, ASIGNACIONES, Response.Status.BAD_REQUEST);
        when(client.asignarComunicaciones(dto)).thenReturn(theResponse);
        when(procesoClient.iniciarTercero(any(EntradaProcesoDTO.class))).thenReturn(mock(Response.class));

        // when
        Response response = gatewayApi.asignarComunicaciones(dto);

        // then
        ApiUtils.assertThat(CorrespondenciaGatewayApi.class, "asignarComunicaciones")
                .hasPostMapping("/correspondencia-gateway-api/asignar")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(EMPTY_LIST);
    }

    @Test
    public void reasignarComunicaciones() {

//        // given
//        ReasignarComunicacionDTO dto = mock(ReasignarComunicacionDTO.class);
//        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
////        when(client.asignarComunicaciones(dto)).thenReturn(theResponse);
//
//        // when
//        Response response = gatewayApi.reasignarComunicaciones(dto);
//
//        // then
//        ApiUtils.assertThat(CorrespondenciaGatewayApi.class, "radicarSalida")
//                .hasPostMapping("/correspondencia-gateway-api/reasignar")
//                .produces(MediaType.APPLICATION_JSON)
//                .consumes(MediaType.APPLICATION_JSON)
//                .hasJWTTokenSecurity();
//
//        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
//        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void redireccionarComunicaciones() {
    }

    @Test
    public void devolverComunicaciones() {
    }

    @Test
    public void devolverComunicacionesAsignacion() {
    }

    @Test
    public void metricasTiempo() {
    }

    @Test
    public void obtenerContactoDestinatarioExterna() {
    }

    @Test
    public void obtenerComunicacion() {
    }

    @Test
    public void obtenerComunicacionfull() {
    }

    @Test
    public void obtenerObservaciones() {
    }

    @Test
    public void registrarObservacion() {
    }

    @Test
    public void constantes() {
    }

    @Test
    public void listarDistribucion() {
    }

    @Test
    public void listarPlanillas() {
    }

    @Test
    public void generarPlanilla() {
    }

    @Test
    public void cargarPlanilla() {
    }

    @Test
    public void exportarPlanilla() {
    }

    @Test
    public void salvarCorrespondenciaEntrada() {
    }

    @Test
    public void actualizarComunicacion() {
    }

    @Test
    public void restablecerCorrespondenciaEntrada() {
    }

    @Test
    public void verificarRedirecciones() {
    }

    @Test
    public void listarAnexos() {
    }

    @Test
    public void crearSolicitudUnidad() {
    }

    @Test
    public void listarSolicitudUnidadNoTramitadas() {
    }

    @Test
    public void listarSolicitudUnidadTramitadas() {
    }

    @Test
    public void actualizarSolicitudUnidad() {
    }
}