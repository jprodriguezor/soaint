package co.com.soaint.bpm.services.integration.services.impl;

import co.com.soaint.bpm.services.integration.services.IProcessServices;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.bpm.EstadosEnum;
import co.com.soaint.foundation.canonical.bpm.RespuestaProcesoDTO;
import co.com.soaint.foundation.canonical.bpm.RespuestaTareaDTO;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.hornetq.utils.json.JSONArray;
import org.hornetq.utils.json.JSONException;
import org.hornetq.utils.json.JSONObject;
import org.jbpm.services.api.RuntimeDataService;
import org.jbpm.kie.services.impl.query.QueryServiceImpl;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.audit.AuditService;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Status;
import org.kie.api.task.model.TaskSummary;
import org.kie.server.client.KieServicesClient;
import org.kie.services.client.api.RemoteRuntimeEngineFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created by Arce on 6/7/2017.
 */
@Service
public class ProcessService implements IProcessServices {

    private RuntimeEngine engine;
    private KieSession ksession;
    private TaskService taskService;
    private AuditService auditService;
    @Value( "${procesos.listar.endpoint.url}" )
    private String endpointProcesosListar = "";
    @Value( "${jbpm.endpoint.url}" )
    private String endpointJBPConsole = "";
    private RuntimeDataService runtimeDataService;
    private KieServicesClient kieServicesClient;
    private QueryServiceImpl queryService;


    private ProcessService() throws MalformedURLException {

    }
    @Override
    public List<RespuestaProcesoDTO> listarProcesos(EntradaProcesoDTO entrada) throws IOException, JSONException {

        String encoding = java.util.Base64.getEncoder().encodeToString(new String(entrada.getUsuario()+":"+entrada.getPass()).getBytes());
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet getRequest = new HttpGet(endpointProcesosListar);
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
    public List<RespuestaProcesoDTO> listarProcesosInstancia(EntradaProcesoDTO entrada) throws IOException, JSONException {

        List<RespuestaProcesoDTO> listaProcesos = new ArrayList<>();
        taskService = obtenerEngine(entrada).getTaskService();
        List<TaskSummary> tasks = taskService.getTasksOwned(entrada.getUsuario(), "en-UK");
        long taskId = -1;
        for (TaskSummary task : tasks) {
            if (task.getProcessId().equals(entrada.getIdProceso() )) {
                RespuestaProcesoDTO respuesta = RespuestaProcesoDTO.newInstance()
                        .codigoProceso(String.valueOf(task.getProcessInstanceId()))
                        .nombreProceso(task.getDeploymentId())
                        .idDespliegue(task.getDeploymentId())
                        .estado(String.valueOf(task.getStatus()))
                        .build();
                listaProcesos.add(respuesta);
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
    public RespuestaTareaDTO completarTarea(EntradaProcesoDTO entrada) throws MalformedURLException {
        taskService = obtenerEngine(entrada).getTaskService();
        taskService.start(entrada.getIdTarea(), entrada.getUsuario());
        taskService.complete(entrada.getIdTarea(), entrada.getUsuario(), entrada.getParametros());

        RespuestaTareaDTO respuestaTarea = RespuestaTareaDTO.newInstance()
                .idTarea(entrada.getIdTarea())
                .estado(String.valueOf(EstadosEnum.COMPLETADO))
                .idProceso(entrada.getIdProceso())
                .idDespliegue(entrada.getIdDespliegue())
                .build();

        return respuestaTarea;
    }

    @Override
    public List<RespuestaTareaDTO> listarTareasEstados(EntradaProcesoDTO entrada) throws MalformedURLException {
        List<RespuestaTareaDTO> tareas = new ArrayList<>();
        List<Status> estadosActivos = new ArrayList<>();
        Iterator<EstadosEnum> estadosEnviados = entrada.getEstados().iterator();
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
        taskService = obtenerEngine(entrada).getTaskService();
        List<TaskSummary> tasks = taskService.getTasksOwnedByStatus(entrada.getUsuario(), estadosActivos, "en-UK");
        long taskId = -1;
        for (TaskSummary task : tasks) {
            if (task.getProcessId().equals(entrada.getIdProceso() )) {
                RespuestaTareaDTO respuestaTarea = RespuestaTareaDTO.newInstance()
                        .idTarea(task.getId())
                        .estado(task.getStatusId())
                        .idProceso(task.getProcessId())
                        .idDespliegue(task.getDeploymentId())
                        .nombre(task.getName())
                        .prioridad(task.getPriority())
                        .idInstanciaProceso(task.getProcessInstanceId())
                        .build();
                tareas.add(respuestaTarea);
            }
        }
        return tareas;
    }

    @Override
    public List<RespuestaTareaDTO> listarTareasEstadosInstanciaProceso(EntradaProcesoDTO entrada) throws MalformedURLException {
        List<RespuestaTareaDTO> tareas = new ArrayList<>();
        List<Status> estadosActivos = new ArrayList<>();
        Iterator<EstadosEnum> estadosEnviados = entrada.getEstados().iterator();
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
        taskService = obtenerEngine(entrada).getTaskService();
        List<TaskSummary> tasks = taskService.getTasksOwnedByStatus(entrada.getUsuario(), estadosActivos, "en-UK");
        long taskId = -1;
        for (TaskSummary task : tasks) {
            if (task.getProcessInstanceId().longValue() == entrada.getInstanciaProceso().longValue() ) {
                RespuestaTareaDTO respuestaTarea = RespuestaTareaDTO.newInstance()
                        .idTarea(task.getId())
                        .estado(task.getStatusId())
                        .idProceso(task.getProcessId())
                        .idDespliegue(task.getDeploymentId())
                        .nombre(task.getName())
                        .prioridad(task.getPriority())
                        .idInstanciaProceso(task.getProcessInstanceId())
                        .build();
                tareas.add(respuestaTarea);
            }
        }
        return tareas;
    }

    @Override
    public List<RespuestaTareaDTO> listarTareasEstadosPorUsuario(EntradaProcesoDTO entrada) throws MalformedURLException {
        List<RespuestaTareaDTO> tareas = new ArrayList<>();
        List<Status> estadosActivos = new ArrayList<>();
        Iterator<EstadosEnum> estadosEnviados = entrada.getEstados().iterator();
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
        taskService = obtenerEngine(entrada).getTaskService();
        List<TaskSummary> tasks = taskService.getTasksOwnedByStatus(entrada.getUsuario(), estadosActivos, "en-UK");
        long taskId = -1;
        for (TaskSummary task : tasks) {

                RespuestaTareaDTO respuestaTarea = RespuestaTareaDTO.newInstance()
                        .idTarea(task.getId())
                        .estado(task.getStatusId())
                        .idProceso(task.getProcessId())
                        .idDespliegue(task.getDeploymentId())
                        .nombre(task.getName())
                        .prioridad(task.getPriority())
                        .idInstanciaProceso(task.getProcessInstanceId())
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
}
