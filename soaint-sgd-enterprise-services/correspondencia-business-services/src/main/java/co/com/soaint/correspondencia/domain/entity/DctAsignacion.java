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
 *
 * @author jrodriguez
 */
@Data
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DCT_ASIGNACION")
@NamedQueries({
    @NamedQuery(name = "DctAsignacion.findAll", query = "SELECT d FROM DctAsignacion d"),
        @NamedQuery(name = "DctAsignacion.findByIdeAsignacion", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.DctAsignacionDTO " +
                "(d.ideAsignacion, d.fecAsignacion, d.ideFunci, d.codDependencia, d.codTipAsignacion, d.observaciones, d.codTipCausal, d.codTipProceso)" +
                "FROM DctAsignacion d " +
                "WHERE d.ideAsignacion = :IDE_ASIGNACION")})
@javax.persistence.TableGenerator(name = "DCT_ASIGNACION_GENERATOR", table = "TABLE_GENERATOR", pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE", pkColumnValue = "DCT_ASIGNACION_SEQ", allocationSize = 1)
public class DctAsignacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "DCT_ASIGNACION_GENERATOR")
    @Column(name = "IDE_ASIGNACION")
    private BigInteger ideAsignacion;
    @Basic(optional = false)
    @Column(name = "FEC_ASIGNACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecAsignacion;
    @Column(name = "IDE_FUNCI")
    private BigInteger ideFunci;
    @Column(name = "COD_DEPENDENCIA")
    private String codDependencia;
    @Basic(optional = false)
    @Column(name = "COD_TIP_ASIGNACION")
    private String codTipAsignacion;
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    @Column(name = "COD_TIP_CAUSAL")
    private String codTipCausal;
    @Basic(optional = false)
    @Column(name = "IDE_USUARIO_CREO")
    private String ideUsuarioCreo;
    @Basic(optional = false)
    @Column(name = "FEC_CREO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCreo;
    @Column(name = "COD_TIP_PROCESO")
    private String codTipProceso;
    @JoinColumn(name = "IDE_AGENTE", referencedColumnName = "IDE_AGENTE")
    @ManyToOne
    private CorAgente corAgente;
    @JoinColumn(name = "IDE_DOCUMENTO", referencedColumnName = "IDE_DOCUMENTO")
    @ManyToOne(optional = false)
    private CorCorrespondencia corCorrespondencia;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dctAsignacion")
    private List<DctAsigUltimo> dctAsigUltimoList;

}
