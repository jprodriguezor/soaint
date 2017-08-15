package co.com.soaint.correspondencia.integration.service.ws;

import co.com.soaint.correspondencia.business.boundary.GestionarMunicipio;
import co.com.soaint.foundation.canonical.correspondencia.MunicipiosDTO;
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
public class GestionarMunicipioWS {

    @Autowired
    private GestionarMunicipio boundary;

    /**
     * Constructor
     */
    public GestionarMunicipioWS() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     * @param codDepar
     * @param estado
     * @return
     * @throws SystemException
     */
    @WebMethod(action = "listarMunicipiosByCodDeparAndEstado", operationName = "listarMunicipiosByCodDeparAndEstado")
    public MunicipiosDTO listarMunicipiosByCodDeparAndEstado(@WebParam(name = "codDepar") final String codDepar, @WebParam(name = "estado") final String estado) throws SystemException {
        return MunicipiosDTO.newInstance().municipios(boundary.listarMunicipiosByCodDeparAndEstado(codDepar, estado)).build();
    }

    /**
     * @param estado
     * @return
     * @throws SystemException
     */
    @WebMethod(action = "listarMunicipiosByEstado", operationName = "listarMunicipiosByEstado")
    public MunicipiosDTO listarMunicipiosByEstado(@WebParam(name = "estado") final String estado) throws SystemException {
        return MunicipiosDTO.newInstance().municipios(boundary.listarMunicipiosByEstado(estado)).build();
    }
}
