package co.com.soaint.foundation.canonical.correspondencia;

import lombok.Builder;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created:2-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Data
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/dct-asig-ultimo/1.0.0")
public class DctAsigUltimoDTO {
    private Long ideAsigUltimo;
    private String numRedirecciones;
    private Short nivLectura;
    private Short nivEscritura;
    private Date fechaVencimiento;
    private String idInstancia;
    private String codTipProceso;

    public DctAsigUltimoDTO(){super();}

    public DctAsigUltimoDTO(Long ideAsigUltimo, String numRedirecciones, Short nivLectura, Short nivEscritura, Date fechaVencimiento,
                            String idInstancia, String codTipProceso){
        this.ideAsigUltimo = ideAsigUltimo;
        this.numRedirecciones = numRedirecciones;
        this.nivLectura = nivLectura;
        this.nivEscritura = nivEscritura;
        this.fechaVencimiento = fechaVencimiento;
        this.idInstancia = idInstancia;
        this.codTipProceso = codTipProceso;
    }
}
