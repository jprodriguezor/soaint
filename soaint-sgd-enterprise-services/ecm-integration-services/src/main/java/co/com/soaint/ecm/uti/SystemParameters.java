package co.com.soaint.ecm.uti;

public class SystemParameters {

    public static final String BUSINESS_PLATFORM_ENDPOINT = "ecm-endpoint";
    public static final String BUSINESS_PLATFORM_USER = "ecm-user";
    public static final String BUSINESS_PLATFORM_PASS = "ecm-pass";

    private SystemParameters() {
    }

    public static String getParameter(final String parameterName) {
        return System.getProperty(parameterName);
    }

}