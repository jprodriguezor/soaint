package co.com.soaint.ecm.component;

import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Utilities;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentControl;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.impl.RecordServices;
import co.com.soaint.ecm.domain.entity.AccionUsuario;
import co.com.soaint.foundation.canonical.ecm.UnidadDocumentalDTO;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

@Component
@EnableScheduling
public class AutoCloseTaskProcessor implements Serializable {

    private static final Logger logger = LogManager.getLogger(AutoCloseTaskProcessor.class.getName());

    private static final long serialVersionUID = 1L;

    @Autowired
    private RecordServices recordServices;

    @Autowired
    private ContentControl contentControl;

    @Scheduled(cron = "0 0 0 * * ?")
    public void autoCloseExecutor() {
        logger.info("Buscando Unidades documentales con fecha de cierre para hoy");

        final Session session = contentControl.obtenerConexion().getSession();
        final String query = "SELECT * FROM cmcor:CM_Unidad_Documental" +
                " WHERE " + ContentControl.CMCOR_UD_FECHA_CIERRE + " IS NOT NULL" +
                " AND " + ContentControl.CMCOR_UD_CERRADA + " = 'false'";

        final ItemIterable<QueryResult> queryResults = session.query(query, false);

        queryResults.forEach(queryResult -> {
            final String objectId = queryResult.getPropertyValueById(PropertyIds.OBJECT_ID);
            final Folder udFolder = (Folder) session.getObject(session.createObjectId(objectId));
            try {

                final Calendar calendar = udFolder.getPropertyValue(ContentControl.CMCOR_UD_FECHA_CIERRE);
                final int compareTo = Utilities.comparaFecha(GregorianCalendar.getInstance(), calendar);

                if (compareTo >= 0) {
                    String idUD = udFolder.getPropertyValue(ContentControl.CMCOR_UD_ID);
                    UnidadDocumentalDTO documentalDTO = new UnidadDocumentalDTO();
                    documentalDTO.setId(idUD);
                    documentalDTO.setAccion(AccionUsuario.CERRAR.name());
                    recordServices.gestionarUnidadDocumentalECM(documentalDTO);
                }
            } catch (Exception e) {
                logger.error("Ocurrio un error al cerrar la unidad documental {}", udFolder.getName());
                logger.error("### Mensaje de Error: " + e.getMessage());
            }
        });
    }
}
