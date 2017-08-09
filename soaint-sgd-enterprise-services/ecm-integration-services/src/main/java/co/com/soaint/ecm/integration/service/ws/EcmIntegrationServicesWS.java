package co.com.soaint.ecm.integration.service.ws;

import co.com.soaint.ecm.business.boundary.mediator.FactoriaEcmManagerMediator;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.exceptions.InfrastructureException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

/**
 * Created by jrodriguez on 19/05/2017.
 */


@WebService(targetNamespace = "http://co.com.foundation.soaint.ecm/services")
public class EcmIntegrationServicesWS {

    @Autowired
    FactoriaEcmManagerMediator fEcmManager;

    @WebMethod(operationName = "createStructureRecords", action = "createStructureRecords")
    public MensajeRespuesta generarEstructuraRecords(@WebParam(name = "estructura") List <EstructuraTrdDTO> estructura,
                                                     @WebParam(name = "user") String user,
                                                     @WebParam(name = "password") String password) throws InfrastructureException {
        return null;
    }

    @WebMethod(operationName = "createStructureContent", action = "createStructureContent")
    public MensajeRespuesta generarEstructuraContent(@WebParam(name = "estructura") List <EstructuraTrdDTO> estructura,
                                                     @WebParam(name = "user") String user,
                                                     @WebParam(name = "password") String password) throws InfrastructureException {
        return null;
    }

    @WebMethod(operationName = "generarEstructuraECM", action = "createStructureECM")
    public MensajeRespuesta generarEstructuraECM(@WebParam(name = "estructura") List <EstructuraTrdDTO> estructura,
                                                 @WebParam(name = "user") String user,
                                                 @WebParam(name = "password") String password) throws InfrastructureException {
         MensajeRespuesta mnsRespuesta = null;
        try {
            mnsRespuesta = fEcmManager.getManagerMediator ("alfresco").crearEstructuraECM (estructura);
        } catch (SystemException e) {
            e.printStackTrace ( );
        }
        return mnsRespuesta;
    }
}

