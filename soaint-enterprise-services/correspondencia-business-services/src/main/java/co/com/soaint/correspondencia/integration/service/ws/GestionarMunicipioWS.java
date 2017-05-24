package co.com.soaint.correspondencia.integration.service.ws;

import co.com.soaint.correspondencia.business.boundary.GestionarMunicipio;
import co.com.soaint.correspondencia.domain.entity.TvsMunicipio;
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
public class GestionarMunicipioWS {

    @Autowired
    private GestionarMunicipio boundary;

    public GestionarMunicipioWS() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @WebMethod(action = "listarMunicipiosPorDepartamento", operationName = "listarMunicipiosPorDepartamento")
    public List<TvsMunicipio> listarMunicipiosByCodDepar(@WebParam(name = "codDepar") String codDepar)throws BusinessException, SystemException{
        return boundary.listarMunicipiosByCodDepar(codDepar);
    }
}
