package co.com.soaint.correspondencia.business.control;

import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.soaint.facebook_api.FacebookApi_Service;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 06-Apr-2018
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
@Log4j2
public class RedesSocialesIntegration {

    public String publicarFacebook(String msg, String file)throws SystemException{
        FacebookApi_Service facebookApi_service = new FacebookApi_Service();
        try{
        return facebookApi_service.getFacebookApiSOAP().sendMessageWithImage(msg, file);
        } catch (Exception ex) {
            log.info("Fallo en servicio que publica en Redes Sociales");
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }
}
