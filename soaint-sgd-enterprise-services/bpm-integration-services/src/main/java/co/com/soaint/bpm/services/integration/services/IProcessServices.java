package co.com.soaint.bpm.services.integration.services;


import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.bpm.EntradaTareaDTO;
import co.com.soaint.foundation.canonical.bpm.RespuestaProcesoDTO;
import co.com.soaint.foundation.canonical.bpm.RespuestaTareaDTO;


import java.util.List;

/**
 * Created by Arce on 6/7/2017.
 */
public interface IProcessServices {

      RespuestaProcesoDTO inicarProceso( EntradaProcesoDTO entradaProceso);

//       List<RespuestaTareaDTO> listarTareas();

//      RespuestaTareaDTO completarTarea(EntradaTareaDTO entradaTarea);



}
