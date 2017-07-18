package co.com.soaint.bpm.services.rest;

import co.com.soaint.bpm.services.integration.services.IProcessServices;
import co.com.soaint.foundation.canonical.bpm.*;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hornetq.utils.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import javax.ws.rs.*;
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

    private static Logger LOGGER = LogManager.getLogger(BpmIntegrationServicesClientRest.class.getName());

    @Autowired
    private IProcessServices proceso;

    public BpmIntegrationServicesClientRest(){
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @POST
    @Path("/proceso/listar/")
    public List<RespuestaProcesoDTO> listarProcesos(EntradaProcesoDTO entradaProceso) throws SystemException, BusinessException, IOException, JSONException {
        LOGGER.info("processing rest request - listar procesos");
        try {
            return proceso.listarProcesos(entradaProceso);
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }

    @POST
    @Path("/proceso/sennal/digitalizacion/")
    public RespuestaProcesoDTO enviarSennalProceso(EntradaProcesoDTO entradaProceso) throws SystemException, BusinessException, IOException, JSONException {
        LOGGER.info("processing rest request - enviar señal proceso");
        try {
            return proceso.senalEsperaDigitalizacion(entradaProceso);
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }

    @POST
    @Path("/proceso/sennal/inicio/")
    public RespuestaProcesoDTO senalInicioAutomatico(EntradaProcesoDTO entradaProceso) throws SystemException, BusinessException, IOException, JSONException {
        LOGGER.info("processing rest request - enviar señal proceso");
        try {
            return proceso.senalInicioAutomatico(entradaProceso);
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }

    @POST
    @Path("/proceso/listar-instancias/")
    public List<RespuestaProcesoDTO> listarProcesosInstanciaPorUsuarios(EntradaProcesoDTO entradaProceso) throws SystemException, BusinessException, IOException, JSONException, URISyntaxException {
        LOGGER.info("processing rest request - listar procesos");
        try {
            return proceso.listarProcesosInstanciaPorUsuarios(entradaProceso);
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }

    @POST
    @Path("/proceso/iniciar/")
    public RespuestaProcesoDTO iniciarProceso(EntradaProcesoDTO entradaProceso) throws SystemException, BusinessException, MalformedURLException {
        LOGGER.info("processing rest request - iniciar proceso");
        try {
            return proceso.iniciarProceso(entradaProceso);
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }

    @POST
    @Path("/proceso/iniciar/manual")
    public RespuestaProcesoDTO iniciarProcesoManual(EntradaProcesoDTO entradaProceso) throws SystemException, BusinessException, IOException, JSONException, URISyntaxException {
        LOGGER.info("processing rest request - iniciar proceso");
        try {
            return proceso.iniciarProcesoManual(entradaProceso);
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }

    @POST
    @Path("/tareas/completar/")
    public RespuestaTareaDTO completarTarea(EntradaProcesoDTO entradaTarea) throws SystemException, BusinessException, MalformedURLException {
        LOGGER.info("processing rest request - completar tarea");
        try {
            return proceso.completarTarea(entradaTarea);
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }

    @POST
    @Path("/tareas/iniciar/")
    public RespuestaTareaDTO iniciarTarea(EntradaProcesoDTO entradaTarea) throws SystemException, BusinessException, MalformedURLException {
        LOGGER.info("processing rest request - completar tarea");
        try {
            return proceso.iniciarTarea(entradaTarea);
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }

    @POST
    @Path("/tareas/reservar/")
    public RespuestaTareaDTO reservarTarea(EntradaProcesoDTO entradaTarea) throws SystemException, BusinessException, IOException, JSONException, URISyntaxException {
        LOGGER.info("processing rest request - completar tarea");
        try {
            return proceso.reservarTarea(entradaTarea);
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }

    @POST
    @Path("/tareas/listar/estados")
    public List<RespuestaTareaDTO> listarTareasEstados(EntradaProcesoDTO entradaTarea) throws SystemException, BusinessException, MalformedURLException {
        LOGGER.info("processing rest request - listar tareas con sus estados");
        try {
            return proceso.listarTareasEstados(entradaTarea);
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;

        }
    }

    @POST
    @Path("/tareas/listar/estados-usuario/")
    public List<RespuestaTareaDTO> listarTareaPorUsuario(EntradaProcesoDTO entradaTarea) throws SystemException, BusinessException, MalformedURLException {
        LOGGER.info("processing rest request - listar tareas con sus estados por usuario");
        try {
            return proceso.listarTareasEstadosPorUsuario(entradaTarea);
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;

        }
    }

    @POST
    @Path("/tareas/listar/estados-instancia/")
    public List<RespuestaTareaDTO> listarTareasEstadosInstanciaProceso(EntradaProcesoDTO entradaTarea) throws SystemException, BusinessException, MalformedURLException {
        LOGGER.info("processing rest request - listar tareas con sus estados");
        try {
            return proceso.listarTareasEstadosInstanciaProceso(entradaTarea);
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;

        }
    }
    @POST
    @Path("/tareas/listar/estados-instancia/")
    public List<RespuestaTareaDTO> listarTareasPorInstanciaProceso(EntradaProcesoDTO entradaTarea) throws SystemException, BusinessException, MalformedURLException {
        LOGGER.info("processing rest request - listar tareas con sus estados");
        try {
            return proceso.listarTareasPorInstanciaProceso(entradaTarea);
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;

        }
    }

}
