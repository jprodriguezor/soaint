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
@Table(name = "TVS_CONSTANTES")
@NamedQueries({
    @NamedQuery(name = "TvsConstantes.findAll", query = "SELECT t FROM TvsConstantes t"),
        @NamedQuery(name = "TvsConstantes.findAllByCodigo", query = "SELECT t FROM TvsConstantes t WHERE t.codigo = :CODIGO")})
public class TvsConstantes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDE_CONST")
    private BigInteger ideConst;
    @Column(name = "CODIGO")
    private String codigo;
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "COD_PADRE")
    private String codPadre;
    @Column(name = "ESTADO")
    private String estado;
    
}
