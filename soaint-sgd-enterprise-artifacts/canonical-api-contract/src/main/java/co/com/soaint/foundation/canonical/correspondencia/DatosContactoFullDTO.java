package co.com.soaint.foundation.canonical.correspondencia;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created:15-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Data
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/cor-agente/1.0.0")
@ToString
public class DatosContactoFullDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private BigInteger ideContacto;
    private String nroViaGeneradora;
    private String nroPlaca;
    private String codTipoVia;
    private String descTipoVia;
    private String codPrefijoCuadrant;
    private String descPrefijoCuadrant;
    private String codPostal;
    private String direccion;
    private String celular;
    private String telFijo;
    private String extension;
    private String corrElectronico;
    private String codPais;
    private PaisDTO pais;
    private String codDepartamento;
    private DepartamentoDTO departamento;
    private String codMunicipio;
    private MunicipioDTO municipio;
    private String provEstado;
    private String principal;
    private String ciudad;
}
