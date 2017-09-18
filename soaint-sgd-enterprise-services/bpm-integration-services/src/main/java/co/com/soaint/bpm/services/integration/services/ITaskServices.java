package co.com.soaint.bpm.services.integration.services;

import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.bpm.RespuestaTareaBamDTO;
import co.com.soaint.foundation.canonical.bpm.RespuestaTareaDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;

import java.util.List;

/**
 * Created by Arce on 8/16/2017.
 */
public interface ITaskServices {

    List<RespuestaTareaDTO> listarTareasEstados(EntradaProcesoDTO entradaTarea) throws SystemException;

    List<RespuestaTareaDTO> listarTareasEstadosInstanciaProceso(EntradaProcesoDTO entradaTarea) throws SystemException;

    List<RespuestaTareaDTO> listarTareasPorInstanciaProceso(EntradaProcesoDTO entradaTarea) throws SystemException;

    List<RespuestaTareaDTO> listarTareasEstadosPorUsuario(EntradaProcesoDTO entradaTarea) throws SystemException;

    List<RespuestaTareaBamDTO> listarTareasCompletadas(EntradaProcesoDTO entradaTarea) throws SystemException;

    RespuestaTareaDTO completarTarea(EntradaProcesoDTO entradaTarea) throws SystemException;

    RespuestaTareaDTO iniciarTarea(EntradaProcesoDTO entradaTarea) throws SystemException;

    RespuestaTareaDTO reservarTarea(EntradaProcesoDTO entradaTarea) throws SystemException;

    RespuestaTareaDTO reasignarTarea(EntradaProcesoDTO entradaTarea) throws SystemException;
}
