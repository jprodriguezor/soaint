package co.com.soaint.bpm.services.integration.services.impl;

import co.com.soaint.bpm.services.integration.services.ITaskServices;
import co.com.soaint.bpm.services.util.EngineConexion;
import co.com.soaint.bpm.services.util.Estados;
import co.com.soaint.bpm.services.util.SystemParameters;
import co.com.soaint.foundation.canonical.bpm.*;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.hornetq.utils.json.JSONException;
import org.hornetq.utils.json.JSONObject;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Status;
import org.kie.api.task.model.Task;
import org.kie.api.task.model.TaskSummary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Arce on 8/16/2017.
 */
@Service
@Log4j2

public class TasksService implements ITaskServices {

    private TaskService taskService;
    @Value("${jbpm.endpoint.url}")
    private String endpointJBPConsole = "";
    @Value("${mensaje.error.sistema}")
    private String errorSistema = "";
    @Value("${mensaje.error.sistema.generico}")
    private String errorSistemaGenerico = "";
    @Value("${header.accept}")
    private String headerAccept = "";
    @Value("${header.authorization}")
    private String headerAuthorization = "";
    @Value("${header.value.application.type}")
    private String valueApplicationType = "";
    @Value("${header.value.authorization}")
    private String valueAuthorization = "";
    @Value("${mensaje.error.negocio.fallo}")
    private String errorNegocioFallo = "";
    @Value("${formato.idioma}")
    private String formatoIdioma = "";
    @Value("${protocolo}")
    private String protocolo = "";
    private HttpClient httpClient;
    private HttpPost postRequest;
    private HttpResponse response;
    private String estado = "";
    @PersistenceContext
    private EntityManager em;
    EngineConexion engine = EngineConexion.getInstance();
    Estados estadosOperaciones = new Estados();


    private TasksService() {
    }

    /**
     * Permite iniciar una tarea asociada a un proceso
     *
     * @param entrada Objeto que contiene los parametros de entrada para un proceso
     * @return Los datos de la tarea iniciada
     * @throws MalformedURLException
     */
    @Override
    public RespuestaTareaDTO iniciarTarea(EntradaProcesoDTO entrada) throws SystemException {
        try {
            log.info("iniciar - tarea: {}", entrada);
            taskService = engine.obtenerEngine(entrada).getTaskService();
            taskService.start(entrada.getIdTarea(), entrada.getUsuario());
            Task task = taskService.getTaskById(entrada.getIdTarea());
            return RespuestaTareaDTO.newInstance()
                    .idTarea(entrada.getIdTarea())
                    .estado(String.valueOf(EstadosEnum.ENPROGRESO))
                    .nombre(task.getName())
                    .idProceso(entrada.getIdProceso())
                    .idDespliegue(entrada.getIdDespliegue())
                    .idParent(task.getTaskData().getParentId())
                    .idResponsable(task.getTaskData().getActualOwner().getId())
                    .idInstanciaProceso(task.getTaskData().getProcessInstanceId())
                    .tiempoExpiracion(task.getTaskData().getExpirationTime())
                    .tiempoActivacion(task.getTaskData().getActivationTime())
                    .fechaCreada(task.getTaskData().getCreatedOn())
                    .prioridad(task.getPriority())
                    .build();

        } catch (Exception e) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("fin - iniciar - tarea ");
        }

    }

    /**
     * Permite completar una tarea asociada a un proceso
     *
     * @param entrada Objeto que contiene los parametros de entrada para un proceso
     * @return Los datos de la tarea completada
     * @throws MalformedURLException
     */
    @Override
    public RespuestaTareaDTO completarTarea(EntradaProcesoDTO entrada) throws SystemException {
        try {
            log.info("iniciar - tarea: {}", entrada);
            taskService = engine.obtenerEngine(entrada).getTaskService();
            taskService.complete(entrada.getIdTarea(), entrada.getUsuario(), entrada.getParametros());
            return RespuestaTareaDTO.newInstance()
                    .idTarea(entrada.getIdTarea())
                    .estado(String.valueOf(EstadosEnum.COMPLETADO))
                    .idProceso(entrada.getIdProceso())
                    .build();
        } catch (Exception e) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("fin - iniciar - tarea ");
        }

    }

    /**
     * Permite reservar una tarea asociada a un proceso
     *
     * @param entrada Objeto que contiene los parametros de entrada para un proceso
     * @return Los datos de la tarea reservada
     * @throws IOException
     * @throws URISyntaxException
     * @throws JSONException
     */
    @Override
    public RespuestaTareaDTO reservarTarea(EntradaProcesoDTO entrada) throws SystemException {
        String encoding = java.util.Base64.getEncoder().encodeToString(new String(entrada.getUsuario() + ":" + entrada.getPass()).getBytes());
        log.info("iniciar - reservar tarea: {}", entrada);
        try {
            URI uri = new URIBuilder(protocolo.concat(SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_ENDPOINT)))
                    .setPath("/jbpm-console/rest/task/" + entrada.getIdTarea() + "/claim")
                    .build();
            httpClient = HttpClientBuilder.create().build();
            postRequest = new HttpPost(uri);
            postRequest.addHeader(headerAuthorization, valueAuthorization + " " + encoding);
            postRequest.addHeader(headerAccept, valueApplicationType);
            response = httpClient.execute(postRequest);
            JSONObject respuestaJson = new JSONObject(EntityUtils.toString(response.getEntity()));
            if (response.getStatusLine().getStatusCode() != 200) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage(errorNegocioFallo + response.getStatusLine().getStatusCode())
                        .buildBusinessException();
            } else {
                String estadoResp = respuestaJson.getString("status");

                if ("SUCCESS".equals(estadoResp)) {
                    estado = String.valueOf(EstadosEnum.ENPROGRESO);
                } else {
                    estado = String.valueOf(EstadosEnum.ERROR);
                }
            }
            return RespuestaTareaDTO.newInstance()
                    .idTarea(entrada.getIdTarea())
                    .estado(estado)
                    .idProceso(entrada.getIdProceso())
                    .idDespliegue(entrada.getIdDespliegue())
                    .build();
        } catch (BusinessException e) {
            log.error(e.getMessage());
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        } catch (Exception ex) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(ex)
                    .buildSystemException();
        } finally {
            log.info("fin - reservar - tarea ");
        }


    }

    /**
     * Permite reasignar una tarea a otro usuario
     *
     * @param entrada Objeto que contiene los parametros de entrada para un proceso
     * @return Los datos de la tarea reservada
     * @throws MalformedURLException
     */
    @Override
    public RespuestaTareaDTO reasignarTarea(EntradaProcesoDTO entrada) throws SystemException {
        try {
            log.info("iniciar - reasignar tarea: {}", entrada);
            URI uri = new URIBuilder(protocolo.concat(SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_ENDPOINT)))
                    .setPath("/jbpm-console/rest/task/" + entrada.getIdTarea() + "/delegate")
                    .addParameter("targetEntityId", entrada.getParametros().get("usuarioReasignar").toString())
                    .build();
            httpClient = HttpClientBuilder.create().build();
            postRequest = new HttpPost(uri);
            postRequest.addHeader(headerAuthorization, valueAuthorization + " " + entrada.getPass());
            postRequest.addHeader(headerAccept, valueApplicationType);
            response = httpClient.execute(postRequest);
            JSONObject respuestaJson = new JSONObject(EntityUtils.toString(response.getEntity()));
            if (response.getStatusLine().getStatusCode() != 200) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage(errorNegocioFallo + response.getStatusLine().getStatusCode())
                        .buildBusinessException();
            } else {
                String estadoResp = respuestaJson.getString("status");

                if ("SUCCESS".equals(estadoResp)) {
                    estado = String.valueOf(EstadosEnum.RESERVADO);
                } else {
                    estado = String.valueOf(EstadosEnum.ERROR);
                }
            }
            return RespuestaTareaDTO.newInstance()
                    .idTarea(entrada.getIdTarea())
                    .estado(estado)
                    .idProceso(entrada.getIdProceso())
                    .idDespliegue(entrada.getIdDespliegue())
                    .build();
        } catch (Exception e) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("fin - reasignar - tarea ");
        }
    }

    /**
     * Permite listar las tareas por estados
     *
     * @param entrada Objeto que contiene los parametros de entrada para un proceso
     * @return lista de tareas que cumplen con los filtros de estado solicitdos
     * @throws MalformedURLException
     */
    @Override
    public List<RespuestaTareaDTO> listarTareasEstados(EntradaProcesoDTO entrada) throws SystemException {
        List<RespuestaTareaDTO> tareas = new ArrayList<>();
        try {
            log.info("iniciar - listar tareas estados: {}", entrada);
            taskService = engine.obtenerEngine(entrada).getTaskService();
            List<TaskSummary> tasks = taskService.getTasksAssignedAsPotentialOwner(entrada.getUsuario(), formatoIdioma);
            for (TaskSummary task : tasks) {
                RespuestaTareaDTO respuestaTarea = RespuestaTareaDTO.newInstance()
                        .idTarea(task.getId())
                        .estado(estadosOperaciones.estadoRespuesta(task.getStatusId()))
                        .idProceso(task.getProcessId())
                        .idDespliegue(task.getDeploymentId())
                        .nombre(task.getName())
                        .prioridad(task.getPriority())
                        .idInstanciaProceso(task.getProcessInstanceId())
                        .fechaCreada(task.getCreatedOn())
                        .tiempoActivacion(task.getActivationTime())
                        .tiempoExpiracion(task.getExpirationTime())
                        .build();
                tareas.add(respuestaTarea);
            }
            return tareas;
        } catch (Exception e) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("fin - listar - tareas - estados ");
        }
    }

    /**
     * Permite listar las tareas asociadas a una instancia de procesos por sus estados
     *
     * @param entrada Objeto que contiene los parametros de entrada para un proceso
     * @return lista de tareas
     * @throws MalformedURLException
     */
    @Override
    public List<RespuestaTareaDTO> listarTareasEstadosInstanciaProceso(EntradaProcesoDTO entrada) throws SystemException {
        List<RespuestaTareaDTO> tareas = new ArrayList<>();
        Iterator<EstadosEnum> estadosEnviados = entrada.getEstados().iterator();
        List<Status> estadosActivos = estadosOperaciones.estadosActivos(estadosEnviados);
        try {
            log.info("iniciar - listar tareas estados instancias proceso: {}", entrada);
            taskService = engine.obtenerEngine(entrada).getTaskService();
            List<TaskSummary> tasks = taskService.getTasksOwnedByStatus(entrada.getUsuario(), estadosActivos, formatoIdioma);
            for (TaskSummary task : tasks) {
                if (task.getProcessInstanceId().longValue() == entrada.getInstanciaProceso().longValue()) {
                    RespuestaTareaDTO respuestaTarea = RespuestaTareaDTO.newInstance()
                            .idTarea(task.getId())
                            .estado(estadosOperaciones.estadoRespuesta(task.getStatusId()))
                            .idProceso(task.getProcessId())
                            .idDespliegue(task.getDeploymentId())
                            .nombre(task.getName())
                            .prioridad(task.getPriority())
                            .idInstanciaProceso(task.getProcessInstanceId())
                            .fechaCreada(task.getCreatedOn())
                            .tiempoActivacion(task.getActivationTime())
                            .tiempoExpiracion(task.getExpirationTime())
                            .build();
                    tareas.add(respuestaTarea);
                }
            }
            return tareas;
        } catch (Exception e) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("fin - listar - tareas estados instandias proceso ");
        }
    }


    /**
     * Permite listar tareas asociados a usuarios por estados
     *
     * @param entrada Objeto que contiene los parametros de entrada para un proceso
     * @return lista de tareas
     * @throws MalformedURLException
     */
    @Override
    public List<RespuestaTareaDTO> listarTareasEstadosPorUsuario(EntradaProcesoDTO entrada) throws SystemException {
        List<RespuestaTareaDTO> tareas = new ArrayList<>();
        Iterator<EstadosEnum> estadosEnviados = entrada.getEstados().iterator();
        List<Status> estadosActivos = estadosOperaciones.estadosActivos(estadosEnviados);
        try {
            log.info("iniciar - listar tareas estados usuarios: {}", entrada);
            taskService = engine.obtenerEngine(entrada).getTaskService();
            List<TaskSummary> tasks = taskService.getTasksOwnedByStatus(entrada.getUsuario(), estadosActivos, formatoIdioma);
            for (TaskSummary task : tasks) {

                RespuestaTareaDTO respuestaTarea = RespuestaTareaDTO.newInstance()
                        .idTarea(task.getId())
                        .estado(estadosOperaciones.estadoRespuesta(task.getStatusId()))
                        .idProceso(task.getProcessId())
                        .idDespliegue(task.getDeploymentId())
                        .nombre(task.getName())
                        .prioridad(task.getPriority())
                        .idInstanciaProceso(task.getProcessInstanceId())
                        .fechaCreada(task.getCreatedOn())
                        .tiempoActivacion(task.getActivationTime())
                        .tiempoExpiracion(task.getExpirationTime())
                        .build();
                tareas.add(respuestaTarea);

            }
            return tareas;
        } catch (Exception e) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("fin - listar tareas estados usuarios ");
        }
    }

    /**
     * Listar tareas por instancia de proceso
     *
     * @param entrada Objeto que contiene los parametros de entrada para un proceso
     * @return lista de tareas
     * @throws MalformedURLException
     */
    @Override
    public List<RespuestaTareaDTO> listarTareasPorInstanciaProceso(EntradaProcesoDTO entrada) throws SystemException {

        List<RespuestaTareaDTO> tareas = new ArrayList<>();
        Iterator<EstadosEnum> estadosEnviados = entrada.getEstados().iterator();
        List<Status> estadosActivos = estadosOperaciones.estadosActivos(estadosEnviados);
        try {
            log.info("iniciar - listar tareas instancias proceso: {}", entrada);
            taskService = engine.obtenerEngine(entrada).getTaskService();
            List<TaskSummary> tasks = taskService.getTasksByStatusByProcessInstanceId(entrada.getInstanciaProceso(), estadosActivos, formatoIdioma);
            for (TaskSummary task : tasks) {
                RespuestaTareaDTO respuestaTarea = RespuestaTareaDTO.newInstance()
                        .idTarea(task.getId())
                        .estado(estadosOperaciones.estadoRespuesta(task.getStatusId()))
                        .idProceso(task.getProcessId())
                        .idDespliegue(task.getDeploymentId())
                        .nombre(task.getName())
                        .prioridad(task.getPriority())
                        .idInstanciaProceso(task.getProcessInstanceId())
                        .fechaCreada(task.getCreatedOn())
                        .tiempoActivacion(task.getActivationTime())
                        .tiempoExpiracion(task.getExpirationTime())
                        .build();
                tareas.add(respuestaTarea);

            }
            return tareas;
        } catch (Exception e) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("fin - listar - tareas estados instancias proceso ");
        }
    }


    /**
     * Permite listar las tareas por estados
     *
     * @param entrada Objeto que contiene los parametros de entrada para un proceso
     * @return lista de tareas que cumplen con los filtros de estado solicitdos
     * @throws MalformedURLException
     */
    @Override
    public List<RespuestaTareaBamDTO> listarTareasCompletadas(EntradaProcesoDTO entrada) throws SystemException {

        try {
            log.info("iniciar - listar tareas completadas por usuario: {}", entrada);

            return em.createNamedQuery("BamTaskSummary.findTaskComplete", RespuestaTareaBamDTO.class)
                    .setParameter("ESTADO", Status.Completed.name())
                    .setParameter("USUARIO", entrada.getParametros().get("usuario").toString())
                    .getResultList();

        } catch (Exception e) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("fin - listar tareas completadas por usuario ");
        }
    }
}
