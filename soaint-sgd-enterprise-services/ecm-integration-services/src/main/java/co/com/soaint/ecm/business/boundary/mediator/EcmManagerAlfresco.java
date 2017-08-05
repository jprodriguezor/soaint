package co.com.soaint.ecm.business.boundary.mediator;

import co.com.soaint.ecm.business.boundary.documentmanager.FactoriaContent;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentManagerMediator;
import co.com.soaint.ecm.business.boundary.mediator.interfaces.EcmManagerMediator;
//import co.com.soaint.ecm.business.boundary.records.RecordsManager;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.common.MessageUtil;
import co.com.soaint.foundation.framework.exceptions.InfrastructureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by sarias on 11/11/2016.
 */
@Service
public class EcmManagerAlfresco implements EcmManagerMediator {

    @Autowired
    ContentManagerMediator content;

//    @Autowired
//    RecordsManager records;

    public MensajeRespuesta crearEstructuraECM(List<EstructuraTrdDTO> structure) throws InfrastructureException {
        MensajeRespuesta response;
        ContentManagerMediator content = FactoriaContent.getContentManager("alfresco");
        response = content.crearEstructuraContent(structure);
//        if(response.getCodMensaje().equals(MessageUtil.getMessage("cod00"))){
//            response = records.createStructureRecords(structure);
//        }
        return response;
    }

    public String subirDocumento(String carpetaContenedora,String caminoLocal,String nombreDocumento,String user,String titulo,String descripcion) throws InfrastructureException {
        String idDocumento;
        ContentManagerMediator content = FactoriaContent.getContentManager("alfresco");
        idDocumento = content.subirDocumentoContent (carpetaContenedora,caminoLocal,nombreDocumento, user, titulo, descripcion);
//        if(response.getCodMensaje().equals(MessageUtil.getMessage("cod00"))){
//            response = records.createStructureRecords(structure);
//        }
        return idDocumento;
    }

    public MensajeRespuesta moverDocumento(String documento, String CarpetaFuente, String CarpetaDestino) throws InfrastructureException {
        MensajeRespuesta response;
        ContentManagerMediator content = FactoriaContent.getContentManager("alfresco");
        response = content.moverDocumento(documento, CarpetaFuente, CarpetaDestino);
//        if(response.getCodMensaje().equals(MessageUtil.getMessage("cod00"))){
//            response = records.createStructureRecords(structure);
//        }
        return response;
    }
}
