package co.com.soaint.ecm.business.boundary.documentmanager;

import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Utilities;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentManagerMediator;
import co.com.soaint.ecm.business.boundary.mediator.ContentControlAlfresco;
import co.com.soaint.ecm.domain.entity.Carpeta;
import co.com.soaint.ecm.domain.entity.Conexion;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.exceptions.InfrastructureException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.core.MultivaluedMap;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by dasiel on 1/06/2017.
 */


@Service
public class ContentManagerAlfresco extends ContentManagerMediator {

    @Autowired
    ContentControlAlfresco control;

    Logger LOGGER = Logger.getLogger (ContentManagerAlfresco.class.getName ( ));

    @Override
    public MensajeRespuesta crearEstructuraContent(List <EstructuraTrdDTO> structure) throws InfrastructureException {
        LOGGER.info ("### Creando estructura content..");
        MensajeRespuesta response = new MensajeRespuesta ( );
        Carpeta carpeta;
        try {

            /**
             * Se establece la conexion*/
            LOGGER.info ("### Estableciendo Conexion con el ECM..");
            response = control.establecerConexiones ( );

            Utilities utils = new Utilities ( );
            Conexion conexion = new Conexion ( );
            for (EstructuraTrdDTO EstructuraTrdDTO : structure) {
                utils.ordenarListaOrganigrama (EstructuraTrdDTO.getOrganigramaItemList ( ));
            }

            try {
                conexion = FactoriaContent.getContentControl ("alfresco").obtenerConexion ( );
            } catch (SystemException e) {
                e.printStackTrace ( );
                response.setMensaje ("Error de conexion");
                response.setCodMensaje ("11111");
            }
            carpeta = new Carpeta ( );
            carpeta.setFolder (conexion.getSession ( ).getRootFolder ( ));
            response = control.generarArbol (structure, carpeta);

        } catch (Exception e) {
            e.printStackTrace ( );
            response.setCodMensaje ("Error creando estructura");
            response.setMensaje ("11113");
            }
        return response;
    }

    public String subirDocumentoContent(String nombreDocumento, MultipartFormDataInput documento, String tipoComunicacion) throws InfrastructureException {


        LOGGER.info ("### Subiendo documento al content..");
        MensajeRespuesta response = new MensajeRespuesta ( );
        String idDocumento = "";
        Carpeta carpeta;
        try {
            Conexion conexion = new Conexion ( );
            /**
             * Se establece la conexion*/

            try {
                LOGGER.info ("### Estableciendo la conexion..");
                conexion = FactoriaContent.getContentControl ("alfresco").obtenerConexion ( );
            } catch (SystemException e) {
                e.printStackTrace ( );
            }
            //Carpeta donde se va a guardar el documento
            carpeta = new Carpeta ( );
            carpeta.setFolder (conexion.getSession ( ).getRootFolder ( ));
            LOGGER.info ("### Se invoca el metodo de subir el documento..");
            idDocumento = control.subirDocumento (conexion.getSession ( ), nombreDocumento, documento, tipoComunicacion);

            response.setCodMensaje("0000");
            response.setMensaje("OK");

        } catch (Exception e) {
            e.printStackTrace ( );

            response.setCodMensaje("00005");
            response.setMensaje("Error al crear el documento");

        }
        return idDocumento;

    }


    public MensajeRespuesta moverDocumento(String documento, String CarpetaFuente, String CarpetaDestino) throws InfrastructureException {

        LOGGER.info ("### Moviendo Documento " + documento + " desde la carpeta: " + CarpetaFuente + " a la carpeta: " + CarpetaDestino);
        MensajeRespuesta response = new MensajeRespuesta ( );

        try {

            /**
             * Se establece la conexion*/
            LOGGER.info ("###Se va a establecer la conexion");
            response = control.establecerConexiones ( );
            LOGGER.info ("###Conexion establecida");

            Conexion conexion = new Conexion ( );

            try {
                conexion = FactoriaContent.getContentControl ("alfresco").obtenerConexion ( );
            } catch (SystemException e) {
                e.printStackTrace ( );
            }

            response = control.movDocumento (conexion.getSession ( ), documento, CarpetaFuente, CarpetaDestino);

        } catch (Exception e) {
            e.printStackTrace ( );
            response.setCodMensaje("0003");
            response.setMensaje("Error moviendo documento, esto puede ocurrir al no existir alguna de las carpetas");
        }
        return response;
    }


}
