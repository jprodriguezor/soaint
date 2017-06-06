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
@Table(name = "COR_ANEXO")
@NamedQueries({
    @NamedQuery(name = "CorAnexo.findAll", query = "SELECT c FROM CorAnexo c"),
        @NamedQuery(name = "CorAnexo.findByIdePpdDocumento", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.CorAnexoDTO " +
                "(c.ideAnexo, c.codAnexo, c.descripcion) " +
                "FROM CorAnexo c " +
                "INNER JOIN c.ppdDocumento pp " +
                "WHERE pp.idePpdDocumento = :IDE_PPD_DOCUMENTO")})
@javax.persistence.TableGenerator(name = "COR_ANEXO_GENERATOR", table = "TABLE_GENERATOR", pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE", pkColumnValue = "COR_ANEXO_SEQ", allocationSize = 1)
public class CorAnexo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "COR_ANEXO_GENERATOR")
    @Column(name = "IDE_ANEXO")
    private BigInteger ideAnexo;
    @Column(name = "COD_ANEXO")
    private String codAnexo;
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @JoinColumn(name = "IDE_PPD_DOCUMENTO", referencedColumnName = "IDE_PPD_DOCUMENTO")
    @ManyToOne
    private PpdDocumento ppdDocumento;

}
