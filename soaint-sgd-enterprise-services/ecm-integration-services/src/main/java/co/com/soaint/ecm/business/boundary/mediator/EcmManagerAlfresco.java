package co.com.soaint.ecm.domain.mediator;

import co.com.foundation.soaint.documentmanager.domain.EstructuraTrdVO;
import co.com.foundation.soaint.documentmanager.domain.MessageResponse;
import co.com.foundation.soaint.documentmanager.exception.ECMIntegrationException;
import co.com.foundation.soaint.documentmanager.mediator.content.ContentManager;
import co.com.foundation.soaint.documentmanager.mediator.records.RecordsManager;
import co.com.foundation.soaint.infrastructure.common.MessageUtil;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.exceptions.InfrastructureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by sarias on 11/11/2016.
 */
@Service
public class EcmManagerAlfresco implements EcmManagerMediator {

    @Autowired
    ContentManager content;

    @Autowired
    RecordsManager records;

    public MensajeRespuesta crearEstructuraECM(List<EstructuraTrdDTO> structure) throws InfrastructureException {
        MensajeRespuesta response;
        response = content.createStructureContent(structure);
        if(response.getCodMensaje().equals(MessageUtil.getMessage("cod00"))){
            response = records.createStructureRecords(structure);
        }
        return response;
    }
}
