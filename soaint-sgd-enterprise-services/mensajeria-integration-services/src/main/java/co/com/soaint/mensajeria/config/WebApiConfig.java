package co.com.soaint.mensajeria.config;

import co.com.soaint.mensajeria.integration.service.rest.*;
import io.swagger.jaxrs.config.BeanConfig;
import lombok.extern.log4j.Log4j2;
import javax.management.ObjectName;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.lang.management.ManagementFactory;
import java.util.HashSet;
import java.util.Set;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 5-Mar-2018
 * Author: eric.rodriguez
 * Type: JAVA class Artifact
 * Purpose: Config
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@ApplicationPath("/services")
@Log4j2
public class WebApiConfig extends Application {

    public WebApiConfig() {
        //Init Rest Api
        initSwagger();
    }

    private void initSwagger() {
        try {
            ObjectName socketBinding = new ObjectName("jboss.as:socket-binding-group=standard-sockets,socket-binding=http");
            Integer port = (Integer) ManagementFactory.getPlatformMBeanServer().getAttribute(socketBinding, "port");
            String host = (String) ManagementFactory.getPlatformMBeanServer().getAttribute(socketBinding, "boundAddress");
            BeanConfig beanConfig = new BeanConfig();
            beanConfig.setTitle("Mensajeria Integration Services");
            beanConfig.setVersion("1.0");
            beanConfig.setSchemes(new String[]{"http"});
            beanConfig.setHost(host + ":" + port);
            beanConfig.setBasePath("/mensajeria-integration-services/services");
            beanConfig.setResourcePackage("co.com.soaint.mensajeria.service.rest");
            beanConfig.setScan(true);
        } catch (Exception ex) {
            log.error("Rest Api - a system error has occurred", ex);
        }
    }

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(MensajeWebApi.class);
        return resources;
    }

    /*public SimpleMessageListenerContainer listenerContainer() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(SystemParameters.getParameter(SystemParameters.BROKER_HOST));
        connectionFactory.setUsername(SystemParameters.getParameter(SystemParameters.BROKER_USER));
        connectionFactory.setPassword(SystemParameters.getParameter(SystemParameters.BROKER_PASS));

        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory);
        listenerContainer.setQueues(new Queue("simple.queue.name"));
        listenerContainer.setMessageConverter(new JsonMessageConverter());
        listenerContainer.setMessageListener(new RabbitMensajeConsumer());
        listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return listenerContainer;
    }*/
}
