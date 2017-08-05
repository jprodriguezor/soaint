package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarMunicipio;
import co.com.soaint.foundation.canonical.correspondencia.MunicipiosDTO;
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
@Path("/municipios-web-api")
@Produces({"application/json", "application/xml"})
public class MunicipiosWebApi {

    private static Logger logger = LogManager.getLogger(MunicipiosWebApi.class.getName());

    @Autowired
    private GestionarMunicipio boundary;

    public MunicipiosWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("/municipios/{codDepar}/{estado}")
    public MunicipiosDTO listarMunicipiosByCodDeparAndEstado(@PathParam("codDepar") final String codDepar, @PathParam("estado") final String estado)throws SystemException{
        logger.info("processing rest request - listar municipios por codigo del departamento y estado");
        return MunicipiosDTO.newInstance().municipios(boundary.listarMunicipiosByCodDeparAndEstado(codDepar, estado)).build();
    }

    @GET
    @Path("/municipios/{estado}")
    public MunicipiosDTO listarMunicipiosByEstado(@PathParam("estado") final String estado)throws SystemException{
        logger.info("processing rest request - listar municipios por estado");
        return MunicipiosDTO.newInstance().municipios(boundary.listarMunicipiosByEstado(estado)).build();
    }
}
