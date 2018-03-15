package co.com.soaint.ecm.business.boundary.documentmanager.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author sarias
 */
@Component
public class Configuracion {

    @Value("${formatoNombreSerie}")
    private String aformatoNombreSerie;
    @Value("${formatoNombreSubserie}")
    private String aformatoNombreSubserie;
    @Value("${claseSubserie}")
    private String aclaseSubserie;
    @Value("${claseSerie}")
    private String aclaseSerie;
    @Value("${claseDependencia}")
    private String aclaseDependencia;
    @Value("${claseUnidadDocumental}")
    private String aclaseUnidadDocumental;
    @Value("${claseBase}")
    private String aclaseBase;
    @Value("${metadatoCodBase}")
    private String ametadatoCodBase;
    @Value("${metadatoCodDependencia}")
    private String ametadatoCodDependencia;
    @Value("${metadatoCodSubserie}")
    private String ametadatoCodSubserie;
    @Value("${metadatoCodSerie}")
    private String ametadatoCodSerie;
    @Value("${metadatoCodUnidadAdminParent}")
    private String ametadatoCodUnidadAdminParent;
    @Value("${ecm}")
    private String aecm;
    @Value("${REPOSITORY_ID}")
    private String aRepositoryId;
    @Value("${errorSubirDocumento}")
    private String aerrorSubirDocumento;
    @Value("${errorSubirDocumentoDuplicado}")
    private String aerrorSubirDocumentoDuplicado;
    @Value("${comunicacionInterna}")
    private String acomunicacionInterna;
    @Value("${comunicacionExterna}")
    private String acomunicacionExterna;
    @Value("${carpetaPlantilla}")
    private String acarpetaPlantilla;


    private Configuracion(){
        /*
         *a
          */
    }
    /**
     * Metodo que dado el nombre del parametro devuelve el valor
     *
     * @param name Nombre del parametro
     * @return Retorna el valor de la propiedad que se pide
     */
    public String getPropiedad(String name) {

        switch (name) {
            case "formatoNombreSerie":
                return aformatoNombreSerie != null ? aformatoNombreSerie : "1.2_3";
            case "formatoNombreSubserie":
                return aformatoNombreSubserie != null ? aformatoNombreSubserie : "1.2.4_5";

            case "claseSubserie":
                return aclaseSubserie != null ? aclaseSubserie : "CM_Subserie";

            case "claseSerie":
                return aclaseSerie != null ? aclaseSerie : "CM_Serie";

            case "claseUnidadDocumental":
                return aclaseUnidadDocumental != null ? aclaseUnidadDocumental : "CM_Unidad_Documental";

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
            case "REPOSITORY_ID":
                return aRepositoryId != null ? aRepositoryId : "-default-";
            case "ECM_ERROR":
                return aerrorSubirDocumento != null ? aerrorSubirDocumento : "ECMError";
            case "ECM_ERROR_DUPLICADO":
                return aerrorSubirDocumentoDuplicado != null ? aerrorSubirDocumentoDuplicado : "ECMErrorDuplicado";
            case "comunicacionInterna":
                return acomunicacionInterna != null ? acomunicacionInterna : "EI";
            case "comunicacionExterna":
                return acomunicacionExterna != null ? acomunicacionExterna : "EE";
            case "carpetaPlantilla":
                return acarpetaPlantilla != null ? acarpetaPlantilla : "100100.00303_PLANTILLAS";
            default:
                return "";
        }
    }
}
