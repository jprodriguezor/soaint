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
import java.util.Date;
import java.util.List;
import javax.persistence.*;

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
                "(f.ideFunci, f.codTipDocIdent, f.nroIdentificacion, f.nomFuncionario, f.valApellido1, f.valApellido2, " +
                "f.corrElectronico, f.loginName, f.auditColumns.estado) " +
                "FROM Funcionarios f " +
                "WHERE TRIM(f.loginName) = TRIM(:LOGIN_NAME) AND TRIM(f.auditColumns.estado) = TRIM(:ESTADO)"),
        @NamedQuery(name = "Funcionarios.findAllByCodOrgaAdmiAndEstado", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO " +
                "(f.ideFunci, f.codTipDocIdent, f.nroIdentificacion, f.nomFuncionario, f.valApellido1, f.valApellido2, " +
                "f.corrElectronico, f.loginName, f.auditColumns.estado) " +
                "FROM Funcionarios f " +
                "INNER JOIN f.tvsOrgaAdminXFunciPkList o " +
                "WHERE TRIM(f.auditColumns.estado) = TRIM(:ESTADO) AND o.tvsOrgaAdminXFunciPkPk.codOrgaAdmi = :COD_ORGA_ADMI")})
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
    @Column(name = "CORR_ELECTRONICO")
    private String corrElectronico;
    @Basic(optional = false)
    @Column(name = "LOGIN_NAME")
    private String loginName;
    private AuditColumns auditColumns;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "funcionarios")
    private List<TvsOrgaAdminXFunciPk> tvsOrgaAdminXFunciPkList;
    
}
