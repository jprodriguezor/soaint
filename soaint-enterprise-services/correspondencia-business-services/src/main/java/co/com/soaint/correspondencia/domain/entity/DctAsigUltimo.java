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
@Table(name = "DCT_ASIG_ULTIMO")
@NamedQueries({
    @NamedQuery(name = "DctAsigUltimo.findAll", query = "SELECT d FROM DctAsigUltimo d")})
public class DctAsigUltimo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDE_ASIG_ULTIMO")
    private Long ideAsigUltimo;
    @Column(name = "NUM_REDIRECCIONES")
    private String numRedirecciones;
    @Basic(optional = false)
    @Column(name = "IDE_USUARIO_CREO")
    private String ideUsuarioCreo;
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
    @Column(name = "FECHA_VENCIMIENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVencimiento;
    @Column(name = "ID_INSTANCIA")
    private String idInstancia;
    @Column(name = "COD_TIP_PROCESO")
    private String codTipProceso;
    @JoinColumn(name = "IDE_AGENTE", referencedColumnName = "IDE_AGENTE")
    @ManyToOne(optional = false)
    private CorAgente corAgente;
    @JoinColumn(name = "IDE_DOCUMENTO", referencedColumnName = "IDE_DOCUMENTO")
    @ManyToOne(optional = false)
    private CorCorrespondencia corCorrespondencia;
    @JoinColumn(name = "IDE_ASIGNACION", referencedColumnName = "IDE_ASIGNACION")
    @ManyToOne(optional = false)
    private DctAsignacion dctAsignacion;

    public DctAsigUltimo() {
    }

    public DctAsigUltimo(Long ideAsigUltimo) {
        this.ideAsigUltimo = ideAsigUltimo;
    }

    public DctAsigUltimo(Long ideAsigUltimo, String ideUsuarioCreo, Date fecCreo, long ideUsuarioCambio, Date fecCambio) {
        this.ideAsigUltimo = ideAsigUltimo;
        this.ideUsuarioCreo = ideUsuarioCreo;
        this.fecCreo = fecCreo;
        this.ideUsuarioCambio = ideUsuarioCambio;
        this.fecCambio = fecCambio;
    }

    public Long getIdeAsigUltimo() {
        return ideAsigUltimo;
    }

    public void setIdeAsigUltimo(Long ideAsigUltimo) {
        this.ideAsigUltimo = ideAsigUltimo;
    }

    public String getNumRedirecciones() {
        return numRedirecciones;
    }

    public void setNumRedirecciones(String numRedirecciones) {
        this.numRedirecciones = numRedirecciones;
    }

    public String getIdeUsuarioCreo() {
        return ideUsuarioCreo;
    }

    public void setIdeUsuarioCreo(String ideUsuarioCreo) {
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

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getIdInstancia() {
        return idInstancia;
    }

    public void setIdInstancia(String idInstancia) {
        this.idInstancia = idInstancia;
    }

    public String getCodTipProceso() {
        return codTipProceso;
    }

    public void setCodTipProceso(String codTipProceso) {
        this.codTipProceso = codTipProceso;
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

    public DctAsignacion getDctAsignacion() {
        return dctAsignacion;
    }

    public void setDctAsignacion(DctAsignacion dctAsignacion) {
        this.dctAsignacion = dctAsignacion;
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
