package co.com.soaint.mensajeria.business.control;

import co.com.soaint.foundation.canonical.integration.DigitalizacionDTO;
import co.com.soaint.foundation.canonical.mensajeria.MensajeGenericoQueueDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;

/**
 * Created by esaliaga on 07/03/2018.
 */
@BusinessControl
public class Transformer {

    public DigitalizacionDTO transformToDigitalizacionDTO(MensajeGenericoQueueDTO mensajeGenericoQueue){
        return DigitalizacionDTO.newInstance()
                .nroRadicado((String) mensajeGenericoQueue.getMensajeData().get("nroRadicado"))
                .fileName((String) mensajeGenericoQueue.getMensajeData().get("fileName"))
                .fileType((String) mensajeGenericoQueue.getMensajeData().get("fileType"))
                .encodedFile((String) mensajeGenericoQueue.getMensajeData().get("encodedFile"))
                .build();
    }
}
