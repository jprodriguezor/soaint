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
 * Created:2-Jun-2017
 * Author: jrodriguez
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/cor-agente/1.0.0")
public class AgenteDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private BigInteger ideAgente;
    private String codTipoRemite;
    private String codTipoPers;
    private String nombre;
    private String razonSocial;
    private String nit;
    private String codCortesia;
    private String codEnCalidad;
    private String codTipDocIdent;
    private String nroDocuIdentidad;
    private String codSede;
    private String codDependencia;
    private String codEstado;
    private Date fecAsignacion;
    private String codTipAgent;
    private String indOriginal;

}
