package co.com.soaint.correspondencia.integration.service.ws;

import co.com.soaint.correspondencia.business.boundary.GestionarDepartamento;
import co.com.soaint.foundation.canonical.correspondencia.DepartamentosDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by esanchez on 5/24/2017.
 */
@WebService(targetNamespace = "http://co.com.soaint.sgd.correspondencia.service")
public class GestionarDepartamentoWS {
    @Autowired
    private GestionarDepartamento boundary;

    /**
     * Constructor
     */
    public GestionarDepartamentoWS() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     * @param codPais
     * @param estado
     * @return
     * @throws SystemException
     */
    @WebMethod(action = "listarDepartamentosByCodPaisAndEstado", operationName = "listarDepartamentosByCodPaisAndEstado")
    public DepartamentosDTO listarDepartamentosByCodPaisAndEstado(@WebParam(name = "codPais") final String codPais, @WebParam(name = "estado") final String estado) throws SystemException {
        return DepartamentosDTO.newInstance().departamentos(boundary.listarDepartamentosByCodPaisAndEstado(codPais, estado)).build();
    }

    /**
     * @param estado
     * @return
     * @throws SystemException
     */
    @WebMethod(action = "listarDepartamentosByEstado", operationName = "listarDepartamentosByEstado")
    public DepartamentosDTO listarDepartamentosByEstado(@WebParam(name = "estado") final String estado) throws SystemException {
        return DepartamentosDTO.newInstance().departamentos(boundary.listarDepartamentosByEstado(estado)).build();
    }
}
