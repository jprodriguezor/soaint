package co.com.soaint.foundation.canonical.correspondencia;


import lombok.Builder;
import lombok.Data;

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
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/correspondencia/1.0.0")
public class CorrespondenciaDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private BigInteger ideDocumento;
    private String descripcion;
    private BigInteger tiempoRespuesta;
    private String codUnidadTiempo;
    private String codMedioRecepcion;
    private Date fecRadicado;
    private String nroRadicado;
    private Date fecDocumento;
    private String codTipoDoc;
    private String codTipoCmc;
    private String ideInstancia;
    private String reqDistFisica;
    private String codFuncRadica;
    private String codSede;
    private String codDependencia;
    private String reqDigita;
    private String codEmpMsj;
    private String nroGuia;
    private Date fecVenGestion;
    private String codEstado;

    public CorrespondenciaDTO() {
        super();
    }

    public CorrespondenciaDTO(BigInteger ideDocumento, String descripcion, BigInteger tiempoRespuesta,
                              String codUnidadTiempo, String codMedioRecepcion, Date fecRadicado, String nroRadicado,
                              Date fecDocumento, String codTipoDoc, String codTipoCmc, String reqDistFisica,
                              String ideInstancia, String codFuncRadica, String codSede, String codDependencia,
                              String reqDigita, String nroGuia, String codEmpMsj, Date fecVenGestion, String codEstado) {
        this.ideDocumento = ideDocumento;
        this.descripcion = descripcion;
        this.tiempoRespuesta = tiempoRespuesta;
        this.codUnidadTiempo = codUnidadTiempo;
        this.codMedioRecepcion = codMedioRecepcion;
        this.fecRadicado = fecRadicado;
        this.nroRadicado = nroRadicado;
        this.fecDocumento = fecDocumento;
        this.codTipoDoc = codTipoDoc;
        this.codTipoCmc = codTipoCmc;
        this.reqDistFisica = reqDistFisica;
        this.ideInstancia = ideInstancia;
        this.codFuncRadica = codFuncRadica;
        this.codSede = codSede;
        this.codDependencia = codDependencia;
        this.reqDigita = reqDigita;
        this.nroGuia = nroGuia;
        this.codEmpMsj = codEmpMsj;
        this.fecVenGestion = fecVenGestion;
        this.codEstado = codEstado;
    }
}
