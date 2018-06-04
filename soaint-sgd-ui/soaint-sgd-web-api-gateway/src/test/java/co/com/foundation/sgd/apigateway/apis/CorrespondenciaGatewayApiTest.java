package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.CorrespondenciaClient;
import co.com.foundation.sgd.apigateway.apis.delegator.ProcesoClient;
import co.com.foundation.test.mocks.ApiUtils;
import co.com.foundation.test.mocks.JaxRsUtils;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.bpm.EstadosEnum;
import co.com.soaint.foundation.canonical.bpm.RespuestaTareaDTO;
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

        // given
        BigInteger ID_FUNCIONARIO = BigInteger.valueOf(400);
        BigInteger ID_AGENTE = BigInteger.valueOf(100);
        String LOGIN_NAME = "LOGIN NAME";
        String FUN_CREDENCIAL = "PASSWORD";
        Long ID_INSTANCIA = Long.valueOf(300);
        Long ID_TAREA = 200L;

        AsignacionesDTO RESULT = mock(AsignacionesDTO.class);

        AsignacionesDTO asignacionesDTO = AsignacionesDTO.newInstance()
                .asignaciones(Arrays.asList(
                        AsignacionDTO.newInstance()
                                .ideAgente(ID_AGENTE)
                                .loginName(LOGIN_NAME)
                                .build()
                ))
                .build();

        ReasignarComunicacionDTO dto = ReasignarComunicacionDTO.newInstance()
                .asignaciones(asignacionesDTO)
                .idFunc(ID_FUNCIONARIO)
                .build();

        List<RespuestaTareaDTO> tareasResponse = Arrays.asList(
                RespuestaTareaDTO.newInstance()
                        .idTarea(ID_TAREA)
                        .build()
        );

        FuncAsigDTO asigResponse = FuncAsigDTO.newInstance()
                .credenciales(FUN_CREDENCIAL)
                .asignacion(AsignacionDTO.newInstance()
                        .idInstancia(ID_INSTANCIA.toString())
                        .loginName(LOGIN_NAME)
                        .build())
                .build();

        Response entradaResponse = JaxRsUtils.mockGenericResponse(List.class, tareasResponse);
        Response resultResponse = JaxRsUtils.mockResponse(AsignacionesDTO.class, RESULT);
        Response funcionarioResponse = JaxRsUtils.mockResponse(FuncAsigDTO.class, asigResponse);

        when(procesoClient.listarPorUsuarioYPorIdProceso(any(EntradaProcesoDTO.class))).thenReturn(entradaResponse);
        when(procesoClient.reasignarTarea(any(EntradaProcesoDTO.class))).thenReturn(mock(Response.class));
        when(client.asignarComunicaciones(any(AsignacionTramiteDTO.class))).thenReturn(resultResponse);
        when(client.obtenerFuncionarInfoParaReasignar(ID_AGENTE)).thenReturn(funcionarioResponse);

        // when
        Response response = gatewayApi.reasignarComunicaciones(dto);

        // then
        ApiUtils.assertThat(CorrespondenciaGatewayApi.class, "reasignarComunicaciones")
                .hasPostMapping("/correspondencia-gateway-api/reasignar")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isSameAs(RESULT);

        ArgumentCaptor<EntradaProcesoDTO> tareasListCaptor = ArgumentCaptor.forClass(EntradaProcesoDTO.class);
        ArgumentCaptor<EntradaProcesoDTO> tareasReassignCaptor = ArgumentCaptor.forClass(EntradaProcesoDTO.class);
        ArgumentCaptor<AsignacionTramiteDTO> asignacionCaptor = ArgumentCaptor.forClass(AsignacionTramiteDTO.class);

        verify(client).obtenerFuncionarInfoParaReasignar(ID_AGENTE);
        verify(client).asignarComunicaciones(asignacionCaptor.capture());
        verify(procesoClient).reasignarTarea(tareasReassignCaptor.capture());
        verify(procesoClient).listarPorUsuarioYPorIdProceso(tareasListCaptor.capture());

        assertThat(tareasListCaptor.getAllValues()).allSatisfy(entradaProcesoDTO -> {
           assertThat(entradaProcesoDTO.getPass()).as("Credencial").isEqualTo(FUN_CREDENCIAL);
           assertThat(entradaProcesoDTO.getInstanciaProceso()).as("Id de Instancia").isEqualTo(ID_INSTANCIA);
           assertThat(entradaProcesoDTO.getParametros()).as("Parametro 'usuario'").contains(
                   entry("usuario", LOGIN_NAME)
           );
        });

        assertThat(tareasReassignCaptor.getAllValues()).allSatisfy(entradaProcesoDTO -> {
            assertThat(entradaProcesoDTO.getIdProceso()).as("Id de Proceso").isEqualTo("proceso.recibir-gestionar-doc");
            assertThat(entradaProcesoDTO.getIdDespliegue()).as("Id de Despliegue").isEqualTo("co.com.soaint.sgd.process:proceso-recibir-gestionar-doc:1.0.4-SNAPSHOT");
            assertThat(entradaProcesoDTO.getIdTarea()).as("Id de tarea").isEqualTo(ID_TAREA);
            assertThat(entradaProcesoDTO.getPass()).as("Credencial").isEqualTo(FUN_CREDENCIAL);
            assertThat(entradaProcesoDTO.getParametros()).as("Parametro 'usuario'").contains(
                    entry("usuarioReasignar", LOGIN_NAME)
            );
        });

        assertThat(asignacionCaptor.getValue()).satisfies(asignacion -> {
           assertThat(asignacion.getAsignaciones()).as("Asignaciones").isEqualTo(asignacionesDTO);
           assertThat(asignacion.getTraza()).as("Tiene Traza").isNotNull();
           assertThat(asignacion.getTraza().getIdeFunci()).as("Id Funcionario").isEqualTo(ID_FUNCIONARIO);
        });
    }

    @Test
    public void reasignarComunicacionesFail() {

        // given
        BigInteger ID_FUNCIONARIO = BigInteger.valueOf(400);

        AsignacionesDTO RESULT = mock(AsignacionesDTO.class);

        AsignacionesDTO asignacionesDTO = AsignacionesDTO.newInstance()
                .asignaciones(Arrays.asList())
                .build();

        ReasignarComunicacionDTO dto = ReasignarComunicacionDTO.newInstance()
                .asignaciones(asignacionesDTO)
                .idFunc(ID_FUNCIONARIO)
                .build();


        Response resultResponse = JaxRsUtils.mockResponse(AsignacionesDTO.class, RESULT, Response.Status.BAD_REQUEST);

        when(client.asignarComunicaciones(any(AsignacionTramiteDTO.class))).thenReturn(resultResponse);

        // when
        Response response = gatewayApi.reasignarComunicaciones(dto);

        // then
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(new ArrayList<>());
    }

    @Test
    public void redireccionarComunicaciones() {
        // given
        RedireccionDTO dto = mock(RedireccionDTO.class);
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.redireccionarComunicaciones(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.redireccionarComunicaciones(dto);

        // then
        ApiUtils.assertThat(CorrespondenciaGatewayApi.class, "redireccionarComunicaciones")
                .hasPostMapping("/correspondencia-gateway-api/redireccionar")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void devolverComunicaciones() {
        // given
        List<ItemDevolucionDTO> ITEMS_DEVOLUCION = Arrays.asList(
                ItemDevolucionDTO.newInstance().build(),
                ItemDevolucionDTO.newInstance().build(),
                ItemDevolucionDTO.newInstance().build()
        );

        DevolucionDTO dto = DevolucionDTO.newInstance()
                .itemsDevolucion(ITEMS_DEVOLUCION)
                .build();

        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.devolverComunicaciones(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.devolverComunicaciones(dto);

        // then
        ApiUtils.assertThat(CorrespondenciaGatewayApi.class, "devolverComunicaciones")
                .hasPostMapping("/correspondencia-gateway-api/devolver")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);

        ArgumentCaptor<EntradaProcesoDTO> captor = ArgumentCaptor.forClass(EntradaProcesoDTO.class);
        int procesoDevolucionesCallsCount = (int) Math.pow(ITEMS_DEVOLUCION.size(), 2); // Esto debe revisarse
        verify(procesoClient, times(procesoDevolucionesCallsCount)).iniciarProcesoGestorDevoluciones(any(ItemDevolucionDTO.class), captor.capture());

        assertThat(captor.getAllValues()).allSatisfy(entradaProcesoDTO -> {
            assertThat(entradaProcesoDTO.getIdProceso()).isEqualTo("proceso.gestor-devoluciones");
            assertThat(entradaProcesoDTO.getIdDespliegue()).isEqualTo("co.com.soaint.sgd.process:proceso-gestor-devoluciones:1.0.0-SNAPSHOT");
        });
    }

    @Test
    public void devolverComunicacionesAsignacion() {
        // given
        List<ItemDevolucionDTO> ITEMS_DEVOLUCION = Arrays.asList(
                ItemDevolucionDTO.newInstance().build(),
                ItemDevolucionDTO.newInstance().build(),
                ItemDevolucionDTO.newInstance().build()
        );

        DevolucionDTO dto = DevolucionDTO.newInstance()
                .itemsDevolucion(ITEMS_DEVOLUCION)
                .build();

        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.devolverComunicaciones(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.devolverComunicacionesAsignacion(dto);

        // then
        ApiUtils.assertThat(CorrespondenciaGatewayApi.class, "devolverComunicacionesAsignacion")
                .hasPostMapping("/correspondencia-gateway-api/devolver/asignacion")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);

        ArgumentCaptor<EntradaProcesoDTO> captor = ArgumentCaptor.forClass(EntradaProcesoDTO.class);
        verify(procesoClient, times(ITEMS_DEVOLUCION.size())).iniciarProcesoGestorDevoluciones(any(ItemDevolucionDTO.class), captor.capture());

        assertThat(captor.getAllValues()).allSatisfy(entradaProcesoDTO -> {
            assertThat(entradaProcesoDTO.getIdProceso()).isEqualTo("proceso.gestor-devoluciones");
            assertThat(entradaProcesoDTO.getIdDespliegue()).isEqualTo("co.com.soaint.sgd.process:proceso-gestor-devoluciones:1.0.0-SNAPSHOT");
        });
    }

    @Test
    public void metricasTiempo() {
        // given
        String PAYLOAD = "PAYLOAD";
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.metricasTiempoDrools(PAYLOAD)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.metricasTiempo(PAYLOAD);

        // then
        ApiUtils.assertThat(CorrespondenciaGatewayApi.class, "metricasTiempo")
                .hasGetMapping("/correspondencia-gateway-api/metricasTiempo")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasQueryParams("payload")
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void obtenerContactoDestinatarioExterna() {
        // given
        String NO_RADICADO = "NR01";
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.obtenerContactoDestinatarioExterna(NO_RADICADO)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.obtenerContactoDestinatarioExterna(NO_RADICADO);

        // then
        ApiUtils.assertThat(CorrespondenciaGatewayApi.class, "obtenerContactoDestinatarioExterna")
                .hasGetMapping("/correspondencia-gateway-api/contactos-destinatario-externo/{nro_radicado}")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void obtenerComunicacion() {
        // given
        String NO_RADICADO = "NR01";
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.obtenerCorrespondenciaPorNroRadicado(NO_RADICADO)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.obtenerComunicacion(NO_RADICADO);

        // then
        ApiUtils.assertThat(CorrespondenciaGatewayApi.class, "obtenerComunicacion")
                .hasGetMapping("/correspondencia-gateway-api/obtener-comunicacion/{nro_radicado}")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void obtenerComunicacionfull() {
        // given
        String NO_RADICADO = "NR01";
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.obtenerCorrespondenciaFullPorNroRadicado(NO_RADICADO)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.obtenerComunicacionfull(NO_RADICADO);

        // then
        ApiUtils.assertThat(CorrespondenciaGatewayApi.class, "obtenerComunicacionfull")
                .hasGetMapping("/correspondencia-gateway-api/obtener-comunicacion-full/{nro_radicado}")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void obtenerObservaciones() {
        // given
        BigInteger ID_CORRESPONDENCIA = BigInteger.valueOf(100);
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.listarObservaciones(ID_CORRESPONDENCIA)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.obtenerObservaciones(ID_CORRESPONDENCIA);

        // then
        ApiUtils.assertThat(CorrespondenciaGatewayApi.class, "obtenerObservaciones")
                .hasGetMapping("/correspondencia-gateway-api/obtenerObservaciones/{idCorrespondencia}")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void registrarObservacion() {
        // given
        PpdTrazDocumentoDTO dto = mock(PpdTrazDocumentoDTO.class);
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.registrarObservacion(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.registrarObservacion(dto);

        // then
        ApiUtils.assertThat(CorrespondenciaGatewayApi.class, "registrarObservacion")
                .hasPostMapping("/correspondencia-gateway-api/registrarObservacion")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void constantes() {
        // given
        String CODIGOS = "COD01";
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.obtnerContantesPorCodigo(CODIGOS)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.constantes(CODIGOS);

        // then
        ApiUtils.assertThat(CorrespondenciaGatewayApi.class, "constantes")
                .hasGetMapping("/correspondencia-gateway-api/constantes")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void listarDistribucion() {
        // given
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.listarDistribucion(null, null, null, null, null)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.listarDistribucion(null, null, null, null, null);

        // then
        ApiUtils.assertThat(CorrespondenciaGatewayApi.class, "listarDistribucion")
                .hasGetMapping("/correspondencia-gateway-api/listar-distribucion")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity()
                .hasQueryParams("fecha_ini", "fecha_fin", "cod_dependencia", "cod_tipologia_documental", "nro_radicado");

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void listarDistribucionFail() {
        // given
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT, Response.Status.BAD_REQUEST);
        when(client.listarDistribucion(null, null, null, null, null)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.listarDistribucion(null, null, null, null, null);

        // then
        ApiUtils.assertThat(CorrespondenciaGatewayApi.class, "listarDistribucion")
                .hasGetMapping("/correspondencia-gateway-api/listar-distribucion")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity()
                .hasQueryParams("fecha_ini", "fecha_fin", "cod_dependencia", "cod_tipologia_documental", "nro_radicado");

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(new ArrayList<>());
    }

    @Test
    public void listarPlanillas() {
        // given
        String NRO_PLANILLA = "NP01";
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.listarPlanillas(NRO_PLANILLA)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.listarPlanillas(null, null, null, null, NRO_PLANILLA);

        // then
        ApiUtils.assertThat(CorrespondenciaGatewayApi.class, "listarPlanillas")
                .hasGetMapping("/correspondencia-gateway-api/listar-planillas")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity()
                .hasQueryParams("fecha_ini", "fecha_fin", "cod_dependencia", "cod_tipologia_documental", "nro_planilla");

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void listarPlanillasFail() {
        // given
        String NRO_PLANILLA = "NP01";
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT, Response.Status.BAD_REQUEST);
        when(client.listarPlanillas(NRO_PLANILLA)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.listarPlanillas(null, null, null, null, NRO_PLANILLA);

        // then
        ApiUtils.assertThat(CorrespondenciaGatewayApi.class, "listarPlanillas")
                .hasGetMapping("/correspondencia-gateway-api/listar-planillas")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity()
                .hasQueryParams("fecha_ini", "fecha_fin", "cod_dependencia", "cod_tipologia_documental", "nro_planilla");

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isInstanceOf(PlanillaDTO.class);
    }

    @Test
    public void generarPlanilla() {
        // given
        String NRO_PLANILLA = "NP01";
        String COD_DEPENDENCIA_ORIGEN = "CDO01";
        PlanillaDTO dto = PlanillaDTO.newInstance().codDependenciaOrigen(COD_DEPENDENCIA_ORIGEN).build();
        PlanillaDTO dtoResponse = PlanillaDTO.newInstance().nroPlanilla(NRO_PLANILLA).build();
        Response theResponse = JaxRsUtils.mockResponse(PlanillaDTO.class, dtoResponse);
        when(client.generarPlantilla(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.generarPlanilla(dto);

        // then
        ApiUtils.assertThat(CorrespondenciaGatewayApi.class, "generarPlanilla")
                .hasPostMapping("/correspondencia-gateway-api/generar-plantilla")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON);

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(dtoResponse);

        ArgumentCaptor<EntradaProcesoDTO> captor = ArgumentCaptor.forClass(EntradaProcesoDTO.class);
        verify(procesoClient).iniciarTercero(captor.capture());

        EntradaProcesoDTO entradaAIniciar = captor.getValue();
        assertThat(entradaAIniciar.getIdProceso()).isEqualTo("proceso.gestion-planillas");
        assertThat(entradaAIniciar.getIdDespliegue()).isEqualTo("co.com.soaint.sgd.process:proceso-gestion-planillas:1.0.0-SNAPSHOT");
        assertThat(entradaAIniciar.getParametros()).containsOnly(
                entry("numPlanilla", NRO_PLANILLA),
                entry("codDependencia", COD_DEPENDENCIA_ORIGEN)
        );
    }

    @Test
    public void cargarPlanilla() {
        // given
        PlanillaDTO dto = mock(PlanillaDTO.class);
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.cargarPlantilla(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.cargarPlanilla(dto);

        // then
        ApiUtils.assertThat(CorrespondenciaGatewayApi.class, "cargarPlanilla")
                .hasPostMapping("/correspondencia-gateway-api/cargar-plantilla")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON);

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void exportarPlanilla() {
        // given
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.exportarPlanilla(null, null)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.exportarPlanilla(null, null);

        // then
        ApiUtils.assertThat(CorrespondenciaGatewayApi.class, "exportarPlanilla")
                .hasGetMapping("/correspondencia-gateway-api/exportar-plantilla")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasQueryParams("nroPlanilla", "formato");

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void salvarCorrespondenciaEntrada() {
        // given
        TareaDTO dto = mock(TareaDTO.class);
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.salvarCorrespondenciaEntrada(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.salvarCorrespondenciaEntrada(dto);

        // then
        ApiUtils.assertThat(CorrespondenciaGatewayApi.class, "salvarCorrespondenciaEntrada")
                .hasPostMapping("/correspondencia-gateway-api/salvar-correspondencia-entrada")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON);

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void salvarCorrespondenciaEntradaFail() {
        // given
        TareaDTO dto = mock(TareaDTO.class);
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT, Response.Status.NO_CONTENT);
        when(client.salvarCorrespondenciaEntrada(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.salvarCorrespondenciaEntrada(dto);

        // then
        ApiUtils.assertThat(CorrespondenciaGatewayApi.class, "salvarCorrespondenciaEntrada")
                .hasPostMapping("/correspondencia-gateway-api/salvar-correspondencia-entrada")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(new ArrayList<>());
    }

    @Test
    public void actualizarComunicacion() {
        // given
        ComunicacionOficialDTO dto = mock(ComunicacionOficialDTO.class);
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.actualizarComunicacion(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.actualizarComunicacion(dto);

        // then
        ApiUtils.assertThat(CorrespondenciaGatewayApi.class, "actualizarComunicacion")
                .hasPutMapping("/correspondencia-gateway-api/actualizar-comunicacion")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void restablecerCorrespondenciaEntrada() {
        // given
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.restablecerCorrespondenciaEntrada(null, null)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.restablecerCorrespondenciaEntrada(null, null);

        // then
        ApiUtils.assertThat(CorrespondenciaGatewayApi.class, "restablecerCorrespondenciaEntrada")
                .hasGetMapping("/correspondencia-gateway-api/restablecer_correspondencia_entrada/{proceso}/{tarea}")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void restablecerCorrespondenciaEntradaFail() {
        // given
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT, Response.Status.INTERNAL_SERVER_ERROR);
        when(client.restablecerCorrespondenciaEntrada(null, null)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.restablecerCorrespondenciaEntrada(null, null);

        // then
        ApiUtils.assertThat(CorrespondenciaGatewayApi.class, "restablecerCorrespondenciaEntrada")
                .hasGetMapping("/correspondencia-gateway-api/restablecer_correspondencia_entrada/{proceso}/{tarea}")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(new ArrayList<>());
    }

    @Test
    public void verificarRedirecciones() {
        // given
        String PAYLOAD = "PAYLOAD";
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.verificarRedireccionesDrools(PAYLOAD)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.verificarRedirecciones(PAYLOAD);

        // then
        ApiUtils.assertThat(CorrespondenciaGatewayApi.class, "verificarRedirecciones")
                .hasGetMapping("/correspondencia-gateway-api/verificar-redirecciones")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity()
                .hasQueryParams("payload");

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void listarAnexos() {
        // given
        String NRO_RADICADO = "PAYLOAD";
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.listarAnexos(NRO_RADICADO)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.listarAnexos(NRO_RADICADO);

        // then
        ApiUtils.assertThat(CorrespondenciaGatewayApi.class, "listarAnexos")
                .hasGetMapping("/correspondencia-gateway-api/listar-anexos/{nroRadicado}")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void crearSolicitudUnidad() {
        // given
        SolicitudesUnidadDocumentalDTO dto = mock(SolicitudesUnidadDocumentalDTO.class);
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.crearSolicitudUnidadDocuemntal(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.crearSolicitudUnidad(dto);

        // then
        ApiUtils.assertThat(CorrespondenciaGatewayApi.class, "crearSolicitudUnidad")
                .hasPostMapping("/correspondencia-gateway-api/crear-solicitud-unidad-documental")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void listarSolicitudUnidadNoTramitadas() {
        // given
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.listarSolicitudUnidadDocumentalNoTramitadas(null, null, null, null)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.listarSolicitudUnidadNoTramitadas(null, null, null, null);

        // then
        ApiUtils.assertThat(CorrespondenciaGatewayApi.class, "listarSolicitudUnidadNoTramitadas")
                .hasGetMapping("/correspondencia-gateway-api/listar-solicitud-ud-no-tramitadas")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity()
                .hasQueryParams("codSede", "codDependencia", "idSolicitante", "fechaSolicitud");

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void listarSolicitudUnidadTramitadas() {
        // given
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.listarSolicitudUnidadDocumentalTramitadas(null, null, null)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.listarSolicitudUnidadTramitadas(null, null, null);

        // then
        ApiUtils.assertThat(CorrespondenciaGatewayApi.class, "listarSolicitudUnidadTramitadas")
                .hasGetMapping("/correspondencia-gateway-api/listar-solicitud-ud-tramitadas")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity()
                .hasQueryParams("codSede", "codDependencia", "idSolicitante");

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void actualizarSolicitudUnidad() {
        // given
        SolicitudUnidadDocumentalDTO dto = mock(SolicitudUnidadDocumentalDTO.class);
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.actualizarSolicitudUnidadDcoumental(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.actualizarSolicitudUnidad(dto);

        // then
        ApiUtils.assertThat(CorrespondenciaGatewayApi.class, "actualizarSolicitudUnidad")
                .hasPutMapping("/correspondencia-gateway-api/actualizar-solicitud-unidad-documental")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }
}