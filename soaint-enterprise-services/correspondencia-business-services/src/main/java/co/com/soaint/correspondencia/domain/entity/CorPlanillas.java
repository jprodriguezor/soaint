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
@Table(name = "COR_PLANILLAS")
@NamedQueries({
    @NamedQuery(name = "CorPlanillas.findAll", query = "SELECT c FROM CorPlanillas c")})
public class CorPlanillas implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "IDE_PLANILLA")
    private BigDecimal idePlanilla;
    @Column(name = "NRO_PLANILLA")
    private String nroPlanilla;
    @Column(name = "FEC_GENERACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecGeneracion;
    @Column(name = "COD_TIPO_PLANILLA")
    private String codTipoPlanilla;
    @Column(name = "COD_FUNC_GENERA")
    private String codFuncGenera;
    @Column(name = "COD_SEDE_ORIGEN")
    private BigInteger codSedeOrigen;
    @Column(name = "COD_DEPENDENCIA_ORIGEN")
    private BigInteger codDependenciaOrigen;
    @Column(name = "COD_SEDE_DESTINO")
    private BigInteger codSedeDestino;
    @Column(name = "COD_DEPENDENCIA_DESTINO")
    private BigInteger codDependenciaDestino;
    @Column(name = "COD_CLASE_ENVIO")
    private BigInteger codClaseEnvio;
    @Column(name = "COD_MODALIDAD_ENVIO")
    private BigInteger codModalidadEnvio;
    @Column(name = "FECHA_CREA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCrea;
    @Column(name = "COD_USUARIO_CREA")
    private String codUsuarioCrea;
    @Column(name = "FECHA_MODIF")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModif;
    @Column(name = "COD_USUARIO_MODIF")
    private String codUsuarioModif;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idePlanilla")
    private List<CorPlanAgen> corPlanAgenList;

    public CorPlanillas() {
    }

    public CorPlanillas(BigDecimal idePlanilla) {
        this.idePlanilla = idePlanilla;
    }

    public BigDecimal getIdePlanilla() {
        return idePlanilla;
    }

    public void setIdePlanilla(BigDecimal idePlanilla) {
        this.idePlanilla = idePlanilla;
    }

    public String getNroPlanilla() {
        return nroPlanilla;
    }

    public void setNroPlanilla(String nroPlanilla) {
        this.nroPlanilla = nroPlanilla;
    }

    public Date getFecGeneracion() {
        return fecGeneracion;
    }

    public void setFecGeneracion(Date fecGeneracion) {
        this.fecGeneracion = fecGeneracion;
    }

    public String getCodTipoPlanilla() {
        return codTipoPlanilla;
    }

    public void setCodTipoPlanilla(String codTipoPlanilla) {
        this.codTipoPlanilla = codTipoPlanilla;
    }

    public String getCodFuncGenera() {
        return codFuncGenera;
    }

    public void setCodFuncGenera(String codFuncGenera) {
        this.codFuncGenera = codFuncGenera;
    }

    public BigInteger getCodSedeOrigen() {
        return codSedeOrigen;
    }

    public void setCodSedeOrigen(BigInteger codSedeOrigen) {
        this.codSedeOrigen = codSedeOrigen;
    }

    public BigInteger getCodDependenciaOrigen() {
        return codDependenciaOrigen;
    }

    public void setCodDependenciaOrigen(BigInteger codDependenciaOrigen) {
        this.codDependenciaOrigen = codDependenciaOrigen;
    }

    public BigInteger getCodSedeDestino() {
        return codSedeDestino;
    }

    public void setCodSedeDestino(BigInteger codSedeDestino) {
        this.codSedeDestino = codSedeDestino;
    }

    public BigInteger getCodDependenciaDestino() {
        return codDependenciaDestino;
    }

    public void setCodDependenciaDestino(BigInteger codDependenciaDestino) {
        this.codDependenciaDestino = codDependenciaDestino;
    }

    public BigInteger getCodClaseEnvio() {
        return codClaseEnvio;
    }

    public void setCodClaseEnvio(BigInteger codClaseEnvio) {
        this.codClaseEnvio = codClaseEnvio;
    }

    public BigInteger getCodModalidadEnvio() {
        return codModalidadEnvio;
    }

    public void setCodModalidadEnvio(BigInteger codModalidadEnvio) {
        this.codModalidadEnvio = codModalidadEnvio;
    }

    public Date getFechaCrea() {
        return fechaCrea;
    }

    public void setFechaCrea(Date fechaCrea) {
        this.fechaCrea = fechaCrea;
    }

    public String getCodUsuarioCrea() {
        return codUsuarioCrea;
    }

    public void setCodUsuarioCrea(String codUsuarioCrea) {
        this.codUsuarioCrea = codUsuarioCrea;
    }

    public Date getFechaModif() {
        return fechaModif;
    }

    public void setFechaModif(Date fechaModif) {
        this.fechaModif = fechaModif;
    }

    public String getCodUsuarioModif() {
        return codUsuarioModif;
    }

    public void setCodUsuarioModif(String codUsuarioModif) {
        this.codUsuarioModif = codUsuarioModif;
    }

    public List<CorPlanAgen> getCorPlanAgenList() {
        return corPlanAgenList;
    }

    public void setCorPlanAgenList(List<CorPlanAgen> corPlanAgenList) {
        this.corPlanAgenList = corPlanAgenList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePlanilla != null ? idePlanilla.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CorPlanillas)) {
            return false;
        }
        CorPlanillas other = (CorPlanillas) object;
        if ((this.idePlanilla == null && other.idePlanilla != null) || (this.idePlanilla != null && !this.idePlanilla.equals(other.idePlanilla))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.soaint.correspondencia.domain.entity.CorPlanillas[ idePlanilla=" + idePlanilla + " ]";
    }
    
}
