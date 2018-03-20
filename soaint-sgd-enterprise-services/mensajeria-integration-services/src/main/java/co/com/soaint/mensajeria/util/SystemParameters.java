package co.com.soaint.mensajeria.util;

import lombok.NoArgsConstructor;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 5-Mar-2018
 * Author: eric.rodriguez
 * Type: JAVA class Artifact
 * Purpose: Store global parameters
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@NoArgsConstructor
public class SystemParameters {

    public static final String BROKER_HOST = "broker-host";
    public static final String BROKER_PORT = "broker-port";
    public static final String BROKER_USER = "broker-user";
    public static final String BROKER_PASS = "broker-pass";
    public static final String BROKER_IMPLEMENTATION = "broker-implementation";
    public static final String CORRESPONDENCIA_INTEGRATION_SERVICE_ENDPOINT = "correspondencia-integration-services-endpoint";

    /**
     * @param parameterName
     * @return
     * */
    public static String getParameter(final String parameterName) {
        return System.getProperty(parameterName);
    }
}
