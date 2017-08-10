package co.com.soaint.ecm.business.boundary.documentmanager.configuration;

import org.springframework.beans.factory.annotation.Value;

import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author sarias
 */
public final class Configuracion {

    @Value("${formatoNombreSerie}")
    private static String formatoNombreSerie;
    @Value("${formatoNombreSubserie}")
    private static String formatoNombreSubserie;
    @Value("${claseSubserie}")
    private static String claseSubserie;
    @Value("${claseSerie}")
    private static String claseSerie;

    @Value("${claseDependencia}")
    private static String claseDependencia;
    @Value("${claseBase}")
    private static String claseBase;
    @Value("${metadatoCodBase}")
    private static String metadatoCodBase;
    @Value("${metadatoCodDependencia}")
    private static String metadatoCodDependencia;

    @Value("${metadatoCodSubserie}")
    private static String metadatoCodSubserie;
    @Value("${metadatoCodSerie}")
    private static String metadatoCodSerie;
    @Value("${metadatoCodUnidadAdminParent}")
    private static String metadatoCodUnidadAdminParent;
    @Value("${ecm}")
    private static String ecm;


    private static Properties propiedades = null;
    static final Logger LOGGER = Logger.getLogger (Configuracion.class.getName ( ));

    public static void inicializacion() {

        if (propiedades == null) {
            propiedades = new Properties ( );
        }

    }

    /**
     * Metodo que dado el nombre del parametro devuelve el valor
     *
     * @param name Nombre del parametro
     * @return Retorna el valor de la propiedad que se pide
     */
    public static String getPropiedad(String name) {

        switch (name) {
            case "formatoNombreSerie":
                return "1.2_3";
            case "formatoNombreSubserie":
                return "1.2.4_5";

            case "claseSubserie":
                return "CM_Subserie";

            case "claseSerie":
                return "CM_Serie";

            case "claseDependencia":
                return "CM_Unidad_Administrativa";
            case "claseBase":
                return "CM_Unidad_Base";

            case "metadatoCodDependencia":
                return "CodigoDependencia";

            case "metadatoCodBase":
                return "CodigoBase";

            case "metadatoCodSubserie":
                return "CodigoSubserie";

            case "metadatoCodSerie":
                return "CodigoSerie";

            case "metadatoCodUnidadAdminParent":
                return "CodUnidadPadre";

            default:
                return "";
        }


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
