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
    public static final String API_SERVICE_ALFRESCO = "service-endpoint";

    private SystemParameters() {
    }

    /**
     * Metodo que devuelve el valor de las constantes
     *
     * @param parameterName Numbre del parametro que se va a devolver
     * @return
     */
    public static String getParameter(final String parameterName) {
        switch (parameterName) {
            case BUSINESS_PLATFORM_ENDPOINT:
                return "http://192.168.1.82:8080/alfresco/api/-default-/public/cmis/versions/1.1/atom";
            case BUSINESS_PLATFORM_USER:
                return "admin";
            case BUSINESS_PLATFORM_PASS:
                return "admin";
            case BUSINESS_PLATFORM_RECORD:
                return "http://192.168.1.82:8080/alfresco/api/-default-/public/gs/versions/1";
            case API_SEARCH_ALFRESCO:
                return "http://192.168.1.82:8080/alfresco/api/-default-/public/search/versions/1/search";
            case API_CORE_ALFRESCO:
                return "http://192.168.1.82:8080/alfresco/api/-default-/public/alfresco/versions/1";
            case API_SERVICE_ALFRESCO:
                return "http://192.168.1.82:8080/alfresco/service/api/node/workspace/SpacesStore/";
            default:
                return "";
        }
        /*switch (parameterName) {
            case BUSINESS_PLATFORM_ENDPOINT:
                return "http://192.168.3.1:8080/alfresco/api/-default-/public/cmis/versions/1.1/atom";
            case BUSINESS_PLATFORM_USER:
                return "admin";
            case BUSINESS_PLATFORM_PASS:
                return "admin";
            case BUSINESS_PLATFORM_RECORD:
                return "http://192.168.3.1:8080/alfresco/api/-default-/public/gs/versions/1";
            case API_SEARCH_ALFRESCO:
                return "http://192.168.3.1:8080/alfresco/api/-default-/public/search/versions/1/search";
            case API_CORE_ALFRESCO:
                return "http://192.168.3.1:8080/alfresco/api/-default-/public/alfresco/versions/1";
            case API_SERVICE_ALFRESCO:
                return "http://192.168.3.1:8080/alfresco/service/api/node/workspace/SpacesStore/";
            default:
                return "";*/
        //}
        //return System.getProperty(parameterName);
    }

}