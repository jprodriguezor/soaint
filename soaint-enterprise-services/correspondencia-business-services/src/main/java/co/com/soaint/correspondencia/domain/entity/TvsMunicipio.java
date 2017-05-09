/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soaint.correspondencia.domain.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "TVS_MUNICIPIO")
@NamedQueries({
    @NamedQuery(name = "TvsMunicipio.findAll", query = "SELECT t FROM TvsMunicipio t")})
public class TvsMunicipio implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "IDE_MUNIC")
    private BigDecimal ideMunic;
    @Column(name = "NOMBRE_MUNIC")
    private String nombreMunic;
    @Column(name = "COD_MUNIC")
    private String codMunic;
    @Basic(optional = false)
    @Column(name = "COD_DEPAR")
    private String codDepar;
    @Column(name = "ESTADO")
    private String estado;
    @Column(name = "FEC_CAMBIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCambio;
    @Column(name = "FEC_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCreacion;
    @Column(name = "COD_USUARIO_CREA")
    private String codUsuarioCrea;

    public TvsMunicipio() {
    }

    public TvsMunicipio(BigDecimal ideMunic) {
        this.ideMunic = ideMunic;
    }

    public TvsMunicipio(BigDecimal ideMunic, String codDepar) {
        this.ideMunic = ideMunic;
        this.codDepar = codDepar;
    }

    public BigDecimal getIdeMunic() {
        return ideMunic;
    }

    public void setIdeMunic(BigDecimal ideMunic) {
        this.ideMunic = ideMunic;
    }

    public String getNombreMunic() {
        return nombreMunic;
    }

    public void setNombreMunic(String nombreMunic) {
        this.nombreMunic = nombreMunic;
    }

    public String getCodMunic() {
        return codMunic;
    }

    public void setCodMunic(String codMunic) {
        this.codMunic = codMunic;
    }

    public String getCodDepar() {
        return codDepar;
    }

    public void setCodDepar(String codDepar) {
        this.codDepar = codDepar;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFecCambio() {
        return fecCambio;
    }

    public void setFecCambio(Date fecCambio) {
        this.fecCambio = fecCambio;
    }

    public Date getFecCreacion() {
        return fecCreacion;
    }

    public void setFecCreacion(Date fecCreacion) {
        this.fecCreacion = fecCreacion;
    }

    public String getCodUsuarioCrea() {
        return codUsuarioCrea;
    }

    public void setCodUsuarioCrea(String codUsuarioCrea) {
        this.codUsuarioCrea = codUsuarioCrea;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideMunic != null ? ideMunic.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TvsMunicipio)) {
            return false;
        }
        TvsMunicipio other = (TvsMunicipio) object;
        if ((this.ideMunic == null && other.ideMunic != null) || (this.ideMunic != null && !this.ideMunic.equals(other.ideMunic))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.soaint.correspondencia.domain.entity.TvsMunicipio[ ideMunic=" + ideMunic + " ]";
    }
    
}
