package co.com.soaint.correspondencia.integration.service.rest;

import co.com.soaint.correspondencia.business.boundary.GestionarPlanillas;
import co.com.soaint.foundation.canonical.correspondencia.PlanillaDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
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
 * Created: 05-Sep-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: SERVICE - rest services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@Path("/planillas-web-api")
@Produces({"application/json", "application/xml"})
@Consumes({"application/json", "application/xml"})
@Log4j2
public class PlanillasWebApi {

    @Autowired
    GestionarPlanillas boundary;

    /**
     * Constructor
     */
    public PlanillasWebApi() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @POST
    @Path("/planillas")
    public PlanillaDTO generarPlanilla(PlanillaDTO planilla) throws BusinessException, SystemException {
        log.info("processing rest request - generar planilla distribucion");
        return boundary.generarPlanilla(planilla);
    }
}
