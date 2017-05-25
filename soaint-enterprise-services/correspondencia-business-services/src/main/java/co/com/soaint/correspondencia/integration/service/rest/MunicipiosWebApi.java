package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarMunicipio;
import co.com.soaint.correspondencia.domain.entity.TvsMunicipio;
import co.com.soaint.foundation.canonical.correspondencia.MunicipiosDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * Created by esanchez on 5/24/2017.
 */
@Path("/municipios-web-api")
public class MunicipiosWebApi {

    @Autowired
    private GestionarMunicipio boundary;

    public MunicipiosWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("/municipios/{codDepar}/{estado}")
    @Produces({"application/json", "application/xml"})
    public MunicipiosDTO listarMunicipiosByCodDeparAndEstado(@PathParam("codDepar") final String codDepar, @PathParam("estado") final String estado)throws BusinessException, SystemException{
        return MunicipiosDTO.newInstance().municipios(boundary.listarMunicipiosByCodDeparAndEstado(codDepar, estado)).build();
    }

    @GET
    @Path("/municipios/{estado}")
    @Produces({"application/json", "application/xml"})
    public MunicipiosDTO listarMunicipiosByEstado(@PathParam("estado") final String estado)throws BusinessException, SystemException{
        return MunicipiosDTO.newInstance().municipios(boundary.listarMunicipiosByEstado(estado)).build();
    }
}
