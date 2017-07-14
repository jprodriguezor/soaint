package co.com.soaint.correspondencia.integration.service.ws;

import co.com.soaint.correspondencia.business.boundary.GestionarAgente;
import co.com.soaint.foundation.canonical.correspondencia.AgenteDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by esanchez on 7/14/2017.
 */
@WebService(targetNamespace = "http://co.com.soaint.sgd.correspondencia.service")
public class GestionarAgenteWS {

    @Autowired
    GestionarAgente boundary;

    public GestionarAgenteWS() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @WebMethod(action = "actualizarEstadoAgente", operationName = "actualizarEstadoAgente")
    public void actualizarEstadoAgente(@WebParam(name = "agente")AgenteDTO agenteDTO)throws BusinessException, SystemException {
        boundary.actualizarEstadoAgente(agenteDTO);
    }
}
