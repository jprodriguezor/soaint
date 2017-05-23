/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soaint.correspondencia.domain.entity;

import java.io.Serializable;
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
    @Id
    @Basic(optional = false)
    @Column(name = "IDE_PPD_DOCUMENTO")
    private Long idePpdDocumento;
    @Column(name = "COD_TIPO_DOC")
    private String codTipoDoc;
    @Basic(optional = false)
    @Column(name = "FEC_DOCUMENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecDocumento;
    @Basic(optional = false)
    @Column(name = "COD_ASUNTO")
    private String codAsunto;
    @Column(name = "NRO_FOLIOS")
    private Long nroFolios;
    @Column(name = "NRO_ANEXOS")
    private Long nroAnexos;
    @Column(name = "COD_EST_DOC")
    private String codEstDoc;
    @Basic(optional = false)
    @Column(name = "FEC_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCreacion;
    @Column(name = "IDE_ECM")
    private String ideEcm;
    @Column(name = "COD_TIPO_SOPORTE")
    private String codTipoSoporte;
    @Column(name = "COD_EST_ARCHIVADO")
    private String codEstArchivado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ppdDocumento")
    private List<PpdTrazDocumento> ppdTrazDocumentoList;
    @OneToMany(mappedBy = "ppdDocumento")
    private List<CorAnexo> corAnexoList;
    @JoinColumn(name = "IDE_DOCUMENTO", referencedColumnName = "IDE_DOCUMENTO")
    @ManyToOne
    private CorCorrespondencia corCorrespondencia;

    public PpdDocumento() {
    }

    public PpdDocumento(Long idePpdDocumento) {
        this.idePpdDocumento = idePpdDocumento;
    }

    public PpdDocumento(Long idePpdDocumento, Date fecDocumento, String codAsunto, Date fecCreacion) {
        this.idePpdDocumento = idePpdDocumento;
        this.fecDocumento = fecDocumento;
        this.codAsunto = codAsunto;
        this.fecCreacion = fecCreacion;
    }

    public Long getIdePpdDocumento() {
        return idePpdDocumento;
    }

    public void setIdePpdDocumento(Long idePpdDocumento) {
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

    public Long getNroFolios() {
        return nroFolios;
    }

    public void setNroFolios(Long nroFolios) {
        this.nroFolios = nroFolios;
    }

    public Long getNroAnexos() {
        return nroAnexos;
    }

    public void setNroAnexos(Long nroAnexos) {
        this.nroAnexos = nroAnexos;
    }

    public String getCodEstDoc() {
        return codEstDoc;
    }

    public void setCodEstDoc(String codEstDoc) {
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

    public String getCodTipoSoporte() {
        return codTipoSoporte;
    }

    public void setCodTipoSoporte(String codTipoSoporte) {
        this.codTipoSoporte = codTipoSoporte;
    }

    public String getCodEstArchivado() {
        return codEstArchivado;
    }

    public void setCodEstArchivado(String codEstArchivado) {
        this.codEstArchivado = codEstArchivado;
    }

    public List<PpdTrazDocumento> getPpdTrazDocumentoList() {
        return ppdTrazDocumentoList;
    }

    public void setPpdTrazDocumentoList(List<PpdTrazDocumento> ppdTrazDocumentoList) {
        this.ppdTrazDocumentoList = ppdTrazDocumentoList;
    }

    public List<CorAnexo> getCorAnexoList() {
        return corAnexoList;
    }

    public void setCorAnexoList(List<CorAnexo> corAnexoList) {
        this.corAnexoList = corAnexoList;
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
