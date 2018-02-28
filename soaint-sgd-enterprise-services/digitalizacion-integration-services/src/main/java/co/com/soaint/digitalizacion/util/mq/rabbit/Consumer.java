package co.com.soaint.digitalizacion.util.mq.rabbit;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

public class Consumer implements MessageListener {

    //@Override
    public void onMessage(Message message) {
        System.out.println(new String(message.getBody()));
    }
}