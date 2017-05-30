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
@Table(name = "COR_CORRESPONDENCIA")
@NamedQueries({
    @NamedQuery(name = "CorCorrespondencia.findAll", query = "SELECT c FROM CorCorrespondencia c")})
public class CorCorrespondencia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDE_DOCUMENTO")
    private Long ideDocumento;
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Column(name = "TIEMPO_RESPUESTA")
    private String tiempoRespuesta;
    @Column(name = "COD_UNIDAD_TIEMPO")
    private String codUnidadTiempo;
    @Column(name = "COD_MEDIO_RECEPCION")
    private String codMedioRecepcion;
    @Basic(optional = false)
    @Column(name = "FEC_RADICADO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecRadicado;
    @Basic(optional = false)
    @Column(name = "NRO_RADICADO")
    private String nroRadicado;
    @Column(name = "COD_TIPO_CMC")
    private String codTipoCmc;
    @Column(name = "IDE_INSTANCIA")
    private String ideInstancia;
    @Column(name = "REQ_DIST_FISICA")
    private String reqDistFisica;
    @Column(name = "COD_FUNC_RADICA")
    private String codFuncRadica;
    @Column(name = "COD_SEDE")
    private String codSede;
    @Column(name = "COD_DEPENDENCIA")
    private String codDependencia;
    @Column(name = "REQ_DIGITA")
    private String reqDigita;
    @Column(name = "COD_EMP_MSJ")
    private String codEmpMsj;
    @Column(name = "NRO_GUIA")
    private String nroGuia;
    @Column(name = "FEC_VEN_GESTION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecVenGestion;
    @Column(name = "COD_ESTADO")
    private String codEstado;
    @OneToMany(mappedBy = "corCorrespondencia")
    private List<CorAgente> corAgenteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "corCorrespondencia")
    private List<DctAsignacion> dctAsignacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "corCorrespondencia")
    private List<CorPlanAgen> corPlanAgenList;
    @OneToMany(mappedBy = "corCorrespondencia")
    private List<PpdDocumento> ppdDocumentoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "corCorrespondencia")
    private List<DctAsigUltimo> dctAsigUltimoList;
    @OneToMany(mappedBy = "corCorrespondencia")
    private List<CorReferido> corReferidoList;

    public CorCorrespondencia() {
    }

    public CorCorrespondencia(Long ideDocumento) {
        this.ideDocumento = ideDocumento;
    }

    public CorCorrespondencia(Long ideDocumento, Date fecRadicado, String nroRadicado) {
        this.ideDocumento = ideDocumento;
        this.fecRadicado = fecRadicado;
        this.nroRadicado = nroRadicado;
    }

    public Long getIdeDocumento() {
        return ideDocumento;
    }

    public void setIdeDocumento(Long ideDocumento) {
        this.ideDocumento = ideDocumento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTiempoRespuesta() {
        return tiempoRespuesta;
    }

    public void setTiempoRespuesta(String tiempoRespuesta) {
        this.tiempoRespuesta = tiempoRespuesta;
    }

    public String getCodUnidadTiempo() {
        return codUnidadTiempo;
    }

    public void setCodUnidadTiempo(String codUnidadTiempo) {
        this.codUnidadTiempo = codUnidadTiempo;
    }

    public String getCodMedioRecepcion() {
        return codMedioRecepcion;
    }

    public void setCodMedioRecepcion(String codMedioRecepcion) {
        this.codMedioRecepcion = codMedioRecepcion;
    }

    public Date getFecRadicado() {
        return fecRadicado;
    }

    public void setFecRadicado(Date fecRadicado) {
        this.fecRadicado = fecRadicado;
    }

    public String getNroRadicado() {
        return nroRadicado;
    }

    public void setNroRadicado(String nroRadicado) {
        this.nroRadicado = nroRadicado;
    }

    public String getCodTipoCmc() {
        return codTipoCmc;
    }

    public void setCodTipoCmc(String codTipoCmc) {
        this.codTipoCmc = codTipoCmc;
    }

    public String getIdeInstancia() {
        return ideInstancia;
    }

    public void setIdeInstancia(String ideInstancia) {
        this.ideInstancia = ideInstancia;
    }

    public String getReqDistFisica() {
        return reqDistFisica;
    }

    public void setReqDistFisica(String reqDistFisica) {
        this.reqDistFisica = reqDistFisica;
    }

    public String getCodFuncRadica() {
        return codFuncRadica;
    }

    public void setCodFuncRadica(String codFuncRadica) {
        this.codFuncRadica = codFuncRadica;
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

    public String getReqDigita() {
        return reqDigita;
    }

    public void setReqDigita(String reqDigita) {
        this.reqDigita = reqDigita;
    }

    public String getCodEmpMsj() {
        return codEmpMsj;
    }

    public void setCodEmpMsj(String codEmpMsj) {
        this.codEmpMsj = codEmpMsj;
    }

    public String getNroGuia() {
        return nroGuia;
    }

    public void setNroGuia(String nroGuia) {
        this.nroGuia = nroGuia;
    }

    public Date getFecVenGestion() {
        return fecVenGestion;
    }

    public void setFecVenGestion(Date fecVenGestion) {
        this.fecVenGestion = fecVenGestion;
    }

    public String getCodEstado() {
        return codEstado;
    }

    public void setCodEstado(String codEstado) {
        this.codEstado = codEstado;
    }

    public List<CorAgente> getCorAgenteList() {
        return corAgenteList;
    }

    public void setCorAgenteList(List<CorAgente> corAgenteList) {
        this.corAgenteList = corAgenteList;
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

    public List<PpdDocumento> getPpdDocumentoList() {
        return ppdDocumentoList;
    }

    public void setPpdDocumentoList(List<PpdDocumento> ppdDocumentoList) {
        this.ppdDocumentoList = ppdDocumentoList;
    }

    public List<DctAsigUltimo> getDctAsigUltimoList() {
        return dctAsigUltimoList;
    }

    public void setDctAsigUltimoList(List<DctAsigUltimo> dctAsigUltimoList) {
        this.dctAsigUltimoList = dctAsigUltimoList;
    }

    public List<CorReferido> getCorReferidoList() {
        return corReferidoList;
    }

    public void setCorReferidoList(List<CorReferido> corReferidoList) {
        this.corReferidoList = corReferidoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideDocumento != null ? ideDocumento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CorCorrespondencia)) {
            return false;
        }
        CorCorrespondencia other = (CorCorrespondencia) object;
        if ((this.ideDocumento == null && other.ideDocumento != null) || (this.ideDocumento != null && !this.ideDocumento.equals(other.ideDocumento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.soaint.correspondencia.domain.entity.CorCorrespondencia[ ideDocumento=" + ideDocumento + " ]";
    }
    
}
