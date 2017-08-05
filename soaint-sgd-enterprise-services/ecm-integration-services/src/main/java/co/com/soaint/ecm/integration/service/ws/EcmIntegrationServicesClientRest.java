package co.com.soaint.ecm.integration.service.ws;

import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentManagerMediator;
import co.com.soaint.ecm.business.boundary.mediator.FactoriaEcmManagerMediator;
import co.com.soaint.ecm.business.boundary.mediator.interfaces.EcmManagerMediator;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.bpm.RespuestaProcesoDTO;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.InfrastructureException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

/**
 * Created by Dasiel on 19/06/2017.
 */


@Path("/ecm")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EcmIntegrationServicesClientRest {

    private static Logger LOGGER = LogManager.getLogger(EcmIntegrationServicesClientRest.class.getName());

    public EcmIntegrationServicesClientRest(){
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Autowired
    EcmManagerMediator fEcmManager;

    @POST
    @Path("/crearEstructuraContent/")
    public MensajeRespuesta crearEstructuraContent(List<EstructuraTrdDTO> structure) throws SystemException, BusinessException, IOException {
        LOGGER.info("processing rest request - Crear Estructura ECM");
        try {
            return fEcmManager.crearEstructuraECM (structure);
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }

    @POST
    @Path("/subirDocumentoECM/")
    public String subirDocumentoECM( String carpetaContenedora,String nombreDocumento, String caminoLocal, String user,String titulo, String descripcion) throws InfrastructureException, SystemException {

        LOGGER.info("processing rest request - Subir Documento ECM");
        try {
            return fEcmManager.subirDocumento (carpetaContenedora,nombreDocumento,caminoLocal,user,titulo,descripcion );
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }

    @POST
    @Path("/moverDocumentoECM/")
    public MensajeRespuesta subirDocumentoECM( String moverDocumento,String carpetaFuente, String carpetaDestino) throws InfrastructureException, SystemException {

        LOGGER.info("processing rest request - Subir Documento ECM");
        try {
            return fEcmManager.moverDocumento (moverDocumento,carpetaFuente,carpetaDestino );
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }


//
//    @WebMethod(operationName = "createStructureRecords", action = "createStructureRecords")
//    public MensajeRespuesta generarEstructuraRecords(@WebParam(name = "estructura") List <EstructuraTrdDTO> estructura,
//                                                     @WebParam(name = "user") String user,
//                                                     @WebParam(name = "password") String password) throws InfrastructureException {
//        return null;
//    }
//
//    @WebMethod(operationName = "createStructureContent", action = "createStructureContent")
//    public MensajeRespuesta generarEstructuraContent(@WebParam(name = "estructura") List <EstructuraTrdDTO> estructura,
//                                                     @WebParam(name = "user") String user,
//                                                     @WebParam(name = "password") String password) throws InfrastructureException {
//        return null;
//    }
//
//    @WebMethod(operationName = "generarEstructuraECM", action = "generarEstructuraECM")
//    public MensajeRespuesta generarEstructuraECM(@WebParam(name = "estructura") List <EstructuraTrdDTO> estructura,
//                                                 @WebParam(name = "user") String user,
//                                                 @WebParam(name = "password") String password) throws InfrastructureException {
//        MensajeRespuesta mnsRespuesta = null;
//        try {
//            mnsRespuesta = fEcmManager.getManagerMediator ("alfresco").crearEstructuraECM (estructura);
//        } catch (SystemException e) {
//            e.printStackTrace ( );
//        }
//        return mnsRespuesta;
//    }
//
//    @WebMethod(operationName = "subirDocumento", action = "subirDocumento")
//    public String subirDocumento(
//                                                 @WebParam(name = "carpetaContenedora") String carpetaContenedora,
//                                                 @WebParam(name = "nombreDocumento") String nombreDocumento,
//                                                 @WebParam(name = "caminoLocal") String caminoLocal,
//                                                 @WebParam(name = "user") String user,
//                                                 @WebParam(name = "titulo") String titulo,
//                                                 @WebParam(name = "descripcion") String descripcion) throws InfrastructureException {
//        String idDocumento = null;
//        try {
//            idDocumento = fEcmManager.getManagerMediator ("alfresco").subirDocumento ( carpetaContenedora,caminoLocal,nombreDocumento, user,titulo, descripcion);
//        } catch (SystemException e) {
//            e.printStackTrace ( );
//        }
//        return idDocumento;
//    }
//
////    @WebMethod(operationName = "moverDocumento", action = "moverDocumento")
////    public MensajeRespuesta moverDocumentos(@WebParam(name = "documento") String moverDocumento,
////                                            @WebParam(name = "carpetaFuente") String carpetaFuente,
////                                            @WebParam(name = "carpetaDestino") String carpetaDestino,
////                                            @WebParam(name = "user") String user,
////                                            @WebParam(name = "password") String password) throws InfrastructureException {
////        MensajeRespuesta mnsRespuesta = null;
////        try {
////            mnsRespuesta = fEcmManager.getManagerMediator ("alfresco").moverDocumento (moverDocumento,carpetaFuente,carpetaDestino);
////        } catch (SystemException e) {
////            e.printStackTrace ( );
////        }
////        return mnsRespuesta;
////    }
}

