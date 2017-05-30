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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author jrodriguez
 */
@Entity
@Table(name = "COR_REFERIDO")
@NamedQueries({
    @NamedQuery(name = "CorReferido.findAll", query = "SELECT c FROM CorReferido c")})
public class CorReferido implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDE_REFERIDO")
    private Long ideReferido;
    @Column(name = "NRO_RAD_REF")
    private String nroRadRef;
    @JoinColumn(name = "IDE_DOCUMENTO", referencedColumnName = "IDE_DOCUMENTO")
    @ManyToOne
    private CorCorrespondencia corCorrespondencia;

    public CorReferido() {
    }

    public CorReferido(Long ideReferido) {
        this.ideReferido = ideReferido;
    }

    public Long getIdeReferido() {
        return ideReferido;
    }

    public void setIdeReferido(Long ideReferido) {
        this.ideReferido = ideReferido;
    }

    public String getNroRadRef() {
        return nroRadRef;
    }

    public void setNroRadRef(String nroRadRef) {
        this.nroRadRef = nroRadRef;
    }

    public CorCorrespondencia getCorCorrespondencia() {
        return corCorrespondencia;
    }

    public void setCorCorrespondencia(CorCorrespondencia corCorrespondencia) {
        this.corCorrespondencia = corCorrespondencia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideReferido != null ? ideReferido.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CorReferido)) {
            return false;
        }
        CorReferido other = (CorReferido) object;
        if ((this.ideReferido == null && other.ideReferido != null) || (this.ideReferido != null && !this.ideReferido.equals(other.ideReferido))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.soaint.correspondencia.domain.entity.CorReferido[ ideReferido=" + ideReferido + " ]";
    }
    
}
