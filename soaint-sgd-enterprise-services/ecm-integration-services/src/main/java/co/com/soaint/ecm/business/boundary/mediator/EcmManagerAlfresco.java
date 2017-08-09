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
import java.util.logging.Logger;


/**
 * Created by Dasiel
 */
@Service
public class EcmManagerAlfresco implements EcmManagerMediator {

    private Logger LOGGER = Logger.getLogger (EcmManagerAlfresco.class.getName ( ));

    private final
    ContentManagerMediator content;

    @Autowired
    public EcmManagerAlfresco(ContentManagerMediator content) {
        this.content = content;
    }


    public MensajeRespuesta crearEstructuraECM(List <EstructuraTrdDTO> structure) throws InfrastructureException {
        LOGGER.info ("### Creando estructura content..------");
        MensajeRespuesta response = new MensajeRespuesta ( );
        try {
            response = content.crearEstructuraContent (structure);

        } catch (Exception e) {
            response.setCodMensaje ("000005");
            response.setMensaje ("Error al crear estructura");
            LOGGER.info ("### Error..------"+e);
        }

        return response;
    }

    public String subirDocumento(String nombreDocumento, MultipartFormDataInput documento, String tipoComunicacion) throws InfrastructureException {
        LOGGER.info ("### Subiendo documento al content..");
        String idDocumento="";
        try {
            idDocumento = content.subirDocumentoContent (nombreDocumento, documento, tipoComunicacion);
        }catch (Exception e){
            LOGGER.info ("### Error..------"+e);
        }

        return idDocumento;
    }

    public MensajeRespuesta moverDocumento(String documento, String CarpetaFuente, String CarpetaDestino) throws InfrastructureException {
        MensajeRespuesta response = new MensajeRespuesta ( );
        try {

            response = content.moverDocumento (documento, CarpetaFuente, CarpetaDestino);

        } catch (Exception e) {
            response.setCodMensaje ("000002");
            response.setMensaje ("Error al mover documento");
            LOGGER.info ("### Error..------"+e);
        }

        return response;
    }


}
