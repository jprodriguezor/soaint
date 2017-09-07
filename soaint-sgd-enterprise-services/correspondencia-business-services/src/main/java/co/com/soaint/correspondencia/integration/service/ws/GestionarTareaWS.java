package co.com.soaint.correspondencia.integration.service.ws;

import co.com.soaint.correspondencia.business.boundary.GestionarTarea;
import co.com.soaint.foundation.canonical.correspondencia.TareaDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by esanchez on 9/6/2017.
 */
@WebService(targetNamespace = "http://co.com.soaint.sgd.correspondencia.service")
public class GestionarTareaWS {

    @Autowired
    GestionarTarea boundary;

    /**
     * Constructor
     */
    public GestionarTareaWS() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     *
     * @param tarea
     * @throws SystemException
     */
    @WebMethod(action = "guardarEstadoTarea", operationName = "guardarEstadoTarea")
    public void guardarEstadoTarea(@WebParam(name = "tarea") final TareaDTO tarea)throws SystemException {
        boundary.guardarEstadoTarea(tarea);
    }

    /**
     *
     * @param idInstanciaProceso
     * @param idTareaProceso
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @WebMethod(action = "listarEstadoTarea", operationName = "listarEstadoTarea")
    public TareaDTO listarEstadoTarea(@WebParam(name = "id-instancia-proceso")final String idInstanciaProceso,
                                      @WebParam(name = "id-tarea-proceso")final String idTareaProceso)throws BusinessException, SystemException{
        return boundary.listarEstadoTarea(idInstanciaProceso, idTareaProceso);
    }

}
