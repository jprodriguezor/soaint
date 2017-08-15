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
@Table(name = "TVS_MUNICIPIO")
@NamedQueries({
    @NamedQuery(name = "TvsMunicipio.findAll", query = "SELECT  NEW co.com.soaint.foundation.canonical.correspondencia.MunicipioDTO" +
            "(t.ideMunic, t.nombreMunic, t.codMunic, t.codDepar) " +
            "FROM TvsMunicipio t " +
            "WHERE TRIM(t.auditColumns.estado) = TRIM(:ESTADO)"),
        @NamedQuery(name = "TvsMunicipio.findAllByCodDeparAndEstado", query = "SELECT  NEW co.com.soaint.foundation.canonical.correspondencia.MunicipioDTO" +
                "(t.ideMunic, t.nombreMunic, t.codMunic, t.codDepar) " +
                "FROM TvsMunicipio t " +
                "WHERE TRIM(t.codDepar) = TRIM(:COD_DEPAR) AND TRIM(t.auditColumns.estado) = TRIM(:ESTADO)")})
public class TvsMunicipio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDE_MUNIC")
    private BigInteger ideMunic;
    @Column(name = "NOMBRE_MUNIC")
    private String nombreMunic;
    @Column(name = "COD_MUNIC")
    private String codMunic;
    @Basic(optional = false)
    @Column(name = "COD_DEPAR")
    private String codDepar;
    private AuditColumns auditColumns;
    
}
