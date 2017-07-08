/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soaint.ecm.business.boundary.records.interfaces;



import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.exceptions.InfrastructureException;

import java.util.List;

/**
 *
 * @author JSGONZALEZ
 */
public interface RecordManagerMediator {

    MensajeRespuesta createStructureRecords(List <EstructuraTrdDTO> structure) throws InfrastructureException;


}
