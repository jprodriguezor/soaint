package co.com.soaint.foundation.canonical.correspondencia;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
