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
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "IDE_PLAN_AGEN")
    private BigDecimal idePlanAgen;
    @Basic(optional = false)
    @Column(name = "ESTADO")
    private short estado;
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
    private BigInteger codNuevaSede;
    @Column(name = "COD_NUEVA_DEPEN")
    private BigInteger codNuevaDepen;
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    @Column(name = "COD_CAU_DEVO")
    private BigInteger codCauDevo;
    @Column(name = "FEC_CARGUE_PLA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCarguePla;
    @JoinColumn(name = "IDE_AGENTE", referencedColumnName = "IDE_AGENTE")
    @ManyToOne(optional = false)
    private CorAgente ideAgente;
    @JoinColumn(name = "IDE_DOCUMENTO", referencedColumnName = "IDE_DOCUMENTO")
    @ManyToOne(optional = false)
    private CorCorrespondencia ideDocumento;
    @JoinColumn(name = "IDE_PLANILLA", referencedColumnName = "IDE_PLANILLA")
    @ManyToOne(optional = false)
    private CorPlanillas idePlanilla;

    public CorPlanAgen() {
    }

    public CorPlanAgen(BigDecimal idePlanAgen) {
        this.idePlanAgen = idePlanAgen;
    }

    public CorPlanAgen(BigDecimal idePlanAgen, short estado) {
        this.idePlanAgen = idePlanAgen;
        this.estado = estado;
    }

    public BigDecimal getIdePlanAgen() {
        return idePlanAgen;
    }

    public void setIdePlanAgen(BigDecimal idePlanAgen) {
        this.idePlanAgen = idePlanAgen;
    }

    public short getEstado() {
        return estado;
    }

    public void setEstado(short estado) {
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

    public BigInteger getCodNuevaSede() {
        return codNuevaSede;
    }

    public void setCodNuevaSede(BigInteger codNuevaSede) {
        this.codNuevaSede = codNuevaSede;
    }

    public BigInteger getCodNuevaDepen() {
        return codNuevaDepen;
    }

    public void setCodNuevaDepen(BigInteger codNuevaDepen) {
        this.codNuevaDepen = codNuevaDepen;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public BigInteger getCodCauDevo() {
        return codCauDevo;
    }

    public void setCodCauDevo(BigInteger codCauDevo) {
        this.codCauDevo = codCauDevo;
    }

    public Date getFecCarguePla() {
        return fecCarguePla;
    }

    public void setFecCarguePla(Date fecCarguePla) {
        this.fecCarguePla = fecCarguePla;
    }

    public CorAgente getIdeAgente() {
        return ideAgente;
    }

    public void setIdeAgente(CorAgente ideAgente) {
        this.ideAgente = ideAgente;
    }

    public CorCorrespondencia getIdeDocumento() {
        return ideDocumento;
    }

    public void setIdeDocumento(CorCorrespondencia ideDocumento) {
        this.ideDocumento = ideDocumento;
    }

    public CorPlanillas getIdePlanilla() {
        return idePlanilla;
    }

    public void setIdePlanilla(CorPlanillas idePlanilla) {
        this.idePlanilla = idePlanilla;
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
