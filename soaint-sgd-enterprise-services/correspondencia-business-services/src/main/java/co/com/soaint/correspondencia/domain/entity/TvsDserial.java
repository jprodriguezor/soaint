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
import java.util.Date;

/**
 *
 * @author jrodriguez
 */
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "TVS_DSERIAL")
@NamedQueries({
    @NamedQuery(name = "TvsDserial.findAll", query = "SELECT t FROM TvsDserial t"),
        @NamedQuery(name = "TvsDserial.consultarConsecutivoRadicado", query = "SELECT MAX(t.valConsecutivoRad) " +
                "FROM TvsDserial t " +
                "WHERE t.codSede = :COD_SEDE AND t.codCmc = :COD_CMC AND t.valAno = :ANNO AND NOT t.valConsecutivoRad BETWEEN :RESERVADO_I AND :RESERVADO_F " +
                "GROUP BY t.codSede, t.codCmc, t.valAno")
})
@javax.persistence.TableGenerator(name = "TVS_DSERIAL_GENERATOR", table = "TABLE_GENERATOR", pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE", pkColumnValue = "TVS_DSERIAL_SEQ", allocationSize = 1)
public class TvsDserial implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TVS_DSERIAL_GENERATOR")
    @Column(name = "IDE_SERIAL")
    private BigInteger ideSerial;
    @Basic(optional = false)
    @Column(name = "COD_SEDE")
    private String codSede;
    @Column(name = "COD_GRUPO")
    private String codGrupo;
    @Column(name = "COD_DEPENDENCIA")
    private String codDependencia;
    @Column(name = "COD_CMC")
    private String codCmc;
    @Basic(optional = false)
    @Column(name = "VAL_ANO")
    private String valAno;
    @Column(name = "COD_FUNC_RADICA")
    private String codFuncRadica;
    @Column(name = "FEC_CREA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCrea;
    @Column(name = "IND_TIPO_SERIAL")
    private String indTipoSerial;
    @Column(name = "VAL_CONSECUTIVO_RAD")
    private String valConsecutivoRad;
    @Column(name = "VAL_CONSECUTIVO_PL")
    private String valConsecutivoPl;
    @Column(name = "COD_TIP_CONSECUTIVO")
    private String codTipConsecutivo;
    @Column(name = "VAL_SERIAL_RAD")
    private String valSerialRad;
    @Column(name = "VAL_SERIAL_PL")
    private String valSerialPl;
    
}
