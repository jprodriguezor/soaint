package co.com.soaint.bpm.services.integration.services.impl;

import co.com.soaint.bpm.services.integration.services.IProcessServices;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.bpm.RespuestaProcesoDTO;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.audit.AuditService;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.services.client.api.RemoteRuntimeEngineFactory;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Arce on 6/7/2017.
 */
@Service
public class ProcessService implements IProcessServices {

    private RuntimeEngine engine;
    private KieSession ksession;
    private TaskService taskService;
    private AuditService auditService;


    private ProcessService() throws MalformedURLException {

    }

    @Override
    public RespuestaProcesoDTO inicarProceso(EntradaProcesoDTO entrada){

        RespuestaProcesoDTO respuesta = new RespuestaProcesoDTO();

        URL BPMS_HOST = null;
        try {
            BPMS_HOST = new URL("http://192.168.3.242:28080/jbpm-console");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        RuntimeEngine engine = RemoteRuntimeEngineFactory.newRestBuilder()
                .addUrl(BPMS_HOST)
                .addTimeout(5)
                .addDeploymentId(entrada.getIdDespliegue())
                .addUserName(entrada.getUsuario())
                .addPassword(entrada.getPass())
                        // if you're sending custom class parameters, make sure that
                        // the remote client instance knows about them!
                        //.addExtraJaxbClasses(ProcessRequestContext.class)
                .build();
        taskService = engine.getTaskService();
        ksession = engine.getKieSession();
        auditService = engine.getAuditService();

        ProcessInstance processInstance = ksession.startProcess(entrada.getIdProceso(), entrada.getParametros());
        long procId = processInstance.getId();

        respuesta.setCodigoProceso(String.valueOf(processInstance.getId()));
        respuesta.setEstado(String.valueOf(processInstance.getState()));
        respuesta.setNombreProceso(processInstance.getProcessId());

        return respuesta;

    }
//
//    private RuntimeEngine obtenerEngine() throws MalformedURLException {
//        engine = RemoteRuntimeEngineFactory.newRestBuilder()
//                .addDeploymentId(DEPLOYMENT_ID).addUserName(USERNAME)
//                .addPassword(PASSWORD).addUrl(new URL(SERVER_URL)).build();
//        taskService = engine.getTaskService();
//        ksession = engine.getKieSession();
//        auditService = engine.getAuditService();
//    }getAuditService



}
