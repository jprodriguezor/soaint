package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarAgente;
import co.com.soaint.foundation.canonical.correspondencia.AgenteDTO;
import co.com.soaint.foundation.canonical.correspondencia.DevolucionDTO;
import co.com.soaint.foundation.canonical.correspondencia.RedireccionDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import java.math.BigInteger;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 11-Jul-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: SERVICE - rest services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@Path("/agente-web-api")
@Produces({"application/json", "application/xml"})
@Consumes({"application/json", "application/xml"})
@Log4j2
@Api(value = "AgenteWebApi", description = "")
public class AgenteWebApi {

    @Autowired
    GestionarAgente boundary;

    /**
     * Constructor
     */
    public AgenteWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     * @param agenteDTO
     * @throws BusinessException
     * @throws SystemException
     */
    @PUT
    @Path("/agente/actualizar-estado")
    public void actualizarEstadoAgente(AgenteDTO agenteDTO) throws BusinessException, SystemException {
        log.info("processing rest request - actualizar estado agente");
        boundary.actualizarEstadoAgente(agenteDTO);
    }

    /**
     * @param redireccion
     * @throws SystemException
     */
    @PUT
    @Path("/agente/redireccionar")
    public void redireccionarCorrespondencia(RedireccionDTO redireccion) throws SystemException {
        log.info("processing rest request - redireccionar correspondencia");
        boundary.redireccionarCorrespondencia(redireccion);
    }

    /**
     *
     * @param devolucion
     * @throws SystemException
     */
    @PUT
    @Path("/agente/devolver")
    public void devolverCorrespondencia(DevolucionDTO devolucion) throws SystemException {
        log.info("processing rest request - devolver correspondencia");
        boundary.devolverCorrespondencia(devolucion);
    }

    /**
     * @param agenteDTO
     * @throws BusinessException
     * @throws SystemException
     */
    @PUT
    @Path("/agente/actualizar-remitente")
    public String actualizarRemitente(AgenteDTO agenteDTO) throws BusinessException, SystemException {
        log.info("processing rest request - actualizar remitente");
        //boundary.actualizarEstadoAgente(agenteDTO);
        //TODO Implementacion
        return "1";
    }

    /**
     * @param agenteDTO
     * @throws BusinessException
     * @throws SystemException
     */
    @PUT
    @Path("/agente/actualizar-destinatario")
    public String actualizarDestinatario(AgenteDTO agenteDTO) throws BusinessException, SystemException {
        log.info("processing rest request - actualizar destinatario");
        //boundary.actualizarEstadoAgente(agenteDTO);
        //TODO Implementacion
        return "1";
    }
}
