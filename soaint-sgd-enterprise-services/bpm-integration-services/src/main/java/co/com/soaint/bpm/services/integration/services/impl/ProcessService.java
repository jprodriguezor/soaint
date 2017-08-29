package co.com.soaint.bpm.services.integration.services.impl;

import co.com.soaint.bpm.services.integration.services.IProcessServices;
import co.com.soaint.bpm.services.util.SystemParameters;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.bpm.EstadosEnum;
import co.com.soaint.foundation.canonical.bpm.RespuestaProcesoDTO;
import co.com.soaint.foundation.canonical.bpm.RespuestaTareaDTO;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.hornetq.utils.json.JSONArray;
import org.hornetq.utils.json.JSONException;
import org.hornetq.utils.json.JSONObject;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Status;
import org.kie.api.task.model.Task;
import org.kie.api.task.model.TaskSummary;
import org.kie.services.client.api.RemoteRuntimeEngineFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Arce on 6/7/2017.
 */
@Service
@Log4j2
public class ProcessService implements IProcessServices {

    private RuntimeEngine engine;
    private KieSession ksession;
    private TaskService taskService;
    @Value("${procesos.listar.endpoint.url}")
    private String endpointProcesosListar = "";
    @Value("${procesos.listar.intancias.endpoint.url}")
    private String endpointProcesoVariablesListar = "";
    @Value("${jbpm.endpoint.url}")
    private String endpointJBPConsole = "";
    @Value("${procesos.listar.intancias.endpoint.url}")
    private String endpointProcesoListarInstancia = "";
    @Value("${jbpmconsole.admin.user}")
    private String usuarioAdmin = "";
    @Value("${jbpmconsole.admin.pass}")
    private String passAdmin = "";
    @Value("${tarea.acciones.url}")
    private String endpointTareas = "";
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
    @Value("${mensaje.error.sistema}")
    private String errorSistema = "";
    @Value("${mensaje.error.sistema.generico}")
    private String errorSistemaGenerico = "";
    @Value("${parametro.sennal}")
    private String parametroSennal = "";
    @Value("${formato.idioma}")
    private String formatoIdioma = "";
    @Value("${protocolo}")
    private String protocolo = "";
    HttpClient httpClient;
    HttpGet getRequest;
    HttpPost postRequest;
    HttpResponse response;


    private ProcessService() {
    }

    /**
     * Permite listar todos los procesos desplegados
     *
     * @param entrada Objeto que contiene los parametros de entrada para un proceso
     * @return Lista de procesos que contiene codigoProceso,nombreProceso y idDespliegue.
     * @throws BusinessException
     * @throws SystemException
     */
    @Override
    public List<RespuestaProcesoDTO> listarProcesos(EntradaProcesoDTO entrada) throws SystemException {
        log.info("iniciar - Listar Procesos: {}", entrada);
        String encoding = java.util.Base64.getEncoder().encodeToString(new String(usuarioAdmin + ":" + passAdmin).getBytes());
        httpClient = HttpClientBuilder.create().build();
        getRequest = new HttpGet(protocolo.concat(SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_ENDPOINT)).concat(endpointProcesosListar));
        List<RespuestaProcesoDTO> listaProcesos = new ArrayList<>();
        getRequest.addHeader(headerAccept, valueApplicationType);
        getRequest.addHeader(headerAuthorization, valueAuthorization + " " + encoding);
        try {
            response = httpClient.execute(getRequest);
            JSONObject respuestaJson = new JSONObject(EntityUtils.toString(response.getEntity()));
            if (response.getStatusLine().getStatusCode() != 200) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage(errorNegocioFallo + response.getStatusLine().getStatusCode())
                        .buildBusinessException();
            } else {
                listaProcesos = procesarListaProcesos(respuestaJson);
            }

            return listaProcesos;
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
            log.info("fin - iniciar - listar Proceso ");
        }

    }


    /**
     * Permite listar todos los procesos desplegados
     *
     * @param entrada Objeto que contiene los parametros de entrada para un proceso
     * @return Lista de procesos que contiene codigoProceso,nombreProceso y idDespliegue.
     * @throws BusinessException
     * @throws SystemException
     */
    @Override
    public String listarVariablesProcesos(EntradaProcesoDTO entrada) throws SystemException {
        log.info("iniciar - Listar Procesos: {}", entrada);
        String encoding = java.util.Base64.getEncoder().encodeToString(new String(usuarioAdmin + ":" + passAdmin).getBytes());
        try {
            URI uri = new URIBuilder(protocolo.concat(SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_ENDPOINT)))
                    .setPath("jbpm-console/rest/runtime/".concat(entrada.getIdDespliegue()).concat("/withvars/process/instance/").concat(String.valueOf(entrada.getInstanciaProceso())))
                    .build();
            httpClient = HttpClientBuilder.create().build();
            getRequest = new HttpGet(uri);
            getRequest.addHeader(headerAccept, valueApplicationType);
            getRequest.addHeader(headerAuthorization, valueAuthorization + " " + encoding);

            response = httpClient.execute(getRequest);
            JSONObject respuestaJson = new JSONObject(EntityUtils.toString(response.getEntity()));
            if (response.getStatusLine().getStatusCode() != 200) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage(errorNegocioFallo + response.getStatusLine().getStatusCode())
                        .buildBusinessException();
            }
            return String.valueOf(respuestaJson);
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
            log.info("fin - iniciar - listar Proceso ");
        }
    }

    /**
     * Permite listar las instancias de procesos por usuario
     *
     * @param entrada Objeto que contiene los parametros de entrada para un proceso
     * @return Lista de procesos que contiene codigoProceso,nombreProceso y estado.
     * @throws SystemException
     */
    @Override
    public List<RespuestaProcesoDTO> listarProcesosInstanciaPorUsuarios(EntradaProcesoDTO entrada) throws SystemException {
        log.info("iniciar - listar instancias de usarios de proceso ");
        String encoding = java.util.Base64.getEncoder().encodeToString(new String(usuarioAdmin + ":" + passAdmin).getBytes());
        List<RespuestaProcesoDTO> listaProcesos = new ArrayList<>();
        httpClient = HttpClientBuilder.create().build();
        try {
            URI uri = new URIBuilder(protocolo.concat(SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_ENDPOINT)).concat(endpointProcesoListarInstancia))
                    .addParameter("var_initiator", entrada.getUsuario())
                    .addParameters(listaEstadosProceso(entrada))
                    .build();
            getRequest = new HttpGet(uri);
            getRequest.addHeader(headerAccept, valueApplicationType);
            getRequest.addHeader(headerAuthorization, valueAuthorization + " " + encoding);
            response = httpClient.execute(getRequest);
            JSONObject respuestaJson = new JSONObject(EntityUtils.toString(response.getEntity()));
            if (response.getStatusLine().getStatusCode() != 200) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage(errorNegocioFallo + response.getStatusLine().getStatusCode())
                        .buildBusinessException();
            } else {
                listaProcesos = procesarListaProcesosUsuario(respuestaJson);
            }
            return listaProcesos;
        } catch (BusinessException e) {
            log.error(e.getMessage());
            throw ExceptionBuilder.newBuilder()
                    .withMessage(e.getMessage())
                    .withRootException(e)
                    .buildSystemException();
        } catch (Exception ex) {
            log.error("System error has occurred");
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        } finally {
            log.info("fin - listar instancias de usarios de proceso ");
        }


    }

    /**
     * Permite iniciar un proceso
     *
     * @param entrada Objeto que contiene los parametros de entrada para un proceso
     * @return Los datos del proceso que fue iniciado codigoProceso,nombreProceso,estado y idDespliegue
     * @throws SystemException
     */
    @Override
    public RespuestaProcesoDTO iniciarProceso(EntradaProcesoDTO entrada) throws SystemException {
        try {
            log.info("iniciar - proceso : {}", entrada);
            ksession = obtenerEngine(entrada).getKieSession();
            ProcessInstance processInstance = ksession.startProcess(entrada.getIdProceso(), entrada.getParametros());
            return RespuestaProcesoDTO.newInstance()
                    .codigoProceso(String.valueOf(processInstance.getId()))
                    .nombreProceso(processInstance.getProcessId())
                    .estado(String.valueOf(processInstance.getState()))
                    .idDespliegue(entrada.getIdDespliegue())
                    .build();

        } catch (Exception e) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("fin - iniciar - proceso ");
        }

    }


    /**
     * Permite iniciar un proceso y asignarle una tarea a una tercera persona
     * @param entrada
     * @return Los datos del proceso que fue iniciado codigoProceso,nombreProceso,estado y idDespliegue
     * @throws SystemException
     */
    @Override
    public RespuestaProcesoDTO iniciarProcesoPorTercero(EntradaProcesoDTO entrada) throws SystemException {
        try {

            entrada.setUsuario(usuarioAdmin);
            entrada.setPass(passAdmin);
            RespuestaProcesoDTO processInstance = iniciarProcesoManual(entrada);
            reasignarTarea(entrada);
            return RespuestaProcesoDTO.newInstance()
                    .codigoProceso(String.valueOf(processInstance.getCodigoProceso()))
                    .nombreProceso(processInstance.getNombreProceso())
                    .estado(String.valueOf(processInstance.getEstado()))
                    .idDespliegue(entrada.getIdDespliegue())
                    .build();

        } catch (Exception e) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("fin - iniciar - proceso ");
        }

    }

    /**
     * Permite iniciar un proceso y asignar una tarea de manera auntomatica a un usuario
     *
     * @param entrada Objeto que contiene los parametros de entrada para un proceso
     * @return Los datos del proceso que fue iniciado codigoProceso,nombreProceso,estado y idDespliegue
     * @throws SystemException
     */
    @Override
    public RespuestaProcesoDTO iniciarProcesoManual(EntradaProcesoDTO entrada) throws SystemException {
        EntradaProcesoDTO entradaManual = new EntradaProcesoDTO();
        entradaManual.setIdDespliegue(entrada.getIdDespliegue());
        entradaManual.setUsuario(usuarioAdmin);
        entradaManual.setPass(passAdmin);
        try {
            log.info("iniciar - proceso manual: {}", entrada);
            ksession = obtenerEngine(entradaManual).getKieSession();
            ProcessInstance processInstance = ksession.startProcess(entrada.getIdProceso(), entrada.getParametros());
            entrada.setInstanciaProceso(processInstance.getId());
            List<RespuestaTareaDTO> tareas = listarTareasPorInstanciaProceso(entrada);
            for (RespuestaTareaDTO tarea : tareas) {
                entrada.setIdTarea(tarea.getIdTarea());
                reservarTarea(entrada);

            }
            return RespuestaProcesoDTO.newInstance()
                    .codigoProceso(String.valueOf(processInstance.getId()))
                    .nombreProceso(processInstance.getProcessId())
                    .estado(String.valueOf(processInstance.getState()))
                    .idDespliegue(entrada.getIdDespliegue())
                    .build();
        } catch (Exception e) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("fin - iniciar - proceso manual ");
        }
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
            taskService = obtenerEngine(entrada).getTaskService();
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
            taskService = obtenerEngine(entrada).getTaskService();
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
        String estado = "";
        log.info("iniciar - reservar tarea: {}", entrada);
        try {
            URI uri = new URIBuilder(protocolo.concat(SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_ENDPOINT)))
                    .setPath("/jbpm-console/rest/task/" + entrada.getIdTarea() + "/claim")
                    .build();
            httpClient = HttpClientBuilder.create().build();
            postRequest = new HttpPost(uri);
            postRequest.addHeader(headerAuthorization, valueAuthorization + " " + encoding);
            postRequest.addHeader("X-KIE-ContentType", "XSTREAM");
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
            taskService = obtenerEngine(entrada).getTaskService();
            taskService.delegate(entrada.getIdTarea(), entrada.getUsuario(), entrada.getParametros().get("usuarioReasignar").toString());
            Task task = taskService.getTaskById(entrada.getIdTarea());
            return RespuestaTareaDTO.newInstance()
                    .idTarea(entrada.getIdTarea())
                    .estado(String.valueOf(EstadosEnum.RESERVADO))
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
        Iterator<EstadosEnum> estadosEnviados = entrada.getEstados().iterator();
        List<Status> estadosActivos = estadosActivos(estadosEnviados);
        try {
            log.info("iniciar - listar tareas estados: {}", entrada);
            taskService = obtenerEngine(entrada).getTaskService();
            List<TaskSummary> tasks = taskService.getTasksOwnedByStatus(entrada.getUsuario(), estadosActivos, formatoIdioma);
            for (TaskSummary task : tasks) {
                RespuestaTareaDTO respuestaTarea = RespuestaTareaDTO.newInstance()
                        .idTarea(task.getId())
                        .estado(estadoRespuesta(task.getStatusId()))
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
        List<Status> estadosActivos = estadosActivos(estadosEnviados);
        try {
            log.info("iniciar - listar tareas estados instancias proceso: {}", entrada);
            taskService = obtenerEngine(entrada).getTaskService();
            List<TaskSummary> tasks = taskService.getTasksOwnedByStatus(entrada.getUsuario(), estadosActivos, formatoIdioma);
            for (TaskSummary task : tasks) {
                if (task.getProcessInstanceId().longValue() == entrada.getInstanciaProceso().longValue()) {
                    RespuestaTareaDTO respuestaTarea = RespuestaTareaDTO.newInstance()
                            .idTarea(task.getId())
                            .estado(estadoRespuesta(task.getStatusId()))
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
        List<Status> estadosActivos = estadosActivos(estadosEnviados);
        try {
            log.info("iniciar - listar tareas instancias proceso: {}", entrada);
            taskService = obtenerEngine(entrada).getTaskService();
            List<TaskSummary> tasks = taskService.getTasksByStatusByProcessInstanceId(entrada.getInstanciaProceso(), estadosActivos, formatoIdioma);
            for (TaskSummary task : tasks) {
                RespuestaTareaDTO respuestaTarea = RespuestaTareaDTO.newInstance()
                        .idTarea(task.getId())
                        .estado(estadoRespuesta(task.getStatusId()))
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
        List<Status> estadosActivos = estadosActivos(estadosEnviados);
        try {
            log.info("iniciar - listar tareas estados usuarios: {}", entrada);
            taskService = obtenerEngine(entrada).getTaskService();
            List<TaskSummary> tasks = taskService.getTasksOwnedByStatus(entrada.getUsuario(), estadosActivos, formatoIdioma);
            for (TaskSummary task : tasks) {

                RespuestaTareaDTO respuestaTarea = RespuestaTareaDTO.newInstance()
                        .idTarea(task.getId())
                        .estado(estadoRespuesta(task.getStatusId()))
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
     * Permite enviar una sennal a un proceso
     *
     * @param entrada Objeto que contiene los parametros de entrada para un proceso
     * @return Los datos del proceso que fue invocado
     * @throws IOException
     * @throws JSONException
     */
    @Override
    public RespuestaProcesoDTO enviarSenalProceso(EntradaProcesoDTO entrada) throws SystemException {

        EntradaProcesoDTO entradaManual = new EntradaProcesoDTO();
        entradaManual.setIdDespliegue(entrada.getIdDespliegue());
        entradaManual.setUsuario(usuarioAdmin);
        entradaManual.setPass(passAdmin);
        try {
            log.info("iniciar - enviar sennal a proceso: {}", entrada);
            ksession = obtenerEngine(entradaManual).getKieSession();
            ProcessInstance processInstance = ksession.getProcessInstance(entrada.getInstanciaProceso());
            String nombreSennal = entrada.getParametros().getOrDefault(parametroSennal, "estadoDigitalizacion").toString();

            org.json.JSONObject jsonProceso = new org.json.JSONObject();
            for (Map.Entry<String, Object> entry : entrada.getParametros().entrySet()) {
                if (!parametroSennal.equalsIgnoreCase(entry.getKey())) {
                    jsonProceso.put(entry.getKey(), entry.getValue().toString());
                }
            }
            String datosProceso = jsonProceso.toString();
            log.info("JSON: ".concat(datosProceso));
            ksession.signalEvent(nombreSennal, datosProceso, processInstance.getId());
            return RespuestaProcesoDTO.newInstance()
                    .codigoProceso(String.valueOf(processInstance.getId()))
                    .nombreProceso(processInstance.getProcessId())
                    .estado(String.valueOf(processInstance.getState()))
                    .idDespliegue(entradaManual.getIdDespliegue())
                    .build();
        } catch (Exception e) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("fin - enviar sennal a proceso ");
        }
    }

    /**
     * Permite invocar automaticamente una sennal
     *
     * @param entrada Objeto que contiene los parametros de entrada para un proceso
     * @return Los datos del proceso que fue invocado
     * @throws IOException
     * @throws JSONException
     */
    @Override
    public RespuestaProcesoDTO senalInicioAutomatico(EntradaProcesoDTO entrada) throws SystemException {

        EntradaProcesoDTO entradaManual = new EntradaProcesoDTO();
        entradaManual.setIdDespliegue(entrada.getIdDespliegue());
        entradaManual.setUsuario(usuarioAdmin);
        entradaManual.setPass(passAdmin);
        try {
            log.info("iniciar -  sennal inicio auntomatico: {}", entrada);
            ksession = obtenerEngine(entradaManual).getKieSession();
            String nombreSennal = entrada.getParametros().getOrDefault(parametroSennal, "inicioAutomatico").toString();

            org.json.JSONObject jsonProceso = new org.json.JSONObject();
            for (Map.Entry<String, Object> entry : entrada.getParametros().entrySet()) {
                if (!parametroSennal.equalsIgnoreCase(entry.getKey())) {
                    jsonProceso.put(entry.getKey(), entry.getValue().toString());
                }
            }
            String datosProceso = jsonProceso.toString();
            log.info("JSON inicio: ".concat(datosProceso));
            ksession.signalEvent(nombreSennal, datosProceso);
            return RespuestaProcesoDTO.newInstance()
                    .idDespliegue(entradaManual.getIdDespliegue())
                    .build();
        } catch (Exception e) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("fin - sennal inicio auntomatico ");
        }
    }


    /**
     * Permite crear la conexion con el servidor JPBM
     *
     * @param entrada Objeto que contiene los parametros de entrada para un proceso
     * @return Objeto conexion JBPM
     * @throws MalformedURLException
     */
    private RuntimeEngine obtenerEngine(EntradaProcesoDTO entrada) throws MalformedURLException {
        engine = RemoteRuntimeEngineFactory.newRestBuilder()
                .addDeploymentId(entrada.getIdDespliegue())
                .addUserName(entrada.getUsuario())
                .addPassword(entrada.getPass())
                .addUrl(new URL("http://".concat(SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_ENDPOINT)).concat(endpointJBPConsole)))
                .build();
        return engine;
    }

    /**
     * Permite permite parsear los estados que son enviados en las llamadas
     *
     * @param estadosEnviados Enum de estados
     * @return Lista de estados activos en la llamada
     * @throws MalformedURLException
     */
    private List<Status> estadosActivos(Iterator<EstadosEnum> estadosEnviados) {
        List<Status> estadosActivos = new ArrayList<>();
        while (estadosEnviados.hasNext()) {
            String estadoActivo = estadosEnviados.next().name();
            for (EstadosEnum estado : EstadosEnum.values()) {
                if (estado.name().equals(estadoActivo)) {
                    estadosActivos.add(Status.valueOf(estado.getNombre()));
                }
            }
        }
        return estadosActivos;
    }

    /**
     * Permite cambiar el idioma del estado
     *
     * @param estado cadena de texto con el estado en Ingles
     * @return estado en espannol
     * @throws MalformedURLException
     */
    private String estadoRespuesta(String estado) {

        for (Status enumEstado : Status.values()) {
            if (enumEstado.name().equals(estado)) {
                return EstadosEnum.obtenerClave(estado).toString();
            }
        }
        return estado;
    }

    /**
     * Permite crear lista de parametros para realizar llamada
     *
     * @param entrada Objeto que contiene los parametros de entrada para un proceso
     * @return lista de valores
     */
    private List<NameValuePair> listaEstadosProceso(EntradaProcesoDTO entrada) {
        List<NameValuePair> estadoProceso = new ArrayList<>();

        for (String key : entrada.getParametros().keySet()) {
            Object value = entrada.getParametros().get(key);
            estadoProceso.add(new BasicNameValuePair("pist", value.toString()));
        }

        return estadoProceso;
    }


    /**
     * Permite procesar el json que devuelve la lista de los procesos
     *
     * @param respuestaJson objeto json que se obtiene de la respuesta al servicio
     * @return lista de procesos
     * @throws JSONException
     */
    private List<RespuestaProcesoDTO> procesarListaProcesos(JSONObject respuestaJson) throws JSONException {
        List<RespuestaProcesoDTO> listaProcesada = new ArrayList<>();
        JSONArray listaProcesosJson = respuestaJson.getJSONArray("processDefinitionList");
        for (int i = 0; i < listaProcesosJson.length(); i++) {
            JSONObject proceso = (JSONObject) listaProcesosJson.get(i);
            Iterator keys = proceso.keys();
            while (keys.hasNext()) {
                Object key = keys.next();
                JSONObject valor = proceso.getJSONObject((String) key);
                RespuestaProcesoDTO respuesta = RespuestaProcesoDTO.newInstance()
                        .codigoProceso(valor.getString("id"))
                        .nombreProceso(valor.getString("name"))
                        .idDespliegue(valor.getString("deployment-id"))
                        .build();
                listaProcesada.add(respuesta);
            }
        }
        return listaProcesada;
    }

    /**
     * Permite procesa la lista de procesos por usuario
     *
     * @param respuestaJson
     * @return objeto json que se obtiene de la respuesta al servicio
     * @throws JSONException
     */
    private List<RespuestaProcesoDTO> procesarListaProcesosUsuario(JSONObject respuestaJson) throws JSONException {
        List<RespuestaProcesoDTO> listaProcesada = new ArrayList<>();
        JSONArray listaProcesosJson = respuestaJson.getJSONArray("processInstanceInfoList");
        for (int i = 0; i < listaProcesosJson.length(); i++) {
            JSONObject proceso = (JSONObject) listaProcesosJson.get(i);
            Iterator keys = proceso.keys();
            while (keys.hasNext()) {
                Object key = keys.next();
                if ("process-instance".equalsIgnoreCase(key.toString())) {
                    JSONObject valor = proceso.getJSONObject((String) key);
                    RespuestaProcesoDTO respuesta = RespuestaProcesoDTO.newInstance()
                            .codigoProceso(valor.getString("id"))
                            .nombreProceso(valor.getString("process-id"))
                            .estado(valor.getString("state"))
                            .build();
                    listaProcesada.add(respuesta);
                }
            }
        }
        return listaProcesada;
    }
}

