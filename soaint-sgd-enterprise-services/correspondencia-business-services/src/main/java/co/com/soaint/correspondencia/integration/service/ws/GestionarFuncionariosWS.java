package co.com.soaint.correspondencia.integration.service.ws;

import co.com.soaint.correspondencia.business.boundary.GestionarFuncionarios;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
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
    GestionarFuncionarios boundary;

    public GestionarFuncionariosWS() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @WebMethod(action = "listarFuncionarioByLoginName", operationName = "listarFuncionarioByLoginName")
    public FuncionarioDTO listarFuncionarioByLoginName(@WebParam(name = "login_name") final String loginName)throws BusinessException, SystemException{
        return boundary.listarFuncionarioByLoginName(loginName);
    }
}
