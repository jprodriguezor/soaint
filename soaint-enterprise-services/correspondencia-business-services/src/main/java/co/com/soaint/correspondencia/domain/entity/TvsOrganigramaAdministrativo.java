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
@Table(name = "TVS_ORGANIGRAMA_ADMINISTRATIVO")
@NamedQueries({
    @NamedQuery(name = "TvsOrganigramaAdministrativo.findAll", query = "SELECT t FROM TvsOrganigramaAdministrativo t")})
public class TvsOrganigramaAdministrativo implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "IDE_ORGA_ADMIN")
    private BigDecimal ideOrgaAdmin;
    @Basic(optional = false)
    @Column(name = "COD_ORG")
    private String codOrg;
    @Basic(optional = false)
    @Column(name = "NOM_ORG")
    private String nomOrg;
    @Basic(optional = false)
    @Column(name = "DESC_ORG")
    private String descOrg;
    @Basic(optional = false)
    @Column(name = "IND_ES_ACTIVO")
    private String indEsActivo;
    @Column(name = "IDE_DIRECCION")
    private BigInteger ideDireccion;
    @Column(name = "IDE_PLAN_RESPONSABLE")
    private BigDecimal idePlanResponsable;
    @Column(name = "IDE_ORGA_ADMIN_PADRE")
    private BigInteger ideOrgaAdminPadre;
    @Column(name = "COD_NIVEL")
    private String codNivel;
    @Column(name = "FEC_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCreacion;
    @Column(name = "IDE_USUARIO_CREO")
    private Long ideUsuarioCreo;
    @Basic(optional = false)
    @Column(name = "IDE_USUARIO_CAMBIO")
    private long ideUsuarioCambio;
    @Column(name = "FEC_CAMBIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCambio;
    @Column(name = "NIV_LECTURA")
    private Short nivLectura;
    @Column(name = "NIV_ESCRITURA")
    private Short nivEscritura;
    @Column(name = "IDE_UUID")
    private String ideUuid;
    @Column(name = "VAL_SISTEMA")
    private String valSistema;
    @Column(name = "VAL_VERSION")
    private String valVersion;

    public TvsOrganigramaAdministrativo() {
    }

    public TvsOrganigramaAdministrativo(BigDecimal ideOrgaAdmin) {
        this.ideOrgaAdmin = ideOrgaAdmin;
    }

    public TvsOrganigramaAdministrativo(BigDecimal ideOrgaAdmin, String codOrg, String nomOrg, String descOrg, String indEsActivo, long ideUsuarioCambio) {
        this.ideOrgaAdmin = ideOrgaAdmin;
        this.codOrg = codOrg;
        this.nomOrg = nomOrg;
        this.descOrg = descOrg;
        this.indEsActivo = indEsActivo;
        this.ideUsuarioCambio = ideUsuarioCambio;
    }

    public BigDecimal getIdeOrgaAdmin() {
        return ideOrgaAdmin;
    }

    public void setIdeOrgaAdmin(BigDecimal ideOrgaAdmin) {
        this.ideOrgaAdmin = ideOrgaAdmin;
    }

    public String getCodOrg() {
        return codOrg;
    }

    public void setCodOrg(String codOrg) {
        this.codOrg = codOrg;
    }

    public String getNomOrg() {
        return nomOrg;
    }

    public void setNomOrg(String nomOrg) {
        this.nomOrg = nomOrg;
    }

    public String getDescOrg() {
        return descOrg;
    }

    public void setDescOrg(String descOrg) {
        this.descOrg = descOrg;
    }

    public String getIndEsActivo() {
        return indEsActivo;
    }

    public void setIndEsActivo(String indEsActivo) {
        this.indEsActivo = indEsActivo;
    }

    public BigInteger getIdeDireccion() {
        return ideDireccion;
    }

    public void setIdeDireccion(BigInteger ideDireccion) {
        this.ideDireccion = ideDireccion;
    }

    public BigDecimal getIdePlanResponsable() {
        return idePlanResponsable;
    }

    public void setIdePlanResponsable(BigDecimal idePlanResponsable) {
        this.idePlanResponsable = idePlanResponsable;
    }

    public BigInteger getIdeOrgaAdminPadre() {
        return ideOrgaAdminPadre;
    }

    public void setIdeOrgaAdminPadre(BigInteger ideOrgaAdminPadre) {
        this.ideOrgaAdminPadre = ideOrgaAdminPadre;
    }

    public String getCodNivel() {
        return codNivel;
    }

    public void setCodNivel(String codNivel) {
        this.codNivel = codNivel;
    }

    public Date getFecCreacion() {
        return fecCreacion;
    }

    public void setFecCreacion(Date fecCreacion) {
        this.fecCreacion = fecCreacion;
    }

    public Long getIdeUsuarioCreo() {
        return ideUsuarioCreo;
    }

    public void setIdeUsuarioCreo(Long ideUsuarioCreo) {
        this.ideUsuarioCreo = ideUsuarioCreo;
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

    public String getValSistema() {
        return valSistema;
    }

    public void setValSistema(String valSistema) {
        this.valSistema = valSistema;
    }

    public String getValVersion() {
        return valVersion;
    }

    public void setValVersion(String valVersion) {
        this.valVersion = valVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideOrgaAdmin != null ? ideOrgaAdmin.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TvsOrganigramaAdministrativo)) {
            return false;
        }
        TvsOrganigramaAdministrativo other = (TvsOrganigramaAdministrativo) object;
        if ((this.ideOrgaAdmin == null && other.ideOrgaAdmin != null) || (this.ideOrgaAdmin != null && !this.ideOrgaAdmin.equals(other.ideOrgaAdmin))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.soaint.correspondencia.domain.entity.TvsOrganigramaAdministrativo[ ideOrgaAdmin=" + ideOrgaAdmin + " ]";
    }
    
}
