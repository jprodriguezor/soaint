package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.apis.delegator.BpmApiClient;
import co.com.soaint.correspondencia.apis.delegator.CorrespondenciaApiClient;
import co.com.soaint.correspondencia.apis.delegator.EcmApiClient;
import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import co.com.soaint.foundation.canonical.correspondencia.ComunicacionOficialDTO;
import co.com.soaint.foundation.canonical.ecm.DocumentoDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public void digitalizarDocumento()throws SystemException{
        String nroRadicado = "1000EE2018000011";
        try {
            ComunicacionOficialDTO comunicacionOficial = correspondenciaApiClient.obtenerCorrespondenciaPorNroRadicado(nroRadicado);

            //------------- Dummy -------------------
            Path docPath = Paths.get("/home/tmp/presentacion_contenidos_empresa_alfresco.pdf");

            String fileName = docPath.toFile().getName();
            String fileType = Files.probeContentType(docPath);

            byte[] bytes = Files.readAllBytes(docPath);
            String encodedFile = Base64.getEncoder().encodeToString(bytes);

            //-------------------------------------------

            bytes = Base64.getDecoder().decode(encodedFile);

            DocumentoDTO documento = DocumentoDTO.newInstance()
                    .nroRadicado(comunicacionOficial.getCorrespondencia().getNroRadicado())
                    .sede(comunicacionOficial.getCorrespondencia().getCodSede())
                    .dependencia(comunicacionOficial.getCorrespondencia().getCodDependencia())
                    .nombreDocumento(fileName)
                    .documento(bytes)
                    .tipoDocumento(fileType)
                    .build();
            MensajeRespuesta respuestaEcm = ecmApiClient.uploadDocument(documento, comunicacionOficial.getCorrespondencia().getCodTipoCmc());

            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put("nombreSennal", "estadoDigitalizacion");
            parametros.put("idInstancia", comunicacionOficial.getCorrespondencia().getIdeInstancia());
            parametros.put("ideEcm", respuestaEcm.getDocumentoDTOList().get(0).getIdDocumento());

            EntradaProcesoDTO entradaProceso = EntradaProcesoDTO.newInstance()
                    .idDespliegue("co.com.soaint.sgd.process:proceso-gestor-devoluciones:1.0.0-SNAPSHOT")
                    .parametros(parametros)
                    .build();

            bpmApiClient.enviarSennal(entradaProceso);
        } catch (IOException io){
            System.out.print(io.getMessage());
        }
    }
}
