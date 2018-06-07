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
import java.util.stream.Collectors;

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
        @NamedQuery(name = "CorCorrespondencia.getIdeInstanciaPorRadicado", query = "SELECT c.ideInstancia " +
                "FROM CorCorrespondencia c WHERE c.nroRadicado = :NRO_RADICADO"),
        @NamedQuery(name = "CorCorrespondencia.findByIdeAgente", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.CorrespondenciaDTO " +
                "(c.ideDocumento, c.descripcion, c.tiempoRespuesta, c.codUnidadTiempo, c.codMedioRecepcion, c.fecRadicado, " +
                "c.nroRadicado, c.codTipoCmc, c.reqDistFisica, c.ideInstancia, c.codFuncRadica, " +
                "c.codSede, c.codDependencia, c.reqDigita, c.nroGuia, c.codEmpMsj, c.fecVenGestion, c.codEstado) " +
                "FROM CorCorrespondencia c " +
                "INNER JOIN c.corAgenteList a " +
                "WHERE a.ideAgente = :IDE_AGENTE"),
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
//        @NamedQuery(name = "CorCorrespondencia.findByPeriodoAndCodDependenciaAndCodTipoDocAndNroRadicado", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.CorrespondenciaDTO " +
//                "(c.ideDocumento, c.descripcion, c.tiempoRespuesta, c.codUnidadTiempo, c.codMedioRecepcion, c.fecRadicado, " +
//                "c.nroRadicado, c.codTipoCmc, c.reqDistFisica, c.ideInstancia, c.codFuncRadica, " +
//                "c.codSede, c.codDependencia, c.reqDigita, c.nroGuia, c.codEmpMsj, c.fecVenGestion, c.codEstado) " +
//                "FROM CorCorrespondencia c " +
//                "INNER JOIN c.ppdDocumentoList d " +
//                "INNER JOIN c.corAgenteList ca " +
//                "WHERE c.reqDistFisica = :REQ_DIST_FISICA " +
//                "AND ((:FECHA_INI IS NULL OR c.fecRadicado >= :FECHA_INI) AND (:FECHA_FIN IS NULL OR c.fecRadicado < :FECHA_FIN)) " +
//                "AND (:COD_DEPENDENCIA IS NULL OR ca.codDependencia = :COD_DEPENDENCIA) AND ca.codTipAgent = :COD_TIP_AGENT " +
//                "AND (:ESTADO_DISTRIBUCION IS NULL OR  ca.estadoDistribucion = :ESTADO_DISTRIBUCION) " +
//                "AND (:COD_TIPO_DOC IS NULL OR d.codTipoDoc = :COD_TIPO_DOC) AND (:NRO_RADICADO IS NULL OR c.nroRadicado LIKE :NRO_RADICADO)"),
        @NamedQuery(name = "CorCorrespondencia.findByComunicacionsSalidaConDistribucionFisicaNroPlantillaNoAsociado", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.CorrespondenciaDTO " +
                "(c.ideDocumento, c.descripcion, c.tiempoRespuesta, c.codUnidadTiempo, c.codMedioRecepcion, c.fecRadicado, " +
                "c.nroRadicado, c.codTipoCmc, c.reqDistFisica, c.ideInstancia, c.codFuncRadica, " +
                "c.codSede, c.codDependencia, c.reqDigita, c.nroGuia, c.codEmpMsj, c.fecVenGestion, c.codEstado) " +
                "FROM CorCorrespondencia c " +
                "INNER JOIN c.corAgenteList ca " +
                "INNER JOIN c.ppdDocumentoList d " +
                "WHERE c.reqDistFisica = :REQ_DIST_FISICA AND ((:TIPO_COM1 IS NULL OR c.codTipoCmc = :TIPO_COM1) OR (:TIPO_COM2 IS NULL OR c.codTipoCmc = :TIPO_COM2)) "+
                "AND ((:FECHA_INI IS NULL OR c.fecRadicado >= :FECHA_INI) AND (:FECHA_FIN IS NULL OR c.fecRadicado < :FECHA_FIN)) "+
                "AND c.codClaseEnvio = :CLASE_ENVIO AND c.codModalidadEnvio = :MOD_ENVIO "+
                "AND (:COD_DEPENDENCIA IS NULL OR ca.codDependencia = :COD_DEPENDENCIA) "+
                "AND (ca.estadoDistribucion IS NULL OR  ca.estadoDistribucion = :ESTADO_DISTRIBUCION) AND ca.codTipAgent = :TIPO_AGENTE " +
                "AND (:COD_TIPO_DOC IS NULL OR d.codTipoDoc = :COD_TIPO_DOC) AND (:NRO_RADICADO IS NULL OR c.nroRadicado LIKE :NRO_RADICADO) " +
                "ORDER BY c.codDependencia, c.codTipoCmc, c.nroRadicado"),
        @NamedQuery(name = "CorCorrespondencia.findByComunicacionsSalidaConDistribucionFisicaNroPlantillaAsociado", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.CorrespondenciaDTO " +
                "(c.ideDocumento, c.descripcion, c.tiempoRespuesta, c.codUnidadTiempo, c.codMedioRecepcion, c.fecRadicado, " +
                "c.nroRadicado, c.codTipoCmc, c.reqDistFisica, c.ideInstancia, c.codFuncRadica, " +
                "c.codSede, c.codDependencia, c.reqDigita, c.nroGuia, c.codEmpMsj, c.fecVenGestion, c.codEstado) " +
                "FROM CorCorrespondencia c " +
                "INNER JOIN c.corAgenteList ca " +
                "INNER JOIN c.ppdDocumentoList d " +
                "WHERE c.reqDistFisica = :REQ_DIST_FISICA AND ((:TIPO_COM1 IS NULL OR c.codTipoCmc = :TIPO_COM1) OR (:TIPO_COM2 IS NULL OR c.codTipoCmc = :TIPO_COM2)) "+
                "AND ((:FECHA_INI IS NULL OR c.fecRadicado >= :FECHA_INI) AND (:FECHA_FIN IS NULL OR c.fecRadicado < :FECHA_FIN)) "+
                "AND c.codClaseEnvio = :CLASE_ENVIO AND c.codModalidadEnvio = :MOD_ENVIO "+
                "AND (:COD_DEPENDENCIA IS NULL OR ca.codDependencia = :COD_DEPENDENCIA) "+
                "AND ca.estadoDistribucion = :ESTADO_DISTRIBUCION AND ca.codTipAgent = :TIPO_AGENTE " +
                "AND (:COD_TIPO_DOC IS NULL OR d.codTipoDoc = :COD_TIPO_DOC) AND (:NRO_RADICADO IS NULL OR c.nroRadicado LIKE :NRO_RADICADO) " +
                "ORDER BY c.codDependencia, c.codTipoCmc, c.nroRadicado"),
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
@javax.persistence.TableGenerator(name = "COR_CORRESPONDENCIA_GENERATOR", table = "TABLE_GENERATOR", pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE", pkColumnValue = "COR_CORRESPONDENCIA_SEQ", allocationSize = 1)
public class CorCorrespondencia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "COR_CORRESPONDENCIA_GENERATOR")
    @Column(name = "IDE_DOCUMENTO")
    private BigInteger ideDocumento;
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Column(name = "TIEMPO_RESPUESTA")
    private String tiempoRespuesta;
    @Column(name = "COD_UNIDAD_TIEMPO")
    private String codUnidadTiempo;
    @Column(name = "COD_MEDIO_RECEPCION")
    private String codMedioRecepcion;
    @Basic(optional = false)
    @Column(name = "FEC_RADICADO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecRadicado;
    @Basic(optional = false)
    @Column(name = "NRO_RADICADO")
    private String nroRadicado;
    @Column(name = "COD_TIPO_CMC")
    private String codTipoCmc;
    @Column(name = "IDE_INSTANCIA")
    private String ideInstancia;
    @Column(name = "REQ_DIST_FISICA")
    private String reqDistFisica;
    @Column(name = "COD_FUNC_RADICA")
    private String codFuncRadica;
    @Column(name = "COD_SEDE")
    private String codSede;
    @Column(name = "COD_DEPENDENCIA")
    private String codDependencia;
    @Column(name = "REQ_DIGITA")
    private String reqDigita;
    @Column(name = "COD_EMP_MSJ")
    private String codEmpMsj;
    @Column(name = "NRO_GUIA")
    private String nroGuia;
    @Column(name = "FEC_VEN_GESTION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecVenGestion;
    @Column(name = "COD_ESTADO")
    private String codEstado;
    @Column(name = "COD_CLASE_ENVIO")
    private String codClaseEnvio;
    @Column(name = "COD_MODALIDAD_ENVIO")
    private String codModalidadEnvio;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "corCorrespondencia", orphanRemoval = true)
    private List<CorAgente> corAgenteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "corCorrespondencia", orphanRemoval = true)
    private List<DctAsignacion> dctAsignacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "corCorrespondencia", orphanRemoval = true)
    private List<CorPlanAgen> corPlanAgenList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "corCorrespondencia", orphanRemoval = true)
    private List<PpdDocumento> ppdDocumentoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "corCorrespondencia", orphanRemoval = true)
    private List<DctAsigUltimo> dctAsigUltimoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "corCorrespondencia", orphanRemoval = true)
    private List<CorReferido> corReferidoList;


    public void removeAsignacionByAgente(CorAgente agente) {
        List<DctAsignacion> dctAsignacions = dctAsignacionList.stream()
                .filter(dctAsignacion -> dctAsignacion.getCorAgente().getIdeAgente().equals(agente.getIdeAgente()))
                .collect(Collectors.toList());
        dctAsignacions.forEach(dctAsignacion -> dctAsignacionList.remove(dctAsignacion));
    }
}
