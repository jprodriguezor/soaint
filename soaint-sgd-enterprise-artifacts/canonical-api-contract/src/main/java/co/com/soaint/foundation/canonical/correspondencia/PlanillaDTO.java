package co.com.soaint.foundation.canonical.correspondencia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created:4-Sep-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/cor-planillas/1.0.0")
public class PlanillaDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private BigInteger idePlanilla;
    private String nroPlanilla;
    private Date fecGeneracion;
    private String codTipoPlanilla;
    private String codFuncGenera;
    private String codSedeOrigen;
    private String codDependenciaOrigen;
    private String codSedeDestino;
    private String codDependenciaDestino;
    private String codClaseEnvio;
    private String codModalidadEnvio;
    private List<PlanAgenDTO> planAgenList;
}
