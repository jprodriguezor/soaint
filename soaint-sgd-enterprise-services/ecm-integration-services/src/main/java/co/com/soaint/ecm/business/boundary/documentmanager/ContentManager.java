package co.com.soaint.ecm.business.boundary.documentmanager;

import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Utilities;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentControl;
import co.com.soaint.ecm.domain.entity.Carpeta;
import co.com.soaint.ecm.domain.entity.Conexion;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.InfrastructureException;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.beans.factory.annotation.Autowired;

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

    private static final Logger logger = LogManager.getLogger (ContentManager.class.getName ( ));


    /**
     * Metodo que crea la estructura en el ECM
     *
     * @param structure Listado que contiene la estructura a crear
     * @return Mensaje de respuesta
     * @throws InfrastructureException Excepcion que se recoje ante cualquier error
     */
    public MensajeRespuesta crearEstructuraContent(List <EstructuraTrdDTO> structure)  {
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
            logger.error ("Error creando estructura" , e);
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
    public String subirDocumentoContent(String nombreDocumento, MultipartFormDataInput documento, String tipoComunicacion)  {


        logger.info ("### Subiendo documento al content..");
        MensajeRespuesta response = new MensajeRespuesta ( );
        String idDocumento = "";
        Carpeta carpeta;
        try {
            Conexion conexion;
            new Conexion ( );
            logger.info ("### Estableciendo la conexion..");
            conexion = contentControl.obtenerConexion ( );

            //Carpeta donde se va a guardar el documento
            carpeta = new Carpeta ( );
            carpeta.setFolder (conexion.getSession ( ).getRootFolder ( ));
            logger.info ("### Se invoca el metodo de subir el documento..");
            idDocumento = contentControl.subirDocumento (conexion.getSession ( ), nombreDocumento, documento, tipoComunicacion);

            response.setCodMensaje ("0000");
            response.setMensaje ("OK");

        } catch (Exception e) {
            logger.error ("Error subiendo documento" , e);
            response.setCodMensaje ("00005");
            response.setMensaje ("Error al crear el documento");

        }
        return idDocumento;

    }

    /**
     * Metodo generico para mover documentos dentro del ECM
     *
     * @param documento      Nombre del documento que se va a mover
     * @param carpetaFuente  Carpeta donde esta actualmente el documento.
     * @param CarpetaDestino Carpeta a donde se movera el documento.
     * @return Mensaje de respuesta del metodo (coigo y mensaje)
     * @throws InfrastructureException Excepcion que devuelve el metodo en error
     */
    public MensajeRespuesta moverDocumento(String documento, String carpetaFuente, String CarpetaDestino)  {

        logger.info ("### Moviendo Documento " + documento + " desde la carpeta: " + carpetaFuente + " a la carpeta: " + CarpetaDestino);
        MensajeRespuesta response = new MensajeRespuesta ( );

        try {

            logger.info ("###Se va a establecer la conexion");
            Conexion conexion;
            new Conexion ( );
            conexion = contentControl.obtenerConexion ( );
            logger.info ("###Conexion establecida");
            response = contentControl.movDocumento (conexion.getSession ( ), documento, carpetaFuente, CarpetaDestino);

        } catch (Exception e) {
            logger.error ("Error moviendo documento" , e);
            response.setCodMensaje ("0003");
            response.setMensaje ("Error moviendo documento, esto puede ocurrir al no existir alguna de las carpetas");
        }
        return response;
    }


}
