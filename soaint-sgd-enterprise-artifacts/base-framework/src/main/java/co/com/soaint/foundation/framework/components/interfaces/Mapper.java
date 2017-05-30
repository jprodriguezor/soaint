package co.com.soaint.foundation.framework.components.interfaces;

import co.com.soaint.foundation.framework.exceptions.InfrastructureException;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created: 20-Abr-2017
 * Author: jprodriguez
 * Type: JAVA class Artifact
 * Purpose: Mapper contract
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@FunctionalInterface
public interface Mapper<I, O> {

    // [core method] -------------------------------------

    O map(I input) throws InfrastructureException;

}
