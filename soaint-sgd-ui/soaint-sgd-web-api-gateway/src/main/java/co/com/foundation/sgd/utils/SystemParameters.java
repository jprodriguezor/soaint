package co.com.foundation.sgd.utils;

public class SystemParameters {

    public static final String BACKAPI_ENDPOINT_URL = "correspondencia_api_service";
    public static final String BACKAPI_CARGAMASIVA_ENDPOINT_URL = "massive_loader_api_endpoint";
    public static final String BACKAPI_ENTERPRISE_SERVICE_ENDPOINT_URL = "bpm_api_endpoint";
    public static final String BACKAPI_ECM_SERVICE_ENDPOINT_URL = "ecm_api_endpoint";
    public static final String BACKAPI_ECM_RECORD_SERVICE_ENDPOINT_URL = "ecm_record_api_endpoint";
    public static final String BACKAPI_FUNCIONARIO_SERVICE_ENDPOINT_URL = "funcionario_api_endpoint";
    public static final String BACKAPI_DROOLS_SERVICE_ENDPOINT_URL = "drools_endpoint";
    public static final String BACKAPI_DROOLS_SERVICE_TOKEN = "drools_token";

    private SystemParameters() {
    }

    public static String getParameter(final String parameterName) {
        return System.getProperty(parameterName);
    }

}