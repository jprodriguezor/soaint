package co.com.soaint.digitalizacion.services.util;

public class SystemParameters {

    public static final String DIR_PROCESAR = "dir-procesar";
    public static final String DIR_PROCESADAS = "dir-procesados";
    public static final String MENSAJERIA_SERVICE_ENDPONT = "mensajeria-integration-services-endpoint";

    private SystemParameters() {
    }

    public static String getParameter(final String parameterName) {
        return System.getProperty(parameterName);
    }

}
