package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.utils.SystemParameters;
import co.com.foundation.test.mocks.JaxRsUtils;
import co.com.foundation.test.rules.EnvironmentRule;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyWebTarget;
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
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class CargaMasivaClientTest {

    @Rule
    public EnvironmentRule environmentRule = new EnvironmentRule();

    private String ENDPOINT = SystemParameters.getParameter(SystemParameters.BACKAPI_CARGAMASIVA_ENDPOINT_URL);

    private CargaMasivaClient cargaMasivaClient;

    private JerseyWebTarget wt;

    private JerseyClient client;


    @Before
    public void setup() {
        cargaMasivaClient = new CargaMasivaClient();

        client = mock(JerseyClient.class);
        wt = mock(JerseyWebTarget.class);

        when(client.target(anyString())).thenReturn(wt);

        ReflectionTestUtils.setField(cargaMasivaClient, "client", client);
    }

    @Test
    public void listCargaMasiva() {
        // given
        JaxRsUtils.mockGetPath(wt, "/listadocargamasiva");

        // when
        cargaMasivaClient.listCargaMasiva();

        // then
        verify(client).target(ENDPOINT);
        verify(wt).path("/listadocargamasiva");
    }

    @Test
    public void listEstadoCargaMasiva() {
        // given
        JaxRsUtils.mockGetPath(wt, "/estadocargamasiva");

        // when
        cargaMasivaClient.listEstadoCargaMasiva();

        // then
        verify(client).target(ENDPOINT);
        verify(wt).path("/estadocargamasiva");
    }

    @Test
    public void listEstadoCargaMasivaDadoId() {
        // given
        String ID_CARGA_MASIVA = "151";
        JaxRsUtils.mockGetPath(wt, "/estadocargamasiva/" + ID_CARGA_MASIVA);

        // when
        cargaMasivaClient.listEstadoCargaMasivaDadoId(ID_CARGA_MASIVA);

        // then
        verify(client).target(ENDPOINT);
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

        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, PATH);


        // when
        cargaMasivaClient.cargarDocumento(inputPart, CODIGO_SEDE, CODIGO_DEPENDENCIA, CODIGO_FUN_RADICA, FILENAME);

        // then
        verify(client).target(ENDPOINT);

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

    @Test
    public void cargarDocumentoFail() throws IOException {
        // given
        final InputPart inputPart = mock(InputPart.class);
        final String CODIGO_SEDE = "CS01";
        final String CODIGO_DEPENDENCIA = "CD01";
        final String CODIGO_FUN_RADICA = "CFR01";
        final String FILENAME = "filename.ext";

        String PATH = "/cargar-fichero";

        Invocation.Builder requestBuilder = JaxRsUtils.mockPostPath(wt, PATH);

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
}