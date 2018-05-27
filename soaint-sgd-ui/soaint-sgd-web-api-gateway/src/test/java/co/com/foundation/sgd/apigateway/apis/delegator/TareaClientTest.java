package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.test.mocks.JaxRsUtils;
import co.com.foundation.test.rules.EnvironmentRule;
import co.com.foundation.sgd.utils.SystemParameters;
import co.com.soaint.foundation.canonical.correspondencia.TareaDTO;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class TareaClientTest {


    @Rule
    public EnvironmentRule environmentRule = new EnvironmentRule();

    private String API_ENDPOINT = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

    private TareaClient tareaClient;

    private WebTarget wt;

    private Client client;

    @Before
    public void setup() {

        tareaClient = new TareaClient();

        client = mock(Client.class);
        wt = mock(WebTarget.class);

        when(client.target(anyString())).thenReturn(wt);

        ReflectionTestUtils.setField(tareaClient, "client", client);
    }

    @Test
    public void guardarEstadoTarea() {
        // given
        String path = "/tarea-web-api/tarea/";
        TareaDTO dto = mock(TareaDTO.class);
        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, path);

        // when
        tareaClient.guardarEstadoTarea(dto);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);

        ArgumentCaptor<Entity<TareaDTO>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).buildPost(captor.capture());

        assertThat(captor.getValue().getEntity()).isSameAs(dto);
    }

    @Test
    public void listarEstadoTarea() {
        // given
        String ID_INSTANCIA_PROCESO = "IIP01";
        String ID_TAREA_PROCESO = "ITP01";
        String path = "/tarea-web-api/tarea/" + ID_INSTANCIA_PROCESO + "/" + ID_TAREA_PROCESO;
        JaxRsUtils.mockGetPath(wt, path);

        // when
        tareaClient.listarEstadoTarea(ID_INSTANCIA_PROCESO, ID_TAREA_PROCESO);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);
    }
}