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
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
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

    @Scheduled(cron = "#{'${scheduling.job.cron.custom}' ne '' ? '${scheduling.job.cron.custom}' : configuracion.cronType.cron}")
    public void tasksExecuter() {
        log.info("Running tasks executer for a day {}", GregorianCalendar.getInstance().getTime());
        finishedRetentionTimeExecutor();
        autoCloseExecutor();
    }

    private void autoCloseExecutor() {
        log.info("Buscando Unidades documentales con fecha de cierre para el dia de hoy");
        final Session session = contentControl.obtenerConexion().getSession();
        final String query = "SELECT * FROM rmc:rmarecordFolderCustomProperties" +
                " WHERE rmc:xAutoCierre IS NOT NULL" +
                " AND " + RecordServices.RMC_X_AUTO_CIERRE + " NOT IN('', 'none')";
        final ItemIterable<QueryResult> queryResults = session.query(query, false);
        queryResults.forEach(queryResult -> {
            final String objectId = queryResult.getPropertyValueById(PropertyIds.OBJECT_ID);
            final CmisObject cmisObject = session.getObject(session.createObjectId(objectId));
            final Folder folder = (Folder) cmisObject;
            try {
                final boolean isClosed = folder.getPropertyValue(RecordServices.RMA_IS_CLOSED);
                final String autoCloseDate = folder.getPropertyValue(RecordServices.RMC_X_AUTO_CIERRE);
                if (!isClosed && !StringUtils.isEmpty(autoCloseDate)) {
                    final LocalDateTime localDateTimeVerify = LocalDateTime.parse(autoCloseDate);
                    final LocalDateTime localDateTimeCurrent = LocalDateTime.now();
                    if (localDateTimeVerify.isEqual(localDateTimeCurrent) || localDateTimeVerify.isAfter(localDateTimeCurrent)) {
                        final String idUD = folder.getPropertyValue(RecordServices.RMC_X_IDENTIFICADOR);
                        final String faseArchivo = folder.getPropertyValue(RecordServices.RMC_X_FASE_ARCHIVO);
                        final UnidadDocumentalDTO documentalDTO = new UnidadDocumentalDTO();
                        documentalDTO.setId(idUD);
                        documentalDTO.setFaseArchivo(faseArchivo);
                        documentalDTO.setAccion(AccionUsuario.CERRAR.name());
                        log.info("Efectuando cierre para la UD '{}' con ID '{}'", documentalDTO.getNombreUnidadDocumental(), documentalDTO.getId());
                        recordServices.gestionarUnidadDocumentalECM(documentalDTO);
                        log.info("UD cerrada correctamente");
                    }
                }
            } catch (Exception e) {
                log.error("Ocurrio un error al cerrar la unidad documental {}", folder.getName());
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
                        dto.setFaseArchivo(PhaseType.ARCHIVO_CENTRAL.getName());
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
