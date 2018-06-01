package co.com.foundation.sgd.apigateway.apis.delegator;

import co.com.foundation.test.rules.EnvironmentRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.math.BigInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

public class CorrespondenciaClientTest {

    @Rule
    public EnvironmentRule environmentRule = new EnvironmentRule();

    private CorrespondenciaClient correspondenciaClient;


    @Before
    public void setup() {
        correspondenciaClient = new CorrespondenciaClient();
    }


    @Test
    public void listarComunicaciones() {
        // given
        String FECHA_INI = "01-01-2018";
        String FECHA_FIN = "01-02-2018";
        String COD_DEPENDENCIA = "CD01";
        String COD_ESTADO = "CE01";
        String NO_RADICADO = "NR01";

        // when
        Response response = correspondenciaClient.listarComunicaciones(
                FECHA_INI, FECHA_FIN, COD_DEPENDENCIA,
                COD_ESTADO, NO_RADICADO);

        // then
        assertThat(response.getStatus(), not(404));
    }

    @Test
    public void listarObservaciones() {
        // given
        BigInteger ID_CORRESPONDENCIA = BigInteger.valueOf(100);

        // when
        Response response = correspondenciaClient.listarObservaciones(ID_CORRESPONDENCIA);

        // then
        assertThat(response.getStatus(), not(404));
    }

    @Test
    public void obtenerCorrespondenciaPorNroRadicado() {
        // given
        String NO_RADICADO = "100";

        // when
        Response response = correspondenciaClient.obtenerCorrespondenciaPorNroRadicado(NO_RADICADO);

        // then
        assertThat(response.getStatus(), not(404));
    }

    @Test
    public void obtenerCorrespondenciaFullPorNroRadicado() {
        // given
        String NO_RADICADO = "100";

        // when
        Response response = correspondenciaClient.obtenerCorrespondenciaFullPorNroRadicado(NO_RADICADO);

        // then
        assertThat(response.getStatus(), not(404));
    }

    @Test
    public void obtenerFuncionarInfoParaReasignar() {
        // given
        BigInteger ID_AGENTE = BigInteger.valueOf(100);

        // when
        Response response = correspondenciaClient.obtenerFuncionarInfoParaReasignar(ID_AGENTE);

        // then
        assertThat(response.getStatus(), not(404));
    }

    @Test
    public void obtenerContactoDestinatarioExterna() {
        // given
        String NO_RADICADO = "100";

        // when
        Response response = correspondenciaClient.obtenerContactoDestinatarioExterna(NO_RADICADO);

        // then
        assertThat(response.getStatus(), not(404));
    }

    @Test
    public void obtnerContantesPorCodigo() {
        // given
        String CODIGOS = "100";

        // when
        Response response = correspondenciaClient.obtnerContantesPorCodigo(CODIGOS);

        // then
        assertThat(response.getStatus(), not(404));
    }

    @Test
    public void listarDistribucion() {
        // given
        String FECHA_INI = "01-01-2000";
        String FECHA_FIN = "01-01-2000";
        String COD_DEPENDENCIA = "CD01";
        String COD_TIPOLOGIA_DOCUMENTAL = "CTDE01";
        String NO_RADICADO = "NR01";


        // when
        Response response = correspondenciaClient.listarDistribucion(
                FECHA_INI, FECHA_FIN, COD_DEPENDENCIA,
                COD_TIPOLOGIA_DOCUMENTAL, NO_RADICADO);

        // then
        assertThat(response.getStatus(), not(404));
    }

    @Test
    public void listarPlanillas() {
        // given
        String NO_PLANILLA = "100";

        // when
        Response response = correspondenciaClient.listarPlanillas(NO_PLANILLA);

        // then
        assertThat(response.getStatus(), not(404));
    }

    @Test
    public void exportarPlanilla() {
        // given
        String NO_PLANILLA = "100";
        String FORMATO = "100";

        // when
        Response response = correspondenciaClient.exportarPlanilla(NO_PLANILLA, FORMATO);

        // then
        assertThat(response.getStatus(), not(404));
    }

    @Test
    public void restablecerCorrespondenciaEntrada() {
        // given
        String ID_PROCESO = "100";
        String ID_TAREA = "100";

        // when
        Response response = correspondenciaClient.restablecerCorrespondenciaEntrada(ID_PROCESO, ID_TAREA);

        // then
        assertThat(response.getStatus(), not(404));
    }


    @Test
    public void listarAnexos() {
        // given
        String NO_RADICADO = "1040TP-CMCOE2017000001";

        // when
        Response response = correspondenciaClient.listarAnexos(NO_RADICADO);

        // then
        assertThat(response.getStatus(), not(404));
    }

    @Test
    public void listarSolicitudUnidadDocumentalNoTramitadas() {

        // given
        String COD_SEDE = "CD01";
        String COD_DEPENDENCIA = "CD01";
        String ID_SOLICITANTE = "CTDE01";
        String FECHA_SOLICITUD = "01-01-2001";

        // when
        Response response = correspondenciaClient.listarSolicitudUnidadDocumentalNoTramitadas(
                COD_SEDE, COD_DEPENDENCIA,
                ID_SOLICITANTE, FECHA_SOLICITUD);

        // then
        assertThat(response.getStatus(), not(404));
    }

    @Test
    public void listarSolicitudUnidadDocumentalTramitadas() {

        // given
        String COD_SEDE = "CD01";
        String COD_DEPENDENCIA = "CD01";
        String ID_SOLICITANTE = "CTDE01";

        // when
        Response response = correspondenciaClient.listarSolicitudUnidadDocumentalTramitadas(
                COD_SEDE, COD_DEPENDENCIA, ID_SOLICITANTE);

        // then
        assertThat(response.getStatus(), not(404));
    }
}