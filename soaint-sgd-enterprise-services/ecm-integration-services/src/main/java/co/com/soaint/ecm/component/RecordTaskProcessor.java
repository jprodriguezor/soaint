package co.com.soaint.ecm.component;

import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentControl;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentDigitized;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.GregorianCalendar;
import java.util.Optional;

@Log4j2
@Component
@EnableScheduling
public class RecordTaskProcessor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Value("${scheduling.job.cron.enable}")
    private boolean cronFindDigitalizedDocEnable;

    @Autowired
    private RecordServices recordServices;

    @Autowired
    private ContentControl contentControl;

    @Autowired
    private ContentDigitized contentDigitized;

    @Scheduled(cron = "#{'${scheduling.job.cron.custom}' ne '' ? '${scheduling.job.cron.custom}' : configuracion.cronTypeUD.cron}")
    public void tasksExecuter() {
        log.info("Running tasks executer for a day {}", GregorianCalendar.getInstance().getTime());
        finishedRetentionTimeExecutor();
        autoCloseExecutor();
    }

    @Scheduled(cron = "#{configuracion.cronTypeDD.cron}")
    public void processDigitalizedDocuments() {
        log.info("Running task processDigitalizedDocuments in ECM");
        if (cronFindDigitalizedDocEnable) {
            try {
                contentDigitized.processDigitalizedDocuments();
            } catch (SystemException e) {
                log.error("Ocurrio un error procesar documentos {}");
                log.error("### Mensaje de Error: " + e.getMessage());
            }
        }
    }

    private void autoCloseExecutor() {
        log.info("Buscando Unidades documentales con fecha de cierre para el dia de hoy");
        final Session session = contentControl.obtenerConexion().getSession();
        final String query = "SELECT * FROM rmc:rmarecordFolderCustomProperties" +
                " WHERE rmc:xAutoCierre IS NOT NULL" +
                " AND " + ConstantesECM.RMC_X_AUTO_CIERRE + " NOT IN('', 'none')";
        final ItemIterable<QueryResult> queryResults = session.query(query, false);
        queryResults.forEach(queryResult -> {
            final String objectId = queryResult.getPropertyValueById(PropertyIds.OBJECT_ID);
            final CmisObject cmisObject = session.getObject(session.createObjectId(objectId));
            final Folder folder = (Folder) cmisObject;
            try {
                final boolean isClosed = folder.getPropertyValue(ConstantesECM.RMA_IS_CLOSED);
                final String autoCloseDate = folder.getPropertyValue(ConstantesECM.RMC_X_AUTO_CIERRE);
                if (!isClosed && !StringUtils.isEmpty(autoCloseDate)) {
                    final LocalDateTime localDateTimeVerify = LocalDateTime.parse(autoCloseDate);
                    final LocalDateTime localDateTimeCurrent = LocalDateTime.now();
                    if (localDateTimeVerify.isEqual(localDateTimeCurrent) || localDateTimeVerify.isAfter(localDateTimeCurrent)) {
                        final String idUD = folder.getPropertyValue(ConstantesECM.RMC_X_IDENTIFICADOR);
                        final String faseArchivo = folder.getPropertyValue(ConstantesECM.RMC_X_FASE_ARCHIVO);
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
        log.info("Buscando Unidades documentales que hayan culminado el tiempo de retencion para hoy");
        final Session session = contentControl.obtenerConexion().getSession();
        final String query = "SELECT * FROM rmc:rmarecordFolderCustomProperties" +
                " WHERE " + ConstantesECM.RMC_X_DISPOSITION_AS_OF + " IS NOT NULL";
        //final String query = "SELECT * FROM rma:recordSearch WHERE rma:recordSearchDispositionActionName <> 'retain'";
        final ItemIterable<QueryResult> queryResults = session.query(query, false);
        queryResults.forEach(queryResult -> {
            final String xDisposition = queryResult.getPropertyValueByQueryName(ConstantesECM.RMC_X_DISPOSITION_AS_OF);
            if (!StringUtils.isEmpty(xDisposition) && !xDisposition.trim().isEmpty()) {
                final String xFaseArchivo = queryResult.getPropertyValueByQueryName(ConstantesECM.RMC_X_FASE_ARCHIVO);
                final PhaseType phaseType = PhaseType.getPhaseTypeBy(xFaseArchivo);
                if (null != phaseType) {
                    try {
                        final LocalDateTime dateTimeEcm = LocalDateTime.parse(xDisposition);
                        final LocalDateTime dateTimeCurrent = LocalDateTime.now();
                        if (dateTimeCurrent.isEqual(dateTimeEcm) || dateTimeCurrent.isAfter(dateTimeEcm)) {
                            final UnidadDocumentalDTO unidadDocumentalDTO = new UnidadDocumentalDTO();
                            final String objectId = queryResult.getPropertyValueByQueryName(PropertyIds.OBJECT_ID);
                            final String idUD = queryResult.getPropertyValueByQueryName(ConstantesECM.RMC_X_IDENTIFICADOR);
                            boolean hasValueToUpdate = false;
                            if (phaseType == PhaseType.AG) {
                                final Folder folder = (Folder) session.getObject(session.createObjectId(objectId));
                                final LocalDateTime localDateTime = recordServices.getRetentionDateOf(folder, ConstantesECM.RMC_X_RET_ARCHIVO_CENTRAL);
                                unidadDocumentalDTO.setFechaArchivoRetencion(localDateTime);
                                //unidadDocumentalDTO.setFaseArchivo(PhaseType.AC.getPhaseName());
                                hasValueToUpdate = true;
                            } else {
                                Optional<UnidadDocumentalDTO> udById = contentControl.getUDById(idUD, session);
                                if (udById.isPresent()) {
                                    final UnidadDocumentalDTO udTmp = udById.get();
                                    final Boolean inactive = udTmp.getInactivo();
                                    if (ObjectUtils.isEmpty(inactive) || !inactive) {
                                        unidadDocumentalDTO.setInactivo(true);
                                        hasValueToUpdate = true;
                                    }
                                }
                            }
                            if (hasValueToUpdate) {
                                unidadDocumentalDTO.setId(idUD);
                                unidadDocumentalDTO.setFaseArchivo(phaseType.getPhaseName());
                                contentControl.actualizarUnidadDocumental(unidadDocumentalDTO, session);
                                if (ObjectUtils.isEmpty(unidadDocumentalDTO.getInactivo())) {
                                    unidadDocumentalDTO.setId(objectId);
                                    unidadDocumentalDTO.setFaseArchivo(PhaseType.AC.getPhaseName());
                                    recordServices.modificarRecordFolder(unidadDocumentalDTO);
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.error("Error: {}", e.getMessage());
                    }
                }
            }
        });
    }
}
