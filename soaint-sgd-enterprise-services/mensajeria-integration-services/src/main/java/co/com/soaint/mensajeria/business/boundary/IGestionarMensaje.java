package co.com.soaint.mensajeria.business.boundary;

import co.com.soaint.foundation.canonical.mensajeria.MensajeGenericoQueueDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;

/**
 * Created by esaliaga on 05/03/2018.
 */

public interface IGestionarMensaje {
    public String producirMensaje(MensajeGenericoQueueDTO mensajeGenericoQueueDTO) throws SystemException;
}
