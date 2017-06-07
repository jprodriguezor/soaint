package co.com.soaint.ecm.business.boundary.documentmanager;

import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentControl;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentManagerMediator;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.exceptions.InfrastructureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by dasiel on 1/06/2017.
 */
@Service
public class ContentManager implements ContentManagerMediator {

    Logger LOGGER = Logger.getLogger(ContentManager.class.getName());

    @Autowired
    ContentControl control;

    @Override
    public MensajeRespuesta crearEstructuraContent(List<EstructuraTrdDTO> structure) throws InfrastructureException {
        LOGGER.info("### Creando estructura content..");
        MensajeRespuesta response = new MensajeRespuesta();
        //ContentControl control = new ContentControl();
        try {
            response = control.establecerConexiones();
            Utilities utils = new Utilities();
            for (EstructuraTrdDTO estructuraTrdVO : structure) {
                utils.ordenarListaOrganigrama(estructuraTrdVO.getOrganigramaItemList());
            }
            String nombreCarpetaPadre = Configuracion.getPropiedad("dominio");
            LOGGER.info("### Carpeta padre: "+nombreCarpetaPadre);
            Folder carpetaPadre = control.verificarCarpetaPadre(nombreCarpetaPadre);

            if (carpetaPadre == null) {
                LOGGER.info("### No existe la carpeta padre: "+nombreCarpetaPadre);
                carpetaPadre = control.crearCarpeta(nombreCarpetaPadre);
                response = control.generarArbol(structure, carpetaPadre);
            } else {
                LOGGER.info("### Si existe la carpeta padre: "+nombreCarpetaPadre);
                response = control.generarArbol(structure, carpetaPadre);
            }
            response.setCodMessage(MessageUtil.getMessage("cod00"));
            response.setMessage(MessageUtil.getMessage("msj00"));
            control.cerrarConexionContent();

        } catch (Exception e) {
            e.printStackTrace();
            response.setCodMessage(MessageUtil.getMessage("cod08"));
            response.setMessage(MessageUtil.getMessage("msj08"));
            control.cerrarConexionContent();
        }
        return response;
    }





   
}
