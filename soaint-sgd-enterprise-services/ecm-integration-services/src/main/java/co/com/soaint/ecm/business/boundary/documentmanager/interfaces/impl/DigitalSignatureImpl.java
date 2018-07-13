package co.com.soaint.ecm.business.boundary.documentmanager.interfaces.impl;

import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.DigitalSignature;
import co.com.soaint.test.SignDocument_Service;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service("digitalSignature")
public class DigitalSignatureImpl implements DigitalSignature {

    private final String digitalSignatureUrl;

    public DigitalSignatureImpl(@Value("${digital.signature.url}") String digitalSignatureUrl) {
        this.digitalSignatureUrl = digitalSignatureUrl;
    }

    @Override
    public byte[] signPDF(byte[] pdfDocument) {
        try {
            SignDocument_Service service = new SignDocument_Service();
            return service.getSignDocumentPort().signPDF(pdfDocument);
        } catch (Exception e) {
            log.error("No se pudo establecer la conexion con el servicio expuesto '{}'", digitalSignatureUrl);
            log.warn("No se le ha realizado la firma digital al documento");
            System.out.println(e.getMessage());
            return pdfDocument;
        }
    }
}
