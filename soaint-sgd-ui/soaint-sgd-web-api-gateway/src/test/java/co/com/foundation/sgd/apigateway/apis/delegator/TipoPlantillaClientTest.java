package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.test.rules.EnvironmentRule;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.not;

public class TipoPlantillaClientTest {

    @Rule
    public EnvironmentRule environmentRule = new EnvironmentRule();

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private TipoPlantillaClient tipoPlantillaClient;

    @Before
    public void setup() {

        String PLANTILLA_LOCATION = temporaryFolder.getRoot().getPath() + "/";

        tipoPlantillaClient = new TipoPlantillaClient();

        ReflectionTestUtils.setField(tipoPlantillaClient, "location_root", PLANTILLA_LOCATION);
    }

    @Test
    public void get() {
        // given
        String COD_CLASIFICACION = "CC01";
        String ESTADO = "ES01";

        // when
        Response response = tipoPlantillaClient.get(COD_CLASIFICACION, ESTADO);

        // then
        MatcherAssert.assertThat(response.getStatus(), not(404));
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