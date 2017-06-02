package co.com.soaint.foundation.canonical.correspondencia;

import lombok.Builder;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
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
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/ppd-documento/1.0.0")
public class PpdDocumentoDTO {
    private Long idePpdDocumento;
    private String codTipoDoc;
    private Date fecDocumento;
    private String codAsunto;
    private Long nroFolios;
    private Long nroAnexos;
    private String codEstDoc;
    private String ideEcm;
    private String codTipoSoporte;
    private String codEstArchivado;

    public PpdDocumentoDTO(){super();}

    public PpdDocumentoDTO(Long idePpdDocumento, String codTipoDoc, Date fecDocumento, String codAsunto, Long nroFolios,
                           Long nroAnexos, String codEstDoc, String ideEcm, String codTipoSoporte, String codEstArchivado){
        this.idePpdDocumento = idePpdDocumento;
        this.codTipoDoc = codTipoDoc;
        this.fecDocumento = fecDocumento;
        this.codAsunto = codAsunto;
        this.nroFolios = nroFolios;
        this.nroAnexos = nroAnexos;
        this.codEstDoc = codEstDoc;
        this.ideEcm = ideEcm;
        this.codTipoSoporte = codTipoSoporte;
        this.codEstArchivado = codEstArchivado;
    }
}
