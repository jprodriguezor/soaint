package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarPais;
import co.com.soaint.foundation.canonical.correspondencia.PaisDTO;
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
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by jrodriguez on 24/05/2017.
 */
public class GestionarPaisWebApi {

    private static Logger LOGGER = LogManager.getLogger(GestionarPaisWebApi.class.getName());

    @Autowired
    private GestionarPais boundry;

    public GestionarPaisWebApi(){
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("/paises/{estado}")
    @Produces({"application/json"})
    public PaisesDTO listarPaisesByEstado(@PathParam("estado") final String estado)throws SystemException, BusinessException {
        LOGGER.info("processing rest request - listar paises por estado");
        return PaisesDTO.newInstance().paises(boundry.listarPaisesByEstado(estado)).build();
    }
}
