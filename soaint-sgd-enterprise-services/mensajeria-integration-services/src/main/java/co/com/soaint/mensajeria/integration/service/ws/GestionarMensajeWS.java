package co.com.soaint.mensajeria.integration.service.ws;

/**
 * Created by eric.rodriguez on 05/03/2018.
 */

import co.com.soaint.foundation.canonical.mensajeria.MensajeGenericoQueueDTO;
import co.com.soaint.mensajeria.business.boundary.GestionarMensaje;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(targetNamespace = "http://co.com.soaint.sgd.correspondencia.service")
@Log4j2
public class GestionarMensajeWS {
    @Autowired
    GestionarMensaje boundary;

    /**
     * Constructor
     */
    public GestionarMensajeWS() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    /**
     * @param mensajeGenericoQueueDTO
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @WebMethod(action = "actualizarEstadoAgente", operationName = "actualizarEstadoAgente")
    public String producirMensaje(@WebParam(name = "mensajeGenericoQueueDTO") MensajeGenericoQueueDTO mensajeGenericoQueueDTO) throws BusinessException, SystemException {
        log.info("processing soa request - producir mensaje");
        return "";
    }
}
