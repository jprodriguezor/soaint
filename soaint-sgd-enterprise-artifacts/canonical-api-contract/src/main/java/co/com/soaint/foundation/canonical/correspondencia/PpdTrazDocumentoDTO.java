package co.com.soaint.foundation.canonical.correspondencia;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

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
@ToString
public class PpdTrazDocumentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    @JsonProperty("id")
    private BigInteger ideTrazDocumento;
    @JsonProperty("fecha")
    private Date fecTrazDocumento;
    private String observacion;
    private BigInteger ideFunci;
    @JsonProperty("estado")
    private String codEstado;
    private BigInteger ideDocumento;
    @JsonProperty("codDependencia")
    private String codOrgaAdmin;
    private String nomFuncionario;
    private String valApellido1;
    private String valApellido2;
    private String corrElectronico;
    private String loginName;
    private String nomDependencia;

    public PpdTrazDocumentoDTO(BigInteger ideTrazDocumento, Date fecTrazDocumento, String observacion, BigInteger ideFunci,
                               String codEstado, BigInteger ideDocumento, String codOrgaAdmin){
        this.ideTrazDocumento = ideTrazDocumento;
        this.fecTrazDocumento = fecTrazDocumento;
        this.observacion = observacion;
        this.ideFunci = ideFunci;
        this.codEstado = codEstado;
        this.ideDocumento = ideDocumento;
        this.codOrgaAdmin = codOrgaAdmin;
    }

    public PpdTrazDocumentoDTO(BigInteger ideTrazDocumento, Date fecTrazDocumento, String observacion, BigInteger ideFunci,
                               String codEstado, BigInteger ideDocumento, String codOrgaAdmin, String nomFuncionario,
                               String valApellido1, String valApellido2, String corrElectronico, String loginName){
        this.ideTrazDocumento = ideTrazDocumento;
        this.fecTrazDocumento = fecTrazDocumento;
        this.observacion = observacion;
        this.ideFunci = ideFunci;
        this.codEstado = codEstado;
        this.ideDocumento = ideDocumento;
        this.codOrgaAdmin = codOrgaAdmin;
        this.nomFuncionario = nomFuncionario;
        this.valApellido1 = valApellido1;
        this.valApellido2 = valApellido2;
        this.corrElectronico = corrElectronico;
        this.loginName = loginName;
    }
}
