package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarOrganigramaAdministrativo;
import co.com.soaint.foundation.canonical.correspondencia.OrganigramaAdministrativoDTO;
import co.com.soaint.foundation.canonical.correspondencia.OrganigramaItemDTO;
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
import java.math.BigInteger;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 24-May-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: SERVICE - rest services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Path("/organigrama-web-api")
@Produces({"application/json", "application/xml"})
public class OrganigramaAdministrativoWebApi {
    private static Logger LOGGER = LogManager.getLogger(OrganigramaAdministrativoWebApi.class.getName());

    @Autowired
    private GestionarOrganigramaAdministrativo boundary;

    public OrganigramaAdministrativoWebApi(){
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("/organigrama")
    public OrganigramaAdministrativoDTO consultarOrganigrama() throws BusinessException, SystemException{
        LOGGER.info("processing rest request - consultar organigrama administrativo");
        return OrganigramaAdministrativoDTO.newInstance().organigrama(boundary.consultarOrganigrama()).build();
    }

    @GET
    @Path("/organigrama/sedes")
    public OrganigramaAdministrativoDTO listarDescendientesDirectosDeElementoRayz() throws BusinessException, SystemException{
        LOGGER.info("processing rest request - listar descendientes directos del elemento raiz");
        return OrganigramaAdministrativoDTO.newInstance().organigrama(boundary.listarDescendientesDirectosDeElementoRayz()).build();
    }

    @GET
    @Path("/organigrama/dependencias/{ide_orga_admin_padre}")
    public OrganigramaAdministrativoDTO listarElementosDeNivelInferior(@PathParam("ide_orga_admin_padre") final String idPadre) throws BusinessException, SystemException{
        LOGGER.info("processing rest request - listar descendientes directos de un elemento");
        return OrganigramaAdministrativoDTO.newInstance().organigrama(boundary.listarElementosDeNivelInferior(BigInteger.valueOf(Long.parseLong(idPadre)))).build();
    }

    @GET
    @Path("/organigrama/sede/dependencia/{ide_orga_admin}")
    public OrganigramaItemDTO consultarPadreDeSegundoNivel(@PathParam("ide_orga_admin")final String idDependencia) throws BusinessException, SystemException{
        LOGGER.info("processing rest request - listar padre de segundo nivel");
        return boundary.consultarPadreDeSegundoNivel(BigInteger.valueOf(Long.parseLong(idDependencia)));
    }
}
