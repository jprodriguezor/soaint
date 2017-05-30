package co.com.soaint.foundation.framework.exceptions;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created: 20-Abr-2017
 * Author: jprodriguez
 * Type: JAVA class
 * Artifact Purpose: Exception - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/common/1.0.0")
public class BusinessException extends Exception {

    private static final long serialVersionUID = 1L;
    private String reason;

    public BusinessException() {
        super();
    }

    public BusinessException(String s) {
        super(s);
        this.reason = s;
    }

    public BusinessException(String s, Throwable throwable) {
        super(s, throwable);
        this.reason = s;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
