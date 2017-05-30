package co.com.soaint.correspondencia.integration.service.ws;

import co.com.soaint.correspondencia.business.boundary.GestionarConstantes;
import co.com.soaint.correspondencia.domain.entity.TvsConstantes;
import co.com.soaint.foundation.canonical.correspondencia.ConstantesDTO;
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

    @WebMethod(action = "listarConstantesByEstado", operationName = "listarConstantesByEstado")
    public ConstantesDTO listarConstantes(@WebParam(name = "estado") final String estado) throws BusinessException, SystemException{
        return ConstantesDTO.newInstance().constantes(boundary.listarConstantesByEstado(estado)).build();
    }

    @WebMethod(action = "listarConstantesByCodigoAndEstado", operationName = "listarConstantesByCodigoAndEstado")
    public ConstantesDTO listarConstantesByCodigoAndEstado(@WebParam(name = "codigo") String codigo, @WebParam(name = "estado") String estado) throws BusinessException, SystemException{
        return ConstantesDTO.newInstance().constantes(boundary.listarConstantesByCodigoAndEstado(codigo, estado)).build();
    }

    @WebMethod(action = "listarConstantesByCodPadreAndEstado", operationName = "listarConstantesByCodPadreAndEstado")
    public ConstantesDTO listarConstantesByCodPadreAndEstado(@WebParam(name = "codPadre") String codPadre, @WebParam(name = "estado") String estado) throws BusinessException, SystemException{
        return ConstantesDTO.newInstance().constantes(boundary.listarConstantesByCodPadreAndEstado(codPadre, estado)).build();
    }
}
