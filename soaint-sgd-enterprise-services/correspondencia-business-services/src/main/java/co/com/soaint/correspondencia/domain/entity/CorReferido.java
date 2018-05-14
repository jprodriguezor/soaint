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
@Table(name = "COR_REFERIDO")
@NamedQueries({
    @NamedQuery(name = "CorReferido.findAll", query = "SELECT c FROM CorReferido c"),
        @NamedQuery(name = "CorReferido.findByIdeDocumento", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.ReferidoDTO " +
                "(c.ideReferido, c.nroRadRef) " +
                "FROM CorReferido c " +
                "INNER JOIN c.corCorrespondencia co " +
                "WHERE co.ideDocumento = :IDE_DOCUMENTO"),
        @NamedQuery(name = "CorReferido.findByNroRadicadoCorrespodenciaReferida", query = "SELECT co.nroRadicado " +
                "FROM CorReferido cr " +
                "INNER JOIN cr.corCorrespondencia co " +
                "WHERE TRIM(cr.nroRadRef) = TRIM(:NRO_RAD) ")
})
@javax.persistence.TableGenerator(name = "COR_REFERIDO_GENERATOR", table = "TABLE_GENERATOR", pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE", pkColumnValue = "COR_REFERIDO_SEQ", allocationSize = 1)
public class CorReferido implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "COR_REFERIDO_GENERATOR")
    @Column(name = "IDE_REFERIDO")
    private BigInteger ideReferido;
    @Column(name = "NRO_RAD_REF")
    private String nroRadRef;
    @JoinColumn(name = "IDE_DOCUMENTO", referencedColumnName = "IDE_DOCUMENTO")
    @ManyToOne
    private CorCorrespondencia corCorrespondencia;

}
