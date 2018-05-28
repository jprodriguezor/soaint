package co.com.soaint.foundation.canonical.correspondencia;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
@ToString
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
    private BigInteger ideFunci = null;
    private String codTipDocIdent;
    private String nroDocuIdentidad;
    private String codSede;
    private String codDependencia;
    private String codEstado;
    private Date fecAsignacion;
    private String codTipAgent;
    private String indOriginal;
    private Long numRedirecciones;
    private Long numDevoluciones;
    private List<DatosContactoDTO> datosContactoList;

    /**
     *
     * @param ideAgente
     * @param codTipoRemite
     * @param codTipoPers
     * @param nombre
     * @param razonSocial
     * @param nit
     * @param codCortesia
     * @param codEnCalidad
     * @param ideFunci
     * @param codTipDocIdent
     * @param nroDocuIdentidad
     * @param codSede
     * @param codDependencia
     * @param codEstado
     * @param fecAsignacion
     * @param codTipAgent
     * @param indOriginal
     */
    public AgenteDTO(BigInteger ideAgente, String codTipoRemite, String codTipoPers, String nombre, String razonSocial,
                     String nit, String codCortesia, String codEnCalidad, BigInteger ideFunci, String codTipDocIdent, String nroDocuIdentidad,
                     String codSede, String codDependencia, String codEstado, Date fecAsignacion, String codTipAgent,
                     String indOriginal){
        this.ideAgente = ideAgente;
        this.codTipoRemite = codTipoRemite;
        this.codTipoPers = codTipoPers;
        this.nombre = nombre;
        this.razonSocial = razonSocial;
        this.nit = nit;
        this.codCortesia = codCortesia;
        this.codEnCalidad = codEnCalidad;
        this.ideFunci = ideFunci;
        this.codTipDocIdent = codTipDocIdent;
        this.nroDocuIdentidad = nroDocuIdentidad;
        this.codSede = codSede;
        this.codDependencia = codDependencia;
        this.codEstado = codEstado;
        this.fecAsignacion = fecAsignacion;
        this.codTipAgent = codTipAgent;
        this.indOriginal = indOriginal;
        this.datosContactoList = new ArrayList<>();
    }
    public AgenteDTO(BigInteger ideAgente, String codTipoRemite, String codTipoPers, String nombre, String razonSocial,
                     String nit, String codCortesia, String codEnCalidad, String codTipDocIdent, String nroDocuIdentidad,
                     String codSede, String codDependencia, String codEstado, Date fecAsignacion, String codTipAgent,
                     String indOriginal){
        this.ideAgente = ideAgente;
        this.codTipoRemite = codTipoRemite;
        this.codTipoPers = codTipoPers;
        this.nombre = nombre;
        this.razonSocial = razonSocial;
        this.nit = nit;
        this.codCortesia = codCortesia;
        this.codEnCalidad = codEnCalidad;
        this.codTipDocIdent = codTipDocIdent;
        this.nroDocuIdentidad = nroDocuIdentidad;
        this.codSede = codSede;
        this.codDependencia = codDependencia;
        this.codEstado = codEstado;
        this.fecAsignacion = fecAsignacion;
        this.codTipAgent = codTipAgent;
        this.indOriginal = indOriginal;
        this.datosContactoList = new ArrayList<>();
    }

    public AgenteDTO(BigInteger ideAgente, String codTipoRemite, String codTipoPers, String nombre, String razonSocial,
                     String nit, String codCortesia, String codEnCalidad, BigInteger ideFunci, String codTipDocIdent, String nroDocuIdentidad,
                     String codSede, String codDependencia, String codEstado, Date fecAsignacion, String codTipAgent,
                     String indOriginal, Long numRedirecciones, Long numDevoluciones) {
        this.ideAgente = ideAgente;
        this.codTipoRemite = codTipoRemite;
        this.codTipoPers = codTipoPers;
        this.nombre = nombre;
        this.razonSocial = razonSocial;
        this.nit = nit;
        this.codCortesia = codCortesia;
        this.codEnCalidad = codEnCalidad;
        this.ideFunci = ideFunci;
        this.codTipDocIdent = codTipDocIdent;
        this.nroDocuIdentidad = nroDocuIdentidad;
        this.codSede = codSede;
        this.codDependencia = codDependencia;
        this.codEstado = codEstado;
        this.fecAsignacion = fecAsignacion;
        this.codTipAgent = codTipAgent;
        this.indOriginal = indOriginal;
        this.numRedirecciones = numRedirecciones;
        this.numDevoluciones = numDevoluciones;
    }

}
