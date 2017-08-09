package co.com.soaint.bpm.services.rest;

import co.com.soaint.bpm.services.integration.services.IProcessServices;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.bpm.RespuestaProcesoDTO;
import co.com.soaint.foundation.canonical.bpm.RespuestaTareaDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hornetq.utils.json.JSONException;
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
public class BpmIntegrationServicesClientRest {

    private static Logger logger = LogManager.getLogger(BpmIntegrationServicesClientRest.class.getName());

    @Autowired
    private IProcessServices proceso;

    public BpmIntegrationServicesClientRest(){
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @POST
    @Path("/proceso/listar/")
    public List<RespuestaProcesoDTO> listarProcesos(EntradaProcesoDTO entradaProceso) throws SystemException, BusinessException {
        logger.info("processing rest request - listar procesos");
        return proceso.listarProcesos(entradaProceso);
    }

    @POST
    @Path("/proceso/sennal/")
    public RespuestaProcesoDTO enviarSennalProceso(EntradaProcesoDTO entradaProceso) throws SystemException, BusinessException, IOException, JSONException {
        logger.info("processing rest request - enviar señal proceso");
        return proceso.enviarSenalProceso(entradaProceso);
    }

    @POST
    @Path("/proceso/sennal/inicio/")
    public RespuestaProcesoDTO senalInicioAutomatico(EntradaProcesoDTO entradaProceso) throws SystemException, BusinessException, IOException, JSONException, Throwable {
        logger.info("processing rest request - enviar señal proceso");
        return proceso.senalInicioAutomatico(entradaProceso);
    }

    @POST
    @Path("/proceso/listar-instancias/")
    public List<RespuestaProcesoDTO> listarProcesosInstanciaPorUsuarios(EntradaProcesoDTO entradaProceso) throws SystemException, BusinessException, IOException, JSONException, URISyntaxException, Throwable {
        logger.info("processing rest request - listar procesos");
        return proceso.listarProcesosInstanciaPorUsuarios(entradaProceso);
    }

    @POST
    @Path("/proceso/iniciar/")
    public RespuestaProcesoDTO iniciarProceso(EntradaProcesoDTO entradaProceso) throws SystemException, BusinessException, MalformedURLException, Throwable {
        logger.info("processing rest request - iniciar proceso");
        return proceso.iniciarProceso(entradaProceso);
    }

    @POST
    @Path("/proceso/iniciar/manual")
    public RespuestaProcesoDTO iniciarProcesoManual(EntradaProcesoDTO entradaProceso) throws SystemException, BusinessException, IOException, JSONException, URISyntaxException, Throwable {
        logger.info("processing rest request - iniciar proceso");
        return proceso.iniciarProcesoManual(entradaProceso);
    }

    @POST
    @Path("/tareas/completar/")
    public RespuestaTareaDTO completarTarea(EntradaProcesoDTO entradaTarea) throws SystemException, BusinessException, MalformedURLException, Throwable {
        logger.info("processing rest request - completar tarea");
        return proceso.completarTarea(entradaTarea);
    }

    @POST
    @Path("/tareas/iniciar/")
    public RespuestaTareaDTO iniciarTarea(EntradaProcesoDTO entradaTarea) throws SystemException, BusinessException, MalformedURLException, Throwable {
        logger.info("processing rest request - completar tarea");
        return proceso.iniciarTarea(entradaTarea);
    }

    @POST
    @Path("/tareas/reservar/")
    public RespuestaTareaDTO reservarTarea(EntradaProcesoDTO entradaTarea) throws SystemException, BusinessException, IOException, JSONException, URISyntaxException, Throwable {
        logger.info("processing rest request - completar tarea");
        return proceso.reservarTarea(entradaTarea);
    }

    @POST
    @Path("/tareas/reasignar/")
    public RespuestaTareaDTO reasignarTarea(EntradaProcesoDTO entradaTarea) throws SystemException, BusinessException, IOException, JSONException, URISyntaxException, Throwable {
        logger.info("processing rest request - reasignar tarea");
        return proceso.reasignarTarea(entradaTarea);
    }

    @POST
    @Path("/tareas/listar/estados")
    public List<RespuestaTareaDTO> listarTareasEstados(EntradaProcesoDTO entradaTarea) throws SystemException, BusinessException, MalformedURLException, Throwable {
        logger.info("processing rest request - listar tareas con sus estados");
        return proceso.listarTareasEstados(entradaTarea);
    }

    @POST
    @Path("/tareas/listar/estados-usuario/")
    public List<RespuestaTareaDTO> listarTareaPorUsuario(EntradaProcesoDTO entradaTarea) throws SystemException, BusinessException, MalformedURLException, Throwable {
        logger.info("processing rest request - listar tareas con sus estados por usuario");
        return proceso.listarTareasEstadosPorUsuario(entradaTarea);
    }

    @POST
    @Path("/tareas/listar/estados-instancia/")
    public List<RespuestaTareaDTO> listarTareasEstadosInstanciaProceso(EntradaProcesoDTO entradaTarea) throws SystemException, BusinessException, MalformedURLException, Throwable {
        logger.info("processing rest request - listar tareas con sus estados");
        return proceso.listarTareasEstadosInstanciaProceso(entradaTarea);
    }
    @POST
    @Path("/tareas/listar/estados-instancia/")
    public List<RespuestaTareaDTO> listarTareasPorInstanciaProceso(EntradaProcesoDTO entradaTarea) throws SystemException, BusinessException, MalformedURLException, Throwable {
        logger.info("processing rest request - listar tareas con sus estados");
        return proceso.listarTareasPorInstanciaProceso(entradaTarea);
    }

}
