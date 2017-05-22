/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soaint.correspondencia.domain.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author jrodriguez
 */
@Entity
@Table(name = "PPD_TRAZ_DOCUMENTO")
@NamedQueries({
    @NamedQuery(name = "PpdTrazDocumento.findAll", query = "SELECT p FROM PpdTrazDocumento p")})
public class PpdTrazDocumento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDE_TRAZ_DOCUMENTO")
    private Long ideTrazDocumento;
    @Basic(optional = false)
    @Column(name = "FEC_TRAZ_DOCUMENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecTrazDocumento;
    @Column(name = "OBSERVACION")
    private String observacion;
    @Column(name = "IDE_FUNCI")
    private Long ideFunci;
    @Column(name = "COD_ESTADO")
    private String codEstado;
    @Column(name = "IDE_DOCUMENTO")
    private Long ideDocumento;
    @Column(name = "COD_ORGA_ADMIN")
    private String codOrgaAdmin;
    @JoinColumn(name = "IDE_PPD_DOCUMENTO", referencedColumnName = "IDE_PPD_DOCUMENTO")
    @ManyToOne(optional = false)
    private PpdDocumento ppdDocumento;

    public PpdTrazDocumento() {
    }

    public PpdTrazDocumento(Long ideTrazDocumento) {
        this.ideTrazDocumento = ideTrazDocumento;
    }

    public PpdTrazDocumento(Long ideTrazDocumento, Date fecTrazDocumento) {
        this.ideTrazDocumento = ideTrazDocumento;
        this.fecTrazDocumento = fecTrazDocumento;
    }

    public Long getIdeTrazDocumento() {
        return ideTrazDocumento;
    }

    public void setIdeTrazDocumento(Long ideTrazDocumento) {
        this.ideTrazDocumento = ideTrazDocumento;
    }

    public Date getFecTrazDocumento() {
        return fecTrazDocumento;
    }

    public void setFecTrazDocumento(Date fecTrazDocumento) {
        this.fecTrazDocumento = fecTrazDocumento;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Long getIdeFunci() {
        return ideFunci;
    }

    public void setIdeFunci(Long ideFunci) {
        this.ideFunci = ideFunci;
    }

    public String getCodEstado() {
        return codEstado;
    }

    public void setCodEstado(String codEstado) {
        this.codEstado = codEstado;
    }

    public Long getIdeDocumento() {
        return ideDocumento;
    }

    public void setIdeDocumento(Long ideDocumento) {
        this.ideDocumento = ideDocumento;
    }

    public String getCodOrgaAdmin() {
        return codOrgaAdmin;
    }

    public void setCodOrgaAdmin(String codOrgaAdmin) {
        this.codOrgaAdmin = codOrgaAdmin;
    }

    public PpdDocumento getPpdDocumento() {
        return ppdDocumento;
    }

    public void setPpdDocumento(PpdDocumento ppdDocumento) {
        this.ppdDocumento = ppdDocumento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTrazDocumento != null ? ideTrazDocumento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PpdTrazDocumento)) {
            return false;
        }
        PpdTrazDocumento other = (PpdTrazDocumento) object;
        if ((this.ideTrazDocumento == null && other.ideTrazDocumento != null) || (this.ideTrazDocumento != null && !this.ideTrazDocumento.equals(other.ideTrazDocumento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.soaint.correspondencia.domain.entity.PpdTrazDocumento[ ideTrazDocumento=" + ideTrazDocumento + " ]";
    }
    
}
