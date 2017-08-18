package co.com.soaint.ecm.business.boundary.mediator;

import co.com.soaint.ecm.business.boundary.documentmanager.ContentManager;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.exceptions.InfrastructureException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jboss.resteasy.specimpl.ResponseBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.util.List;



/**
 * Created by Dasiel
 */
@Service
public class EcmManager {

    private static final Logger logger = LogManager.getLogger (EcmManager.class.getName ( ));

    @Autowired
    private
    ContentManager contentManager;

    /**
     * Metodo que llama el servicio para crear la estructura del ECM
     * @param structure Objeto que contiene la estrucutra a crear en el ECM
     * @return Mensaje de respuesta(codigo y mensaje)
     * @throws InfrastructureException Excepcion ante errores del metodo
     */
    public MensajeRespuesta crearEstructuraECM(List <EstructuraTrdDTO> structure) {
        logger.info ("### Creando estructura content..------");
        MensajeRespuesta response = new MensajeRespuesta ( );
        try {
            response = contentManager.crearEstructuraContent (structure);

        } catch (Exception e) {
            response.setCodMensaje ("000005");
            response.setMensaje ("Error al crear estructura");
            logger.error ("### Error Creando estructura content..------" , e);
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
    public String subirDocumento(String nombreDocumento, MultipartFormDataInput documento, String tipoComunicacion)  {
        logger.info ("### Subiendo documento al content..");
        String idDocumento = "";
        try {
            idDocumento = contentManager.subirDocumentoContent (nombreDocumento, documento, tipoComunicacion);
        } catch (Exception e) {
            logger.error ("### Error..------" , e);
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
    public MensajeRespuesta moverDocumento(String documento, String carpetaFuente, String carpetaDestino){
        logger.info ("### Moviendo documento dentro del content..");
        MensajeRespuesta response = new MensajeRespuesta ( );
        try {

            response = contentManager.moverDocumento (documento, carpetaFuente, carpetaDestino);

        } catch (Exception e) {
            response.setCodMensaje ("000002");
            response.setMensaje ("Error al mover documento");
            logger.error ("### Error..------" , e);
        }

        return response;
    }

    /**
     * Metodo generico para descargar los documentos del ECM
     *
     * @param idDoc Identificador del documento dentro del ECM
     * @return Documento
     */
    public Response descargarDocumento(String idDoc){
        logger.info ("### Descargando documento del content..");
        ResponseBuilder response=new com.sun.jersey.core.spi.factory.ResponseBuilderImpl ();
        try{
            response=Response.ok(contentManager.descargarDocumentoContent (idDoc));
        }catch(Exception e){
        logger.error("Error descargando documento",e);
        }
        logger.info ("### Se devuelve el documento del content..");
        return response.build ();
    }

}
