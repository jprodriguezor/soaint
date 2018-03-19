package co.com.soaint.mensajeria.business.control;

import co.com.soaint.foundation.canonical.mensajeria.MensajeGenericoQueueDTO;
import co.com.soaint.mensajeria.apis.delegator.CorrespondenciaIntegrationClient;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Tulkas on 06/03/2018.
 */
@Log4j2
public class RabbitMensajeConsumer implements MessageListener {

    @Autowired
    CorrespondenciaIntegrationClient correspondenciaIntegrationClient;
    @Autowired
    Transformer transformer;

    @Override
    public void onMessage(Message message) {
        log.info(new String(message.getBody()));
        try {
            MensajeGenericoQueueDTO mensajeGenericoQueue = new Gson().fromJson(new String(message.getBody()), MensajeGenericoQueueDTO.class);
            if (mensajeGenericoQueue.getOperacion().equals("digitalizar-documento")){
                correspondenciaIntegrationClient.digitalizarDocumento(transformer.transformToDigitalizacionDTO(mensajeGenericoQueue));
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
