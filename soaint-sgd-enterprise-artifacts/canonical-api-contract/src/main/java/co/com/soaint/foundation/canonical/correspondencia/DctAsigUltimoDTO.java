package co.com.soaint.foundation.canonical.correspondencia;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created:2-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/dct-asig-ultimo/1.0.0")
@ToString
public class DctAsigUltimoDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private BigInteger ideAsigUltimo;
    private Short nivLectura;
    private Short nivEscritura;
    private Date fechaVencimiento;
    private String idInstancia;
    private String codTipProceso;
    private BigInteger ideAsignacion;

}
