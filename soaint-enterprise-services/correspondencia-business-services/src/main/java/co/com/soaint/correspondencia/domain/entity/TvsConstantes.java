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
@Table(name = "TVS_CONSTANTES")
@NamedQueries({
    @NamedQuery(name = "TvsConstantes.findAll", query = "SELECT t FROM TvsConstantes t")})
public class TvsConstantes implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "IDE_CONST")
    private BigDecimal ideConst;
    @Column(name = "CODIGO")
    private String codigo;
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "COD_PADRE")
    private String codPadre;
    @Column(name = "ESTADO")
    private String estado;
    @Column(name = "FEC_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCreacion;
    @Column(name = "FEC_CAMBIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCambio;
    @Column(name = "COD_USUARIO_CREA")
    private String codUsuarioCrea;

    public TvsConstantes() {
    }

    public TvsConstantes(BigDecimal ideConst) {
        this.ideConst = ideConst;
    }

    public TvsConstantes(BigDecimal ideConst, String codPadre) {
        this.ideConst = ideConst;
        this.codPadre = codPadre;
    }

    public BigDecimal getIdeConst() {
        return ideConst;
    }

    public void setIdeConst(BigDecimal ideConst) {
        this.ideConst = ideConst;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodPadre() {
        return codPadre;
    }

    public void setCodPadre(String codPadre) {
        this.codPadre = codPadre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFecCreacion() {
        return fecCreacion;
    }

    public void setFecCreacion(Date fecCreacion) {
        this.fecCreacion = fecCreacion;
    }

    public Date getFecCambio() {
        return fecCambio;
    }

    public void setFecCambio(Date fecCambio) {
        this.fecCambio = fecCambio;
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
        hash += (ideConst != null ? ideConst.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TvsConstantes)) {
            return false;
        }
        TvsConstantes other = (TvsConstantes) object;
        if ((this.ideConst == null && other.ideConst != null) || (this.ideConst != null && !this.ideConst.equals(other.ideConst))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.soaint.correspondencia.domain.entity.TvsConstantes[ ideConst=" + ideConst + " ]";
    }
    
}
