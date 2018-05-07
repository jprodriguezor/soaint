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
    @NamedQuery(name = "TvsSolicitudUM.obtenerSolicitudUnidadDocumentalSedeDependenciaIntervalo", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.SolicitudUnidadDocumentalDTO " +
            "(t.ideSolicitud, t.id, t.idConstante, t.fecHora, t.nombreUD, t.descriptor1, t.descriptor2, t.nro, t.codSerie, t.codSubserie, t.codSede,"+
            " t.codDependencia, t.idSolicitante, t.estado, t.accion, t.observaciones)" +
            "FROM TvsSolicitudUnidadDocumental t " +
            "WHERE t.codDependencia = :COD_DEP AND t.codSede = :COD_SEDE AND t.fecHora >= :FECHA_INI AND t.fecHora <= :FECHA_FIN"),
    @NamedQuery(name = "TvsSolicitudUM.actualizarSolicitudUnidadDocumental", query = "UPDATE TvsSolicitudUnidadDocumental t " +
            "SET  t.id = :ID, t.idConstante = :ID_CONST , t.fecHora = :FECH, t.nombreUD = :NOMBREUD, t.descriptor1 = :DESCP1, "+
            "t.descriptor2 = :DESCP2, t.nro = :NRO, t.codSerie = :COD_SER, t.codSubserie = :COD_SUBS, t.codSede = :COD_SED, t.codDependencia = :COD_DEP, "+
            "t.idSolicitante = :ID_SOL, t.estado = :EST , t.accion = :ACC , t.observaciones = :OBS " +
            "WHERE t.ideSolicitud = :IDE_SOL")
        })
@javax.persistence.TableGenerator(name = "COR_SOLICITUD_GENERATOR", table = "TABLE_GENERATOR", pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE", pkColumnValue = "COR_SOLICITUD_SEQ", allocationSize = 1)
public class TvsSolicitudUnidadDocumental implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "COR_SOLICITUD_GENERATOR")
    @Column(name = "ID")
    private BigInteger ideSolicitud;
    @Basic(optional = false)
    @Column(name = "ID_UM")
    private String id;
    @Column(name = "ID_CONSTANTE")
    private String idConstante;
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
