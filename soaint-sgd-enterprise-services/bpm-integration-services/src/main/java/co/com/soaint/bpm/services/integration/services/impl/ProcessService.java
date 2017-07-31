package co.com.soaint.bpm.services.integration.services.impl;

import co.com.soaint.bpm.services.integration.services.IProcessServices;
import co.com.soaint.foundation.canonical.bpm.*;
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
import org.kie.api.runtime.manager.audit.AuditService;
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

/**
 * Created by Arce on 6/7/2017.
 */
@Service
public class ProcessService implements IProcessServices {

    private RuntimeEngine engine;
    private KieSession ksession;
    private TaskService taskService;

    private String idDespliegue = "";

    private AuditService auditService;
    @Value( "${procesos.listar.endpoint.url}" )
    private String endpointProcesosListar = "";
    @Value( "${jbpm.endpoint.url}" )
    private String endpointJBPConsole = "";
    @Value( "${procesos.listar.intancias.endpoint.url}" )
    private String endpointProcesoListarInstancia = "";
    @Value( "${jbpmconsole.admin.user}" )
    private String usuarioAdmin = "";
    @Value( "${jbpmconsole.admin.pass}" )
    private String passAdmin = "";
    @Value( "${tarea.acciones.url}" )
    private String endpointTareas = "";
    HttpClient httpClient;
    HttpGet getRequest;
    HttpPost postRequest;
    HttpResponse response;


    private ProcessService() throws MalformedURLException {;
    }

    @Override
    public List<RespuestaProcesoDTO> listarProcesos(EntradaProcesoDTO entrada) throws IOException, JSONException {

        String encoding = java.util.Base64.getEncoder().encodeToString(new String(usuarioAdmin + ":" + passAdmin).getBytes());
        httpClient = HttpClientBuilder.create().build();
        getRequest = new HttpGet(endpointProcesosListar);
        List<RespuestaProcesoDTO> listaProcesos = new ArrayList<>();
        getRequest.addHeader("Accept", "application/json");
        getRequest.addHeader("Authorization",  "Basic " + encoding);
        HttpResponse response = httpClient.execute(getRequest);
        JSONObject respuestaJson = new JSONObject(EntityUtils.toString(response.getEntity()));

        //        // Check for HTTP response code: 200 = success
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
        } else {
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
                    listaProcesos.add(respuesta);
                }
            }
        }
        return listaProcesos;
    }

    @Override
    public List<RespuestaProcesoDTO> listarProcesosInstanciaPorUsuarios(EntradaProcesoDTO entrada) throws IOException, JSONException, URISyntaxException {
        String encoding = java.util.Base64.getEncoder().encodeToString(new String(usuarioAdmin + ":" + passAdmin).getBytes());
        URI uri = new URIBuilder(endpointProcesoListarInstancia)
                .addParameter("var_initiator", entrada.getUsuario())
                .addParameters(listaEstadosProceso(entrada))
                .build();
        httpClient = HttpClientBuilder.create().build();
        getRequest = new HttpGet(uri);
        List<RespuestaProcesoDTO> listaProcesos = new ArrayList<>();
        getRequest.addHeader("Accept", "application/json");
        getRequest.addHeader("Authorization",  "Basic " + encoding);
        HttpResponse response = httpClient.execute(getRequest);
        JSONObject respuestaJson = new JSONObject(EntityUtils.toString(response.getEntity()));

        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
        } else {
            JSONArray listaProcesosJson = respuestaJson.getJSONArray("processInstanceInfoList");
            for (int i = 0; i < listaProcesosJson.length(); i++) {
                JSONObject proceso = (JSONObject) listaProcesosJson.get(i);
                Iterator keys = proceso.keys();
                while (keys.hasNext()) {
                    Object key = keys.next();
                    if (key.toString().equalsIgnoreCase("process-instance")) {
                        JSONObject valor = proceso.getJSONObject((String) key);
                        RespuestaProcesoDTO respuesta = RespuestaProcesoDTO.newInstance()
                                .codigoProceso(valor.getString("id"))
                                .nombreProceso(valor.getString("process-id"))
                                .estado(valor.getString("state"))
                                .build();
                        listaProcesos.add(respuesta);
                    }
                }
            }
        }
        return listaProcesos;
    }


    @Override
    public RespuestaProcesoDTO iniciarProceso(EntradaProcesoDTO entrada) throws MalformedURLException {
        ksession = obtenerEngine(entrada).getKieSession();
        ProcessInstance processInstance = ksession.startProcess(entrada.getIdProceso(), entrada.getParametros());
        long procId = processInstance.getId();
        RespuestaProcesoDTO respuesta = RespuestaProcesoDTO.newInstance()
                .codigoProceso(String.valueOf(processInstance.getId()))
                .nombreProceso(processInstance.getProcessId())
                .estado(String.valueOf(processInstance.getState()))
                .idDespliegue(entrada.getIdDespliegue())
                .build();
        return respuesta;
    }

    @Override
    public RespuestaProcesoDTO iniciarProcesoManual(EntradaProcesoDTO entrada) throws IOException, JSONException, URISyntaxException {
        EntradaProcesoDTO entradaManual = new EntradaProcesoDTO();
        List<RespuestaTareaDTO> tareas = new ArrayList<>();
        entradaManual.setIdDespliegue(entrada.getIdDespliegue());
        entradaManual.setUsuario(usuarioAdmin);
        entradaManual.setPass(passAdmin);
        ksession = obtenerEngine(entradaManual).getKieSession();
        ProcessInstance processInstance = ksession.startProcess(entrada.getIdProceso(), entrada.getParametros());
        long procId = processInstance.getId();
        entrada.setInstanciaProceso(processInstance.getId());
        tareas = listarTareasPorInstanciaProceso(entrada);
        for (RespuestaTareaDTO tarea : tareas) {
            entrada.setIdTarea(tarea.getIdTarea());
            reservarTarea(entrada);

        }
        RespuestaProcesoDTO respuesta = RespuestaProcesoDTO.newInstance()
                .codigoProceso(String.valueOf(processInstance.getId()))
                .nombreProceso(processInstance.getProcessId())
                .estado(String.valueOf(processInstance.getState()))
                .idDespliegue(entrada.getIdDespliegue())
                .build();
        return respuesta;
    }

    @Override
    public RespuestaProcesoDTO enviarSenalProceso(EntradaSennalDTO entrada) throws IOException, JSONException {
        EntradaSennalDTO entradaManual = new EntradaSennalDTO();
        entradaManual.setIdDespliegue(entrada.getIdDespliegue());
        entradaManual.setUsuario(usuarioAdmin);
        entradaManual.setPass(passAdmin);

        ksession = obtenerEngine(entradaManual).getKieSession();
        ProcessInstance processInstance = ksession.getProcessInstance(entrada.getInstanciaProceso());

        org.json.JSONObject datosProceso = new org.json.JSONObject();
        datosProceso.put("numeroRadicado", entrada.getParametros().getOrDefault("nroRadicado","RAD87091806789").toString());
        datosProceso.put("ideEcm",entrada.getParametros().getOrDefault("ideEcm","12345").toString());

        ksession.signalEvent("estadoDigitalizacion", datosProceso.toString(), processInstance.getId());

        RespuestaProcesoDTO respuesta = RespuestaProcesoDTO.newInstance()
                .codigoProceso(String.valueOf(processInstance.getId()))
                .nombreProceso(processInstance.getProcessId())
                .estado(String.valueOf(processInstance.getState()))
                .idDespliegue(entradaManual.getIdDespliegue())
                .build();

        return respuesta;
    }

    @Override
    public RespuestaProcesoDTO senalInicioAutomatico(EntradaProcesoDTO entrada) throws IOException, JSONException {

        EntradaProcesoDTO entradaManual = new EntradaProcesoDTO();
        entradaManual.setIdDespliegue(entrada.getIdDespliegue());
        entradaManual.setUsuario(usuarioAdmin);
        entradaManual.setPass(passAdmin);

        ksession = obtenerEngine(entradaManual).getKieSession();

        org.json.JSONObject datosProceso = new org.json.JSONObject();
        datosProceso.put("numeroRadicado", entrada.getParametros().getOrDefault("nroRadicado","RAD87091806789").toString());
        datosProceso.put("requiereDigitalizacion", (int)entrada.getParametros().getOrDefault("requiereDigitalizacion",1));
        datosProceso.put("requiereDistribucion", (int)entrada.getParametros().getOrDefault("requiereDistribucion",1));

        ksession.signalEvent("inicioAutomatico", datosProceso.toString());

        RespuestaProcesoDTO respuesta = RespuestaProcesoDTO.newInstance()
                .idDespliegue(entradaManual.getIdDespliegue())
                .build();

        return respuesta;
    }

    @Override
    public RespuestaTareaDTO iniciarTarea(EntradaProcesoDTO entrada) throws MalformedURLException {
        taskService = obtenerEngine(entrada).getTaskService();
        taskService.start(entrada.getIdTarea(), entrada.getUsuario());
        Task task = taskService.getTaskById(entrada.getIdTarea());
        RespuestaTareaDTO respuestaTarea = RespuestaTareaDTO.newInstance()
                .idTarea(entrada.getIdTarea())
                .estado(String.valueOf(EstadosEnum.ENPROGRESO))
                .nombre(task.getName())
 //               .idCreador(task.getTaskData().getCreatedBy().getId())
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
        return respuestaTarea;
    }

    @Override
    public RespuestaTareaDTO completarTarea(EntradaProcesoDTO entrada) throws MalformedURLException {
        taskService = obtenerEngine(entrada).getTaskService();
        taskService.complete(entrada.getIdTarea(), entrada.getUsuario(), entrada.getParametros());

        RespuestaTareaDTO respuestaTarea = RespuestaTareaDTO.newInstance()
                .idTarea(entrada.getIdTarea())
                .estado(String.valueOf(EstadosEnum.COMPLETADO))
                .idProceso(entrada.getIdProceso())
                .build();

        return respuestaTarea;
    }

    @Override
    public RespuestaTareaDTO reservarTarea(EntradaProcesoDTO entrada) throws IOException, URISyntaxException, JSONException {
        String encoding = java.util.Base64.getEncoder().encodeToString(new String(entrada.getUsuario() + ":" + entrada.getPass()).getBytes());
        URI uri = new URIBuilder(endpointJBPConsole)
                .setPath("/jbpm-console/rest/task/"+String.valueOf(entrada.getIdTarea())+ "/claim")
                .build();
        httpClient = HttpClientBuilder.create().build();
        postRequest = new HttpPost(uri);
        postRequest.addHeader("Authorization", "Basic " + encoding);
        postRequest.addHeader("X-KIE-ContentType", "XSTREAM");
        postRequest.addHeader("Accept", "application/json");
        response = httpClient.execute(postRequest);
        JSONObject respuestaJson = new JSONObject(EntityUtils.toString(response.getEntity()));

        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
        } else {
            String estadoResp = respuestaJson.getString("status");
            String estado;
            if(estadoResp.equals("SUCCESS")){
                estado =  String.valueOf(EstadosEnum.RESERVADO);
            }else{
                estado =  String.valueOf(EstadosEnum.ERROR);
            }
            RespuestaTareaDTO respuestaTarea = RespuestaTareaDTO.newInstance()
                    .idTarea(entrada.getIdTarea())
                    .estado(estado)
                    .idProceso(entrada.getIdProceso())
                    .idDespliegue(entrada.getIdDespliegue())
                    .build();
            return respuestaTarea;
        }
    }

    @Override
    public List<RespuestaTareaDTO> listarTareasEstados(EntradaProcesoDTO entrada) throws MalformedURLException {
        List<RespuestaTareaDTO> tareas = new ArrayList<>();
        Iterator<EstadosEnum> estadosEnviados = entrada.getEstados().iterator();
        List<Status> estadosActivos = estadosActivos(estadosEnviados);
        taskService = obtenerEngine(entrada).getTaskService();
        List<TaskSummary> tasks = taskService.getTasksOwnedByStatus(entrada.getUsuario(), estadosActivos, "en-UK");
        long taskId = -1;
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
    }

    @Override
    public List<RespuestaTareaDTO> listarTareasEstadosInstanciaProceso(EntradaProcesoDTO entrada) throws MalformedURLException {
        List<RespuestaTareaDTO> tareas = new ArrayList<>();
        Iterator<EstadosEnum> estadosEnviados = entrada.getEstados().iterator();
        List<Status> estadosActivos = estadosActivos(estadosEnviados);
        taskService = obtenerEngine(entrada).getTaskService();
        List<TaskSummary> tasks = taskService.getTasksOwnedByStatus(entrada.getUsuario(), estadosActivos, "en-UK");
        long taskId = -1;
        for (TaskSummary task : tasks) {
            if (task.getProcessInstanceId().longValue() == entrada.getInstanciaProceso().longValue() ) {
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
    }

    @Override
    public List<RespuestaTareaDTO> listarTareasPorInstanciaProceso(EntradaProcesoDTO entrada) throws MalformedURLException {
        List<RespuestaTareaDTO> tareas = new ArrayList<>();
        Iterator<EstadosEnum> estadosEnviados = entrada.getEstados().iterator();
        List<Status> estadosActivos = estadosActivos(estadosEnviados);
        taskService = obtenerEngine(entrada).getTaskService();
        List<TaskSummary> tasks = taskService.getTasksByStatusByProcessInstanceId(entrada.getInstanciaProceso(), estadosActivos, "en-UK");
        long taskId = -1;
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
    }

    @Override
    public List<RespuestaTareaDTO> listarTareasEstadosPorUsuario(EntradaProcesoDTO entrada) throws MalformedURLException {
        List<RespuestaTareaDTO> tareas = new ArrayList<>();
        Iterator<EstadosEnum> estadosEnviados = entrada.getEstados().iterator();
        List<Status> estadosActivos = estadosActivos(estadosEnviados);
        taskService = obtenerEngine(entrada).getTaskService();
        List<TaskSummary> tasks = taskService.getTasksOwnedByStatus(entrada.getUsuario(), estadosActivos, "en-UK");
        long taskId = -1;
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
    }


    private RuntimeEngine obtenerEngine(EntradaProcesoDTO entrada) throws MalformedURLException {
        engine = RemoteRuntimeEngineFactory.newRestBuilder()
                .addDeploymentId(entrada.getIdDespliegue())
                .addUserName(entrada.getUsuario())
                .addPassword(entrada.getPass())
                .addUrl(new URL(endpointJBPConsole))
                        //.addExtraJaxbClasses(ProcessRequestContext.class)
                .build();
        return engine;
    }

    private RuntimeEngine obtenerEngine(EntradaSennalDTO entrada) throws MalformedURLException {
        engine = RemoteRuntimeEngineFactory.newRestBuilder()
                .addDeploymentId(entrada.getIdDespliegue())
                .addUserName(entrada.getUsuario())
                .addPassword(entrada.getPass())
                .addUrl(new URL(endpointJBPConsole))
                //.addExtraJaxbClasses(ProcessRequestContext.class)
                .build();
        return engine;
    }

    private List<Status> estadosActivos(Iterator<EstadosEnum> estadosEnviados) throws MalformedURLException {
        List<Status> estadosActivos = new ArrayList<>();
        while (estadosEnviados.hasNext()) {
            switch (estadosEnviados.next()) {
                case CREADO:
                    estadosActivos.add(Status.Created);
                    break;
                case LISTO:
                    estadosActivos.add(Status.Ready);
                    break;
                case RESERVADO:
                    estadosActivos.add(Status.Reserved);
                    break;
                case SUSPENDIDO:
                    estadosActivos.add(Status.Suspended);
                    break;
                case ENPROGRESO:
                    estadosActivos.add(Status.InProgress);
                    break;
                case COMPLETADO:
                    estadosActivos.add(Status.Completed);
                    break;
                case  FALLIDO:
                    estadosActivos.add(Status.Failed);
                    break;
                case  ERROR:
                    estadosActivos.add(Status.Error);
                    break;
                case  SALIDO:
                    estadosActivos.add(Status.Exited);
                    break;
                case  OBSOLETO:
                    estadosActivos.add(Status.Obsolete);
                    break;
                default:
                    System.out.println("Invalid selection");
                    break;
            }
        }
        return estadosActivos;
    }

    private String estadoRespuesta(String estado) throws MalformedURLException {
        EstadosEnum estadoRespuesta = null;
            switch (estado) {
                case "Created":
                    estadoRespuesta = EstadosEnum.CREADO;
                    break;
                case "Ready":
                    estadoRespuesta = EstadosEnum.LISTO;
                    break;
                case "Reserved":
                    estadoRespuesta = EstadosEnum.RESERVADO;
                    break;
                case "Suspended":
                    estadoRespuesta = EstadosEnum.SUSPENDIDO;
                    break;
                case "InProgress":
                    estadoRespuesta = EstadosEnum.ENPROGRESO;
                    break;
                case "Completed":
                    estadoRespuesta = EstadosEnum.COMPLETADO;
                    break;
                case  "Failed":
                    estadoRespuesta = EstadosEnum.FALLIDO;
                    break;
                case  "Error":
                    estadoRespuesta = EstadosEnum.ERROR;
                    break;
                case  "Exited":
                    estadoRespuesta = EstadosEnum.SALIDO;
                    break;
                case  "Obsolete":
                    estadoRespuesta = EstadosEnum.OBSOLETO;
                    break;
                default:
                    System.out.println("Invalid selection");
                    break;
            }

        return estadoRespuesta.toString();
    }

    private List<NameValuePair> listaEstadosProceso(EntradaProcesoDTO entrada) {
        List<NameValuePair> estadoProceso= new ArrayList<>();

        for (String key : entrada.getParametros().keySet()) {
            Object value = entrada.getParametros().get(key);
            estadoProceso.add(new BasicNameValuePair("pist", value.toString()));
        }

        return estadoProceso;
    }


}

