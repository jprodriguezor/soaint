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
@Table(name = "COR_ANEXO")
@NamedQueries({
    @NamedQuery(name = "CorAnexo.findAll", query = "SELECT c FROM CorAnexo c")})
public class CorAnexo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDE_ANEXO")
    private Long ideAnexo;
    @Column(name = "COD_ANEXO")
    private String codAnexo;
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @JoinColumn(name = "IDE_PPD_DOCUMENTO", referencedColumnName = "IDE_PPD_DOCUMENTO")
    @ManyToOne
    private PpdDocumento ppdDocumento;

    public CorAnexo() {
    }

    public CorAnexo(Long ideAnexo) {
        this.ideAnexo = ideAnexo;
    }

    public Long getIdeAnexo() {
        return ideAnexo;
    }

    public void setIdeAnexo(Long ideAnexo) {
        this.ideAnexo = ideAnexo;
    }

    public String getCodAnexo() {
        return codAnexo;
    }

    public void setCodAnexo(String codAnexo) {
        this.codAnexo = codAnexo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
        hash += (ideAnexo != null ? ideAnexo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CorAnexo)) {
            return false;
        }
        CorAnexo other = (CorAnexo) object;
        if ((this.ideAnexo == null && other.ideAnexo != null) || (this.ideAnexo != null && !this.ideAnexo.equals(other.ideAnexo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.soaint.correspondencia.domain.entity.CorAnexo[ ideAnexo=" + ideAnexo + " ]";
    }
    
}
