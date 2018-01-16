package co.com.soaint.ecm.business.boundary.documentmanager;

import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Utilities;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentControl;
import co.com.soaint.ecm.domain.entity.Carpeta;
import co.com.soaint.ecm.domain.entity.Conexion;
import co.com.soaint.foundation.canonical.correspondencia.MetadatosDocumentosDTO;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.InfrastructureException;
import com.sun.jersey.core.spi.factory.ResponseBuilderImpl;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.io.IOException;
import java.util.List;


/**
 * Created by dasiel
 */

@BusinessBoundary
@NoArgsConstructor
public class ContentManager {
    @Autowired
    private
    ContentControl contentControl;
    private static final String MSGCONEXION="### Estableciendo la conexion..";
    private static final Logger logger = LogManager.getLogger (ContentManager.class.getName ( ));


    /**
     * Metodo que crea la estructura en el ECM
     *
     * @param structure Listado que contiene la estructura a crear
     * @return Mensaje de respuesta
     * @throws InfrastructureException Excepcion que se recoje ante cualquier error
     */
    public MensajeRespuesta crearEstructuraContent(List <EstructuraTrdDTO> structure) {
        logger.info ("### Creando estructura content..");
        MensajeRespuesta response = new MensajeRespuesta ( );
        Carpeta carpeta;
        try {

            Utilities utils = new Utilities ( );
            new Conexion ( );
            Conexion conexion;
            for (EstructuraTrdDTO EstructuraTrdDTO : structure) {
                utils.ordenarListaOrganigrama (EstructuraTrdDTO.getOrganigramaItemList ( ));
            }

            logger.info ("### Estableciendo Conexion con el ECM..");
            conexion = contentControl.obtenerConexion ( );

            carpeta = new Carpeta ( );
            carpeta.setFolder (conexion.getSession ( ).getRootFolder ( ));
            response = contentControl.generarArbol (structure, carpeta);

        } catch (Exception e) {
            response.setCodMensaje ("Error creando estructura");
            response.setMensaje ("11113");
            logger.error ("Error creando estructura", e);
        }
        return response;
    }

    /**
     * Metodo generico para subir los dccumentos al content
     *
     * @param nombreDocumento  NOmbre del documento que se va a subir
     * @param documento        Documento que se va a subir
     * @param tipoComunicacion tipo de comunicacion puede ser externa o interna.
     * @return Identificador del documento que se inserto
     * @throws InfrastructureException Excepcion que se lanza en error
     */
    public MensajeRespuesta subirDocumentoContent(String nombreDocumento, MultipartFormDataInput documento, String tipoComunicacion) throws IOException {

        logger.info ("### Subiendo documento al content..");
        MensajeRespuesta response = new MensajeRespuesta ( );
        String idDocumento = "";
        Carpeta carpeta;
        try {
            Conexion conexion;
            new Conexion ( );
            logger.info (MSGCONEXION);
            conexion = contentControl.obtenerConexion ( );

            //Carpeta donde se va a guardar el documento
            carpeta = new Carpeta ( );
            carpeta.setFolder (conexion.getSession ( ).getRootFolder ( ));
            logger.info ("### Se invoca el metodo de subir el documento..");

            response=contentControl.subirDocumento (conexion.getSession ( ), nombreDocumento, documento, tipoComunicacion);

        } catch (Exception e) {
            logger.error ("Error subiendo documento", e);
            response.setCodMensaje ("2222");
            response.setMensaje ("Error al crear el documento");
            throw e;
        }
        return response;

    }

    /**
     * Metodo generico para subir los dccumentos al content
     *
     * @param metadatosDocumentos  Metadatos del documento a modificar
     * @return Identificador del documento que se inserto
     * @throws InfrastructureException Excepcion que se lanza en error
     */
    public MensajeRespuesta modificarMetadatoDocumentoContent(MetadatosDocumentosDTO metadatosDocumentos) throws IOException {

        logger.info ("### Modificando metadatos del documento..");
        MensajeRespuesta response = new MensajeRespuesta ( );
        Carpeta carpeta;
        try {
            Conexion conexion;
            new Conexion ( );
            logger.info (MSGCONEXION);
            conexion = contentControl.obtenerConexion ( );

            logger.info ("### Se invoca el metodo de modificar el documento..");

            response=contentControl.modificarMetadatosDocumento (conexion.getSession ( ), metadatosDocumentos.getIdDocumento(),metadatosDocumentos.getNroRadicado(), metadatosDocumentos.getTipologiaDocumental(), metadatosDocumentos.getNombreRemitente());

        } catch (Exception e) {
            logger.error ("Error modificando el documento", e);
            response.setCodMensaje ("2222");
            response.setMensaje ("Error al modificar el documento");
            throw e;
        }
        return response;

    }

    /**
     * Metodo generico para mover documentos dentro del ECM
     *
     * @param documento      Identificador del documento que se va a mover
     * @param carpetaFuente  Carpeta donde esta actualmente el documento.
     * @param carpetaDestino Carpeta a donde se movera el documento.
     * @return Mensaje de respuesta del metodo (coigo y mensaje)
     */
    public MensajeRespuesta moverDocumento(String documento, String carpetaFuente, String carpetaDestino) {

        logger.info ("### Moviendo Documento " + documento + " desde la carpeta: " + carpetaFuente + " a la carpeta: " + carpetaDestino);
        MensajeRespuesta response = new MensajeRespuesta ( );

        try {

            logger.info ("###Se va a establecer la conexion");
            Conexion conexion;
            new Conexion ( );
            conexion = contentControl.obtenerConexion ( );
            logger.info ("###Conexion establecida");
            response = contentControl.movDocumento (conexion.getSession ( ), documento, carpetaFuente, carpetaDestino);

        } catch (Exception e) {
            logger.error ("Error moviendo documento", e);
            response.setCodMensaje ("0003");
            response.setMensaje ("Error moviendo documento, esto puede ocurrir al no existir alguna de las carpetas");
        }
        return response;
    }

    /**
     * Metodo generico para descargar los documentos del ECM
     *
     * @param idDoc Identificador del documento dentro del ECM
     * @return Documento
     */
    public Response descargarDocumentoContent(String idDoc) {

        logger.info ("### Descargando documento del content..");
        ResponseBuilder response = new ResponseBuilderImpl ( );

        try {
            Conexion conexion;
            new Conexion ( );
            logger.info (MSGCONEXION);
            conexion = contentControl.obtenerConexion ( );
            logger.info ("### Se invoca el metodo de descargar el documento..");
            return contentControl.descargarDocumento (idDoc, conexion.getSession ( ));

        } catch (Exception e) {
            logger.error ("Error descargando documento", e);

        }
        logger.info ("### Se devuelve el documento del content..");
        return response.build ( );

    }

    /**
     * Metodo generico para eliminar los documentos del ECM
     *
     * @param idDoc Identificador del documento dentro del ECM
     * @return true en exito y false en error
     */
    public boolean eliminarDocumento(String idDoc) {

        logger.info ("### Eliminando documento del content..");

        try {
            Conexion conexion;
            new Conexion ( );
            logger.info (MSGCONEXION);
            conexion = contentControl.obtenerConexion ( );
            logger.info ("### Se invoca el metodo de eliminar el documento..");
            if (contentControl.eliminardocumento (idDoc, conexion.getSession ( ))) {
                logger.info ("Documento eliminado con exito");
                return Boolean.TRUE;
            } else
                return Boolean.FALSE;

        } catch (Exception e) {
            logger.error ("Error eliminando documento", e);
            return Boolean.FALSE;
        }
    }

}
