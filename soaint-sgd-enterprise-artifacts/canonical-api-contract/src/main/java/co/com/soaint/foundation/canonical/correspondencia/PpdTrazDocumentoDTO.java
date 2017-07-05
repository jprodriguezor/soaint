package co.com.soaint.foundation.canonical.correspondencia;

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
    private BigInteger ideTrazDocumento;
    private String observacion;
    private Long ideFunci;
    private String codEstado;
    private BigInteger ideDocumento;
    private String codOrgaAdmin;
}
