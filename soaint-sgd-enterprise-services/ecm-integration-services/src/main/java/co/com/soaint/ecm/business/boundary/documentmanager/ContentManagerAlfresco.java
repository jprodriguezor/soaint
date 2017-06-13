package co.com.soaint.ecm.business.boundary.documentmanager;

import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Utilities;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentControl;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentManagerMediator;
import co.com.soaint.ecm.domain.entity.Carpeta;
import co.com.soaint.ecm.domain.entity.Conexion;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.exceptions.InfrastructureException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by dasiel on 1/06/2017.
 */
@Service
public class ContentManagerAlfresco extends ContentManagerMediator {

    Logger LOGGER = Logger.getLogger(ContentManagerAlfresco.class.getName());

    @Autowired
    Utilities utils;

    @Autowired
    Carpeta carpeta;

    @Autowired
    FactoriaContent factoriaContent;


    ContentControl control = FactoriaContent.getContentControl("alfresco");
    @Override
    public MensajeRespuesta crearEstructuraContent(List<EstructuraTrdDTO> structure) throws InfrastructureException {
        LOGGER.info("### Creando estructura content..");
        MensajeRespuesta response = new MensajeRespuesta();

        try {

            /**
             * Se establece la conexion*/
            response = control.establecerConexiones();

            Utilities utils = new Utilities();
            Conexion conexion=new Conexion ();
        for (EstructuraTrdDTO estructuraTrdVO : structure) {
                utils.ordenarListaOrganigrama(estructuraTrdVO.getOrganigramaItemList());
            }

            try {
                conexion= FactoriaContent.getContentControl("alfresco").obtenerConexion ();
            } catch (SystemException e) {
                e.printStackTrace ( );
            }
            carpeta=new Carpeta ();
            carpeta.setFolder (conexion.getSession ().getRootFolder () );
            response = control.generarArbol(structure,carpeta);
//            /**
//             * Obtener carpeta raiz
//             * */
//             Folder carpetaPadre = control.verificarCarpetaPadre(nombreCarpetaPadre);
//            LOGGER.info("### Carpeta padre: "+nombreCarpetaPadre);
//            Carpeta carpeta=new Carpeta ();
//             carpeta.setFolder (carpetaPadre);
//
//             //TODO eliminar la inicializacion de carpeta padre en null
//            carpetaPadre=null;
//            if (carpetaPadre == null) {
//                LOGGER.info("### No existe la carpeta padre: "+nombreCarpetaPadre);
//                carpeta = control.crearCarpeta(nombreCarpetaPadre);
//                //TODO el metodo generarArbol hay que hacerlo
//                response = control.generarArbol(structure, carpeta);
//            } else {
//                LOGGER.info("### Si existe la carpeta padre: "+nombreCarpetaPadre);
//                response = control.generarArbol(structure, carpeta);
//            }
            /**TODO Revisar el tema de los metodos de getMessage*/
//            response.setCodMensaje(MessageUtil.getMessage("cod00"));
//            response.setMensaje(MessageUtil.getMessage("msj00"));
            control.cerrarConexionContent();

        } catch (Exception e) {
            e.printStackTrace();
            /**TODO Revisar el tema de los metodos de getMessage*/
//            response.setCodMensaje(MessageUtil.getMessage("cod08"));
//            response.setMensaje(MessageUtil.getMessage("msj08"));
            control.cerrarConexionContent();
        }
        return response;
    }





   
}
