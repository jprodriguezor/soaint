package co.com.soaint.ecm.business.boundary.mediator;

import co.com.soaint.ecm.business.boundary.documentmanager.FactoriaContent;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentManagerMediator;
import co.com.soaint.ecm.business.boundary.mediator.interfaces.EcmManagerMediator;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.exceptions.InfrastructureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.logging.Logger;

//import co.com.soaint.ecm.business.boundary.records.RecordsManager;

/**
 * Created by sarias on 11/11/2016.
 */
@Service
public class EcmManagerAlfresco implements EcmManagerMediator {

    @Autowired
    ContentManagerMediator content;


    public MensajeRespuesta crearEstructuraECM(List <EstructuraTrdDTO> structure) throws InfrastructureException {
        MensajeRespuesta response;
//        ContentManagerMediator content = FactoriaContent.getContentManager("alfresco");
        response = content.crearEstructuraContent (structure);
        return response;
    }

    public String subirDocumento(String nombreDocumento, MultipartFile documento, String tipoComunicacion) throws InfrastructureException {
        String idDocumento;
//        ContentManagerMediator content = FactoriaContent.getContentManager ("alfresco");
        idDocumento = content.subirDocumentoContent (nombreDocumento, documento, tipoComunicacion);
//        if(response.getCodMensaje().equals(MessageUtil.getMessage("cod00"))){
//            response = records.createStructureRecords(structure);
//        }
        return idDocumento;
    }

    public MensajeRespuesta moverDocumento(String documento, String CarpetaFuente, String CarpetaDestino) throws InfrastructureException {
        MensajeRespuesta response=new MensajeRespuesta ();
//        ContentManagerMediator content = FactoriaContent.getContentManager ("alfresco");
        try{

            response = content.moverDocumento (documento, CarpetaFuente, CarpetaDestino);

        }catch(Exception e)
        {
            response.setCodMensaje ("000002");
            response.setMensaje ("Error al mover documento");
        }

        return response;
        }


}
