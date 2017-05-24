package co.com.soaint.correspondencia.integration.service.ws;

import co.com.soaint.correspondencia.business.boundary.GestionarPais;
import co.com.soaint.correspondencia.domain.entity.TvsPais;
import co.com.soaint.foundation.canonical.correspondencia.PaisDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

/**
 * Created by jrodriguez on 12/05/2017.
 */
@WebService(targetNamespace = "http://co.com.soaint.sgd.correspondencia.service")
public class GestionarPaisWS {

    @Autowired
    public GestionarPais boundary;


    public GestionarPaisWS() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @WebMethod(action = "listarPaisesByEstado", operationName = "listarPaisesByEstado")
    public List<TvsPais> listarPaisesByEstado() throws BusinessException, SystemException {
        return boundary.listarPaisesByEstado();
    }

}
