package co.com.soaint.foundation.canonical.correspondencia;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created:05-Sep-2017
 * Author: esachez
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@ToString
public class ItemReportPlanillaDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private String nroRadicado;
    private Date fecRadicado;
    private String indOriginal;
    private String nroDocumento;
    private String nombreRemitente;
    private String dependenciaOrigen;
    private String asunto;
    private String nroFolios;
    private String nroAnexos;
}
