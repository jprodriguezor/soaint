package co.com.soaint.correspondencia.integration.service.ws;

import co.com.soaint.correspondencia.business.boundary.GestionarPais;
import co.com.soaint.foundation.canonical.correspondencia.PaisesDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by jrodriguez on 12/05/2017.
 */
@WebService(targetNamespace = "http://co.com.soaint.sgd.correspondencia.service")
public class GestionarPaisWS {

    @Autowired
    public GestionarPais boundary;

    /**
     * Constructor
     */
    public GestionarPaisWS() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     * @param estado
     * @return
     * @throws SystemException
     */
    @WebMethod(action = "listarPaisesByEstado", operationName = "listarPaisesByEstado")
    public PaisesDTO listarPaisesByEstado(@WebParam(name = "estado") String estado) throws SystemException {
        return PaisesDTO.newInstance().paises(boundary.listarPaisesByEstado(estado)).build();
    }

    /**
     * @param nombrePais
     * @param estado
     * @return
     * @throws SystemException
     */
    @WebMethod(action = "listarPaisesByNombrePaisAndEstado", operationName = "listarPaisesByNombrePaisAndEstado")
    public PaisesDTO listarPaisesByNombrePaisAndEstado(@WebParam(name = "nombre_pais") String nombrePais, @WebParam(name = "estado") String estado) throws SystemException {
        return PaisesDTO.newInstance().paises(boundary.listarPaisesByNombrePaisAndEstado(nombrePais, estado)).build();
    }

}
