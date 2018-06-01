package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.test.rules.EnvironmentRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

public class ECMClientTest {

    @Rule
    public EnvironmentRule environmentRule = new EnvironmentRule();

    private ECMClient ecmClient;

    @Before
    public void setup() {
        ecmClient = new ECMClient();
    }

    @Test
    public void findByIdDocument() {
        // given
        String ID_DOCUMENTO = "ID_DOC";

        // when
        Response response = ecmClient.findByIdDocument(ID_DOCUMENTO);

        // then
        assertThat(response.getStatus(), not(404));
    }


    @Test
    public void detalleUnidadDocumental() {
        // given
        String ID_UNIDAD_DOCUMENTAL = "IUD01";

        // when
        Response response = ecmClient.detalleUnidadDocumental(ID_UNIDAD_DOCUMENTAL);

        // then
        assertThat(response.getStatus(), not(404));
    }

    @Test
    public void documentosPorArchivar() {
        // given
        String COD_DEPENDENCIA = "CD01";

        // when
        Response response = ecmClient.documentosPorArchivar(COD_DEPENDENCIA);

        // then
        assertThat(response.getStatus(), not(404));
    }

    @Test
    public void documentosArchivados() {
        // given
        String COD_DEPENDENCIA = "CD01";

        // when
        Response response = ecmClient.documentosArchivados(COD_DEPENDENCIA);

        // then
        assertThat(response.getStatus(), not(404));
    }

    @Test
    public void restablecerArchivarDocumentoTask() {
        // given
        String ID_PROCESO = "IP01";
        String ID_TAREA = "IT01";

        // when
        Response response = ecmClient.restablecerArchivarDocumentoTask(ID_PROCESO, ID_TAREA);

        // then
        assertThat(response.getStatus(), not(404));
    }
}