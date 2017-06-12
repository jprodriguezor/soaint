package co.com.soaint.bpm.services.integration.services;


import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.bpm.EntradaTareaDTO;
import co.com.soaint.foundation.canonical.bpm.RespuestaProcesoDTO;
import co.com.soaint.foundation.canonical.bpm.RespuestaTareaDTO;


import java.net.MalformedURLException;
import java.util.List;

/**
 * Created by Arce on 6/7/2017.
 */
public interface IProcessServices {

      List<RespuestaProcesoDTO> listarProcesos(EntradaProcesoDTO entrada) throws MalformedURLException;
      RespuestaProcesoDTO iniciarProceso(EntradaProcesoDTO entradaProceso) throws MalformedURLException;
      List<RespuestaTareaDTO> listarTareasEstados(EntradaProcesoDTO entradaTarea) throws MalformedURLException;
      RespuestaTareaDTO completarTarea(EntradaProcesoDTO entradaTarea) throws MalformedURLException;



}
