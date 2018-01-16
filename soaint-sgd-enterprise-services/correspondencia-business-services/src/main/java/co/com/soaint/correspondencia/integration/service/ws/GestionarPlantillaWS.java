package co.com.soaint.correspondencia.integration.service.ws;

import co.com.soaint.correspondencia.business.boundary.GestionarPlantilla;
import co.com.soaint.foundation.canonical.correspondencia.ConstanteDTO;
import co.com.soaint.foundation.canonical.correspondencia.PlantillaDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

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

    /**
     *
     * @param codTipoDoc
     * @param estado
     * @return
     * @throws SystemException
     * @throws BusinessException
     */
    @WebMethod(action = "consultarPlantillaByCodClasificacionAndEstaddo", operationName = "consultarPlantillaByCodClasificacionAndEstaddo")
    public PlantillaDTO consultarPlantillaByCodClasificacionAndEstaddo(@WebParam(name = "cod_tipo_doc") final String codTipoDoc,
                                                                       @WebParam(name = "estado") final String estado)throws SystemException, BusinessException {
        return boundary.consultarPlantillaByCodClasificacionAndEstaddo(codTipoDoc, estado);
    }

    @WebMethod(action = "listarTipologiasDocumentalesByEstado", operationName = "listarTipologiasDocumentalesByEstado")
    public List<ConstanteDTO> listarTipologiasDocumentalesByEstado(@WebParam(name = "estado") final String estado)throws SystemException{
        return boundary.listarTipologiasDocumentalesByEstado(estado);
    }
}
