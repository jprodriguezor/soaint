/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soaint.correspondencia.domain.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author jrodriguez
 */
@Embeddable
public class TvsPaisPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "IDE_PAIS")
    private String idePais;
    @Basic(optional = false)
    @Column(name = "COD_PAIS")
    private String codPais;

    public TvsPaisPK() {
    }

    public TvsPaisPK(String idePais, String codPais) {
        this.idePais = idePais;
        this.codPais = codPais;
    }

    public String getIdePais() {
        return idePais;
    }

    public void setIdePais(String idePais) {
        this.idePais = idePais;
    }

    public String getCodPais() {
        return codPais;
    }

    public void setCodPais(String codPais) {
        this.codPais = codPais;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePais != null ? idePais.hashCode() : 0);
        hash += (codPais != null ? codPais.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TvsPaisPK)) {
            return false;
        }
        TvsPaisPK other = (TvsPaisPK) object;
        if ((this.idePais == null && other.idePais != null) || (this.idePais != null && !this.idePais.equals(other.idePais))) {
            return false;
        }
        if ((this.codPais == null && other.codPais != null) || (this.codPais != null && !this.codPais.equals(other.codPais))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.soaint.correspondencia.domain.entity.TvsPaisPK[ idePais=" + idePais + ", codPais=" + codPais + " ]";
    }
    
}
