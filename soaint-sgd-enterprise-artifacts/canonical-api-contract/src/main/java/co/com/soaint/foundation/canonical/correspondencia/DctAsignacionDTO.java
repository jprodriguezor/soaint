package co.com.soaint.foundation.canonical.correspondencia;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created:6-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@Data
@Builder(builderMethodName = "newInstance")
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/dct-asignacion/1.0.0")
@ToString
public class DctAsignacionDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private BigInteger ideAsignacion;
    private Date fecAsignacion;
    private BigInteger ideFunci;
    private String codDependencia;
    private String codTipAsignacion;
    private String observaciones;
    private String codTipCausal;
    private String codTipProceso;

}
