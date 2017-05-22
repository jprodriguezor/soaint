/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soaint.correspondencia.domain.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "COR_PLAN_AGEN")
@NamedQueries({
    @NamedQuery(name = "CorPlanAgen.findAll", query = "SELECT c FROM CorPlanAgen c")})
public class CorPlanAgen implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDE_PLAN_AGEN")
    private Long idePlanAgen;
    @Basic(optional = false)
    @Column(name = "ESTADO")
    private String estado;
    @Column(name = "VAR_PESO")
    private String varPeso;
    @Column(name = "VAR_VALOR")
    private String varValor;
    @Column(name = "VAR_NUMERO_GUIA")
    private String varNumeroGuia;
    @Column(name = "FEC_OBSERVACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecObservacion;
    @Column(name = "COD_NUEVA_SEDE")
    private String codNuevaSede;
    @Column(name = "COD_NUEVA_DEPEN")
    private String codNuevaDepen;
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    @Column(name = "COD_CAU_DEVO")
    private String codCauDevo;
    @Column(name = "FEC_CARGUE_PLA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCarguePla;
    @JoinColumn(name = "IDE_AGENTE", referencedColumnName = "IDE_AGENTE")
    @ManyToOne(optional = false)
    private CorAgente corAgente;
    @JoinColumn(name = "IDE_DOCUMENTO", referencedColumnName = "IDE_DOCUMENTO")
    @ManyToOne(optional = false)
    private CorCorrespondencia corCorrespondencia;
    @JoinColumn(name = "IDE_PLANILLA", referencedColumnName = "IDE_PLANILLA")
    @ManyToOne(optional = false)
    private CorPlanillas corPlanillas;

    public CorPlanAgen() {
    }

    public CorPlanAgen(Long idePlanAgen) {
        this.idePlanAgen = idePlanAgen;
    }

    public CorPlanAgen(Long idePlanAgen, String estado) {
        this.idePlanAgen = idePlanAgen;
        this.estado = estado;
    }

    public Long getIdePlanAgen() {
        return idePlanAgen;
    }

    public void setIdePlanAgen(Long idePlanAgen) {
        this.idePlanAgen = idePlanAgen;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getVarPeso() {
        return varPeso;
    }

    public void setVarPeso(String varPeso) {
        this.varPeso = varPeso;
    }

    public String getVarValor() {
        return varValor;
    }

    public void setVarValor(String varValor) {
        this.varValor = varValor;
    }

    public String getVarNumeroGuia() {
        return varNumeroGuia;
    }

    public void setVarNumeroGuia(String varNumeroGuia) {
        this.varNumeroGuia = varNumeroGuia;
    }

    public Date getFecObservacion() {
        return fecObservacion;
    }

    public void setFecObservacion(Date fecObservacion) {
        this.fecObservacion = fecObservacion;
    }

    public String getCodNuevaSede() {
        return codNuevaSede;
    }

    public void setCodNuevaSede(String codNuevaSede) {
        this.codNuevaSede = codNuevaSede;
    }

    public String getCodNuevaDepen() {
        return codNuevaDepen;
    }

    public void setCodNuevaDepen(String codNuevaDepen) {
        this.codNuevaDepen = codNuevaDepen;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getCodCauDevo() {
        return codCauDevo;
    }

    public void setCodCauDevo(String codCauDevo) {
        this.codCauDevo = codCauDevo;
    }

    public Date getFecCarguePla() {
        return fecCarguePla;
    }

    public void setFecCarguePla(Date fecCarguePla) {
        this.fecCarguePla = fecCarguePla;
    }

    public CorAgente getCorAgente() {
        return corAgente;
    }

    public void setCorAgente(CorAgente corAgente) {
        this.corAgente = corAgente;
    }

    public CorCorrespondencia getCorCorrespondencia() {
        return corCorrespondencia;
    }

    public void setCorCorrespondencia(CorCorrespondencia corCorrespondencia) {
        this.corCorrespondencia = corCorrespondencia;
    }

    public CorPlanillas getCorPlanillas() {
        return corPlanillas;
    }

    public void setCorPlanillas(CorPlanillas corPlanillas) {
        this.corPlanillas = corPlanillas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePlanAgen != null ? idePlanAgen.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CorPlanAgen)) {
            return false;
        }
        CorPlanAgen other = (CorPlanAgen) object;
        if ((this.idePlanAgen == null && other.idePlanAgen != null) || (this.idePlanAgen != null && !this.idePlanAgen.equals(other.idePlanAgen))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.soaint.correspondencia.domain.entity.CorPlanAgen[ idePlanAgen=" + idePlanAgen + " ]";
    }
    
}
