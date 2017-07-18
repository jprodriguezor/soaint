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
import javax.persistence.*;

/**
 *
 * @author jrodriguez
 */
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "TVS_DATOS_CONTACTO")
@NamedQueries({
    @NamedQuery(name = "TvsDatosContacto.findAll", query = "SELECT t FROM TvsDatosContacto t"),
        @NamedQuery(name = "TvsDatosContacto.findByIdeAgente", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.DatosContactoDTO " +
                "(t.ideContacto,  t.nroViaGeneradora, t.nroPlaca, t.codTipoVia, t.codPrefijoCuadrant, t.codPostal, t.direccion, t.celular, " +
                "t.telFijo, t.extension, t.corrElectronico, t.codPais, t.codDepartamento, t.codMunicipio, " +
                "t.provEstado, t.principal) " +
                "FROM TvsDatosContacto t INNER JOIN t.corAgente co " +
                "WHERE co.ideAgente = :IDE_AGENTE")})
@javax.persistence.TableGenerator(name = "TVS_DATOS_CONTACTO_GENERATOR", table = "TABLE_GENERATOR", pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE", pkColumnValue = "TVS_DATOS_CONTACTO_SEQ", allocationSize = 1)
public class TvsDatosContacto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TVS_DATOS_CONTACTO_GENERATOR")
    @Column(name = "IDE_CONTACTO")
    private BigInteger ideContacto;
    @Column(name = "NRO_VIA_GENERADORA")
    private String nroViaGeneradora;
    @Column(name = "NRO_PLACA")
    private String nroPlaca;
    @Column(name = "COD_TIPO_VIA")
    private String codTipoVia;
    @Column(name = "COD_PREFIJO_CUADRANT")
    private String codPrefijoCuadrant;
    @Column(name = "COD_POSTAL")
    private String codPostal;
    @Column(name = "DIRECCION")
    private String direccion;
    @Column(name = "CELULAR")
    private String celular;
    @Column(name = "TEL_FIJO")
    private String telFijo;
    @Column(name = "EXTENSION")
    private String extension;
    @Column(name = "CORR_ELECTRONICO")
    private String corrElectronico;
    @Column(name = "COD_PAIS")
    private String codPais;
    @Column(name = "COD_DEPARTAMENTO")
    private String codDepartamento;
    @Column(name = "COD_MUNICIPIO")
    private String codMunicipio;
    @Column(name = "PROV_ESTADO")
    private String provEstado;
    @Column(name = "PRINCIPAL")
    private String principal;
    @JoinColumn(name = "IDE_AGENTE", referencedColumnName = "IDE_AGENTE")
    @ManyToOne(optional = false)
    private CorAgente corAgente;

}
