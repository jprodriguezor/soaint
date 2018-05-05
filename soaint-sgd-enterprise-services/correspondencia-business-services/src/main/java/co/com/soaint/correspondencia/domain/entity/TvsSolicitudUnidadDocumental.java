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
import java.util.List;

/**
 *
 * @author jrodriguez
 */
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "TVS_SOLICITUD_UD")
@NamedQueries({
    @NamedQuery(name = "TvsSolicitudUM.findAll", query = "SELECT t FROM TvsSolicitudUnidadDocumental t"),
    @NamedQuery(name = "TvsSolicitudUM.obtenerSolicitudUnidadDocumentalSedeDependenciaIntervalo", query = "SELECT t FROM TvsSolicitudUnidadDocumental t " +
            "WHERE t.codDependencia = :COD_DEP AND t.codDependencia = :COD_SEDE AND t.fecHora BETWEEN :FECHA_INI AND :FECHA_FIN")
        })
@TableGenerator(name = "COR_CORRESPONDENCIA_GENERATOR", table = "TABLE_GENERATOR", pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE", pkColumnValue = "COR_CORRESPONDENCIA_SEQ", allocationSize = 1)
public class TvsSolicitudUnidadDocumental implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "COR_CORRESPONDENCIA_GENERATOR")
    @Column(name = "IDE_SOLICITUD")
    private String ideSolicitud;
    @Basic(optional = false)
    @Column(name = "ID")
    private String id;
    @Column(name = "ID_CONSTANTE")
    private String idConstante;
    @Basic(optional = false)
    @Column(name = "FEC_HORA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecHora;
    @Basic(optional = false)
    @Column(name = "NOMBRE_UNIDAD_DOCUMENTAL")
    private String nombreUD;
    @Basic(optional = false)
    @Column(name = "DESCRIPTOR1")
    private String descriptor1;
    @Basic(optional = false)
    @Column(name = "DESCRIPTOR2")
    private String descriptor2;
    @Column(name = "NRO")
    private String nro;
    @Basic(optional = false)
    @Column(name = "CODIGO_SERIE")
    private String codSerie;
    @Basic(optional = false)
    @Column(name = "CODIGO_SUBSERIE")
    private String codSubserie;
    @Basic(optional = false)
    @Column(name = "CODIGO_SEDE")
    private String codSede;
    @Column(name = "CODIGO_DEPENDENCIA")
    @Basic(optional = false)
    private String codDependencia;
    @Basic(optional = false)
    @Column(name = "ID_SOLICITANTE")
    private String idSolicitante;
    @Basic(optional = false)
    @Column(name = "ESTADO")
    private String estado;
    @Column(name = "ACCION")
    private String accion;
    @Column(name = "OBSERVACIONES")
    private String observaciones;
}
