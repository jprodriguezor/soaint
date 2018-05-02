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

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;

/**
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
                "(t.ideMunic, t.nombreMunic, t.codMunic) " +
                "FROM TvsMunicipio t " +
                "WHERE TRIM(t.auditColumns.estado) = TRIM(:ESTADO)"),
        @NamedQuery(name = "TvsMunicipio.findAllByCodDeparAndEstado", query = "SELECT  NEW co.com.soaint.foundation.canonical.correspondencia.MunicipioDTO" +
                "(t.ideMunic, t.nombreMunic, t.codMunic ) " +
                "FROM TvsMunicipio t " +
                "INNER JOIN t.departamento d " +
                "WHERE TRIM(d.codDepar) = TRIM(:COD_DEPAR) AND TRIM(t.auditColumns.estado) = TRIM(:ESTADO)"),
        @NamedQuery(name = "TvsMunicipio.findAllByCodigos", query = "SELECT  NEW co.com.soaint.foundation.canonical.correspondencia.MunicipioDTO" +
                "(t.ideMunic, t.nombreMunic, t.codMunic) " +
                "FROM TvsMunicipio t " +
                "WHERE TRIM(t.codMunic) IN :CODIGOS"),
        @NamedQuery(name = "TvsMunicipio.findByCodMun", query = "SELECT  NEW co.com.soaint.foundation.canonical.correspondencia.MunicipioDTO" +
                "(t.ideMunic, t.nombreMunic, t.codMunic) " +
                "FROM TvsMunicipio t " +
                "WHERE TRIM(t.codMunic) = TRIM(:COD_MUN)")})
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
    @JoinColumn(name = "COD_DEPAR", referencedColumnName = "COD_DEPAR")
    @ManyToOne
    private TvsDepartamento departamento;
    @Embedded
    private AuditColumns auditColumns;

}
