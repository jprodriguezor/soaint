package co.com.soaint.correspondencia.integration.service.ws;

import co.com.soaint.correspondencia.business.boundary.GestionarDepartamento;
import co.com.soaint.correspondencia.domain.entity.TvsDepartamento;
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
public class GestionarDepartamentoWS {
    @Autowired
    private GestionarDepartamento boundary;

    public GestionarDepartamentoWS() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @WebMethod(action = "listarDepartamentosPorPais", operationName = "listarDepartamentosPorPais")
    public List<TvsDepartamento> listarDepartamentosByCodPais(@WebParam(name = "codPais") String codPais)throws BusinessException, SystemException{
        return boundary.listarDepartamentosByCodPais(codPais);
    }
}
