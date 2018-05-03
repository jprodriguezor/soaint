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
@Table(name = "COR_CORRESPONDENCIA")
@NamedQueries({
    @NamedQuery(name = "CorCorrespondencia.findAll", query = "SELECT c FROM CorCorrespondencia c"),
        @NamedQuery(name = "CorCorrespondencia.findByNroRadicado", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.CorrespondenciaDTO " +
                "(c.ideDocumento, c.descripcion, c.tiempoRespuesta, c.codUnidadTiempo, c.codMedioRecepcion, c.fecRadicado, " +
                "c.nroRadicado, c.codTipoCmc, c.reqDistFisica, c.ideInstancia, c.codFuncRadica, " +
                "c.codSede, c.codDependencia, c.reqDigita, c.nroGuia, c.codEmpMsj, c.fecVenGestion, c.codEstado) " +
                "FROM CorCorrespondencia c WHERE TRIM(c.nroRadicado) = TRIM(:NRO_RADICADO)"),
        @NamedQuery(name = "CorCorrespondencia.findByIdeDocumento", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.CorrespondenciaDTO " +
                "(c.ideDocumento, c.descripcion, c.tiempoRespuesta, c.codUnidadTiempo, c.codMedioRecepcion, c.fecRadicado, " +
                "c.nroRadicado, c.codTipoCmc, c.reqDistFisica, c.ideInstancia, c.codFuncRadica, " +
                "c.codSede, c.codDependencia, c.reqDigita, c.nroGuia, c.codEmpMsj, c.fecVenGestion, c.codEstado) " +
                "FROM CorCorrespondencia c WHERE c.ideDocumento = :IDE_DOCUMENTO"),
        @NamedQuery(name = "CorCorrespondencia.findByIdeAgente", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.CorrespondenciaDTO " +
                "(c.ideDocumento, c.descripcion, c.tiempoRespuesta, c.codUnidadTiempo, c.codMedioRecepcion, c.fecRadicado, " +
                "c.nroRadicado, c.codTipoCmc, c.reqDistFisica, c.ideInstancia, c.codFuncRadica, " +
                "c.codSede, c.codDependencia, c.reqDigita, c.nroGuia, c.codEmpMsj, c.fecVenGestion, c.codEstado) " +
                "FROM CorCorrespondencia c " +
                "INNER JOIN c.corAgenteList a " +
                "WHERE a.ideAgente = :IDE_AGENTE"),
        @NamedQuery(name = "CorCorrespondencia.findByPeriodoAndCodDependenciaAndCodEstadoAndNroRadicado", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.CorrespondenciaDTO " +
                "(c.ideDocumento, c.descripcion, c.tiempoRespuesta, c.codUnidadTiempo, c.codMedioRecepcion, c.fecRadicado, " +
                "c.nroRadicado, c.codTipoCmc, c.reqDistFisica, c.ideInstancia, c.codFuncRadica, " +
                "c.codSede, c.codDependencia, c.reqDigita, c.nroGuia, c.codEmpMsj, c.fecVenGestion, c.codEstado) " +
                "FROM CorCorrespondencia c " +
                "INNER JOIN c.corAgenteList ca " +
                "WHERE c.fecRadicado BETWEEN :FECHA_INI AND :FECHA_FIN " +
                "AND ca.codEstado = :COD_EST_AG AND c.codEstado = :COD_ESTADO AND ca.codDependencia = :COD_DEPENDENCIA AND ca.codTipAgent = :COD_TIP_AGENT " +
                "AND (:NRO_RADICADO IS NULL OR c.nroRadicado LIKE :NRO_RADICADO)"),
        @NamedQuery(name = "CorCorrespondencia.findByPeriodoAndCodDependenciaAndCodTipoDocAndNroRadicado", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.CorrespondenciaDTO " +
                "(c.ideDocumento, c.descripcion, c.tiempoRespuesta, c.codUnidadTiempo, c.codMedioRecepcion, c.fecRadicado, " +
                "c.nroRadicado, c.codTipoCmc, c.reqDistFisica, c.ideInstancia, c.codFuncRadica, " +
                "c.codSede, c.codDependencia, c.reqDigita, c.nroGuia, c.codEmpMsj, c.fecVenGestion, c.codEstado) " +
                "FROM CorCorrespondencia c " +
                "INNER JOIN c.ppdDocumentoList d " +
                "INNER JOIN c.corAgenteList ca " +
                "WHERE c.fecRadicado BETWEEN :FECHA_INI AND :FECHA_FIN " +
                "AND c.reqDistFisica = :REQ_DIST_FISICA AND ca.codDependencia = :COD_DEPENDENCIA AND ca.codTipAgent = :COD_TIP_AGENT " +
                "AND ca.estadoDistribucion = :ESTADO_DISTRIBUCION " +
                "AND (:COD_TIPO_DOC IS NULL OR d.codTipoDoc = :COD_TIPO_DOC) AND (:NRO_RADICADO IS NULL OR c.nroRadicado LIKE :NRO_RADICADO)"),
        @NamedQuery(name = "CorCorrespondencia.findIdeDocumentoByNroRadicado", query = "SELECT c.ideDocumento " +
                "FROM CorCorrespondencia c " +
                "WHERE TRIM(c.nroRadicado) = TRIM(:NRO_RADICADO)"),
        @NamedQuery(name = "CorCorrespondencia.countByNroRadicado", query = "SELECT COUNT(*) " +
                "FROM CorCorrespondencia c " +
                "WHERE TRIM(c.nroRadicado) = TRIM(:NRO_RADICADO)"),
        @NamedQuery(name = "CorCorrespondencia.maxNroRadicadoByCodSedeAndCodTipoCMC", query = "SELECT MAX(c.nroRadicado) " +
                "FROM CorCorrespondencia c " +
                "WHERE TRIM(c.codSede) = TRIM(:COD_SEDE) AND TRIM(c.codTipoCmc) = TRIM(:COD_TIPO_CMC) " +
                "AND NOT c.nroRadicado BETWEEN :RESERVADO_INI AND :RESERVADO_FIN "),
        @NamedQuery(name = "CorCorrespondencia.findFechaVenGestionByIdeDocumento", query = "SELECT c.fecVenGestion " +
                "FROM CorCorrespondencia c " +
                "WHERE c.ideDocumento = :IDE_DOCUMENTO "),
        @NamedQuery(name = "CorCorrespondencia.updateEstado", query = "UPDATE CorCorrespondencia c " +
                "SET c.codEstado = :COD_ESTADO " +
                "WHERE TRIM(c.nroRadicado) = TRIM(:NRO_RADICADO)"),
        @NamedQuery(name = "CorCorrespondencia.updateIdeInstancia", query = "UPDATE CorCorrespondencia c " +
                "SET c.ideInstancia = :IDE_INSTANCIA " +
                "WHERE TRIM(c.nroRadicado) = TRIM(:NRO_RADICADO)")})
@TableGenerator(name = "COR_CORRESPONDENCIA_GENERATOR", table = "TABLE_GENERATOR", pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE", pkColumnValue = "COR_CORRESPONDENCIA_SEQ", allocationSize = 1)
public class TvsSolicitudUnidadDocumental implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "COR_CORRESPONDENCIA_GENERATOR")
    @Column(name = "IDE_SOLICITUD")
    private BigInteger ideSolicitud;
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
    private Date observaciones;
}
