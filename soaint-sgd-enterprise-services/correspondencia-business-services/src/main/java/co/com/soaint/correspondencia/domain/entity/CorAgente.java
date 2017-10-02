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

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * @author jrodriguez
 */
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "COR_AGENTE")
@NamedQueries({
        @NamedQuery(name = "CorAgente.findAll", query = "SELECT c FROM CorAgente c"),
        @NamedQuery(name = "CorAgente.findByIdeDocumento", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.AgenteDTO " +
                "(c.ideAgente, c.codTipoRemite, c.codTipoPers, c.nombre, c.razonSocial, c.nit, c.codCortesia, " +
                "c.codEnCalidad, c.codTipDocIdent, c.nroDocuIdentidad, c.codSede, c.codDependencia, " +
                "c.codEstado, c.fecAsignacion, c.codTipAgent, c.indOriginal) " +
                "FROM CorAgente c INNER JOIN c.corCorrespondencia co " +
                "WHERE co.ideDocumento = :IDE_DOCUMENTO"),
        @NamedQuery(name = "CorAgente.findByIdeAgente", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.AgenteDTO " +
                "(c.ideAgente, c.codTipoRemite, c.codTipoPers, c.nombre, c.razonSocial, c.nit, c.codCortesia, " +
                "c.codEnCalidad, c.codTipDocIdent, c.nroDocuIdentidad, c.codSede, c.codDependencia, " +
                "c.codEstado, c.fecAsignacion, c.codTipAgent, c.indOriginal) " +
                "FROM CorAgente c INNER JOIN c.corCorrespondencia co " +
                "WHERE c.ideAgente = :IDE_AGENTE"),
        @NamedQuery(name = "CorAgente.findByIdeDocumentoAndCodDependenciaAndCodEstado", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.AgenteDTO " +
                "(c.ideAgente, c.codTipoRemite, c.codTipoPers, c.nombre, c.razonSocial, c.nit, c.codCortesia, " +
                "c.codEnCalidad, c.codTipDocIdent, c.nroDocuIdentidad, c.codSede, c.codDependencia, " +
                "c.codEstado, c.fecAsignacion, c.codTipAgent, c.indOriginal) " +
                "FROM CorAgente c INNER JOIN c.corCorrespondencia co " +
                "WHERE (:COD_ESTADO IS NULL OR c.codEstado = :COD_ESTADO) AND c.codDependencia = :COD_DEPENDENCIA AND c.codTipAgent = :COD_TIP_AGENT " +
                "AND co.ideDocumento = :IDE_DOCUMENTO"),
        @NamedQuery(name = "CorAgente.findByIdeDocumentoAndCodTipoAgente", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.AgenteDTO " +
                "(c.ideAgente, c.codTipoRemite, c.codTipoPers, c.nombre, c.razonSocial, c.nit, c.codCortesia, " +
                "c.codEnCalidad, c.codTipDocIdent, c.nroDocuIdentidad, c.codSede, c.codDependencia, " +
                "c.codEstado, c.fecAsignacion, c.codTipAgent, c.indOriginal) " +
                "FROM CorAgente c INNER JOIN c.corCorrespondencia co " +
                "WHERE c.codTipAgent = :COD_TIP_AGENT " +
                "AND co.ideDocumento = :IDE_DOCUMENTO"),
        @NamedQuery(name = "CorAgente.countByIdeAgente", query = "SELECT COUNT(*) " +
                "FROM CorAgente c " +
                "WHERE c.ideAgente = :IDE_AGENTE"),
        @NamedQuery(name = "CorAgente.updateAsignacion", query = "UPDATE CorAgente c " +
                "SET c.fecAsignacion = :FECHA_ASIGNACION, c.codEstado = :COD_ESTADO " +
                "WHERE c.ideAgente = :IDE_AGENTE"),
        @NamedQuery(name = "CorAgente.redireccionarCorrespondencia", query = "UPDATE CorAgente c " +
                "SET c.codSede = :COD_SEDE, c.codDependencia = :COD_DEPENDENCIA " +
                "WHERE c.ideAgente = :IDE_AGENTE"),
        @NamedQuery(name = "CorAgente.updateEstado", query = "UPDATE CorAgente c " +
                "SET c.codEstado = :COD_ESTADO " +
                "WHERE c.ideAgente = :IDE_AGENTE")})
@javax.persistence.TableGenerator(name = "COR_AGENTE_GENERATOR", table = "TABLE_GENERATOR", pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE", pkColumnValue = "COR_AGENTE_SEQ", allocationSize = 1)
public class CorAgente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "COR_AGENTE_GENERATOR")
    @Column(name = "IDE_AGENTE")
    private BigInteger ideAgente;
    @Column(name = "COD_TIPO_REMITE")
    private String codTipoRemite;
    @Column(name = "COD_TIPO_PERS")
    private String codTipoPers;
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "RAZON_SOCIAL")
    private String razonSocial;
    @Column(name = "NIT")
    private String nit;
    @Column(name = "COD_CORTESIA")
    private String codCortesia;
    @Column(name = "COD_EN_CALIDAD")
    private String codEnCalidad;
    @Column(name = "COD_TIP_DOC_IDENT")
    private String codTipDocIdent;
    @Column(name = "NRO_DOCU_IDENTIDAD")
    private String nroDocuIdentidad;
    @Column(name = "COD_SEDE")
    private String codSede;
    @Column(name = "COD_DEPENDENCIA")
    private String codDependencia;
    @Column(name = "COD_ESTADO")
    private String codEstado;
    @Column(name = "FEC_ASIGNACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecAsignacion;
    @Column(name = "COD_TIP_AGENT")
    private String codTipAgent;
    @Column(name = "IND_ORIGINAL")
    private String indOriginal;
    @Column(name = "NUM_REDIRECCIONES")
    private Long numRedirecciones;
    @Column(name = "NUM_DEVOLUCIONES")
    private Long numDevoluciones;
    @Column(name = "FEC_CREACION", insertable = true, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCreacion;
    @JoinColumn(name = "IDE_DOCUMENTO", referencedColumnName = "IDE_DOCUMENTO")
    @ManyToOne
    private CorCorrespondencia corCorrespondencia;
    @OneToMany(mappedBy = "corAgente")
    private List<DctAsignacion> dctAsignacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "corAgente")
    private List<CorPlanAgen> corPlanAgenList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "corAgente")
    private List<TvsDatosContacto> tvsDatosContactoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "corAgente")
    private List<DctAsigUltimo> dctAsigUltimoList;

    @PrePersist
    protected void onCreate(){
        fecCreacion = new Date();
    }
}
