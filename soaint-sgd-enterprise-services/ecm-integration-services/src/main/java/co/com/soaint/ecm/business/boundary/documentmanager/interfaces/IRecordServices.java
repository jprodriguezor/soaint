package co.com.soaint.ecm.business.boundary.documentmanager.interfaces;

import co.com.soaint.foundation.canonical.ecm.EntradaRecordDTO;
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

    /**
     * Permite crear la estructura en el record
     * @param structure objeto que contiene la estructura
     * @return respuesta satisfactoria para la creacion del estructura
     * @throws SystemException
     */
    MensajeRespuesta crearEstructuraRecord(List<EstructuraTrdDTO> structure) throws SystemException;

    /**
     * permite crear carpeta en el record
     * @param entrada objeto que contiene la informacion necesaria para crear la carpeta
     * @return respuesta satisfactoria para la creacion de la carpeta
     * @throws SystemException
     */
    MensajeRespuesta crearCarpetaRecord(EntradaRecordDTO entrada) throws SystemException;

    String declararRecord(String id) throws SystemException;

}
