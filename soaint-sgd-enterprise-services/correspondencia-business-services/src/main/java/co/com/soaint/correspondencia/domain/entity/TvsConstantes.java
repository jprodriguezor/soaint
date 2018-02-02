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
    @NamedQuery(name = "TvsConstantes.findAll", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.ConstanteDTO" +
            "(t.ideConst, t.codigo, t.nombre, t.codPadre) FROM TvsConstantes t WHERE TRIM(t.estado) = TRIM(:ESTADO)"),
        @NamedQuery(name = "TvsConstantes.findAllByCodigoAndEstado", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.ConstanteDTO" +
                "(t.ideConst, t.codigo, t.nombre, t.codPadre) " +
                "FROM TvsConstantes t " +
                "WHERE TRIM(t.codigo) = TRIM(:CODIGO) AND TRIM(t.estado) = TRIM(:ESTADO)"),
        @NamedQuery(name = "TvsConstantes.findAllByCodPadreAndEstado", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.ConstanteDTO" +
                "(t.ideConst, t.codigo, t.nombre, t.codPadre) FROM TvsConstantes t WHERE TRIM(t.codPadre) = TRIM(:COD_PADRE) AND TRIM(t.estado) = TRIM(:ESTADO)"),
        @NamedQuery(name = "TvsConstantes.findAllByCodigo", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.ConstanteDTO" +
                "(t.ideConst, t.codigo, t.nombre, t.codPadre) " +
                "FROM TvsConstantes t " +
                "WHERE t.codigo IN :CODIGOS"),
        @NamedQuery(name = "TvsConstantes.findByCodigo", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.ConstanteDTO" +
                "(t.ideConst, t.codigo, t.nombre, t.codPadre) " +
                "FROM TvsConstantes t " +
                "WHERE t.codigo = :CODIGO")})
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
