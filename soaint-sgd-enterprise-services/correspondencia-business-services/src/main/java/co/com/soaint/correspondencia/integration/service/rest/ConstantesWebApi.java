package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarConstantes;
import co.com.soaint.foundation.canonical.correspondencia.ConstantesDTO;
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
 * Created by esanchez on 5/24/2017.
 */
@Path("/constantes-web-api")
@Produces({"application/json", "application/xml"})
public class ConstantesWebApi {

    private static Logger logger = LogManager.getLogger(ConstantesWebApi.class.getName());

    @Autowired
    private GestionarConstantes boundary;

    public ConstantesWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("constantes/{estado}")
    public ConstantesDTO listarConstantesByEstado(@PathParam("estado") final String estado) throws BusinessException, SystemException{
        logger.info("processing rest request - listar constantes por estado");
        return ConstantesDTO.newInstance().constantes(boundary.listarConstantesByEstado(estado)).build();
    }

    @GET
    @Path("constantes/{codigo}/{estado}")
    public ConstantesDTO listarConstantesByCodigoAndEstado(@PathParam("codigo") final String codigo, @PathParam("estado") final String estado) throws BusinessException, SystemException{
        logger.info("processing rest request - listar constantes por codigo y estado");
        return ConstantesDTO.newInstance().constantes(boundary.listarConstantesByCodigoAndEstado(codigo, estado)).build();
    }

    @GET
    @Path("constantes/hijos/{cod-padre}/{estado}")
    public ConstantesDTO listarConstantesByCodPadreAndEstado(@PathParam("cod-padre") final String codPadre, @PathParam("estado") final String estado) throws BusinessException, SystemException{
        logger.info("processing rest request - listar constantes por codigo del padre y estado");
        return ConstantesDTO.newInstance().constantes(boundary.listarConstantesByCodPadreAndEstado(codPadre, estado)).build();
    }
}
