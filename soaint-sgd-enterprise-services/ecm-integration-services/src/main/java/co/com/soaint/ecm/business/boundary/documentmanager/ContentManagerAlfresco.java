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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by dasiel on 1/06/2017.
 */


@Service
public class ContentManagerAlfresco extends ContentManagerMediator {

    @Autowired
    ContentControlAlfresco control;

    Logger LOGGER = Logger.getLogger (ContentManagerAlfresco.class.getName ( ));

//    ContentControl control = FactoriaContent.getContentControl ("alfresco");

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
            control.cerrarConexionContent ( );

        } catch (Exception e) {
            e.printStackTrace ( );
            response.setCodMensaje ("Error creando estructura");
            response.setMensaje ("11113");
            control.cerrarConexionContent ( );
        }
        return response;
    }

    public String subirDocumentoContent(String nombreDocumento, MultipartFile documento, String tipoComunicacion) throws InfrastructureException {
        LOGGER.info ("### Subiendo documento al content..");
        MensajeRespuesta response = new MensajeRespuesta ( );
        String idDocumento = "";
        Carpeta carpeta;
        try {

            Utilities utils = new Utilities ( );
            Conexion conexion = new Conexion ( );

            /**
             * Se establece la conexion*/

            try {
                conexion = FactoriaContent.getContentControl ("alfresco").obtenerConexion ( );
            } catch (SystemException e) {
                e.printStackTrace ( );
            }
            //Carpeta donde se va a guardar el documento
            carpeta = new Carpeta ( );

            carpeta.setFolder (conexion.getSession ( ).getRootFolder ( ));
            String mimeType = "pdf";
            //TODO la carpeta target hay que llenarla, el mimetype


            idDocumento = control.subirDocumento (conexion.getSession ( ), nombreDocumento, documento, tipoComunicacion);

            /**TODO Revisar el tema de los metodos de getMessage*/
//            response.setCodMensaje(MessageUtil.getMessage("cod00"));
//            response.setMensaje(MessageUtil.getMessage("msj00"));
            control.cerrarConexionContent ( );

        } catch (Exception e) {
            e.printStackTrace ( );
            /**TODO Revisar el tema de los metodos de getMessage*/
//            response.setCodMensaje(MessageUtil.getMessage("cod08"));
//            response.setMensaje(MessageUtil.getMessage("msj08"));
            control.cerrarConexionContent ( );
        }
        return idDocumento;
    }

    //TODO Mover docuemnto
    public MensajeRespuesta moverDocumento(String documento, String CarpetaFuente, String CarpetaDestino) throws InfrastructureException {
        LOGGER.info ("### Moviendo Documento " + documento + " desde la carpeta: " + CarpetaFuente + " a la carpeta: " + CarpetaDestino);
        MensajeRespuesta response = new MensajeRespuesta ( );

        try {

            /**
             * Se establece la conexion*/
            response = control.establecerConexiones ( );

            Utilities utils = new Utilities ( );
            Conexion conexion = new Conexion ( );

            try {
                conexion = FactoriaContent.getContentControl ("alfresco").obtenerConexion ( );
            } catch (SystemException e) {
                e.printStackTrace ( );
            }

            response = control.movDocumento (conexion.getSession ( ), documento, CarpetaFuente, CarpetaDestino);
            /**TODO Revisar el tema de los metodos de getMessage*/
//            response.setCodMensaje(MessageUtil.getMessage("cod00"));
//            response.setMensaje(MessageUtil.getMessage("msj00"));
            control.cerrarConexionContent ( );

        } catch (Exception e) {
            e.printStackTrace ( );
            /**TODO Revisar el tema de los metodos de getMessage*/
//            response.setCodMensaje(MessageUtil.getMessage("cod08"));
//            response.setMensaje(MessageUtil.getMessage("msj08"));
            control.cerrarConexionContent ( );
        }
        return response;
    }


}
