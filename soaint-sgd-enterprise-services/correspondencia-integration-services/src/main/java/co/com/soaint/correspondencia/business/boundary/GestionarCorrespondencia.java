package co.com.soaint.correspondencia.business.boundary;

import co.com.soaint.correspondencia.apis.delegator.CorrespondenciaApiClient;
import co.com.soaint.correspondencia.apis.delegator.DependenciaApiClient;
import co.com.soaint.correspondencia.apis.delegator.DocumentoApiClient;
import co.com.soaint.correspondencia.apis.delegator.EcmApiClient;
import co.com.soaint.correspondencia.business.control.FacturaElectronicaControl;
import co.com.soaint.correspondencia.business.control.FirmaDigital;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.canonical.correspondencia.constantes.EstadoAgenteEnum;
import co.com.soaint.foundation.canonical.correspondencia.constantes.EstadoCorrespondenciaEnum;
import co.com.soaint.foundation.canonical.correspondencia.constantes.TipoAgenteEnum;
import co.com.soaint.foundation.canonical.correspondencia.constantes.TipoRemitenteEnum;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.canonical.integration.FileBase64DTO;
import co.com.soaint.foundation.framework.annotations.BusinessBoundary;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dom4j.Document;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 04-Apr-2018
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: BOUNDARY - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessBoundary
@NoArgsConstructor
@Log4j2
public class GestionarCorrespondencia {

    @Autowired
    private CorrespondenciaApiClient correspondenciaApiClient;
    @Autowired
    private EcmApiClient ecmApiClient;
    @Autowired
    private DocumentoApiClient documentoApiClient;
    @Autowired
    private FacturaElectronicaControl facturaElectronicaControl;
    @Autowired
    private DependenciaApiClient dependenciaApiClient;
    @Autowired
    private FirmaDigital firmaDigital;

    public void radicarFacturaElectronica(FileBase64DTO xmlDocument) throws SystemException {

        //--------------------Parsear Base64 a Document---------------
        Document facturaElectronica = facturaElectronicaControl.parseBase64AsDocument(xmlDocument.getBase64());
        Map<String, Object> metadatosFactura = facturaElectronicaControl.obtenerMetadatos(facturaElectronica);

        //--------------------Correspondencia-------------------------
        String tiempoRespuesta = "1";
        String codUnidadTiempo = "UNID-TID";
        String codMedioRecepcion = "ME-RECSE";
        String codTipoDoc = "TL-DOCF";
        String codTipoCmc = "EE";

        CorrespondenciaDTO correspondenciaDTO = CorrespondenciaDTO.newInstance()
                .descripcion("Factura Electrónica No. - " + metadatosFactura.get("noFactura"))
                .tiempoRespuesta(tiempoRespuesta)
                .codUnidadTiempo(codUnidadTiempo)
                .codMedioRecepcion(codMedioRecepcion)
                .codTipoDoc(codTipoDoc)
                .codTipoCmc(codTipoCmc)
                .reqDistFisica("0")
                .codSede("1000")
                .codDependencia("10001040")
                .reqDigita("1")
                .inicioConteo("DSH")
                .codFuncRadica("1")
                .build();

        //--------------------Documento-------------------------

        PpdDocumentoDTO ppdDocumentoDTO = PpdDocumentoDTO.newInstance()
                .asunto("Factura Electrónica No. - " + metadatosFactura.get("noFactura"))
                .nroAnexos(new Long("1"))
                .nroFolios(new Long("2"))
                .fecDocumento(new Date())
                .codTipoDoc(codTipoDoc)
                .build();

        List<PpdDocumentoDTO> ppdDocumentoDTOList = new ArrayList<>();
        ppdDocumentoDTOList.add(ppdDocumentoDTO);

        //--------------------Agentes---------------------------

        AgenteDTO remitente = AgenteDTO.newInstance()
                .codTipAgent(TipoAgenteEnum.REMITENTE.getCodigo())
                .codTipoRemite(TipoRemitenteEnum.EXTERNO.getCodigo())
                .codTipoPers("TP-PERPJ")
                .razonSocial((String) metadatosFactura.get("nombreRemitente"))
                .codEstado(EstadoAgenteEnum.SIN_ASIGNAR.getCodigo())
                .build();

        AgenteDTO destinatario = AgenteDTO.newInstance()
                .codTipAgent(TipoAgenteEnum.DESTINATARIO.getCodigo())
                .codSede("1000")
                .codDependencia("10001010")
                .indOriginal("TP-DESP")
                .build();

        List<AgenteDTO> agentes = new ArrayList<>();
        agentes.add(remitente);
        agentes.add(destinatario);

        //--------------------Anexos-------------------------

        AnexoDTO anexoDTO = AnexoDTO.newInstance()
                .codAnexo("ANE-OT")
                .codTipoSoporte("TP-SOPE")
                .descripcion("Factura Electrónica No. - " + metadatosFactura.get("noFactura"))
                .build();

        List<AnexoDTO> anexoDTOList = new ArrayList<>();
        anexoDTOList.add(anexoDTO);

        //--------------------Comunicacion Oficial-----------

        ComunicacionOficialDTO comunicacionOficialDTO = ComunicacionOficialDTO.newInstance()
                .correspondencia(correspondenciaDTO)
                .ppdDocumentoList(ppdDocumentoDTOList)
                .agenteList(agentes)
                .anexoList(anexoDTOList)
                .referidoList(new ArrayList<>())
                .datosContactoList(new ArrayList<>())
                .build();

        comunicacionOficialDTO = correspondenciaApiClient.radicar(comunicacionOficialDTO).readEntity(ComunicacionOficialDTO.class);

        DependenciaDTO dependenciaDTO = dependenciaApiClient.obtenerPorCodigo(comunicacionOficialDTO.getCorrespondencia().getCodDependencia()).readEntity(DependenciaDTO.class);

        co.com.soaint.foundation.canonical.ecm.DocumentoDTO documentoECM = co.com.soaint.foundation.canonical.ecm.DocumentoDTO.newInstance()
                .nombreDocumento(comunicacionOficialDTO.getCorrespondencia().getNroRadicado().concat(".pdf"))
                .codigoSede(dependenciaDTO.getCodSede())
                .sede(dependenciaDTO.getNomSede())
                .codigoDependencia(dependenciaDTO.getCodDependencia())
                .dependencia(dependenciaDTO.getNomDependencia())
                .nroRadicado(comunicacionOficialDTO.getCorrespondencia().getNroRadicado())
                .documento(firmaDigital.signPDF(facturaElectronicaControl.getPDF(facturaElectronica, comunicacionOficialDTO)))
                .tipologiaDocumental(codTipoDoc)
                .tipoDocumento("application/pdf")
                .build();
        MensajeRespuesta respuestaEcm = ecmApiClient.uploadDocument(documentoECM, codTipoCmc);

        co.com.soaint.foundation.canonical.ecm.DocumentoDTO anexo = co.com.soaint.foundation.canonical.ecm.DocumentoDTO.newInstance()
                .nombreDocumento(comunicacionOficialDTO.getCorrespondencia().getNroRadicado().concat(".xml"))
                .codigoSede(dependenciaDTO.getCodSede())
                .sede(dependenciaDTO.getNomSede())
                .codigoDependencia(dependenciaDTO.getCodDependencia())
                .dependencia(dependenciaDTO.getNomDependencia())
                .nroRadicado(comunicacionOficialDTO.getCorrespondencia().getNroRadicado())
                .documento(Base64.getDecoder().decode(xmlDocument.getBase64()))
                .tipologiaDocumental(codTipoDoc)
                .tipoDocumento("text/xml")
                .idDocumentoPadre(respuestaEcm.getDocumentoDTOList().get(0).getIdDocumento())
                .build();
        ecmApiClient.uploadDocument(anexo, codTipoCmc);

        log.info("-------- Respuesta Ecm ----" + respuestaEcm.getCodMensaje() + "   " + respuestaEcm.getMensaje());

        documentoApiClient.actualizaReferenciaEcm(DocumentoDTO.newInstance()
                .nroRadicado(comunicacionOficialDTO.getCorrespondencia().getNroRadicado())
                .ideEcm(respuestaEcm.getDocumentoDTOList().get(0).getIdDocumento())
                .build());

        correspondenciaApiClient.actualizarEstado(CorrespondenciaDTO.newInstance()
                .nroRadicado(comunicacionOficialDTO.getCorrespondencia().getNroRadicado())
                .codEstado(EstadoCorrespondenciaEnum.ASIGNACION.getCodigo())
                .build());

    }

}
