package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.apis.delegator.DependenciaApiClient;
import co.com.soaint.correspondencia.utils.Concepto;
import co.com.soaint.correspondencia.utils.Conceptos;
import co.com.soaint.foundation.canonical.correspondencia.ComunicacionOficialDTO;
import co.com.soaint.foundation.canonical.correspondencia.DependenciaDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 05-Apr-2018
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
@Log4j2
public class FacturaElectronicaControl {

    private String reportPath = "/home/wildfly/correspondencia_reports/factura_electronica_viewer.jrxml";
    private String logoPath = "/home/wildfly/correspondencia_reports/jb_logo.png";
    private String logoSmallPath = "/home/wildfly/correspondencia_reports/logoSmall.png";

    @Autowired
    private DependenciaApiClient dependenciaApiClient;

    public Map<String, Object> obtenerMetadatos(Document document) {
        Map<String, Object> metadatos = new HashMap<>();
        metadatos.put("noFactura", document.valueOf("/fe:Invoice/cbc:ID"));
        metadatos.put("nombreRemitente", document.valueOf("/fe:Invoice/fe:AccountingSupplierParty/fe:Party/cac:PartyName/cbc:Name"));
        return metadatos;
    }

    public Document parseBase64AsDocument(String base64Document) throws SystemException {
        try {
            InputStream file = new ByteArrayInputStream(Base64.getDecoder().decode(base64Document));
            StringWriter writer = new StringWriter();
            IOUtils.copy(file, writer, StandardCharsets.UTF_8);
            String xmlString = writer.toString();
            return new SAXReader().read(new StringReader(xmlString));
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public byte[] getPDF(Document documento, ComunicacionOficialDTO comunicacionOficialDTO)throws SystemException{
        try {
        JasperReport report = JasperCompileManager.compileReport(reportPath);
        return JasperRunManager.runReportToPdf(report, getReportParameters(documento, comunicacionOficialDTO), getReportDataSource(documento));
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    private Map<String, Object> getReportParameters(Document document, ComunicacionOficialDTO comunicacionOficialDTO) throws IOException {
        BufferedImage logo = ImageIO.read(new FileImageInputStream(new File(logoPath)));
        BufferedImage logoSmall = ImageIO.read(new FileImageInputStream(new File(logoSmallPath)));

        DependenciaDTO dependenciaDTO = dependenciaApiClient.obtenerPorCodigo(comunicacionOficialDTO.getCorrespondencia().getCodDependencia()).readEntity(DependenciaDTO.class);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("pLogo", logo);
        parameters.put("pLogoSmall", logoSmall);
        parameters.put("pNoRadicado", comunicacionOficialDTO.getCorrespondencia().getNroRadicado());
        parameters.put("pFechaRadicacion", document.valueOf("/fe:Invoice/cbc:IssueDate").concat(" ").concat(document.valueOf("/fe:Invoice/cbc:IssueTime")));
        parameters.put("pRemitente", "SOFTWARE ASSOCIATES S.A.S.");
        parameters.put("pDestinatarioSede", dependenciaDTO.getNomSede());
        parameters.put("pDestinatarioDependencia", dependenciaDTO.getNomDependencia());
        parameters.put("pAnexos", 1);
        parameters.put("pFolios", 2);
        parameters.put("pNoFactura", document.valueOf("/fe:Invoice/cbc:ID"));
        parameters.put("pFechaExpedicion", document.valueOf("/fe:Invoice/cbc:IssueDate").concat(" ").concat(document.valueOf("/fe:Invoice/cbc:IssueTime")));
        parameters.put("pLugarExpedicion", document.valueOf("/fe:Invoice/fe:AccountingSupplierParty/fe:Party/fe:PhysicalLocation/fe:Address/cbc:Department"));
        //proveedor
        parameters.put("pEmisorRFC", document.valueOf("/fe:Invoice/fe:AccountingSupplierParty/fe:Party/fe:PartyLegalEntity/cbc:RegistrationName"));
        parameters.put("pEmisorResidenciaFiscal", document.valueOf("/fe:Invoice/fe:AccountingSupplierParty/fe:Party/fe:PhysicalLocation/fe:Address/cac:AddressLine/cbc:Line").trim());
        parameters.put("pEmisorNombre", document.valueOf("/fe:Invoice/fe:AccountingSupplierParty/fe:Party/cac:PartyName/cbc:Name"));
        //cliente
        parameters.put("pReceptorRFC", document.valueOf("/fe:Invoice/fe:AccountingCustomerParty/fe:Party/fe:PartyLegalEntity/cbc:RegistrationName"));
        parameters.put("pReceptorNombre", document.valueOf("/fe:Invoice/fe:AccountingCustomerParty/fe:Party/cac:PartyName/cbc:Name"));
        parameters.put("pReceptorResidenciaFiscal", document.valueOf("/fe:Invoice/fe:AccountingCustomerParty/fe:Party/fe:PhysicalLocation/fe:Address/cac:AddressLine/cbc:Line").trim());
        parameters.put("pNoCertificado", "NoCertificado");
        parameters.put("pSubTotal", new BigDecimal(document.valueOf("/fe:Invoice/fe:LegalMonetaryTotal/cbc:LineExtensionAmount")));
        parameters.put("pDescuento", new BigDecimal(document.valueOf("/fe:Invoice/fe:LegalMonetaryTotal/cbc:TaxExclusiveAmount")));
        parameters.put("pImpuestosTraslado", BigDecimal.ZERO);
        parameters.put("pImpuestosRetenidos", BigDecimal.ZERO);
        parameters.put("pTotal", new BigDecimal(document.valueOf("/fe:Invoice/fe:LegalMonetaryTotal/cbc:PayableAmount")));
        parameters.put("pFechaTimbrado", document.valueOf("/fe:Invoice/ext:UBLExtensions/ext:UBLExtension[2]/ext:ExtensionContent/ds:Signature/ds:Object/xades:QualifyingProperties/xades:SignedProperties/xades:SignedSignatureProperties/xades:SigningTime"));
        parameters.put("pFirmaDigital", document.valueOf("/fe:Invoice/ext:UBLExtensions/ext:UBLExtension[2]/ext:ExtensionContent/ds:Signature/ds:SignatureValue/text()"));
        parameters.put("pCertificadoDigital", document.valueOf("/fe:Invoice/ext:UBLExtensions/ext:UBLExtension[2]/ext:ExtensionContent/ds:Signature/ds:KeyInfo/ds:X509Data/ds:X509Certificate"));

        return parameters;
    }

    private JRBeanCollectionDataSource getReportDataSource(Document document) {
        ArrayList<Conceptos> dataSource = new ArrayList();
        ArrayList<Concepto> conceptos = new ArrayList<Concepto>();

        Concepto concepto = new Concepto(
                null,
                null,
                new BigDecimal(document.valueOf("/fe:Invoice/fe:InvoiceLine/cbc:InvoicedQuantity")),
                null,
                "U",
                document.valueOf("/fe:Invoice/fe:InvoiceLine/fe:Item/cbc:Description"),
                new BigDecimal(document.valueOf("/fe:Invoice/fe:InvoiceLine/fe:Price/cbc:PriceAmount")),
                new BigDecimal(document.valueOf("/fe:Invoice/fe:InvoiceLine/cbc:LineExtensionAmount")),
                BigDecimal.ZERO,
                "",
                BigDecimal.ZERO
        );
        conceptos.add(concepto);

        Conceptos cotos = new Conceptos();
        cotos.setConceptos(conceptos);

        dataSource.add(cotos);
        return new JRBeanCollectionDataSource(dataSource, false);
    }
}
