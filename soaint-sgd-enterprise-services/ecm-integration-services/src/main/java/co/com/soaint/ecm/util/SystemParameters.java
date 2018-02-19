package co.com.soaint.ecm.util;

/**
 * Clase utilitaria para devolver las constantes
 */
public class SystemParameters {


    public static final String BUSINESS_PLATFORM_ENDPOINT = "ecm-endpoint";
    public static final String BUSINESS_PLATFORM_USER = "ecm-user";
    public static final String BUSINESS_PLATFORM_PASS = "ecm-pass";
    public static final String BUSINESS_PLATFORM_RECORD = "record-endpoint";
    public static final String API_SEARCH_ALFRESCO = "search-endpoint";
    public static final String API_CORE_ALFRESCO = "core-endpoint";

    private SystemParameters() {
    }

    /**
     * Metodo que devuelve el valor de las constantes
     *
     * @param parameterName Numbre del parametro que se va a devolver
     * @return
     */
    public static String getParameter(final String parameterName) {
        return System.getProperty(parameterName);
    }

}