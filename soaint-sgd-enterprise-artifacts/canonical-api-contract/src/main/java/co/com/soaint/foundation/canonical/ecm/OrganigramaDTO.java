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
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/ecm/organigrama/1.0.0")
public class OrganigramaDTO {

    private Long ideOrgaAdmin;
    private String codOrg;
    private String nomOrg;
    private String tipo;

    public OrganigramaDTO() {
    }

    public OrganigramaDTO(Long ideOrgaAdmin, String codOrg, String nomOrg, String tipo) {
        this.ideOrgaAdmin = ideOrgaAdmin;
        this.codOrg = codOrg;
        this.nomOrg = nomOrg;
        this.tipo = tipo;
    }
}
