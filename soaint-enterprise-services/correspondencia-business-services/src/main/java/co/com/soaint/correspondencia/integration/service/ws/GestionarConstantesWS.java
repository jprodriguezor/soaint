package co.com.soaint.correspondencia.integration.service.ws;

import co.com.soaint.correspondencia.business.boundary.GestionarConstantes;
import co.com.soaint.correspondencia.domain.entity.TvsConstantes;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

/**
 * Created by esanchez on 5/24/2017.
 */
@WebService(targetNamespace = "http://co.com.soaint.sgd.correspondencia.service")
public class GestionarConstantesWS {
    @Autowired
    private GestionarConstantes boundary;

    public GestionarConstantesWS() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @WebMethod(action = "listarConstantes", operationName = "listarConstantes")
    public List<TvsConstantes> listarConstantes() throws BusinessException, SystemException{
        return boundary.listarConstantesByEstado();
    }

    @WebMethod(action = "listarConstantesPorCodigo", operationName = "listarConstantesPorCodigo")
    public List<TvsConstantes> listarConstantesByCodigoAndEstado(@WebParam(name = "codigo") String codigo) throws BusinessException, SystemException{
        return boundary.listarConstantesByCodigoAndEstado(codigo);
    }
}
