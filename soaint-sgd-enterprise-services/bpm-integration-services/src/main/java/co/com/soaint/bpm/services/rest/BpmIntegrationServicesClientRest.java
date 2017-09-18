package co.com.soaint.bpm.services.rest;

import co.com.soaint.bpm.services.integration.services.IProcessServices;
import co.com.soaint.bpm.services.integration.services.ITaskServices;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.bpm.RespuestaProcesoDTO;
import co.com.soaint.foundation.canonical.bpm.RespuestaTareaBamDTO;
import co.com.soaint.foundation.canonical.bpm.RespuestaTareaDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;


/**
 * Created by Arce on 6/7/2017.
 */

@Path("/bpm")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class BpmIntegrationServicesClientRest {


    @Autowired
    private IProcessServices proceso;
    @Autowired
    private ITaskServices tarea;

    /**
     * Contructor de la clase
     */
    public BpmIntegrationServicesClientRest() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     * Permite listar procesos
     *
     * @param entradaProceso Objeto que define los parametros de un proceso
     * @return lista de Procesos
     * @throws Throwable
     */
    @POST
    @Path("/proceso/listar/")
    public List<RespuestaProcesoDTO> listarProcesos(EntradaProcesoDTO entradaProceso) throws SystemException {
        log.info("processing rest request - listar procesos");
        return proceso.listarProcesos(entradaProceso);
    }

    /**
     * Permite listar procesos
     *
     * @param entradaProceso Objeto que define los parametros de un proceso
     * @return lista de Procesos
     * @throws Throwable
     */
    @POST
    @Path("/proceso/listar-variables/")
    public String listarVariablesProcesos(EntradaProcesoDTO entradaProceso) throws SystemException {
        log.info("processing rest request - listar variables procesos");
        return proceso.listarVariablesProcesos(entradaProceso);
    }

    /**
     * Permite enviar una sennal al proceso
     *
     * @param entradaProceso Objeto que define los parametros de un proceso
     * @return Los datos del proceso que fue invocado
     * @throws Throwable
     */
    @POST
    @Path("/proceso/sennal/")
    public RespuestaProcesoDTO enviarSennalProceso(EntradaProcesoDTO entradaProceso) throws SystemException {
        log.info("processing rest request - enviar señal proceso");
        return proceso.enviarSenalProceso(entradaProceso);
    }

    /**
     * Permite enviar una sennal de manera automatica al proceso
     *
     * @param entradaProceso Objeto que define los parametros de un proceso
     * @return Los datos del proceso que fue invocado
     * @throws SystemException
     * @throws BusinessException
     * @throws IOException
     */
    @POST
    @Path("/proceso/sennal/inicio/")
    public RespuestaProcesoDTO senalInicioAutomatico(EntradaProcesoDTO entradaProceso) throws SystemException {
        log.info("processing rest request - enviar señal inicia automatico proceso ");
        return proceso.senalInicioAutomatico(entradaProceso);
    }

    /**
     * Permite listar las instancias de procesos por usuario
     *
     * @param entradaProceso Objeto que contiene los parametros de entrada para un proceso
     * @return lista de instancias de procesos
     * @throws Throwable
     */
    @POST
    @Path("/proceso/listar-instancias/")
    public List<RespuestaProcesoDTO> listarProcesosInstanciaPorUsuarios(EntradaProcesoDTO entradaProceso) throws SystemException {
        log.info("processing rest request - listar instancias de procesos por usuarios");
        return proceso.listarProcesosInstanciaPorUsuarios(entradaProceso);
    }

    /**
     * Permite iniciar un proceso
     *
     * @param entradaProceso Objeto que contiene los parametros de entrada para un proceso
     * @return parametros del proceso iniciado
     * @throws SystemException
     * @throws BusinessException
     * @throws MalformedURLException
     */
    @POST
    @Path("/proceso/iniciar/")
    public RespuestaProcesoDTO iniciarProceso(EntradaProcesoDTO entradaProceso) throws SystemException {
        log.info("processing rest request - iniciar proceso");
        return proceso.iniciarProceso(entradaProceso);
    }

    /**
     * Permite iniciar un proceso y asignar una tarea de manera auntomatica a un usuario
     *
     * @param entradaProceso Objeto que contiene los parametros de entrada para un proceso
     * @return Los datos del proceso que fue iniciado codigoProceso,nombreProceso,estado y idDespliegue
     * @throws IOException
     * @throws URISyntaxException
     */
    @POST
    @Path("/proceso/iniciar/manual")
    public RespuestaProcesoDTO iniciarProcesoManual(EntradaProcesoDTO entradaProceso) throws SystemException {
        log.info("processing rest request - iniciar proceso manual");
        return proceso.iniciarProcesoManual(entradaProceso);
    }

    /**
     * Permite iniciar un proceso y asignar una tarea de manera auntomatica a un tercero
     *
     * @param entradaProceso
     * @return Los datos del proceso que fue iniciado codigoProceso,nombreProceso,estado y idDespliegue
     * @throws SystemException
     */
    @POST
    @Path("/proceso/iniciar-tercero")
    public RespuestaProcesoDTO iniciarProcesoPorTercero(EntradaProcesoDTO entradaProceso) throws SystemException {
        log.info("processing rest request - iniciar proceso");
        return proceso.iniciarProcesoPorTercero(entradaProceso);
    }


    /**
     * Permite completar una tarea asociada a un proceso
     *
     * @param entradaTarea Objeto que contiene los parametros de entrada para un proceso
     * @return Los datos de la tarea completada
     * @throws MalformedURLException
     */
    @POST
    @Path("/tareas/completar/")
    public RespuestaTareaDTO completarTarea(EntradaProcesoDTO entradaTarea) throws SystemException {
        log.info("processing rest request - completar tarea");
        return tarea.completarTarea(entradaTarea);
    }

    /**
     * Permite iniciar una tarea asociada a un proceso
     *
     * @param entradaTarea Objeto que contiene los parametros de entrada para un proceso
     * @return Los datos de la tarea iniciada
     * @throws MalformedURLException
     */
    @POST
    @Path("/tareas/iniciar/")
    public RespuestaTareaDTO iniciarTarea(EntradaProcesoDTO entradaTarea) throws SystemException {
        log.info("processing rest request - iniciar tarea");
        return tarea.iniciarTarea(entradaTarea);
    }

    /**
     * Permite reservar una tarea asociada a un proceso
     *
     * @param entradaTarea Objeto que contiene los parametros de entrada para un proceso
     * @return Los datos de la tarea reservada
     * @throws IOException
     * @throws URISyntaxException
     */
    @POST
    @Path("/tareas/reservar/")
    public RespuestaTareaDTO reservarTarea(EntradaProcesoDTO entradaTarea) throws SystemException {
        log.info("processing rest request - reservar tarea");
        return tarea.reservarTarea(entradaTarea);
    }

    /**
     * Permite reasignar una tarea a otro usuario
     *
     * @param entradaTarea Objeto que contiene los parametros de entrada para un proceso
     * @return Los datos de la tarea reservada
     * @throws MalformedURLException
     */
    @POST
    @Path("/tareas/reasignar/")
    public RespuestaTareaDTO reasignarTarea(EntradaProcesoDTO entradaTarea) throws SystemException {
        log.info("processing rest request - reasignar tarea");
        return tarea.reasignarTarea(entradaTarea);
    }


    /**
     * Permite listar las tareas por estados
     *
     * @param entradaTarea Objeto que contiene los parametros de entrada para un proceso
     * @return lista de tareas que cumplen con los filtros de estado solicitdos
     * @throws MalformedURLException
     */
    @POST
    @Path("/tareas/listar/estados")
    public List<RespuestaTareaDTO> listarTareasEstados(EntradaProcesoDTO entradaTarea) throws SystemException {
        log.info("processing rest request - listar tareas con sus estados");
        return tarea.listarTareasEstados(entradaTarea);
    }

    /**
     * Permite listar las tareas por estados
     *
     * @param entradaTarea Objeto que contiene los parametros de entrada para un proceso
     * @return lista de tareas que cumplen con los filtros de estado solicitdos
     * @throws MalformedURLException
     */
    @POST
    @Path("/tareas/listar/completadas")
    public List<RespuestaTareaBamDTO> listarTareasCompletadas(EntradaProcesoDTO entradaTarea) throws SystemException {
        log.info("processing rest request - listar tareas con sus estados");
        return tarea.listarTareasCompletadas(entradaTarea);
    }

    /**
     * Permite listar tareas asociados a usuarios por estados
     *
     * @param entradaTarea Objeto que contiene los parametros de entrada para un proceso
     * @return lista de tareas
     * @throws MalformedURLException
     */
    @POST
    @Path("/tareas/listar/estados-usuario/")
    public List<RespuestaTareaDTO> listarTareaPorUsuario(EntradaProcesoDTO entradaTarea) throws SystemException {
        log.info("processing rest request - listar tareas con sus estados por usuario");
        return tarea.listarTareasEstadosPorUsuario(entradaTarea);
    }

    /**
     * Permite listar las tareas asociadas a una instancia de procesos por sus estados
     *
     * @param entradaTarea Objeto que contiene los parametros de entrada para un proceso
     * @return lista de tareas
     * @throws MalformedURLException
     */
    @POST
    @Path("/tareas/listar/estados-instancia/")
    public List<RespuestaTareaDTO> listarTareasEstadosInstanciaProceso(EntradaProcesoDTO entradaTarea) throws SystemException {
        log.info("processing rest request - listar instancias de procesos");
        return tarea.listarTareasEstadosInstanciaProceso(entradaTarea);
    }

    /**
     * Listar tareas por instancia de proceso
     *
     * @param entradaTarea Objeto que contiene los parametros de entrada para un proceso
     * @return lista de tareas
     * @throws MalformedURLException
     */
    @POST
    @Path("/tareas/listar/estados-instancia/")
    public List<RespuestaTareaDTO> listarTareasPorInstanciaProceso(EntradaProcesoDTO entradaTarea) throws SystemException {
        log.info("processing rest request - listar tareas por instancias de procesos");
        return tarea.listarTareasPorInstanciaProceso(entradaTarea);
    }

}
