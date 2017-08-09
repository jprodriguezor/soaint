package co.com.soaint.bpm.services.integration.services;


import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.bpm.RespuestaProcesoDTO;
import co.com.soaint.foundation.canonical.bpm.RespuestaTareaDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.hornetq.utils.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by Arce on 6/7/2017.
 */
public interface IProcessServices {

      List<RespuestaProcesoDTO> listarProcesos(EntradaProcesoDTO entrada) throws BusinessException, SystemException;
      List<RespuestaProcesoDTO> listarProcesosInstanciaPorUsuarios(EntradaProcesoDTO entrada) throws BusinessException, SystemException;
      RespuestaProcesoDTO iniciarProceso(EntradaProcesoDTO entradaProceso) throws  SystemException;
      RespuestaProcesoDTO iniciarProcesoManual(EntradaProcesoDTO entradaProceso) throws IOException, JSONException, URISyntaxException, SystemException, BusinessException;
      List<RespuestaTareaDTO> listarTareasEstados(EntradaProcesoDTO entradaTarea) throws MalformedURLException;
      List<RespuestaTareaDTO> listarTareasEstadosInstanciaProceso(EntradaProcesoDTO entradaTarea) throws MalformedURLException;
      List<RespuestaTareaDTO> listarTareasPorInstanciaProceso(EntradaProcesoDTO entradaTarea) throws MalformedURLException;
      List<RespuestaTareaDTO> listarTareasEstadosPorUsuario(EntradaProcesoDTO entradaTarea) throws MalformedURLException;
      RespuestaTareaDTO completarTarea(EntradaProcesoDTO entradaTarea) throws MalformedURLException;
      RespuestaTareaDTO iniciarTarea(EntradaProcesoDTO entradaTarea) throws MalformedURLException;
      RespuestaTareaDTO reservarTarea(EntradaProcesoDTO entradaTarea) throws  BusinessException, SystemException;
      RespuestaTareaDTO reasignarTarea(EntradaProcesoDTO entradaTarea) throws IOException, URISyntaxException, JSONException;

      RespuestaProcesoDTO enviarSenalProceso(EntradaProcesoDTO entrada) throws IOException, JSONException ;
      RespuestaProcesoDTO senalInicioAutomatico(EntradaProcesoDTO entrada) throws IOException, JSONException ;
}
