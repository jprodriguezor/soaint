package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarProduccionDocumental;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.*;
import java.io.IOException;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 05-Apr-2018
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: SERVICE - rest services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@Path("/produccion-documental-web-api")
@Produces({"application/json", "application/xml"})
@Consumes({"application/json", "application/xml"})
@Log4j2
@Api(value = "ProduccionDocumentalWebApi", description = "")
public class ProduccionDocumentalWebApi {

    @Autowired
    private GestionarProduccionDocumental boundary;

    public ProduccionDocumentalWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @GET
    @Path("/generar-version-final/{id_instancia}")
    public String producirVersionFinal(@PathParam("id_instancia") final String idInstanciaProceso) throws SystemException, IOException {
        return boundary.producirVersionFinal(idInstanciaProceso);
    }
}
