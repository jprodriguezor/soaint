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
 *
 * @author jrodriguez
 */
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "PPD_TRAZ_DOCUMENTO")
@NamedQueries({
    @NamedQuery(name = "PpdTrazDocumento.findAll", query = "SELECT p FROM PpdTrazDocumento p")})
@javax.persistence.TableGenerator(name = "PPD_TRAZ_DOCUMENTO_GENERATOR", table = "TABLE_GENERATOR", pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE", pkColumnValue = "PPD_TRAZ_DOCUMENTO_SEQ", allocationSize = 1)
public class PpdTrazDocumento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PPD_TRAZ_DOCUMENTO_GENERATOR")
    @Column(name = "IDE_TRAZ_DOCUMENTO")
    private BigInteger ideTrazDocumento;
    @Basic(optional = false)
    @Column(name = "FEC_TRAZ_DOCUMENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecTrazDocumento;
    @Column(name = "OBSERVACION")
    private String observacion;
    @Column(name = "IDE_FUNCI")
    private Long ideFunci;
    @Column(name = "COD_ESTADO")
    private String codEstado;
    @Column(name = "IDE_DOCUMENTO")
    private BigInteger ideDocumento;
    @Column(name = "COD_ORGA_ADMIN")
    private String codOrgaAdmin;
    @JoinColumn(name = "IDE_PPD_DOCUMENTO", referencedColumnName = "IDE_PPD_DOCUMENTO")
    @ManyToOne(optional = false)
    private PpdDocumento ppdDocumento;

}
