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
    @NamedQuery(name = "PpdDocumento.findAll", query = "SELECT p FROM PpdDocumento p")})
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
    @Column(name = "COD_ASUNTO")
    private String codAsunto;
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
    @Column(name = "COD_TIPO_SOPORTE")
    private String codTipoSoporte;
    @Column(name = "COD_EST_ARCHIVADO")
    private String codEstArchivado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ppdDocumento")
    private List<PpdTrazDocumento> ppdTrazDocumentoList;
    @OneToMany(mappedBy = "ppdDocumento")
    private List<CorAnexo> corAnexoList;
    @JoinColumn(name = "IDE_DOCUMENTO", referencedColumnName = "IDE_DOCUMENTO")
    @ManyToOne
    private CorCorrespondencia corCorrespondencia;
    
}
