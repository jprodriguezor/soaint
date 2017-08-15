package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarOrganigramaAdministrativo;
import co.com.soaint.foundation.canonical.correspondencia.OrganigramaAdministrativoDTO;
import co.com.soaint.foundation.canonical.correspondencia.OrganigramaItemDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.math.BigInteger;

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
@Log4j2
public class OrganigramaAdministrativoWebApi {

    @Autowired
    private GestionarOrganigramaAdministrativo boundary;

    /**
     * Constructor
     */
    public OrganigramaAdministrativoWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @GET
    @Path("/organigrama")
    public OrganigramaAdministrativoDTO consultarOrganigrama() throws BusinessException, SystemException {
        log.info("processing rest request - consultar organigrama administrativo");
        return OrganigramaAdministrativoDTO.newInstance().organigrama(boundary.consultarOrganigrama()).build();
    }

    /**
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @GET
    @Path("/organigrama/sedes")
    public OrganigramaAdministrativoDTO listarDescendientesDirectosDeElementoRayz() throws BusinessException, SystemException {
        log.info("processing rest request - listar descendientes directos del elemento raiz");
        return OrganigramaAdministrativoDTO.newInstance().organigrama(boundary.listarDescendientesDirectosDeElementoRayz()).build();
    }

    /**
     * @param idPadre
     * @return
     * @throws SystemException
     */
    @GET
    @Path("/organigrama/dependencias/{ide_orga_admin_padre}")
    public OrganigramaAdministrativoDTO listarElementosDeNivelInferior(@PathParam("ide_orga_admin_padre") final String idPadre) throws SystemException {
        log.info("processing rest request - listar descendientes directos de un elemento");
        return OrganigramaAdministrativoDTO.newInstance().organigrama(boundary.listarElementosDeNivelInferior(BigInteger.valueOf(Long.parseLong(idPadre)))).build();
    }

    /**
     * @param idDependencia
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @GET
    @Path("/organigrama/sede/dependencia/{ide_orga_admin}")
    public OrganigramaItemDTO consultarPadreDeSegundoNivel(@PathParam("ide_orga_admin") final String idDependencia) throws BusinessException, SystemException {
        log.info("processing rest request - listar padre de segundo nivel");
        return boundary.consultarPadreDeSegundoNivel(BigInteger.valueOf(Long.parseLong(idDependencia)));
    }
}
