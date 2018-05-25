package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.apigateway.rules.EnvironmentRule;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.jboss.resteasy.plugins.providers.multipart.OutputPart;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CargaMasivaClientTest {

    @Rule
    public EnvironmentRule environmentRule = new EnvironmentRule();

    private CargaMasivaClient cargaMasivaClient;

    private WebTarget wt;


    @Before
    public void setup() {
        cargaMasivaClient = new CargaMasivaClient();
        wt = mock(WebTarget.class);
        ReflectionTestUtils.setField(cargaMasivaClient, "wt", wt);
    }

    @Test
    public void listCargaMasiva() {
        // given
        mockGetPath("/listadocargamasiva");

        // when
        cargaMasivaClient.listCargaMasiva();

        // then
        verify(wt).path("/listadocargamasiva");
    }

    @Test
    public void listEstadoCargaMasiva() {
        // given
        mockGetPath("/estadocargamasiva");

        // when
        cargaMasivaClient.listEstadoCargaMasiva();

        // then
        verify(wt).path("/estadocargamasiva");
    }

    @Test
    public void listEstadoCargaMasivaDadoId() {
        // given
        String ID_CARGA_MASIVA = "151";
        mockGetPath("/estadocargamasiva/" + ID_CARGA_MASIVA);

        // when
        cargaMasivaClient.listEstadoCargaMasivaDadoId(ID_CARGA_MASIVA);

        // then
        verify(wt).path("/estadocargamasiva/" + ID_CARGA_MASIVA);
    }

    @Test
    public void cargarDocumento() throws IOException {
        // given
        final InputPart inputPart = mock(InputPart.class);
        final String CODIGO_SEDE = "CS01";
        final String CODIGO_DEPENDENCIA = "CD01";
        final String CODIGO_FUN_RADICA = "CFR01";
        final String FILENAME = "filename.ext";

        String PATH = "/cargar-fichero";

        when(inputPart.getBody(InputStream.class, null)).thenReturn(mock(InputStream.class));

        Invocation.Builder requestBuilder = mockPostPath(PATH);


        // when
        cargaMasivaClient.cargarDocumento(inputPart, CODIGO_SEDE, CODIGO_DEPENDENCIA, CODIGO_FUN_RADICA, FILENAME);

        // then
        verify(wt).path(PATH);

        ArgumentCaptor<Entity<GenericEntity<MultipartFormDataOutput>>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        Map<String, OutputPart> formData = captor.getValue().getEntity().getEntity().getFormData();

        assertThat(formData).hasEntrySatisfying("file", part -> {
            assertThat(part.getEntity()).isInstanceOf(InputStream.class);
            assertThat(part.getMediaType()).isEqualTo(MediaType.APPLICATION_OCTET_STREAM_TYPE);
        });

        assertThat(formData).hasEntrySatisfying("codigoSede", part -> {
            assertThat(part.getEntity()).isEqualTo(CODIGO_SEDE);
            assertThat(part.getMediaType()).isEqualTo(MediaType.TEXT_PLAIN_TYPE);
        });

        assertThat(formData).hasEntrySatisfying("codigoDependencia", part -> {
            assertThat(part.getEntity()).isEqualTo(CODIGO_DEPENDENCIA);
            assertThat(part.getMediaType()).isEqualTo(MediaType.TEXT_PLAIN_TYPE);
        });

        assertThat(formData).hasEntrySatisfying("codfunRadica", part -> {
            assertThat(part.getEntity()).isEqualTo(CODIGO_DEPENDENCIA);
            assertThat(part.getMediaType()).isEqualTo(MediaType.TEXT_PLAIN_TYPE);
        });
    }

    private Invocation.Builder mockPostPath(String PATH) {
        WebTarget webTarget = mock(WebTarget.class);
        Invocation.Builder requestBuilder = mock(Invocation.Builder.class);
        when(requestBuilder.post(any(Entity.class))).thenReturn(mock(Response.class));
        when(webTarget.request()).thenReturn(requestBuilder);
        when(wt.path(PATH)).thenReturn(webTarget);
        return requestBuilder;
    }

    @Test
    public void cargarDocumentoFail() throws IOException {
        // given
        final InputPart inputPart = mock(InputPart.class);
        final String CODIGO_SEDE = "CS01";
        final String CODIGO_DEPENDENCIA = "CD01";
        final String CODIGO_FUN_RADICA = "CFR01";
        final String FILENAME = "filename.ext";

        String PATH = "/cargar-fichero";

        Invocation.Builder requestBuilder = mockPostPath(PATH);

        when(inputPart.getBody(InputStream.class, null)).thenThrow(IOException.class);

        // when
        cargaMasivaClient.cargarDocumento(inputPart, CODIGO_SEDE, CODIGO_DEPENDENCIA, CODIGO_FUN_RADICA, FILENAME);

        // then
        verify(wt).path(PATH);

        ArgumentCaptor<Entity<GenericEntity<MultipartFormDataOutput>>> captor = ArgumentCaptor.forClass(Entity.class);

        verify(requestBuilder).post(captor.capture());

        Map<String, OutputPart> formData = captor.getValue().getEntity().getEntity().getFormData();

        assertThat(formData.size()).isEqualTo(0);
    }

    private void mockGetPath(String path) {
        Response response = mock(Response.class);
        WebTarget webTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        when(builder.get()).thenReturn(response);
        when(webTarget.request()).thenReturn(builder);
        when(wt.path(path)).thenReturn(webTarget);
    }
}