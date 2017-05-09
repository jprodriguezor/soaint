/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soaint.correspondencia.domain.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author jrodriguez
 */
@Entity
@Table(name = "PPD_DOCUMENTO")
@NamedQueries({
    @NamedQuery(name = "PpdDocumento.findAll", query = "SELECT p FROM PpdDocumento p")})
public class PpdDocumento implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "IDE_PPD_DOCUMENTO")
    private BigDecimal idePpdDocumento;
    @Basic(optional = false)
    @Column(name = "COD_TIPO_DOC")
    private String codTipoDoc;
    @Basic(optional = false)
    @Column(name = "FEC_DOCUMENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecDocumento;
    @Basic(optional = false)
    @Column(name = "COD_ASUNTO")
    private String codAsunto;
    @Basic(optional = false)
    @Column(name = "NRO_FOLIOS")
    private BigInteger nroFolios;
    @Basic(optional = false)
    @Column(name = "NRO_ANEXOS")
    private BigInteger nroAnexos;
    @Basic(optional = false)
    @Column(name = "COD_EST_DOC")
    private BigInteger codEstDoc;
    @Basic(optional = false)
    @Column(name = "FEC_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCreacion;
    @Basic(optional = false)
    @Column(name = "IDE_ECM")
    private String ideEcm;
    @Basic(optional = false)
    @Column(name = "COD_TIPO_SOPORTE")
    private BigInteger codTipoSoporte;
    @Basic(optional = false)
    @Column(name = "COD_EST_ARCHIVADO")
    private BigInteger codEstArchivado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idePpdDocumento1")
    private List<PpdTrazDocumento> ppdTrazDocumentoList;
    @JoinColumn(name = "IDE_DOCUMENTO", referencedColumnName = "IDE_DOCUMENTO")
    @ManyToOne(optional = false)
    private CorCorrespondencia ideDocumento;

    public PpdDocumento() {
    }

    public PpdDocumento(BigDecimal idePpdDocumento) {
        this.idePpdDocumento = idePpdDocumento;
    }

    public PpdDocumento(BigDecimal idePpdDocumento, String codTipoDoc, Date fecDocumento, String codAsunto, BigInteger nroFolios, BigInteger nroAnexos, BigInteger codEstDoc, Date fecCreacion, String ideEcm, BigInteger codTipoSoporte, BigInteger codEstArchivado) {
        this.idePpdDocumento = idePpdDocumento;
        this.codTipoDoc = codTipoDoc;
        this.fecDocumento = fecDocumento;
        this.codAsunto = codAsunto;
        this.nroFolios = nroFolios;
        this.nroAnexos = nroAnexos;
        this.codEstDoc = codEstDoc;
        this.fecCreacion = fecCreacion;
        this.ideEcm = ideEcm;
        this.codTipoSoporte = codTipoSoporte;
        this.codEstArchivado = codEstArchivado;
    }

    public BigDecimal getIdePpdDocumento() {
        return idePpdDocumento;
    }

    public void setIdePpdDocumento(BigDecimal idePpdDocumento) {
        this.idePpdDocumento = idePpdDocumento;
    }

    public String getCodTipoDoc() {
        return codTipoDoc;
    }

    public void setCodTipoDoc(String codTipoDoc) {
        this.codTipoDoc = codTipoDoc;
    }

    public Date getFecDocumento() {
        return fecDocumento;
    }

    public void setFecDocumento(Date fecDocumento) {
        this.fecDocumento = fecDocumento;
    }

    public String getCodAsunto() {
        return codAsunto;
    }

    public void setCodAsunto(String codAsunto) {
        this.codAsunto = codAsunto;
    }

    public BigInteger getNroFolios() {
        return nroFolios;
    }

    public void setNroFolios(BigInteger nroFolios) {
        this.nroFolios = nroFolios;
    }

    public BigInteger getNroAnexos() {
        return nroAnexos;
    }

    public void setNroAnexos(BigInteger nroAnexos) {
        this.nroAnexos = nroAnexos;
    }

    public BigInteger getCodEstDoc() {
        return codEstDoc;
    }

    public void setCodEstDoc(BigInteger codEstDoc) {
        this.codEstDoc = codEstDoc;
    }

    public Date getFecCreacion() {
        return fecCreacion;
    }

    public void setFecCreacion(Date fecCreacion) {
        this.fecCreacion = fecCreacion;
    }

    public String getIdeEcm() {
        return ideEcm;
    }

    public void setIdeEcm(String ideEcm) {
        this.ideEcm = ideEcm;
    }

    public BigInteger getCodTipoSoporte() {
        return codTipoSoporte;
    }

    public void setCodTipoSoporte(BigInteger codTipoSoporte) {
        this.codTipoSoporte = codTipoSoporte;
    }

    public BigInteger getCodEstArchivado() {
        return codEstArchivado;
    }

    public void setCodEstArchivado(BigInteger codEstArchivado) {
        this.codEstArchivado = codEstArchivado;
    }

    public List<PpdTrazDocumento> getPpdTrazDocumentoList() {
        return ppdTrazDocumentoList;
    }

    public void setPpdTrazDocumentoList(List<PpdTrazDocumento> ppdTrazDocumentoList) {
        this.ppdTrazDocumentoList = ppdTrazDocumentoList;
    }

    public CorCorrespondencia getIdeDocumento() {
        return ideDocumento;
    }

    public void setIdeDocumento(CorCorrespondencia ideDocumento) {
        this.ideDocumento = ideDocumento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePpdDocumento != null ? idePpdDocumento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PpdDocumento)) {
            return false;
        }
        PpdDocumento other = (PpdDocumento) object;
        if ((this.idePpdDocumento == null && other.idePpdDocumento != null) || (this.idePpdDocumento != null && !this.idePpdDocumento.equals(other.idePpdDocumento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.soaint.correspondencia.domain.entity.PpdDocumento[ idePpdDocumento=" + idePpdDocumento + " ]";
    }
    
}
