package co.com.soaint.foundation.canonical.correspondencia;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

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

    @JsonProperty("id")
    private BigInteger ideFunci;
    private String codTipDocIdent;
    private String nroIdentificacion;
    @JsonProperty("nombre")
    private String nomFuncionario;
    private String valApellido1;
    private String valApellido2;
    private String corrElectronico;
    private String loginName;
    private String estado;
    private List<DependenciaDTO> dependencias;
    private List<RolDTO> roles;
    private String password;
    private String usuarioCrea;

    public FuncionarioDTO(BigInteger ideFunci, String codTipDocIdent, String nroIdentificacion, String nomFuncionario,
                          String valApellido1, String valApellido2, String corrElectronico,
                          String loginName, String estado) {
        this.ideFunci = ideFunci;
        this.codTipDocIdent = codTipDocIdent;
        this.nroIdentificacion = nroIdentificacion;
        this.nomFuncionario = nomFuncionario;
        this.valApellido1 = valApellido1;
        this.valApellido2 = valApellido2;
        this.corrElectronico = corrElectronico;
        this.loginName = loginName;
        this.estado = estado;

    }
    /*
    public String getNombreCompleto(){
        return this.getNomFuncionario() + " " + this.getValApellido1() + " " + this.getValApellido2();
    }
    */
}
