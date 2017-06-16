package co.com.soaint.foundation.canonical.correspondencia;

import lombok.Builder;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

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
public class PpdDocumentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private BigInteger idePpdDocumento;
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

    public PpdDocumentoDTO(BigInteger idePpdDocumento, String codTipoDoc, Date fecDocumento, String codAsunto, Long nroFolios,
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
