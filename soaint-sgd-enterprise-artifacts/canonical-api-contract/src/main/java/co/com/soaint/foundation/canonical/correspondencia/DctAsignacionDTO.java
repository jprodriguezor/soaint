package co.com.soaint.foundation.canonical.correspondencia;

import lombok.Builder;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created:6-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@Data
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/dct-asignacion/1.0.0")
public class DctAsignacionDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long ideAsignacion;
    private Date fecAsignacion;
    private Long ideFunci;
    private String codDependencia;
    private String codTipAsignacion;
    private String observaciones;
    private String codTipCausal;
    private String codTipProceso;

    public DctAsignacionDTO(){super();}

    public DctAsignacionDTO(Long ideAsignacion, Date fecAsignacion, Long ideFunci, String codDependencia, String codTipAsignacion, String observaciones,
                            String codTipCausal, String codTipProceso){
        this.ideAsignacion = ideAsignacion;
        this.fecAsignacion = fecAsignacion;
        this.ideFunci = ideFunci;
        this.codDependencia = codDependencia;
        this.codTipAsignacion = codTipAsignacion;
        this.observaciones = observaciones;
        this.codTipCausal = codTipCausal;
        this.codTipProceso = codTipProceso;
    }
}
