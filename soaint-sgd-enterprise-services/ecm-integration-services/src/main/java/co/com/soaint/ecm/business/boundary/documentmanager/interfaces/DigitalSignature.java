package co.com.soaint.ecm.business.boundary.documentmanager.interfaces;

import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public interface DigitalSignature extends Serializable {

    byte[] signPDF(byte[] pdfDocument);
}
