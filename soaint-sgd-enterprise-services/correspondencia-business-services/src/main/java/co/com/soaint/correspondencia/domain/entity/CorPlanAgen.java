/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soaint.correspondencia.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author jrodriguez
 */
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "COR_PLAN_AGEN")
@NamedQueries({
    @NamedQuery(name = "CorPlanAgen.findAll", query = "SELECT c FROM CorPlanAgen c"),
        @NamedQuery(name = "CorPlanAgen.findByIdePlanilla", query = "SELECT   NEW co.com.soaint.foundation.canonical.correspondencia.PlanAgenDTO " +
                "(c.idePlanAgen, c.estado, c.varPeso, c.varValor, c.varNumeroGuia, c.fecObservacion, c.codNuevaSede, " +
                "c.codNuevaDepen, c.observaciones, c.codCauDevo, c.fecCarguePla, a.ideAgente, co.ideDocumento, " +
                "co.nroRadicado ) " +
                "FROM CorPlanAgen c " +
                "INNER JOIN c.corCorrespondencia co " +
                "INNER JOIN c.corAgente a " +
                "INNER JOIN c.corPlanillas cp " +
                "WHERE cp.idePlanilla = :IDE_PLANILLA"),
        @NamedQuery(name = "CorPlanAgen.updateEstadoDistribucion", query = "UPDATE CorPlanAgen c " +
                "SET c.estado = :ESTADO, c.varPeso = :VAR_PESO, c.varValor = :VAR_VALOR, c.varNumeroGuia = :VAR_NUMERO_GUIA, " +
                "c.fecObservacion = :FEC_OBSERVACION, c.codNuevaSede = :COD_NUEVA_SEDE, c.codNuevaDepen = :COD_NUEVA_DEPEN, " +
                "c.observaciones = :OBSERVACIONES, c.codCauDevo = :COD_CAU_DEVO, c.fecCarguePla = :FEC_CARGUE_PLA " +
                "WHERE c.idePlanAgen = :IDE_PLAN_AGEN")})
@javax.persistence.TableGenerator(name = "COR_PLAN_AGEN_GENERATOR", table = "TABLE_GENERATOR", pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE", pkColumnValue = "COR_PLAN_AGEN_SEQ", allocationSize = 1)
public class CorPlanAgen implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "COR_PLAN_AGEN_GENERATOR")
    @Column(name = "IDE_PLAN_AGEN")
    private BigInteger idePlanAgen;
    @Basic(optional = false)
    @Column(name = "ESTADO")
    private String estado;
    @Column(name = "VAR_PESO")
    private String varPeso;
    @Column(name = "VAR_VALOR")
    private String varValor;
    @Column(name = "VAR_NUMERO_GUIA")
    private String varNumeroGuia;
    @Column(name = "FEC_OBSERVACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecObservacion;
    @Column(name = "COD_NUEVA_SEDE")
    private String codNuevaSede;
    @Column(name = "COD_NUEVA_DEPEN")
    private String codNuevaDepen;
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    @Column(name = "COD_CAU_DEVO")
    private String codCauDevo;
    @Column(name = "FEC_CARGUE_PLA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCarguePla;
    @JoinColumn(name = "IDE_AGENTE", referencedColumnName = "IDE_AGENTE")
    @ManyToOne(optional = false)
    private CorAgente corAgente;
    @JoinColumn(name = "IDE_DOCUMENTO", referencedColumnName = "IDE_DOCUMENTO")
    @ManyToOne(optional = false)
    private CorCorrespondencia corCorrespondencia;
    @JoinColumn(name = "IDE_PLANILLA", referencedColumnName = "IDE_PLANILLA")
    @ManyToOne(optional = false)
    private CorPlanillas corPlanillas;
    
}
