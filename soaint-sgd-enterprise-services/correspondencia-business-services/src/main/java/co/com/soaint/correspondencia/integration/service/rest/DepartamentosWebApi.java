package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarDepartamento;
import co.com.soaint.foundation.canonical.correspondencia.DepartamentosDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
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
@Produces({"application/json", "application/xml"})
@Log4j2
@Api(value = "DepartamentosWebApi")
public class DepartamentosWebApi {

    @Autowired
    private GestionarDepartamento boundary;

    /**
     * Constructor
     */
    public DepartamentosWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     * @param codPais
     * @param estado
     * @return
     * @throws SystemException
     */
    @GET
    @Path("/departamentos/{codPais}/{estado}")
    public DepartamentosDTO listarDepartamentosByCodPaisAndEstado(@PathParam("codPais") final String codPais, @PathParam("estado") final String estado) throws SystemException {
        log.info("processing rest request - listar departamentos por codigo del pais y estado");
        return DepartamentosDTO.newInstance().departamentos(boundary.listarDepartamentosByCodPaisAndEstado(codPais, estado)).build();
    }

    /**
     * @param estado
     * @return
     * @throws SystemException
     */
    @GET
    @Path("/departamentos/{estado}")
    public DepartamentosDTO listarDepartamentosByEstado(@PathParam("estado") final String estado) throws SystemException {
        log.info("processing rest request - listar departamentos por estado");
        return DepartamentosDTO.newInstance().departamentos(boundary.listarDepartamentosByEstado(estado)).build();
    }
}
