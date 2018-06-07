package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.ProcesoClient;
import co.com.foundation.test.mocks.ApiUtils;
import co.com.foundation.test.mocks.JaxRsUtils;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.bpm.RespuestaTareaBamDTO;
import co.com.soaint.foundation.canonical.bpm.RespuestaTareaDTO;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProcesoGatewayApiTest {

    private final String JSON_CONTENT = "{\"data\": 100}";

    private ProcesoGatewayApi gatewayApi;

    private ProcesoClient client;

    @Before
    public void setUp() {

        gatewayApi = new ProcesoGatewayApi();

        client = mock(ProcesoClient.class);

        ReflectionTestUtils.setField(gatewayApi, "procesoClient", client);
    }

    @Test
    public void list() {

        // given
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.list()).thenReturn(theResponse);

        // when
        Response response = gatewayApi.list();

        // then
        ApiUtils.assertThat(ProcesoGatewayApi.class, "list")
                .hasGetMapping("/proceso-gateway-api")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void iniciarProceso() {
        // given
        EntradaProcesoDTO dto = mock(EntradaProcesoDTO.class);
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.iniciarManual(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.iniciarProceso(dto);

        // then
        ApiUtils.assertThat(ProcesoGatewayApi.class, "iniciarProceso")
                .hasPostMapping("/proceso-gateway-api/iniciar")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void listTareasIdProceso() {
        // given
        EntradaProcesoDTO dto = mock(EntradaProcesoDTO.class);
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.listarPorIdProceso(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.listTareasIdProceso(dto);

        // then
        ApiUtils.assertThat(ProcesoGatewayApi.class, "listTareasIdProceso")
                .hasPostMapping("/proceso-gateway-api/listar/estados-instancia")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void listTareas() {
        // given
        String COD_DEPENDENCIA_FAKE = "CD01";
        String COD_DEPENDENCIA = "CD02";
        EntradaProcesoDTO dto = EntradaProcesoDTO.newInstance()
                .parametros(Collections.singletonMap("codDependencia", COD_DEPENDENCIA))
                .build();

        RespuestaTareaDTO tareaDTO1 = RespuestaTareaDTO.newInstance()
                .codigoDependencia(COD_DEPENDENCIA)
                .build();
        RespuestaTareaDTO tareaDTO2 = RespuestaTareaDTO.newInstance()
                .codigoDependencia(COD_DEPENDENCIA)
                .build();
        RespuestaTareaDTO tareaDTO3 = RespuestaTareaDTO.newInstance()
                .codigoDependencia(COD_DEPENDENCIA_FAKE)
                .build();
        List<RespuestaTareaDTO> responseContent = Arrays.asList(tareaDTO1, tareaDTO2, tareaDTO3);
        Response theResponse = JaxRsUtils.mockGenericResponse(List.class, responseContent);
        when(client.listarTareas(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.listTareas(dto);

        // then
        ApiUtils.assertThat(ProcesoGatewayApi.class, "listTareas")
                .hasPostMapping("/proceso-gateway-api/tareas/listar/estados")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        List<RespuestaTareaDTO> tareasCompletadas = (List<RespuestaTareaDTO>) response.getEntity();

        assertThat(tareasCompletadas).containsOnly(tareaDTO1, tareaDTO2);
    }

    @Test
    public void listTareasFail() {
        // given
        EntradaProcesoDTO dto = mock(EntradaProcesoDTO.class);

        when(client.listarTareas(dto)).thenThrow(RuntimeException.class);

        // when
        Response response = gatewayApi.listTareas(dto);

        // then
        ApiUtils.assertThat(ProcesoGatewayApi.class, "listTareas")
                .hasPostMapping("/proceso-gateway-api/tareas/listar/estados")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void listTareasCompletadas() {
        // given
        EntradaProcesoDTO dto = mock(EntradaProcesoDTO.class);
        String TASK_STATUS_1 = "STATUS 1";
        String TASK_STATUS_2 = "STATUS 2";
        String TASK_NAME_1 = "TASK 1";
        String TASK_NAME_2 = "TASK 2";
        Integer TASK_ID_1 = 100;
        Integer TASK_ID_2 = 200;
        List<RespuestaTareaBamDTO> responseContent = Arrays.asList(
                RespuestaTareaBamDTO.newInstance()
                        .taskid(TASK_ID_1)
                        .status(TASK_STATUS_1)
                        .taskname(TASK_NAME_1)
                        .build(),
                RespuestaTareaBamDTO.newInstance()
                        .taskid(TASK_ID_2)
                        .status(TASK_STATUS_2)
                        .taskname(TASK_NAME_2)
                        .build()
        );
        Response theResponse = JaxRsUtils.mockGenericResponse(List.class, responseContent);
        when(client.listarTareasCompletadas(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.listTareasCompletadas(dto);

        // then
        ApiUtils.assertThat(ProcesoGatewayApi.class, "listTareasCompletadas")
                .hasPostMapping("/proceso-gateway-api/tareas/listar/completadas")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        List<RespuestaTareaDTO> tareasCompletadas = (List<RespuestaTareaDTO>) response.getEntity();

        assertThat(tareasCompletadas).anySatisfy(tareaDTO -> {
           assertThat(tareaDTO.getIdTarea()).isEqualTo((long)TASK_ID_1);
           assertThat(tareaDTO.getEstado()).isEqualTo(TASK_STATUS_1);
           assertThat(tareaDTO.getNombre()).isEqualTo(TASK_NAME_1);
        });

        assertThat(tareasCompletadas).anySatisfy(tareaDTO -> {
            assertThat(tareaDTO.getIdTarea()).isEqualTo((long)TASK_ID_2);
            assertThat(tareaDTO.getEstado()).isEqualTo(TASK_STATUS_2);
            assertThat(tareaDTO.getNombre()).isEqualTo(TASK_NAME_2);
        });
    }

    @Test
    public void listEstadisticasTareas() {
        // given
        EntradaProcesoDTO dto = mock(EntradaProcesoDTO.class);
        List<RespuestaTareaBamDTO> responseContent = mock(List.class);
        Response theResponse = JaxRsUtils.mockGenericResponse(List.class, responseContent);
        when(client.listarEstadisticasTareas(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.listEstadisticasTareas(dto);

        // then
        ApiUtils.assertThat(ProcesoGatewayApi.class, "listEstadisticasTareas")
                .hasPostMapping("/proceso-gateway-api/tareas/listar/usuario")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(responseContent);
    }

    @Test
    public void iniciarTarea() {
        // given
        EntradaProcesoDTO dto = mock(EntradaProcesoDTO.class);
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.iniciarTarea(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.iniciarTarea(dto);

        // then
        ApiUtils.assertThat(ProcesoGatewayApi.class, "iniciarTarea")
                .hasPostMapping("/proceso-gateway-api/tareas/iniciar")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void reservarTarea() {
        // given
        EntradaProcesoDTO dto = mock(EntradaProcesoDTO.class);
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.reservarTarea(dto)).thenReturn(theResponse);
        when(client.iniciarTarea(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.reservarTarea(dto);

        // then
        ApiUtils.assertThat(ProcesoGatewayApi.class, "reservarTarea")
                .hasPostMapping("/proceso-gateway-api/tareas/reservar")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON);

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void completarTarea() {
        // given
        EntradaProcesoDTO dto = mock(EntradaProcesoDTO.class);
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.completarTarea(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.completarTarea(dto);

        // then
        ApiUtils.assertThat(ProcesoGatewayApi.class, "completarTarea")
                .hasPostMapping("/proceso-gateway-api/tareas/completar")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON);

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void abortarTarea() {
        // given
        EntradaProcesoDTO dto = mock(EntradaProcesoDTO.class);
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.abortarTarea(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.abortarTarea(dto);

        // then
        ApiUtils.assertThat(ProcesoGatewayApi.class, "abortarTarea")
                .hasPostMapping("/proceso-gateway-api/tareas/abortar")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void listarIntanciasProceso() {

        // given
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.listarIntanciasProceso()).thenReturn(theResponse);

        // when
        Response response = gatewayApi.listarIntanciasProceso();

        // then
        ApiUtils.assertThat(ProcesoGatewayApi.class, "listarIntanciasProceso")
                .hasGetMapping("/proceso-gateway-api/proceso/listar-instancias")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }

    @Test
    public void obtenerVaraiblesTarea() {
        // given
        EntradaProcesoDTO dto = mock(EntradaProcesoDTO.class);
        Response theResponse = JaxRsUtils.mockResponse(String.class, JSON_CONTENT);
        when(client.obtenerVariablesTarea(dto)).thenReturn(theResponse);

        // when
        Response response = gatewayApi.obtenerVaraiblesTarea(dto);

        // then
        ApiUtils.assertThat(ProcesoGatewayApi.class, "obtenerVaraiblesTarea")
                .hasPostMapping("/proceso-gateway-api/tareas/obtener-variables")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .hasJWTTokenSecurity();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(JSON_CONTENT);
    }
}