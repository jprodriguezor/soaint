package co.com.soaint.ecm.business.boundary.documentmanager;

import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Utilities;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentManagerMediator;
import co.com.soaint.ecm.business.boundary.mediator.ContentControlAlfresco;
import co.com.soaint.ecm.domain.entity.Carpeta;
import co.com.soaint.ecm.domain.entity.Conexion;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.exceptions.InfrastructureException;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by dasiel
 */


@Service
public class ContentManagerAlfresco extends ContentManagerMediator {

    private final
    ContentControlAlfresco control;

    private final Logger LOGGER = Logger.getLogger (ContentManagerAlfresco.class.getName ( ));

    /**
     * Constructor de la clase
     *
     * @param control parametro que recibe la clase
     */
    @Autowired
    public ContentManagerAlfresco(ContentControlAlfresco control) {
        this.control = control;
    }

    /**
     * Metodo que crea la estructura en el ECM
     *
     * @param structure Listado que contiene la estructura a crear
     * @return Mensaje de respuesta
     * @throws InfrastructureException Excepcion que se recoje ante cualquier error
     */
    @Override
    public MensajeRespuesta crearEstructuraContent(List <EstructuraTrdDTO> structure) throws InfrastructureException {
        LOGGER.info ("### Creando estructura content..");
        MensajeRespuesta response = new MensajeRespuesta ( );
        Carpeta carpeta;
        try {

            Utilities utils = new Utilities ( );
            new Conexion ( );
            Conexion conexion;
            for (EstructuraTrdDTO EstructuraTrdDTO : structure) {
                utils.ordenarListaOrganigrama (EstructuraTrdDTO.getOrganigramaItemList ( ));
            }

            LOGGER.info ("### Estableciendo Conexion con el ECM..");
            conexion = FactoriaContent.getContentControl ("alfresco").obtenerConexion ( );

            carpeta = new Carpeta ( );
            carpeta.setFolder (conexion.getSession ( ).getRootFolder ( ));
            response = control.generarArbol (structure, carpeta);

        } catch (Exception e) {
            e.printStackTrace ( );
            response.setCodMensaje ("Error creando estructura");
            response.setMensaje ("11113");
            LOGGER.info ("Error crando estructura"+ e);
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
    public String subirDocumentoContent(String nombreDocumento, MultipartFormDataInput documento, String tipoComunicacion) throws InfrastructureException {


        LOGGER.info ("### Subiendo documento al content..");
        MensajeRespuesta response = new MensajeRespuesta ( );
        String idDocumento = "";
        Carpeta carpeta;
        try {
            Conexion conexion;
            new Conexion ( );
            LOGGER.info ("### Estableciendo la conexion..");
            conexion = FactoriaContent.getContentControl ("alfresco").obtenerConexion ( );

            //Carpeta donde se va a guardar el documento
            carpeta = new Carpeta ( );
            carpeta.setFolder (conexion.getSession ( ).getRootFolder ( ));
            LOGGER.info ("### Se invoca el metodo de subir el documento..");
            idDocumento = control.subirDocumento (conexion.getSession ( ), nombreDocumento, documento, tipoComunicacion);

            response.setCodMensaje ("0000");
            response.setMensaje ("OK");

        } catch (Exception e) {
            e.printStackTrace ( );
            LOGGER.info ("Error subiendo documento"+ e);
            response.setCodMensaje ("00005");
            response.setMensaje ("Error al crear el documento");

        }
        return idDocumento;

    }

    /**
     * Metodo generico para mover documentos dentro del ECM
     *
     * @param documento      Nombre del documento que se va a mover
     * @param CarpetaFuente  Carpeta donde esta actualmente el documento.
     * @param CarpetaDestino Carpeta a donde se movera el documento.
     * @return Mensaje de respuesta del metodo (coigo y mensaje)
     * @throws InfrastructureException Excepcion que devuelve el metodo en error
     */
    public MensajeRespuesta moverDocumento(String documento, String CarpetaFuente, String CarpetaDestino) throws InfrastructureException {

        LOGGER.info ("### Moviendo Documento " + documento + " desde la carpeta: " + CarpetaFuente + " a la carpeta: " + CarpetaDestino);
        MensajeRespuesta response = new MensajeRespuesta ( );

        try {

            //Se establece la conexion
            LOGGER.info ("###Se va a establecer la conexion");
            LOGGER.info ("###Conexion establecida");

            Conexion conexion;
            new Conexion ( );

                conexion = FactoriaContent.getContentControl ("alfresco").obtenerConexion ( );

            response = control.movDocumento (conexion.getSession ( ), documento, CarpetaFuente, CarpetaDestino);

        } catch (Exception e) {
            e.printStackTrace ( );
            LOGGER.info ("Error moviendo documento"+ e);
            response.setCodMensaje ("0003");
            response.setMensaje ("Error moviendo documento, esto puede ocurrir al no existir alguna de las carpetas");
        }
        return response;
    }


}
