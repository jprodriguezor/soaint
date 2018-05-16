package co.com.soaint.foundation.canonical.correspondencia;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created:2-Jun-2017
 * Author: gyanet
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/cor-agente/1.0.0")
@ToString
public class AgenteFullDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private BigInteger ideAgente;
    private String codTipoRemite;
    private String descTipoRemite;
    private String codTipoPers;
    private String descTipoPers;
    private String nombre;
    private String razonSocial;
    private String nit;
    private String codCortesia;
    private String descCortesia;
    private String codEnCalidad;
    private String descEnCalidad;
    private String codTipDocIdent;
    private String descTipDocIdent;
    private String nroDocuIdentidad;
    private String codSede;
    private String descSede;
    private String codDependencia;
    private String descDependencia;
    private String codEstado;
    private String descEstado;
    private Date fecAsignacion;
    private String codTipAgent;
    private String descTipAgent;
    private String indOriginal;
    private Long numRedirecciones;
    private Long numDevoluciones;
    private List<DatosContactoFullDTO> datosContactoList;

}
