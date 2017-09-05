package co.com.soaint.correspondencia.integration.service.ws;

import co.com.soaint.correspondencia.business.boundary.GestionarPlanillas;
import co.com.soaint.foundation.canonical.correspondencia.PlanillaDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created by esanchez on 9/5/2017.
 */
@WebService(targetNamespace = "http://co.com.soaint.sgd.correspondencia.service")
public class GestionarPlanillasWS {

    @Autowired
    GestionarPlanillas boundary;

    /**
     * Constructor
     */
    public GestionarPlanillasWS() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     *
     * @param planilla
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @WebMethod(action = "generarPlanilla", operationName = "generarPlanilla")
    public PlanillaDTO generarPlanilla(PlanillaDTO planilla) throws BusinessException, SystemException {
        return boundary.generarPlanilla(planilla);
    }
}
