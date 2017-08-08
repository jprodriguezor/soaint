package co.com.soaint.ecm.business.boundary.mediator;

import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentManagerMediator;
import co.com.soaint.ecm.business.boundary.mediator.interfaces.EcmManagerMediator;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.exceptions.InfrastructureException;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
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


    public MensajeRespuesta crearEstructuraECM(List <EstructuraTrdDTO> structure) throws InfrastructureException {
        MensajeRespuesta response = new MensajeRespuesta ( );
        try {
            response = content.crearEstructuraContent (structure);

        } catch (Exception e) {
            response.setCodMensaje ("000005");
            response.setMensaje ("Error al crear estructura");
        }

        return response;
    }

    public String subirDocumento(String nombreDocumento, MultipartFormDataInput documento, String tipoComunicacion) throws InfrastructureException {
        String idDocumento;
        idDocumento = content.subirDocumentoContent (nombreDocumento, documento, tipoComunicacion);
        return idDocumento;
    }

    public MensajeRespuesta moverDocumento(String documento, String CarpetaFuente, String CarpetaDestino) throws InfrastructureException {
        MensajeRespuesta response = new MensajeRespuesta ( );
        try {

            response = content.moverDocumento (documento, CarpetaFuente, CarpetaDestino);

        } catch (Exception e) {
            response.setCodMensaje ("000002");
            response.setMensaje ("Error al mover documento");
        }

        return response;
    }


}
