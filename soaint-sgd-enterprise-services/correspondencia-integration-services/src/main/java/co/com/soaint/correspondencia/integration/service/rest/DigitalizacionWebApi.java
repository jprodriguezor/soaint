package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarDigitalizacion;
import co.com.soaint.foundation.canonical.integration.DigitalizacionDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 06-Mar-2018
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: SERVICE - rest services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@Path("/digitalizacion-web-api")
@Produces({"application/json", "application/xml"})
@Consumes({"application/json", "application/xml"})
@Log4j2
@Api(value = "DigitalizacionWebApi", description = "")
public class DigitalizacionWebApi {

    @Autowired
    GestionarDigitalizacion boundary;

    public DigitalizacionWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @POST
    @Path("/digitalizacion")
    public void digitalizarDocumento(DigitalizacionDTO digitalizacionDTO)throws SystemException {
        log.info("processing rest request - digitalizar documento");
        boundary.digitalizarDocumento(digitalizacionDTO);
    }
}
