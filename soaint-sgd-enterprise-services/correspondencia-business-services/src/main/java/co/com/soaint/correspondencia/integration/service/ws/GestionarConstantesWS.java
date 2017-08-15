package co.com.soaint.correspondencia.integration.service.ws;

import co.com.soaint.correspondencia.business.boundary.GestionarConstantes;
import co.com.soaint.foundation.canonical.correspondencia.ConstantesDTO;
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
public class GestionarConstantesWS {
    @Autowired
    private GestionarConstantes boundary;

    /**
     * Constructor
     */
    public GestionarConstantesWS() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     * @param estado
     * @return
     * @throws SystemException
     */
    @WebMethod(action = "listarConstantesByEstado", operationName = "listarConstantesByEstado")
    public ConstantesDTO listarConstantes(@WebParam(name = "estado") final String estado) throws SystemException {
        return ConstantesDTO.newInstance().constantes(boundary.listarConstantesByEstado(estado)).build();
    }

    /**
     * @param codigo
     * @param estado
     * @return
     * @throws SystemException
     */
    @WebMethod(action = "listarConstantesByCodigoAndEstado", operationName = "listarConstantesByCodigoAndEstado")
    public ConstantesDTO listarConstantesByCodigoAndEstado(@WebParam(name = "codigo") String codigo, @WebParam(name = "estado") String estado) throws SystemException {
        return ConstantesDTO.newInstance().constantes(boundary.listarConstantesByCodigoAndEstado(codigo, estado)).build();
    }

    /**
     * @param codPadre
     * @param estado
     * @return
     * @throws SystemException
     */
    @WebMethod(action = "listarConstantesByCodPadreAndEstado", operationName = "listarConstantesByCodPadreAndEstado")
    public ConstantesDTO listarConstantesByCodPadreAndEstado(@WebParam(name = "codPadre") String codPadre, @WebParam(name = "estado") String estado) throws SystemException {
        return ConstantesDTO.newInstance().constantes(boundary.listarConstantesByCodPadreAndEstado(codPadre, estado)).build();
    }
}
