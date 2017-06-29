package co.com.soaint.foundation.canonical.correspondencia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created:15-Jun-2017
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
public class FuncionarioDTO  implements Serializable {

    private static final long serialVersionUID = 1L;
    private BigInteger ideFunci;
    private String codTipDocIdent;
    private String nroIdentificacion;
    private String nomFuncionario;
    private String valApellido1;
    private String valApellido2;
    private String codCargo;
    private String corrElectronico;
    private String codOrgaAdmi;
    private String loginName;
    private String estado;
    private OrganigramaItemDTO sede;
    private OrganigramaItemDTO dependencia;

    public FuncionarioDTO(BigInteger ideFunci, String codTipDocIdent, String nroIdentificacion, String nomFuncionario,
                          String valApellido1, String valApellido2, String codCargo, String corrElectronico,
                          String codOrgaAdmi, String loginName, String estado){
        this.ideFunci = ideFunci;
        this.codTipDocIdent = codTipDocIdent;
        this.nroIdentificacion = nroIdentificacion;
        this.nomFuncionario = nomFuncionario;
        this.valApellido1 = valApellido1;
        this.valApellido2 = valApellido2;
        this.codCargo = codCargo;
        this.corrElectronico = corrElectronico;
        this.codOrgaAdmi = codOrgaAdmi;
        this.loginName = loginName;
        this.estado = estado;

    }
}
