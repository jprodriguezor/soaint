package co.com.soaint.funcionario.integration.service.ws;

import co.com.soaint.foundation.canonical.correspondencia.CredencialesDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.correspondencia.FuncionariosDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import co.com.soaint.funcionario.business.boundary.GestionarFuncionarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.math.BigInteger;

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
    @WebMethod(action = "verificarCredenciales", operationName = "verificarCredenciales")
    public FuncionarioDTO verificarCredenciales(@WebParam(name = "credenciales")final CredencialesDTO credenciales) throws BusinessException, SystemException {
        return boundary.verificarCredenciales(credenciales);
    }

    @WebMethod(action = "crearFuncionario", operationName = "crearFuncionario")
    public void crearFuncionario(@WebParam(name = "funcionario")final FuncionarioDTO funcionario)throws SystemException{
        boundary.crearFuncionario(funcionario);
        //TODo devolver un codigo indicando si funciono
    }

    @WebMethod(action = "actualizarFuncionario", operationName = "actualizarFuncionario")
    public String actualizarFuncionario(@WebParam(name = "funcionario")final FuncionarioDTO funcionario)throws SystemException{
        //boundary.crearFuncionario(funcionario);
        //TODo implementacion
        return "1";
    }

    @WebMethod(action = "eliminarFuncionario", operationName = "eliminarFuncionario")
    public String eliminarFuncionario(@WebParam(name = "id-funcionario")final BigInteger idFuncionario)throws SystemException{
        //boundary.crearFuncionario(funcionario);
        //TODo implementacion
        return "1";
    }

    @WebMethod(action = "buscarFuncionarios", operationName = "buscarFuncionarios")
    public FuncionariosDTO buscarFuncionarios(@WebParam(name = "funcionario")final FuncionarioDTO funcionario) throws BusinessException, SystemException {
        //TODo implementacion
        return new FuncionariosDTO();
    }

    @WebMethod(action = "obtenerFuncionario", operationName = "obtenerFuncionario")
    public FuncionarioDTO obtenerFuncionario(@WebParam(name = "id-funcionario")final BigInteger idFuncionario) throws BusinessException, SystemException {
        //TODo implementacion
        return new FuncionarioDTO();
    }

    /**
     *
     * @param loginNames
     * @return
     * @throws SystemException
     */
    @WebMethod(action = "listarFuncionariosByLoginNameList", operationName = "listarFuncionariosByLoginNameList")
    public FuncionariosDTO listarFuncionariosByLoginNameList(@WebParam(name = "login_name")final String[] loginNames) throws SystemException {
        return boundary.listarFuncionariosByLoginNameList(String.join(",", loginNames));
    }
}
