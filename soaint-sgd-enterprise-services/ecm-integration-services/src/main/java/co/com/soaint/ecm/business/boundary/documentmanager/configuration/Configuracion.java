package co.com.soaint.ecm.business.boundary.documentmanager.configuration;

import org.springframework.beans.factory.annotation.Value;

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







    /**
     * Metodo que dado el nombre del parametro devuelve el valor
     *
     * @param name Nombre del parametro
     * @return Retorna el valor de la propiedad que se pide
     */
    public static String getPropiedad(String name) {

        switch (name) {
            case "formatoNombreSerie":
                return formatoNombreSerie;
            case "formatoNombreSubserie":
                return formatoNombreSubserie;

            case "claseSubserie":
                return claseSubserie;

            case "claseSerie":
                return claseSerie;

            case "claseDependencia":
                return claseDependencia;
            case "claseBase":
                return claseBase;

            case "metadatoCodDependencia":
                return metadatoCodDependencia;

            case "metadatoCodBase":
                return metadatoCodBase;

            case "metadatoCodSubserie":
                return metadatoCodSubserie;

            case "metadatoCodSerie":
                return metadatoCodSerie;

            case "metadatoCodUnidadAdminParent":
                return metadatoCodUnidadAdminParent;

            default:
                return "";
        }

    }
}
