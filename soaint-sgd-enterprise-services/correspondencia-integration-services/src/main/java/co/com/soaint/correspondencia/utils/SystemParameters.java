package co.com.soaint.correspondencia.utils;

public class SystemParameters {

    public static final String BACKAPI_ENDPOINT_URL = "correspondencia-api-service";
    public static final String BACKAPI_CARGAMASIVA_ENDPOINT_URL = "massive-loader-api-endpoint";
    public static final String BACKAPI_ENTERPRISE_SERVICE_ENDPOINT_URL = "bpm-api-endpoint";
    public static final String BACKAPI_ECM_SERVICE_ENDPOINT_URL = "ecm-api-endpoint";
    public static final String BACKAPI_FUNCIONARIO_SERVICE_ENDPOINT_URL = "funcionario-api-endpoint";
    public static final String BACKAPI_DROOLS_SERVICE_ENDPOINT_URL = "drools-endpoint";
    public static final String BACKAPI_DROOLS_SERVICE_TOKEN = "drools-token";

    private SystemParameters() {
    }

    public static String getParameter(final String parameterName) {
        return System.getProperty(parameterName);
    }

}