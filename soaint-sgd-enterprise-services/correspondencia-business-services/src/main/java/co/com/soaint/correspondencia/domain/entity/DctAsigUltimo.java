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
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author jrodriguez
 */
@Data
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DCT_ASIG_ULTIMO")
@NamedQueries({
    @NamedQuery(name = "DctAsigUltimo.findAll", query = "SELECT d FROM DctAsigUltimo d"),
        @NamedQuery(name = "DctAsigUltimo.findByIdeAgente", query = "SELECT NEW co.com.soaint.correspondencia.domain.entity.DctAsigUltimo " +
                "(d.ideAsigUltimo, d.numRedirecciones, d.ideUsuarioCreo, d.fecCreo, d.nivLectura, " +
                "d.nivEscritura, d.fechaVencimiento, d.idInstancia, d.codTipProceso) " +
                "FROM DctAsigUltimo d " +
                "INNER JOIN d.corAgente c " +
                "WHERE c.ideAgente = :IDE_AGENTE "),
        @NamedQuery(name = "DctAsigUltimo.findByIdeAsignacion", query = "SELECT NEW co.com.soaint.correspondencia.domain.entity.DctAsigUltimo " +
                "(d.ideAsigUltimo, d.numRedirecciones, d.ideUsuarioCreo, d.fecCreo, d.nivLectura, " +
                "d.nivEscritura, d.fechaVencimiento, d.idInstancia, d.codTipProceso) " +
                "FROM DctAsigUltimo d " +
                "INNER JOIN d.dctAsignacion a " +
                "WHERE a.ideAsignacion = :IDE_ASIGNACION "),
        @NamedQuery(name = "DctAsigUltimo.findByIdeFunciAndNroRadicado", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.DctAsigUltimoDTO " +
                "(d.ideAsigUltimo, d.numRedirecciones, d.nivLectura, d.nivEscritura, d.fechaVencimiento, d.idInstancia, d.codTipProceso, a.ideAsignacion) " +
                "FROM DctAsigUltimo d " +
                "INNER JOIN d.dctAsignacion a " +
                "INNER JOIN d.corCorrespondencia c " +
                "WHERE a.ideFunci = :IDE_FUNCI AND d.idInstancia IS NULL AND (:NRO_RADICADO IS NULL OR c.nroRadicado LIKE :NRO_RADICADO)"),
        @NamedQuery(name = "DctAsigUltimo.findCorrespondenciaByIdeAsigUltimo", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.CorrespondenciaDTO " +
                "(c.ideDocumento, c.descripcion, c.tiempoRespuesta, c.codUnidadTiempo, c.codMedioRecepcion, c.fecRadicado, " +
                "c.nroRadicado, c.codTipoCmc, c.reqDistFisica, c.ideInstancia, c.codFuncRadica, " +
                "c.codSede, c.codDependencia, c.reqDigita, c.nroGuia, c.codEmpMsj, c.fecVenGestion, c.codEstado) " +
                "FROM DctAsigUltimo d " +
                "INNER JOIN d.corCorrespondencia c " +
                "WHERE d.ideAsigUltimo = :IDE_ASIG_ULTIMO"),
        @NamedQuery(name = "DctAsigUltimo.findAgenteByIdeAsigUltimo", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.AgenteDTO " +
                "(c.ideAgente, c.codTipoRemite, c.codTipoPers, c.nombre, c.nroDocumentoIden, c.razonSocial, c.nit, c.codCortesia, " +
                "c.codCargo, c.codEnCalidad, c.codTipDocIdent, c.nroDocuIdentidad, c.codSede, c.codDependencia, c.codFuncRemite, " +
                "c.fecAsignacion, c.ideContacto, c.codTipAgent, c.indOriginal) " +
                "FROM DctAsigUltimo d " +
                "INNER JOIN d.corAgente c " +
                "WHERE d.ideAsigUltimo = :IDE_ASIG_ULTIMO"),
        @NamedQuery(name = "DctAsigUltimo.updateIdInstancia", query = "UPDATE DctAsigUltimo d " +
                "SET d.idInstancia = :ID_INSTANCIA " +
                "WHERE d.ideAsigUltimo = :IDE_ASIG_ULTIMO")})
@javax.persistence.TableGenerator(name = "DCT_ASIG_ULTIMO_GENERATOR", table = "TABLE_GENERATOR", pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE", pkColumnValue = "DCT_ASIG_ULTIMO_SEQ", allocationSize = 1)
public class DctAsigUltimo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "DCT_ASIG_ULTIMO_GENERATOR")
    @Column(name = "IDE_ASIG_ULTIMO")
    private Long ideAsigUltimo;
    @Column(name = "NUM_REDIRECCIONES")
    private String numRedirecciones;
    @Basic(optional = false)
    @Column(name = "IDE_USUARIO_CREO")
    private String ideUsuarioCreo;
    @Basic(optional = false)
    @Column(name = "FEC_CREO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCreo;
    @Basic(optional = false)
    @Column(name = "IDE_USUARIO_CAMBIO")
    private long ideUsuarioCambio;
    @Basic(optional = false)
    @Column(name = "FEC_CAMBIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCambio;
    @Column(name = "NIV_LECTURA")
    private Short nivLectura;
    @Column(name = "NIV_ESCRITURA")
    private Short nivEscritura;
    @Column(name = "FECHA_VENCIMIENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVencimiento;
    @Column(name = "ID_INSTANCIA")
    private String idInstancia;
    @Column(name = "COD_TIP_PROCESO")
    private String codTipProceso;
    @JoinColumn(name = "IDE_AGENTE", referencedColumnName = "IDE_AGENTE")
    @ManyToOne(optional = false)
    private CorAgente corAgente;
    @JoinColumn(name = "IDE_DOCUMENTO", referencedColumnName = "IDE_DOCUMENTO")
    @ManyToOne(optional = false)
    private CorCorrespondencia corCorrespondencia;
    @JoinColumn(name = "IDE_ASIGNACION", referencedColumnName = "IDE_ASIGNACION")
    @ManyToOne(optional = false)
    private DctAsignacion dctAsignacion;

    public DctAsigUltimo(Long ideAsigUltimo, String numRedirecciones, String ideUsuarioCreo, Date fecCreo, Short nivLectura,
                         Short nivEscritura, Date fechaVencimiento, String idInstancia, String codTipProceso){
        this.ideAsigUltimo = ideAsigUltimo;
        this.numRedirecciones = numRedirecciones;
        this.ideUsuarioCreo = ideUsuarioCreo;
        this.fecCreo = fecCreo;
        this.nivLectura = nivLectura;
        this.nivEscritura = nivEscritura;
        this.fechaVencimiento = fechaVencimiento;
        this.idInstancia = idInstancia;
        this.codTipProceso = codTipProceso;

    }

}
