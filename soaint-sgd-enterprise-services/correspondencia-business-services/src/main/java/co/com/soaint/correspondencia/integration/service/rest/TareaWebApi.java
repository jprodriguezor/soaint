package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarTarea;
import co.com.soaint.foundation.canonical.correspondencia.TareaDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 06-Sep-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: SERVICE - rest services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@Path("/tarea-web-api")
@Produces({"application/json", "application/xml"})
@Consumes({"application/json", "application/xml"})
@Log4j2
@Api(value = "TareaWebApi")
public class TareaWebApi {

    @Autowired
    GestionarTarea boundary;

    /**
     * Constructor
     */
    public TareaWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     *
     * @param tarea
     * @throws SystemException
     */
    @POST
    @Path("/tarea")
    public void guardarEstadoTarea(TareaDTO tarea)throws SystemException{
        log.info("processing rest request - guardar estado de una tarea");
        boundary.guardarEstadoTarea(tarea);
    }

    /**
     *
     * @param idInstanciaProceso
     * @param idTareaProceso
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @GET
    @Path("/tarea/{id-instancia-proceso}/{id-tarea-proceso}")
    public TareaDTO listarEstadoTarea(@PathParam("id-instancia-proceso")final String idInstanciaProceso,
                                      @PathParam("id-tarea-proceso")final String idTareaProceso)throws SystemException{
        log.info("processing rest request - listar estado de una tarea");
        return boundary.listarEstadoTarea(idInstanciaProceso, idTareaProceso);
    }
}
