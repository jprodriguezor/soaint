package co.com.soaint.bpm.services.integration.services.impl;

import co.com.soaint.bpm.services.integration.services.IProcessServices;
import co.com.soaint.bpm.services.util.EngineConexion;
import co.com.soaint.bpm.services.util.Estados;
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
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.hornetq.utils.json.JSONArray;
import org.hornetq.utils.json.JSONException;
import org.hornetq.utils.json.JSONObject;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.net.URI;
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

    private KieSession ksession;
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
    HttpResponse response;
    @PersistenceContext
    private EntityManager em;
    @Autowired
    TasksService tareaOperaciones;
    EngineConexion engine = EngineConexion.getInstance();
    Estados estadosOperaciones = new Estados();


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
                    .addParameters(estadosOperaciones.listaEstadosProceso(entrada))
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
     * Permite abortar un proceso
     *
     * @param entrada Objeto que contiene los parametros de entrada para un proceso
     * @return Los datos del proceso que fue abortdado codigoProceso,nombreProceso,estado y idDespliegue
     * @throws SystemException
     */
    @Override
    public RespuestaProcesoDTO abortarProceso(EntradaProcesoDTO entrada) throws SystemException {
        try {
            log.info("abortar - proceso : {}", entrada);
            entrada.setUsuario(usuarioAdmin);
            entrada.setPass(passAdmin);
            ksession = engine.obtenerEngine(entrada).getKieSession();
            ksession.abortProcessInstance(entrada.getInstanciaProceso());
            return RespuestaProcesoDTO.newInstance()
                    .codigoProceso(String.valueOf(entrada.getInstanciaProceso()))
                    .nombreProceso(entrada.getIdProceso())
                    .estado(String.valueOf(EstadosEnum.SALIDO))
                    .idDespliegue(entrada.getIdDespliegue())
                    .build();

        } catch (Exception e) {
            log.error(errorSistema);
            throw ExceptionBuilder.newBuilder()
                    .withMessage(errorSistemaGenerico)
                    .withRootException(e)
                    .buildSystemException();
        } finally {
            log.info("fin - abortar - proceso ");
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
            ksession = engine.obtenerEngine(entrada).getKieSession();
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
     *
     * @param entrada
     * @return Los datos del proceso que fue iniciado codigoProceso,nombreProceso,estado y idDespliegue
     * @throws SystemException
     */
    @Override
    public RespuestaProcesoDTO iniciarProcesoPorTercero(EntradaProcesoDTO entrada) throws SystemException {
        try {

            entrada.setUsuario(usuarioAdmin);
            entrada.setPass(passAdmin);
            RespuestaProcesoDTO processInstance = iniciarProceso(entrada);
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
            ksession = engine.obtenerEngine(entradaManual).getKieSession();
            ProcessInstance processInstance = ksession.startProcess(entrada.getIdProceso(), entrada.getParametros());
            entrada.setInstanciaProceso(processInstance.getId());
            List<RespuestaTareaDTO> tareas = tareaOperaciones.listarTareasPorInstanciaProceso(entrada);
            for (RespuestaTareaDTO tarea : tareas) {
                entrada.setIdTarea(tarea.getIdTarea());
                tareaOperaciones.reservarTarea(entrada);

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
            ksession = engine.obtenerEngine(entradaManual).getKieSession();
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
            ksession = engine.obtenerEngine(entradaManual).getKieSession();
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

