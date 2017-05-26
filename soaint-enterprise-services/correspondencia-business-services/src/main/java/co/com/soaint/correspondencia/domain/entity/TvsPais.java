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
import javax.persistence.*;

/**
 * @author jrodriguez
 */

@Data
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TVS_PAIS")
@NamedQueries({
        @NamedQuery(name = "TvsPais.findAll", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.PaisDTO" +
                "(t.idePais, t.nombrePais, t.codPais) FROM TvsPais t WHERE TRIM(t.estado) = TRIM(:ESTADO)")})
@TableGenerator(name = "TVS_PAIS_GENERATOR", table = "TABLE_GENERATOR", pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE", pkColumnValue = "TVS_PAIS_SEQ", allocationSize = 1)
public class TvsPais implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDE_PAIS")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TVS_PAIS_GENERATOR")
    private BigInteger idePais;
    @Column(name = "NOMBRE_PAIS")
    private String nombrePais;
    @Basic(optional = false)
    @Column(name = "COD_PAIS")
    private String codPais;
    @Column(name = "ESTADO")
    private String estado;
    @Column(name = "FEC_CAMBIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCambio;
    @Column(name = "FEC_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCreacion;
    @Column(name = "COD_USUARIO_CREA")
    private String codUsuarioCrea;

}
