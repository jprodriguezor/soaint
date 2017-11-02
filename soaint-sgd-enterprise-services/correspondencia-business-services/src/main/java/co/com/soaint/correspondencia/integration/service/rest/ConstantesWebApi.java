package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarConstantes;
import co.com.soaint.foundation.canonical.correspondencia.ConstantesDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;

/**
 * Created by esanchez on 5/24/2017.
 */
@Path("/constantes-web-api")
@Produces({"application/json;charset=UTF-8", "application/xml"})
@Log4j2
@Api(value = "ConstantesWebApi", description = "")
public class ConstantesWebApi {

    @Autowired
    private GestionarConstantes boundary;

    /**
     * Constructor
     */
    public ConstantesWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     * @param estado
     * @return
     * @throws SystemException
     */
    @GET
    @Path("constantes/{estado}")
    public ConstantesDTO listarConstantesByEstado(@PathParam("estado") final String estado) throws SystemException {
        log.info("processing rest request - listar constantes por estado");
        return ConstantesDTO.newInstance().constantes(boundary.listarConstantesByEstado(estado)).build();
    }

    /**
     * @param codigo
     * @param estado
     * @return
     * @throws SystemException
     */
    @GET
    @Path("constantes/{codigo}/{estado}")
    public ConstantesDTO listarConstantesByCodigoAndEstado(@PathParam("codigo") final String codigo, @PathParam("estado") final String estado) throws SystemException {
        log.info("processing rest request - listar constantes por codigo y estado");
        return ConstantesDTO.newInstance().constantes(boundary.listarConstantesByCodigoAndEstado(codigo, estado)).build();
    }

    /**
     * @param codPadre
     * @param estado
     * @return
     * @throws SystemException
     */
    @GET
    @Path("constantes/hijos/{cod-padre}/{estado}")
    public ConstantesDTO listarConstantesByCodPadreAndEstado(@PathParam("cod-padre") final String codPadre, @PathParam("estado") final String estado) throws SystemException {
        log.info("processing rest request - listar constantes por codigo del padre y estado");
        return ConstantesDTO.newInstance().constantes(boundary.listarConstantesByCodPadreAndEstado(codPadre, estado)).build();
    }

    /**
     *
     * @param codigos
     * @return
     * @throws SystemException
     */
    @GET
    @Path("constantes")
    public ConstantesDTO listarConstantesByCodigo(@QueryParam("codigos")String codigos) throws SystemException {
        log.info("processing rest request - listar constantes por codigo");
        return boundary.listarConstantesByCodigo(codigos.split(","));
    }
}
