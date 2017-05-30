/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soaint.correspondencia.domain.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author jrodriguez
 */
@Entity
@Table(name = "FUNCIONARIOS")
@NamedQueries({
    @NamedQuery(name = "Funcionarios.findAll", query = "SELECT f FROM Funcionarios f")})
public class Funcionarios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDE_FUNCI")
    private Long ideFunci;
    @Basic(optional = false)
    @Column(name = "COD_TIP_DOC_IDENT")
    private String codTipDocIdent;
    @Basic(optional = false)
    @Column(name = "NRO_IDENTIFICACION")
    private String nroIdentificacion;
    @Basic(optional = false)
    @Column(name = "NOM_FUNCIONARIO")
    private String nomFuncionario;
    @Basic(optional = false)
    @Column(name = "VAL_APELLIDO1")
    private String valApellido1;
    @Column(name = "VAL_APELLIDO2")
    private String valApellido2;
    @Basic(optional = false)
    @Column(name = "COD_CARGO")
    private String codCargo;
    @Column(name = "CORR_ELECTRONICO")
    private String corrElectronico;
    @Basic(optional = false)
    @Column(name = "COD_ORGA_ADMI")
    private String codOrgaAdmi;
    @Basic(optional = false)
    @Column(name = "LOGIN_NAME")
    private String loginName;
    @Column(name = "ESTADO")
    private String estado;

    public Funcionarios() {
    }

    public Funcionarios(Long ideFunci) {
        this.ideFunci = ideFunci;
    }

    public Funcionarios(Long ideFunci, String codTipDocIdent, String nroIdentificacion, String nomFuncionario, String valApellido1, String codCargo, String codOrgaAdmi, String loginName) {
        this.ideFunci = ideFunci;
        this.codTipDocIdent = codTipDocIdent;
        this.nroIdentificacion = nroIdentificacion;
        this.nomFuncionario = nomFuncionario;
        this.valApellido1 = valApellido1;
        this.codCargo = codCargo;
        this.codOrgaAdmi = codOrgaAdmi;
        this.loginName = loginName;
    }

    public Long getIdeFunci() {
        return ideFunci;
    }

    public void setIdeFunci(Long ideFunci) {
        this.ideFunci = ideFunci;
    }

    public String getCodTipDocIdent() {
        return codTipDocIdent;
    }

    public void setCodTipDocIdent(String codTipDocIdent) {
        this.codTipDocIdent = codTipDocIdent;
    }

    public String getNroIdentificacion() {
        return nroIdentificacion;
    }

    public void setNroIdentificacion(String nroIdentificacion) {
        this.nroIdentificacion = nroIdentificacion;
    }

    public String getNomFuncionario() {
        return nomFuncionario;
    }

    public void setNomFuncionario(String nomFuncionario) {
        this.nomFuncionario = nomFuncionario;
    }

    public String getValApellido1() {
        return valApellido1;
    }

    public void setValApellido1(String valApellido1) {
        this.valApellido1 = valApellido1;
    }

    public String getValApellido2() {
        return valApellido2;
    }

    public void setValApellido2(String valApellido2) {
        this.valApellido2 = valApellido2;
    }

    public String getCodCargo() {
        return codCargo;
    }

    public void setCodCargo(String codCargo) {
        this.codCargo = codCargo;
    }

    public String getCorrElectronico() {
        return corrElectronico;
    }

    public void setCorrElectronico(String corrElectronico) {
        this.corrElectronico = corrElectronico;
    }

    public String getCodOrgaAdmi() {
        return codOrgaAdmi;
    }

    public void setCodOrgaAdmi(String codOrgaAdmi) {
        this.codOrgaAdmi = codOrgaAdmi;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideFunci != null ? ideFunci.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Funcionarios)) {
            return false;
        }
        Funcionarios other = (Funcionarios) object;
        if ((this.ideFunci == null && other.ideFunci != null) || (this.ideFunci != null && !this.ideFunci.equals(other.ideFunci))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.soaint.correspondencia.domain.entity.Funcionarios[ ideFunci=" + ideFunci + " ]";
    }
    
}
