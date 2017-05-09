/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soaint.correspondencia.domain.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "TVS_PAIS")
@NamedQueries({
    @NamedQuery(name = "TvsPais.findAll", query = "SELECT t FROM TvsPais t")})
public class TvsPais implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TvsPaisPK tvsPaisPK;
    @Column(name = "NOMBRE_PAIS")
    private String nombrePais;
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

    public TvsPais() {
    }

    public TvsPais(TvsPaisPK tvsPaisPK) {
        this.tvsPaisPK = tvsPaisPK;
    }

    public TvsPais(String idePais, String codPais) {
        this.tvsPaisPK = new TvsPaisPK(idePais, codPais);
    }

    public TvsPaisPK getTvsPaisPK() {
        return tvsPaisPK;
    }

    public void setTvsPaisPK(TvsPaisPK tvsPaisPK) {
        this.tvsPaisPK = tvsPaisPK;
    }

    public String getNombrePais() {
        return nombrePais;
    }

    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
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
        hash += (tvsPaisPK != null ? tvsPaisPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TvsPais)) {
            return false;
        }
        TvsPais other = (TvsPais) object;
        if ((this.tvsPaisPK == null && other.tvsPaisPK != null) || (this.tvsPaisPK != null && !this.tvsPaisPK.equals(other.tvsPaisPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.soaint.correspondencia.domain.entity.TvsPais[ tvsPaisPK=" + tvsPaisPK + " ]";
    }
    
}
