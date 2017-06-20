/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soaint.correspondencia.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;
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
@Data
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FUNCIONARIOS")
@NamedQueries({
    @NamedQuery(name = "Funcionarios.findAll", query = "SELECT f FROM Funcionarios f"),
        @NamedQuery(name = "Funcionarios.findByLoginNameAndEstado", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO " +
                "(f.ideFunci, f.codTipDocIdent, f.nroIdentificacion, f.nomFuncionario, f.valApellido1, f.valApellido2, f.codCargo, " +
                "f.corrElectronico, f.codOrgaAdmi, f.loginName, f.estado) " +
                "FROM Funcionarios f " +
                "WHERE TRIM(f.loginName) = TRIM(:LOGIN_NAME) AND TRIM(f.estado) = TRIM(:ESTADO)")})
public class Funcionarios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDE_FUNCI")
    private BigInteger ideFunci;
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
    
}
