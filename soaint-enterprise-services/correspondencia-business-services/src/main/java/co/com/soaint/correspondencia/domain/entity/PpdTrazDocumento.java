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
@Table(name = "PPD_TRAZ_DOCUMENTO")
@NamedQueries({
    @NamedQuery(name = "PpdTrazDocumento.findAll", query = "SELECT p FROM PpdTrazDocumento p")})
public class PpdTrazDocumento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDE_TRAZ_DOCUMENTO")
    private String ideTrazDocumento;
    @Basic(optional = false)
    @Column(name = "FEC_TRAZ_DOCUMENTO")
    private String fecTrazDocumento;
    @Basic(optional = false)
    @Column(name = "VAL_ASUNTO")
    private String valAsunto;
    @Basic(optional = false)
    @Column(name = "NUM_FOLIOS")
    private String numFolios;
    @Basic(optional = false)
    @Column(name = "NUM_ANEXOS")
    private String numAnexos;
    @Basic(optional = false)
    @Column(name = "IDE_FUNCIONARIO")
    private String ideFuncionario;
    @Basic(optional = false)
    @Column(name = "COD_ESTADO")
    private String codEstado;
    @Basic(optional = false)
    @Column(name = "IDE_DOCUMENTO")
    private String ideDocumento;
    @Basic(optional = false)
    @Column(name = "IDE_ORGA_ADMIN")
    private String ideOrgaAdmin;
    @JoinColumn(name = "IDE_PPD_DOCUMENTO", referencedColumnName = "IDE_DOCUMENTO")
    @ManyToOne(optional = false)
    private CorCorrespondencia idePpdDocumento;
    @JoinColumn(name = "IDE_PPD_DOCUMENTO", referencedColumnName = "IDE_PPD_DOCUMENTO")
    @ManyToOne(optional = false)
    private PpdDocumento idePpdDocumento1;

    public PpdTrazDocumento() {
    }

    public PpdTrazDocumento(String ideTrazDocumento) {
        this.ideTrazDocumento = ideTrazDocumento;
    }

    public PpdTrazDocumento(String ideTrazDocumento, String fecTrazDocumento, String valAsunto, String numFolios, String numAnexos, String ideFuncionario, String codEstado, String ideDocumento, String ideOrgaAdmin) {
        this.ideTrazDocumento = ideTrazDocumento;
        this.fecTrazDocumento = fecTrazDocumento;
        this.valAsunto = valAsunto;
        this.numFolios = numFolios;
        this.numAnexos = numAnexos;
        this.ideFuncionario = ideFuncionario;
        this.codEstado = codEstado;
        this.ideDocumento = ideDocumento;
        this.ideOrgaAdmin = ideOrgaAdmin;
    }

    public String getIdeTrazDocumento() {
        return ideTrazDocumento;
    }

    public void setIdeTrazDocumento(String ideTrazDocumento) {
        this.ideTrazDocumento = ideTrazDocumento;
    }

    public String getFecTrazDocumento() {
        return fecTrazDocumento;
    }

    public void setFecTrazDocumento(String fecTrazDocumento) {
        this.fecTrazDocumento = fecTrazDocumento;
    }

    public String getValAsunto() {
        return valAsunto;
    }

    public void setValAsunto(String valAsunto) {
        this.valAsunto = valAsunto;
    }

    public String getNumFolios() {
        return numFolios;
    }

    public void setNumFolios(String numFolios) {
        this.numFolios = numFolios;
    }

    public String getNumAnexos() {
        return numAnexos;
    }

    public void setNumAnexos(String numAnexos) {
        this.numAnexos = numAnexos;
    }

    public String getIdeFuncionario() {
        return ideFuncionario;
    }

    public void setIdeFuncionario(String ideFuncionario) {
        this.ideFuncionario = ideFuncionario;
    }

    public String getCodEstado() {
        return codEstado;
    }

    public void setCodEstado(String codEstado) {
        this.codEstado = codEstado;
    }

    public String getIdeDocumento() {
        return ideDocumento;
    }

    public void setIdeDocumento(String ideDocumento) {
        this.ideDocumento = ideDocumento;
    }

    public String getIdeOrgaAdmin() {
        return ideOrgaAdmin;
    }

    public void setIdeOrgaAdmin(String ideOrgaAdmin) {
        this.ideOrgaAdmin = ideOrgaAdmin;
    }

    public CorCorrespondencia getIdePpdDocumento() {
        return idePpdDocumento;
    }

    public void setIdePpdDocumento(CorCorrespondencia idePpdDocumento) {
        this.idePpdDocumento = idePpdDocumento;
    }

    public PpdDocumento getIdePpdDocumento1() {
        return idePpdDocumento1;
    }

    public void setIdePpdDocumento1(PpdDocumento idePpdDocumento1) {
        this.idePpdDocumento1 = idePpdDocumento1;
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
