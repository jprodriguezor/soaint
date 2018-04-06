package co.com.soaint.correspondencia.business.control;

import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.test.SignDocument_Service;
import lombok.extern.log4j.Log4j2;

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
public class FirmaDigital {

    public byte[] signPDF(byte[] pdfDocument){
        SignDocument_Service service = new SignDocument_Service();
        return service.getSignDocumentPort().signPDF(pdfDocument);
    }
}
