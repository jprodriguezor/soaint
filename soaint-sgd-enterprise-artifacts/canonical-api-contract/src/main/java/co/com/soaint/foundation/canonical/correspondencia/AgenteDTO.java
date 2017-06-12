package co.com.soaint.foundation.canonical.correspondencia;

import lombok.Builder;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigInteger;

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
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/cor-agente/1.0.0")
public class AgenteDTO {
    private BigInteger ideAgente;
    private String codTipoRemite;
    private String codTipoPers;
    private String nombre;
    private String nroDocumentoIden;
    private String razonSocial;
    private String nit;
    private String codCortesia;
    private String codCargo;
    private String codEnCalidad;
    private String codTipDocIdent;
    private String nroDocuIdentidad;
    private String codSede;
    private String codDependencia;
    private String codFuncRemite;
    private String fecAsignacion;
    private Long ideContacto;
    private String codTipAgent;
    private String indOriginal;

    public AgenteDTO(){super();}

    public AgenteDTO(BigInteger ideAgente, String codTipoRemite, String codTipoPers, String nombre, String nroDocumentoIden,
                     String razonSocial, String nit, String codCortesia, String codCargo, String codEnCalidad,
                     String codTipDocIdent, String nroDocuIdentidad, String codSede, String codDependencia,
                     String codFuncRemite, String fecAsignacion, Long ideContacto, String codTipAgent, String indOriginal){
        this.ideAgente = ideAgente;
        this.codTipoRemite = codTipoRemite;
        this.codTipoPers = codTipoPers;
        this.nombre = nombre;
        this.nroDocumentoIden = nroDocumentoIden;
        this.razonSocial = razonSocial;
        this.nit = nit;
        this.codCortesia = codCortesia;
        this.codCargo = codCargo;
        this.codEnCalidad = codEnCalidad;
        this.codTipDocIdent = codTipDocIdent;
        this.nroDocuIdentidad = nroDocuIdentidad;
        this.codSede = codSede;
        this.codDependencia = codDependencia;
        this.codFuncRemite = codFuncRemite;
        this.fecAsignacion = fecAsignacion;
        this.ideContacto = ideContacto;
        this.codTipAgent = codTipAgent;
        this.indOriginal = indOriginal;
    }
}
