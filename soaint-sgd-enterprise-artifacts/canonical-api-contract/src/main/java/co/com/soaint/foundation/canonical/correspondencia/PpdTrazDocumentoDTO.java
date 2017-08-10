package co.com.soaint.foundation.canonical.correspondencia;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String codOrgaAdmin;
    private String nomFuncionario;
    private String valApellido1;
    private String valApellido2;
    private String corrElectronico;
    private String loginName;

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
}
