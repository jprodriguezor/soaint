package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarPais;
import co.com.soaint.foundation.canonical.correspondencia.PaisesDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 24-May-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: SERVICE - rest services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Path("/paises-web-api")
@Produces({"application/json", "application/xml"})
public class PaisesWebApi {

    private static Logger logger = LogManager.getLogger(PaisesWebApi.class.getName());

    @Autowired
    private GestionarPais boundary;

    public PaisesWebApi(){
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("/paises/{estado}")
    public PaisesDTO listarPaisesByEstado(@PathParam("estado") final String estado)throws SystemException, BusinessException {
        logger.info("processing rest request - listar paises por estado");
        return PaisesDTO.newInstance().paises(boundary.listarPaisesByEstado(estado)).build();
    }

    @GET
    @Path("/paises/{nombre_pais}/{estado}")
    public PaisesDTO listarPaisesByNombrePaisAndEstado(@PathParam("nombre_pais") final String nombrePais, @PathParam("estado") final String estado)throws SystemException, BusinessException {
        logger.info("processing rest request - listar paises por nombre y estado");
        return PaisesDTO.newInstance().paises(boundary.listarPaisesByNombrePaisAndEstado(nombrePais, estado)).build();
    }
}
