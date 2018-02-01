package co.com.soaint.ecm.business.boundary.documentmanager.interfaces;

import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by amartinez on 24/01/2018.
 */
@Service
public interface IRecordServices {


    MensajeRespuesta crearEstructuraRecord(List<EstructuraTrdDTO> structure) throws SystemException;


}
