package co.com.soaint.ecm.business.boundary.documentmanager.configuration;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author sarias
 */
public final class Configuracion {

    @Value("${formatoNombreSerie}")
    private static String aformatoNombreSerie;
    @Value("${formatoNombreSubserie}")
    private static String aformatoNombreSubserie;
    @Value("${claseSubserie}")
    private static String aclaseSubserie;
    @Value("${claseSerie}")
    private static String aclaseSerie;

    @Value("${claseDependencia}")
    private static String aclaseDependencia;
    @Value("${claseBase}")
    private static String aclaseBase;
    @Value("${metadatoCodBase}")
    private static String ametadatoCodBase;
    @Value("${metadatoCodDependencia}")
    private static String ametadatoCodDependencia;

    @Value("${metadatoCodSubserie}")
    private static String ametadatoCodSubserie;
    @Value("${metadatoCodSerie}")
    private static String ametadatoCodSerie;
    @Value("${metadatoCodUnidadAdminParent}")
    private static String ametadatoCodUnidadAdminParent;
    @Value("${ecm}")
    private static String aecm;


    @Value("${ALFRESCO_ATOMPUB_URL}")
    private static String propiedadALFRESCO_ATOMPUB_URL;
    @Value("${REPOSITORY_ID}")
    private static String propiedadREPOSITORY_ID;
    @Value("${ALFRESCO_USER}")
    private static String propiedadALFRESCO_USER;
    @Value("${ALFRESCO_PASS}")
    private static String propiedadALFRESCO_PASS;

    /**
     * Metodo que dado el nombre del parametro devuelve el valor
     *
     * @param name Nombre del parametro
     * @return Retorna el valor de la propiedad que se pide
     */
    public static String getPropiedad(String name) {

        switch (name) {
            case "formatoNombreSerie":
                return aformatoNombreSerie != null ? aformatoNombreSerie : "1.2_3";
            case "formatoNombreSubserie":
                return aformatoNombreSubserie != null ? aformatoNombreSubserie : "1.2.4_5";

            case "claseSubserie":
                return aclaseSubserie != null ? aclaseSubserie : "CM_Subserie";

            case "claseSerie":
                return aclaseSerie != null ? aclaseSerie : "CM_Serie";

            case "claseDependencia":
                return aclaseDependencia != null ? aclaseDependencia : "CM_Unidad_Administrativa";
            case "claseBase":
                return aclaseBase != null ? aclaseBase : "CM_Unidad_Base";

            case "metadatoCodDependencia":
                return ametadatoCodDependencia != null ? ametadatoCodDependencia : "CodigoDependencia";

            case "metadatoCodBase":
                return ametadatoCodBase != null ? ametadatoCodBase : "CodigoBase";

            case "metadatoCodSubserie":
                return ametadatoCodSubserie != null ? ametadatoCodSubserie : "CodigoSubserie";

            case "metadatoCodSerie":
                return ametadatoCodSerie != null ? ametadatoCodSerie : "CodigoSerie";

            case "metadatoCodUnidadAdminParent":
                return ametadatoCodUnidadAdminParent != null ? ametadatoCodUnidadAdminParent : "CodUnidadPadre";
            case "ALFRESCO_ATOMPUB_URL":
                return propiedadALFRESCO_ATOMPUB_URL != null ? propiedadALFRESCO_ATOMPUB_URL : "http://192.168.1.82:8080/alfresco/api/-default-/public/cmis/versions/1.1/atom";
            case "REPOSITORY_ID":
                return propiedadREPOSITORY_ID != null ? propiedadREPOSITORY_ID : "-default-";
            case "ALFRESCO_USER":
                return propiedadALFRESCO_USER != null ? propiedadALFRESCO_USER : "admin";
            case "ALFRESCO_PASS":
                return propiedadALFRESCO_PASS != null ? propiedadALFRESCO_PASS : "admin";
            default:
                return "";
        }
//        private static Properties propiedades = null;
//        public static void inicializacion() {
//
//            if (propiedades == null) {
//                propiedades = new Properties ( );
//            }
//
//        }
//        static final Logger LOGGER = Logger.getLogger (Configuracion.class.getName ( ));
//        String propiedad = null;
//        try {
//            inicializacion ( );
//            propiedades.load (new FileInputStream ("ecm-integration-services/src/main/resources/configurationServices.properties"));
//            propiedad = propiedades.getProperty (name);
//        } catch (Exception e) {
//            e.printStackTrace ( );
//            LOGGER.info ("Error al leer properties de configuracion");
//        }
//        return propiedad;
    }
}
