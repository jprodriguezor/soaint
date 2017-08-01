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
 * Created by Dasiel on 19/06/2017.
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

    @WebMethod(operationName = "generarEstructuraECM", action = "generarEstructuraECM")
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

    @WebMethod(operationName = "subirDocumento", action = "subirDocumento")
    public String subirDocumento(
                                                 @WebParam(name = "carpetaContenedora") String carpetaContenedora,
                                                 @WebParam(name = "nombreDocumento") String nombreDocumento,
                                                 @WebParam(name = "caminoLocal") String caminoLocal,
                                                 @WebParam(name = "user") String user,
                                                 @WebParam(name = "titulo") String titulo,
                                                 @WebParam(name = "descripcion") String descripcion) throws InfrastructureException {
        String idDocumento = null;
        try {
            idDocumento = fEcmManager.getManagerMediator ("alfresco").subirDocumento ( carpetaContenedora,caminoLocal,nombreDocumento, user,titulo, descripcion);
        } catch (SystemException e) {
            e.printStackTrace ( );
        }
        return idDocumento;
    }

//    @WebMethod(operationName = "moverDocumento", action = "moverDocumento")
//    public MensajeRespuesta moverDocumentos(@WebParam(name = "documento") String moverDocumento,
//                                            @WebParam(name = "carpetaFuente") String carpetaFuente,
//                                            @WebParam(name = "carpetaDestino") String carpetaDestino,
//                                            @WebParam(name = "user") String user,
//                                            @WebParam(name = "password") String password) throws InfrastructureException {
//        MensajeRespuesta mnsRespuesta = null;
//        try {
//            mnsRespuesta = fEcmManager.getManagerMediator ("alfresco").moverDocumento (moverDocumento,carpetaFuente,carpetaDestino);
//        } catch (SystemException e) {
//            e.printStackTrace ( );
//        }
//        return mnsRespuesta;
//    }
}

