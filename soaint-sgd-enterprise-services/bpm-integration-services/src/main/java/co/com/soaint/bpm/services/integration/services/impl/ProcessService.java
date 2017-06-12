package co.com.soaint.bpm.services.integration.services.impl;

import co.com.soaint.bpm.services.integration.services.IProcessServices;
import co.com.soaint.foundation.canonical.bpm.*;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.audit.AuditService;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Status;
import org.kie.api.task.model.TaskSummary;
import org.kie.services.client.api.RemoteRuntimeEngineFactory;
import org.springframework.stereotype.Service;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by Arce on 6/7/2017.
 */
@Service
public class ProcessService implements IProcessServices {

    private RuntimeEngine engine;
    private KieSession ksession;
    private TaskService taskService;
    private AuditService auditService;
    protected RuntimeDataService dataService;

    private ProcessService() throws MalformedURLException {

    }

    public List<RespuestaProcesoDTO> listarProcesos(EntradaProcesoDTO entrada) throws MalformedURLException {
        ksession = obtenerEngine(entrada).getKieSession();
        Collection <ProcessInstance> processInstances = ksession.getProcessInstances();
        List<RespuestaProcesoDTO> listaProcesos = new ArrayList<>();
        for (ProcessInstance processInstance : processInstances) {

            RespuestaProcesoDTO respuesta = RespuestaProcesoDTO.newInstance()
                    .codigoProceso(String.valueOf(processInstance.getId()))
                    .nombreProceso(processInstance.getProcessId())
                    .estado(String.valueOf(processInstance.getState()))
                    .build();
            listaProcesos.add(respuesta);
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
                .build();
        return respuesta;
    }

    @Override
    public RespuestaTareaDTO completarTarea(EntradaProcesoDTO entrada) throws MalformedURLException {
        RespuestaTareaDTO tarea = new RespuestaTareaDTO();
        taskService = obtenerEngine(entrada).getTaskService();
        taskService.start(entrada.getIdTarea(), entrada.getUsuario());
        taskService.complete(entrada.getIdTarea(), entrada.getUsuario(), entrada.getParametros());
        List<TaskSummary> tasks = taskService.getTasksAssignedAsPotentialOwner(entrada.getUsuario(), "en-UK");

        long taskId = -1;
        for (TaskSummary task : tasks) {
            if (task.getProcessId().equals(entrada.getIdProceso())  ) {
                tarea.setIdTarea(task.getId());
                tarea.setEstado(task.getStatusId());
                tarea.setIdProceso(task.getProcessId());
                tarea.setIdDespliegue(task.getDeploymentId());
                tarea.setNombre(task.getName());
                tarea.setPrioridad(task.getPriority());
            }
        }
        return tarea;
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
        //List<TaskSummary> tasks = taskService.getTasksOwnedByStatus(entrada.getUsuario(), estadosActivos, "en-UK");
        List<TaskSummary> tasks = taskService.getTasksAssignedAsPotentialOwner(entrada.getUsuario(), "en-UK");
        long taskId = -1;
        for (TaskSummary task : tasks) {
            if (task.getProcessId().equals(entrada.getIdProceso())  ) {
                RespuestaTareaDTO respuestaTarea = RespuestaTareaDTO.newInstance()
                        .idTarea(task.getId())
                        .estado(task.getStatusId())
                        .idProceso(task.getProcessId())
                        .idDespliegue(task.getDeploymentId())
                        .nombre(task.getName())
                        .prioridad(task.getPriority())
                        .build();
                tareas.add(respuestaTarea);
            }
        }
        return tareas;
    }


    private RuntimeEngine obtenerEngine(EntradaProcesoDTO entrada) throws MalformedURLException {
        engine = RemoteRuntimeEngineFactory.newRestBuilder()
                .addDeploymentId(entrada.getIdDespliegue())
                .addUserName(entrada.getUsuario())
                .addPassword(entrada.getPass())
                .addUrl(new URL("http://192.168.3.242:28080/jbpm-console"))
                        //.addExtraJaxbClasses(ProcessRequestContext.class)
                .build();
        return engine;
    }
}
