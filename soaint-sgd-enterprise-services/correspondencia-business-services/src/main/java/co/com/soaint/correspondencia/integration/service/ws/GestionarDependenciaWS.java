package co.com.soaint.correspondencia.integration.service.ws;

import co.com.soaint.correspondencia.business.boundary.GestionarDependencia;
import co.com.soaint.foundation.canonical.correspondencia.DependenciaDTO;
import co.com.soaint.foundation.canonical.correspondencia.DependenciasDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by esanchez on 8/23/2017.
 */
@WebService(targetNamespace = "http://co.com.soaint.sgd.correspondencia.service")
public class GestionarDependenciaWS {

    @Autowired
    GestionarDependencia boundary;

    /**
     * Constructor
     */
    public GestionarDependenciaWS() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     *
     * @param codOrg
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @WebMethod(action = "listarDependenciaByCodigo", operationName = "listarDependenciaByCodigo")
    public DependenciaDTO listarDependenciaByCodigo(@WebParam(name = "cod-org")final String codOrg)throws BusinessException, SystemException{
        return boundary.listarDependenciaByCodigo(codOrg);
    }

    /**
     *
     * @param codigosOrg
     * @return
     * @throws SystemException
     */
    @WebMethod(action = "listarDependenciasByCodigo", operationName = "listarDependenciasByCodigo")
    public DependenciasDTO listarDependenciasByCodigo(@WebParam(name = "cod-org")final String[] codigosOrg)throws SystemException{
        return boundary.listarDependenciasByCodigo(codigosOrg);
    }

    /**
     *
     * @return
     * @throws SystemException
     */
    @WebMethod(action = "listarDependencias", operationName = "listarDependencias")
    public DependenciasDTO listarDependencias() throws SystemException {
        return boundary.listarDependencias();
    }
}
