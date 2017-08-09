package co.com.soaint.ecm.business.boundary.mediator.interfaces;

import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.exceptions.SystemException;

import java.util.List;

/**
 * Created by dasiel on 01/06/2017.
 */
public interface EcmManagerMediator {
    public MensajeRespuesta crearEstructuraECM(List <EstructuraTrdDTO> structure)throws SystemException;
}
