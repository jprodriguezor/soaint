package co.com.soaint.foundation.canonical.ecm;

import lombok.*;

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
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(includeFieldNames = false, of = "nomOrg")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/ecm/organigrama/1.0.0")
public class OrganigramaDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    private Long ideOrgaAdmin;
    private String codOrg;
    private String nomOrg;
    private String tipo;

    @Builder(toBuilder = true, builderMethodName = "newInstance")
    public OrganigramaDTO(String codigoBase, String nombreBase, Long ideOrgaAdmin,
                          String codOrg, String nomOrg, String tipo) {
        super(codigoBase, nombreBase);
        this.ideOrgaAdmin = ideOrgaAdmin;
        this.codOrg = codOrg;
        this.nomOrg = nomOrg;
        this.tipo = tipo;
    }
}
