package co.com.soaint.correspondencia.integration.service.ws;

import co.com.soaint.correspondencia.business.boundary.GestionarFuncionarios;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionariosDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by esanchez on 6/17/2017.
 */
@WebService(targetNamespace = "http://co.com.soaint.sgd.correspondencia.service")
public class GestionarFuncionariosWS {

    @Autowired
    private GestionarFuncionarios boundary;

    /**
     * Constructor
     */
    public GestionarFuncionariosWS() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     * @param loginName
     * @param estado
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @WebMethod(action = "listarFuncionarioByLoginNameAndEstado", operationName = "listarFuncionarioByLoginNameAndEstado")
    public FuncionarioDTO listarFuncionarioByLoginNameAndEstado(@WebParam(name = "login_name") final String loginName, @WebParam(name = "estado") final String estado) throws BusinessException, SystemException {
        return boundary.listarFuncionarioByLoginNameAndEstado(loginName, estado);
    }

    /**
     * @param codDependencia
     * @param codEstado
     * @return
     * @throws SystemException
     */
    @WebMethod(action = "listarFuncionariosByCodDependenciaAndCodEstado", operationName = "listarFuncionariosByCodDependenciaAndCodEstado")
    public FuncionariosDTO listarFuncionariosByCodDependenciaAndCodEstado(@WebParam(name = "cod_dependencia") final String codDependencia, @WebParam(name = "cod_estado") final String codEstado) throws SystemException {
        return boundary.listarFuncionariosByCodDependenciaAndCodEstado(codDependencia, codEstado);
    }
}
