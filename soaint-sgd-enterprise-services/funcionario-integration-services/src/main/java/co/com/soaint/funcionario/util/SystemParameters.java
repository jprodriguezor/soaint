package co.com.soaint.funcionario.util;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SystemParameters {

    public static final String BACKAPI_ENDPOINT_URL = "correspondencia-api-service";
    public static final String SECURITY_FOUNDATION_SERVICE_ENDPOINT_URL = "security-foundation-endpoint";

    public static String getParameter(final String parameterName) {
        return System.getProperty(parameterName);
    }

}