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
@Table(name = "DCT_ASIG_ULTIMO")
@NamedQueries({
    @NamedQuery(name = "DctAsigUltimo.findAll", query = "SELECT d FROM DctAsigUltimo d")})
public class DctAsigUltimo implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "IDE_ASIG_ULTIMO")
    private BigDecimal ideAsigUltimo;
    @Column(name = "NUM_REDIRECCIONES")
    private BigInteger numRedirecciones;
    @Basic(optional = false)
    @Column(name = "IDE_USUARIO_CREO")
    private long ideUsuarioCreo;
    @Basic(optional = false)
    @Column(name = "FEC_CREO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCreo;
    @Basic(optional = false)
    @Column(name = "IDE_USUARIO_CAMBIO")
    private long ideUsuarioCambio;
    @Basic(optional = false)
    @Column(name = "FEC_CAMBIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCambio;
    @Column(name = "NIV_LECTURA")
    private Short nivLectura;
    @Column(name = "NIV_ESCRITURA")
    private Short nivEscritura;
    @Column(name = "IDE_UUID")
    private String ideUuid;
    @Column(name = "FECHA_VENCIMIENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVencimiento;
    @Column(name = "ID_INSTANCIA_BPM")
    private String idInstanciaBpm;
    @Column(name = "COD_TIP_PROCESO")
    private String codTipProceso;
    @JoinColumn(name = "IDE_AGENTE", referencedColumnName = "IDE_AGENTE")
    @ManyToOne(optional = false)
    private CorAgente ideAgente;
    @JoinColumn(name = "IDE_DOCUMENTO", referencedColumnName = "IDE_DOCUMENTO")
    @ManyToOne(optional = false)
    private CorCorrespondencia ideDocumento;
    @JoinColumn(name = "IDE_ASIGNACION", referencedColumnName = "IDE_ASIGNACION")
    @ManyToOne(optional = false)
    private DctAsignacion ideAsignacion;

    public DctAsigUltimo() {
    }

    public DctAsigUltimo(BigDecimal ideAsigUltimo) {
        this.ideAsigUltimo = ideAsigUltimo;
    }

    public DctAsigUltimo(BigDecimal ideAsigUltimo, long ideUsuarioCreo, Date fecCreo, long ideUsuarioCambio, Date fecCambio) {
        this.ideAsigUltimo = ideAsigUltimo;
        this.ideUsuarioCreo = ideUsuarioCreo;
        this.fecCreo = fecCreo;
        this.ideUsuarioCambio = ideUsuarioCambio;
        this.fecCambio = fecCambio;
    }

    public BigDecimal getIdeAsigUltimo() {
        return ideAsigUltimo;
    }

    public void setIdeAsigUltimo(BigDecimal ideAsigUltimo) {
        this.ideAsigUltimo = ideAsigUltimo;
    }

    public BigInteger getNumRedirecciones() {
        return numRedirecciones;
    }

    public void setNumRedirecciones(BigInteger numRedirecciones) {
        this.numRedirecciones = numRedirecciones;
    }

    public long getIdeUsuarioCreo() {
        return ideUsuarioCreo;
    }

    public void setIdeUsuarioCreo(long ideUsuarioCreo) {
        this.ideUsuarioCreo = ideUsuarioCreo;
    }

    public Date getFecCreo() {
        return fecCreo;
    }

    public void setFecCreo(Date fecCreo) {
        this.fecCreo = fecCreo;
    }

    public long getIdeUsuarioCambio() {
        return ideUsuarioCambio;
    }

    public void setIdeUsuarioCambio(long ideUsuarioCambio) {
        this.ideUsuarioCambio = ideUsuarioCambio;
    }

    public Date getFecCambio() {
        return fecCambio;
    }

    public void setFecCambio(Date fecCambio) {
        this.fecCambio = fecCambio;
    }

    public Short getNivLectura() {
        return nivLectura;
    }

    public void setNivLectura(Short nivLectura) {
        this.nivLectura = nivLectura;
    }

    public Short getNivEscritura() {
        return nivEscritura;
    }

    public void setNivEscritura(Short nivEscritura) {
        this.nivEscritura = nivEscritura;
    }

    public String getIdeUuid() {
        return ideUuid;
    }

    public void setIdeUuid(String ideUuid) {
        this.ideUuid = ideUuid;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getIdInstanciaBpm() {
        return idInstanciaBpm;
    }

    public void setIdInstanciaBpm(String idInstanciaBpm) {
        this.idInstanciaBpm = idInstanciaBpm;
    }

    public String getCodTipProceso() {
        return codTipProceso;
    }

    public void setCodTipProceso(String codTipProceso) {
        this.codTipProceso = codTipProceso;
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

    public DctAsignacion getIdeAsignacion() {
        return ideAsignacion;
    }

    public void setIdeAsignacion(DctAsignacion ideAsignacion) {
        this.ideAsignacion = ideAsignacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAsigUltimo != null ? ideAsigUltimo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DctAsigUltimo)) {
            return false;
        }
        DctAsigUltimo other = (DctAsigUltimo) object;
        if ((this.ideAsigUltimo == null && other.ideAsigUltimo != null) || (this.ideAsigUltimo != null && !this.ideAsigUltimo.equals(other.ideAsigUltimo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.soaint.correspondencia.domain.entity.DctAsigUltimo[ ideAsigUltimo=" + ideAsigUltimo + " ]";
    }
    
}
