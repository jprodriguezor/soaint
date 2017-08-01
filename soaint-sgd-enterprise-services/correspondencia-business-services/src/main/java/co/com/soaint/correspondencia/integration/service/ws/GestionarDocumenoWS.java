package co.com.soaint.correspondencia.integration.service.ws;

import co.com.soaint.correspondencia.business.boundary.GestionarDocumento;
import co.com.soaint.foundation.canonical.correspondencia.DocumentoDTO;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by esanchez on 8/1/2017.
 */
@WebService(targetNamespace = "http://co.com.soaint.sgd.correspondencia.service")
public class GestionarDocumenoWS {

    @Autowired
    GestionarDocumento boundary;

    public GestionarDocumenoWS() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @WebMethod(action = "actualizarReferenciaECM", operationName = "actualizarReferenciaECM")
    public  void actualizarReferenciaECM(@WebParam(name = "documento") final DocumentoDTO documentoDTO) throws BusinessException, SystemException {
        boundary.actualizarReferenciaECM(documentoDTO);
    }
}
