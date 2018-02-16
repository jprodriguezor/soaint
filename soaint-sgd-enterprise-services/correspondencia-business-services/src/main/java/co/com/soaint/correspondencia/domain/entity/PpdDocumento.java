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
import java.util.List;
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
@Table(name = "PPD_DOCUMENTO")
@NamedQueries({
    @NamedQuery(name = "PpdDocumento.findAll", query = "SELECT p FROM PpdDocumento p"),
        @NamedQuery(name = "PpdDocumento.findByIdeDocumento", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.PpdDocumentoDTO " +
                "(p.idePpdDocumento, p.codTipoDoc, p.fecDocumento, p.asunto, p.nroFolios, p.nroAnexos, p.codEstDoc, p.ideEcm) " +
                "FROM PpdDocumento p " +
                "INNER JOIN p.corCorrespondencia co " +
                "WHERE co.ideDocumento = :IDE_DOCUMENTO"),
        @NamedQuery(name = "PpdDocumento.findIdePpdDocumentoByIdeDocumento", query = "SELECT p.idePpdDocumento " +
                "FROM PpdDocumento p " +
                "INNER JOIN p.corCorrespondencia co " +
                "WHERE co.ideDocumento = :IDE_DOCUMENTO"),
        @NamedQuery(name = "PpdDocumento.findIdePpdDocumentoByNroRadicado", query = "SELECT p.idePpdDocumento " +
                "FROM PpdDocumento p " +
                "INNER JOIN p.corCorrespondencia co " +
                "WHERE co.nroRadicado = :NRO_RADICADO"),
        @NamedQuery(name = "PpdDocumento.findPpdDocumentoByNroRadicado", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.PpdDocumentoDTO " +
                "(p.idePpdDocumento, p.codTipoDoc, p.fecDocumento, p.asunto, p.nroFolios, p.nroAnexos, p.codEstDoc, p.ideEcm) " +
                "FROM PpdDocumento p " +
                "WHERE TRIM(p.corCorrespondencia.nroRadicado) = TRIM(:NRO_RADICADO)"),
        @NamedQuery(name = "PpdDocumento.updateIdEcm", query = "UPDATE PpdDocumento p " +
                "SET p.ideEcm = :IDE_ECM " +
                "WHERE p.idePpdDocumento = :IDE_PPDDOCUMENTO")})
@javax.persistence.TableGenerator(name = "PPD_DOCUMENTO_GENERATOR", table = "TABLE_GENERATOR", pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE", pkColumnValue = "PPD_DOCUMENTO_SEQ", allocationSize = 1)
public class PpdDocumento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PPD_DOCUMENTO_GENERATOR")
    @Column(name = "IDE_PPD_DOCUMENTO")
    private BigInteger idePpdDocumento;
    @Column(name = "COD_TIPO_DOC")
    private String codTipoDoc;
    @Basic(optional = false)
    @Column(name = "FEC_DOCUMENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecDocumento;
    @Basic(optional = false)
    @Column(name = "ASUNTO")
    private String asunto;
    @Column(name = "NRO_FOLIOS")
    private Long nroFolios;
    @Column(name = "NRO_ANEXOS")
    private Long nroAnexos;
    @Column(name = "COD_EST_DOC")
    private String codEstDoc;
    @Basic(optional = false)
    @Column(name = "FEC_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCreacion;
    @Column(name = "IDE_ECM")
    private String ideEcm;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ppdDocumento")
    private List<PpdTrazDocumento> ppdTrazDocumentoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ppdDocumento")
    private List<CorAnexo> corAnexoList;
    @JoinColumn(name = "IDE_DOCUMENTO", referencedColumnName = "IDE_DOCUMENTO")
    @ManyToOne
    private CorCorrespondencia corCorrespondencia;
    
}
