package co.com.soaint.foundation.canonical.ecm;

import lombok.Builder;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created:4-May-2017
 * Author: jrodriguez
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Data
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/ecm/contenidoDependencia/1.0.0")
public class ContenidoDependenciaTrdDTO {

    private String idOrgAdm;
    private String idOrgOfc;
    private String codSerie;
    private String nomSerie;
    private String codSubSerie;
    private String nomSubSerie;
    private Long retArchivoGestion;
    private Long retArchivoCentral;
    private String procedimiento;
    private int diposicionFinal;


    public ContenidoDependenciaTrdDTO() {
        super();
    }

    public ContenidoDependenciaTrdDTO(String idOrgAdm, String idOrgOfc, String codSerie, String nomSerie, String codSubSerie,
                                      String nomSubSerie, Long retArchivoGestion, Long retArchivoCentral, String procedimiento,
                                      int diposicionFinal) {
        this.idOrgAdm = idOrgAdm;
        this.idOrgOfc = idOrgOfc;
        this.codSerie = codSerie;
        this.nomSerie = nomSerie;
        this.codSubSerie = codSubSerie;
        this.nomSubSerie = nomSubSerie;
        this.retArchivoGestion = retArchivoGestion;
        this.retArchivoCentral = retArchivoCentral;
        this.procedimiento = procedimiento;
        this.diposicionFinal = diposicionFinal;
    }
}