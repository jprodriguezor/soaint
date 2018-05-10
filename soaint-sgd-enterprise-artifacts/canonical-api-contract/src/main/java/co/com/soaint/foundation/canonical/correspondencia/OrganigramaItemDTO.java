package co.com.soaint.foundation.canonical.correspondencia;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created:22-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Data
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/cor-agente/1.0.0")
@ToString
public class OrganigramaItemDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private BigInteger ideOrgaAdmin;
    @JsonProperty("codigo")
    private String codOrg;
    @JsonProperty("nombre")
    private String nomOrg;
    private String idOrgaAdminPadre;
    private String refPadre;
    private String nomPadre;
    private String estado;
    private String nivel;
    private String descOrg;
    private String nivelPadre;

    public OrganigramaItemDTO(BigInteger ideOrgaAdmin, String codOrg, String nomOrg, String idOrgaAdminPadre, String estado, String descOrg, String nivel) {
        this.ideOrgaAdmin = ideOrgaAdmin;
        this.codOrg = codOrg;
        this.nomOrg = nomOrg;
        this.idOrgaAdminPadre = idOrgaAdminPadre;
        this.estado = estado;
        this.descOrg = descOrg;
        this.nivel = nivel;
    }

    public OrganigramaItemDTO(BigInteger ideOrgaAdmin, String codOrg, String nomOrg, String estado, String descOrg, String nivel) {
        this.ideOrgaAdmin = ideOrgaAdmin;
        this.codOrg = codOrg;
        this.nomOrg = nomOrg;
        this.estado = estado;
        this.descOrg = descOrg;
        this.nivel = nivel;
    }

    public OrganigramaItemDTO(BigInteger ideOrgaAdmin, String codOrg, String nomOrg, String idOrgaAdminPadre, String nomPadre,
                             String estado, String nivel, String descOrg, String nivelPadre) {
        this.ideOrgaAdmin = ideOrgaAdmin;
        this.codOrg = codOrg;
        this.nomOrg = nomOrg;
        this.idOrgaAdminPadre = idOrgaAdminPadre;
        this.nomPadre = nomPadre;
        this.estado = estado;
        this.nivel = nivel;
        this.descOrg = descOrg;
        this.nivelPadre = nivelPadre;
    }
}
