package co.com.soaint.foundation.framework.components.adapters;

import java.util.Optional;

import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created: 31-Abr-2017
 * Author: jprodriguez
 * Type: JAVA class Artifact
 * Purpose: Business delegator contract
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

public interface BusinessDelegator<I,O> {

	static int SYNC_PROCESSING = 1;
	static int ASYNC_PROCESSING = 0;
	
    // [core delegation logic] -------------------------------------

	Optional<O> delegate( Optional<I> input, String command, int processingType ) throws SystemException,BusinessException;
	
}
