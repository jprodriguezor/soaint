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

/**
 * @author jrodriguez
 */
@Data
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DCT_ASIG_ULTIMO")
@NamedQueries({
        @NamedQuery(name = "DctAsigUltimo.findAll", query = "SELECT d FROM DctAsigUltimo d"),
        @NamedQuery(name = "DctAsigUltimo.findByIdeAgente", query = "SELECT NEW co.com.soaint.correspondencia.domain.entity.DctAsigUltimo " +
                "(d.ideAsigUltimo, d.numRedirecciones, d.ideUsuarioCreo, d.fecCreo, d.nivLectura, " +
                "d.nivEscritura, d.fechaVencimiento, d.idInstancia, d.codTipProceso) " +
                "FROM DctAsigUltimo d " +
                "INNER JOIN d.corAgente c " +
                "WHERE c.ideAgente = :IDE_AGENTE "),
        @NamedQuery(name = "DctAsigUltimo.findByIdeAsignacion", query = "SELECT NEW co.com.soaint.correspondencia.domain.entity.DctAsigUltimo " +
                "(d.ideAsigUltimo, d.numRedirecciones, d.ideUsuarioCreo, d.fecCreo, d.nivLectura, " +
                "d.nivEscritura, d.fechaVencimiento, d.idInstancia, d.codTipProceso) " +
                "FROM DctAsigUltimo d " +
                "INNER JOIN d.dctAsignacion a " +
                "WHERE a.ideAsignacion = :IDE_ASIGNACION "),
        @NamedQuery(name = "DctAsigUltimo.findByIdeFunciAndNroRadicado", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.AsignacionDTO " +
                "(a.ideAsignacion, a.fecAsignacion, a.ideFunci, a.codDependencia, a.codTipAsignacion, a.observaciones, a.codTipCausal, a.codTipProceso, " +
                "d.ideAsigUltimo, d.numRedirecciones, d.nivLectura, d.nivEscritura, d.fechaVencimiento, d.idInstancia, " +
                "g.ideAgente, " +
                "c.ideDocumento, c.nroRadicado) " +
                "FROM DctAsigUltimo d " +
                "INNER JOIN d.dctAsignacion a " +
                "INNER JOIN d.corCorrespondencia c " +
                "INNER JOIN d.corAgente g " +
                "WHERE a.ideFunci = :IDE_FUNCI AND d.idInstancia IS NULL AND (:NRO_RADICADO IS NULL OR c.nroRadicado LIKE :NRO_RADICADO)"),
        @NamedQuery(name = "DctAsigUltimo.findByIdeAgenteAndCodEstado", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.AsignacionDTO " +
                "(a.ideAsignacion, a.fecAsignacion, a.ideFunci, a.codDependencia, a.codTipAsignacion, a.observaciones, a.codTipCausal, a.codTipProceso, " +
                "d.ideAsigUltimo, d.numRedirecciones, d.nivLectura, d.nivEscritura, d.fechaVencimiento, d.idInstancia, " +
                "g.ideAgente, " +
                "c.ideDocumento, c.nroRadicado) " +
                "FROM DctAsigUltimo d " +
                "INNER JOIN d.dctAsignacion a " +
                "INNER JOIN d.corCorrespondencia c " +
                "INNER JOIN d.corAgente g " +
                "WHERE g.ideAgente = :IDE_AGENTE AND g.codEstado = :COD_ESTADO "),
        @NamedQuery(name = "DctAsigUltimo.consultarByIdeAgente", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.AsignacionDTO " +
                "(a.ideAsignacion, a.fecAsignacion, a.ideFunci, a.codDependencia, a.codTipAsignacion, a.observaciones, a.codTipCausal, a.codTipProceso, " +
                "d.ideAsigUltimo, d.numRedirecciones, d.nivLectura, d.nivEscritura, d.fechaVencimiento, d.idInstancia, " +
                "g.ideAgente, " +
                "c.ideDocumento, c.nroRadicado) " +
                "FROM DctAsigUltimo d " +
                "INNER JOIN d.dctAsignacion a " +
                "INNER JOIN d.corCorrespondencia c " +
                "INNER JOIN d.corAgente g " +
                "WHERE g.ideAgente = :IDE_AGENTE "),
        @NamedQuery(name = "DctAsigUltimo.updateIdInstancia", query = "UPDATE DctAsigUltimo d " +
                "SET d.idInstancia = :ID_INSTANCIA " +
                "WHERE d.ideAsigUltimo = :IDE_ASIG_ULTIMO"),
        @NamedQuery(name = "DctAsigUltimo.updateNumRedirecciones", query = "UPDATE DctAsigUltimo d " +
                "SET d.numRedirecciones = :NUM_REDIRECCIONES " +
                "WHERE d.ideAsigUltimo = :IDE_ASIG_ULTIMO"),
        @NamedQuery(name = "DctAsigUltimo.updateTipoProceso", query = "UPDATE DctAsigUltimo d " +
                "SET d.codTipProceso = :COD_TIPO_PROCESO " +
                "WHERE d.ideAsigUltimo = :IDE_ASIG_ULTIMO")})
@javax.persistence.TableGenerator(name = "DCT_ASIG_ULTIMO_GENERATOR", table = "TABLE_GENERATOR", pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE", pkColumnValue = "DCT_ASIG_ULTIMO_SEQ", allocationSize = 1)
public class DctAsigUltimo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "DCT_ASIG_ULTIMO_GENERATOR")
    @Column(name = "IDE_ASIG_ULTIMO")
    private BigInteger ideAsigUltimo;
    @Basic(optional = false)
    @Column(name = "IDE_USUARIO_CREO")
    private String ideUsuarioCreo;
    @Basic(optional = false)
    @Column(name = "FEC_CREO", insertable = true, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCreo;
    @Basic(optional = false)
    @Column(name = "IDE_USUARIO_CAMBIO")
    private long ideUsuarioCambio;
    @Basic(optional = false)
    @Column(name = "FEC_CAMBIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCambio;
    @Column(name = "NIV_LECTURA")
    private Short nivLectura;
    @Column(name = "NIV_ESCRITURA")
    private Short nivEscritura;
    @Column(name = "FECHA_VENCIMIENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVencimiento;
    @Column(name = "ID_INSTANCIA")
    private String idInstancia;
    @Column(name = "COD_TIP_PROCESO")
    private String codTipProceso;
    @JoinColumn(name = "IDE_AGENTE", referencedColumnName = "IDE_AGENTE")
    @ManyToOne(optional = false)
    private CorAgente corAgente;
    @JoinColumn(name = "IDE_DOCUMENTO", referencedColumnName = "IDE_DOCUMENTO")
    @ManyToOne(optional = false)
    private CorCorrespondencia corCorrespondencia;
    @JoinColumn(name = "IDE_ASIGNACION", referencedColumnName = "IDE_ASIGNACION")
    @ManyToOne(optional = false)
    private DctAsignacion dctAsignacion;

    /**
     * @param ideAsigUltimo
     * @param numRedirecciones
     * @param ideUsuarioCreo
     * @param fecCreo
     * @param nivLectura
     * @param nivEscritura
     * @param fechaVencimiento
     * @param idInstancia
     * @param codTipProceso
     */
    public DctAsigUltimo(BigInteger ideAsigUltimo, String numRedirecciones, String ideUsuarioCreo, Date fecCreo, Short nivLectura,
                         Short nivEscritura, Date fechaVencimiento, String idInstancia, String codTipProceso) {
        this.ideAsigUltimo = ideAsigUltimo;
        this.numRedirecciones = numRedirecciones;
        this.ideUsuarioCreo = ideUsuarioCreo;
        this.fecCreo = fecCreo;
        this.nivLectura = nivLectura;
        this.nivEscritura = nivEscritura;
        this.fechaVencimiento = fechaVencimiento;
        this.idInstancia = idInstancia;
        this.codTipProceso = codTipProceso;

    }

    @PrePersist
    protected void onCreate() {
        fecCreo = new Date();
        fecCambio = fecCreo;
    }

    @PreUpdate
    protected void onUpdate() {
        fecCambio = new Date();
    }

}
