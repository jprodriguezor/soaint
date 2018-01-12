package co.com.soaint.correspondencia.integration.service.ws;

import co.com.soaint.correspondencia.business.boundary.GestionarPlantilla;
import co.com.soaint.foundation.canonical.correspondencia.PlantillaDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by esaliaga on 12/01/2018.
 */
@WebService(targetNamespace = "http://co.com.soaint.sgd.correspondencia.service")
public class GestionarPlantillaWS {

    @Autowired
    GestionarPlantilla boundary;

    /**
     * Constructor
     */
    public GestionarPlantillaWS() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @WebMethod(action = "consultarPlantillaByCodClasificacionAndEstaddo", operationName = "consultarPlantillaByCodClasificacionAndEstaddo")
    public PlantillaDTO consultarPlantillaByCodClasificacionAndEstaddo(@WebParam(name = "cod_clasificacion") final String codClasificacion,
                                                                       @WebParam(name = "estado") final String estado)throws SystemException, BusinessException {
        return boundary.consultarPlantillaByCodClasificacionAndEstaddo(codClasificacion, estado);
    }
}
