package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.sgd.utils.SystemParameters;
import co.com.foundation.test.mocks.JaxRsUtils;
import co.com.foundation.test.rules.EnvironmentRule;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyWebTarget;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class TipoPlantillaClientTest {

    @Rule
    public EnvironmentRule environmentRule = new EnvironmentRule();

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private final String API_ENDPOINT = SystemParameters.getParameter(SystemParameters.BACKAPI_ENDPOINT_URL);

    private TipoPlantillaClient tipoPlantillaClient;

    private JerseyWebTarget wt;

    private JerseyClient client;

    @Before
    public void setup() {

        String PLANTILLA_LOCATION = temporaryFolder.getRoot().getPath() + "/";

        tipoPlantillaClient = new TipoPlantillaClient();

        client = mock(JerseyClient.class);
        wt = mock(JerseyWebTarget.class);

        when(client.target(anyString())).thenReturn(wt);

        ReflectionTestUtils.setField(tipoPlantillaClient, "client", client);
        ReflectionTestUtils.setField(tipoPlantillaClient, "location_root", PLANTILLA_LOCATION);
    }

    @Test
    public void get() {
        // given
        String COD_CLASIFICACION = "CC01";
        String ESTADO = "ES01";
        String path = "/plantilla-web-api/plantilla/" + COD_CLASIFICACION + "/" + ESTADO;
        JaxRsUtils.mockGetPath(wt, path);

        // when
        tipoPlantillaClient.get(COD_CLASIFICACION, ESTADO);

        // then
        verify(client).target(API_ENDPOINT);
        verify(wt).path(path);
    }

    @Test
    public void readFromFile() throws IOException {
        // given
        String PLANTILLA_NAME = "plantilla01";
        String PLANTILLA_CONTENT = "<!DOCTYPE html><html></html>";
        File plantillaFile = temporaryFolder.newFile(PLANTILLA_NAME + ".html");
        Files.write(plantillaFile.toPath(), PLANTILLA_CONTENT.getBytes());

        // when
        String plantilla = tipoPlantillaClient.readFromFile(PLANTILLA_NAME);

        // then
        assertThat(plantilla).isEqualTo(PLANTILLA_CONTENT);
    }
}