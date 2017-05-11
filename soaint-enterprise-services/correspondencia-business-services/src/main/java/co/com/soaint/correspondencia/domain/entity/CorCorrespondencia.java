/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soaint.correspondencia.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author jrodriguez
 */

@Data
@Builder(builderMethodName="newInstance")
@AllArgsConstructor
@Entity
@Table(name = "COR_CORRESPONDENCIA")
public class CorCorrespondencia implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "IDE_DOCUMENTO")
    private BigInteger ideDocumento;

    @Column(name = "DESCRIPCION")
    private String descripcion;

    @Column(name = "TIEMPO_RESPUESTA")
    private BigInteger tiempoRespuesta;

    @Column(name = "COD_UNIDAD_TIEMPO")
    private String codUnidadTiempo;

    @Column(name = "COD_MEDIO_RECEPCION")
    private String codMedioRecepcion;

    @Column(name = "FEC_RADICADO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecRadicado;

    @Column(name = "NRO_RADICADO")
    private String nroRadicado;

    @Column(name = "FEC_DOCUMENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecDocumento;

    @Column(name = "COD_TIPO_DOC")
    private String codTipoDoc;

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

    @OneToMany(mappedBy = "ideDocumento")
    private List<CorAgente> corAgenteList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideDocumento")
    private List<DctAsignacion> dctAsignacionList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idePpdDocumento")
    private List<PpdTrazDocumento> ppdTrazDocumentoList;

    @OneToMany(mappedBy = "ideDocumento")
    private List<CorAnexo> corAnexoList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideDocumento")
    private List<CorPlanAgen> corPlanAgenList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideDocumento")
    private List<PpdDocumento> ppdDocumentoList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideDocumento")
    private List<DctAsigUltimo> dctAsigUltimoList;

    @OneToMany(mappedBy = "ideDocumento")
    private List<CorReferido> corReferidoList;

    public CorCorrespondencia() {
        super();
    }


    public CorCorrespondencia(BigInteger ideDocumento, String descripcion, BigInteger tiempoRespuesta) {
        this.ideDocumento = ideDocumento;
        this.descripcion = descripcion;
        this.tiempoRespuesta = tiempoRespuesta;
    }
}
