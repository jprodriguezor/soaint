package co.com.soaint.foundation.framework.components.adapters;

import java.util.Map;

import javax.jms.MessageListener;

import co.com.soaint.foundation.framework.exceptions.InfrastructureException;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created: 31-Abr-2017
 * Author: jprodriguez
 * Type: JAVA class Artifact
 * Purpose: Message activator contract
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

public interface MiddlewareActivator extends MessageListener{

    // [command selection logic] -------------------------------------

    void selectCommandToProcess(String message, Map<String, Object> header);
    
    // [transformation logic] -------------------------------------

     Object convertRequestMessage(String message) throws InfrastructureException;

}
