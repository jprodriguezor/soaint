package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.test.mocks.JaxRsUtils;
import co.com.foundation.test.rules.EnvironmentRule;
import co.com.foundation.sgd.utils.SystemParameters;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ProduccionDocumentalClientTest {


    @Rule
    public EnvironmentRule environmentRule = new EnvironmentRule();

    private String API_ENDPOINT = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

    private ProduccionDocumentalClient produccionDocumentalClient;

    private WebTarget wt;

    private Client client;

    private ProcesoClient procesoClient;

    @Before
    public void setup() {

        produccionDocumentalClient = new ProduccionDocumentalClient();
        procesoClient = mock(ProcesoClient.class);

        client = mock(Client.class);
        wt = mock(WebTarget.class);

        when(client.target(anyString())).thenReturn(wt);

        ReflectionTestUtils.setField(produccionDocumentalClient, "client", client);
        ReflectionTestUtils.setField(produccionDocumentalClient, "procesoClient", procesoClient);
    }

    @Test
    public void ejecutarProyeccionMultiple() {
        // given
        String LOGIN_NAME = "LOGIN_NAME";
        String GENERIC_COD = "GENERIC_COD";

        String USUARIO = "USUARIO";
        String PASSWORD = "PASSWORD";
        String ID_DESPLIEGUE = "PASSWORD";
        String ID_PROCESO = "PASSWORD";
        String NRO_RADICADO = "PASSWORD";
        String FECHA_RADICACION = "PASSWORD";


        // creando proyectores
        List<Map<String, Map<String, String>>> proyectores = new ArrayList<>();

        int PROYECTORES_CONT = 5;
        for (int i = 0; i < PROYECTORES_CONT; i ++) {
            Map<String, String> funcionario = new LinkedHashMap<>();
            Map<String, String> sedeAdministrativa = new LinkedHashMap<>();
            Map<String, String> dependencia = new LinkedHashMap<>();
            Map<String, String> tipoPlantilla = new LinkedHashMap<>();
            funcionario.put("loginName", LOGIN_NAME);
            dependencia.put("codigo", GENERIC_COD);
            tipoPlantilla.put("codigo", GENERIC_COD);
            sedeAdministrativa.put("codigo", GENERIC_COD);

            Map<String, Map<String, String>> proyector = new HashMap<>();
            proyector.put("funcionario", funcionario);
            proyector.put("sede", sedeAdministrativa);
            proyector.put("dependencia", dependencia);
            proyector.put("tipoPlantilla", tipoPlantilla);

            proyectores.add(proyector);
        }

        // creando entrada
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("idDespliegue", ID_DESPLIEGUE);
        parametros.put("idProceso", ID_PROCESO);
        parametros.put("numeroRadicado", NRO_RADICADO);
        parametros.put("fechaRadicacion", FECHA_RADICACION);
        parametros.put("proyectores", proyectores);

        EntradaProcesoDTO dto = EntradaProcesoDTO.newInstance()
                .usuario(USUARIO)
                .pass(PASSWORD)
                .parametros(parametros)
                .build();

        // when
        produccionDocumentalClient.ejecutarProyeccionMultiple(dto);

        // then
        ArgumentCaptor<EntradaProcesoDTO> iniciarCaptor = ArgumentCaptor.forClass(EntradaProcesoDTO.class);
        ArgumentCaptor<EntradaProcesoDTO> listarCaptor = ArgumentCaptor.forClass(EntradaProcesoDTO.class);

        verify(procesoClient, times(PROYECTORES_CONT)).iniciar(iniciarCaptor.capture());
        verify(procesoClient).listarPorIdProceso(listarCaptor.capture());

        assertThat(listarCaptor.getValue()).isSameAs(dto);

        List<EntradaProcesoDTO> entradas = iniciarCaptor.getAllValues();
        assertThat(entradas.size()).as("cantidad de procesos iniciados").isEqualTo(PROYECTORES_CONT);
        assertThat(entradas).allSatisfy(entrada -> {
            assertThat(entrada.getIdDespliegue()).isEqualTo(ID_DESPLIEGUE);
            assertThat(entrada.getIdProceso()).isEqualTo(ID_PROCESO);
            assertThat(entrada.getUsuario()).isEqualTo(USUARIO);
            assertThat(entrada.getPass()).isEqualTo(PASSWORD);
            assertThat(entrada.getParametros()).containsOnly(
                    entry("usuarioProyector", LOGIN_NAME),
                    entry("numeroRadicado", NRO_RADICADO),
                    entry("fechaRadicacion", FECHA_RADICACION),
                    entry("codigoSede", GENERIC_COD),
                    entry("codDependencia", GENERIC_COD),
                    entry("codigoTipoPlantilla", GENERIC_COD)
            );
        });
    }

    @Test
    public void obtenerDatosDocumentoPorNroRadicado() {
        // given
        String NRO_RADICADO = "NR01";
        String path = "/documento-web-api/documento/" + NRO_RADICADO;

        JaxRsUtils.mockGetPath(wt, path);

        // when
        produccionDocumentalClient.obtenerDatosDocumentoPorNroRadicado(NRO_RADICADO);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);
    }
}