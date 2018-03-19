package co.com.soaint.mensajeria.business.boundary.impl;

import co.com.soaint.foundation.canonical.mensajeria.MensajeGenericoQueueDTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import co.com.soaint.mensajeria.business.boundary.IGestionarMensaje;
import co.com.soaint.mensajeria.util.SystemParameters;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 5-Mar-2018
 * Author: eric.rodriguez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@BusinessBoundary
@Log4j2
public class GestionarMensaje implements IGestionarMensaje {

    /**
     * @param mensajeGenericoQueueDTO
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public String producirMensaje(MensajeGenericoQueueDTO mensajeGenericoQueueDTO) throws SystemException {
        try {

            log.info("Host: " + SystemParameters.getParameter(SystemParameters.BROKER_HOST));
            log.info("User: " + SystemParameters.getParameter(SystemParameters.BROKER_USER));
            log.info("Pass: " + SystemParameters.getParameter(SystemParameters.BROKER_PASS));
            log.info("Cola: " + mensajeGenericoQueueDTO.getColaEntrada());
            //Creando ConnectionFactory
            CachingConnectionFactory connectionFactory = new CachingConnectionFactory(SystemParameters.getParameter(SystemParameters.BROKER_HOST));
            connectionFactory.setUsername(SystemParameters.getParameter(SystemParameters.BROKER_USER));
            connectionFactory.setPassword(SystemParameters.getParameter(SystemParameters.BROKER_PASS));

            //Creando Template
            RabbitTemplate template = new RabbitTemplate(connectionFactory);
            template.setRoutingKey(mensajeGenericoQueueDTO.getColaEntrada());
            template.setMessageConverter(new JsonMessageConverter());

            log.info("Sending Message");
            template.convertAndSend(mensajeGenericoQueueDTO);

            return "1";
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }
}
