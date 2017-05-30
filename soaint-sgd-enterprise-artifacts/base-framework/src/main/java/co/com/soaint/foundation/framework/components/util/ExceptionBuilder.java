package co.com.soaint.foundation.framework.components.util;

import co.com.soaint.foundation.framework.common.MessageUtil;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.InfrastructureException;
import co.com.soaint.foundation.framework.exceptions.SystemException;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created: 20-Abr-2017
 * Author: jprodriguez
 * Type: JAVA class Artifact
 * Purpose: Util component
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

public class ExceptionBuilder {

    private String message;
    private Throwable rootException;

    private ExceptionBuilder() {
    }

    public static ExceptionBuilder newBuilder() {
        return new ExceptionBuilder();
    }

    public ExceptionBuilder withMessage(String message) {
        this.message = message;
        return this;
    }

    public ExceptionBuilder withMessage(String bundleId, String message) {
        this.message = MessageUtil.getInstance(bundleId).getMessage(message);
        return this;
    }

    public ExceptionBuilder withRootException(Throwable rootException) {
        this.rootException = rootException;
        return this;
    }

    public BusinessException buildBusinessException() {
        return new BusinessException(message, rootException);
    }

    public SystemException buildSystemException() {
        return new SystemException(message, rootException);
    }

    public InfrastructureException buildInfrastructureException() {
        return new InfrastructureException(message, rootException);
    }

}
