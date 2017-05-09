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
import javax.persistence.Lob;
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
@Table(name = "COR_AGENTE")
@NamedQueries({
    @NamedQuery(name = "CorAgente.findAll", query = "SELECT c FROM CorAgente c")})
public class CorAgente implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "IDE_AGENTE")
    private BigDecimal ideAgente;
    @Column(name = "COD_TIPO_REMITE")
    private String codTipoRemite;
    @Column(name = "COD_TIPO_PERS")
    private String codTipoPers;
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "NRO_DOCUMENTO_IDEN")
    private String nroDocumentoIden;
    @Column(name = "RAZON_SOCIAL")
    private String razonSocial;
    @Column(name = "NIT")
    private String nit;
    @Column(name = "COD_CORTESIA")
    private BigInteger codCortesia;
    @Column(name = "COD_CARGO")
    private BigInteger codCargo;
    @Column(name = "COD_EN_CALIDAD")
    private BigInteger codEnCalidad;
    @Column(name = "COD_TIP_DOC_IDENT")
    private BigInteger codTipDocIdent;
    @Column(name = "NRO_DOCU_IDENTIDAD")
    private String nroDocuIdentidad;
    @Column(name = "COD_SEDE")
    private BigInteger codSede;
    @Column(name = "COD_DEPENDENCIA")
    private BigInteger codDependencia;
    @Column(name = "COD_FUNC_REMITE")
    private String codFuncRemite;
    @Lob
    @Column(name = "IND_ORIGINAL")
    private Serializable indOriginal;
    @Column(name = "COD_ESTADO")
    private String codEstado;
    @Column(name = "FEC_ASIGNACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecAsignacion;
    @Column(name = "IDE_CONTACTO")
    private BigInteger ideContacto;
    @Column(name = "COD_TIP_AGENT")
    private String codTipAgent;
    @JoinColumn(name = "IDE_DOCUMENTO", referencedColumnName = "IDE_DOCUMENTO")
    @ManyToOne
    private CorCorrespondencia ideDocumento;
    @OneToMany(mappedBy = "ideAgente")
    private List<DctAsignacion> dctAsignacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideAgente")
    private List<CorPlanAgen> corPlanAgenList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideAgente")
    private List<DctAsigUltimo> dctAsigUltimoList;

    public CorAgente() {
    }

    public CorAgente(BigDecimal ideAgente) {
        this.ideAgente = ideAgente;
    }

    public BigDecimal getIdeAgente() {
        return ideAgente;
    }

    public void setIdeAgente(BigDecimal ideAgente) {
        this.ideAgente = ideAgente;
    }

    public String getCodTipoRemite() {
        return codTipoRemite;
    }

    public void setCodTipoRemite(String codTipoRemite) {
        this.codTipoRemite = codTipoRemite;
    }

    public String getCodTipoPers() {
        return codTipoPers;
    }

    public void setCodTipoPers(String codTipoPers) {
        this.codTipoPers = codTipoPers;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNroDocumentoIden() {
        return nroDocumentoIden;
    }

    public void setNroDocumentoIden(String nroDocumentoIden) {
        this.nroDocumentoIden = nroDocumentoIden;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public BigInteger getCodCortesia() {
        return codCortesia;
    }

    public void setCodCortesia(BigInteger codCortesia) {
        this.codCortesia = codCortesia;
    }

    public BigInteger getCodCargo() {
        return codCargo;
    }

    public void setCodCargo(BigInteger codCargo) {
        this.codCargo = codCargo;
    }

    public BigInteger getCodEnCalidad() {
        return codEnCalidad;
    }

    public void setCodEnCalidad(BigInteger codEnCalidad) {
        this.codEnCalidad = codEnCalidad;
    }

    public BigInteger getCodTipDocIdent() {
        return codTipDocIdent;
    }

    public void setCodTipDocIdent(BigInteger codTipDocIdent) {
        this.codTipDocIdent = codTipDocIdent;
    }

    public String getNroDocuIdentidad() {
        return nroDocuIdentidad;
    }

    public void setNroDocuIdentidad(String nroDocuIdentidad) {
        this.nroDocuIdentidad = nroDocuIdentidad;
    }

    public BigInteger getCodSede() {
        return codSede;
    }

    public void setCodSede(BigInteger codSede) {
        this.codSede = codSede;
    }

    public BigInteger getCodDependencia() {
        return codDependencia;
    }

    public void setCodDependencia(BigInteger codDependencia) {
        this.codDependencia = codDependencia;
    }

    public String getCodFuncRemite() {
        return codFuncRemite;
    }

    public void setCodFuncRemite(String codFuncRemite) {
        this.codFuncRemite = codFuncRemite;
    }

    public Serializable getIndOriginal() {
        return indOriginal;
    }

    public void setIndOriginal(Serializable indOriginal) {
        this.indOriginal = indOriginal;
    }

    public String getCodEstado() {
        return codEstado;
    }

    public void setCodEstado(String codEstado) {
        this.codEstado = codEstado;
    }

    public Date getFecAsignacion() {
        return fecAsignacion;
    }

    public void setFecAsignacion(Date fecAsignacion) {
        this.fecAsignacion = fecAsignacion;
    }

    public BigInteger getIdeContacto() {
        return ideContacto;
    }

    public void setIdeContacto(BigInteger ideContacto) {
        this.ideContacto = ideContacto;
    }

    public String getCodTipAgent() {
        return codTipAgent;
    }

    public void setCodTipAgent(String codTipAgent) {
        this.codTipAgent = codTipAgent;
    }

    public CorCorrespondencia getIdeDocumento() {
        return ideDocumento;
    }

    public void setIdeDocumento(CorCorrespondencia ideDocumento) {
        this.ideDocumento = ideDocumento;
    }

    public List<DctAsignacion> getDctAsignacionList() {
        return dctAsignacionList;
    }

    public void setDctAsignacionList(List<DctAsignacion> dctAsignacionList) {
        this.dctAsignacionList = dctAsignacionList;
    }

    public List<CorPlanAgen> getCorPlanAgenList() {
        return corPlanAgenList;
    }

    public void setCorPlanAgenList(List<CorPlanAgen> corPlanAgenList) {
        this.corPlanAgenList = corPlanAgenList;
    }

    public List<DctAsigUltimo> getDctAsigUltimoList() {
        return dctAsigUltimoList;
    }

    public void setDctAsigUltimoList(List<DctAsigUltimo> dctAsigUltimoList) {
        this.dctAsigUltimoList = dctAsigUltimoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAgente != null ? ideAgente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CorAgente)) {
            return false;
        }
        CorAgente other = (CorAgente) object;
        if ((this.ideAgente == null && other.ideAgente != null) || (this.ideAgente != null && !this.ideAgente.equals(other.ideAgente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.soaint.correspondencia.domain.entity.CorAgente[ ideAgente=" + ideAgente + " ]";
    }
    
}
