package co.com.soaint.bpm.services.integration.services;


import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.bpm.RespuestaProcesoDTO;
import co.com.soaint.foundation.canonical.bpm.RespuestaTareaBamDTO;
import co.com.soaint.foundation.canonical.bpm.RespuestaTareaDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;

import java.util.List;

/**
 * Created by Arce on 6/7/2017.
 */
public interface IProcessServices {

    List<RespuestaProcesoDTO> listarProcesos(EntradaProcesoDTO entrada) throws SystemException;

    String listarVariablesProcesos(EntradaProcesoDTO entrada) throws SystemException;

    List<RespuestaProcesoDTO> listarProcesosInstanciaPorUsuarios(EntradaProcesoDTO entrada) throws SystemException;

    RespuestaProcesoDTO iniciarProceso(EntradaProcesoDTO entradaProceso) throws SystemException;

    RespuestaProcesoDTO iniciarProcesoPorTercero(EntradaProcesoDTO entradaProceso) throws SystemException;

    RespuestaProcesoDTO iniciarProcesoManual(EntradaProcesoDTO entradaProceso) throws SystemException;

    List<RespuestaTareaDTO> listarTareasEstados(EntradaProcesoDTO entradaTarea) throws SystemException;

    List<RespuestaTareaBamDTO> listarTareasCompletadas(EntradaProcesoDTO entradaTarea) throws SystemException;

    List<RespuestaTareaDTO> listarTareasEstadosInstanciaProceso(EntradaProcesoDTO entradaTarea) throws SystemException;

    List<RespuestaTareaDTO> listarTareasPorInstanciaProceso(EntradaProcesoDTO entradaTarea) throws SystemException;

    List<RespuestaTareaDTO> listarTareasEstadosPorUsuario(EntradaProcesoDTO entradaTarea) throws SystemException;

    RespuestaTareaDTO completarTarea(EntradaProcesoDTO entradaTarea) throws SystemException;

    RespuestaTareaDTO iniciarTarea(EntradaProcesoDTO entradaTarea) throws SystemException;

    RespuestaTareaDTO reservarTarea(EntradaProcesoDTO entradaTarea) throws SystemException;

    RespuestaTareaDTO reasignarTarea(EntradaProcesoDTO entradaTarea) throws SystemException;

    RespuestaProcesoDTO enviarSenalProceso(EntradaProcesoDTO entrada) throws SystemException;

    RespuestaProcesoDTO senalInicioAutomatico(EntradaProcesoDTO entrada) throws SystemException;
}
