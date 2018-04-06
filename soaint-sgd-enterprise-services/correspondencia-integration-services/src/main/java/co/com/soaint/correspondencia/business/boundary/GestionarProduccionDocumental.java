package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.apis.delegator.DependenciaApiClient;
import co.com.soaint.correspondencia.apis.delegator.EcmApiClient;
import co.com.soaint.correspondencia.apis.delegator.TareaApiClient;
import co.com.soaint.correspondencia.business.control.FirmaDigital;
import co.com.soaint.correspondencia.business.control.RedesSocialesIntegration;
import co.com.soaint.foundation.canonical.correspondencia.DependenciaDTO;
import co.com.soaint.foundation.canonical.correspondencia.TareaDTO;
import co.com.soaint.foundation.canonical.ecm.DocumentoDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import com.google.gson.Gson;
import com.itextpdf.html2pdf.HtmlConverter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.Base64;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 05-Apr-2018
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessBoundary
@NoArgsConstructor
@Log4j2
public class GestionarProduccionDocumental {

    @Autowired
    private TareaApiClient tareaApiClient;
    @Autowired
    private EcmApiClient ecmApiClient;
    @Autowired
    private FirmaDigital firmaDigital;
    @Autowired
    private DependenciaApiClient dependenciaApiClient;
    @Autowired
    private RedesSocialesIntegration redesSocialesIntegration;

    public String producirVersionFinal(String idInstanciaProceso) throws SystemException, IOException {
        FileInputStream fileInputStream = null;
        log.info("Procesando Instancia de Proceso -------------- > " + idInstanciaProceso);
        try {
            Gson gson = new Gson();
            TareaDTO tareaDTO = tareaApiClient.consultaDataTarea(idInstanciaProceso, "0000").readEntity(TareaDTO.class);
            JSONObject respuestaJson = new JSONObject(gson.toJson(tareaDTO.getPayload()));
            JSONObject datosGenerales = (JSONObject) respuestaJson.get("datosGenerales");
            JSONArray listaDocumentos = datosGenerales.getJSONArray("listaVersionesDocumento");
            JSONObject documento = (JSONObject) listaDocumentos.get(listaDocumentos.length() - 1);

            //MensajeRespuesta mensajeRespuestaEcm = ecmApiClient.findByIdDocument(documento.getString("id"));

            //DocumentoDTO documentoEcm = mensajeRespuestaEcm.getDocumentoDTOList().get(0);

            File fileTmp = File.createTempFile("pdftmp.pdf", "");

            log.info(documento.getString("contenido"));

            HtmlConverter.convertToPdf(documento.getString("contenido"), new FileOutputStream(fileTmp.getPath()));
            //HtmlConverter.convertToPdf(new ByteArrayInputStream(documentoEcm.getDocumento()), new FileOutputStream(fileTmp.getPath()));
            fileInputStream = new FileInputStream(fileTmp);
            /*DependenciaDTO dependenciaDTO = dependenciaApiClient.obtenerPorCodigo("10001040").readEntity(DependenciaDTO.class);
            co.com.soaint.foundation.canonical.ecm.DocumentoDTO documentoECM = co.com.soaint.foundation.canonical.ecm.DocumentoDTO.newInstance()
                    .nombreDocumento(documento.getString("id").concat(".pdf"))
                    .codigoSede(dependenciaDTO.getCodSede())
                    .sede(dependenciaDTO.getNomSede())
                    .codigoDependencia(dependenciaDTO.getCodDependencia())
                    .dependencia(dependenciaDTO.getNomDependencia())
                    .documento(firmaDigital.signPDF(IOUtils.toByteArray(fileInputStream)))
                    .tipoDocumento("application/pdf")
                    .build();
            MensajeRespuesta respuestaEcm = ecmApiClient.uploadDocument(documentoECM, "EE");*/

            fileTmp.deleteOnExit();
            return redesSocialesIntegration.publicarFacebook("Documento producido desde SOADOC", Base64.getEncoder().encodeToString(firmaDigital.signPDF(IOUtils.toByteArray(fileInputStream))));
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        } finally {
            if (fileInputStream != null)
                fileInputStream.close();
        }
    }
}
