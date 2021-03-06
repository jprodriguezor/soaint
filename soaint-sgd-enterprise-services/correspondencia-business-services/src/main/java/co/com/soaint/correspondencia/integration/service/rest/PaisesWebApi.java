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
 * Created by jrodriguez on 24/05/2017.
 */

@Path("/paises-web-api")
public class PaisesWebApi {

    private static Logger LOGGER = LogManager.getLogger(PaisesWebApi.class.getName());

    @Autowired
    private GestionarPais boundary;

    public PaisesWebApi(){
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("/paises/{estado}")
    @Produces({"application/json", "application/xml"})
    public PaisesDTO listarPaisesByEstado(@PathParam("estado") final String estado)throws SystemException, BusinessException {
        LOGGER.info("processing rest request - listar paises por estado");
        return PaisesDTO.newInstance().paises(boundary.listarPaisesByEstado(estado)).build();
    }
}
