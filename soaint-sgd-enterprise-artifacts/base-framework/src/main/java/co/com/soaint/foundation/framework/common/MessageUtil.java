package co.com.soaint.foundation.framework.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ResourceBundle;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created: 20-Abril-2017
 * Author: jprodriguez
 * Type: JAVA class Artifact
 * Purpose: Util component
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

public class MessageUtil {

    private static Logger LOGGER = LogManager.getLogger(MessageUtil.class.getName());
    private static ResourceBundle bundle;
    private static MessageUtil INSTANCE;

    // [constructor] ----------------------------

    private MessageUtil(String bundleId) {
        try {
            bundle = ResourceBundle.getBundle(bundleId);
        } catch (Exception e) {
            LOGGER.error("Message Util - a system error has occurred", e);
            throw new RuntimeException(e);
        }
    }

    // ----------------------------

    public static MessageUtil getInstance(String bundleId) {
        if (INSTANCE == null)
            INSTANCE = new MessageUtil(bundleId);
        return INSTANCE;
    }


    // ----------------------------

    public static String getMessage(String key) {
        return bundle.getString(key);
    }

}

