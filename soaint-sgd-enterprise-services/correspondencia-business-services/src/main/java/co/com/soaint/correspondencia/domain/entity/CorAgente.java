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
@Table(name = "COR_AGENTE")
@NamedQueries({
    @NamedQuery(name = "CorAgente.findAll", query = "SELECT c FROM CorAgente c"),
        @NamedQuery(name = "CorAgente.findByIdeDocumento", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.AgenteDTO " +
                "(c.ideAgente, c.codTipoRemite, c.codTipoPers, c.nombre, c.nroDocumentoIden, c.razonSocial, c.nit, c.codCortesia, " +
                "c.codCargo, c.codEnCalidad, c.codTipDocIdent, c.nroDocuIdentidad, c.codSede, c.codDependencia, c.codFuncRemite, " +
                "c.fecAsignacion, c.ideContacto, c.codTipAgent, c.indOriginal) " +
                "FROM CorAgente c INNER JOIN c.corCorrespondencia co " +
                "WHERE co.ideDocumento = :IDE_DOCUMENTO"),
@NamedQuery(name = "CorAgente.redireccionarCorrespondencia", query = "UPDATE CorAgente c " +
        "SET c.codSede = :COD_SEDE, c.codDependencia = :COD_DEPENDENCIA, c.codFuncRemite = :COD_FUNC_REMITE, c.fecAsignacion = :FEC_ASIGNCION")})
@javax.persistence.TableGenerator(name = "COR_AGENTE_GENERATOR", table = "TABLE_GENERATOR", pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE", pkColumnValue = "COR_AGENTE_SEQ", allocationSize = 1)
public class CorAgente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "COR_AGENTE_GENERATOR")
    @Column(name = "IDE_AGENTE")
    private BigInteger ideAgente;
    @Column(name = "COD_TIPO_REMITE")
    private String codTipoRemite;
    @Column(name = "COD_TIPO_PERS")
    private String codTipoPers;
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "NRO_DOCUMENTO_IDEN")
    private String nroDocumentoIden;
    @Column(name = "RAZON_SOCIAL")
    private String razonSocial;
    @Column(name = "NIT")
    private String nit;
    @Column(name = "COD_CORTESIA")
    private String codCortesia;
    @Column(name = "COD_CARGO")
    private String codCargo;
    @Column(name = "COD_EN_CALIDAD")
    private String codEnCalidad;
    @Column(name = "COD_TIP_DOC_IDENT")
    private String codTipDocIdent;
    @Column(name = "NRO_DOCU_IDENTIDAD")
    private String nroDocuIdentidad;
    @Column(name = "COD_SEDE")
    private String codSede;
    @Column(name = "COD_DEPENDENCIA")
    private String codDependencia;
    @Column(name = "COD_FUNC_REMITE")
    private String codFuncRemite;
    @Column(name = "COD_ESTADO")
    private String codEstado;
    @Column(name = "FEC_ASIGNACION")
    private String fecAsignacion;
    @Column(name = "IDE_CONTACTO")
    private Long ideContacto;
    @Column(name = "COD_TIP_AGENT")
    private String codTipAgent;
    @Column(name = "IND_ORIGINAL")
    private String indOriginal;
    @JoinColumn(name = "IDE_DOCUMENTO", referencedColumnName = "IDE_DOCUMENTO")
    @ManyToOne
    private CorCorrespondencia corCorrespondencia;
    @OneToMany(mappedBy = "corAgente")
    private List<DctAsignacion> dctAsignacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "corAgente")
    private List<CorPlanAgen> corPlanAgenList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "corAgente")
    private List<TvsDatosContacto> tvsDatosContactoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "corAgente")
    private List<DctAsigUltimo> dctAsigUltimoList;

}
