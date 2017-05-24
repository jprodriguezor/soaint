/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soaint.correspondencia.domain.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author jrodriguez
 */
@Entity
@Table(name = "TVS_CONSTANTES")
@NamedQueries({
    @NamedQuery(name = "TvsConstantes.findAll", query = "SELECT t FROM TvsConstantes t"),
        @NamedQuery(name = "TvsConstantes.findAllByCodigo", query = "SELECT t FROM TvsConstantes t WHERE t.codigo = :CODIGO")})
public class TvsConstantes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDE_CONST")
    private Long ideConst;
    @Column(name = "CODIGO")
    private String codigo;
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "COD_PADRE")
    private String codPadre;
    @Column(name = "ESTADO")
    private String estado;

    public TvsConstantes() {
    }

    public TvsConstantes(Long ideConst) {
        this.ideConst = ideConst;
    }

    public Long getIdeConst() {
        return ideConst;
    }

    public void setIdeConst(Long ideConst) {
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
