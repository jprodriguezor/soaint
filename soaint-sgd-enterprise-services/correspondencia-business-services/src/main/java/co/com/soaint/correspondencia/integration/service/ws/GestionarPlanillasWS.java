package co.com.soaint.correspondencia.integration.service.ws;

import co.com.soaint.correspondencia.business.boundary.GestionarPlanillas;
import co.com.soaint.foundation.canonical.correspondencia.PlanillaDTO;
import co.com.soaint.foundation.canonical.correspondencia.ReportDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.jws.WebMethod;
import javax.jws.WebParam;
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

    /**
     *
     * @param planilla
     * @throws BusinessException
     * @throws SystemException
     */
    @WebMethod(action = "cargarPlanilla", operationName = "cargarPlanilla")
    public void cargarPlanilla(@WebParam(name = "planilla")final PlanillaDTO planilla) throws SystemException {
        boundary.cargarPlanilla(planilla);
    }

    /**
     *
     * @param nroPlanilla
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @WebMethod(action = "listarPlanillasByNroPlanilla", operationName = "listarPlanillasByNroPlanilla")
    public PlanillaDTO listarPlanillasByNroPlanilla(@WebParam(name = "nro_planilla")final String nroPlanilla) throws BusinessException, SystemException {
        return boundary.listarPlanillasByNroPlanilla(nroPlanilla);
    }

    /**
     *
     * @param nroPlanilla
     * @param formato
     * @return
     * @throws SystemException
     */
    @WebMethod(action = "exportarPlanilla", operationName = "exportarPlanilla")
    public ReportDTO exportarPlanilla(@WebParam(name = "nro_planilla")final String nroPlanilla,
                                      @WebParam(name = "formato")final String formato) throws SystemException {
        return boundary.exportarPlanilla(nroPlanilla, formato);
    }
}
