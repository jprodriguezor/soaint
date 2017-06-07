/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.foundation.soaint.documentmanager.mediator.content.interfaces;

import co.com.foundation.soaint.documentmanager.domain.EstructuraTrdVO;
import co.com.foundation.soaint.documentmanager.domain.MessageResponse;
import co.com.foundation.soaint.documentmanager.exception.ECMIntegrationException;
import com.filenet.api.core.Folder;
import com.ibm.jarm.api.core.FilePlan;
import com.ibm.jarm.api.core.FilePlanRepository;
import com.ibm.jarm.api.core.RMFactory.RecordFolder;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author sarias
 */
public interface ContentManagerMediator {
    
    MessageResponse createStructureContent(List<EstructuraTrdVO> structure) throws ECMIntegrationException;

}
