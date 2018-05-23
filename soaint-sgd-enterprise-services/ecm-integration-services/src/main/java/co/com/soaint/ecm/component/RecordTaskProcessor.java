package co.com.soaint.ecm.component;

import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Utilities;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentControl;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.impl.RecordServices;
import co.com.soaint.ecm.domain.entity.AccionUsuario;
import co.com.soaint.ecm.domain.entity.PhaseType;
import co.com.soaint.ecm.util.ConstantesECM;
import co.com.soaint.foundation.canonical.ecm.UnidadDocumentalDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;

@Log4j2
@Component
@EnableScheduling
public class RecordTaskProcessor implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String RMA_DISPOSITION_AS_OF = "rma:recordSearchDispositionActionAsOf";

    @Autowired
    private RecordServices recordServices;

    @Autowired
    private ContentControl contentControl;

    @Scheduled(cron = "${scheduling.job.cron}")
    public void tasksExecuter() {
        log.info("Running tasks executer for a day {}", GregorianCalendar.getInstance().getTime());
        autoCloseExecutor();
        finishedRetentionTimeExecutor();
    }

    private void autoCloseExecutor() {
        log.info("Buscando Unidades documentales con fecha de cierre para el dia de hoy");

        final Session session = contentControl.obtenerConexion().getSession();
        final String query = "SELECT * FROM cmcor:CM_Unidad_Documental" +
                " WHERE " + ConstantesECM.CMCOR_UD_FECHA_CIERRE + " IS NOT NULL" +
                " AND " + ConstantesECM.CMCOR_UD_CERRADA + " = 'false'";

        final ItemIterable<QueryResult> queryResults = session.query(query, false);

        queryResults.forEach(queryResult -> {
            final String objectId = queryResult.getPropertyValueById(PropertyIds.OBJECT_ID);
            final Folder udFolder = (Folder) session.getObject(session.createObjectId(objectId));
            try {
                final Calendar calendar = udFolder.getPropertyValue(ConstantesECM.CMCOR_UD_FECHA_CIERRE);
                final int compareTo = Utilities.comparaFecha(GregorianCalendar.getInstance(), calendar);

                if (compareTo >= 0) {
                    String idUD = udFolder.getPropertyValue(ConstantesECM.CMCOR_UD_ID);
                    UnidadDocumentalDTO documentalDTO = new UnidadDocumentalDTO();
                    documentalDTO.setId(idUD);
                    documentalDTO.setAccion(AccionUsuario.CERRAR.name());
                    log.info("Efectuando cierre para la UD '{}' con ID '{}'", documentalDTO.getNombreUnidadDocumental(), documentalDTO.getId());
                    recordServices.gestionarUnidadDocumentalECM(documentalDTO);
                    log.info("UD cerrada correctamente");
                }
            } catch (Exception e) {
                log.error("Ocurrio un error al cerrar la unidad documental {}", udFolder.getName());
                log.error("### Mensaje de Error: " + e.getMessage());
            }
        });
    }

    private void finishedRetentionTimeExecutor() {
        final Calendar currentCalendar = GregorianCalendar.getInstance();
        log.info("Buscando Unidades documentales que hayan culminado el tiempo de retencion para hoy");
        final Session session = contentControl.obtenerConexion().getSession();
        final String query = "SELECT * FROM rma:recordSearch WHERE rma:recordSearchDispositionActionName <> 'retain'";
        final ItemIterable<QueryResult> queryResults = session.query(query, false);
        queryResults.forEach(queryResult -> {
            Calendar dispositionCalendar = queryResult.getPropertyValueByQueryName(RMA_DISPOSITION_AS_OF);
            if (!ObjectUtils.isEmpty(dispositionCalendar)) {
                String idUd = queryResult.getPropertyValueByQueryName(ConstantesECM.RMC_X_IDENTIFICADOR);
                Optional<UnidadDocumentalDTO> optionalUnidadDocumentalDTO = contentControl
                        .getUDById(idUd, session);
                if (optionalUnidadDocumentalDTO.isPresent()) {
                    UnidadDocumentalDTO dto = optionalUnidadDocumentalDTO.get();
                    try {
                        final int compareTo = Utilities.comparaFecha(currentCalendar, dispositionCalendar);
                        if (compareTo >= 0 && (dto.getCerrada() && dto.getInactivo())) {
                            dto.setAccion(AccionUsuario.ABRIR.name());
                            recordServices.gestionarUnidadDocumentalECM(dto);
                            dto.setInactivo(true);
                        }
                        dto.setFaseArchivo(PhaseType.ARCHIVO_CENTRAL.getValueAt(1));
                        contentControl.actualizarUnidadDocumental(dto, session);
                        dto.setCerrada(null);
                        recordServices.modificarRecordFolder(dto);
                    } catch (SystemException e) {
                        log.error("Error: " + e.getMessage());
                    }
                }
            }
        });
    }
}
