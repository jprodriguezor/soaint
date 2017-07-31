package co.com.soaint.bpm.services.integration.services;


import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.bpm.EntradaSennalDTO;
import co.com.soaint.foundation.canonical.bpm.RespuestaProcesoDTO;
import co.com.soaint.foundation.canonical.bpm.RespuestaTareaDTO;
import org.hornetq.utils.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by Arce on 6/7/2017.
 */
public interface IProcessServices {

      List<RespuestaProcesoDTO> listarProcesos(EntradaProcesoDTO entrada) throws IOException, JSONException;
      List<RespuestaProcesoDTO> listarProcesosInstanciaPorUsuarios(EntradaProcesoDTO entrada) throws IOException, JSONException, URISyntaxException;
      RespuestaProcesoDTO iniciarProceso(EntradaProcesoDTO entradaProceso) throws MalformedURLException;
      RespuestaProcesoDTO iniciarProcesoManual(EntradaProcesoDTO entradaProceso) throws IOException, JSONException, URISyntaxException;
      List<RespuestaTareaDTO> listarTareasEstados(EntradaProcesoDTO entradaTarea) throws MalformedURLException;
      List<RespuestaTareaDTO> listarTareasEstadosInstanciaProceso(EntradaProcesoDTO entradaTarea) throws MalformedURLException;
      List<RespuestaTareaDTO> listarTareasPorInstanciaProceso(EntradaProcesoDTO entradaTarea) throws MalformedURLException;
      List<RespuestaTareaDTO> listarTareasEstadosPorUsuario(EntradaProcesoDTO entradaTarea) throws MalformedURLException;
      RespuestaTareaDTO completarTarea(EntradaProcesoDTO entradaTarea) throws MalformedURLException;
      RespuestaTareaDTO iniciarTarea(EntradaProcesoDTO entradaTarea) throws MalformedURLException;
      RespuestaTareaDTO reservarTarea(EntradaProcesoDTO entradaTarea) throws IOException, URISyntaxException, JSONException;

      RespuestaProcesoDTO enviarSenalProceso(EntradaSennalDTO entrada) throws IOException, JSONException ;
      RespuestaProcesoDTO senalInicioAutomatico(EntradaProcesoDTO entrada) throws IOException, JSONException ;
}
