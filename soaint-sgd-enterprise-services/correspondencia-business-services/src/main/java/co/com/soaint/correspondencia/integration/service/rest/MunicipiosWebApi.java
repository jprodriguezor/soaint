package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarMunicipio;
import co.com.soaint.foundation.canonical.correspondencia.MunicipioDTO;
import co.com.soaint.foundation.canonical.correspondencia.MunicipiosDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import java.util.List;

/**
 * Created by esanchez on 5/24/2017.
 */
@Path("/municipios-web-api")
@Produces({"application/json", "application/xml"})
@Log4j2
@Api(value = "MunicipiosWebApi", description = "")
public class MunicipiosWebApi {

    @Autowired
    private GestionarMunicipio boundary;

    /**
     * Constructor
     */
    public MunicipiosWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     * @param codDepar
     * @param estado
     * @return
     * @throws SystemException
     */
    @GET
    @Path("/municipios/{codDepar}/{estado}")
    public MunicipiosDTO listarMunicipiosByCodDeparAndEstado(@PathParam("codDepar") final String codDepar, @PathParam("estado") final String estado) throws SystemException {
        log.info("processing rest request - listar municipios por codigo del departamento y estado");
        return MunicipiosDTO.newInstance().municipios(boundary.listarMunicipiosByCodDeparAndEstado(codDepar, estado)).build();
    }

    /**
     * @param estado
     * @return
     * @throws SystemException
     */
    @GET
    @Path("/municipios/{estado}")
    public MunicipiosDTO listarMunicipiosByEstado(@PathParam("estado") final String estado) throws SystemException {
        log.info("processing rest request - listar municipios por estado");
        return MunicipiosDTO.newInstance().municipios(boundary.listarMunicipiosByEstado(estado)).build();
    }

    /**
     *
     * @param codigos
     * @return
     * @throws SystemException
     */
    @GET
    @Path("/municipios")
    public List<MunicipioDTO> listarMunicipiosByCodidos(@QueryParam("codigos")final String codigos) throws SystemException {
        log.info("processing rest request - listar municipios por codigos");
        return boundary.listarMunicipiosByCodidos(codigos.split(","));
    }
}
