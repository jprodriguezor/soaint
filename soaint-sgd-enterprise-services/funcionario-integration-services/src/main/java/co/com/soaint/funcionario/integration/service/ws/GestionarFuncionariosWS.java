package co.com.soaint.funcionario.integration.service.ws;

import co.com.soaint.foundation.canonical.correspondencia.FuncionariosDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import co.com.soaint.funcionario.business.boundary.GestionarFuncionarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by esanchez on 8/28/2017.
 */
@WebService(targetNamespace = "http://co.com.soaint.sgd.funcionarios.service")
public class GestionarFuncionariosWS {
    @Autowired
    GestionarFuncionarios boundary;

    /**
     * Constructor
     */
    public GestionarFuncionariosWS() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     *
     * @param codDependencia
     * @param rol
     * @param codEstado
     * @return
     * @throws SystemException
     */
    @WebMethod(action = "listarFuncionariosByDependenciaAndRolAndEstado", operationName = "listarFuncionariosByDependenciaAndRolAndEstado")
    public FuncionariosDTO listarFuncionariosByDependenciaAndRolAndEstado(@WebParam(name = "cod-dependencia")final String codDependencia,
                                                                          @WebParam(name = "rol")final String rol,
                                                                          @WebParam(name = "cod-estado")final String codEstado) throws BusinessException, SystemException {
        return boundary.listarFuncionariosByDependenciaAndRolAndEstado(codDependencia, rol, codEstado);
    }
}
