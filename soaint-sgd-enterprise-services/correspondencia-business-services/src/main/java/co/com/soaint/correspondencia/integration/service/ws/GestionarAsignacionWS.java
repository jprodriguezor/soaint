package co.com.soaint.correspondencia.integration.service.ws;

import co.com.soaint.correspondencia.business.boundary.GestionarAsignacion;
import co.com.soaint.foundation.canonical.correspondencia.AgentesDTO;
import co.com.soaint.foundation.canonical.correspondencia.AsignacionesDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.math.BigInteger;

/**
 * Created by esanchez on 7/11/2017.
 */
@WebService(targetNamespace = "http://co.com.soaint.sgd.correspondencia.service")
public class GestionarAsignacionWS {

    @Autowired
    GestionarAsignacion boundary;

    public GestionarAsignacionWS() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @WebMethod(action = "asignarCorrespondencia", operationName = "asignarCorrespondencia")
    public AsignacionesDTO asignarCorrespondencia(@WebParam(name = "asignacionList")final AsignacionesDTO asignacionesDTO)throws BusinessException, SystemException{
        return boundary.asignarCorrespondencia(asignacionesDTO);
    }

    @WebMethod(action = "actualizarIdInstancia", operationName = "actualizarIdInstancia")
    public void actualizarIdInstancia(@WebParam(name = "ide_asignacion")final Long ideAsignacion, @WebParam(name = "id_instancia")final String idInstancia)throws BusinessException, SystemException{
        boundary.actualizarIdInstancia(ideAsignacion, idInstancia);
    }

    @WebMethod(action = "redireccionarCorrespondencia", operationName = "redireccionarCorrespondencia")
    public void redireccionarCorrespondencia(@WebParam(name = "agenteList")final AgentesDTO agentesDTO) throws BusinessException, SystemException{
        boundary.redireccionarCorrespondencia(agentesDTO);
    }

    @WebMethod(action = "listarAsignacionesByFuncionarioAndNroRadicado", operationName = "listarAsignacionesByFuncionarioAndNroRadicado")
    public AsignacionesDTO listarAsignacionesByFuncionarioAndNroRadicado(@WebParam(name = "ide_funcionario")final BigInteger ideFunci, @WebParam(name = "nro_radicado")final String nroRadicado)throws BusinessException, SystemException{
        return boundary.listarAsignacionesByFuncionarioAndNroRadicado(ideFunci, nroRadicado);
    }
}
