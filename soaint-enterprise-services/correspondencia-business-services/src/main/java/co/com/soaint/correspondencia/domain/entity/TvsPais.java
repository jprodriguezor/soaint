/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soaint.correspondencia.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

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
 * @author jrodriguez
 */

@Data
@Builder(builderMethodName = "newInstance")
@AllArgsConstructor
@Entity
@Table(name = "TVS_PAIS")
@NamedQueries({
        @NamedQuery(name = "TvsPais.findAll", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.PaisDTO(t.idePais,t.nombrePais,t.codPais) " +
                "FROM TvsPais t WHERE t.estado =:ESTADO ")})
public class TvsPais implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDE_PAIS")
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

    public TvsPais() {
        super();
    }


}
