package co.com.soaint.mensajeria.business.control;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

/**
 * Created by Tulkas on 06/03/2018.
 */
@Log4j2
public class RabbitMensajeConsumer implements MessageListener {

    @Override
    public void onMessage(Message message) {
        log.info(new String(message.getBody()));
    }
}
