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
@Table(name = "TVS_DEPARTAMENTO")
@NamedQueries({
    @NamedQuery(name = "TvsDepartamento.findAll", query = "SELECT t FROM TvsDepartamento t")})
public class TvsDepartamento implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "IDE_DEPAR")
    private BigDecimal ideDepar;
    @Column(name = "NOMBRE_DEPAR")
    private String nombreDepar;
    @Column(name = "COD_DEPAR")
    private String codDepar;
    @Column(name = "COD_PAIS")
    private String codPais;
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

    public TvsDepartamento() {
    }

    public TvsDepartamento(BigDecimal ideDepar) {
        this.ideDepar = ideDepar;
    }

    public BigDecimal getIdeDepar() {
        return ideDepar;
    }

    public void setIdeDepar(BigDecimal ideDepar) {
        this.ideDepar = ideDepar;
    }

    public String getNombreDepar() {
        return nombreDepar;
    }

    public void setNombreDepar(String nombreDepar) {
        this.nombreDepar = nombreDepar;
    }

    public String getCodDepar() {
        return codDepar;
    }

    public void setCodDepar(String codDepar) {
        this.codDepar = codDepar;
    }

    public String getCodPais() {
        return codPais;
    }

    public void setCodPais(String codPais) {
        this.codPais = codPais;
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
        hash += (ideDepar != null ? ideDepar.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TvsDepartamento)) {
            return false;
        }
        TvsDepartamento other = (TvsDepartamento) object;
        if ((this.ideDepar == null && other.ideDepar != null) || (this.ideDepar != null && !this.ideDepar.equals(other.ideDepar))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.soaint.correspondencia.domain.entity.TvsDepartamento[ ideDepar=" + ideDepar + " ]";
    }
    
}
