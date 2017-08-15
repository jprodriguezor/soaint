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
@Data
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TVS_DEPARTAMENTO")
@NamedQueries({
    @NamedQuery(name = "TvsDepartamento.findAll", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.DepartamentoDTO" +
            "(t.ideDepar, t.nombreDepar, t.codDepar, t.codPais) " +
            "FROM TvsDepartamento t " +
            "WHERE TRIM(t.auditColumns.estado) = TRIM(:ESTADO)"),
        @NamedQuery(name = "TvsDepartamento.findAllByCodPaisAndEstado", query = "SELECT  NEW co.com.soaint.foundation.canonical.correspondencia.DepartamentoDTO" +
                "(t.ideDepar, t.nombreDepar, t.codDepar, t.codPais) " +
                "FROM TvsDepartamento t " +
                "WHERE TRIM(t.codPais) = TRIM(:COD_PAIS) AND TRIM(t.auditColumns.estado) = TRIM(:ESTADO)")})
public class TvsDepartamento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDE_DEPAR")
    private BigInteger ideDepar;
    @Column(name = "NOMBRE_DEPAR")
    private String nombreDepar;
    @Column(name = "COD_DEPAR")
    private String codDepar;
    @Column(name = "COD_PAIS")
    private String codPais;
    private AuditColumns auditColumns;
    
}
