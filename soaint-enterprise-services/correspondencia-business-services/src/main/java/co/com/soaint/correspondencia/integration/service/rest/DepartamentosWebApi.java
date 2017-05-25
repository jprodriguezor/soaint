package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarDepartamento;
import co.com.soaint.foundation.canonical.correspondencia.DepartamentosDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * Created by esanchez on 5/24/2017.
 */
@Path("/departamentos-web-api")
public class DepartamentosWebApi {

    @Autowired
    private GestionarDepartamento boundary;

    public DepartamentosWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("/departamentos/{codPais}/{estado}")
    @Produces({"application/json", "application/xml"})
    public DepartamentosDTO listarDepartamentosByCodPaisAndEstado(@PathParam("codPais") final String codPais, @PathParam("estado") final String estado) throws BusinessException, SystemException{
        return DepartamentosDTO.newInstance().departamentos(boundary.listarDepartamentosByCodPaisAndEstado(codPais, estado)).build();
    }

    @GET
    @Path("/departamentos/{estado}")
    @Produces({"application/json", "application/xml"})
    public DepartamentosDTO listarDepartamentosByEstado(@PathParam("estado") final String estado) throws BusinessException, SystemException{
        return DepartamentosDTO.newInstance().departamentos(boundary.listarDepartamentosByEstado(estado)).build();
    }
}
