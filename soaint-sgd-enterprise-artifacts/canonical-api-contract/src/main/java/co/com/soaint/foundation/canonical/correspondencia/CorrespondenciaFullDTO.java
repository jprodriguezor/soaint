package co.com.soaint.foundation.canonical.correspondencia;


import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created:4-May-2017
 * Author: jrodriguez
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/correspondencia/1.0.0")
@ToString
public class CorrespondenciaFullDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private BigInteger ideDocumento;
    private String descripcion;
    private String tiempoRespuesta;
    private String codUnidadTiempo;
    private String descUnidadTiempo;
    private String codMedioRecepcion;
    private String descMedioRecepcion;
    private Date fecRadicado;
    private String nroRadicado;
    private Date fecDocumento;
    private String codTipoDoc;
    private String descTipoDoc;
    private String codTipoCmc;
    private String descTipoCmc;
    private String ideInstancia;
    private String reqDistFisica;
    private String codFuncRadica;
    private String descFuncRadica;
    private String codSede;
    private String descSede;
    private String codDependencia;
    private String descDependencia;
    private String reqDigita;
    private String codEmpMsj;
    private String descEmpMsj;
    private String nroGuia;
    private Date fecVenGestion;
    private String codEstado;
    private String descEstado;
    private String inicioConteo;
    private String codClaseEnvio;
    private String descClaseEnvio;
    private String codModalidadEnvio;
    private String descModalidadEnvio;

    public CorrespondenciaFullDTO(BigInteger ideDocumento, String descripcion, String tiempoRespuesta,
                              String codUnidadTiempo, String descUnidadTiempo, String codMedioRecepcion, String descMedioRecepcion, Date fecRadicado, String nroRadicado,
                              Date fecDocumento, String codTipoDoc, String descTipoDoc, String codTipoCmc, String descTipoCmc, String reqDistFisica,
                              String ideInstancia, String codFuncRadica, String descFuncRadica, String codSede, String descSede, String codDependencia, String descDependencia,
                              String reqDigita, String nroGuia, String codEmpMsj, String descEmpMsj, Date fecVenGestion, String codEstado, String descEstado) {
        this.ideDocumento = ideDocumento;
        this.descripcion = descripcion;
        this.tiempoRespuesta = tiempoRespuesta;
        this.codUnidadTiempo = codUnidadTiempo;
        this.descUnidadTiempo = descUnidadTiempo;
        this.codMedioRecepcion = codMedioRecepcion;
        this.descMedioRecepcion = descMedioRecepcion;
        this.fecRadicado = fecRadicado;
        this.nroRadicado = nroRadicado;
        this.fecDocumento = fecDocumento;
        this.codTipoDoc = codTipoDoc;
        this.descTipoDoc = descTipoDoc;
        this.codTipoCmc = codTipoCmc;
        this.descTipoCmc = descTipoCmc;
        this.reqDistFisica = reqDistFisica;
        this.ideInstancia = ideInstancia;
        this.codFuncRadica = codFuncRadica;
        this.descFuncRadica = descFuncRadica;
        this.codSede = codSede;
        this.descSede = descSede;
        this.codDependencia = codDependencia;
        this.descDependencia = descDependencia;
        this.reqDigita = reqDigita;
        this.nroGuia = nroGuia;
        this.codEmpMsj = codEmpMsj;
        this.descEmpMsj = descEmpMsj;
        this.fecVenGestion = fecVenGestion;
        this.codEstado = codEstado;
        this.descEstado = descEstado;
    }
}
