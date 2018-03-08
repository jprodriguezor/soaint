package co.com.soaint.digitalizacion.services.util;

public class SystemParameters {

    public static final String DIR_PROCESAR = "dir-procesar";
    public static final String DIR_PROCESADAS = "dir-procesados";

    private SystemParameters() {
    }

    public static String getParameter(final String parameterName) {
        return System.getProperty(parameterName);
    }

}
