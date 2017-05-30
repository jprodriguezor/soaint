/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soaint.correspondencia.domain.entity;

import java.io.Serializable;
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
    @Id
    @Basic(optional = false)
    @Column(name = "IDE_AGENTE")
    private Long ideAgente;
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
    private String codCortesia;
    @Column(name = "COD_CARGO")
    private String codCargo;
    @Column(name = "COD_EN_CALIDAD")
    private String codEnCalidad;
    @Column(name = "COD_TIP_DOC_IDENT")
    private String codTipDocIdent;
    @Column(name = "NRO_DOCU_IDENTIDAD")
    private String nroDocuIdentidad;
    @Column(name = "COD_SEDE")
    private String codSede;
    @Column(name = "COD_DEPENDENCIA")
    private String codDependencia;
    @Column(name = "COD_FUNC_REMITE")
    private String codFuncRemite;
    @Column(name = "COD_ESTADO")
    private String codEstado;
    @Column(name = "FEC_ASIGNACION")
    private String fecAsignacion;
    @Column(name = "IDE_CONTACTO")
    private Long ideContacto;
    @Column(name = "COD_TIP_AGENT")
    private String codTipAgent;
    @Column(name = "IND_ORIGINAL")
    private String indOriginal;
    @JoinColumn(name = "IDE_DOCUMENTO", referencedColumnName = "IDE_DOCUMENTO")
    @ManyToOne
    private CorCorrespondencia corCorrespondencia;
    @OneToMany(mappedBy = "corAgente")
    private List<DctAsignacion> dctAsignacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "corAgente")
    private List<CorPlanAgen> corPlanAgenList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "corAgente")
    private List<TvsDatosContacto> tvsDatosContactoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "corAgente")
    private List<DctAsigUltimo> dctAsigUltimoList;

    public CorAgente() {
    }

    public CorAgente(Long ideAgente) {
        this.ideAgente = ideAgente;
    }

    public Long getIdeAgente() {
        return ideAgente;
    }

    public void setIdeAgente(Long ideAgente) {
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

    public String getCodCortesia() {
        return codCortesia;
    }

    public void setCodCortesia(String codCortesia) {
        this.codCortesia = codCortesia;
    }

    public String getCodCargo() {
        return codCargo;
    }

    public void setCodCargo(String codCargo) {
        this.codCargo = codCargo;
    }

    public String getCodEnCalidad() {
        return codEnCalidad;
    }

    public void setCodEnCalidad(String codEnCalidad) {
        this.codEnCalidad = codEnCalidad;
    }

    public String getCodTipDocIdent() {
        return codTipDocIdent;
    }

    public void setCodTipDocIdent(String codTipDocIdent) {
        this.codTipDocIdent = codTipDocIdent;
    }

    public String getNroDocuIdentidad() {
        return nroDocuIdentidad;
    }

    public void setNroDocuIdentidad(String nroDocuIdentidad) {
        this.nroDocuIdentidad = nroDocuIdentidad;
    }

    public String getCodSede() {
        return codSede;
    }

    public void setCodSede(String codSede) {
        this.codSede = codSede;
    }

    public String getCodDependencia() {
        return codDependencia;
    }

    public void setCodDependencia(String codDependencia) {
        this.codDependencia = codDependencia;
    }

    public String getCodFuncRemite() {
        return codFuncRemite;
    }

    public void setCodFuncRemite(String codFuncRemite) {
        this.codFuncRemite = codFuncRemite;
    }

    public String getCodEstado() {
        return codEstado;
    }

    public void setCodEstado(String codEstado) {
        this.codEstado = codEstado;
    }

    public String getFecAsignacion() {
        return fecAsignacion;
    }

    public void setFecAsignacion(String fecAsignacion) {
        this.fecAsignacion = fecAsignacion;
    }

    public Long getIdeContacto() {
        return ideContacto;
    }

    public void setIdeContacto(Long ideContacto) {
        this.ideContacto = ideContacto;
    }

    public String getCodTipAgent() {
        return codTipAgent;
    }

    public void setCodTipAgent(String codTipAgent) {
        this.codTipAgent = codTipAgent;
    }

    public String getIndOriginal() {
        return indOriginal;
    }

    public void setIndOriginal(String indOriginal) {
        this.indOriginal = indOriginal;
    }

    public CorCorrespondencia getCorCorrespondencia() {
        return corCorrespondencia;
    }

    public void setCorCorrespondencia(CorCorrespondencia corCorrespondencia) {
        this.corCorrespondencia = corCorrespondencia;
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

    public List<TvsDatosContacto> getTvsDatosContactoList() {
        return tvsDatosContactoList;
    }

    public void setTvsDatosContactoList(List<TvsDatosContacto> tvsDatosContactoList) {
        this.tvsDatosContactoList = tvsDatosContactoList;
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
