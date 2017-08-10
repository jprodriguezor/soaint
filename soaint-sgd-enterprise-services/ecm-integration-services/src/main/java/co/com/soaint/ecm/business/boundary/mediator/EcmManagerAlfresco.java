package co.com.soaint.ecm.business.boundary.mediator;

import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentManagerMediator;
import co.com.soaint.ecm.business.boundary.mediator.interfaces.EcmManagerMediator;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.exceptions.InfrastructureException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



/**
 * Created by Dasiel
 */
@Service
public class EcmManagerAlfresco implements EcmManagerMediator {

    private static final Logger logger = LogManager.getLogger (EcmManagerAlfresco.class.getName ( ));

    @Autowired
    private
    ContentManagerMediator content;

    /**
     * Metodo que llama el servicio para crear la estructura del ECM
     * @param structure Objeto que contiene la estrucutra a crear en el ECM
     * @return Mensaje de respuesta(codigo y mensaje)
     * @throws InfrastructureException Excepcion ante errores del metodo
     */
    public MensajeRespuesta crearEstructuraECM(List <EstructuraTrdDTO> structure) throws InfrastructureException {
        logger.info ("### Creando estructura content..------");
        MensajeRespuesta response = new MensajeRespuesta ( );
        try {
            response = content.crearEstructuraContent (structure);

        } catch (Exception e) {
            response.setCodMensaje ("000005");
            response.setMensaje ("Error al crear estructura");
            logger.error ("### Error..------crearEstructuraECM " + e);
        }

        return response;
    }

    /**
     * Metodo que llama el servicio para subir documentos al ECM
     * @param nombreDocumento Nombre del documento a subir
     * @param documento Documento que se va a subir
     * @param tipoComunicacion TIpo de comunicacion que puede ser Externa o Interna
     * @return Identificador del documento creado
     * @throws InfrastructureException Excepcion ante errores del metodo
     */
    public String subirDocumento(String nombreDocumento, MultipartFormDataInput documento, String tipoComunicacion) throws InfrastructureException {
        logger.info ("### Subiendo documento al content..");
        String idDocumento = "";
        try {
            idDocumento = content.subirDocumentoContent (nombreDocumento, documento, tipoComunicacion);
        } catch (Exception e) {
            logger.error ("### Error..------subirDocumento " + e);
        }

        return idDocumento;
    }

    /**
     * Metodo que llama el servicio para mover documentos dentro del ECM
     * @param documento Nombre del documento a mover
     * @param carpetaFuente Carpeta donde se encuentra el documento
     * @param carpetaDestino Carpeta a donde se va a mover el documento.
     * @return Mensaje de respuesta (codigo y mensaje)
     * @throws InfrastructureException Excepcion ante errores del metodo
     */
    public MensajeRespuesta moverDocumento(String documento, String carpetaFuente, String carpetaDestino) throws InfrastructureException {
        logger.info ("### Moviendo documento dentro del content..");
        MensajeRespuesta response = new MensajeRespuesta ( );
        try {

            response = content.moverDocumento (documento, carpetaFuente, carpetaDestino);

        } catch (Exception e) {
            response.setCodMensaje ("000002");
            response.setMensaje ("Error al mover documento");
            logger.error ("### Error..------moverDocumento " + e);
        }

        return response;
    }


}
