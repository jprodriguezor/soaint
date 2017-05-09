/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soaint.correspondencia.domain.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author jrodriguez
 */
@Entity
@Table(name = "DCT_ASIGNACION")
@NamedQueries({
    @NamedQuery(name = "DctAsignacion.findAll", query = "SELECT d FROM DctAsignacion d")})
public class DctAsignacion implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "IDE_ASIGNACION")
    private BigDecimal ideAsignacion;
    @Basic(optional = false)
    @Column(name = "FEC_ASIGNACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecAsignacion;
    @Column(name = "IDE_DEPENDENCIA")
    private BigInteger ideDependencia;
    @Basic(optional = false)
    @Column(name = "COD_TIP_ASIGNACION")
    private String codTipAsignacion;
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    @Column(name = "COD_TIP_CAUSAL")
    private String codTipCausal;
    @Basic(optional = false)
    @Column(name = "IDE_USUARIO_CREO")
    private long ideUsuarioCreo;
    @Basic(optional = false)
    @Column(name = "FEC_CREO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCreo;
    @Column(name = "COD_TIP_PROCESO")
    private String codTipProceso;
    @JoinColumn(name = "IDE_AGENTE", referencedColumnName = "IDE_AGENTE")
    @ManyToOne
    private CorAgente ideAgente;
    @JoinColumn(name = "IDE_DOCUMENTO", referencedColumnName = "IDE_DOCUMENTO")
    @ManyToOne(optional = false)
    private CorCorrespondencia ideDocumento;
    @JoinColumn(name = "IDE_FUNCI", referencedColumnName = "IDE_FUNCI")
    @ManyToOne
    private Funcionarios ideFunci;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideAsignacion")
    private List<DctAsigUltimo> dctAsigUltimoList;

    public DctAsignacion() {
    }

    public DctAsignacion(BigDecimal ideAsignacion) {
        this.ideAsignacion = ideAsignacion;
    }

    public DctAsignacion(BigDecimal ideAsignacion, Date fecAsignacion, String codTipAsignacion, long ideUsuarioCreo, Date fecCreo) {
        this.ideAsignacion = ideAsignacion;
        this.fecAsignacion = fecAsignacion;
        this.codTipAsignacion = codTipAsignacion;
        this.ideUsuarioCreo = ideUsuarioCreo;
        this.fecCreo = fecCreo;
    }

    public BigDecimal getIdeAsignacion() {
        return ideAsignacion;
    }

    public void setIdeAsignacion(BigDecimal ideAsignacion) {
        this.ideAsignacion = ideAsignacion;
    }

    public Date getFecAsignacion() {
        return fecAsignacion;
    }

    public void setFecAsignacion(Date fecAsignacion) {
        this.fecAsignacion = fecAsignacion;
    }

    public BigInteger getIdeDependencia() {
        return ideDependencia;
    }

    public void setIdeDependencia(BigInteger ideDependencia) {
        this.ideDependencia = ideDependencia;
    }

    public String getCodTipAsignacion() {
        return codTipAsignacion;
    }

    public void setCodTipAsignacion(String codTipAsignacion) {
        this.codTipAsignacion = codTipAsignacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getCodTipCausal() {
        return codTipCausal;
    }

    public void setCodTipCausal(String codTipCausal) {
        this.codTipCausal = codTipCausal;
    }

    public long getIdeUsuarioCreo() {
        return ideUsuarioCreo;
    }

    public void setIdeUsuarioCreo(long ideUsuarioCreo) {
        this.ideUsuarioCreo = ideUsuarioCreo;
    }

    public Date getFecCreo() {
        return fecCreo;
    }

    public void setFecCreo(Date fecCreo) {
        this.fecCreo = fecCreo;
    }

    public String getCodTipProceso() {
        return codTipProceso;
    }

    public void setCodTipProceso(String codTipProceso) {
        this.codTipProceso = codTipProceso;
    }

    public CorAgente getIdeAgente() {
        return ideAgente;
    }

    public void setIdeAgente(CorAgente ideAgente) {
        this.ideAgente = ideAgente;
    }

    public CorCorrespondencia getIdeDocumento() {
        return ideDocumento;
    }

    public void setIdeDocumento(CorCorrespondencia ideDocumento) {
        this.ideDocumento = ideDocumento;
    }

    public Funcionarios getIdeFunci() {
        return ideFunci;
    }

    public void setIdeFunci(Funcionarios ideFunci) {
        this.ideFunci = ideFunci;
    }

    public List<DctAsigUltimo> getDctAsigUltimoList() {
        return dctAsigUltimoList;
    }

    public void setDctAsigUltimoList(List<DctAsigUltimo> dctAsigUltimoList) {
        this.dctAsigUltimoList = dctAsigUltimoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAsignacion != null ? ideAsignacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DctAsignacion)) {
            return false;
        }
        DctAsignacion other = (DctAsignacion) object;
        if ((this.ideAsignacion == null && other.ideAsignacion != null) || (this.ideAsignacion != null && !this.ideAsignacion.equals(other.ideAsignacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.soaint.correspondencia.domain.entity.DctAsignacion[ ideAsignacion=" + ideAsignacion + " ]";
    }
    
}
