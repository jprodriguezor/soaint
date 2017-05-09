package co.com.soaint.foundation.framework.components.adapters;

import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created: 31-Abr-2017
 * Author: jprodriguez
 * Type: JAVA class Artifact
 * Purpose: SOA legacy wrapper contract
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

public interface LegacyWrapper<I, O, R> {

    // [resource setup logic] -------------------------------------

    void initResources(R resource, String bundle) throws SystemException;

    // [dispatching logic] -------------------------------------

    O callTarget(I input) throws SystemException, BusinessException;

}
