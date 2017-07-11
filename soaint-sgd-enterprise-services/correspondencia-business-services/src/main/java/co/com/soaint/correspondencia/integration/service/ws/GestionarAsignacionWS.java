package co.com.soaint.correspondencia.integration.service.ws;

import co.com.soaint.correspondencia.business.boundary.GestionarAsignacion;
import co.com.soaint.foundation.canonical.correspondencia.AsignacionDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by esanchez on 7/11/2017.
 */
@WebService(targetNamespace = "http://co.com.soaint.sgd.correspondencia.service")
public class GestionarAsignacionWS {

    @Autowired
    GestionarAsignacion boundary;

    public GestionarAsignacionWS() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @WebMethod(action = "asignarCorrespondencia", operationName = "asignarCorrespondencia")
    public void asignarCorrespondencia(@WebParam(name = "asignacion")final AsignacionDTO asignacionDTO)throws BusinessException, SystemException{
        boundary.asignarCorrespondencia(asignacionDTO);
    }

    @WebMethod(action = "actualizarIdInstancia", operationName = "actualizarIdInstancia")
    public void actualizarIdInstancia(@WebParam(name = "ide_asignacion")final Long ideAsignacion, @WebParam(name = "id_instancia")final String idInstancia)throws BusinessException, SystemException{
        boundary.actualizarIdInstancia(ideAsignacion, idInstancia);
    }
}
