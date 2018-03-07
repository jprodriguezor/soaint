package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.apis.delegator.BpmApiClient;
import co.com.soaint.correspondencia.apis.delegator.CorrespondenciaApiClient;
import co.com.soaint.correspondencia.apis.delegator.EcmApiClient;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.correspondencia.ComunicacionOficialDTO;
import co.com.soaint.foundation.canonical.correspondencia.DependenciaDTO;
import co.com.soaint.foundation.canonical.ecm.DocumentoDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.canonical.integration.DigitalizacionDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 06-Mar-2018
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
@Log4j2
public class DigitalizacionControl {

    @Autowired
    CorrespondenciaApiClient correspondenciaApiClient;
    @Autowired
    EcmApiClient ecmApiClient;
    @Autowired
    BpmApiClient bpmApiClient;

    public void digitalizarDocumento(DigitalizacionDTO digitalizacionDTO)throws SystemException{
        try {
            ComunicacionOficialDTO comunicacionOficial = correspondenciaApiClient.obtenerCorrespondenciaPorNroRadicado(digitalizacionDTO.getNroRadicado());

            byte[] bytes = Base64.getDecoder().decode(digitalizacionDTO.getEncodedFile());

            DependenciaDTO dependencia = correspondenciaApiClient.consultarDependenciaByCodigo(comunicacionOficial.getCorrespondencia().getCodDependencia());

            DocumentoDTO documento = DocumentoDTO.newInstance()
                    .nroRadicado(comunicacionOficial.getCorrespondencia().getNroRadicado())
                    .sede(dependencia.getNomSede())
                    .dependencia(dependencia.getNomDependencia())
                    .nombreDocumento(digitalizacionDTO.getFileName())
                    .documento(bytes)
                    .tipoDocumento(digitalizacionDTO.getFileType())
                    .build();
            MensajeRespuesta respuestaEcm = ecmApiClient.uploadDocument(documento, comunicacionOficial.getCorrespondencia().getCodTipoCmc());

            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put("nombreSennal", "estadoDigitalizacion");
            parametros.put("ideEcm", respuestaEcm.getDocumentoDTOList().get(0).getIdDocumento());

            EntradaProcesoDTO entradaProceso = EntradaProcesoDTO.newInstance()
                    .idDespliegue("co.com.soaint.sgd.process:proceso-correspondencia-entrada:1.3.1-SNAPSHOT")
                    .instanciaProceso(new Long(comunicacionOficial.getCorrespondencia().getIdeInstancia()))
                    .parametros(parametros)
                    .build();

            bpmApiClient.enviarSennal(entradaProceso);
        } catch (Exception io){
            System.out.print(io.getMessage());
        }
    }
}
